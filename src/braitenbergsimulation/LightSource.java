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

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * This class simulates a light source with an intensity. Note that this class is immutable.
 * Note that I don't return the Point2D because it's mutable.
 * 
 * @author Douglas B. Caulkins
 */
public class LightSource implements PerceptibleItem
{
	private static final int WIDTH = 6;
	private final Point2D location;
	private final int intensity;
	
	/**
	 * Constructor
	 * 
	 * @param pnlConfig the panel containing various configured settings 
	 * @param loc the location of the light source
	 */
	LightSource(ConfigurationPanel pnlConfig, Point2D loc)
	{
		location = loc;
		intensity = pnlConfig.getLightIntensity();
	}
	
	/**
	 * Get the location x coordinate of this light source
	 * 
	 * @return the x coordinate
	 */
	public double getX()
	{
		return location.getX();
	}
	
	/**
	 * Get the location y coordinate of this light source
	 * 
	 * @return the y coordinate
	 */
	public double getY()
	{
		return location.getY();
	}
	
	/**
	 * Get the intensity of this light source
	 * 
	 * @return the intensity
	 */
	public int getIntensity()
	{
		return intensity;
	}
	
	/**
	 * Render the light as a circle
	 * 
	 * @param g the graphics object
	 */
	void draw(Graphics g) 
	{
		int x = (int) (location.getX() - (WIDTH/2));
		int y = (int) (location.getY() - (WIDTH/2));
		g.drawOval(x, y, WIDTH, WIDTH);
	}
    
	/**
	 * Return a useful string representation of this light source
	 */
    @Override
	public String toString()
    {
		return "Light: " + location.toString() + " Intensity:" + intensity;
    }
}