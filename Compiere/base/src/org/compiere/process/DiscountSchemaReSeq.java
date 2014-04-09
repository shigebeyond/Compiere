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

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Renumber Discount Schema
 *	
 *  @author Jorg Janke
 *  @version $Id: DiscountSchemaReSeq.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class DiscountSchemaReSeq extends SvrProcess
{
	/** Discount Schema			*/
	private int p_M_DiscountSchema_ID = 0;

	/**
	 * 	Prepare
	 *	@see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare ()
	{
		p_M_DiscountSchema_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Execute
	 *	@return info
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("M_DiscountSchema_ID=" + p_M_DiscountSchema_ID);
		if (p_M_DiscountSchema_ID == 0)
			throw new CompiereUserException("@M_DiscountSchema_ID@ = 0");
		MDiscountSchema ds = new MDiscountSchema(getCtx(), p_M_DiscountSchema_ID, get_TrxName());
		if (ds.get_ID() == 0)
			throw new CompiereUserException("@NotFound@ M_DiscountSchema_ID=" + p_M_DiscountSchema_ID);
		//
		int updated = ds.reSeq();
		
		return "@Updated@ #" + updated;
	}	//	doIt
	
}	// DiscountSchemaRenumber
