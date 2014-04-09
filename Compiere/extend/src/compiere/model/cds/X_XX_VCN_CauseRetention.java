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
/** Generated Model for XX_VCN_CauseRetention
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_CauseRetention extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_CauseRetention_ID id
    @param trx transaction
    */
    public X_XX_VCN_CauseRetention (Ctx ctx, int XX_VCN_CauseRetention_ID, Trx trx)
    {
        super (ctx, XX_VCN_CauseRetention_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_CauseRetention_ID == 0)
        {
            setName (null);
            setXX_VCN_CAUSERETENTION_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_CauseRetention (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27546906289789L;
    /** Last Updated Timestamp 2010-01-29 11:32:53.0 */
    public static final long updatedMS = 1264780973000L;
    /** AD_Table_ID=1000081 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_CauseRetention");
        
    }
    ;
    
    /** TableName=XX_VCN_CauseRetention */
    public static final String Table_Name="XX_VCN_CauseRetention";
    
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
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
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
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_VCN_CAUSERETENTION_ID.
    @param XX_VCN_CAUSERETENTION_ID Id de la Tabla de Causas de Retención */
    public void setXX_VCN_CAUSERETENTION_ID (int XX_VCN_CAUSERETENTION_ID)
    {
        if (XX_VCN_CAUSERETENTION_ID < 1) throw new IllegalArgumentException ("XX_VCN_CAUSERETENTION_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_CAUSERETENTION_ID", Integer.valueOf(XX_VCN_CAUSERETENTION_ID));
        
    }
    
    /** Get XX_VCN_CAUSERETENTION_ID.
    @return Id de la Tabla de Causas de Retención */
    public int getXX_VCN_CAUSERETENTION_ID() 
    {
        return get_ValueAsInt("XX_VCN_CAUSERETENTION_ID");
        
    }
    
    
}
