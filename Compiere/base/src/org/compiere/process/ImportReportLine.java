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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Import ReportLines from I_ReportLine
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportReportLine.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ImportReportLine extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/** Default Report Line Set			*/
	private int				m_PA_ReportLineSet_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("PA_ReportLineSet_ID"))
				m_PA_ReportLineSet_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID= ? ";

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_ReportLine "
				+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID,?),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Reset=" + no);

		//	ReportLineSetName (Default)
		if (m_PA_ReportLineSet_ID != 0)
		{
			sql = new StringBuffer ("UPDATE I_ReportLine i "
				+ "SET ReportLineSetName=(SELECT Name "
				                      + " FROM PA_ReportLineSet r "
				                      + " WHERE PA_ReportLineSet_ID= ? "
				                      + " AND i.AD_Client_ID=r.AD_Client_ID) "
				+ "WHERE ReportLineSetName IS NULL AND PA_ReportLineSet_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString(),m_PA_ReportLineSet_ID,m_AD_Client_ID);
			log.fine("Set ReportLineSetName Default=" + no);
		}
		//	Set PA_ReportLineSet_ID
		sql = new StringBuffer ("UPDATE I_ReportLine i "
			+ "SET PA_ReportLineSet_ID=(SELECT PA_ReportLineSet_ID FROM PA_ReportLineSet r"
			+ " WHERE i.ReportLineSetName=r.Name AND i.AD_Client_ID=r.AD_Client_ID) "
			+ "WHERE PA_ReportLineSet_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set PA_ReportLineSet_ID=" + no);
		//
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ReportLineSet, ' "
			+ "WHERE PA_ReportLineSet_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Invalid ReportLineSet=" + no);

		//	Ignore if there is no Report Line Name or ID
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Ignored=NoLineName, ' "
			+ "WHERE PA_ReportLine_ID IS NULL AND Name IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Invalid LineName=" + no);

		//	Validate ElementValue
		sql = new StringBuffer ("UPDATE I_ReportLine i "
			+ "SET C_ElementValue_ID=(SELECT C_ElementValue_ID FROM C_ElementValue e"
			+ " WHERE i.ElementValue=e.Value AND i.AD_Client_ID=e.AD_Client_ID) "
			+ "WHERE C_ElementValue_ID IS NULL AND ElementValue IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set C_ElementValue_ID=" + no);
		
		//	Validate C_ElementValue_ID
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ElementValue, ' "
			+ "WHERE C_ElementValue_ID IS NULL AND LineType<>'C'" // MReportLine.LINETYPE_Calculation
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Invalid AccountType=" + no);

		//	Set SeqNo
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET SeqNo=I_ReportLine_ID "
			+ "WHERE SeqNo IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set SeqNo Default=" + no);

		//	Copy/Sync from first Row of Line
		sql = new StringBuffer ("UPDATE I_ReportLine i "
			+ "SET (Description, SeqNo, IsSummary, IsPrinted, LineType, CalculationType, AmountType, PostingType)="
			+ " (SELECT Description, SeqNo, IsSummary, IsPrinted, LineType, CalculationType, AmountType, PostingType"
			+ " FROM I_ReportLine ii WHERE i.Name=ii.Name AND i.PA_ReportLineSet_ID=ii.PA_ReportLineSet_ID"
			+ " AND ii.I_ReportLine_ID=(SELECT MIN(I_ReportLine_ID) FROM I_ReportLine iii"
			+ " WHERE i.Name=iii.Name AND i.PA_ReportLineSet_ID=iii.PA_ReportLineSet_ID)) "
			+ "WHERE EXISTS (SELECT *"
			+ " FROM I_ReportLine ii WHERE i.Name=ii.Name AND i.PA_ReportLineSet_ID=ii.PA_ReportLineSet_ID"
			+ " AND ii.I_ReportLine_ID=(SELECT MIN(I_ReportLine_ID) FROM I_ReportLine iii"
			+ " WHERE i.Name=iii.Name AND i.PA_ReportLineSet_ID=iii.PA_ReportLineSet_ID))"
			+ " AND I_IsImported='N'").append(clientCheck);		//	 not if previous error
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Sync from first Row of Line=" + no);

		//	Validate IsSummary - (N) Y
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET IsSummary='N' "
			+ "WHERE IsSummary IS NULL OR IsSummary NOT IN ('Y','N')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set IsSummary Default=" + no);

		//	Validate IsPrinted - (Y) N
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET IsPrinted='Y' "
			+ "WHERE IsPrinted IS NULL OR IsPrinted NOT IN ('Y','N')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set IsPrinted Default=" + no);

		//	Validate Line Type - (S) C
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET LineType='S' "
			+ "WHERE LineType IS NULL OR LineType NOT IN ('S','C')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set LineType Default=" + no);

		//	Validate Optional Calculation Type - A P R S
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid CalculationType, ' "
			+ "WHERE CalculationType IS NOT NULL AND CalculationType NOT IN ('A','P','R','S')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Invalid CalculationType=" + no);

		//	Validate Optional Amount Type -
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid CalculationType, ' "
			+ "WHERE AmountType IS NOT NULL AND UPPER(AmountType) NOT IN ('BP','CP','DP','QP', 'BY','CY','DY','QY', 'BT','CT','DT','QT')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Invalid AmountType=" + no);

		//	Validate Optional Posting Type - A B E S R
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid CalculationType, ' "
			+ "WHERE PostingType IS NOT NULL AND PostingType NOT IN ('A','B','E','S','R')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Invalid PostingType=" + no);

		//	Set PA_ReportLine_ID
		sql = new StringBuffer ("UPDATE I_ReportLine i "
			+ "SET PA_ReportLine_ID=(SELECT MAX(PA_ReportLine_ID) FROM PA_ReportLine r"
			+ " WHERE i.Name=r.Name AND i.PA_ReportLineSet_ID=r.PA_ReportLineSet_ID) "
			+ "WHERE PA_ReportLine_ID IS NULL AND PA_ReportLineSet_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set PA_ReportLine_ID=" + no);

		commit();
		
		//	-------------------------------------------------------------------
		int noInsertLine = 0;
		int noUpdateLine = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt_insertLine = null;
		ResultSet rs = null;
		
		//	****	Create Missing ReportLines
		sql = new StringBuffer ("SELECT DISTINCT PA_ReportLineSet_ID, Name "
			+ "FROM I_ReportLine "
			+ "WHERE I_IsImported='N' AND PA_ReportLine_ID IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		try
		{
			//	Insert ReportLine
			pstmt_insertLine = DB.prepareStatement
				("INSERT INTO PA_ReportLine "
				+ "(PA_ReportLine_ID,PA_ReportLineSet_ID,"
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "Name,SeqNo,IsPrinted,IsSummary,LineType)"
				+ "SELECT ?,PA_ReportLineSet_ID,"
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "Name,SeqNo,IsPrinted,IsSummary,LineType "
				+ "FROM I_ReportLine "
				+ "WHERE I_ReportLine_ID=(SELECT MAX(I_ReportLine_ID) "		
				+ "FROM I_ReportLine "
				+ "WHERE PA_ReportLineSet_ID=? AND Name=? "		//	#2..3
				+ clientCheck + ")", get_TrxName());

			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int PA_ReportLineSet_ID = rs.getInt(1);
				String Name = rs.getString(2);
				//
				try
				{
					int PA_ReportLine_ID = DB.getNextID(m_AD_Client_ID, "PA_ReportLine", get_TrxName());
					if (PA_ReportLine_ID <= 0)
						throw new DBException("No NextID (" + PA_ReportLine_ID + ")");
					pstmt_insertLine.setInt(1, PA_ReportLine_ID);
					pstmt_insertLine.setInt(2, PA_ReportLineSet_ID);
					pstmt_insertLine.setString(3, Name);
					pstmt_insertLine.setInt(4, m_AD_Client_ID);
					//
					no = pstmt_insertLine.executeUpdate();
					log.finest("Insert ReportLine = " + no + ", PA_ReportLine_ID=" + PA_ReportLine_ID);
					noInsertLine++;
				}
				catch (Exception ex)
				{
					log.finest(ex.toString());
					continue;
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "Create ReportLine", e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeStatement(pstmt_insertLine);
			DB.closeResultSet(rs);
		}

		//	Set PA_ReportLine_ID (for newly created)
		sql = new StringBuffer ("UPDATE I_ReportLine i "
			+ "SET PA_ReportLine_ID=(SELECT MAX(PA_ReportLine_ID) FROM PA_ReportLine r"
			+ " WHERE i.Name=r.Name AND i.PA_ReportLineSet_ID=r.PA_ReportLineSet_ID) "
			+ "WHERE PA_ReportLine_ID IS NULL AND PA_ReportLineSet_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.fine("Set PA_ReportLine_ID=" + no);

		//	****	Update ReportLine
		sql = new StringBuffer ("UPDATE PA_ReportLine r "
			+ "SET (Description,SeqNo,IsSummary,IsPrinted,LineType,CalculationType,AmountType,PostingType,Updated,UpdatedBy)="
			+ " (SELECT Description,SeqNo,IsSummary,IsPrinted,LineType,CalculationType,AmountType,PostingType,SysDate,UpdatedBy"
			+ " FROM I_ReportLine i WHERE r.Name=i.Name AND r.PA_ReportLineSet_ID=i.PA_ReportLineSet_ID"
			+ " AND i.I_ReportLine_ID=(SELECT MIN(I_ReportLine_ID) FROM I_ReportLine iii"
			+ " WHERE i.Name=iii.Name AND i.PA_ReportLineSet_ID=iii.PA_ReportLineSet_ID)) "
			+ "WHERE EXISTS (SELECT *"
			+ " FROM I_ReportLine i WHERE r.Name=i.Name AND r.PA_ReportLineSet_ID=i.PA_ReportLineSet_ID"
			+ " AND i.I_ReportLine_ID=(SELECT MIN(I_ReportLine_ID) FROM I_ReportLine iii"
			+ " WHERE i.Name=iii.Name AND i.PA_ReportLineSet_ID=iii.PA_ReportLineSet_ID AND i.I_IsImported='N'))")
			.append(clientCheck);
		noUpdateLine = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		log.config("Update PA_ReportLine=" + noUpdateLine);


		//	-------------------------------------------------------------------
		int noInsertSource = 0;
		int noUpdateSource = 0;
		PreparedStatement pstmt_insertSource = null;
		PreparedStatement pstmt_setImported = null;
		//	****	Create ReportSource
		sql = new StringBuffer ("SELECT I_ReportLine_ID, PA_ReportSource_ID "
			+ "FROM I_ReportLine "
			+ "WHERE PA_ReportLine_ID IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		try
		{
			//	Insert ReportSource
			pstmt_insertSource = DB.prepareStatement
				("INSERT INTO PA_ReportSource "
				+ "(PA_ReportSource_ID,"
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "PA_ReportLine_ID,ElementType,C_ElementValue_ID) "
				+ "SELECT ?,"
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "PA_ReportLine_ID,'AC',C_ElementValue_ID "
				+ "FROM I_ReportLine "
				+ "WHERE I_ReportLine_ID=?"
				+ " AND I_IsImported='N'"
				+ clientCheck, get_TrxName());

			//	Set Imported = Y
			pstmt_setImported = DB.prepareStatement
				("UPDATE I_ReportLine SET I_IsImported='Y',"
				+ " PA_ReportSource_ID=?, "
				+ " Updated=SysDate, Processed='Y' WHERE I_ReportLine_ID=?", get_TrxName());

			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int I_ReportLine_ID = rs.getInt(1);
				int PA_ReportSource_ID = rs.getInt(2);
				//
				if (PA_ReportSource_ID == 0)			//	New ReportSource
				{
					try
					{
						PA_ReportSource_ID = DB.getNextID(m_AD_Client_ID, "PA_ReportSource", get_TrxName());
						if (PA_ReportSource_ID <= 0)
							throw new DBException("No NextID (" + PA_ReportSource_ID + ")");
						pstmt_insertSource.setInt(1, PA_ReportSource_ID);
						pstmt_insertSource.setInt(2, I_ReportLine_ID);
						pstmt_insertSource.setInt(3, m_AD_Client_ID);
						//
						no = pstmt_insertSource.executeUpdate();
						log.finest("Insert ReportSource = " + no + ", I_ReportLine_ID=" + I_ReportLine_ID + ", PA_ReportSource_ID=" + PA_ReportSource_ID);
						noInsertSource++;
					}
					catch (Exception ex)
					{
						log.finest("Insert ReportSource - " + ex.toString());
						sql = new StringBuffer ("UPDATE I_ReportLine i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Insert ElementSource: " + ex.toString()))
							.append("WHERE I_ReportLine_ID= ? ");
						DB.executeUpdate(get_TrxName(), sql.toString(),I_ReportLine_ID);
						continue;
					}
				}
				else								//	update Report Source
				{
					String sqlt=" UPDATE PA_ReportSource "
						      + " SET ElementType = (SELECT CAST('AC' AS CHAR(2)) "
						                         + " FROM I_ReportLine"
						                         + " WHERE I_ReportLine_ID= ? ), " 
					          + " C_ElementValue_ID = (SELECT C_ElementValue_ID "
							                       + " FROM I_ReportLine"
							                       + " WHERE I_ReportLine_ID= ? ), "
						      + " UpdatedBy = (SELECT UpdatedBy "
						                   + " FROM I_ReportLine"
								           + " WHERE I_ReportLine_ID= ? )," 
							  + " Updated = (SELECT SYSDATE FROM DUAL ) "
							  + " WHERE PA_ReportSource_ID= ? "
						      + clientCheck;
					PreparedStatement pstmt_updateSource = DB.prepareStatement
						(sqlt, get_TrxName());
					try
					{
						pstmt_updateSource.setInt(1, I_ReportLine_ID);
						pstmt_updateSource.setInt(2, I_ReportLine_ID);
						pstmt_updateSource.setInt(3, I_ReportLine_ID);
						pstmt_updateSource.setInt(4, PA_ReportSource_ID);
						pstmt_updateSource.setInt(5, m_AD_Client_ID);
						no = pstmt_updateSource.executeUpdate();
						log.finest("Update ReportSource = " + no + ", I_ReportLine_ID=" + I_ReportLine_ID + ", PA_ReportSource_ID=" + PA_ReportSource_ID);
						noUpdateSource++;
					}
					catch (SQLException ex)
					{
						log.finest( "Update ReportSource - " + ex.toString());
						sql = new StringBuffer ("UPDATE I_ReportLine i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Update ElementSource: " + ex.toString()))
							.append("WHERE I_ReportLine_ID= ? ");
						DB.executeUpdate(get_TrxName(), sql.toString(),I_ReportLine_ID);
						continue;
					}
					finally
					{
						DB.closeStatement(pstmt_updateSource);
					}
				}	//	update source

				//	Set Imported to Y
				pstmt_setImported.setInt(1, PA_ReportSource_ID);
				pstmt_setImported.setInt(2, I_ReportLine_ID);
				no = pstmt_setImported.executeUpdate();
				if (no != 1)
					log.log(Level.SEVERE, "Set Imported=" + no);
				//
				commit();
			}
		}
		catch (SQLException e)
		{
		}
		finally
		{
			DB.closeStatement(pstmt_insertSource);
			DB.closeStatement(pstmt_setImported);
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}

		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_ReportLine "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),m_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsertLine), "@PA_ReportLine_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdateLine), "@PA_ReportLine_ID@: @Updated@");
		addLog (0, null, new BigDecimal (noInsertSource), "@PA_ReportSource_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdateSource), "@PA_ReportSource_ID@: @Updated@");

		return "";
	}	//	doIt

}	//	ImportReportLine
