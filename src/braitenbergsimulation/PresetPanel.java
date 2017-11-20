package braitenbergsimulation;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

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

/**
 * This class allows the user to choose from a number of preset configurations.
 * 
 * @author Douglas B. Caulkins
 */
public class PresetPanel extends GridbagPanel
{
	private static final long serialVersionUID = 7377002061782346579L;

	ConfigurationPanel pnlConfiguration;

	private final JRadioButton rbtnMixed;
	private final JRadioButton rbtnFearful;
	private final JRadioButton rbtnAggressive;
	private final JRadioButton rbtnAdoring;
	private final JRadioButton rbtnExploring;
	private final JRadioButton rbtnDecidingUnX;
	private final JRadioButton rbtnDecidingX;

	private final JRadioButton rbtnWhirligigAggressive;
	
	private final JRadioButton rbtnMixedLights;
	private final JRadioButton rbtnFearfulLights;
	private final JRadioButton rbtnAggressiveLights;
	private final JRadioButton rbtnAdoringLights;
	private final JRadioButton rbtnExploringLights;
	private final JRadioButton rbtnDecidingUnXLights;
	private final JRadioButton rbtnDecidingXLights;
	
	/**
	 * Constructor
	 * 
	 * @param pnlConfig the panel containing various configured settings 
	 */
	PresetPanel(ConfigurationPanel pnlConfig)
	{
		pnlConfiguration = pnlConfig;

		ButtonGroup grpPresetButton = new ButtonGroup();
		
		rbtnMixed = new JRadioButton("Mixed vehicles", false);
		grpPresetButton.add(rbtnMixed);
		rbtnFearful = new JRadioButton("Fearful vehicles", false);
		grpPresetButton.add(rbtnFearful);
		rbtnAggressive = new JRadioButton("Aggressive vehicles", false);
		grpPresetButton.add(rbtnAggressive);
		rbtnAdoring = new JRadioButton("Adoring vehicles", false);
		grpPresetButton.add(rbtnAdoring);
		rbtnExploring = new JRadioButton("Exploring vehicles", false);
		grpPresetButton.add(rbtnExploring);
		rbtnDecidingUnX = new JRadioButton("Uncrossed deciding vehicles", false);
		grpPresetButton.add(rbtnDecidingUnX);
		rbtnDecidingX = new JRadioButton("Crossed deciding vehicles", false);
		grpPresetButton.add(rbtnDecidingX);
		
		rbtnWhirligigAggressive = new JRadioButton("Whirligig Aggressive vehicles", false);
		grpPresetButton.add(rbtnWhirligigAggressive);

		rbtnMixedLights = new JRadioButton("Mixed vehicles and lights", false);
		grpPresetButton.add(rbtnMixedLights);
		rbtnFearfulLights = new JRadioButton("Fearful vehicles and lights", false);
		grpPresetButton.add(rbtnFearfulLights);
		rbtnAggressiveLights = new JRadioButton("Aggressive vehicles and lights", false);
		grpPresetButton.add(rbtnAggressiveLights);
		rbtnAdoringLights = new JRadioButton("Adoring vehicles and lights", false);
		grpPresetButton.add(rbtnAdoringLights);
		rbtnExploringLights = new JRadioButton("Exploring vehicle and lights", false);
		grpPresetButton.add(rbtnExploringLights);
		rbtnDecidingUnXLights = new JRadioButton("Uncrossed deciding vehicles and lights", false);
		grpPresetButton.add(rbtnDecidingUnXLights);
		rbtnDecidingXLights = new JRadioButton("Crossed deciding vehicles and lights", false);
		grpPresetButton.add(rbtnDecidingXLights);
		
		/* Layout all the components */
		layoutComponents();
		addListeners();
		
		/* Select this radio button, triggering the action to update the configuration */ 
		rbtnWhirligigAggressive.doClick();
	}
	
	/**
	 * Layout the components on the screen in an understandable and aesthetically pleasing manner
	 */
	private void layoutComponents()
	{
		add(rbtnMixed, 0, 0, 1, 1, GridBagConstraints.WEST);
		add(rbtnFearful, 0, 1, 1, 1, GridBagConstraints.WEST);
		add(rbtnAggressive, 0, 2, 1, 1, GridBagConstraints.WEST);
		add(rbtnAdoring, 0, 3, 1, 1, GridBagConstraints.WEST);
		add(rbtnExploring, 0, 4, 1, 1, GridBagConstraints.WEST);
		add(rbtnDecidingUnX, 0, 5, 1, 1, GridBagConstraints.WEST);
		add(rbtnDecidingX, 0, 6, 1, 1, GridBagConstraints.WEST);

		add(rbtnWhirligigAggressive, 0, 7, 1, 1, GridBagConstraints.WEST);
		
		add(rbtnMixedLights, 0, 8, 1, 1, GridBagConstraints.WEST);
		add(rbtnFearfulLights, 0, 9, 1, 1, GridBagConstraints.WEST);
		add(rbtnAggressiveLights, 0, 10, 1, 1, GridBagConstraints.WEST);
		add(rbtnAdoringLights, 0, 11, 1, 1, GridBagConstraints.WEST);
		add(rbtnExploringLights, 0, 12, 1, 1, GridBagConstraints.WEST);
		add(rbtnDecidingUnXLights, 0, 13, 1, 1, GridBagConstraints.WEST);
		add(rbtnDecidingXLights, 0, 14, 1, 1, GridBagConstraints.WEST);
	}

	/**
	 * Add listeners to various components
	 */
	private void addListeners()
	{
		rbtnMixed.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 2732622502779886036L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setUncrossedExcitatoryCount(5);
				pnlConfiguration.setCrossedExcitatoryCount(5);
				pnlConfiguration.setUncrossedInhibitoryCount(5);
				pnlConfiguration.setCrossedInhibitoryCount(5);
				pnlConfiguration.setUncrossedThresholdCount(5);
				pnlConfiguration.setCrossedThresholdCount(5);
			}
		});
		rbtnFearful.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 2732622502779886036L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setUncrossedExcitatoryCount(10);
			}
		});
		rbtnAggressive.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -8603436360861668901L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setCrossedExcitatoryCount(10);
			}
		});
		rbtnAdoring.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 7475694785908565005L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setUncrossedInhibitoryCount(10);
			}
		});
		rbtnExploring.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 5167729031978447891L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setCrossedInhibitoryCount(10);
			}
		});
		rbtnDecidingUnX.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -1753765278367342215L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setUncrossedThresholdCount(27);
			}
		});
		rbtnDecidingX.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 6606685442508572749L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setCrossedThresholdCount(10);
			}
		});

		rbtnWhirligigAggressive.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 1966210941358342925L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setPerceptibleVehicles(true);
				pnlConfiguration.setCrossedExcitatoryCount(9);
				pnlConfiguration.setSensorRangeAngle(210);
				pnlConfiguration.setSensorSplayAngle(30);
				pnlConfiguration.setMaxAngle(30);
			}
		});

		rbtnMixedLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 1966210941358342925L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setUncrossedExcitatoryCount(5);
				pnlConfiguration.setCrossedExcitatoryCount(5);
				pnlConfiguration.setUncrossedInhibitoryCount(5);
				pnlConfiguration.setCrossedInhibitoryCount(5);
				pnlConfiguration.setUncrossedThresholdCount(5);
				pnlConfiguration.setCrossedThresholdCount(5);
			}
		});
		rbtnFearfulLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 5757070498202736361L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setUncrossedExcitatoryCount(10);
			}
		});
		rbtnAggressiveLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 1966210941358342925L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setCrossedExcitatoryCount(10);
			}
		});
		rbtnAdoringLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -4966800383809064202L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setUncrossedInhibitoryCount(10);
			}
		});
		rbtnExploringLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 5453952024087575584L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setCrossedInhibitoryCount(10);
			}
		});
		rbtnDecidingUnXLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 7550934133034283418L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setUncrossedThresholdCount(10);
			}
		});
		rbtnDecidingXLights.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 1261142116470148932L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlConfiguration.clearConfiguration();
				pnlConfiguration.setLightSourceCount(5);
				pnlConfiguration.setCrossedThresholdCount(10);
			}
		});
	}
}
