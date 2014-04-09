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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_ProductName
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id$ */
public class X_XX_ProductName extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_ProductName_ID id
    @param trxName transaction
    */
    public X_XX_ProductName (Ctx ctx, int XX_ProductName_ID, Trx trxName)
    {
        super (ctx, XX_ProductName_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_ProductName_ID == 0)
        {
            setValue (null);
            setXX_ProductName (null);
            setXX_ProductName_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_XX_ProductName (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27569641549789L;
    /** Last Updated Timestamp 2010-10-19 14:53:53.0 */
    public static final long updatedMS = 1287516233000L;
    /** AD_Table_ID=1000388 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_ProductName");
        
    }
    ;
    
    /** TableName=XX_ProductName */
    public static final String Table_Name="XX_ProductName";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"XX_ProductName");

    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Product Name.
    @param XX_ProductName Product Name */
    public void setXX_ProductName (String XX_ProductName)
    {
        if (XX_ProductName == null) throw new IllegalArgumentException ("XX_ProductName is mandatory.");
        set_Value ("XX_ProductName", XX_ProductName);
        
    }
    
    /** Get Product Name.
    @return Product Name */
    public String getXX_ProductName() 
    {
        return (String)get_Value("XX_ProductName");
        
    }
    
    /** Set XX_PRODUCTNAME_ID.
    @param XX_PRODUCTNAME_ID XX_PRODUCTNAME_ID */
    public void setXX_ProductName_ID (int XX_ProductName_ID)
    {
        if (XX_ProductName_ID < 1) throw new IllegalArgumentException ("XX_ProductName_ID is mandatory.");
        set_ValueNoCheck ("XX_ProductName_ID", Integer.valueOf(XX_ProductName_ID));
        
    }
    
    /** Get XX_PRODUCTNAME_ID.
    @return XX_PRODUCTNAME_ID */
    public int getXX_ProductName_ID() 
    {
        return get_ValueAsInt("XX_ProductName_ID");
        
    }
    
    
}
