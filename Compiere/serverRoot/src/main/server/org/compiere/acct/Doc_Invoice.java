/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Invoice (318)
 *  Document Types:     ARI, ARC, ARF, API, APC
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Invoice.java 8778 2010-05-19 19:00:10Z ragrawal $
 */
public class Doc_Invoice extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trx p_trx
	 */
	public Doc_Invoice(MAcctSchema[] ass, ResultSet rs, Trx trx)
	{
		super (ass, MInvoice.class, rs, null, trx);
	}	//	Doc_Invoice

	/** Contained Optional Tax Lines    */
	private DocTax[]        m_taxes = null;
	/** Currency Precision				*/
	private int				m_precision = -1;
	/** All lines are Service			*/
	private boolean			m_allLinesService = true;
	/** All lines are product item		*/
	private boolean			m_allLinesItem = true;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	@Override
	public String loadDocumentDetails()
	{
		MInvoice invoice = (MInvoice)getPO();
		setDateDoc(invoice.getDateInvoiced());
		setIsTaxIncluded(invoice.isTaxIncluded());
		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, invoice.getGrandTotal());
		setAmount(Doc.AMTTYPE_Net, invoice.getTotalLines());
		setAmount(Doc.AMTTYPE_Charge, invoice.getChargeAmt());
				
		//	Contained Objects
		m_taxes = loadTaxes();
		p_lines = loadLines(invoice);
		log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Taxes
	 *  @return DocTax Array
	 */
	private DocTax[] loadTaxes()
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();
		String sql = "SELECT it.C_Tax_ID, t.Name, t.Rate, it.TaxBaseAmt, it.TaxAmt, t.IsSalesTax "
			+ "FROM C_Tax t, C_InvoiceTax it "
			+ "WHERE t.C_Tax_ID=it.C_Tax_ID AND it.C_Invoice_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, getTrx());
			pstmt.setInt(1, get_ID());
			rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_Tax_ID = rs.getInt(1);
				String name = rs.getString(2);
				BigDecimal rate = rs.getBigDecimal(3);
				BigDecimal taxBaseAmt = rs.getBigDecimal(4);
				BigDecimal amount = rs.getBigDecimal(5);
				boolean salesTax = "Y".equals(rs.getString(6));
				//
				DocTax taxLine = new DocTax(C_Tax_ID, name, rate, 
					taxBaseAmt, amount, salesTax);
				log.fine(taxLine.toString());
				list.add(taxLine);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Return Array
		DocTax[] tl = new DocTax[list.size()];
		list.toArray(tl);
		return tl;
	}	//	loadTaxes

	/**
	 *	Load Invoice Line
	 *	@param invoice invoice
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines (MInvoice invoice)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		//
		MInvoiceLine[] lines = invoice.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			if (line.isDescription())
				continue;
			DocLine docLine = new DocLine(line, this);
			//	Qty
			BigDecimal Qty = line.getQtyInvoiced();
			boolean cm = getDocumentType().equals(MDocBaseType.DOCBASETYPE_ARCreditMemo) 
				|| getDocumentType().equals(MDocBaseType.DOCBASETYPE_APCreditMemo);
			docLine.setQty(cm ? Qty.negate() : Qty, invoice.isSOTrx());
			//
			BigDecimal LineNetAmt = line.getLineNetAmt();
			BigDecimal PriceList = line.getPriceList();
			int C_Tax_ID = docLine.getC_Tax_ID();
			//	Correct included Tax
			if (isTaxIncluded() && C_Tax_ID != 0)
			{
				MTax tax = MTax.get(getCtx(), C_Tax_ID);
				if (!tax.isZeroTax())
				{
					BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, getStdPercision());
					log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
					LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
					for (int t = 0; t < m_taxes.length; t++)
					{
						if (m_taxes[t].getC_Tax_ID() == C_Tax_ID)
						{
							m_taxes[t].addIncludedTax(LineNetAmtTax);
							break;
						}
					}
					BigDecimal PriceListTax = tax.calculateTax(PriceList, true, getStdPercision());
					PriceList = PriceList.subtract(PriceListTax);
				}
			}	//	correct included Tax
			
			docLine.setAmount (LineNetAmt, PriceList, Qty);	//	qty for discount calc
			if (docLine.isItem())
				m_allLinesService = false;
			else
				m_allLinesItem = false;
			//
			log.fine(docLine.toString());
			list.add(docLine);
		}
		
		//	Convert to Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);

		//	Included Tax - make sure that no difference
		if (isTaxIncluded())
		{
			for (int i = 0; i < m_taxes.length; i++)
			{
				if (m_taxes[i].isIncludedTaxDifference())
				{
					BigDecimal diff = m_taxes[i].getIncludedTaxDifference(); 
					for (int j = 0; j < dls.length; j++)
					{
						if (dls[j].getC_Tax_ID() == m_taxes[i].getC_Tax_ID())
						{
							dls[j].setLineNetAmtDifference(diff);
							break;
						}
					}	//	for all lines
				}	//	tax difference
			}	//	for all taxes
		}	//	Included Tax difference
		
		//	Return Array
		return dls;
	}	//	loadLines

	/**
	 * 	Get Currency Percision
	 *	@return precision
	 */
	private int getStdPercision()
	{
		if (m_precision == -1)
			m_precision = MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
		return m_precision;
	}	//	getPrecision

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	@Override
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Header Charge
		retValue = retValue.subtract(getAmount(Doc.AMTTYPE_Charge));
		sb.append("-").append(getAmount(Doc.AMTTYPE_Charge));
		//  - Tax
		for (int i = 0; i < m_taxes.length; i++)
		{
			retValue = retValue.subtract(m_taxes[i].getAmount());
			sb.append("-").append(m_taxes[i].getAmount());
		}
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.subtract(p_lines[i].getAmtSource());
			sb.append("-").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  ARI, ARC, ARF, API, APC.
	 *  <pre>
	 *  ARI, ARF
	 *      Receivables     DR
	 *      Charge                  CR
	 *      TaxDue                  CR
	 *      Revenue                 CR
	 *
	 *  ARC
	 *      Receivables             CR
	 *      Charge          DR
	 *      TaxDue          DR
	 *      Revenue         RR
	 *
	 *  API
	 *      Payables                CR
	 *      Charge          DR
	 *      TaxCredit       DR
	 *      Expense         DR
	 *
	 *  APC
	 *      Payables        DR
	 *      Charge                  CR
	 *      TaxCredit               CR
	 *      Expense                 CR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	@Override
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);

		//  Cash based accounting
		if (!as.isAccrual())
			return facts;

		//  ** ARI, ARF
		if (getDocumentType().equals(MDocBaseType.DOCBASETYPE_ARInvoice) 
			|| getDocumentType().equals(MDocBaseType.DOCBASETYPE_ARProFormaInvoice))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), null, amt);
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = Env.ZERO;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
					}
					fact.createLine (p_lines[i],
							p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
							getC_Currency_ID(), BigDecimal.ZERO, amt);
					fact.createLine (p_lines[i],
							p_lines[i].getAccount(ProductCost.ACCTTYPE_P_TDiscountGrant, as),
							getC_Currency_ID(), dAmt, BigDecimal.ZERO);
				}
				else
				{
					fact.createLine (p_lines[i],
							p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
							getC_Currency_ID(), dAmt, amt);
				}
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			
			//  Receivables     DR
			int receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_C_Receivable, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
		}
		//  ARC
		else if (getDocumentType().equals(MDocBaseType.DOCBASETYPE_ARCreditMemo))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Header Charge   DR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), amt, null);
			//  TaxDue          DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), amt, null);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue         CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
					}
					fact.createLine (p_lines[i],
							p_lines[i].getAccount (ProductCost.ACCTTYPE_P_Revenue, as),
							getC_Currency_ID(), amt, BigDecimal.ZERO);
					fact.createLine (p_lines[i],
							p_lines[i].getAccount (ProductCost.ACCTTYPE_P_TDiscountGrant, as),
							getC_Currency_ID(), BigDecimal.ZERO, dAmt);
				}
				else
				{
					fact.createLine (p_lines[i],
							p_lines[i].getAccount (ProductCost.ACCTTYPE_P_Revenue, as),
							getC_Currency_ID(), amt, dAmt);
				}
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			//  Receivables             CR
			int receivables_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(), null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(), null, serviceAmt);
		}
		
		//  ** API
		else if (getDocumentType().equals(MDocBaseType.DOCBASETYPE_APInvoice))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Charge          DR
			fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
			//  TaxCredit       DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), m_taxes[i].getAmount(), null);
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, true);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), amt, dAmt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource(), line.getQty(),
							line.getDescription(), getTrx());
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), null, serviceAmt);
			//
			updateProductPO(as);	//	Only API
		}
		//  APC
		else if (getDocumentType().equals(MDocBaseType.DOCBASETYPE_APCreditMemo))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			//  Charge                  CR
			fact.createLine (null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), null, getAmount(Doc.AMTTYPE_Charge));
			//  TaxCredit               CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), null, m_taxes[i].getAmount());
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, false);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), dAmt, amt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource().negate(), line.getQty(),
							line.getDescription(), getTrx());
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}
			//  Liability       DR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
		}
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		//
		facts.add(fact);
		return facts;
	}   //  createFact
	
	/**
	 * 	Create Fact Cash Based (i.e. only revenue/expense)
	 *	@param as accounting schema
	 *	@param fact fact to add lines to
	 *	@param multiplier source amount multiplier
	 *	@return accounted amount
	 */
	public BigDecimal createFactCash (MAcctSchema as, Fact fact, BigDecimal multiplier)
	{
		boolean creditMemo = getDocumentType().equals(MDocBaseType.DOCBASETYPE_ARCreditMemo)
			|| getDocumentType().equals(MDocBaseType.DOCBASETYPE_APCreditMemo);
		boolean payables = getDocumentType().equals(MDocBaseType.DOCBASETYPE_APInvoice)
			|| getDocumentType().equals(MDocBaseType.DOCBASETYPE_APCreditMemo);
		BigDecimal acctAmt = Env.ZERO;
		FactLine fl = null;
		//	Revenue/Cost
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			boolean landedCost = false;
			if  (payables)
				landedCost = landedCost(as, fact, line, false);
			if (landedCost && as.isExplicitCostAdjustment())
			{
				fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
					getC_Currency_ID(), null, line.getAmtSource());
				//
				fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
					getC_Currency_ID(), line.getAmtSource(), null);
				String desc = line.getDescription();
				if (desc == null)
					desc = "100%";
				else
					desc += " 100%";
				fl.setDescription(desc);
			}
			if (!landedCost)
			{
				MAccount acct = line.getAccount(
					payables ? ProductCost.ACCTTYPE_P_Expense : ProductCost.ACCTTYPE_P_Revenue, as);
				if (payables)
				{
					//	if Fixed Asset
					if (line.isItem())
						acct = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
				}
				BigDecimal amt = line.getAmtSource().multiply(multiplier);
				BigDecimal amt2 = null;
				if (creditMemo)
				{
					amt2 = amt;
					amt = null;
				}
				if (payables)	//	Vendor = DR
					fl = fact.createLine (line, acct,
						getC_Currency_ID(), amt, amt2);
				else			//	Customer = CR
					fl = fact.createLine (line, acct,
						getC_Currency_ID(), amt2, amt);
				if (fl != null)
					acctAmt = acctAmt.add(fl.getAcctBalance());
			}
		}
		//  Tax
		for (int i = 0; i < m_taxes.length; i++)
		{
			BigDecimal amt = m_taxes[i].getAmount();
			BigDecimal amt2 = null;
			if (creditMemo)
			{
				amt2 = amt;
				amt = null;
			}
			FactLine tl = null;
			if (payables)
				tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), amt, amt2);
			else
				tl = fact.createLine (null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
					getC_Currency_ID(), amt2, amt);
			if (tl != null)
				tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
		}
		//  Set Locations
		FactLine[] fLines = fact.getLines();
		for (int i = 0; i < fLines.length; i++)
		{
			if (fLines[i] != null)
			{
				if (payables)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
				else
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);    //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
		}
		return acctAmt;
	}	//	createFactCash
	
	
	/**
	 * 	Create Landed Cost accounting & Cost lines
	 *	@param as accounting schema
	 *	@param fact fact
	 *	@param line document line
	 *	@param dr DR entry (normal api)
	 *	@return true if landed costs were created
	 */
	private boolean landedCost (MAcctSchema as, Fact fact, DocLine line, boolean dr) 
	{
		int C_InvoiceLine_ID = line.get_ID();
		MLandedCostAllocation[] lcas = MLandedCostAllocation.getOfInvoiceLine(
			getCtx(), C_InvoiceLine_ID, getTrx());
		if (lcas.length == 0)
			return false;
		
		//	Delete Old
		String sql = "DELETE FROM M_CostDetail WHERE C_InvoiceLine_ID= ? " 
		              + " AND C_AcctSchema_ID = ?";
		Object[] params = new Object[]{C_InvoiceLine_ID,as.get_ID()};
		int no = DB.executeUpdate(getTrx(), sql,params);
		if (no != 0)
			log.config("CostDetail Deleted #" + no);

		//	Calculate Total Base
		double totalBase = 0;
		for (int i = 0; i < lcas.length; i++)
			totalBase += lcas[i].getBase().doubleValue();
		
		//	Create New
		MInvoiceLine il = new MInvoiceLine (getCtx(), C_InvoiceLine_ID, getTrx());
		for (int i = 0; i < lcas.length; i++)
		{
			MLandedCostAllocation lca = lcas[i];
			if (lca.getBase().signum() == 0)
				continue;
			double percent = totalBase / lca.getBase().doubleValue();
			String desc = il.getDescription();
			if (desc == null)
				desc = percent + "%";
			else
				desc += " - " + percent + "%";
			if (line.getDescription() != null)
				desc += " - " + line.getDescription(); 
			
			//	Accounting
			ProductCost pc = new ProductCost (Env.getCtx(), 
				lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(), getTrx());
			BigDecimal drAmt = null;
			BigDecimal crAmt = null;
			if (dr)
				drAmt = lca.getAmt();
			else
				crAmt = lca.getAmt();
			FactLine fl = fact.createLine (line, pc.getAccount(ProductCost.ACCTTYPE_P_CostAdjustment, as),
				getC_Currency_ID(), drAmt, crAmt);
			fl.setDescription(desc);
			
			//	Cost Detail - Convert to AcctCurrency
			BigDecimal allocationAmt =  lca.getAmt();
			if (getC_Currency_ID() != as.getC_Currency_ID())
				allocationAmt = MConversionRate.convert(getCtx(), allocationAmt, 
					getC_Currency_ID(), as.getC_Currency_ID(),
					getDateAcct(), getC_ConversionType_ID(), 
					getAD_Client_ID(), getAD_Org_ID());
			if (allocationAmt.scale() > as.getCostingPrecision())
				allocationAmt = allocationAmt.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
			if (!dr)
				allocationAmt = allocationAmt.negate();
			MCostDetail cd = new MCostDetail (as, lca.getAD_Org_ID(), 
				lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(),
				lca.getM_CostElement_ID(),
				allocationAmt, lca.getQty(),		//	Qty
				desc, getTrx());
			cd.setC_InvoiceLine_ID(C_InvoiceLine_ID);
			boolean ok = cd.save();
			if (ok && !cd.isProcessed())
			{
				MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
				if (client.isCostImmediate())
					cd.process();
			}
		}
		
		log.config("Created #" + lcas.length);
		return true;
	}	//	landedCosts

	/**
	 * 	Update ProductPO PriceLastInv
	 *	@param as accounting schema
	 */
	private void updateProductPO (MAcctSchema as)
	{
		MClientInfo ci = MClientInfo.get(getCtx(), as.getAD_Client_ID());
		if (ci.getC_AcctSchema1_ID() != as.getC_AcctSchema_ID())
			return;
		
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_PO po "
			+ "SET PriceLastInv = "
			+ "(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,po.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) "
			+ "FROM C_Invoice i, C_InvoiceLine il "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID");
			if (DB.isOracle()) //jz
			{
				sql.append(" AND ROWNUM=1 ");
			}
			else 
				sql.append(" AND il.C_InvoiceLine_ID IN (SELECT MIN(il1.C_InvoiceLine_ID) "
						+ "FROM C_Invoice i1, C_InvoiceLine il1 "
						+ "WHERE i1.C_Invoice_ID=il1.C_Invoice_ID"
						+ " AND po.M_Product_ID=il1.M_Product_ID AND po.C_BPartner_ID=i1.C_BPartner_ID")
						.append("  AND i1.C_Invoice_ID= ? ) ");
			sql.append("  AND i.C_Invoice_ID= ?) ")
			//	update
			.append("WHERE EXISTS (SELECT * "
			+ "FROM C_Invoice i, C_InvoiceLine il "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID"
			+ " AND i.C_Invoice_ID=?)");

		Object[] params;
		if (DB.isOracle())
			params = new Object[]{get_ID(),get_ID()};
		else
			params = new Object[]{get_ID(),get_ID(),get_ID()};
		int no = DB.executeUpdate(getTrx(), sql.toString(),params);
		log.fine("Updated=" + no);
	}	//	updateProductPO

}   //  Doc_Invoice
