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

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Cash Journal Line
 *
 *  @author Jorg Janke
 *  @version  $Id: DocLine_Cash.java 8987 2010-06-24 10:06:59Z ragrawal $
 */
public class DocLine_Cash extends DocLine
{
	/**
	 *  Constructor
	 *  @param line cash line
	 *  @param doc header
	 */
	public DocLine_Cash (MCashLine line, Doc_Cash doc)
	{
		super (line, doc);
		m_CashType = line.getCashType();
		m_C_BankAccount_ID = line.getC_BankAccount_ID();
		m_C_Invoice_ID = line.getC_Invoice_ID();
		m_C_Cash_ExpenseReceiptType_ID = line.getC_Cash_ExpenseReceiptType_ID();
		if (m_C_Invoice_ID != 0)
		{
			MInvoice invoice = MInvoice.get(line.getCtx(), m_C_Invoice_ID);
			setC_BPartner_ID(invoice.getC_BPartner_ID());
		}
		m_Amount = line.getAmount();
		m_DiscountAmt = line.getDiscountAmt();
		m_WriteOffAmt = line.getWriteOffAmt();
		setAmount(m_Amount);


	}   //  DocLine_Cash

	/** Cash Type               */
	private String  m_CashType = "";

	//  AD_Reference_ID=217
	/** Charge - C		*/
	public static final String  CASHTYPE_CHARGE = "C";
	/** Difference - D	*/
	public static final String  CASHTYPE_DIFFERENCE = "D";
	/** Expense - E		*/
	public static final String  CASHTYPE_EXPENSE = "E";
	/** Onvoice - I 	*/
	public static final String  CASHTYPE_INVOICE = "I";
	/** Receipt - R		*/
	public static final String  CASHTYPE_RECEIPT = "R";
	/** Transfer - T	*/
	public static final String  CASHTYPE_TRANSFER = "T";

	//  References
	private int     m_C_BankAccount_ID = 0;
	private int     m_C_Invoice_ID = 0;
	private int     m_C_Cash_ExpenseReceiptType_ID = 0;

	//  Amounts
	private BigDecimal      m_Amount = Env.ZERO;
	private BigDecimal      m_DiscountAmt = Env.ZERO;
	private BigDecimal      m_WriteOffAmt = Env.ZERO;


	/**
	 *  Get Cash Type
	 *  @return cash type
	 */
	public String getCashType()
	{
		return m_CashType;
	}   //  getCashType

	/**
	 *  Get Bank Account
	 *  @return Bank Account
	 */
	public int getC_BankAccount_ID()
	{
		return m_C_BankAccount_ID;
	}   //  getC_BankAccount_ID

	/**
	 *  Get Invoice
	 *  @return C_Invoice_ID
	 */
	public int getC_Invoice_ID()
	{
		return m_C_Invoice_ID;
	}   //  getC_Invoice_ID

	/**
	 *  Get Amount
	 *  @return Payment Amount
	 */
	public BigDecimal getAmount()
	{
		return m_Amount;
	}
	/**
	 *  Get Discount
	 *  @return Discount Amount
	 */
	public BigDecimal getDiscountAmt()
	{
		return m_DiscountAmt;
	}
	/**
	 *  Get WriteOff
	 *  @return Write-Off Amount
	 */
	public BigDecimal getWriteOffAmt()
	{
		return m_WriteOffAmt;
	}
	
	public int getC_Cash_ExpenseReceiptType_ID()
	{
		return m_C_Cash_ExpenseReceiptType_ID;
	}
	
	public MAccount getAccount(String AcctType, MAcctSchema as)
	{
		return MCashExpenseReceiptType.getAccount(getC_Cash_ExpenseReceiptType_ID(),AcctType,as);
	}

}   //  DocLine_Cash
