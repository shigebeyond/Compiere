/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AD_Workflow_Access
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Workflow_Access.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Workflow_Access extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Workflow_Access_ID id
    @param trx transaction
    */
    public X_AD_Workflow_Access (Ctx ctx, int AD_Workflow_Access_ID, Trx trx)
    {
        super (ctx, AD_Workflow_Access_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Workflow_Access_ID == 0)
        {
            setAD_Role_ID (0);
            setAD_Workflow_ID (0);
            setIsReadWrite (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Workflow_Access (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=202 */
    public static final int Table_ID=202;
    
    /** TableName=AD_Workflow_Access */
    public static final String Table_Name="AD_Workflow_Access";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID < 0) throw new IllegalArgumentException ("AD_Role_ID is mandatory.");
        set_ValueNoCheck ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Role_ID()));
        
    }
    
    /** Set Workflow.
    @param AD_Workflow_ID Workflow or combination of tasks */
    public void setAD_Workflow_ID (int AD_Workflow_ID)
    {
        if (AD_Workflow_ID < 1) throw new IllegalArgumentException ("AD_Workflow_ID is mandatory.");
        set_ValueNoCheck ("AD_Workflow_ID", Integer.valueOf(AD_Workflow_ID));
        
    }
    
    /** Get Workflow.
    @return Workflow or combination of tasks */
    public int getAD_Workflow_ID() 
    {
        return get_ValueAsInt("AD_Workflow_ID");
        
    }
    
    /** Set Read Write.
    @param IsReadWrite Field is read / write */
    public void setIsReadWrite (boolean IsReadWrite)
    {
        set_Value ("IsReadWrite", Boolean.valueOf(IsReadWrite));
        
    }
    
    /** Get Read Write.
    @return Field is read / write */
    public boolean isReadWrite() 
    {
        return get_ValueAsBoolean("IsReadWrite");
        
    }
    
    
}
