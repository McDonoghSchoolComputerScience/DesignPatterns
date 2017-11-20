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

import javax.swing.JTabbedPane;

/**
 * This class wraps the three configuration panels in a set of three tabbed panes, and
 * supplies accessors and mutators to the various attributes in the three configuration
 * panels.
 * 
 * @author Douglas B. Caulkins
 */
class ConfigurationPanel extends JTabbedPane
{
	private static final long serialVersionUID = -8075669938586550214L;

	private final CountSettingsPanel pnlCountSettings;
	private final VehicleSettingsPanel pnlVehicleSettings;
	private final MiscSettingsPanel pnlMiscSettings;

	/**
	 * Constructor
	 */
	ConfigurationPanel()
	{
		pnlCountSettings = new CountSettingsPanel();
		pnlVehicleSettings = new VehicleSettingsPanel();
		pnlMiscSettings = new MiscSettingsPanel();
		/* Add all the various panels to the tabbed pane for display */
		addTab("Vehicle Settings", pnlVehicleSettings);
		addTab("Vehicle Counts", pnlCountSettings);
		addTab("Miscellaneous Settings", pnlMiscSettings);
	}
	
	/* Mutators */
	
	/**
	 * Set all the counts to 0. Set the vehicle and miscellaneous settings to default values.
	 */
	void clearConfiguration()
	{
		pnlCountSettings.clearFields();
		pnlVehicleSettings.setDefaults();
		pnlMiscSettings.setDefaults();
	}
	
	/**
	 * Set the light source count
	 * 
	 * @param count the number of light sources
	 */
	void setLightSourceCount(int count)
	{
		pnlCountSettings.setLightSourceCount(count);
	}
	
	/**
	 * Set the uncrossed excitatory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setUncrossedExcitatoryCount(int count)
	{
		pnlCountSettings.setUncrossedExcitatoryCount(count);
	}
	
	/**
	 * Set the crossed excitatory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setCrossedExcitatoryCount(int count)
	{
		pnlCountSettings.setCrossedExcitatoryCount(count);
	}
	
	/**
	 * Set the uncrossed inhibitory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setUncrossedInhibitoryCount(int count)
	{
		pnlCountSettings.setUncrossedInhibitoryCount(count);
	}
	
	/**
	 * set the crossed inhibitory vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setCrossedInhibitoryCount(int count)
	{
		pnlCountSettings.setCrossedInhibitoryCount(count);
	}
	
	/**
	 * Set the uncrossed threshold vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setUncrossedThresholdCount(int count)
	{
		pnlCountSettings.setUncrossedThresholdCount(count);
	}
	
	/**
	 * Set the crossed threshold vehicle count
	 * 
	 * @param count the number of vehicles
	 */
	void setCrossedThresholdCount(int count)
	{
		pnlCountSettings.setCrossedThresholdCount(count);
	}

	/**
	 * Set if vehicles perceive other vehicles
	 * 
	 * @param b true if vehicles perceive other vehicles
	 */
	void setPerceptibleVehicles(boolean b)
	{
		pnlVehicleSettings.setPerceptibleVehicles(b);
	}

	/**
	 * Set if vehicle ids should be displayed
	 * 
	 * @param b true if vehicle ids should be displayed
	 */
	void setDisplayIds(boolean b)
	{
		pnlVehicleSettings.setDisplayIds(b);
	}

	/**
	 * Set the range angle of a sensor. This is the angle relative to the direction the sensor is
	 * facing. Perceptible objects outside of this range are not perceived by the sensor.
	 * 
	 * @param angle the sensor range angle, in radians
	 */
	void setSensorRangeAngle(double angle)
	{
		pnlVehicleSettings.setSensorRangeAngle(angle);
	}

	/**
	 * Set the sensor splay angle, that is, the angle that the sensor differs from the direction
	 * the vehicle is facing.
	 * 
	 * @param angle the sensor splay angle, in radians
	 */
	void setSensorSplayAngle(double angle)
	{
		pnlVehicleSettings.setSensorSplayAngle(angle);
	}

	/**
	 * Set the sensor gap, that is, how far apart the two sensors are on a vehicle.
	 * 
	 * @param gap the gap between the two sensors
	 */
	void setSensorGap(int gap)
	{
		pnlVehicleSettings.setSensorGap(gap);
	}

	/**
	 * Set angle increment, that is, the angle change per wheel step, in radians.
	 * 
	 * @param angle the angle increment, in radians
	 */
	void setAngleDelta(double angle)
	{
		pnlVehicleSettings.setAngleDelta(angle);
	}

	/**
	 * Set the maximum speed, that is, the maximum distance a vehicle can travel in one turn
	 * 
	 * @param speed the maximum speed
	 */
	void setMaxSpeed(int speed)
	{
		pnlVehicleSettings.setMaxSpeed(speed);
	}

	/**
	 * Set the maximum angle, that is, the maximum angle that a vehicle can move in one turn
	 * 
	 * @param angle, the maximum turning angle, in radians
	 */
	void setMaxAngle(double angle)
	{
		pnlVehicleSettings.setMaxAngle(angle);
	}
	
	/**
	 * Set the width of the dark plain
	 * 
	 * @param width the width of the dark plain, in pixels
	 */
	void setDarkPlainWidth(int width)
	{
		pnlMiscSettings.setDarkPlainWidth(width);
	}
	
	/**
	 * Set the height of the dark plain
	 * 
	 * @param height the height of the dark plain, in pixels
	 */
	void setDarkPlainHeight(int height)
	{
		pnlMiscSettings.setDarkPlainHeight(height);
	}
	
	/**
	 * Set the light intensity
	 * 
	 * @param intensity the light intensity
	 */
	void setLightIntensity(int intensity)
	{
		pnlMiscSettings.setLightIntensity(intensity);
	}
	
	/**
	 * Set the number of microseconds the timer sleeps
	 * 
	 * @param pause the number of microseconds the timer sleeps
	 */
	void setTimerPause(int pause)
	{
		pnlMiscSettings.setTimerPause(pause);
	}

	/* Accessors */

	/**
	 * Get the light source count
	 * 
	 * @return the number of vehicles
	 */
	int getLightSourceCount()
	{
		return pnlCountSettings.getLightSourceCount();
	}
	
	/**
	 * Get the uncrossed excitatory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getUncrossedExcitatoryCount()
	{
		return pnlCountSettings.getUncrossedExcitatoryCount();
	}
	
	/**
	 * Get the crossed excitatory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getCrossedExcitatoryCount()
	{
		return pnlCountSettings.getCrossedExcitatoryCount();
	}
	
	/**
	 * Get the uncrossed inhibitory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getUncrossedInhibitoryCount()
	{
		return pnlCountSettings.getUncrossedInhibitoryCount();
	}
	
	/**
	 * Get the crossed inhibitory vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getCrossedInhibitoryCount()
	{
		return pnlCountSettings.getCrossedInhibitoryCount();
	}
	
	/**
	 * Get the uncrossed threshold vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getUncrossedThresholdCount()
	{
		return pnlCountSettings.getUncrossedThresholdCount();
	}
	
	/**
	 * Get the crossed threshold vehicle count
	 * 
	 * @return the number of vehicles
	 */
	int getCrossedThresholdCount()
	{
		return pnlCountSettings.getCrossedThresholdCount();
	}

	/**
	 * Get if vehicles perceive other vehicles
	 * 
	 * @return true if vehicles perceive other vehicles
	 */
	boolean isPerceptibleVehicles()
	{
		return pnlVehicleSettings.isPerceptibleVehicles();
	}

	/**
	 * Get if vehicle ids should be displayed
	 * 
	 * @return true if vehicle ids should be displayed
	 */
	boolean isDisplayIds()
	{
		return pnlVehicleSettings.isDisplayIds();
	}
        
        /**
         * Get if vehicle wraps when it collides with plain wall
         * 
         * @return true if vehicle wraps (vs. reflects) on wall collision
         */
        boolean isWrappingPlain()
        {
                return pnlVehicleSettings.isWrappingPlain();
        }

	/**
	 * Get if vehicle sensors should be displayed
	 * 
	 * @return true if vehicle sensors should be displayed
	 */
	boolean isDisplaySensors()
	{
		return pnlVehicleSettings.isDisplaySensors();
	}

	/**
	 * Get the range angle of a sensor. This is the angle relative to the direction the sensor is
	 * facing. Perceptible objects outside of this range are not perceived by the sensor.
	 * 
	 * @return the sensor range angle, in radians
	 */
	double getSensorRangeAngle()
	{
		return pnlVehicleSettings.getSensorRangeAngle();
	}

	/**
	 * Get the sensor splay angle, that is, the angle that the sensor differs from the direction
	 * the vehicle is facing.
	 * 
	 * @return the sensor splay angle, in radians
	 */
	double getSensorSplayAngle()
	{
		return pnlVehicleSettings.getSensorSplayAngle();
	}

	/**
	 * Get the sensor gap, that is, how far apart the two sensors are on a vehicle.
	 * 
	 * @return the number of vehicles
	 */
	int getSensorGap()
	{
		return pnlVehicleSettings.getSensorGap();
	}

	/**
	 * Get angle increment, that is, the angle change per wheel step, in radians.
	 * 
	 * @return the angle increment, in radians
	 */
	double getAngleDelta()
	{
		return pnlVehicleSettings.getAngleDelta();
	}

	/**
	 * Get the maximum speed, that is, the maximum distance a vehicle can travel in one turn
	 * 
	 * @return the maximum speed
	 */
	int getMaxSpeed()
	{
		return pnlVehicleSettings.getMaxSpeed();
	}

	/**
	 * Get the maximum angle, that is, the maximum angle that a vehicle can move in one turn
	 * 
	 * @return the maximum angle, in radians
	 */
	double getMaxAngle()
	{
		return pnlVehicleSettings.getMaxAngle();
	}
	
	/**
	 * Get the width of the dark plain
	 * 
	 * @return the width of the dark plain, in pixels
	 */
	int getDarkPlainWidth()
	{
		return pnlMiscSettings.getDarkPlainWidth();
	}
	
	/**
	 * Get the height of the dark plain
	 * 
	 * @return the height of the dark plain, in pixels
	 */
	int getDarkPlainHeight()
	{
		return pnlMiscSettings.getDarkPlainHeight();
	}
	
	/**
	 * Get the light intensity
	 * 
	 * @return the light intensity
	 */
	int getLightIntensity()
	{
		return pnlMiscSettings.getLightIntensity();
	}
	
	/**
	 * Get the number of microseconds the timer sleeps
	 * 
	 * @return the number of microseconds the timer sleeps
	 */
	int getTimerPause()
	{
		return pnlMiscSettings.getTimerPause();
	}
}