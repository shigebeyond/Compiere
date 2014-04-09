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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_Element
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_Element extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_Element_ID id
    @param trx transaction
    */
    public X_XX_VME_Element (Ctx ctx, int XX_VME_Element_ID, Trx trx)
    {
        super (ctx, XX_VME_Element_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_Element_ID == 0)
        {
            setXX_VMA_BrochurePage_ID (0);
            setXX_VME_Element_ID (0);
            setXX_VME_Type (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_Element (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27603410984789L;
    /** Last Updated Timestamp 2011-11-14 11:17:48.0 */
    public static final long updatedMS = 1321285668000L;
    /** AD_Table_ID=1000434 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_Element");
        
    }
    ;
    
    /** TableName=XX_VME_Element */
    public static final String Table_Name="XX_VME_Element";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Image.
    @param AD_Image_ID Image or Icon */
    public void setAD_Image_ID (int AD_Image_ID)
    {
        if (AD_Image_ID <= 0) set_Value ("AD_Image_ID", null);
        else
        set_Value ("AD_Image_ID", Integer.valueOf(AD_Image_ID));
        
    }
    
    /** Get Image.
    @return Image or Icon */
    public int getAD_Image_ID() 
    {
        return get_ValueAsInt("AD_Image_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
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
    
    /** Set Max Amount.
    @param MaxAmt Maximum Amount in invoice currency */
    public void setMaxAmt (java.math.BigDecimal MaxAmt)
    {
        set_Value ("MaxAmt", MaxAmt);
        
    }
    
    /** Get Max Amount.
    @return Maximum Amount in invoice currency */
    public java.math.BigDecimal getMaxAmt() 
    {
        return get_ValueAsBigDecimal("MaxAmt");
        
    }
    
    /** Set Min Amount.
    @param MinAmt Minimum Amount in invoice currency */
    public void setMinAmt (java.math.BigDecimal MinAmt)
    {
        set_Value ("MinAmt", MinAmt);
        
    }
    
    /** Get Min Amount.
    @return Minimum Amount in invoice currency */
    public java.math.BigDecimal getMinAmt() 
    {
        return get_ValueAsBigDecimal("MinAmt");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        set_Value ("Price", Price);
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
    }
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        set_Value ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** Set Price.
    @param PriceEntered Price Entered - the price based on the selected/base UoM */
    public void setPriceEntered (java.math.BigDecimal PriceEntered)
    {
        set_Value ("PriceEntered", PriceEntered);
        
    }
    
    /** Get Price.
    @return Price Entered - the price based on the selected/base UoM */
    public java.math.BigDecimal getPriceEntered() 
    {
        return get_ValueAsBigDecimal("PriceEntered");
        
    }
    
    /** Set List Price.
    @param PriceList List Price */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        set_Value ("PriceList", PriceList);
        
    }
    
    /** Get List Price.
    @return List Price */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
    }
    
    /** Set Standard Price.
    @param PriceStd Standard Price */
    public void setPriceStd (java.math.BigDecimal PriceStd)
    {
        set_Value ("PriceStd", PriceStd);
        
    }
    
    /** Get Standard Price.
    @return Standard Price */
    public java.math.BigDecimal getPriceStd() 
    {
        return get_ValueAsBigDecimal("PriceStd");
        
    }
    
    /** Set Priority.
    @param PriorityRule Priority of a document */
    public void setPriorityRule (boolean PriorityRule)
    {
        set_Value ("PriorityRule", Boolean.valueOf(PriorityRule));
        
    }
    
    /** Get Priority.
    @return Priority of a document */
    public boolean isPriorityRule() 
    {
        return get_ValueAsBoolean("PriorityRule");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_ValueNoCheck ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set Available Quantity.
    @param QtyAvailable Available Quantity (On Hand - Reserved) */
    public void setQtyAvailable (java.math.BigDecimal QtyAvailable)
    {
        set_Value ("QtyAvailable", QtyAvailable);
        
    }
    
    /** Get Available Quantity.
    @return Available Quantity (On Hand - Reserved) */
    public java.math.BigDecimal getQtyAvailable() 
    {
        return get_ValueAsBigDecimal("QtyAvailable");
        
    }
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        set_Value ("QtyEntered", QtyEntered);
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
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
    
    /** Set Delete Group of Products.
    @param XX_DeleteGoP Delete Group of Products */
    public void setXX_DeleteGoP (String XX_DeleteGoP)
    {
        set_Value ("XX_DeleteGoP", XX_DeleteGoP);
        
    }
    
    /** Get Delete Group of Products.
    @return Delete Group of Products */
    public String getXX_DeleteGoP() 
    {
        return (String)get_Value("XX_DeleteGoP");
        
    }
    
    /** Set Samples Quantity.
    @param XX_SamplesQty Samples Quantity */
    public void setXX_SamplesQty (int XX_SamplesQty)
    {
        set_Value ("XX_SamplesQty", Integer.valueOf(XX_SamplesQty));
        
    }
    
    /** Get Samples Quantity.
    @return Samples Quantity */
    public int getXX_SamplesQty() 
    {
        return get_ValueAsInt("XX_SamplesQty");
        
    }
    
    /** Set Brochure Page.
    @param XX_VMA_BrochurePage_ID Identifier of the Brochure Page. */
    public void setXX_VMA_BrochurePage_ID (int XX_VMA_BrochurePage_ID)
    {
        if (XX_VMA_BrochurePage_ID < 1) throw new IllegalArgumentException ("XX_VMA_BrochurePage_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_BrochurePage_ID", Integer.valueOf(XX_VMA_BrochurePage_ID));
        
    }
    
    /** Get Brochure Page.
    @return Identifier of the Brochure Page. */
    public int getXX_VMA_BrochurePage_ID() 
    {
        return get_ValueAsInt("XX_VMA_BrochurePage_ID");
        
    }
    
    /** Set Final Inventory.
    @param XX_VMA_FinalInventory Final Inventory */
    public void setXX_VMA_FinalInventory (int XX_VMA_FinalInventory)
    {
        set_Value ("XX_VMA_FinalInventory", Integer.valueOf(XX_VMA_FinalInventory));
        
    }
    
    /** Get Final Inventory.
    @return Final Inventory */
    public int getXX_VMA_FinalInventory() 
    {
        return get_ValueAsInt("XX_VMA_FinalInventory");
        
    }
    
    /** Set Initial Amount.
    @param XX_VMA_InitialAmount Initial Amount */
    public void setXX_VMA_InitialAmount (java.math.BigDecimal XX_VMA_InitialAmount)
    {
        set_Value ("XX_VMA_InitialAmount", XX_VMA_InitialAmount);
        
    }
    
    /** Get Initial Amount.
    @return Initial Amount */
    public java.math.BigDecimal getXX_VMA_InitialAmount() 
    {
        return get_ValueAsBigDecimal("XX_VMA_InitialAmount");
        
    }
    
    /** Set Initial Inventory.
    @param XX_VMA_InitialInventory Initial Inventory */
    public void setXX_VMA_InitialInventory (int XX_VMA_InitialInventory)
    {
        set_Value ("XX_VMA_InitialInventory", Integer.valueOf(XX_VMA_InitialInventory));
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public int getXX_VMA_InitialInventory() 
    {
        return get_ValueAsInt("XX_VMA_InitialInventory");
        
    }
    
    /** Set PO Consult.
    @param XX_VMA_POConsultForm PO Consult */
    public void setXX_VMA_POConsultForm (String XX_VMA_POConsultForm)
    {
        set_Value ("XX_VMA_POConsultForm", XX_VMA_POConsultForm);
        
    }
    
    /** Get PO Consult.
    @return PO Consult */
    public String getXX_VMA_POConsultForm() 
    {
        return (String)get_Value("XX_VMA_POConsultForm");
        
    }
    
    /** Set Add Image.
    @param XX_VME_AddImage Determines if the user wants to associate an Image. */
    public void setXX_VME_AddImage (boolean XX_VME_AddImage)
    {
        set_Value ("XX_VME_AddImage", Boolean.valueOf(XX_VME_AddImage));
        
    }
    
    /** Get Add Image.
    @return Determines if the user wants to associate an Image. */
    public boolean isXX_VME_AddImage() 
    {
        return get_ValueAsBoolean("XX_VME_AddImage");
        
    }
    
    /** Set Add Vendor Reference.
    @param XX_VME_AddVendorReference Add Vendor Reference */
    public void setXX_VME_AddVendorReference (String XX_VME_AddVendorReference)
    {
        set_Value ("XX_VME_AddVendorReference", XX_VME_AddVendorReference);
        
    }
    
    /** Get Add Vendor Reference.
    @return Add Vendor Reference */
    public String getXX_VME_AddVendorReference() 
    {
        return (String)get_Value("XX_VME_AddVendorReference");
        
    }
    
    /** Set Change Element to Page.
    @param XX_VME_ChangeElementPage Change the element to another page inside the brochure. */
    public void setXX_VME_ChangeElementPage (String XX_VME_ChangeElementPage)
    {
        set_Value ("XX_VME_ChangeElementPage", XX_VME_ChangeElementPage);
        
    }
    
    /** Get Change Element to Page.
    @return Change the element to another page inside the brochure. */
    public String getXX_VME_ChangeElementPage() 
    {
        return (String)get_Value("XX_VME_ChangeElementPage");
        
    }
    
    /** Set Product characteristics to publish.
    @param XX_VME_CharactPublished Product characteristics to publish */
    public void setXX_VME_CharactPublished (String XX_VME_CharactPublished)
    {
        set_Value ("XX_VME_CharactPublished", XX_VME_CharactPublished);
        
    }
    
    /** Get Product characteristics to publish.
    @return Product characteristics to publish */
    public String getXX_VME_CharactPublished() 
    {
        return (String)get_Value("XX_VME_CharactPublished");
        
    }
    
    /** Set Element.
    @param XX_VME_Element_ID Is the identifier of the element used for Marketing Activities in Compiere. */
    public void setXX_VME_Element_ID (int XX_VME_Element_ID)
    {
        if (XX_VME_Element_ID < 1) throw new IllegalArgumentException ("XX_VME_Element_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_Element_ID", Integer.valueOf(XX_VME_Element_ID));
        
    }
    
    /** Get Element.
    @return Is the identifier of the element used for Marketing Activities in Compiere. */
    public int getXX_VME_Element_ID() 
    {
        return get_ValueAsInt("XX_VME_Element_ID");
        
    }
    
    /** Set Final Inventory.
    @param XX_VME_InvFin Final inventory  at the end of the marketing activity. */
    public void setXX_VME_InvFin (java.math.BigDecimal XX_VME_InvFin)
    {
        set_Value ("XX_VME_InvFin", XX_VME_InvFin);
        
    }
    
    /** Get Final Inventory.
    @return Final inventory  at the end of the marketing activity. */
    public java.math.BigDecimal getXX_VME_InvFin() 
    {
        return get_ValueAsBigDecimal("XX_VME_InvFin");
        
    }
    
    /** Set Initial Inventory.
    @param XX_VME_InvIni Initial inventory at the beginning of the marketing activity */
    public void setXX_VME_InvIni (java.math.BigDecimal XX_VME_InvIni)
    {
        set_Value ("XX_VME_InvIni", XX_VME_InvIni);
        
    }
    
    /** Get Initial Inventory.
    @return Initial inventory at the beginning of the marketing activity */
    public java.math.BigDecimal getXX_VME_InvIni() 
    {
        return get_ValueAsBigDecimal("XX_VME_InvIni");
        
    }
    
    /** Set Is Basic.
    @param XX_VME_IsBasic Establishes if the element of  a Marketing Activity is Basic or not. */
    public void setXX_VME_IsBasic (boolean XX_VME_IsBasic)
    {
        set_Value ("XX_VME_IsBasic", Boolean.valueOf(XX_VME_IsBasic));
        
    }
    
    /** Get Is Basic.
    @return Establishes if the element of  a Marketing Activity is Basic or not. */
    public boolean isXX_VME_IsBasic() 
    {
        return get_ValueAsBoolean("XX_VME_IsBasic");
        
    }
    
    /** Set Is Element Active.
    @param XX_VME_IsElementActive The element is active or not */
    public void setXX_VME_IsElementActive (boolean XX_VME_IsElementActive)
    {
        set_Value ("XX_VME_IsElementActive", Boolean.valueOf(XX_VME_IsElementActive));
        
    }
    
    /** Get Is Element Active.
    @return The element is active or not */
    public boolean isXX_VME_IsElementActive() 
    {
        return get_ValueAsBoolean("XX_VME_IsElementActive");
        
    }
    
    /** Set Is Oportunity.
    @param XX_VME_IsOportunity Establishes if the element of  a Marketing Activity is of Oportunity or not. */
    public void setXX_VME_IsOportunity (boolean XX_VME_IsOportunity)
    {
        set_Value ("XX_VME_IsOportunity", Boolean.valueOf(XX_VME_IsOportunity));
        
    }
    
    /** Get Is Oportunity.
    @return Establishes if the element of  a Marketing Activity is of Oportunity or not. */
    public boolean isXX_VME_IsOportunity() 
    {
        return get_ValueAsBoolean("XX_VME_IsOportunity");
        
    }
    
    /** Set Is Star.
    @param XX_VME_IsStar Establishes if the element of  a Marketing Activity is Star or not. */
    public void setXX_VME_IsStar (boolean XX_VME_IsStar)
    {
        set_Value ("XX_VME_IsStar", Boolean.valueOf(XX_VME_IsStar));
        
    }
    
    /** Get Is Star.
    @return Establishes if the element of  a Marketing Activity is Star or not. */
    public boolean isXX_VME_IsStar() 
    {
        return get_ValueAsBoolean("XX_VME_IsStar");
        
    }
    
    /** Set Is Tendence.
    @param XX_VME_IsTendence Establishes if the element of  a Marketing Activity is of Tendence or not. */
    public void setXX_VME_IsTendence (boolean XX_VME_IsTendence)
    {
        set_Value ("XX_VME_IsTendence", Boolean.valueOf(XX_VME_IsTendence));
        
    }
    
    /** Get Is Tendence.
    @return Establishes if the element of  a Marketing Activity is of Tendence or not. */
    public boolean isXX_VME_IsTendence() 
    {
        return get_ValueAsBoolean("XX_VME_IsTendence");
        
    }
    
    /** Set Final Amount.
    @param XX_VME_MontFin Final amount of the inventory at the end of the marketing activity.  */
    public void setXX_VME_MontFin (java.math.BigDecimal XX_VME_MontFin)
    {
        set_Value ("XX_VME_MontFin", XX_VME_MontFin);
        
    }
    
    /** Get Final Amount.
    @return Final amount of the inventory at the end of the marketing activity.  */
    public java.math.BigDecimal getXX_VME_MontFin() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontFin");
        
    }
    
    /** Set Initial Amount.
    @param XX_VME_MontIni Initial amount of the inventory at the beginning of the marketing activity. */
    public void setXX_VME_MontIni (java.math.BigDecimal XX_VME_MontIni)
    {
        set_Value ("XX_VME_MontIni", XX_VME_MontIni);
        
    }
    
    /** Get Initial Amount.
    @return Initial amount of the inventory at the beginning of the marketing activity. */
    public java.math.BigDecimal getXX_VME_MontIni() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontIni");
        
    }
    
    /** Set Number of elements.
    @param XX_VME_NumElem The number of element asociated to a group of products */
    public void setXX_VME_NumElem (int XX_VME_NumElem)
    {
        set_Value ("XX_VME_NumElem", Integer.valueOf(XX_VME_NumElem));
        
    }
    
    /** Get Number of elements.
    @return The number of element asociated to a group of products */
    public int getXX_VME_NumElem() 
    {
        return get_ValueAsInt("XX_VME_NumElem");
        
    }
    
    /** Set Product without code.
    @param XX_VME_ProductWithoutCode_ID Identifier of the Product without code in Compiere. */
    public void setXX_VME_ProductWithoutCode_ID (int XX_VME_ProductWithoutCode_ID)
    {
        if (XX_VME_ProductWithoutCode_ID <= 0) set_Value ("XX_VME_ProductWithoutCode_ID", null);
        else
        set_Value ("XX_VME_ProductWithoutCode_ID", Integer.valueOf(XX_VME_ProductWithoutCode_ID));
        
    }
    
    /** Get Product without code.
    @return Identifier of the Product without code in Compiere. */
    public int getXX_VME_ProductWithoutCode_ID() 
    {
        return get_ValueAsInt("XX_VME_ProductWithoutCode_ID");
        
    }
    
    /** Set Publicity Contribution.
    @param XX_VME_PublicityContribution Is the amount invested by the Product inside the publicitary investment. */
    public void setXX_VME_PublicityContribution (java.math.BigDecimal XX_VME_PublicityContribution)
    {
        set_Value ("XX_VME_PublicityContribution", XX_VME_PublicityContribution);
        
    }
    
    /** Get Publicity Contribution.
    @return Is the amount invested by the Product inside the publicitary investment. */
    public java.math.BigDecimal getXX_VME_PublicityContribution() 
    {
        return get_ValueAsBigDecimal("XX_VME_PublicityContribution");
        
    }
    
    /** Set Cantidad en CD.
    @param XX_VME_QtyCD Cantidad en CD */
    public void setXX_VME_QtyCD (int XX_VME_QtyCD)
    {
        set_Value ("XX_VME_QtyCD", Integer.valueOf(XX_VME_QtyCD));
        
    }
    
    /** Get Cantidad en CD.
    @return Cantidad en CD */
    public int getXX_VME_QtyCD() 
    {
        return get_ValueAsInt("XX_VME_QtyCD");
        
    }
    
    /** Set Dynamic quantity to publish.
    @param XX_VME_QTYPUBLISHED Dynamic quantity to publish */
    public void setXX_VME_QTYPUBLISHED (java.math.BigDecimal XX_VME_QTYPUBLISHED)
    {
        set_Value ("XX_VME_QTYPUBLISHED", XX_VME_QTYPUBLISHED);
        
    }
    
    /** Get Dynamic quantity to publish.
    @return Dynamic quantity to publish */
    public java.math.BigDecimal getXX_VME_QTYPUBLISHED() 
    {
        return get_ValueAsBigDecimal("XX_VME_QTYPUBLISHED");
        
    }
    
    /** Set Return.
    @param XX_VME_Return Return */
    public void setXX_VME_Return (boolean XX_VME_Return)
    {
        set_Value ("XX_VME_Return", Boolean.valueOf(XX_VME_Return));
        
    }
    
    /** Get Return.
    @return Return */
    public boolean isXX_VME_Return() 
    {
        return get_ValueAsBoolean("XX_VME_Return");
        
    }
    
    /** Set Samples delivered.
    @param XX_VME_SamplesDelivered Samples delivered */
    public void setXX_VME_SamplesDelivered (boolean XX_VME_SamplesDelivered)
    {
        set_Value ("XX_VME_SamplesDelivered", Boolean.valueOf(XX_VME_SamplesDelivered));
        
    }
    
    /** Get Samples delivered.
    @return Samples delivered */
    public boolean isXX_VME_SamplesDelivered() 
    {
        return get_ValueAsBoolean("XX_VME_SamplesDelivered");
        
    }
    
    /** G - Group of products = G */
    public static final String XX_VME_TYPE_G_GroupOfProducts = X_Ref_XX_VME_Type.G__GROUP_OF_PRODUCTS.getValue();
    /** I - Image = I */
    public static final String XX_VME_TYPE_I_Image = X_Ref_XX_VME_Type.I__IMAGE.getValue();
    /** N - Product without code = N */
    public static final String XX_VME_TYPE_N_ProductWithoutCode = X_Ref_XX_VME_Type.N__PRODUCT_WITHOUT_CODE.getValue();
    /** P - Product = P */
    public static final String XX_VME_TYPE_P_Product = X_Ref_XX_VME_Type.P__PRODUCT.getValue();
    /** R - Vendor Reference = R */
    public static final String XX_VME_TYPE_R_VendorReference = X_Ref_XX_VME_Type.R__VENDOR_REFERENCE.getValue();
    /** Set Type.
    @param XX_VME_Type Is the type of the element contained in a Marketing Activity. */
    public void setXX_VME_Type (String XX_VME_Type)
    {
        if (XX_VME_Type == null) throw new IllegalArgumentException ("XX_VME_Type is mandatory");
        if (!X_Ref_XX_VME_Type.isValid(XX_VME_Type))
        throw new IllegalArgumentException ("XX_VME_Type Invalid value - " + XX_VME_Type + " - Reference_ID=1000332 - G - I - N - P - R");
        set_ValueNoCheck ("XX_VME_Type", XX_VME_Type);
        
    }
    
    /** Get Type.
    @return Is the type of the element contained in a Marketing Activity. */
    public String getXX_VME_Type() 
    {
        return (String)get_Value("XX_VME_Type");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
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
    
    /** Set PriceConsecutive.
    @param XX_VMR_PriceConsecutive_ID PriceConsecutive */
    public void setXX_VMR_PriceConsecutive_ID (int XX_VMR_PriceConsecutive_ID)
    {
        if (XX_VMR_PriceConsecutive_ID <= 0) set_Value ("XX_VMR_PriceConsecutive_ID", null);
        else
        set_Value ("XX_VMR_PriceConsecutive_ID", Integer.valueOf(XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_VMR_PriceConsecutive_ID");
        
    }
    
    /** Set Sumary Product Characteristics.
    @param XX_VMR_ProdCharacteristics Sumary Product Characteristics */
    public void setXX_VMR_ProdCharacteristics (String XX_VMR_ProdCharacteristics)
    {
        throw new IllegalArgumentException ("XX_VMR_ProdCharacteristics is virtual column");
        
    }
    
    /** Get Sumary Product Characteristics.
    @return Sumary Product Characteristics */
    public String getXX_VMR_ProdCharacteristics() 
    {
        return (String)get_Value("XX_VMR_ProdCharacteristics");
        
    }
    
    /** Set First set of product characteristic.
    @param XX_VMR_ProductCharacteristics1 First set of product characteristic */
    public void setXX_VMR_ProductCharacteristics1 (String XX_VMR_ProductCharacteristics1)
    {
        set_Value ("XX_VMR_ProductCharacteristics1", XX_VMR_ProductCharacteristics1);
        
    }
    
    /** Get First set of product characteristic.
    @return First set of product characteristic */
    public String getXX_VMR_ProductCharacteristics1() 
    {
        return (String)get_Value("XX_VMR_ProductCharacteristics1");
        
    }
    
    /** Set Second set of product characteristic.
    @param XX_VMR_ProductCharacteristics2 Second set of product characteristic */
    public void setXX_VMR_ProductCharacteristics2 (String XX_VMR_ProductCharacteristics2)
    {
        set_Value ("XX_VMR_ProductCharacteristics2", XX_VMR_ProductCharacteristics2);
        
    }
    
    /** Get Second set of product characteristic.
    @return Second set of product characteristic */
    public String getXX_VMR_ProductCharacteristics2() 
    {
        return (String)get_Value("XX_VMR_ProductCharacteristics2");
        
    }
    
    /** Set Third set of product characteristic.
    @param XX_VMR_ProductCharacteristics3 Third set of product characteristic */
    public void setXX_VMR_ProductCharacteristics3 (String XX_VMR_ProductCharacteristics3)
    {
        set_Value ("XX_VMR_ProductCharacteristics3", XX_VMR_ProductCharacteristics3);
        
    }
    
    /** Get Third set of product characteristic.
    @return Third set of product characteristic */
    public String getXX_VMR_ProductCharacteristics3() 
    {
        return (String)get_Value("XX_VMR_ProductCharacteristics3");
        
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
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
