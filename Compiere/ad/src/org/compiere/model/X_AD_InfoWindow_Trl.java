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
/** Generated Model for AD_InfoWindow_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_InfoWindow_Trl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_InfoWindow_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_InfoWindow_Trl_ID id
    @param trx transaction
    */
    public X_AD_InfoWindow_Trl (Ctx ctx, int AD_InfoWindow_Trl_ID, Trx trx)
    {
        super (ctx, AD_InfoWindow_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_InfoWindow_Trl_ID == 0)
        {
            setAD_InfoWindow_ID (0);
            setAD_Language (null);
            setIsTranslated (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_InfoWindow_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523942183789L;
    /** Last Updated Timestamp 2009-05-08 14:07:47.0 */
    public static final long updatedMS = 1241816867000L;
    /** AD_Table_ID=896 */
    public static final int Table_ID=896;
    
    /** TableName=AD_InfoWindow_Trl */
    public static final String Table_Name="AD_InfoWindow_Trl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Info Window.
    @param AD_InfoWindow_ID Info and search/select Window */
    public void setAD_InfoWindow_ID (int AD_InfoWindow_ID)
    {
        if (AD_InfoWindow_ID < 1) throw new IllegalArgumentException ("AD_InfoWindow_ID is mandatory.");
        set_ValueNoCheck ("AD_InfoWindow_ID", Integer.valueOf(AD_InfoWindow_ID));
        
    }
    
    /** Get Info Window.
    @return Info and search/select Window */
    public int getAD_InfoWindow_ID() 
    {
        return get_ValueAsInt("AD_InfoWindow_ID");
        
    }
    
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_ValueNoCheck ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Language()));
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Translated.
    @param IsTranslated This column is translated */
    public void setIsTranslated (boolean IsTranslated)
    {
        set_Value ("IsTranslated", Boolean.valueOf(IsTranslated));
        
    }
    
    /** Get Translated.
    @return This column is translated */
    public boolean isTranslated() 
    {
        return get_ValueAsBoolean("IsTranslated");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    
}
