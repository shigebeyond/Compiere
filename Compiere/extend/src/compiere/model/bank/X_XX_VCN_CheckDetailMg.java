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
package compiere.model.bank;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_CheckDetailMg
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_CheckDetailMg extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_CheckDetailMg_ID id
    @param trx transaction
    */
    public X_XX_VCN_CheckDetailMg (Ctx ctx, int XX_VCN_CheckDetailMg_ID, Trx trx)
    {
        super (ctx, XX_VCN_CheckDetailMg_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_CheckDetailMg_ID == 0)
        {
            setXX_VCN_Amount (Env.ZERO);
            setXX_VCN_CHECKDETAILMG_ID (0);
            setXX_VCN_ManagementCheck_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_CheckDetailMg (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27663631020789L;
    /** Last Updated Timestamp 2013-10-11 11:05:04.0 */
    public static final long updatedMS = 1381505704000L;
    /** AD_Table_ID=1005354 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_CheckDetailMg");
        
    }
    ;
    
    /** TableName=XX_VCN_CheckDetailMg */
    public static final String Table_Name="XX_VCN_CheckDetailMg";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set XX_VCN_Amount.
    @param XX_VCN_Amount XX_VCN_Amount */
    public void setXX_VCN_Amount (java.math.BigDecimal XX_VCN_Amount)
    {
        if (XX_VCN_Amount == null) throw new IllegalArgumentException ("XX_VCN_Amount is mandatory.");
        set_Value ("XX_VCN_Amount", XX_VCN_Amount);
        
    }
    
    /** Get XX_VCN_Amount.
    @return XX_VCN_Amount */
    public java.math.BigDecimal getXX_VCN_Amount() 
    {
        return get_ValueAsBigDecimal("XX_VCN_Amount");
        
    }
    
    /** Set XX_VCN_CHECKDETAILMG_ID.
    @param XX_VCN_CHECKDETAILMG_ID XX_VCN_CHECKDETAILMG_ID */
    public void setXX_VCN_CHECKDETAILMG_ID (int XX_VCN_CHECKDETAILMG_ID)
    {
        if (XX_VCN_CHECKDETAILMG_ID < 1) throw new IllegalArgumentException ("XX_VCN_CHECKDETAILMG_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_CHECKDETAILMG_ID", Integer.valueOf(XX_VCN_CHECKDETAILMG_ID));
        
    }
    
    /** Get XX_VCN_CHECKDETAILMG_ID.
    @return XX_VCN_CHECKDETAILMG_ID */
    public int getXX_VCN_CHECKDETAILMG_ID() 
    {
        return get_ValueAsInt("XX_VCN_CHECKDETAILMG_ID");
        
    }
    
    /** Set XX_VCN_ManagementCheck_ID.
    @param XX_VCN_ManagementCheck_ID XX_VCN_ManagementCheck_ID */
    public void setXX_VCN_ManagementCheck_ID (int XX_VCN_ManagementCheck_ID)
    {
        if (XX_VCN_ManagementCheck_ID < 1) throw new IllegalArgumentException ("XX_VCN_ManagementCheck_ID is mandatory.");
        set_Value ("XX_VCN_ManagementCheck_ID", Integer.valueOf(XX_VCN_ManagementCheck_ID));
        
    }
    
    /** Get XX_VCN_ManagementCheck_ID.
    @return XX_VCN_ManagementCheck_ID */
    public int getXX_VCN_ManagementCheck_ID() 
    {
        return get_ValueAsInt("XX_VCN_ManagementCheck_ID");
        
    }
    
    
}
