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
package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Application Task
 *
 *  @author     Jorg Janke
 *  @version    $Id: ATask.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class ATask extends CFrame 
	implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Start Application Task
	 *  @param task task model
	 */
	static public void start (final String title, final MTask task)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				new ATask(title, task);
			}
		}.start();
	}   //  start

	
	/**************************************************************************
	 *  Full Constructor
	 *  @param title title
	 *  @param task task
	 */
	public ATask (String title, MTask task)
	{
		super (title);
		this.setIconImage(Compiere.getImage16());
		try
		{
			jbInit();
			AEnv.showCenterScreen(this);
			//
			if (task.isServerProcess())
				info.setText("Executing on Server ...");
			else
				info.setText("Executing locally ...");
			String result = task.execute();
			info.setText(result);
			confirmPanel.getCancelButton().setEnabled(false);
			confirmPanel.getOKButton().setEnabled(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, task.toString(), e);
		}
	}   //  ATask

	private Task    m_task = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ATask.class);

	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private JScrollPane infoScrollPane = new JScrollPane();
	private JTextArea info = new JTextArea();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		info.setEditable(false);
		info.setBackground(CompierePLAF.getFieldBackground_Inactive());
		infoScrollPane.getViewport().add(info, null);
		infoScrollPane.setPreferredSize(new Dimension(500,300));
		this.getContentPane().add(infoScrollPane, BorderLayout.CENTER);
		this.getContentPane().add(confirmPanel,  BorderLayout.SOUTH);
		//
		confirmPanel.addActionListener(this);
		confirmPanel.getOKButton().setEnabled(false);
	}   //  jbInit


	/**
	 *  Action Listener
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (m_task != null && m_task.isAlive())
			m_task.interrupt();
		dispose();
	}   //  actionPerformed

}   //  ATask
