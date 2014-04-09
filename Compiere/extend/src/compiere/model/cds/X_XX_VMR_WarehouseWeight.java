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
/** Generated Model for XX_VMR_WarehouseWeight
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_WarehouseWeight extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_WarehouseWeight_ID id
    @param trx transaction
    */
    public X_XX_VMR_WarehouseWeight (Ctx ctx, int XX_VMR_WarehouseWeight_ID, Trx trx)
    {
        super (ctx, XX_VMR_WarehouseWeight_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_WarehouseWeight_ID == 0)
        {
            setXX_VMR_WAREHOUSEWEIGHT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_WarehouseWeight (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567519880789L;
    /** Last Updated Timestamp 2010-09-25 01:32:44.0 */
    public static final long updatedMS = 1285394564000L;
    /** AD_Table_ID=1000335 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_WarehouseWeight");
        
    }
    ;
    
    /** TableName=XX_VMR_WarehouseWeight */
    public static final String Table_Name="XX_VMR_WarehouseWeight";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID <= 0) set_Value ("C_Period_ID", null);
        else
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set Year.
    @param C_Year_ID Calendar Year */
    public void setC_Year_ID (int C_Year_ID)
    {
        if (C_Year_ID <= 0) set_Value ("C_Year_ID", null);
        else
        set_Value ("C_Year_ID", Integer.valueOf(C_Year_ID));
        
    }
    
    /** Get Year.
    @return Calendar Year */
    public int getC_Year_ID() 
    {
        return get_ValueAsInt("C_Year_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Check .
    @param XX_Check Check  */
    public void setXX_Check (boolean XX_Check)
    {
        set_Value ("XX_Check", Boolean.valueOf(XX_Check));
        
    }
    
    /** Get Check .
    @return Check  */
    public boolean isXX_Check() 
    {
        return get_ValueAsBoolean("XX_Check");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    /** Set XX_VMR_WAREHOUSEWEIGHT_ID.
    @param XX_VMR_WAREHOUSEWEIGHT_ID XX_VMR_WAREHOUSEWEIGHT_ID */
    public void setXX_VMR_WAREHOUSEWEIGHT_ID (int XX_VMR_WAREHOUSEWEIGHT_ID)
    {
        if (XX_VMR_WAREHOUSEWEIGHT_ID < 1) throw new IllegalArgumentException ("XX_VMR_WAREHOUSEWEIGHT_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_WAREHOUSEWEIGHT_ID", Integer.valueOf(XX_VMR_WAREHOUSEWEIGHT_ID));
        
    }
    
    /** Get XX_VMR_WAREHOUSEWEIGHT_ID.
    @return XX_VMR_WAREHOUSEWEIGHT_ID */
    public int getXX_VMR_WAREHOUSEWEIGHT_ID() 
    {
        return get_ValueAsInt("XX_VMR_WAREHOUSEWEIGHT_ID");
        
    }
    
    /** Set Warehouse Weight.
    @param XX_WarehouseWeight Warehouse Weight */
    public void setXX_WarehouseWeight (java.math.BigDecimal XX_WarehouseWeight)
    {
        set_Value ("XX_WarehouseWeight", XX_WarehouseWeight);
        
    }
    
    /** Get Warehouse Weight.
    @return Warehouse Weight */
    public java.math.BigDecimal getXX_WarehouseWeight() 
    {
        return get_ValueAsBigDecimal("XX_WarehouseWeight");
        
    }
    
    /** Set Weight Calculator.
    @param XX_WeightCalculator Weight Calculator */
    public void setXX_WeightCalculator (String XX_WeightCalculator)
    {
        set_Value ("XX_WeightCalculator", XX_WeightCalculator);
        
    }
    
    /** Get Weight Calculator.
    @return Weight Calculator */
    public String getXX_WeightCalculator() 
    {
        return (String)get_Value("XX_WeightCalculator");
        
    }
    
    /** Set Weight Check.
    @param XX_WeightCheck Weight Check */
    public void setXX_WeightCheck (boolean XX_WeightCheck)
    {
        set_Value ("XX_WeightCheck", Boolean.valueOf(XX_WeightCheck));
        
    }
    
    /** Get Weight Check.
    @return Weight Check */
    public boolean isXX_WeightCheck() 
    {
        return get_ValueAsBoolean("XX_WeightCheck");
        
    }
    
    
}
