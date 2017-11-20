package braitenbergsimulation;
/*
 * Copyright 2010 Douglas B. Caulkins
 * 
 * This file is part of the Braitenberg Simulation Java package.
 *     
 * The Braitenberg Simulation Java package is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Braitenberg Simulation Java package is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with the Braitenberg Simulation Java package.  If not, 
 * see <http://www.gnu.org/licenses/>.
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This panel contains the simulated dark plain where the vehicles roam.
 * 
 * @author Douglas B. Caulkins
 */
class DarkPlainFramePanel extends JPanel
{
	private static final long serialVersionUID = -8209612104407716073L;
	
	/* 
	 * Used for configuring the number of lights, the number of vehicles and whether vehicles perceive
	 * other vehicles.
	 */
	private final ConfigurationPanel pnlConfiguration;
	/* This button starts and stops the simulation. */
	private final JButton btnStartStop;
	/* This panel contains a key to the colors of the various vehicle types */
	private final JPanel pnlKey;
	/* This panel contains the current simulated dark plain */
	private final DarkPlainPanel pnlDarkPlain;
	
	/**
	 * Constructor
	 * 
	 * @param pnlConfig the configuration attributes
	 */
	DarkPlainFramePanel(ConfigurationPanel pnlConfig)
	{
		pnlConfiguration = pnlConfig;
		
		pnlDarkPlain = new DarkPlainPanel(pnlConfig);
		pnlKey = VehicleRenderer.getVehicleKeyPanel();
		btnStartStop = new JButton("Start");
		
		/* Layout all the components */
		layoutComponents();
		addListeners();
	}
	
	/**
	 * Layout the components on the screen in an understandable and aesthetically pleasing manner
	 */
	private void layoutComponents()
	{
		GridbagPanel pnlPlainFrame = new GridbagPanel();
		pnlPlainFrame.add(pnlDarkPlain, 0, 0, 1, 1, GridBagConstraints.CENTER);
		pnlPlainFrame.add(pnlKey, 0, 1, 1, 1, GridBagConstraints.CENTER);		
		
		JScrollPane spPlainFrame = new JScrollPane(pnlPlainFrame);
		setLayout(new BorderLayout());
		add(spPlainFrame, BorderLayout.CENTER);
		JPanel pnlButton = new JPanel();
		pnlButton.add(btnStartStop);
		add(pnlButton, BorderLayout.SOUTH);
	}

	/**
	 * Add listeners to various components
	 */
	private void addListeners()
	{
		/* The button starts or stops the simulation */
		btnStartStop.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -4004363373865332230L;
			
			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				/* If the simulation is running, stop it */
				if (pnlDarkPlain.isRunning())
				{
					pnlDarkPlain.stop();
					btnStartStop.setText("Start");
				}
				/* Else the simulation is not running, so start it */
				else
				{
					pnlDarkPlain.updateSettings(pnlConfiguration);
					pnlDarkPlain.start();
					btnStartStop.setText("Stop");
				}
			}
		});
	}
	
	/**
	 * Restart the simulation
	 */
	void restart()
	{
		/* Stop the simulation if it's running */
		if (pnlDarkPlain.isRunning())
		{
			pnlDarkPlain.stop();
		}
		/* Start the simulation */
		pnlDarkPlain.updateSettings(pnlConfiguration);
		pnlDarkPlain.start();
		btnStartStop.setText("Stop");		
	}
}