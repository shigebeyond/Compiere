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
package org.compiere.apps.form;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Payment Print & Export
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VPayPrint.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VPayPrint extends CPanel
	implements FormPanel, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Used Bank Account	*/
	private int				m_C_BankAccount_ID = -1;

	/** Payment Information */
	private MPaySelectionCheck[]     m_checks = null;
	/** Payment Batch		*/
	private MPaymentBatch	m_batch = null; 
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VPayPrint.class);
	
	//  Static Variables
	private CPanel centerPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private FlowLayout southLayout = new FlowLayout();
	private GridBagLayout centerLayout = new GridBagLayout();
	private JButton bPrint = ConfirmPanel.createPrintButton(true);
	private JButton bExport = ConfirmPanel.createExportButton(true);
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bProcess = ConfirmPanel.createProcessButton(Msg.getMsg(Env.getCtx(), "VPayPrintProcess"));
	private JCheckBox fShowPrinted = new JCheckBox();
	private CLabel lPaySelect = new CLabel();
	private CComboBox fPaySelect = new CComboBox();
	private CLabel lBank = new CLabel();
	private CLabel fBank = new CLabel();
	private CLabel lPaymentRule = new CLabel();
	private CComboBox fPaymentRule = new CComboBox();
	private CLabel lDocumentNo = new CLabel();
	private VNumber fDocumentNo = new VNumber();
	private CLabel lNoPayments = new CLabel();
	private CLabel fNoPayments = new CLabel();
	private CLabel lBalance = new CLabel();
	private VNumber fBalance = new VNumber();
	private CLabel lCurrency = new CLabel();
	private CLabel fCurrency = new CLabel();

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		southPanel.setLayout(southLayout);
		southLayout.setAlignment(FlowLayout.RIGHT);
		centerPanel.setLayout(centerLayout);
		//
		bPrint.addActionListener(this);
		bExport.addActionListener(this);
		bCancel.addActionListener(this);
		//
		bProcess.setText(Msg.getMsg(Env.getCtx(), "EFT"));
		bProcess.setEnabled(false);
		bProcess.addActionListener(this);
		//
		fShowPrinted.setText(Msg.getMsg(Env.getCtx(), "ShowPrintedPayments"));
		fShowPrinted.addActionListener(this);
		//
		lPaySelect.setText(Msg.translate(Env.getCtx(), "C_PaySelection_ID"));
		fPaySelect.addActionListener(this);
		//
		lBank.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		//
		lPaymentRule.setText(Msg.translate(Env.getCtx(), "PaymentRule"));
		fPaymentRule.addActionListener(this);
		//
		lDocumentNo.setText(Msg.getMsg(Env.getCtx(), "Check No"));
		fDocumentNo.setDisplayType(DisplayTypeConstants.Integer);
		lNoPayments.setText(Msg.getMsg(Env.getCtx(), "NoOfPayments"));
		fNoPayments.setText("0");
		lBalance.setText(Msg.translate(Env.getCtx(), "CurrentBalance"));
		fBalance.setReadWrite(false);
		fBalance.setDisplayType(DisplayTypeConstants.Amount);
		lCurrency.setText(Msg.translate(Env.getCtx(), "C_Currency_ID"));
		//
		southPanel.add(bCancel, null);
		southPanel.add(bExport, null);
		southPanel.add(bPrint, null);
		southPanel.add(bProcess, null);
		//
		centerPanel.add(fShowPrinted,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 5, 5), 0, 0));
		centerPanel.add(lPaySelect,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fPaySelect,    new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lBank,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fBank,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lPaymentRule,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fPaymentRule,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lDocumentNo,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fDocumentNo,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lNoPayments,   new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fNoPayments,    new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lBalance,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fBalance,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lCurrency,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 12, 5), 0, 0));
		centerPanel.add(fCurrency,   new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12), 0, 0));
	}   //  VPayPrint

	/**
	 *  Dynamic Init
	 */
	private void dynInit()
	{
		log.config("");
		loadPaySelections();
	}   //  dynInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *  Load Pay Selections
	 */
	private void loadPaySelections()
	{		
		fPaySelect.removeAllItems();
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		//  Load PaySelect
		StringBuffer sql = new StringBuffer("SELECT C_PaySelection_ID, Name || ' - ' || " + DB.TO_CHAR("TotalAmt", DisplayTypeConstants.Number, Env.getAD_Language(Env.getCtx())) + " FROM C_PaySelection "
			+ "WHERE AD_Client_ID=? AND Processed='Y' AND IsActive='Y'");
		if (!fShowPrinted.isSelected())
			sql.append(" AND EXISTS (SELECT * FROM C_PaySelectionCheck psc" +
				       " WHERE psc.C_PaySelection_ID = C_PaySelection.C_PaySelection_ID	AND psc.IsPrinted = 'N') ");
		sql.append("ORDER BY PayDate DESC, C_PaySelection_ID DESC");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				fPaySelect.addItem(pp);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (fPaySelect.getItemCount() == 0)
			ADialog.info(m_WindowNo, this, "VPayPrintNoRecords");
	}
	
	/**
	 * 	Set Payment Selection
	 *	@param C_PaySelection_ID id
	 */
	public void setPaySelection (int C_PaySelection_ID)
	{
		if (C_PaySelection_ID == 0)
			return;
		//
		for (int i = 0; i < fPaySelect.getItemCount(); i++)
		{
			KeyNamePair pp = (KeyNamePair)fPaySelect.getItemAt(i);
			if (pp.getKey() == C_PaySelection_ID)
			{
				fPaySelect.setSelectedIndex(i);
				return;
			}
		}
	}	//	setsetPaySelection

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(fShowPrinted))
			loadPaySelections();
		else if (e.getSource() == fPaySelect)
			loadPaySelectInfo();
		else if (e.getSource() == fPaymentRule)
			loadPaymentRuleInfo();
		//
		else if (e.getSource() == bCancel)
			dispose();
		else if (e.getSource() == bExport)
			cmd_export();
		else if (e.getSource() == bProcess)
			cmd_EFT();
		else if (e.getSource() == bPrint)
			cmd_print();
	}   //  actionPerformed

	/**
	 *  PaySelect changed - load Bank
	 */
	private void loadPaySelectInfo()
	{
		log.info( "VPayPrint.loadPaySelectInfo");
		if (fPaySelect.getSelectedIndex() == -1)
			return;

		//  load Banks from PaySelectLine
		int C_PaySelection_ID = ((KeyNamePair)fPaySelect.getSelectedItem()).getKey();
		m_C_BankAccount_ID = -1;
		String sql = "SELECT ps.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo,"	//	1..2
			+ " c.ISO_Code, CurrentBalance "										//	3..4
			+ "FROM C_PaySelection ps"
			+ " INNER JOIN C_BankAccount ba ON (ps.C_BankAccount_ID=ba.C_BankAccount_ID)"
			+ " INNER JOIN C_Bank b ON (ba.C_Bank_ID=b.C_Bank_ID)"
			+ " INNER JOIN C_Currency c ON (ba.C_Currency_ID=c.C_Currency_ID) "
			+ "WHERE ps.C_PaySelection_ID=? AND ps.Processed='Y' AND ba.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_PaySelection_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_C_BankAccount_ID = rs.getInt(1);
				fBank.setText(rs.getString(2));
				fCurrency.setText(rs.getString(3));
				fBalance.setValue(rs.getBigDecimal(4));
			}
			else
			{
				m_C_BankAccount_ID = -1;
				fBank.setText("");
				fCurrency.setText("");
				fBalance.setValue(Env.ZERO);
				log.log(Level.SEVERE, "No active BankAccount for C_PaySelection_ID=" + C_PaySelection_ID);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		loadPaymentRule();
	}   //  loadPaySelectInfo

	/**
	 *  Bank changed - load PaymentRule
	 */
	private void loadPaymentRule()
	{
		log.info("");
		if (m_C_BankAccount_ID == -1)
			return;

		// load PaymentRule for Bank
		int C_PaySelection_ID = ((KeyNamePair)fPaySelect.getSelectedItem()).getKey();
		fPaymentRule.removeAllItems();
		int AD_Reference_ID = 195; 
		Language language = Language.getLanguage(Env.getAD_Language(Env.getCtx()));
		MLookupInfo info = MLookupFactory.getLookup_List(language, AD_Reference_ID);
		String infoQuery = info.getQuery();
		String sql = infoQuery.substring(0, infoQuery.indexOf(" ORDER BY"))
			+ " AND " + info.KeyColumn
			+ " IN (SELECT PaymentRule FROM C_PaySelectionCheck WHERE C_PaySelection_ID=?) "
			+ infoQuery.substring(infoQuery.indexOf(" ORDER BY"));
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_PaySelection_ID);
			rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				ValueNamePair pp = new ValueNamePair(rs.getString(2), rs.getString(3));
				fPaymentRule.addItem(pp);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (fPaymentRule.getItemCount() == 0)
			log.config("PaySel=" + C_PaySelection_ID + ", BAcct=" + m_C_BankAccount_ID + " - " + sql);
		loadPaymentRuleInfo();
	}   //  loadPaymentRule

	/**
	 *  PaymentRule changed - load DocumentNo, NoPayments,
	 *  enable/disable EFT, Print
	 */
	private void loadPaymentRuleInfo()
	{
		ValueNamePair pp = (ValueNamePair)fPaymentRule.getSelectedItem();
		if (pp == null)
			return;
		String PaymentRule = pp.getValue();

		log.info("PaymentRule=" + PaymentRule);
		fNoPayments.setText(" ");

		int C_PaySelection_ID = ((KeyNamePair)fPaySelect.getSelectedItem()).getKey();
		String sql = "SELECT COUNT(*) "
			+ "FROM C_PaySelectionCheck "
			+ "WHERE C_PaySelection_ID=?";
		int count = QueryUtil.getSQLValue((Trx) null, sql, C_PaySelection_ID);
		fNoPayments.setText(String.valueOf(count));
		bProcess.setEnabled(PaymentRule.equals("T"));

		//  DocumentNo
		sql = "SELECT CurrentNext "
			+ "FROM C_BankAccountDoc "
			+ "WHERE C_BankAccount_ID=? AND PaymentRule=? AND IsActive='Y'";
		int next = QueryUtil.getSQLValue((Trx) null, sql, m_C_BankAccount_ID,PaymentRule);
		if (next != 0)
			fDocumentNo.setValue(next);
		else
		{
				log.log(Level.SEVERE, "VPayPrint.loadPaymentRuleInfo - No active BankAccountDoc for C_BankAccount_ID="
					+ m_C_BankAccount_ID + " AND PaymentRule=" + PaymentRule);
				ADialog.error(m_WindowNo, this, "VPayPrintNoDoc");
		}
	}   //  loadPaymentRuleInfo


	/**************************************************************************
	 *  Export payments to file
	 */
	private void cmd_export()
	{
		String PaymentRule = ((ValueNamePair)fPaymentRule.getSelectedItem()).getValue();
		log.info(PaymentRule);
		if (!getChecks(PaymentRule))
			return;

		//  Get File Info
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(Msg.getMsg(Env.getCtx(), "Export"));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setSelectedFile(new java.io.File("paymentExport.txt"));
		if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		
		Map<MPaySelectionCheck,String> oldCheckValues = new HashMap<MPaySelectionCheck,String>();
		//  Create File
		for (MPaySelectionCheck check : m_checks) {
			oldCheckValues.put(check,(String)check.get_ValueOld("DocumentNo"));
		}

		int no = MPaySelectionCheck.exportToFile(m_checks, fc.getSelectedFile());
		ADialog.info(m_WindowNo, this, "Saved",
			fc.getSelectedFile().getAbsolutePath() + "\n"
			+ Msg.getMsg(Env.getCtx(), "NoOfLines") + "=" + no);

		if (ADialog.ask(m_WindowNo, this, "VPayPrintSuccess?"))
		{
		//	int lastDocumentNo = 
			MPaySelectionCheck.confirmPrint (m_checks, m_batch);
			//	document No not updated
		}
		else
		{
			for (MPaySelectionCheck check : m_checks) 
			{
				check.setCheckNo(oldCheckValues.get(check));
				check.save();
			}
		}
		dispose();
	}   //  cmd_export

	/**
	 *  Create EFT payment
	 */
	private void cmd_EFT()
	{
		String PaymentRule = ((ValueNamePair)fPaymentRule.getSelectedItem()).getValue();
		log.info(PaymentRule);
		if (!getChecks(PaymentRule))
			return;
		dispose();
	}   //  cmd_EFT

	/**
	 *  Print Checks and/or Remittance
	 */
	private void cmd_print()
	{
		String PaymentRule = ((ValueNamePair)fPaymentRule.getSelectedItem()).getValue();
		log.info(PaymentRule);
		if (!getChecks(PaymentRule))
			return;

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		boolean somethingPrinted = false;
		boolean directPrint = !Ini.isPropertyBool(Ini.P_PRINTPREVIEW);
		Map<MPaySelectionCheck,String> oldCheckValues = new HashMap<MPaySelectionCheck,String>();
		//	for all checks
		for (MPaySelectionCheck check : m_checks) {
			//	ReportCtrl will check BankAccountDoc for PrintFormat
			oldCheckValues.put(check,(String.valueOf(check.get_ValueOld("CheckNo"))));
			check.save();
			ReportEngine re = ReportCtl.startDocumentPrint( Env.getCtx(), ReportEngine.CHECK, check.get_ID(), directPrint );
			if (re == null) // Payment
				ADialog.error(0, null, CLogger.retrieveError().getName() ); 

			if( !directPrint && re != null )
				new Viewer( re );
			if (!somethingPrinted && re != null)
				somethingPrinted = true;
		}

		//	Confirm Print and Update BankAccountDoc
		if (somethingPrinted && ADialog.ask(m_WindowNo, this, "VPayPrintSuccess?"))
		{
			int lastDocumentNo = MPaySelectionCheck.confirmPrint (m_checks, m_batch);
			if (lastDocumentNo != 0)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("UPDATE C_BankAccountDoc SET CurrentNext= ? ")
					.append(" WHERE C_BankAccount_ID= ? ")
					.append(" AND PaymentRule= ? ");
				DB.executeUpdate((Trx) null, sb.toString(),++lastDocumentNo,m_C_BankAccount_ID,PaymentRule);
			}
		}	//	confirm
		else
		{
			for (MPaySelectionCheck check : m_checks) 
			{
				check.setCheckNo(oldCheckValues.get(check));
				check.save();
			}

		}

		if (ADialog.ask(m_WindowNo, this, "VPayPrintPrintRemittance"))
		{
			for (MPaySelectionCheck check : m_checks) {
				ReportEngine re = ReportCtl.startDocumentPrint( Env.getCtx(), ReportEngine.REMITTANCE, check.get_ID(), directPrint );
				if( !directPrint )
					new Viewer( re );
			}
		}	//	remittance

		this.setCursor(Cursor.getDefaultCursor());
		dispose();
	}   //  cmd_print

	
	/**************************************************************************
	 *  Get Checks
	 *  @param PaymentRule Payment Rule
	 *  @return true if payments were created
	 */
	private boolean getChecks(String PaymentRule)
	{
		//  do we have values
		if (fPaySelect.getSelectedIndex() == -1 || m_C_BankAccount_ID == -1
			|| fPaymentRule.getSelectedIndex() == -1 || fDocumentNo.getValue() == null)
		{
			ADialog.error(m_WindowNo, this, "VPayPrintNoRecords",
				"(" + Msg.translate(Env.getCtx(), "C_PaySelectionLine_ID") + "=0)");
			return false;
		}

		//  get data
		int C_PaySelection_ID = ((KeyNamePair)fPaySelect.getSelectedItem()).getKey();
		int startDocumentNo = ((Number)fDocumentNo.getValue()).intValue();

		log.config("C_PaySelection_ID=" + C_PaySelection_ID + ", PaymentRule=" +  PaymentRule + ", DocumentNo=" + startDocumentNo);
		//
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		//	get Selections
		m_checks = MPaySelectionCheck.get(C_PaySelection_ID, PaymentRule, startDocumentNo, null);

		this.setCursor(Cursor.getDefaultCursor());
		//
		if (m_checks == null || m_checks.length == 0)
		{
			ADialog.error(m_WindowNo, this, "VPayPrintNoRecords",
				"(" + Msg.translate(Env.getCtx(), "C_PaySelectionLine_ID") + " #0");
			return false;
		}
		m_batch = MPaymentBatch.getForPaySelection (Env.getCtx(), C_PaySelection_ID, null);
		return true;
	}   //  getChecks

}   //  PayPrint
