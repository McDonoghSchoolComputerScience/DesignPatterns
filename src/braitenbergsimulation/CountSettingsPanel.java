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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

/**
 * This panel contains the widgets that allow the number of lights and vehicles to be
 * configured by the user. There's also a checkbox for indicating that vehicles can perceive
 * other vehicles.
 * 
 * @author Douglas B. Caulkins
 */
class CountSettingsPanel extends GridbagPanel 
{
	private static final long serialVersionUID = 4157200368922418423L;

	private static final int SPINNER_DEFAULT = 1;
	private static final int SPINNER_MIN = 0;
	private static final int SPINNER_MAX = 99;
	private static final int SPINNER_INCREMENT = 1;
	private static final Integer ZERO = Integer.valueOf(0);
	private static final Integer ONE = Integer.valueOf(SPINNER_DEFAULT);
	
	private final JSpinner spnrLightSource;

	private final JSpinner spnrUncrossedExcitatory;
	private final JSpinner spnrCrossedExcitatory;
	private final JSpinner spnrUncrossedInhibitory;
	private final JSpinner spnrCrossedInhibitory;
	private final JSpinner spnrUncrossedThreshold;
	private final JSpinner spnrCrossedThreshold;

	private final JButton btnClear;
	private final JButton btnDefault;
	
	/**
	 * Constructor
	 */
	CountSettingsPanel()
	{
		/*
		 * Initialize the components that contain configurable values
		 */
		spnrLightSource = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT, 
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));
		
		spnrUncrossedExcitatory = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT,
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));
		spnrCrossedExcitatory = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT,
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));
		spnrUncrossedInhibitory = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT,
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));
		spnrCrossedInhibitory = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT,
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));
		spnrUncrossedThreshold = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT,
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));
		spnrCrossedThreshold = new JSpinner(new SpinnerNumberModel(SPINNER_DEFAULT,
				SPINNER_MIN, SPINNER_MAX, SPINNER_INCREMENT));

		btnClear = new JButton("Clear");
		btnDefault = new JButton("Default Values");
		
		/* Layout all the components */
		layoutComponents();
		addListeners();
	}
	
	/**
	 * Layout the components on the screen in an understandable and aesthetically pleasing manner
	 */
	private void layoutComponents()
	{
		GridbagPanel pnlCounts = new GridbagPanel();

		JLabel lblLightSource = new JLabel("Light Sources :");
		pnlCounts.add(lblLightSource, 0, 0, 1, 1, GridBagConstraints.WEST);
		pnlCounts.add(spnrLightSource, 1, 0, 1, 1, GridBagConstraints.EAST);

		JLabel lblUncrossedExcitatory = new JLabel(
				"Cowardly (2a - Uncrossed Excitatory) :");
		JLabel lblCrossedExcitatory = new JLabel(
				"Aggressive (2b - Crossed Excitatory) :");
		JLabel lblUncrossedInhibatory = new JLabel(
				"Adoring (3a - Uncrossed Inhibatory) :");
		JLabel lblCrossedInhibatory = new JLabel(
				"Exploring (3b - Crossed Inhibatory) :");
		JLabel lblUncrossedThreshold = new JLabel(
				"Deciding (4b - Uncrossed Threshold) :");
		JLabel lblCrossedThreshold = new JLabel(
				"Crossed Deciding (4b - Crossed Threshold) :");
		
		GridbagPanel pnlVehicleCounts = new GridbagPanel();
		pnlVehicleCounts.add(lblUncrossedExcitatory, 0, 0, 1, 1, GridBagConstraints.WEST);
		pnlVehicleCounts.add(lblCrossedExcitatory, 0, 1, 1, 1, GridBagConstraints.WEST);
		pnlVehicleCounts.add(lblUncrossedInhibatory, 0, 2, 1, 1, GridBagConstraints.WEST);
		pnlVehicleCounts.add(lblCrossedInhibatory, 0, 3, 1, 1, GridBagConstraints.WEST);
		pnlVehicleCounts.add(lblUncrossedThreshold, 0, 4, 1, 1, GridBagConstraints.WEST);
		pnlVehicleCounts.add(lblCrossedThreshold, 0, 5, 1, 1, GridBagConstraints.WEST);		
		pnlVehicleCounts.add(spnrUncrossedExcitatory, 1, 0, 1, 1, GridBagConstraints.EAST);
		pnlVehicleCounts.add(spnrCrossedExcitatory, 1, 1, 1, 1, GridBagConstraints.EAST);
		pnlVehicleCounts.add(spnrUncrossedInhibitory, 1, 2, 1, 1, GridBagConstraints.EAST);
		pnlVehicleCounts.add(spnrCrossedInhibitory, 1, 3, 1, 1, GridBagConstraints.EAST);
		pnlVehicleCounts.add(spnrUncrossedThreshold, 1, 4, 1, 1, GridBagConstraints.EAST);
		pnlVehicleCounts.add(spnrCrossedThreshold, 1, 5, 1, 1, GridBagConstraints.EAST);

		Border bdrLine = BorderFactory.createLineBorder(Color.BLACK);
		Border bdrTitled = BorderFactory.createTitledBorder(bdrLine, "Vehicle Count");
		pnlVehicleCounts.setBorder(bdrTitled);
		
		pnlCounts.add(pnlVehicleCounts, 0, 1, 2, 1, GridBagConstraints.CENTER);
		
		JPanel pnlButton = new JPanel();
		pnlButton.add(btnClear);
		pnlButton.add(btnDefault);
		
		pnlCounts.add(pnlButton, 1, 2, 1, 1, GridBagConstraints.EAST);

		add(pnlCounts, 0, 0, 1, 1, GridBagConstraints.NORTHWEST);
	}

	/**
	 * Add listeners to various components
	 */
	private void addListeners()
	{
		btnDefault.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -218409281543690414L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				setDefaults();
			}
		});
		btnClear.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 4690571438392492031L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				clearFields();
			}
		});
	}

	/* Mutators */

	/**
	 * Clear all the fields
	 */
	void clearFields()
	{
		spnrLightSource.setValue(ZERO);
		spnrUncrossedExcitatory.setValue(ZERO);
		spnrCrossedExcitatory.setValue(ZERO);
		spnrUncrossedInhibitory.setValue(ZERO);
		spnrCrossedInhibitory.setValue(ZERO);
		spnrUncrossedThreshold.setValue(ZERO);
		spnrCrossedThreshold.setValue(ZERO);
	}
	
	/**
	 * Set all the components to default values
	 */
	private void setDefaults()
	{
		spnrLightSource.setValue(ONE);
		spnrUncrossedExcitatory.setValue(ONE);
		spnrCrossedExcitatory.setValue(ONE);
		spnrUncrossedInhibitory.setValue(ONE);
		spnrCrossedInhibitory.setValue(ONE);
		spnrUncrossedThreshold.setValue(ONE);
		spnrCrossedThreshold.setValue(ONE);
	}
	
	/**
	 * Set the light source count
	 * 
	 * @param count the number of light sources
	 */
	void setLightSourceCount(int count)
	{
		spnrLightSource.setValue(Integer.valueOf(count));
	}
	
	/**
	 * Set the uncrossed excitatory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setUncrossedExcitatoryCount(int count)
	{
		spnrUncrossedExcitatory.setValue(Integer.valueOf(count));
	}
	
	/**
	 * Set the crossed excitatory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setCrossedExcitatoryCount(int count)
	{
		spnrCrossedExcitatory.setValue(Integer.valueOf(count));
	}
	
	/**
	 * Set the uncrossed inhibitory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setUncrossedInhibitoryCount(int count)
	{
		spnrUncrossedInhibitory.setValue(Integer.valueOf(count));
	}
	
	/**
	 * set the crossed inhibitory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setCrossedInhibitoryCount(int count)
	{
		spnrCrossedInhibitory.setValue(Integer.valueOf(count));
	}
	
	/**
	 * Set the uncrossed threshold vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setUncrossedThresholdCount(int count)
	{
		spnrUncrossedThreshold.setValue(Integer.valueOf(count));
	}
	
	/**
	 * Set the crossed threshold vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setCrossedThresholdCount(int count)
	{
		spnrCrossedThreshold.setValue(Integer.valueOf(count));
	}

	/* Accessors */

	/**
	 * Get the light source count
	 * 
	 * @return the number of vehicles
	 */
	int getLightSourceCount()
	{
		return ((Number) spnrLightSource.getValue()).intValue();
	}
	
	/**
	 * Get the uncrossed excitatory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getUncrossedExcitatoryCount()
	{
		return ((Number) spnrUncrossedExcitatory.getValue()).intValue();
	}
	
	/**
	 * Get the crossed excitatory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getCrossedExcitatoryCount()
	{
		return ((Number) spnrCrossedExcitatory.getValue()).intValue();
	}
	
	/**
	 * Get the uncrossed inhibitory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getUncrossedInhibitoryCount()
	{
		return ((Number) spnrUncrossedInhibitory.getValue()).intValue();
	}
	
	/**
	 * Get the crossed inhibitory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getCrossedInhibitoryCount()
	{
		return ((Number) spnrCrossedInhibitory.getValue()).intValue();
	}
	
	/**
	 * Get the uncrossed threshold vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getUncrossedThresholdCount()
	{
		return ((Number) spnrUncrossedThreshold.getValue()).intValue();
	}
	
	/**
	 * Get the crossed threshold vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getCrossedThresholdCount()
	{
		return ((Number) spnrCrossedThreshold.getValue()).intValue();
	}
}