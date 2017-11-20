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

/**
 * This interface specifies the methods that a perceptible item must implement. Essentially this
 * item must supply three attributes: the x and y coordinates of its current position, and its intensity.
 * 
 * @author Douglas B. Caulkins
 */
interface PerceptibleItem
{
	/**
	 * Get the x coordinate of the current position
	 * 
	 * @return the x coordinate of the current position
	 */
	double getX();
	
	/**
	 * Get the y coordinate of the current position
	 * 
	 * @return the y coordinate of the current position
	 */
	double getY();
	
	/**
	 * Get the intensity of this item, a measure of how strongly it registers with a Sensor
	 * 
	 * @return the intensity of this item
	 */
	int getIntensity();
}