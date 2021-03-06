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
package org.compiere.print;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.print.layout.*;
import org.compiere.util.*;

import com.qoppa.pdf.*;
import com.qoppa.pdfProcess.*;


/**
 *	Archive Engine.
 *	Based on Settings on Client Level
 *	Keys set for
 *	- Menu Reports - AD_Report_ID
 *	- Win Report - AD_Table_ID
 *	- Documents - AD_Table_ID & Record_ID & C_Customer_ID 
 *	
 *  @author Jorg Janke
 *  @version $Id: ArchiveEngine.java,v 1.3 2006/07/30 00:53:02 jjanke Exp $
 */
public class ArchiveEngine
{
	/**
	 * 	Get/Create Archive.
	 * 	@param layout layout
	 * 	@param info print info
	 * 	@return existing document or newly created if Client enabled archiving. 
	 * 	Will return NULL if archiving not enabled
	 */ 
	public PDFDocument archive (LayoutEngine layout, PrintInfo info)
	{
		//	Do we need to Archive ?
		MClient client = MClient.get(layout.getCtx());
		String aaClient = client.getAutoArchive();
		String aaRole = null; 	//	role.getAutoArchive();	//	TODO
		String aa = aaClient;
		if (aa == null)
			aa = X_AD_Client.AUTOARCHIVE_None;
		if (aaRole != null)
		{
			if (aaRole.equals(X_AD_Client.AUTOARCHIVE_AllReportsDocuments))
				aa = aaRole;
			else if (aaRole.equals(X_AD_Client.AUTOARCHIVE_Documents) && !aaClient.equals(X_AD_Client.AUTOARCHIVE_AllReportsDocuments))
				aa = aaRole;
		}
		//	Mothing to Archive
		if (aa.equals(X_AD_Client.AUTOARCHIVE_None))
			return null;
		//	Archive External only
		if (aa.equals(X_AD_Client.AUTOARCHIVE_ExternalDocuments))
		{
			if (info.isReport())
				return null;
		}
		//	Archive Documents only
		if (aa.equals(X_AD_Client.AUTOARCHIVE_Documents))
		{
			if (info.isReport())
				return null;
		}
		
		//	Create Printable
		byte[] data = Document.getPDFAsArray(layout.getPageable(false));	//	No Copy
		if (data == null)
			return null;

		//	TODO to be done async
		MArchive archive = new MArchive (layout.getCtx(),info, null);
		archive.setBinaryData(data);
		archive.save();
		log.info(info.toString());
		
		return null;
	}	//	archive
	
	/**
	 * 	Can we archive the document?
	 *	@param layout layout
	 *	@return true if can be archived
	 */
	public static boolean isValid (LayoutEngine layout)
	{
		return (layout != null 
			&& Document.isValid(layout)
			&& layout.getNumberOfPages() > 0);
	}	//	isValid
	
	
	/**
	 * 	Get Archive Engine
	 *	@return engine
	 */
	public static ArchiveEngine get()
	{
		if (s_engine == null)
			s_engine = new ArchiveEngine();
		return s_engine;
	}	//	get
	
	//	Create Archiver
	static {
		s_engine = new ArchiveEngine();
	}
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ArchiveEngine.class);
	/** Singleton		*/
	private static ArchiveEngine s_engine = null;
	
	
	/**************************************************************************
	 * 	ArchiveEngine
	 */
	private ArchiveEngine ()
	{
		super ();
		if (s_engine == null)
			s_engine = this;
	}	//	ArchiveEngine

	/** The base document			*/
//	private PDFDocument m_document = Document.createBlank();
	
	
	
}	//	ArchiveEngine
