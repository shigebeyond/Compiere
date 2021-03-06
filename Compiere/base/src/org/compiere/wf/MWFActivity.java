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
package org.compiere.wf;

import java.io.*;
import java.math.*;
import java.rmi.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.framework.*;
import org.compiere.interfaces.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *	Workflow Activity Model.
 *	Controlled by WF Process:
 *		set Node - startWork
 *
 *  @author Jorg Janke
 *  @version $Id: MWFActivity.java,v 1.4 2006/07/30 00:51:05 jjanke Exp $
 */
public class MWFActivity extends X_AD_WF_Activity
{
    /** Logger for class MWFActivity */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWFActivity.class);
	/**  */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Activities for table/record
	 *	@param ctx context
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param activeOnly if true only not processed records are returned
	 *	@return activity
	 */
	public static MWFActivity[] get (Ctx ctx, int AD_Table_ID, int Record_ID, boolean activeOnly)
	{
		ArrayList<MWFActivity> list = new ArrayList<MWFActivity>();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_WF_Activity WHERE AD_Table_ID=? AND Record_ID=?";
		if (activeOnly)
			sql += " AND Processed<>'Y'";
		sql += " ORDER BY AD_WF_Activity_ID";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Table_ID);
			pstmt.setInt (2, Record_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MWFActivity (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e){}
		}
		MWFActivity[] retValue = new MWFActivity[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Active Info
	 * 	@param ctx context
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@return activity summary
	 */
	public static String getActiveInfo (Ctx ctx, int AD_Table_ID, int Record_ID)
	{
		MWFActivity[] acts = get (ctx, AD_Table_ID, Record_ID, true);
		if ((acts == null) || (acts.length == 0))
			return null;
		//
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < acts.length; i++)
		{
			if (i > 0)
				sb.append("\n");
			MWFActivity activity = acts[i];
			sb.append(activity.toStringX());
		}
		return sb.toString();
	}	//	getActivityInfo

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWFActivity.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WF_Activity_ID id
	 *	@param trx transaction
	 */
	public MWFActivity (Ctx ctx, int AD_WF_Activity_ID, Trx trx)
	{
		super (ctx, AD_WF_Activity_ID, trx);
		if (AD_WF_Activity_ID == 0)
			throw new IllegalArgumentException ("Cannot create new WF Activity directly");
		m_state = new StateEngine (getWFState());
	}	//	MWFActivity

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWFActivity (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
		m_state = new StateEngine (getWFState());
	}	//	MWFActivity

	/**
	 * 	Parent Constructor
	 *	@param process process
	 *	@param AD_WF_Node_ID start node
	 */
	public MWFActivity (MWFProcess process, int AD_WF_Node_ID)
	{
		super (process.getCtx(), 0, process.get_Trx());
		setAD_WF_Process_ID (process.getAD_WF_Process_ID());
		setPriority(process.getPriority());
		//	Document Link
		setAD_Table_ID(process.getAD_Table_ID());
		setRecord_ID(process.getRecord_ID());
		m_po = process.getPO();		//	May need to reload
		//	Status
		super.setWFState(WFSTATE_NotStarted);
		m_state = new StateEngine (getWFState());
		setProcessed (false);
		//	Set Workflow Node
		setAD_Workflow_ID (process.getAD_Workflow_ID());
		setAD_WF_Node_ID (AD_WF_Node_ID);
		//	Node Priority & End Duration
		MWFNode node = MWFNode.get(getCtx(), AD_WF_Node_ID);
		int priority = node.getPriority();
		if ((priority != 0) && (priority != getPriority()))
			setPriority (priority);
		long limitMS = node.getDurationLimitMS();
		if (limitMS != 0)
			setEndWaitTime(new Timestamp(limitMS + System.currentTimeMillis()));
		//	Responsible
		setResponsible(process);
		save();
		//
		m_audit = new MWFEventAudit(this);
		m_audit.save();
		//
		m_process = process;
	}	//	MWFActivity

	/**	State Machine				*/
	private StateEngine			m_state = null;
	/**	Workflow Node				*/
	protected MWFNode				m_node = null;
	/** Transaction					*/
	private Trx 				m_trx = null;
	/**	Audit						*/
	private MWFEventAudit		m_audit = null;
	/** Persistent Object			*/
	private PO					m_po = null;
	/** Document Status				*/
	private String				m_docStatus = null;
	/**	New Value to save in audit	*/
	private String				m_newValue = null;
	/** Process						*/
	private MWFProcess 			m_process = null;
	/** Post Immediate Candidate	*/
	private DocAction			m_postImmediate = null;
	/** List of email recipients	*/
	private ArrayList<String> 	m_emails = new ArrayList<String>();

	/**************************************************************************
	 * 	Get State
	 *	@return state
	 */
	public StateEngine getState()
	{
		return m_state;
	}	//	getState

	/**
	 * 	Set Activity State
	 *	@param WFState
	 */
	@Override
	public void setWFState (String WFState)
	{
		if (m_state == null)
			m_state = new StateEngine (getWFState());
		if (m_state.isClosed())
			return;
		if (getWFState().equals(WFState))
			return;
		//
		if (m_state.isValidNewState(WFState))
		{
			String oldState = getWFState();
			log.fine(oldState + "->"+ WFState + ", Msg=" + getTextMsg());
			super.setWFState (WFState);
			m_state = new StateEngine (getWFState());
			save();			//	closed in MWFProcess.checkActivities()
			updateEventAudit();

			//	Inform Process
			if (m_process == null)
				m_process = new MWFProcess (getCtx(), getAD_WF_Process_ID(), get_Trx());
			
			m_process.checkActivities();
		}
		else
		{
			String msg = "Ignored Invalid Transformation - New="
				+ WFState + ", Current=" + getWFState();
			log.log(Level.SEVERE, msg);
			Trace.printStack();
			setTextMsg("Set WFState - " + msg);
			save();
		}
	}	//	setWFState

	/**
	 * 	Is Activity closed
	 * 	@return true if closed
	 */
	public boolean isClosed()
	{
		return m_state.isClosed();
	}	//	isClosed

	/**************************************************************************
	 * 	Update Event Audit
	 */
	private void updateEventAudit()
	{
		//	log.fine("");
		getEventAudit();
		m_audit.setTextMsg(getTextMsg());
		m_audit.setWFState(getWFState());
		if (m_newValue != null)
			m_audit.setNewValue(m_newValue);
		if (m_state.isClosed())
		{
			m_audit.setEventType(X_AD_WF_EventAudit.EVENTTYPE_ProcessCompleted);
			long ms = System.currentTimeMillis() - m_audit.getCreated().getTime();
			m_audit.setElapsedTimeMS(new BigDecimal(ms));
		}
		else
			m_audit.setEventType(X_AD_WF_EventAudit.EVENTTYPE_StateChanged);
		m_audit.save();
	}	//	updateEventAudit

	/**
	 * 	Get/Create Event Audit
	 * 	@return event
	 */
	public MWFEventAudit getEventAudit()
	{
		if (m_audit != null)
			return m_audit;
		MWFEventAudit[] events = MWFEventAudit.get(getCtx(), getAD_WF_Process_ID(), getAD_WF_Node_ID());
		if ((events == null) || (events.length == 0))
			m_audit = new MWFEventAudit(this);
		else
			m_audit = events[events.length-1];		//	last event
		return m_audit;
	}	//	getEventAudit


	/**************************************************************************
	 * 	Get Persistent Object in Transaction
	 * 	@param trx transaction
	 *	@return po
	 */
	public PO getPO (Trx trx)
	{
		if (m_po != null)
		{
			if ((m_po.get_Trx() == null) && (trx == null))
				return m_po;
			if ((m_po.get_Trx() != null) && (trx != null)
					&& m_po.get_Trx().equals(trx))
				return m_po;
			log.fine("Reloading - PO=" + m_po.get_Trx() + " -> " + trx);
			m_po.load(trx);		//	reload
			return m_po;
		}

		MTable table = MTable.get (getCtx(), getAD_Table_ID());
		m_po = table.getPO(getCtx(), getRecord_ID(), trx);
		return m_po;
	}	//	getPO

	/**
	 *	Get Persistent Object in Transaction
	 * 	@param p_trx transaction
	 *	@return po
	 */
	/*
	public PO getPO (Trx p_trx)
	{
		if (p_trx == null)
			return getPO((String)null);
		return getPO (p_trx.getTrxName());
	}	//	getPO
	 */

	/**
	 * 	Get Persistent Object.
	 * 	Current p_trx
	 *	@return po
	 */
	public PO getPO()
	{
		return getPO(m_trx);
	}	//	getPO

	/**
	 * 	Get Document Summary
	 *	@return PO Summary
	 */
	public String getSummary()
	{
		PO po = getPO();
		if (po == null)
			return null;
		StringBuffer sb = new StringBuffer();
		String[] keyColumns = po.get_Info().getKeyColumns();
		if ((keyColumns != null) && (keyColumns.length > 0))
			sb.append(Msg.getElement(getCtx(), keyColumns[0])).append(" ");
		int index = po.get_ColumnIndex("DocumentNo");
		if (index != -1)
			sb.append(po.get_Value(index)).append(": ");
		index = po.get_ColumnIndex("SalesRep_ID");
		Integer sr = null;
		if (index != -1)
			sr = (Integer)po.get_Value(index);
		else
		{
			index = po.get_ColumnIndex("AD_User_ID");
			if (index != -1)
				sr = (Integer)po.get_Value(index);
		}
		if (sr != null)
		{
			MUser user = MUser.get(getCtx(), sr.intValue());
			if (user != null)
				sb.append(user.getName()).append(" ");
		}
		//
		index = po.get_ColumnIndex("C_BPartner_ID");
		if (index != -1)
		{
			Integer bp = (Integer)po.get_Value(index);
			if (bp != null)
			{
				MBPartner partner = MBPartner.get(getCtx(), bp.intValue());
				if (partner != null)
					sb.append(partner.getName()).append(" ");
			}
		}
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get PO AD_Client_ID
	 * 	@param trx p_trx
	 *	@return client of PO or -1
	 */
	public int getPO_AD_Client_ID (Trx trx)
	{
		if ((m_po == null) && (trx == null))
			getPO(m_trx);
		if (m_po == null)
			getPO(trx);
		if (m_po != null)
			return m_po.getAD_Client_ID();
		return -1;
	}	//	getPO_AD_Client_ID

	/**
	 * 	Get Attribute Value (based on Node) of PO
	 *	@return Attribute Value or null
	 */
	public Object getAttributeValue()
	{
		MWFNode node = getNode();
		if (node == null)
			return null;
		int AD_Column_ID = node.getAD_Column_ID();
		if (AD_Column_ID == 0)
			return null;
		PO po = getPO();
		if (po.get_ID() == 0)
			return null;
		return po.get_ValueOfColumn(AD_Column_ID);
	}	//	getAttributeValue

	/**
	 * 	Is SO Trx
	 *	@return SO Trx or of not found true
	 */
	public boolean isSOTrx()
	{
		PO po = getPO();
		if (po.get_ID() == 0)
			return true;
		//	Is there a Column?
		int index = po.get_ColumnIndex("IsSOTrx");
		if (index < 0)
		{
			if (po.get_TableName().startsWith("M_"))
				return false;
			return true;
		}
		//	we have a column
		try
		{
			Boolean IsSOTrx = (Boolean)po.get_Value(index);
			return IsSOTrx.booleanValue();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return true;
	}	//	isSOTrx


	/**************************************************************************
	 * 	Set AD_WF_Node_ID.
	 * 	(Re)Set to Not Started
	 *	@param AD_WF_Node_ID now node
	 */
	@Override
	public void setAD_WF_Node_ID (int AD_WF_Node_ID)
	{
		if (AD_WF_Node_ID == 0)
			throw new IllegalArgumentException("Workflow Node is not defined");
		super.setAD_WF_Node_ID (AD_WF_Node_ID);
		//
		if (!WFSTATE_NotStarted.equals(getWFState()))
		{
			super.setWFState(WFSTATE_NotStarted);
			m_state = new StateEngine (getWFState());
		}
		if (isProcessed())
			setProcessed (false);
	}	//	setAD_WF_Node_ID

	/**
	 * 	Get WF Node
	 *	@return node
	 */
	public MWFNode getNode()
	{
		if (m_node == null)
			m_node = MWFNode.get (getCtx(), getAD_WF_Node_ID());
		return m_node;
	}	//	getNode

	/**
	 * 	Get WF Node Name
	 *	@return translated node name
	 */
	public String getNodeName()
	{
		return getNode().getName(true);
	}	//	getNodeName

	/**
	 * 	Get Node Description
	 *	@return translated node description
	 */
	public String getNodeDescription()
	{
		return getNode().getDescription(true);
	}	//	getNodeDescription

	/**
	 * 	Get Node Help
	 *	@return translated node help
	 */
	public String getNodeHelp()
	{
		return getNode().getHelp(true);
	}	//	getNodeHelp


	/**
	 * 	Is this an user Approval step?
	 *	@return true if User Approval
	 */
	public boolean isUserApproval()
	{
		return getNode().isUserApproval();
	}	//	isNodeApproval

	/**
	 * 	Is this a Manual user step?
	 *	@return true if Window/Form/..
	 */
	public boolean isUserManual()
	{
		return getNode().isUserManual();
	}	//	isUserManual

	/**
	 * 	Is this a user choice step?
	 *	@return true if User Choice
	 */
	public boolean isUserChoice()
	{
		return getNode().isUserChoice();
	}	//	isUserChoice


	/**
	 * 	Set Text Msg (add to existing)
	 *	@param TextMsg
	 */
	@Override
	public void setTextMsg (String TextMsg)
	{
		if ((TextMsg == null) || (TextMsg.length() == 0))
			return;
		String oldText = getTextMsg();
		if ((oldText == null) || (oldText.length() == 0))
			super.setTextMsg (Util.trimSize(TextMsg,1000));
		else if ((TextMsg != null) && (TextMsg.length() > 0))
			super.setTextMsg (Util.trimSize(oldText + "\n - " + TextMsg,1000));
	}	//	setTextMsg

	/**
	 * 	Add to Text Msg
	 *	@param obj some object
	 */
	public void addTextMsg (Object obj)
	{
		if (obj == null)
			return;
		//
		StringBuffer TextMsg = new StringBuffer (obj.toString());
		if (obj instanceof Exception)
		{
			Exception ex = (Exception)obj;
			while (ex != null)
			{
				StackTraceElement[] st = ex.getStackTrace();
				for (int i = 0; i < st.length; i++)
				{
					StackTraceElement ste = st[i];
					if ((i == 0) || ste.getClassName().startsWith("org.compiere"))
						TextMsg.append(" (").append(i).append("): ")
						.append(ste.toString())
						.append("\n");
				}
				if (ex.getCause() instanceof Exception)
					ex = (Exception)ex.getCause();
				else
					ex = null;
			}
		}
		//
		String oldText = getTextMsg();
		if ((oldText == null) || (oldText.length() == 0))
			super.setTextMsg(Util.trimSize(TextMsg.toString(),1000));
		else if ((TextMsg != null) && (TextMsg.length() > 0))
			super.setTextMsg(Util.trimSize(oldText + "\n - " + TextMsg.toString(),1000));
	}	//	setTextMsg

	/**
	 * 	Get WF State text
	 *	@return state text
	 */
	public String getWFStateText ()
	{
		return MRefList.getListName(getCtx(), X_Ref_WF_Instance_State.AD_Reference_ID, getWFState());
	}	//	getWFStateText

	/**
	 * 	Set Responsible and User from Process / Node
	 *	@param process process
	 */
	private void setResponsible (MWFProcess process)
	{
		//	Responsible
		int AD_WF_Responsible_ID = getNode().getAD_WF_Responsible_ID();
		if (AD_WF_Responsible_ID == 0)	//	not defined on Node Level
			AD_WF_Responsible_ID = process.getAD_WF_Responsible_ID();
		setAD_WF_Responsible_ID (AD_WF_Responsible_ID);
		MWFResponsible resp = getResponsible();


		//	User - Directly responsible
		int AD_User_ID = resp.getAD_User_ID();
		//	Invoker - get Sales Rep or last updater of document
		if ((AD_User_ID == 0) && resp.isInvoker())
			AD_User_ID = process.getAD_User_ID();
		else if (resp.isInvokerOrganization()) {
			MOrgInfo org = MOrgInfo.get(getCtx(), m_po.getAD_Org_ID(), null);
			if (org.getSupervisor_ID() == 0)
				log.fine("No Supervisor for AD_Org_ID=" + m_po.getAD_Org_ID());
			else 
				AD_User_ID = org.getSupervisor_ID();
		}
		else if (resp.isRole()) {
			MUser[] users = MUser.getWithRole(resp.getRole());
			for (MUser user : users) {
				if (user.isActive()) {
					AD_User_ID = user.getAD_User_ID();
					break;
				}
			}
		}

		//
		setAD_User_ID(AD_User_ID);
	}	//	setResponsible

	/**
	 * 	Get Responsible
	 *	@return responsible
	 */
	public MWFResponsible getResponsible()
	{
		MWFResponsible resp = MWFResponsible.get(getCtx(), getAD_WF_Responsible_ID());
		return resp;
	}	//	isInvoker

	/**
	 * 	Is Invoker (no user & no role)
	 *	@return true if invoker
	 */
	public boolean isInvoker()
	{
		return getResponsible().isInvoker();
	}	//	isInvoker

	/**
	 * 	Get Approval User.
	 * 	If the returned user is the same, the document is approved.
	 *	@param AD_User_ID starting User
	 *	@param C_Currency_ID currency
	 *	@param amount amount
	 *	@param AD_Org_ID document organization
	 *	@param ownDocument the document is owned by AD_User_ID
	 *	@return AD_User_ID - if -1 no Approver
	 */
	public int getApprovalUser (int AD_User_ID,
			int C_Currency_ID, BigDecimal amount,
			int AD_Org_ID, boolean ownDocument)
	{
		//	Starting user
		MUser user = MUser.get(getCtx(), AD_User_ID);
		log.info("For User=" + user
				+ ", Amt=" + amount
				+ ", Own=" + ownDocument);

		MUser oldUser = null;
		while (user != null)
		{
			if (user.equals(oldUser))
			{
				log.info("Loop - " + user.getName());
				return -1;
			}
			oldUser = user;
			log.fine("User=" + user.getName());
			//	Get Roles of User
			MRole[] roles = user.getRoles(AD_Org_ID);
			for (MRole role : roles) {
				if (ownDocument && !role.isCanApproveOwnDoc())
					continue;	//	find a role with allows them to approve own
				BigDecimal roleAmt = role.getAmtApproval();
				if ((roleAmt == null) || (roleAmt.signum() == 0))
					continue;
				if ((C_Currency_ID != role.getC_Currency_ID())
						&& (role.getC_Currency_ID() != 0))			//	No currency = amt only
				{
					roleAmt =  MConversionRate.convert(getCtx(),//	today & default rate
							roleAmt, role.getC_Currency_ID(),
							C_Currency_ID, getAD_Client_ID(), AD_Org_ID);
					if ((roleAmt == null) || (roleAmt.signum() == 0))
						continue;
				}
				boolean approved = amount.compareTo(roleAmt) <= 0;
				log.fine("Approved=" + approved
						+ " - User=" + user.getName() + ", Role=" + role.getName()
						+ ", ApprovalAmt=" + roleAmt);
				if (approved)
					return user.getAD_User_ID();
			}

			//	**** Find next User
			//	Get Supervisor
			if (user.getSupervisor_ID() != 0)
			{
				user = MUser.get(getCtx(), user.getSupervisor_ID());
				log.fine("Supervisor: " + user.getName());
			}
			else
			{
				log.fine("No Supervisor");
				MOrg org = MOrg.get (getCtx(), AD_Org_ID);
				MOrgInfo orgInfo = org.getInfo();
				//	Get Org Supervisor
				if (orgInfo.getSupervisor_ID() != 0)
				{
					user = MUser.get(getCtx(), orgInfo.getSupervisor_ID());
					log.fine("Org=" + org.getName() + ",Supervisor: " + user.getName());
				}
				else
				{
					log.fine("No Org Supervisor");
					//	Get Parent Org Supervisor
					if (orgInfo.getParent_Org_ID() != 0)
					{
						org = MOrg.get (getCtx(), orgInfo.getParent_Org_ID());
						orgInfo = org.getInfo();
						if (orgInfo.getSupervisor_ID() != 0)
						{
							user = MUser.get(getCtx(), orgInfo.getSupervisor_ID());
							log.fine("Parent Org Supervisor: " + user.getName());
						}
					}
				}
			}	//	No Supervisor

		}	//	while there is a user to approve

		log.fine("No user found");
		return -1;
	}	//	getApproval


	/**************************************************************************
	 * 	Execute Work.
	 * 	Called from MWFProcess.startNext
	 * 	Feedback to Process via setWFState -> checkActivities
	 */
	public void run(Trx trx)
	{
		log.info (toString());
		m_newValue = null;
		if (!m_state.isValidAction(StateEngine.ACTION_Start))
		{
			setTextMsg("State=" + getWFState() + " - cannot start");
			setWFState(StateEngine.STATE_Terminated);
			return;
		}
		//
		setWFState(StateEngine.STATE_Running);

		if (getNode().get_ID() == 0)
		{
			setTextMsg("Node not found - AD_WF_Node_ID=" + getAD_WF_Node_ID());
			setWFState(StateEngine.STATE_Aborted);
			return;
		}

		//
		try
		{
			//	Do Work
			/****	Trx Start	****/
			//	log.config("*Start " + toString() + " - " + m_trx.getTrxName());
			boolean done = false;
			Trx actTrx = Trx.get("MWFActivity");
			try {
				done = performWork(actTrx);
				actTrx.commit();
			}
			catch(Exception e) {
				done = false;
				actTrx.rollback();
				throw e;
			}
			finally {
				actTrx.close();
			}
			/****	Trx End		****/
			//	log.config("*Commit " + toString() + " - " + m_trx.getTrxName());
			//
			//	log.config("*State " + toString());
			setWFState (done ? StateEngine.STATE_Completed : StateEngine.STATE_Suspended);
			//	log.config("*Done  " + toString());
			//
			if (m_postImmediate != null)
				postImmediate();
		}
		catch (Exception e)
		{
			trx.rollback();
			log.log(Level.WARNING, "" + getNode(), e);
			/****	Trx Rollback	****/
			//
			if (e.getCause() != null)
				log.log(Level.WARNING, "Cause", e.getCause());
			String processMsg = e.getLocalizedMessage();
			if ((processMsg == null) || (processMsg.length() == 0))
				processMsg = e.getMessage();
			setTextMsg(processMsg);
			addTextMsg(e);
			setWFState (StateEngine.STATE_Terminated);	//	unlocks
			//	Set Document Status
			if ((m_po != null) && (m_docStatus != null))
			{
				m_po.load(trx);
				DocAction doc = (DocAction)m_po;
				doc.setDocStatus(m_docStatus);
				m_po.save();
				trx.commit();
			}
		} 
	}	//	run


	/**
	 * 	Perform Work.
	 * 	Set Text Msg.
	 * 	@param p_trx transaction
	 *	@return true if completed, false otherwise
	 *	@throws Exception if error
	 */
	protected boolean performWork (Trx p_trx) throws Exception
	{
		log.info (m_node + " " + p_trx);
		m_postImmediate = null;
		m_docStatus = null;
		if (m_node.getPriority() != 0)		//	overwrite priority if defined
			setPriority(m_node.getPriority());
		String action = m_node.getAction();

		/******	Sleep (Start/End)			******/
		if (X_AD_WF_Node.ACTION_WaitSleep.equals(action))
		{
			log.fine("Sleep:WaitTime=" + m_node.getWaitTime());
			if (m_node.getWaitingTime() == 0)
				return true;	//	done
			Calendar cal = Calendar.getInstance();
			cal.add(m_node.getDurationCalendarField(), m_node.getWaitTime());
			setEndWaitTime(new Timestamp(cal.getTimeInMillis()));
			return false;		//	not done
		}

		/******	Document Action				******/
		else if (X_AD_WF_Node.ACTION_DocumentAction.equals(action))
		{
			log.fine("DocumentAction=" + m_node.getDocAction());
			getPO(p_trx);
			if (m_po == null)
				throw new Exception("Persistent Object not found - AD_Table_ID="
						+ getAD_Table_ID() + ", Record_ID=" + getRecord_ID());
			m_po.set_Trx(p_trx);
			boolean success = false;
			String processMsg = null;
			DocAction doc = null;
			if (m_po instanceof DocAction)
			{
				doc = (DocAction)m_po;
				//
				success = DocumentEngine.processIt(doc, m_node.getDocAction()); //	** Do the work
				setTextMsg(doc.getSummary());
				processMsg = doc.getProcessMsg();
				m_docStatus = doc.getDocStatus();
				//	Post Immediate
				if (success && DocActionConstants.ACTION_Complete.equals(m_node.getDocAction()))
				{
					MClient client = MClient.get(doc.getCtx(), doc.getAD_Client_ID());
					if (client.isPostImmediate())
						m_postImmediate = doc;
				}
				//
				if (m_process != null)
					m_process.setProcessMsg(processMsg);
			}
			else
				throw new CompiereStateException("Persistent Object not DocAction - "
						+ m_po.getClass().getName()
						+ " - AD_Table_ID=" + getAD_Table_ID() + ", Record_ID=" + getRecord_ID());
			//
			if (!m_po.save())
			{
				success = false;
				processMsg = "SaveError";
				ValueNamePair ppE = CLogger.retrieveError();
				if (ppE == null)
					ppE = CLogger.retrieveWarning();
				if (ppE != null)
					processMsg += " " + ppE.getValue() + ": " + ppE.getName();
			}
			if (!success)
			{
				if ((processMsg == null) || (processMsg.length() == 0))
				{
					processMsg = "PerformWork Error - " + m_node.toStringX();
					if (doc != null)	//	problem: status will be rolled back
						processMsg += " - DocStatus=" + doc.getDocStatus();
				}
				throw new Exception(processMsg);
			}
			return success;
		}	//	DocumentAction

		/******	Report						******/
		else if (X_AD_WF_Node.ACTION_AppsReport.equals(action))
		{
			log.fine("Report:AD_Process_ID=" + m_node.getAD_Process_ID());
			//	Process
			MProcess process = MProcess.get(getCtx(), m_node.getAD_Process_ID());
			if (!process.isReport() || (process.getAD_ReportView_ID() == 0))
				throw new CompiereStateException("Not a Report AD_Process_ID=" + m_node.getAD_Process_ID());
			//
			ProcessInfo pi = new ProcessInfo (m_node.getName(true), m_node.getAD_Process_ID(),
					getAD_Table_ID(), getRecord_ID());
			pi.setAD_User_ID(getAD_User_ID());
			pi.setAD_Client_ID(getAD_Client_ID());
			MPInstance pInstance = new MPInstance(process, getRecord_ID());
			fillParameter(pInstance, p_trx);
			pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
			//	Report
			ReportEngine re = ReportEngine.get(getCtx(), pi);
			if (re == null)
				throw new CompiereStateException("Cannot create Report AD_Process_ID=" + m_node.getAD_Process_ID());
			File report = re.getPDF();
			//	Notice
			int AD_Message_ID = 753;		//	HARDCODED WorkflowResult
			MNote note = new MNote(getCtx(), AD_Message_ID, getAD_User_ID(), p_trx);
			note.setTextMsg(m_node.getName(true));
			note.setDescription(m_node.getDescription(true));
			note.setRecord(getAD_Table_ID(), getRecord_ID());
			note.save();
			//	Attachment
			MAttachment attachment = new MAttachment (getCtx(), X_AD_Note.Table_ID, note.getAD_Note_ID(), get_Trx());
			attachment.addEntry(report);
			attachment.setTextMsg(m_node.getName(true));
			attachment.save();
			return true;
		}

		/******	Process						******/
		else if (X_AD_WF_Node.ACTION_AppsProcess.equals(action))
		{
			log.fine("Process:AD_Process_ID=" + m_node.getAD_Process_ID());
			//	Process
			MProcess process = MProcess.get(getCtx(), m_node.getAD_Process_ID());
			MPInstance pInstance = new MPInstance(process, getRecord_ID());
			fillParameter(pInstance, p_trx);
			//
			ProcessInfo pi = new ProcessInfo (m_node.getName(true), m_node.getAD_Process_ID(),
					getAD_Table_ID(), getRecord_ID());
			pi.setAD_User_ID(getAD_User_ID());
			pi.setAD_Client_ID(getAD_Client_ID());
			pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
			return process.processIt(pi, p_trx);
		}

		/******	TODO Start Task				******/
		else if (X_AD_WF_Node.ACTION_AppsTask.equals(action))
		{
			log.warning ("Task:AD_Task_ID=" + m_node.getAD_Task_ID());
		}

		/******	EMail						******/
		else if (X_AD_WF_Node.ACTION_EMail.equals(action))
		{
			log.fine ("EMail:EMailRecipient=" + m_node.getEMailRecipient());
			getPO(p_trx);
			if (m_po == null)
				throw new CompiereStateException("Persistent Object not found - AD_Table_ID="
						+ getAD_Table_ID() + ", Record_ID=" + getRecord_ID());
			if (m_po instanceof DocAction)
			{
				m_emails = new ArrayList<String>();
				sendEMail();
				setTextMsg(m_emails.toString());
			}
			return true;	//	done
		}	//	EMail

		/******	Set Variable				******/
		else if (X_AD_WF_Node.ACTION_SetVariable.equals(action))
		{
			String value = m_node.getAttributeValue();
			log.fine("SetVariable:AD_Column_ID=" + m_node.getAD_Column_ID()
					+ " to " +  value);
			MColumn column = m_node.getColumn();
			int dt = column.getAD_Reference_ID();
			return setVariable (value, dt, null);
		}	//	SetVariable

		/******	TODO Start WF Instance		******/
		else if (X_AD_WF_Node.ACTION_SubWorkflow.equals(action))
		{
			log.warning ("Workflow:AD_Workflow_ID=" + m_node.getAD_Workflow_ID());
		}

		/******	User Choice					******/
		else if (X_AD_WF_Node.ACTION_UserChoice.equals(action))
		{
			log.fine("UserChoice:AD_Column_ID=" + m_node.getAD_Column_ID());
			getPO(p_trx);
			//	Approval
			if (m_node.isUserApproval()
					&& (m_po instanceof DocAction))
			{
				DocAction doc = (DocAction)m_po;
				boolean autoApproval = false;
				//	Approval Hierarchy
				if (isInvoker())
				{
					//	Set Approver
					int startAD_User_ID = getAD_User_ID();
					if (startAD_User_ID == 0)
						startAD_User_ID = doc.getDoc_User_ID();
					int nextAD_User_ID = getApprovalUser(startAD_User_ID,
							doc.getC_Currency_ID(), doc.getApprovalAmt(),
							doc.getAD_Org_ID(),
							startAD_User_ID == doc.getDoc_User_ID());	//	own doc
					//	same user = approved
					autoApproval = startAD_User_ID == nextAD_User_ID;
					if (!autoApproval)
						setAD_User_ID(nextAD_User_ID);
				}
				// Approval is supervisor of the invoker
				else if (getResponsible().isInvokerOrganization())
				{
					// find supervisor for the organization of invoker
					MOrgInfo org = MOrgInfo.get(getCtx(), m_po.getAD_Org_ID(), null);
					if (org.getSupervisor_ID() == 0)
						log.fine("No Supervisor for AD_Org_ID=" + m_po.getAD_Org_ID());
					else {
						setAD_User_ID(org.getSupervisor_ID());
					}					
				}
				// Approval is supervisor of specified organization
				else if (getResponsible().isOrganization())
				{
					// find supervisor for the specified organization
					MOrgInfo org = MOrgInfo.get(getCtx(), getResponsible().getAD_Org_ID(), null);
					if (org.getSupervisor_ID() == 0)
						log.fine("No Supervisor for AD_Org_ID=" + getResponsible().getAD_Org_ID());
					else {
						setAD_User_ID(org.getSupervisor_ID());
					}					
				}
				else if (getResponsible().isRole()) {
					MUser[] users = MUser.getWithRole(getResponsible().getRole());
					for (MUser user : users) {
						if (user.isActive()) {
							setAD_User_ID(user.getAD_User_ID());
						}
					}
				}
				else 	//	fixed Approver
				{
					MWFResponsible resp = getResponsible();
					autoApproval = resp.getAD_User_ID() == doc.getDoc_User_ID();
					if (!autoApproval && (resp.getAD_User_ID() != 0))
						setAD_User_ID(resp.getAD_User_ID());
				}
				if (autoApproval
						&& DocumentEngine.processIt(doc, DocActionConstants.ACTION_Approve)
						&& doc.save())
					return true;	//	done
			}	//	approval
			return false;	//	wait for user
		}
		/******	User Workbench				******/
		else if (X_AD_WF_Node.ACTION_UserWorkbench.equals(action))
		{
			log.fine("Workbench:?");
			return false;
		}
		/******	User Form					******/
		else if (X_AD_WF_Node.ACTION_UserForm.equals(action))
		{
			log.fine("Form:AD_Form_ID=" + m_node.getAD_Form_ID());
			return false;
		}
		/******	User Window					******/
		else if (X_AD_WF_Node.ACTION_UserWindow.equals(action))
		{
			log.fine("Window:AD_Window_ID=" + m_node.getAD_Window_ID());
			return false;
		}
		//
		throw new IllegalArgumentException("Invalid Action (Not Implemented) =" + action);
	}	//	performWork

	/**
	 * 	Set Variable
	 *	@param value new Value
	 *	@param displayType display type
	 *	@param textMsg optional Message
	 *	@return true if set
	 *	@throws Exception if error
	 */
	private boolean setVariable(String value, int displayType, String textMsg) throws Exception
	{
		m_newValue = null;
		getPO();
		if (m_po == null)
			throw new Exception("Persistent Object not found - AD_Table_ID="
					+ getAD_Table_ID() + ", Record_ID=" + getRecord_ID());
		//	Set Value
		Object dbValue = null;
		if (value == null)
			;
		else if (displayType == DisplayTypeConstants.YesNo)
			dbValue = Boolean.valueOf("Y".equals(value));
		else if (FieldType.isNumeric(displayType))
			dbValue = new BigDecimal (value);
		else
			dbValue = value;
		m_po.set_ValueOfColumn(getNode().getAD_Column_ID(), dbValue);
		m_po.save();
		Object dbValueNew = m_po.get_ValueOfColumn(getNode().getAD_Column_ID());
		if ((dbValue != null) && !dbValue.equals(dbValueNew))
		{
			if (!value.equals(dbValueNew))
				throw new Exception("Persistent Object not updated - AD_Table_ID="
						+ getAD_Table_ID() + ", Record_ID=" + getRecord_ID()
						+ " - Should=" + value + ", Is=" + dbValueNew);
		}
		//	Info
		String msg = getNode().getAttributeName() + "=" + value;
		if ((textMsg != null) && (textMsg.length() > 0))
			msg += " - " + textMsg;
		setTextMsg (msg);
		m_newValue = value;
		return true;
	}	//	setVariable

	/**
	 * 	Set User Choice
	 * 	@param AD_User_ID user
	 *	@param value new Value
	 *	@param displayType display type
	 *	@param textMsg optional Message
	 *	@return true if set
	 *	@throws Exception if error
	 */
	public boolean setUserChoice (int AD_User_ID, String value, int displayType,
			String textMsg) throws Exception
			{
		//	Check if user approves own document when a role is responsible
		if (getNode().isUserApproval() && (getPO() instanceof DocAction))
		{
			DocAction doc = (DocAction)m_po;
			MUser user = new MUser (getCtx(), AD_User_ID, null);
			MRole[] roles = user.getRoles(m_po.getAD_Org_ID());
			boolean canApproveOwnDoc = false;
			for (MRole element : roles) {
				if (element.isCanApproveOwnDoc())
				{
					canApproveOwnDoc = true;
					break;
				}	//	found a role which allows to approve own document
			}
			if (!canApproveOwnDoc)
			{
				String info = user.getName() + " cannot approve own document " + doc;
				addTextMsg(info);
				log.fine(info);
				return false;		//	ignore
			}
		}

		setWFState (StateEngine.STATE_Running);
		setAD_User_ID(AD_User_ID);
		boolean ok = setVariable (value, displayType, textMsg);
		if (!ok)
			return false;

		String newState = StateEngine.STATE_Completed;
		//	Approval
		if (getNode().isUserApproval() && (getPO() instanceof DocAction))
		{
			DocAction doc = (DocAction)m_po;
			try
			{
				//	Not pproved
				if (!"Y".equals(value))
				{
					newState = StateEngine.STATE_Aborted;
					if (!(DocumentEngine.processIt (doc, DocActionConstants.ACTION_Reject)))
						setTextMsg ("Cannot Reject - Document Status: " + doc.getDocStatus());
				}
				else
				{
					if (isInvoker())
					{
						int startAD_User_ID = getAD_User_ID();
						if (startAD_User_ID == 0)
							startAD_User_ID = doc.getDoc_User_ID();
						int nextAD_User_ID = getApprovalUser(startAD_User_ID,
								doc.getC_Currency_ID(), doc.getApprovalAmt(),
								doc.getAD_Org_ID(),
								startAD_User_ID == doc.getDoc_User_ID());	//	own doc
						//	No Approver
						if (nextAD_User_ID <= 0)
						{
							newState = StateEngine.STATE_Aborted;
							setTextMsg ("Cannot Approve - No Approver");
							DocumentEngine.processIt (doc, DocActionConstants.ACTION_Reject);
						}
						else if (startAD_User_ID != nextAD_User_ID)
						{
							forwardTo(nextAD_User_ID, "Next Approver");
							newState = StateEngine.STATE_Suspended;
						}
						else	//	Approve
						{
							if (!(DocumentEngine.processIt (doc, DocActionConstants.ACTION_Approve)))
							{
								newState = StateEngine.STATE_Aborted;
								setTextMsg ("Cannot Approve - Document Status: " + doc.getDocStatus());
							}
						}
					}
					//	No Invoker - Approve
					else if (!(DocumentEngine.processIt (doc, DocActionConstants.ACTION_Approve)))
					{
						newState = StateEngine.STATE_Aborted;
						setTextMsg ("Cannot Approve - Document Status: " + doc.getDocStatus());
					}
				}
				doc.save();
			}
			catch (Exception e)
			{
				newState = StateEngine.STATE_Terminated;
				setTextMsg ("User Choice: " + e.toString());
				log.log(Level.WARNING, "", e);
			}
			//	Send Approval Notification
			if (newState.equals(StateEngine.STATE_Aborted))
			{
				MClient client = MClient.get(getCtx(), doc.getAD_Client_ID());
				client.sendEMail(doc.getDoc_User_ID(),
						doc.getDocumentInfo() + ": " + Msg.getMsg(getCtx(), "NotApproved"),
						doc.getSummary()
						+ "\n" + doc.getProcessMsg()
						+ "\n" + getTextMsg(),
						doc.createPDF());
			}
		}
		setWFState (newState);
		return ok;
			}	//	setUserChoice

	/**
	 * 	Forward To
	 *	@param AD_User_ID user
	 *	@param textMsg text message
	 *	@return true if forwarded
	 */
	public boolean forwardTo (int AD_User_ID, String textMsg)
	{
		if (AD_User_ID == getAD_User_ID())
		{
			log.log(Level.WARNING, "Same User - AD_User_ID=" + AD_User_ID);
			return false;
		}
		//
		MUser oldUser = MUser.get(getCtx(), getAD_User_ID());
		MUser user = MUser.get(getCtx(), AD_User_ID);
		if ((user == null) || (user.get_ID() == 0))
		{
			log.log(Level.WARNING, "Does not exist - AD_User_ID=" + AD_User_ID);
			return false;
		}
		//	Update
		setAD_User_ID (user.getAD_User_ID());
		setTextMsg(textMsg);
		save();
		//	Close up Old Event
		getEventAudit();
		m_audit.setAD_User_ID(oldUser.getAD_User_ID());
		m_audit.setTextMsg(getTextMsg());
		m_audit.setAttributeName("AD_User_ID");
		m_audit.setOldValue(oldUser.getName()+ "("+oldUser.getAD_User_ID()+")");
		m_audit.setNewValue(user.getName()+ "("+user.getAD_User_ID()+")");
		//
		m_audit.setWFState(getWFState());
		m_audit.setEventType(X_AD_WF_EventAudit.EVENTTYPE_StateChanged);
		long ms = System.currentTimeMillis() - m_audit.getCreated().getTime();
		m_audit.setElapsedTimeMS(new BigDecimal(ms));
		m_audit.save();
		//	Create new one
		m_audit = new MWFEventAudit(this);
		m_audit.save();
		return true;
	}	//	forwardTo

	/**
	 * 	Set User Confirmation
	 * 	@param AD_User_ID user
	 *	@param textMsg optional message
	 */
	public void setUserConfirmation (int AD_User_ID, String textMsg)
	{
		log.fine(textMsg);
		setWFState (StateEngine.STATE_Running);
		setAD_User_ID(AD_User_ID);
		if (textMsg != null)
			setTextMsg (textMsg);
		setWFState (StateEngine.STATE_Completed);
	}	//	setUserConfirmation


	/**
	 * 	Fill Parameter
	 *	@param pInstance process instance
	 * 	@param p_trx transaction
	 */
	private void fillParameter(MPInstance pInstance, Trx p_trx)
	{
		getPO(p_trx);
		//
		MWFNodePara[] nParams = m_node.getParameters();
		MPInstancePara[] iParams = pInstance.getParameters();
		for (MPInstancePara iPara : iParams) {
			for (MWFNodePara nPara : nParams) {
				if (iPara.getParameterName().equals(nPara.getAttributeName()))
				{
					String variableName = nPara.getAttributeValue();
					log.fine(nPara.getAttributeName()
							+ " = " + variableName);
					//	Value - Constant/Variable
					Object value = variableName;
					if ((variableName == null)
							|| ((variableName != null) && (variableName.length() == 0)))
						value = null;
					else if ((variableName.indexOf("@") != -1) && (m_po != null))	//	we have a variable
					{
						//	Strip
						int index = variableName.indexOf("@");
						String columnName = variableName.substring(index+1);
						index = columnName.indexOf("@");
						if (index == -1)
						{
							log.warning(nPara.getAttributeName()
									+ " - cannot evaluate=" + variableName);
							break;
						}
						columnName = columnName.substring(0, index);
						index = m_po.get_ColumnIndex(columnName);
						if (index != -1)
						{
							value = m_po.get_Value(index);
						}
						else	//	not a column
						{
							//	try Env
							String env = getCtx().getContext( columnName);
							if (env.length() == 0)
							{
								log.warning(nPara.getAttributeName()
										+ " - not column nor environment =" + columnName
										+ "(" + variableName + ")");
								break;
							}
							else
								value = env;
						}
					}	//	@variable@

					//	No Value
					if (value == null)
					{
						if (nPara.isMandatory())
							log.warning(nPara.getAttributeName()
									+ " - empty - mandatory!");
						else
							log.fine(nPara.getAttributeName()
									+ " - empty");
						break;
					}

					//	Convert to Type
					try
					{
						if (FieldType.isNumeric(nPara.getDisplayType())
								|| FieldType.isID(nPara.getDisplayType()))
						{
							BigDecimal bd = null;
							if (value instanceof BigDecimal)
								bd = (BigDecimal)value;
							else if (value instanceof Integer)
								bd = new BigDecimal (((Integer)value).intValue());
							else
								bd = new BigDecimal (value.toString());
							iPara.setP_Number(bd);
							log.fine(nPara.getAttributeName()
									+ " = " + variableName + " (=" + bd + "=)");
						}
						else if (FieldType.isDate(nPara.getDisplayType()))
						{
							Timestamp ts = null;
							if (value instanceof Timestamp)
								ts = (Timestamp)value;
							else
								ts = Timestamp.valueOf(value.toString());
							iPara.setP_Date(ts);
							log.fine(nPara.getAttributeName()
									+ " = " + variableName + " (=" + ts + "=)");
						}
						else
						{
							iPara.setP_String(value.toString());
							log.fine(nPara.getAttributeName()
									+ " = " + variableName
									+ " (=" + value + "=) " + value.getClass().getName());
						}
						if (!iPara.save())
							log.warning("Not Saved - " + nPara.getAttributeName());
					}
					catch (Exception e)
					{
						log.warning(nPara.getAttributeName()
								+ " = " + variableName + " (" + value
								+ ") " + value.getClass().getName()
								+ " - " + e.getLocalizedMessage());
					}
					break;
				}
			}	//	node parameter loop
		}	//	instance parameter loop
	}	//	fillParameter

	/**
	 * 	Post Immediate
	 */
	private void postImmediate()
	{
		if (CConnection.get().isAppsServerOK(false))
		{
			try
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					String error = server.postImmediate(Env.getCtx(),
							m_postImmediate.getAD_Client_ID(),
							m_postImmediate.get_Table_ID(), m_postImmediate.get_ID(),
							true, m_postImmediate.get_Trx());
					m_postImmediate.get_Logger().config("Server: " + error == null ? "OK" : error);
					return;
				}
				else
					m_postImmediate.get_Logger().config("NoAppsServer");
			}
			catch (RemoteException e)
			{
				m_postImmediate.get_Logger().config("(RE) " + e.getMessage());
			}
			catch (Exception e)
			{
				m_postImmediate.get_Logger().config("(ex) " + e.getMessage());
			}
		}
	}	//	PostImmediate


	/*********************************
	 * 	Send EMail
	 */
	private void sendEMail()
	{
		// Agregado GMARQUES
		DocAction doc = (DocAction)m_po;
		MMailText text = new MMailText (getCtx(), m_node.getR_MailText_ID(), null);
		text.setPO(m_po, true);
		
		String subject = //doc.getDocumentInfo()+ ": " +
					text.getMailHeader();
		String message = text.getMailText(true)	+ "\n-----\n" + 
				(doc.getDocumentInfo()!=null ? doc.getDocumentInfo() : "") + "\n" ;//+ doc.getSummary();
		//File pdf = doc.createPDF();
		File pdf = null; //No colocar pdf

		// Se agrega info al email:
		// Orden de Compra
		if (!m_po.get_Value("C_Order_ID").equals(null)) {
			String qCorreo = "select documentno from c_order where c_order_id = " + m_po.get_Value("C_Order_ID");
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(qCorreo, null);			
				rs = ps.executeQuery();
				if (rs.next()) {
					message += "\nOrden de Compra: " +rs.getString("documentno");
				}
			} catch (SQLException e) {			
				log.log(Level.SEVERE, qCorreo, e);
				e.printStackTrace();
			}  finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
		}
		// Proveedor
		if (!m_po.get_Value("C_BPartner_ID").equals(null)) {
			String qCorreo = "select name from c_bpartner where c_bpartner_id = " +m_po.get_Value("C_BPartner_ID");
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(qCorreo, null);			
				rs = ps.executeQuery();
				if (rs.next()) {
					message += "\nProveedor: " +rs.getString("name");
				}
			} catch (SQLException e) {			
				log.log(Level.SEVERE, qCorreo, e);
				e.printStackTrace();
			}  finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
		}
		// Departamento
		if (!m_po.get_Value("XX_VMR_Department_ID").equals(null)) {
			String qCorreo = "select value ||' - '||name as name from XX_VMR_Department" +
					" where XX_VMR_Department_ID = " +m_po.get_Value("XX_VMR_Department_ID");
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(qCorreo, null);			
				rs = ps.executeQuery();
				if (rs.next()) {
					message += "\nDepartamento: " +rs.getString("name");
				}
			} catch (SQLException e) {			
				log.log(Level.SEVERE, qCorreo, e);
				e.printStackTrace();
			}  finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
	
		}
//		System.out.println("EMAIL: "+subject + " " +message );
		MClient client = MClient.get(doc.getCtx(), doc.getAD_Client_ID());
		//	Explicit EMail
		sendEMail(client, 0, m_node.getEMail(), subject, message, pdf);
		// Recipient Type
		String recipient = m_node.getEMailRecipient();
		// email to document user
		if ((recipient == null) || (recipient.length() == 0)) {
			sendEMail(client, doc.getDoc_User_ID(), null, subject, message, pdf);
		} 	
		else if (recipient.equals("B")) { //Jefe de Categor�a
			// ID del Comprador de la OC
			String qCorreo = "SELECT XX_UserBuyer_ID from " +
					" C_ORDER where join ad_user on (xx_categorymanager_id = c_bpartner_id) where XX_VMR_Category_ID= '" +
					m_po.get_Value("XX_VMR_Category_ID") + "' " +
					"\n		 and ad_user.isactive='Y'";
			int id = 0;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(qCorreo, null);			
				rs = ps.executeQuery();
				if (rs.next()) {
					id = rs.getInt("ad_user_id");
				}
			} catch (SQLException e) {			
				log.log(Level.SEVERE, qCorreo, e);
				e.printStackTrace();
			}  finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
//			System.out.println("Comprador");
			sendEMail(client, id, null, subject, message, pdf);
			
		}
		
		else if (recipient.equals("C")) { //Jefe de Categor�a
			// ID del Jefe de Categor�a correspondiente a la categor�a de la OC
			String qCorreo = "SELECT ad_user_id from " +
					" XX_VMR_CATEGORY join ad_user on (xx_categorymanager_id = c_bpartner_id) where XX_VMR_Category_ID= '" +
					m_po.get_Value("XX_VMR_Category_ID") + "' " +
					"\n		 and ad_user.isactive='Y'";
			int id = 0;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(qCorreo, null);			
				rs = ps.executeQuery();
				if (rs.next()) {
					id = rs.getInt("ad_user_id");
				}
			} catch (SQLException e) {			
				log.log(Level.SEVERE, qCorreo, e);
				e.printStackTrace();
			}  finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
//			System.out.println("Jefe de Categor�a");
			sendEMail(client, id, null, subject, message, pdf);
		}
		
		else if (recipient.equals("P")) { //Jefe de Planificaci�n
			MRole role = MRole.get(getCtx(), Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")) ;
			if (role != null) {
				MUser[] users = MUser.getWithRole(role);
				for (MUser element : users) //{}
						sendEMail(client, element.getAD_User_ID(), null, subject, message, pdf);
//				System.out.println("Jefe de Planificacion");
			}
		}
		else if (recipient.equals("L")) { //Gerente de Log�stica
			MRole role = MRole.get(getCtx(), Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID")) ;
			if (role != null) {
				MUser[] users = MUser.getWithRole(role);
				for (MUser element : users) //{}
						sendEMail(client, element.getAD_User_ID(), null, subject, message, pdf);

//				System.out.println("Gerente de Log�stica");
			}
		}
		else if (recipient.equals("M")){ //Gerente de Merchandising
			MRole role = MRole.get(getCtx(), Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID")) ;
			if (role != null) {
				MUser[] users = MUser.getWithRole(role);
				for (MUser element : users) //{}
						sendEMail(client, element.getAD_User_ID(), null, subject, message, pdf);

//				System.out.println("Gerente de Merchandising");
			}
		} 

			// Fin GMARQUES
		
//			DocAction doc = (DocAction)m_po;
//			MMailText text = new MMailText (getCtx(), m_node.getR_MailText_ID(), null);
//			text.setPO(m_po, true);
//			//
//			String subject = doc.getDocumentInfo()
//			+ ": " + text.getMailHeader();
//			String message = text.getMailText(true)
//			+ "\n-----\n" + doc.getDocumentInfo()
//			+ "\n" + doc.getSummary();
//			File pdf = doc.createPDF();
			//
//			MClient client = MClient.get(doc.getCtx(), doc.getAD_Client_ID());
//	
//			//	Explicit EMail
//			sendEMail(client, 0, m_node.getEMail(), subject, message, pdf);
//			//	Recipient Type
//			String recipient = m_node.getEMailRecipient();
//			//	email to document user
//			if ((recipient == null) || (recipient.length() == 0))
//				sendEMail(client, doc.getDoc_User_ID(), null, subject, message, pdf);
			
		else if (recipient.equals(X_AD_WF_Node.EMAILRECIPIENT_DocumentBusinessPartner))
		{
			int index = m_po.get_ColumnIndex("AD_User_ID");
			if (index > 0)
			{
				Object oo = m_po.get_Value(index);
				if (oo instanceof Integer)
				{
					int AD_User_ID = ((Integer)oo).intValue();
					if (AD_User_ID != 0)
						sendEMail(client, AD_User_ID, null, subject, message, pdf);
					else
						log.fine("No User in Document");
				}
				else
					log.fine("Empty User in Document");
			}
			else
				log.fine("No User Field in Document");
		}
		else if (recipient.equals(X_AD_WF_Node.EMAILRECIPIENT_DocumentOwner))
			sendEMail(client, doc.getDoc_User_ID(), null, subject, message, pdf);
		else if (recipient.equals(X_AD_WF_Node.EMAILRECIPIENT_WFOwner))
		{
			MWFResponsible resp = getResponsible();
			if (resp.isInvoker())
				sendEMail(client, doc.getDoc_User_ID(), null, subject, message, pdf);
			else if (resp.isHuman())
				sendEMail(client, resp.getAD_User_ID(), null, subject, message, pdf);
			else if (resp.isRole())
			{
				MRole role = resp.getRole();
				if (role != null)
				{
					MUser[] users = MUser.getWithRole(role);
					for (MUser element : users)
						sendEMail(client, element.getAD_User_ID(), null, subject, message, pdf);
				}
			}
			else if (resp.isOrganization())
			{
				MOrgInfo org = MOrgInfo.get(getCtx(), m_po.getAD_Org_ID(), null);
				if (org.getSupervisor_ID() == 0)
					log.fine("No Supervisor for AD_Org_ID=" + m_po.getAD_Org_ID());
				else
					sendEMail(client, org.getSupervisor_ID(), null, subject, message, pdf);
			}
		} //GMARQUES
	}	//	sendEMail

	/**
	 * 	Send actual EMail
	 *	@param client client
	 *	@param AD_User_ID user
	 *	@param email email string
	 *	@param subject subject
	 *	@param message message
	 *	@param pdf attachment
	 */
	private void sendEMail (MClient client, int AD_User_ID, String email,
			String subject, String message, File pdf)
	{
		if (AD_User_ID != 0)
		{
			MUser user = MUser.get(getCtx(), AD_User_ID);
			email = user.getEMail();
			if ((email != null) && (email.length() > 0))
			{
				email = email.trim();
				if (!m_emails.contains(email))
				{
					client.sendEMail(null, user, subject, message, pdf);
					m_emails.add(email);
				}
			}
			else
				log.info("No EMail for User " + user.getName());
		}
		else if ((email != null) && (email.length() > 0))
		{
			//	Just one
			if (email.indexOf(";") == -1)
			{
				email = email.trim();
				if (!m_emails.contains(email))
				{
					client.sendEMail(email, null, subject, message, pdf);
					m_emails.add(email);
				}
				return;
			}
			//	Multiple EMail
			StringTokenizer st = new StringTokenizer(email, ";");
			while (st.hasMoreTokens())
			{
				String email1 = st.nextToken().trim();
				if (email1.length() == 0)
					continue;
				if (!m_emails.contains(email1))
				{
					client.sendEMail(email1, null, subject, message, pdf);
					m_emails.add(email1);
				}
			}
		}
	}	//	sendEMail

	/**************************************************************************
	 * 	Get Process Activity (Event) History
	 *	@return history
	 */
	public String getHistoryHTML()
	{
		SimpleDateFormat format = DisplayType.getDateFormat(DisplayTypeConstants.DateTime);
		StringBuffer sb = new StringBuffer();
		MWFEventAudit[] events = MWFEventAudit.get(getCtx(), getAD_WF_Process_ID());
		for (MWFEventAudit audit : events) {
			//	sb.append("<p style=\"width:400\">");
			sb.append("<p>");
			sb.append(format.format(audit.getCreated()))
			// Modificado GMARQUES
			.append(": ")
			//.append(" (")
			//.append(getHTMLpart(null, MUser.getNameOfUser(audit.getAD_User_ID())))
			//.append(") ")
			// Fin GMARQUES
			.append(getHTMLpart("b", audit.getNodeName()))
			.append(": ")
			.append(getHTMLpart(null, audit.getDescription()))
			.append(getHTMLpart("i", audit.getTextMsg()));
			sb.append("</p>");
		}
		return sb.toString();
	}	//	getHistory

	/**
	 * 	Get HTML part
	 *	@param tag HTML tag
	 *	@param content content
	 *	@return <tag>content</tag>
	 */
	private StringBuffer getHTMLpart (String tag, String content)
	{
		StringBuffer sb = new StringBuffer();
		if ((content == null) || (content.length() == 0))
			return sb;
		if ((tag != null) && (tag.length() > 0))
			sb.append("<").append(tag).append(">");
		sb.append(content);
		if ((tag != null) && (tag.length() > 0))
			sb.append("</").append(tag).append(">");
		return sb;
	}	//	getHTMLpart


	/**************************************************************************
	 * 	Does the underlying PO (!) object have a PDF Attachment
	 * 	@return true if there is a pdf attachment
	 */
	@Override
	public boolean isPdfAttachment()
	{
		if (getPO() == null)
			return false;
		return m_po.isPdfAttachment();
	}	//	isPDFAttachment

	/**
	 * 	Get PDF Attachment of underlying PO (!) object
	 *	@return pdf data or null
	 */
	@Override
	public byte[] getPdfAttachment()
	{
		if (getPO() == null)
			return null;
		return m_po.getPdfAttachment();
	}	//	getPdfAttachment


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MWFActivity[");
		sb.append(get_ID()).append(",Node=");
		if (m_node == null)
			sb.append(getAD_WF_Node_ID());
		else
			sb.append(m_node.getName());
		sb.append(",State=").append(getWFState())
		.append(",AD_User_ID=").append(getAD_User_ID())
		.append(",").append(getCreated())
		.append ("]");
		return sb.toString ();
	} 	//	toString

	/**
	 * 	User String Representation.
	 * 	Suspended: Approve it (Joe)
	 *	@return info
	 */
	@Override
	public String toStringX ()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getWFStateText())
		.append(": ").append(getNode().getName());
		if (getAD_User_ID() > 0)
		{
			MUser user = MUser.get(getCtx(), getAD_User_ID());
			sb.append(" (").append(user.getName()).append(")");
		}
		return sb.toString();
	}	//	toStringX

}	//	MWFActivity
