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
/** Generated Model for MRP_Forecast
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_MRP_Forecast.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_MRP_Forecast extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_Forecast_ID id
    @param trx transaction
    */
    public X_MRP_Forecast (Ctx ctx, int MRP_Forecast_ID, Trx trx)
    {
        super (ctx, MRP_Forecast_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_Forecast_ID == 0)
        {
            setIsFrozen (false);	// N
            setIsPreviouslyFrozen (false);
            setMRP_Forecast_ID (0);
            setMRP_Plan_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_Forecast (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27530480039789L;
    /** Last Updated Timestamp 2009-07-23 06:12:03.0 */
    public static final long updatedMS = 1248354723000L;
    /** AD_Table_ID=2094 */
    public static final int Table_ID=2094;
    
    /** TableName=MRP_Forecast */
    public static final String Table_Name="MRP_Forecast";
    
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
    
    /** Set Freeze.
    @param FreezeAction Freeze the data */
    public void setFreezeAction (String FreezeAction)
    {
        set_Value ("FreezeAction", FreezeAction);
        
    }
    
    /** Get Freeze.
    @return Freeze the data */
    public String getFreezeAction() 
    {
        return (String)get_Value("FreezeAction");
        
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
    
    /** Set Frozen.
    @param IsFrozen Indicates if the data record is frozen */
    public void setIsFrozen (boolean IsFrozen)
    {
        set_Value ("IsFrozen", Boolean.valueOf(IsFrozen));
        
    }
    
    /** Get Frozen.
    @return Indicates if the data record is frozen */
    public boolean isFrozen() 
    {
        return get_ValueAsBoolean("IsFrozen");
        
    }
    
    /** Set Previously Frozen.
    @param IsPreviouslyFrozen Indicates that the data record was frozen and then unfrozen */
    public void setIsPreviouslyFrozen (boolean IsPreviouslyFrozen)
    {
        set_Value ("IsPreviouslyFrozen", Boolean.valueOf(IsPreviouslyFrozen));
        
    }
    
    /** Get Previously Frozen.
    @return Indicates that the data record was frozen and then unfrozen */
    public boolean isPreviouslyFrozen() 
    {
        return get_ValueAsBoolean("IsPreviouslyFrozen");
        
    }
    
    /** Set Forecast.
    @param MRP_Forecast_ID Material requirements forecast */
    public void setMRP_Forecast_ID (int MRP_Forecast_ID)
    {
        if (MRP_Forecast_ID < 1) throw new IllegalArgumentException ("MRP_Forecast_ID is mandatory.");
        set_ValueNoCheck ("MRP_Forecast_ID", Integer.valueOf(MRP_Forecast_ID));
        
    }
    
    /** Get Forecast.
    @return Material requirements forecast */
    public int getMRP_Forecast_ID() 
    {
        return get_ValueAsInt("MRP_Forecast_ID");
        
    }
    
    /** Set Plan.
    @param MRP_Plan_ID Material Requirements Plan */
    public void setMRP_Plan_ID (int MRP_Plan_ID)
    {
        if (MRP_Plan_ID < 1) throw new IllegalArgumentException ("MRP_Plan_ID is mandatory.");
        set_ValueNoCheck ("MRP_Plan_ID", Integer.valueOf(MRP_Plan_ID));
        
    }
    
    /** Get Plan.
    @return Material Requirements Plan */
    public int getMRP_Plan_ID() 
    {
        return get_ValueAsInt("MRP_Plan_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getMRP_Plan_ID()));
        
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
    
    /** Set Unfreeze.
    @param Unfreeze Unfreeze the data record to allow changes */
    public void setUnfreeze (String Unfreeze)
    {
        set_Value ("Unfreeze", Unfreeze);
        
    }
    
    /** Get Unfreeze.
    @return Unfreeze the data record to allow changes */
    public String getUnfreeze() 
    {
        return (String)get_Value("Unfreeze");
        
    }
    
    
}
