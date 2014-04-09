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
package org.compiere.translate;

import java.util.*;

/**
 * Setup Resources
 * 
 * @version $Id: SetupRes_zh_CN.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_zh_CN extends ListResourceBundle
{

	/** Translation Info */
	static final Object[][]	contents	= new String[][] {
		{ "CompiereServerSetup", "Compiere \u670d\u52a1\u5668\u8bbe\u7f6e"},
		{ "Ok", "\u786e\u5b9a"},
		{ "File", "\u6587\u4ef6"},
		{ "Exit", "\u9000\u51fa"},
		{ "Help", "\u5e2e\u52a9"},
		{ "PleaseCheck", "\u8bf7\u68c0\u67e5"},
		{ "UnableToConnect",
		"\u65e0\u6cd5\u4ece Compiere \u7f51\u7ad9\u83b7\u5f97\u5e2e\u52a9\u4fe1\u606f"},
		{ "CompiereHomeInfo", "Compiere \u4e3b\u76ee\u5f55"},
		{ "CompiereHome", "Compiere \u4e3b\u76ee\u5f55"},
		{ "WebPortInfo", "Web (HTML) \u8fde\u63a5\u7aef\u53e3"},
		{ "WebPort", "Web \u7aef\u53e3"},
		{ "AppsServerInfo", "\u5e94\u7528\u670d\u52a1\u5668\u540d\u79f0"},
		{ "AppsServer", "\u5e94\u7528\u670d\u52a1\u5668"},
		{ "DatabaseTypeInfo", "\u6570\u636e\u5e93\u7c7b\u578b"},
		{ "DatabaseType", "\u6570\u636e\u5e93\u7c7b\u578b"},
		{ "DatabaseNameInfo", "\u6570\u636e\u5e93\u540d\u79f0 "},
		{ "DatabaseName", "\u6570\u636e\u5e93\u540d\u79f0 (SID)"},
		{ "DatabasePortInfo", "\u6570\u636e\u5e93\u8fde\u63a5\u7aef\u53e3"},
		{ "DatabasePort", "\u6570\u636e\u5e93\u7aef\u53e3"},
		{ "DatabaseUserInfo",
		"Compiere \u6570\u636e\u5e93\u7528\u6237\u5e10\u53f7"},
		{ "DatabaseUser", "\u6570\u636e\u5e93\u5e10\u53f7"},
		{ "DatabasePasswordInfo",
		"Compiere \u6570\u636e\u5e93\u7528\u6237\u5e10\u53f7\u7684\u53e3\u4ee4"},
		{ "DatabasePassword", "\u6570\u636e\u5e93\u53e3\u4ee4"},
		{ "TNSNameInfo",
		"TNS \u6216 \u5168\u5c40\u6570\u636e\u5e93\u540d(Global Database Name)"},
		{ "TNSName", "TNS \u540d"},
		{ "SystemPasswordInfo", "\u7cfb\u7edf\u7528\u6237\u53e3\u4ee4"},
		{ "SystemPassword", "\u7cfb\u7edf\u7528\u6237\u53e3\u4ee4"},
		{ "MailServerInfo", "\u7535\u5b50\u90ae\u4ef6\u670d\u52a1\u5668"},
		{ "MailServer", "\u7535\u5b50\u90ae\u4ef6\u670d\u52a1\u5668"},
		{ "AdminEMailInfo", "Compiere \u7ba1\u7406\u5458 EMail"},
		{ "AdminEMail", "\u7ba1\u7406\u5458\u7684\u7535\u5b50\u90ae\u4ef6"},
		{ "DatabaseServerInfo",
		"\u6570\u636e\u5e93\u670d\u52a1\u5668\u540d\u79f0"},
		{ "DatabaseServer", "\u6570\u636e\u5e93\u670d\u52a1\u5668"},
		{ "JavaHomeInfo", "Java \u4e3b\u76ee\u5f55"},
		{ "JavaHome", "Java \u4e3b\u76ee\u5f55"},
		{ "JNPPortInfo",
		"\u5e94\u7528\u670d\u52a1\u5668\u7684 JNP \u8fde\u63a5\u7aef\u53e3"},
		{ "JNPPort", "JNP \u7aef\u53e3"},
		{ "MailUserInfo", "Compiere \u7535\u5b50\u90ae\u4ef6\u5e10\u53f7"},
		{ "MailUser", "\u7535\u5b50\u90ae\u4ef6\u5e10\u53f7"},
		{ "MailPasswordInfo",
		"Compiere \u7535\u5b50\u90ae\u4ef6\u8d26\u53f7\u7684\u53e3\u4ee4"},
		{ "MailPassword", "\u7535\u5b50\u90ae\u4ef6\u53e3\u4ee4"},
		{ "KeyStorePassword", "Key Store Password"},
		{ "KeyStorePasswordInfo", "Password for SSL Key Store"},
		//
		{ "JavaType", "Java VM"},
		{ "JavaTypeInfo", "Java VM Vendor"},
		{ "AppsType", "Server Type"},
		{ "AppsTypeInfo", "J2EE Application Server Type"},
		{ "DeployDir", "Deployment"},
		{ "DeployDirInfo", "J2EE Deployment Directory"},
		{ "ErrorDeployDir", "Error Deployment Directory"},
		//
		{ "TestInfo", "\u6d4b\u8bd5\u8bbe\u7f6e"},
		{ "Test", "\u6d4b\u8bd5"},
		{ "SaveInfo", "\u4fdd\u5b58\u8bbe\u7f6e"},
		{ "Save", "\u4fdd\u5b58"},
		{ "HelpInfo", "\u83b7\u53d6\u5e2e\u52a9"},
		{ "ServerError", "\u670d\u52a1\u5668\u8bbe\u7f6e\u9519\u8bef"},
		{ "ErrorJavaHome", "Java \u4e3b\u76ee\u5f55\u8bbe\u7f6e\u9519\u8bef"},
		{ "ErrorCompiereHome",
		"Compiere \u4e3b\u76ee\u5f55\u8bbe\u7f6e\u9519\u8bef"},
		{
		"ErrorAppsServer",
		"\u5e94\u7528\u670d\u52a1\u5668\u8bbe\u7f6e\u9519\u8bef (\u4e0d\u80fd\u4f7f\u7528\u672c\u673a-localhost)"},
		{ "ErrorWebPort",
		"Web \u670d\u52a1\u5668\u8fde\u63a5\u7aef\u53e3\u8bbe\u7f6e\u9519\u8bef"},
		{ "ErrorJNPPort",
		"JNP \u8fde\u63a5\u7aef\u53e3\u8bbe\u7f6e\u9519\u8bef"},
		{
		"ErrorDatabaseServer",
		"\u6570\u636e\u5e93\u670d\u52a1\u5668\u8bbe\u7f6e\u9519\u8bef (\u4e0d\u80fd\u4f7f\u7528\u672c\u673a-localhost)"},
		{ "ErrorDatabasePort",
		"\u6570\u636e\u5e93\u7aef\u53e3\u8bbe\u7f6e\u9519\u8bef"},
		{ "ErrorJDBC", "JDBC \u8fde\u63a5\u9519\u8bef"},
		{ "ErrorTNS", "TNS \u8fde\u63a5\u9519\u8bef"},
		{
		"ErrorMailServer",
		"\u90ae\u4ef6\u670d\u52a1\u5668\u8bbe\u7f6e\u9519\u8bef (\u4e0d\u80fd\u4f7f\u7528\u672c\u673a-localhost)"},
		{ "ErrorMail", "\u7535\u5b50\u90ae\u4ef6\u9519\u8bef"},
		{ "ErrorSave", "\u5b58\u6863\u9519\u8bef"},

		{ "NewSecurityKey",			"Your new Security Key (lib/CompiereSecure.dat)\n"
			+ "was created for you to be used for Encryption.\n" 
			+ "Please make sure to back it up securely.\n"
			+ "If lost, you are not able to recover encrypted data." },
		{ "SecurityKeyError",		"Error ocured when creating/installing your Security Key.\n"
			+ "Please contact support with Log information." }, 	
		{ "Start",					"Start" },
		{ "Done",					"Finished" },
		{ "SelectOption",			"Select Option" },
		{ "ServerInstall",			"Server Install" },
		{ "CreateNewDatabase",		"Create new Database" },
		{ "DropOldCreateNewDatabase",	"DROP OLD and create NEW Database" },
		{ "MigrateExistingDatabase",	"Migrate Database" },
			
		{ "ErrorProcess",			"Process Error\nPlease check log for details" },
		{ "ServerSetupComplete",	"Server Setup Complete\nPlease Migrate or Import new Database." },
		{ "DropExistingDatabase",	"Do you want to DROP the existing database\n(loosing all existing data)\nand create a NEW database?" }, 
			
		{ "JNDIPort", 				"JNDI Port" },
		{ "ErrorWasClient", 		"Error WAS Client PATH" }

	};

	/**
	 * Get Contents
	 * 
	 * @return contents
	 */
	@Override
	public Object[][] getContents ()
	{
		return contents;
	} // getContents
} // SerupRes
