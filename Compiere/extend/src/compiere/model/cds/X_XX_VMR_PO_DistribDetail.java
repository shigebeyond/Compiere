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
/** Generated Model for XX_VMR_PO_DistribDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PO_DistribDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PO_DistribDetail_ID id
    @param trx transaction
    */
    public X_XX_VMR_PO_DistribDetail (Ctx ctx, int XX_VMR_PO_DistribDetail_ID, Trx trx)
    {
        super (ctx, XX_VMR_PO_DistribDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PO_DistribDetail_ID == 0)
        {
            setM_Warehouse_ID (0);
            setXX_ProductQuantity (0);
            setXX_SizeCurveCompliance (false);
            setXX_VMR_PO_DistribDetail_ID (0);
            setXX_VMR_PO_ProductDistrib_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PO_DistribDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27578030077789L;
    /** Last Updated Timestamp 2011-01-24 17:02:41.0 */
    public static final long updatedMS = 1295904761000L;
    /** AD_Table_ID=1000191 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PO_DistribDetail");
        
    }
    ;
    
    /** TableName=XX_VMR_PO_DistribDetail */
    public static final String Table_Name="XX_VMR_PO_DistribDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Product Quantity.
    @param XX_ProductQuantity Product Quantity */
    public void setXX_ProductQuantity (int XX_ProductQuantity)
    {
        set_Value ("XX_ProductQuantity", Integer.valueOf(XX_ProductQuantity));
        
    }
    
    /** Get Product Quantity.
    @return Product Quantity */
    public int getXX_ProductQuantity() 
    {
        return get_ValueAsInt("XX_ProductQuantity");
        
    }
    
    /** Set Size Curve Compliance.
    @param XX_SizeCurveCompliance Size Curve Compliance */
    public void setXX_SizeCurveCompliance (boolean XX_SizeCurveCompliance)
    {
        set_Value ("XX_SizeCurveCompliance", Boolean.valueOf(XX_SizeCurveCompliance));
        
    }
    
    /** Get Size Curve Compliance.
    @return Size Curve Compliance */
    public boolean isXX_SizeCurveCompliance() 
    {
        return get_ValueAsBoolean("XX_SizeCurveCompliance");
        
    }
    
    /** Set Distribution Detail.
    @param XX_VMR_PO_DistribDetail_ID Distribution Detail */
    public void setXX_VMR_PO_DistribDetail_ID (int XX_VMR_PO_DistribDetail_ID)
    {
        if (XX_VMR_PO_DistribDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_PO_DistribDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PO_DistribDetail_ID", Integer.valueOf(XX_VMR_PO_DistribDetail_ID));
        
    }
    
    /** Get Distribution Detail.
    @return Distribution Detail */
    public int getXX_VMR_PO_DistribDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_DistribDetail_ID");
        
    }
    
    /** Set XX_VMR_PO_ProductDistrib_ID.
    @param XX_VMR_PO_ProductDistrib_ID XX_VMR_PO_ProductDistrib_ID */
    public void setXX_VMR_PO_ProductDistrib_ID (int XX_VMR_PO_ProductDistrib_ID)
    {
        if (XX_VMR_PO_ProductDistrib_ID < 1) throw new IllegalArgumentException ("XX_VMR_PO_ProductDistrib_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PO_ProductDistrib_ID", Integer.valueOf(XX_VMR_PO_ProductDistrib_ID));
        
    }
    
    /** Get XX_VMR_PO_ProductDistrib_ID.
    @return XX_VMR_PO_ProductDistrib_ID */
    public int getXX_VMR_PO_ProductDistrib_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_ProductDistrib_ID");
        
    }
    
    
}
