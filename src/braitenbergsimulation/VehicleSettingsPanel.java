package braitenbergsimulation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VehicleSettingsPanel extends GridbagPanel
{
	private static final long serialVersionUID = 4359479573832200713L;
	private static final int RANGEANGLEDEFAULT = 75;
	/* The angle a sensor splays out from the vehicle direction */
	private static final int SPLAYANGLEDEFAULT = 30;
	/* The gap between the two sensors */
	private static final int GAPDEFAULT = 0;
	/* The angle increment is the angle change per wheel step */
	private static final double ANGLEDELTADEFAULT = 1.0;
	private static final double ANGLEDELTA_MIN = 0.1;
	private static final double ANGLEDELTA_MAX = 9.0;
	private static final double ANGLEDELTA_INCREMENT = 0.1;
	/* The maximum speed is the maximum distance a vehicle can move in one turn */
	private static final int MAXSPEEDDEFAULT = 20;
	/* The maximum angle is the maximum amount the vehicle direction can change in one turn */
	private static final int MAXANGLEDEFAULT = 15;
	
	/* Increments, max and mins */
	private static final int SPINNER_INCREMENT = 1;
	private static final int ANGLE_MIN = 1;
	private static final int ANGLE_MAX = 359;
	private static final int GAP_MIN = 0;
	private static final int GAP_MAX = 9;
	private static final int SPEED_MIN = 1;
	private static final int SPEED_MAX = 99;
	
	private final JCheckBox cbxPerceptibleVehicles;
	private final JCheckBox cbxDisplayIds;
	private final JCheckBox cbxDisplaySensors;
        private final JCheckBox cbxWrappingPlain;
	private final JSpinner spnrSensorRangeAngle;
	private final JSpinner spnrSensorSplayAngle;
	private final JSpinner spnrSensorGap;
	private final JSpinner spnrAngleDelta;
	private final JSpinner spnrMaxSpeed;
	private final JSpinner spnrMaxAngle;
	private final JButton btnDefault;
	
	private final JPanel pnlSensorVehicle;

	/**
	 * Constructor
	 */
	VehicleSettingsPanel()
	{
		cbxPerceptibleVehicles = new JCheckBox();
		cbxDisplayIds = new JCheckBox();
		cbxDisplaySensors = new JCheckBox();
                cbxWrappingPlain = new JCheckBox();

		spnrSensorRangeAngle = new JSpinner(new SpinnerNumberModel(RANGEANGLEDEFAULT,
				ANGLE_MIN, ANGLE_MAX, SPINNER_INCREMENT));
		spnrSensorSplayAngle = new JSpinner(new SpinnerNumberModel(SPLAYANGLEDEFAULT,
				ANGLE_MIN, ANGLE_MAX, SPINNER_INCREMENT));
		spnrSensorGap = new JSpinner(new SpinnerNumberModel(GAPDEFAULT,
				GAP_MIN, GAP_MAX, SPINNER_INCREMENT));
		spnrAngleDelta = new JSpinner(new SpinnerNumberModel(ANGLEDELTADEFAULT,
				ANGLEDELTA_MIN, ANGLEDELTA_MAX, ANGLEDELTA_INCREMENT));
		spnrMaxSpeed = new JSpinner(new SpinnerNumberModel(MAXSPEEDDEFAULT,
				SPEED_MIN, SPEED_MAX, SPINNER_INCREMENT));
		spnrMaxAngle = new JSpinner(new SpinnerNumberModel(MAXANGLEDEFAULT,
				ANGLE_MIN, ANGLE_MAX, SPINNER_INCREMENT));

		btnDefault = new JButton("Default Values");

		pnlSensorVehicle = VehicleRenderer.getSensorVehiclePanel(this);
		
		/* Layout all the components */
		layoutComponents();
		addListeners();
	}
	
	/**
	 * Layout the components on the screen in an understandable and aesthetically pleasing manner
	 */
	private void layoutComponents()
	{
		GridbagPanel pnlSettings = new GridbagPanel();

		JLabel lblPerceptibleVehicles = new JLabel("Vehicles Perceive Other Vehicles :");
		JLabel lblDisplayIds = new JLabel("Display Vehicle IDs");
		JLabel lblDisplaySensors = new JLabel("Display Sensors");
                JLabel lblWrappingPlain = new JLabel("Vehicles Wrap Plain");
		pnlSettings.add(lblPerceptibleVehicles, 0, 0, 1, 1, GridBagConstraints.WEST);
		pnlSettings.add(lblDisplayIds, 0, 1, 1, 1, GridBagConstraints.WEST);
		pnlSettings.add(lblDisplaySensors, 0, 2, 1, 1, GridBagConstraints.WEST);
                pnlSettings.add(lblWrappingPlain, 0, 3, 1, 1, GridBagConstraints.WEST);

		pnlSettings.add(cbxPerceptibleVehicles, 1, 0, 1, 1, GridBagConstraints.EAST);
		pnlSettings.add(cbxDisplayIds, 1, 1, 1, 1, GridBagConstraints.EAST);
		pnlSettings.add(cbxDisplaySensors, 1, 2, 1, 1, GridBagConstraints.EAST);
                pnlSettings.add(cbxWrappingPlain, 1, 3, 1, 1, GridBagConstraints.EAST);

		JLabel lblSensorRangeAngle = new JLabel("Sensor Range Angle (Degrees) :");
		JLabel lblSensorSplayAngle = new JLabel("Sensor Splay Angle (Degrees) :");
		JLabel lblSensorGap = new JLabel("Gap between Sensors :");
		JLabel lblAngleDelta = new JLabel("Angle Change per Step (Degrees) :");
		JLabel lblMaxSpeed = new JLabel("Maximum Speed :");
		JLabel lblMaxAngle = new JLabel("Maximum Angle Change (Degrees) :");
		
		GridbagPanel pnlVehicleSettings = new GridbagPanel();
		pnlVehicleSettings.add(lblSensorRangeAngle, 0, 0, 1, 1, GridBagConstraints.WEST);
		pnlVehicleSettings.add(lblSensorSplayAngle, 0, 1, 1, 1, GridBagConstraints.WEST);
		pnlVehicleSettings.add(lblSensorGap, 0, 2, 1, 1, GridBagConstraints.WEST);
		pnlVehicleSettings.add(lblAngleDelta, 0, 3, 1, 1, GridBagConstraints.WEST);
		pnlVehicleSettings.add(lblMaxSpeed, 0, 4, 1, 1, GridBagConstraints.WEST);
		pnlVehicleSettings.add(lblMaxAngle, 0, 5, 1, 1, GridBagConstraints.WEST);		
		pnlVehicleSettings.add(spnrSensorRangeAngle, 1, 0, 1, 1, GridBagConstraints.EAST);
		pnlVehicleSettings.add(spnrSensorSplayAngle, 1, 1, 1, 1, GridBagConstraints.EAST);
		pnlVehicleSettings.add(spnrSensorGap, 1, 2, 1, 1, GridBagConstraints.EAST);
		pnlVehicleSettings.add(spnrAngleDelta, 1, 3, 1, 1, GridBagConstraints.EAST);
		pnlVehicleSettings.add(spnrMaxSpeed, 1, 4, 1, 1, GridBagConstraints.EAST);
		pnlVehicleSettings.add(spnrMaxAngle, 1, 5, 1, 1, GridBagConstraints.EAST);

		Border bdrLine = BorderFactory.createLineBorder(Color.BLACK);
		Border bdrTitled = BorderFactory.createTitledBorder(bdrLine, "Vehicle Attributes");
		pnlVehicleSettings.setBorder(bdrTitled);
		
		pnlSettings.add(pnlVehicleSettings, 0, 4, 2, 1, GridBagConstraints.CENTER);

		pnlSettings.add(pnlSensorVehicle, 0, 5, 2, 1, GridBagConstraints.CENTER);
		
		JPanel pnlButton = new JPanel();
		pnlButton.add(btnDefault);
		
		pnlSettings.add(pnlButton, 1, 6, 1, 1, GridBagConstraints.EAST);

		add(pnlSettings, 0, 0, 1, 1, GridBagConstraints.NORTHWEST);
	}

	/**
	 * Add listeners to various components
	 */
	private void addListeners()
	{
		/* Listen for changes in the sensor related spinners, and update the rendered vehicle */
		spnrSensorRangeAngle.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent cevt) 
			{
				repaintRenderedVehicle();
			}
		});
		spnrSensorSplayAngle.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent cevt) 
			{
				repaintRenderedVehicle();
			}
		});
		spnrSensorGap.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent cevt) 
			{
				repaintRenderedVehicle();
			}
		});
		/* Set the defaults when the default button is clicked */
		btnDefault.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -2348307108903202682L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				setDefaults();
			}
		});
	}
	
	/**
	 * Repaint the rendered vehicle
	 */
	private void repaintRenderedVehicle()
	{
		pnlSensorVehicle.repaint();
	}

	/* Mutators */
	
	/**
	 * Set all the components to default values
	 */
	void setDefaults()
	{
		cbxPerceptibleVehicles.setSelected(false);
		cbxDisplayIds.setSelected(false);
		cbxDisplaySensors.setSelected(false);
                cbxWrappingPlain.setSelected(true);
		spnrSensorRangeAngle.setValue(Integer.valueOf(RANGEANGLEDEFAULT));
		spnrSensorSplayAngle.setValue(Integer.valueOf(SPLAYANGLEDEFAULT));
		spnrSensorGap.setValue(Integer.valueOf(GAPDEFAULT));
		spnrAngleDelta.setValue(Double.valueOf(ANGLEDELTADEFAULT));
		spnrMaxSpeed.setValue(Integer.valueOf(MAXSPEEDDEFAULT));
		spnrMaxAngle.setValue(Integer.valueOf(MAXANGLEDEFAULT));
	}

	/**
	 * Set if vehicles perceive other vehicles
	 * 
	 * @param b true if vehicles perceive other vehicles
	 */
	void setPerceptibleVehicles(boolean b)
	{
		cbxPerceptibleVehicles.setSelected(b);
	}

	/**
	 * Set if vehicle ids should be displayed
	 * 
	 * @param b true if vehicle ids should be displayed
	 */
	void setDisplayIds(boolean b)
	{
		cbxDisplayIds.setSelected(b);
	}

	/**
	 * Set if vehicle sensors should be displayed
	 * 
	 * @param b true if vehicle sensors should be displayed
	 */
	void setDisplaySensors(boolean b)
	{
		cbxDisplaySensors.setSelected(b);
	}
        
        /**
         * Set if vehicle wraps around plain (vs. reflection) when goes too far in dimension
         * 
         * @param b true if vehicle should wrap (vs. reflect)
         */
        void setWrappingPlain(boolean b)
        {
                cbxWrappingPlain.setSelected(b);
        }

	/**
	 * Set the range angle of a sensor. This is the angle relative to the direction the sensor is
	 * facing. Perceptible objects outside of this range are not perceived by the sensor.
	 * 
	 * @param angle the sensor range angle, in degrees
	 */
	void setSensorRangeAngle(double angle)
	{
		spnrSensorRangeAngle.setValue(Double.valueOf(angle));
	}

	/**
	 * Set the sensor splay angle, that is, the angle that the sensor differs from the direction
	 * the vehicle is facing.
	 * 
	 * @param angle the sensor splay angle, in degrees
	 */
	void setSensorSplayAngle(double angle)
	{
		spnrSensorSplayAngle.setValue(Double.valueOf(angle));
	}

	/**
	 * Set the sensor gap, that is, how far apart the two sensors are on a vehicle.
	 * 
	 * @param gap the gap between the two sensors
	 */
	void setSensorGap(int gap)
	{
		spnrSensorGap.setValue(Integer.valueOf(gap));
	}

	/**
	 * Set angle increment, that is, the angle change per wheel step.
	 * 
	 * @param angle the angle increment, in degrees
	 */
	void setAngleDelta(double angle)
	{
		spnrAngleDelta.setValue(Double.valueOf(angle));
	}

	/**
	 * Set the maximum speed, that is, the maximum distance a vehicle can travel in one turn
	 * 
	 * @param speed the maximum speed
	 */
	void setMaxSpeed(int speed)
	{
		spnrMaxSpeed.setValue(Integer.valueOf(speed));
	}

	/**
	 * Set the maximum angle, that is, the maximum angle that a vehicle can move in one turn
	 * 
	 * @param angle, the maximum turning angle, in degrees
	 */
	void setMaxAngle(double angle)
	{
		spnrMaxAngle.setValue(Double.valueOf(angle));
	}

	/* Accessors */

	/**
	 * Get if vehicles perceive other vehicles
	 * 
	 * @return true if vehicles perceive other vehicles
	 */
	boolean isPerceptibleVehicles()
	{
		return cbxPerceptibleVehicles.isSelected();
	}

	/**
	 * Get if vehicle ids should be displayed
	 * 
	 * @return true if vehicle ids should be displayed
	 */
	boolean isDisplayIds()
	{
		return cbxDisplayIds.isSelected();
	}

	/**
	 * Get if vehicle sensors should be displayed
	 * 
	 * @return true if vehicle sensors should be displayed
	 */
	boolean isDisplaySensors()
	{
		return cbxDisplaySensors.isSelected();
	}

        /**
         * Get if vehicle should wrap (vs. reflect) on wall collision
         * 
         * @return true if vehicle should wrap
         */
        boolean isWrappingPlain()
        {
                return cbxWrappingPlain.isSelected();
        }
	/**
	 * Get the range angle of a sensor. This is the angle relative to the direction the sensor is
	 * facing. Perceptible objects outside of this range are not perceived by the sensor.
	 * 
	 * @return the sensor range angle, in radians
	 */
	double getSensorRangeAngle()
	{
		return Math.toRadians(((Number) spnrSensorRangeAngle.getValue()).intValue());
	}

	/**
	 * Get the sensor splay angle, that is, the angle that the sensor differs from the direction
	 * the vehicle is facing.
	 * 
	 * @return the sensor splay angle, in radians
	 */
	double getSensorSplayAngle()
	{
		return Math.toRadians(((Number) spnrSensorSplayAngle.getValue()).intValue());
	}

	/**
	 * Get the sensor gap, that is, how far apart the two sensors are on a vehicle.
	 * 
	 * @return the number of vehicles
	 */
	int getSensorGap()
	{
		return ((Number) spnrSensorGap.getValue()).intValue();
	}

	/**
	 * Get angle increment, that is, the angle change per wheel step, in radians.
	 * 
	 * @return the angle increment, in radians
	 */
	double getAngleDelta()
	{
		return Math.toRadians(((Number) spnrAngleDelta.getValue()).intValue());
	}

	/**
	 * Get the maximum speed, that is, the maximum distance a vehicle can travel in one turn
	 * 
	 * @return the maximum speed
	 */
	int getMaxSpeed()
	{
		return ((Number) spnrMaxSpeed.getValue()).intValue();
	}

	/**
	 * Get the maximum angle, that is, the maximum angle that a vehicle can move in one turn
	 * 
	 * @return the maximum angle, in radians
	 */
	double getMaxAngle()
	{
		return Math.toRadians(((Number) spnrMaxAngle.getValue()).intValue());
	}
}
