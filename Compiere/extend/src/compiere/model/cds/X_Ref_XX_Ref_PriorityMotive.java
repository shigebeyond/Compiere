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


/** XX_Motive AD_Reference_ID=1000450 */
public enum X_Ref_XX_Ref_PriorityMotive 
{
    /** Evento = EV */
    EVENTO("EV"),
    /** Falla de Inventario = FI */
    FALLA_DE_INVENTARIO("FI"),
    /** Folleto = FO */
    FOLLETO("FO"),
    /** Pentacorporativa = PE */
    PENTACORPORATIVA("PE");
    
    public static final int AD_Reference_ID=1000450;
    private final String value;
    private X_Ref_XX_Ref_PriorityMotive(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_XX_Ref_PriorityMotive v : X_Ref_XX_Ref_PriorityMotive.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
