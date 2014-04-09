/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.util;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.*;
import org.compiere.model.*;

/**
 *  Web Environment and debugging
 *
 *  @author Jorg Janke
 *  @version $Id: WebEnv.java 7781 2009-06-05 22:36:55Z freyes $
 */
public class WebEnv
{
	/** Add HTML Debug Info                     */
	public static final boolean DEBUG                 = true;
	/**	Logging									*/
	private static CLogger			log = CLogger.getCLogger(WebEnv.class);

	/**
	 *  Base Directory links <b>http://localhost:8080/compiere</b>
	 *  to the physical <i>%COMPIERE_HOME%/tomcat/webroot/compiere</i> directory
	 */
	public static final String   	DIR_BASE    = "/wstore";      //  /compiere
	/** Image Sub-Directory under BASE          */
	private static final String     DIR_IMAGE   = "images";         //  /compiere/images
	/** Stylesheet Name                         */
	private static final String     STYLE_STD   = "standard.css";   //  /compiere/standard.css
	/** Small Logo.
	/** Removing/modifying the Compiere logo is a violation of the license	*/
	private static final String     LOGO        = "res/LogoSmall.png";  //  /compiere/LogoSmall.gif
	/** Store Sub-Directory under BASE          */
	private static final String     DIR_STORE   = "store";          //  /compiere/store

	/**  Frame name for Commands - WCmd	*/
	public static final String      TARGET_CMD  = "WCmd";
	/**  Frame name for Menu - WMenu	*/
	public static final String      TARGET_MENU = "WMenu";
	/**  Frame name for Apps Window - WWindow	*/
	public static final String      TARGET_WINDOW = "WWindow";
	/**  Frame name for Apps PopUp - WPopUp		*/
	public static final String      TARGET_POPUP = "WPopUp";

	/** Character Set (iso-8859-1 - utf-8) 		*/
	public static final String      CHARSET = "UTF-8";     //  Default: UNKNOWN
	/** Encoding (ISO-8859-1 - UTF-8) 		*/
	public static final String      ENCODING = "UTF-8";
	/** Cookie Name                             */
	public static final String      COOKIE_INFO = "CompiereInfo";

	/** Timeout - 15 Minutes                    */
	public static final int         TIMEOUT     = 15*60;


	/** Initialization OK?                      */
	private static boolean          s_initOK    = false;
	/** Not Braking Space						*/
	public static final String			NBSP = "&nbsp;";
	public static final boolean isMeasurePerf = "y".equals(System.getProperty("MeasurePerf"));

	/**
	 *  Init Web Environment.
	 *  To be called from every Servlet in the init method
	 *  or any other Web resource to make sure that the
	 *  environment is properly set.
	 *  @param config config
	 *  @return false if initialization problems
	 */
	public static synchronized boolean initWeb (ServletConfig config)
	{
		if (s_initOK)
		{
			log.info(config.getServletName());
			return true;
		}
		else
			initWeb (config.getServletContext(), "ServletInit");

		Enumeration<?> en = config.getInitParameterNames();
		StringBuffer info2 = new StringBuffer("Servlet Init Parameter: ")
			.append(config.getServletName());
		while (en.hasMoreElements())
		{
			String name = en.nextElement().toString();
			String value = config.getInitParameter(name);
			System.setProperty(name, value);
			info2.append("\n").append(name).append("=").append(value);
		}

		boolean retValue = initWeb (config.getServletContext(), config.getServletName());
		
		//	Logging now initiated
		log.info(info2.toString());
		return retValue;
	}   //  initWeb

	/**
	 * 	Init Web.
	 * 	Only call directly for Filters, etc.
	 *	@param context servlet context
	 *  @return false if initialization problems
	 */
	public static synchronized boolean initWeb (ServletContext context, String info)
	{
		if (s_initOK)
		{
			log.info(context.getServletContextName());
			return true;
		}
		
		//  Load Environment Variables (serverApps/src/web/WEB-INF/web.xml)
		Enumeration<?> en = context.getInitParameterNames();
		StringBuffer info2 = new StringBuffer("Servlet Context Init Parameters: ")
			.append(context.getServletContextName());
		while (en.hasMoreElements())
		{
			String name = en.nextElement().toString();
			String value = context.getInitParameter(name);
			System.setProperty(name, value);
			info2.append("\n").append(name).append("=").append(value);
		}

		try
		{
			s_initOK = Compiere.startup(false, info);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "startup", ex); 
		}
		if (!s_initOK)
			return false;

		//	Logging now initiated
		log.info(info2.toString());
		//
		Ctx ctx = new Ctx();
		MClient client = MClient.get(ctx, 0);
		MSystem system = MSystem.get(ctx);
		client.sendEMail(client.getRequestEMail(), client.getName(),
			"Server started: " + system.getName(), 
			"ServerInfo: " + context.getServerInfo(), null);

		return s_initOK;
	}	//	initWeb

	
	/**************************************************************************
	 *  Get Base Directory entry.
	 *  <br>
	 *  /compiere/
	 *  @param entry file entry or path
	 *  @return url to entry in base directory
	 */
	public static String getBaseDirectory (String entry)
	{
		StringBuffer sb = new StringBuffer (DIR_BASE);
		if (!entry.startsWith("/"))
			sb.append("/");
		sb.append(entry);
		return sb.toString();
	}   //  getBaseDirectory

	/**
	 *  Get Image Directory entry.
	 *  <br>
	 *  /compiere/images
	 *  @param entry file entry or path
	 *  @return url to entry in image directory
	 */
	public static String getImageDirectory(String entry)
	{
		StringBuffer sb = new StringBuffer (DIR_BASE);
		sb.append("/").append(DIR_IMAGE);
		if (!entry.startsWith("/"))
			sb.append("/");
		sb.append(entry);
		return sb.toString();
	}   //  getImageDirectory

	/**
	 *  Get Store Directory entry.
	 *  <br>
	 *  /compiere/store
	 *  @param entry file entry or path
	 *  @return url to entry in store directory
	 */
	public static String getStoreDirectory(String entry)
	{
		StringBuffer sb = new StringBuffer (DIR_BASE);
		sb.append("/").append(DIR_STORE);
		if (!entry.startsWith("/"))
			sb.append("/");
		sb.append(entry);
		return sb.toString();
	}   //  getStoreDirectory

	/**
	 *  Get Logo Path.
	 *	Removing/modifying the Compiere logo is a violation of the license
	 *  <p>
	 *  /compiere/LogoSmall.gif
	 *  @return url to logo
	 */
	public static String getLogoURL()
	{
		return getBaseDirectory(LOGO);
	}   //  getLogoPath

	/**
	 *  Get Logo Image HTML tag.
	 *	Removing/modifying the Compiere logo or copyright notice is a violation of the license
	 *  @return Image
	 */
	public static img getLogo()
	{
		/** Removing/modifying the Compiere logo is a violation of the license	*/
		return new img(getLogoURL()).setAlign(AlignType.RIGHT)
		//	Changing the copyright notice in any way violates the license 
		//	and you'll be held liable for any damage claims
			.setAlt("&copy; Jorg Janke/Compiere");	
	}   //  getLogo

	/**
	 *  Get Stylesheet Path.
	 *  <p>
	 *  /compiere/standard.css
	 *  @return url of Stylesheet
	 */
	public static String getStylesheetURL()
	{
		return getBaseDirectory(STYLE_STD);
	}   //  getStylesheetURL

	/**
	 * 	Get Cell Content
	 *	@param content optional content
	 *	@return string content or non breaking space
	 */
	public static String getCellContent (Object content)
	{
		if (content == null)
			return NBSP;
		String str = content.toString();
		if (str.length() == 0)
			return NBSP;
		return str;
	}	//	getCellContent

	/**
	 * 	Get Cell Content
	 *	@param content optional content
	 *	@return string content
	 */
	public static String getCellContent (int content)
	{
		return String.valueOf(content);
	}	//	getCellContent

	/**************************************************************************
	 * 	Dump Servlet Config
	 * 	@param config config
	 */
	public static void dump (ServletConfig config)
	{
		log.config("ServletConfig " + config.getServletName());
		log.config("- Context=" + config.getServletContext());
		if (!CLogMgt.isLevelFiner())
			return;
		boolean first = true;
		Enumeration<?> e = config.getInitParameterNames();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("InitParameter:");
			first = false;
			String key = (String)e.nextElement();
			Object value = config.getInitParameter(key);
			log.finer("- " + key + " = " + value);
		}
	}	//	dump (ServletConfig)

	/**
	 * 	Dump Session
	 * 	@param ctx servlet context
	 */
	public static void dump (ServletContext ctx)
	{
		log.config("ServletContext " + ctx.getServletContextName());
		log.config("- ServerInfo=" + ctx.getServerInfo());
		if (!CLogMgt.isLevelFiner())
			return;
		boolean first = true;
		Enumeration<?> e = ctx.getInitParameterNames();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("InitParameter:");
			first = false;
			String key = (String)e.nextElement();
			Object value = ctx.getInitParameter(key);
			log.finer("- " + key + " = " + value);
		}
		first = true;
		e = ctx.getAttributeNames();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("Attributes:");
			first = false;
			String key = (String)e.nextElement();
			Object value = ctx.getAttribute(key);
			log.finer("- " + key + " = " + value);
		}
	}	//	dump

	/**
	 * 	Dump Session
	 * 	@param session session
	 */
	public static void dump (HttpSession session)
	{
		log.config("Session " + session.getId());
		log.config("- Created=" + new Timestamp(session.getCreationTime()));
		if (!CLogMgt.isLevelFiner())
			return;
		boolean first = true;
		Enumeration<?> e = session.getAttributeNames();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("Attributes:");
			first = false;
			String key = (String)e.nextElement();
			Object value = session.getAttribute(key);
			log.finer("- " + key + " = " + value);
		}
	}	//	dump (session)

	/**
	 * 	Dump Request
	 * 	@param request request
	 */
	public static void dump (HttpServletRequest request)
	{
		log.config("Request " + request.getProtocol() + " " + request.getMethod());
		if (!CLogMgt.isLevelFiner())
			return;
		log.finer("- Server="  + request.getServerName() + ", Port=" + request.getServerPort());
		log.finer("- ContextPath=" + request.getContextPath()
			+ ", ServletPath=" + request.getServletPath()
			+ ", Query=" + request.getQueryString());
		log.finer("- From " + request.getRemoteHost() + "/" + request.getRemoteAddr()
			//	+ ":" + request.getRemotePort() 
				+ " - User=" + request.getRemoteUser());
		log.finer("- URI=" + request.getRequestURI() + ", URL=" + request.getRequestURL());
		log.finer("- AuthType=" + request.getAuthType());
		log.finer("- Secure=" + request.isSecure());
		log.finer("- PathInfo=" + request.getPathInfo() + " - " + request.getPathTranslated());
		log.finer("- UserPrincipal=" + request.getUserPrincipal());
		//
		boolean first = true;
		Enumeration<?> e = request.getHeaderNames();
		/** Header Names */
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("- Header:");
			first = false;
			String key = (String)e.nextElement();
			Object value = request.getHeader(key);
			log.finer("  - " + key + " = " + value);
		}
		/** **/
		first = true;
		/** Parameter	*/
		try
		{
			String enc = request.getCharacterEncoding();
			if (enc == null)
				request.setCharacterEncoding(WebEnv.ENCODING);
		}
		catch (Exception ee)
		{
			log.log(Level.SEVERE, "Set CharacterEncoding=" + WebEnv.ENCODING, ee);
		}
		e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("- Parameter:");
			first = false;
			String key = (String)e.nextElement();
			String value = WebUtil.getParameter (request, key);
			log.finer("  - " + key + " = " + value);
		}
		first = true;
		/** Attributes	*/
		e = request.getAttributeNames();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("- Attributes:");
			first = false;
			String key = (String)e.nextElement();
			Object value = request.getAttribute(key);
			log.finer("  - " + key + " = " + value);
		}
		/** Cookies	*/
		Cookie[] ccc = request.getCookies();
		if (ccc != null)
		{
			for (int i = 0; i < ccc.length; i++)
			{
				if (i == 0)
					log.finer("- Cookies:");
				log.finer ("  - " + ccc[i].getName ()
					+ ", Domain=" + ccc[i].getDomain ()
					+ ", Path=" + ccc[i].getPath ()
					+ ", MaxAge=" + ccc[i].getMaxAge ());
			}
		}
		log.finer("- Encoding=" + request.getCharacterEncoding());
		log.finer("- Locale=" + request.getLocale());
		first = true;
		e = request.getLocales();
		while (e.hasMoreElements())
		{
			if (first)
				log.finer("- Locales:");
			first = false;
			log.finer("  - " + e.nextElement());
		}
		log.finer("- Class=" + request.getClass().getName());
	}	//	dump (Request)

	
	/**************************************************************************
	 *  Add Footer (with diagnostics)
	 *  @param request request
	 *  @param response response
	 *  @param servlet servlet
	 *  @param body - Body to add footer
	 */
	public static void addFooter(HttpServletRequest request, HttpServletResponse response,
		HttpServlet servlet, body body)
	{
		body.addElement(new hr());
		body.addElement(new comment(" --- Footer Start --- "));
		//  Command Line
		p footer = new p();
		footer.addElement(org.compiere.Compiere.DATE_VERSION + ": ");
		footer.addElement(new a("javascript:diag_window();", "Window Info"));
		footer.addElement(" - ");
		footer.addElement(new a("javascript:diag_navigator();", "Browser Info"));
		footer.addElement(" - ");
		footer.addElement(new a("javascript:diag_request();", "Request Info"));
		footer.addElement(" - ");
		footer.addElement(new a("javascript:diag_document();", "Document Info"));
		footer.addElement(" - ");
		footer.addElement(new a("javascript:diag_form();", "Form Info"));
		footer.addElement(" - ");
		footer.addElement(new a("javascript:toggle('DEBUG');", "Servlet Info"));
		footer.addElement(" - ");
		footer.addElement(new a("javascript:diag_source();", "Show Source"));
		footer.addElement("\n");
		body.addElement(footer);

		//  Add ServletInfo
		body.addElement(new br());
		body.addElement(getServletInfo(request, response, servlet));
		body.addElement(new script("hide('DEBUG');"));
		body.addElement(new comment(" --- Footer End --- "));
	}   //  getFooter

	/**
	 *	Get Information and put it in a HTML table
	 *  @param request request
	 *  @param response response
	 *  @param servlet servlet
	 *  @return Table
	 */
	private static table getServletInfo (HttpServletRequest request,
		HttpServletResponse response, HttpServlet servlet)
	{
		table table = new table();
		table.setID("DEBUG");
		Enumeration<?> e;

		tr space = new tr().addElement(new td().addElement("."));
		//	Request Info
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Request Info")) ));
		table.addElement(new tr().addElement(new td().addElement("Method"))
									.addElement(new td().addElement(request.getMethod() )));
		table.addElement(new tr().addElement(new td().addElement("Protocol"))
									.addElement(new td().addElement(request.getProtocol() )));
		table.addElement(new tr().addElement(new td().addElement("URI"))
									.addElement(new td().addElement(request.getRequestURI() )));
		table.addElement(new tr().addElement(new td().addElement("Context Path"))
									.addElement(new td().addElement(request.getContextPath() )));
		table.addElement(new tr().addElement(new td().addElement("Servlet Path"))
									.addElement(new td().addElement(request.getServletPath() )));
		table.addElement(new tr().addElement(new td().addElement("Path Info"))
									.addElement(new td().addElement(request.getPathInfo() )));
		table.addElement(new tr().addElement(new td().addElement("Path Translated"))
									.addElement(new td().addElement(request.getPathTranslated() )));
		table.addElement(new tr().addElement(new td().addElement("Query String"))
									.addElement(new td().addElement(request.getQueryString() )));
		table.addElement(new tr().addElement(new td().addElement("Content Length"))
									.addElement(new td().addElement("" + request.getContentLength() )));
		table.addElement(new tr().addElement(new td().addElement("Content Type"))
									.addElement(new td().addElement(request.getContentType() )));
		table.addElement(new tr().addElement(new td().addElement("Character Encoding"))
									.addElement(new td().addElement(request.getCharacterEncoding() )));
		table.addElement(new tr().addElement(new td().addElement("Locale"))
									.addElement(new td().addElement(request.getLocale().toString() )));
		table.addElement(new tr().addElement(new td().addElement("Schema"))
									.addElement(new td().addElement(request.getScheme() )));
		table.addElement(new tr().addElement(new td().addElement("Server Name"))
									.addElement(new td().addElement(request.getServerName() )));
		table.addElement(new tr().addElement(new td().addElement("Server Port"))
									.addElement(new td().addElement("" + request.getServerPort() )));
		table.addElement(new tr().addElement(new td().addElement("Remote User"))
									.addElement(new td().addElement(request.getRemoteUser() )));
		table.addElement(new tr().addElement(new td().addElement("Remote Address"))
									.addElement(new td().addElement(request.getRemoteAddr() )));
		table.addElement(new tr().addElement(new td().addElement("Remote Host"))
									.addElement(new td().addElement(request.getRemoteHost() )));
		table.addElement(new tr().addElement(new td().addElement("Authorization Type"))
									.addElement(new td().addElement(request.getAuthType() )));
		table.addElement(new tr().addElement(new td().addElement("User Principal"))
									.addElement(new td().addElement(request.getUserPrincipal()==null ? "" : request.getUserPrincipal().toString())));
		table.addElement(new tr().addElement(new td().addElement("IsSecure"))
									.addElement(new td().addElement(request.isSecure() ? "true" : "false" )));

		//	Request Attributes
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Request Attributes")) ));
		e = request.getAttributeNames();
		while (e.hasMoreElements())
		{
			String name = e.nextElement().toString();
			String attrib = request.getAttribute(name).toString();
			table.addElement(new tr().addElement(new td().addElement(name))
										.addElement(new td().addElement(attrib)));
		}

		//	Request Parameter
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Req Parameters")) ));
		try
		{
			String enc = request.getCharacterEncoding();
			if (enc == null)
				request.setCharacterEncoding(WebEnv.ENCODING);
		}
		catch (Exception ee)
		{
			log.log(Level.SEVERE, "Set CharacterEncoding=" + WebEnv.ENCODING, ee);
		}
		e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			String name = (String)e.nextElement();
			String para = WebUtil.getParameter (request, name);
			table.addElement(new tr().addElement(new td().addElement(name))
										.addElement(new td().addElement(para)));
		}

		//	Request Header
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Req Header")) ));
		e = request.getHeaderNames();
		while (e.hasMoreElements())
		{
			String name = (String)e.nextElement();
			if (!name.equals("Cockie"))
			{
				String hdr = request.getHeader(name);
				table.addElement(new tr().addElement(new td().addElement(name))
											.addElement(new td().addElement(hdr)));
			}
		}

		//  Request Cookies
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Req Cookies")) ));
		Cookie[] cc = request.getCookies();
		if (cc != null)
		{
			for (Cookie element : cc) {
				//	Name and Comment
				table.addElement(new tr().addElement(new td().addElement(element.getName() ))
											.addElement(new td().addElement(element.getValue()) ));
				table.addElement(new tr().addElement(new td().addElement(element.getName()+": Comment" ))
											.addElement(new td().addElement(element.getComment()) ));
				table.addElement(new tr().addElement(new td().addElement(element.getName()+": Domain" ))
											.addElement(new td().addElement(element.getDomain()) ));
				table.addElement(new tr().addElement(new td().addElement(element.getName()+": Max Age" ))
											.addElement(new td().addElement(""+ element.getMaxAge()) ));
				table.addElement(new tr().addElement(new td().addElement(element.getName()+": Path" ))
											.addElement(new td().addElement(element.getPath()) ));
				table.addElement(new tr().addElement(new td().addElement(element.getName()+": Is Secure" ))
											.addElement(new td().addElement(element.getSecure() ? "true" : "false") ));
				table.addElement(new tr().addElement(new td().addElement(element.getName()+": Version" ))
											.addElement(new td().addElement("" + element.getVersion()) ));
			}
		}	//	Cookies

		//  Request Session Info
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Req Session")) ));
		HttpSession session = request.getSession(true);
		table.addElement(new tr().addElement(new td().addElement("Session ID"))
									.addElement(new td().addElement(session.getId() )));
		Timestamp ts = new Timestamp(session.getCreationTime());
		table.addElement(new tr().addElement(new td().addElement("Created"))
									.addElement(new td().addElement(ts.toString() )));
		ts = new Timestamp(session.getLastAccessedTime());
		table.addElement(new tr().addElement(new td().addElement("Accessed"))
									.addElement(new td().addElement(ts.toString() )));
		table.addElement(new tr().addElement(new td().addElement("Request Session ID"))
									.addElement(new td().addElement(request.getRequestedSessionId() )));
		table.addElement(new tr().addElement(new td().addElement(".. via Cookie"))
									.addElement(new td().addElement("" + request.isRequestedSessionIdFromCookie() )));
		table.addElement(new tr().addElement(new td().addElement(".. via URL"))
									.addElement(new td().addElement("" + request.isRequestedSessionIdFromURL() )));
		table.addElement(new tr().addElement(new td().addElement("Valid"))
									.addElement(new td().addElement("" + request.isRequestedSessionIdValid() )));

		//	Request Session Attributes
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Session Attributes")) ));
		e = session.getAttributeNames();
		while (e.hasMoreElements())
		{
			String name = (String)e.nextElement();
			String attrib = session.getAttribute(name).toString();
			table.addElement(new tr().addElement(new td().addElement(name))
										.addElement(new td().addElement(attrib)));
		}

		//	Response Info
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Response")) ));
		table.addElement(new tr().addElement(new td().addElement("Buffer Size"))
									.addElement(new td().addElement(String.valueOf(response.getBufferSize()) )));
		table.addElement(new tr().addElement(new td().addElement("Character Encoding"))
									.addElement(new td().addElement(response.getCharacterEncoding() )));
		table.addElement(new tr().addElement(new td().addElement("Locale"))
									.addElement(new td().addElement(response.getLocale()==null ? "null" : response.getLocale().toString())));

		//  Servlet
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Servlet")) ));
		table.addElement(new tr().addElement(new td().addElement("Name"))
										.addElement(new td().addElement(servlet.getServletName())));
		table.addElement(new tr().addElement(new td().addElement("Info"))
										.addElement(new td().addElement(servlet.getServletInfo())));

		//  Servlet Init
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Servlet Init Parameter")) ));
		e = servlet.getInitParameterNames();
		//  same as:  servlet.getServletConfig().getInitParameterNames();
		while (e.hasMoreElements())
		{
			String name = (String)e.nextElement();
			String para = servlet.getInitParameter(name);
			table.addElement(new tr().addElement(new td().addElement(name))
										.addElement(new td().addElement(para)));
		}

		//  Servlet Context
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Servlet Context")) ));
		ServletContext servCtx = servlet.getServletContext();
		e = servCtx.getAttributeNames();
		while (e.hasMoreElements())
		{
			String name = (String)e.nextElement();
			String attrib = servCtx.getAttribute(name).toString();
			table.addElement(new tr().addElement(new td().addElement(name))
										.addElement(new td().addElement(attrib)));
		}

		//  Servlet Context
		table.addElement(space);
		table.addElement(new tr().addElement(new td().addElement(new h3("Servlet Context Init Parameter")) ));
		e = servCtx.getInitParameterNames();
		while (e.hasMoreElements())
		{
			String name = (String)e.nextElement();
			String attrib = servCtx.getInitParameter(name);
			table.addElement(new tr().addElement(new td().addElement(name))
										.addElement(new td().addElement(attrib)));
		}

		/*	*/
		return table;
	}	//	getServletInfo

}   //  WEnv
