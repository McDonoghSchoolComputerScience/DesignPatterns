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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

/**
 * This class simulates the dark plain populated with light sources and the various
 * Braitenberg vehicles.
 * 
 * @author Douglas B. Caulkins
 */
public class DarkPlainPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = -387481773815979595L;

	private int xMax;
	private int yMax;

	private final List<LightSource> lstLight;
	private final List<Vehicle> lstVehicle;
	private final List<PerceptibleItem> lstPerceptible;
	
	private CountDownLatch paintedGate;
	
	private final long sleepMs;
	private final ScheduledThreadPoolExecutor moveExecutor;
	private ScheduledFuture<?> moveTask;

	/**
	 * Constructor
	 * 
	 * @param pnlConfig the panel containing various configured settings 
	 */
	DarkPlainPanel(ConfigurationPanel pnlConfig)
	{
		xMax = pnlConfig.getDarkPlainWidth();
		yMax = pnlConfig.getDarkPlainHeight();
		sleepMs = pnlConfig.getTimerPause();
		
		/* Create the lists of lights, vehicles and perceptible objects */
		lstLight = new ArrayList<LightSource>();
		lstVehicle = new ArrayList<Vehicle>();
		lstPerceptible = new ArrayList<PerceptibleItem>();

		moveExecutor = new ScheduledThreadPoolExecutor(1);

		/* Set the basic colors of the dark plain */
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		
		/* Set the size of the dark plain */
		setMinimumSize(new Dimension(xMax, yMax));			
		setMaximumSize(new Dimension(xMax, yMax));			
		setPreferredSize(new Dimension(xMax, yMax));
	}
	
	/**
	 * Update the various settings of the vehicles in this dark plain
	 * 
	 * @param pnlConfiguration the number of various vehicles and lights
	 */
	void updateSettings(ConfigurationPanel pnlConfiguration)
	{
		xMax = pnlConfiguration.getDarkPlainWidth();
		yMax = pnlConfiguration.getDarkPlainHeight();
		
		/* Set the size of the dark plain */
		setMinimumSize(new Dimension(xMax, yMax));			
		setMaximumSize(new Dimension(xMax, yMax));			
		setPreferredSize(new Dimension(xMax, yMax));
		
		/* Clear out the lists */
		lstLight.clear();
		lstVehicle.clear();
		lstPerceptible.clear();
		
		/* Add a number of lights at random locations, based on the light count setting */
		for (int i = 0; i < pnlConfiguration.getLightSourceCount(); i++)
		{
			lstLight.add(new LightSource(pnlConfiguration, createRandomPoint()));				
		}

		/* 
		 * Add the various types of Braitenberg vehicles at random locations and 
		 * facing random directions.
		 */
		for (int i = 0; i < pnlConfiguration.getUncrossedExcitatoryCount(); i++)
		{
			lstVehicle.add(Vehicle.createUncrossedExcitatoryVehicle(pnlConfiguration,
					createRandomPoint(), Math.toRadians(Math.random() * 360)));
		}
		for (int i = 0; i < pnlConfiguration.getCrossedExcitatoryCount(); i++)
		{
			lstVehicle.add(Vehicle.createCrossedExcitatoryVehicle(pnlConfiguration,
					createRandomPoint(), Math.toRadians(Math.random() * 360)));
		}
		for (int i = 0; i < pnlConfiguration.getUncrossedInhibitoryCount(); i++)
		{
			lstVehicle.add(Vehicle.createUncrossedInhibitoryVehicle(pnlConfiguration,
					createRandomPoint(), Math.toRadians(Math.random() * 360)));
		}
		for (int i = 0; i < pnlConfiguration.getCrossedInhibitoryCount(); i++)
		{
			lstVehicle.add(Vehicle.createCrossedInhibitoryVehicle(pnlConfiguration,
					createRandomPoint(), Math.toRadians(Math.random() * 360)));
		}
		for (int i = 0; i < pnlConfiguration.getUncrossedThresholdCount(); i++)
		{
			lstVehicle.add(Vehicle.createUncrossedThresholdVehicle(pnlConfiguration,
					createRandomPoint(), Math.toRadians(Math.random() * 360)));
		}
		for (int i = 0; i < pnlConfiguration.getCrossedThresholdCount(); i++)
		{
			lstVehicle.add(Vehicle.createCrossedThresholdVehicle(pnlConfiguration,
					createRandomPoint(), Math.toRadians(Math.random() * 360)));
		}

		/*
		 * Add lights to the list of perceptible items, and vehicles if they are currently
		 * perceptible.
		 */
		if (pnlConfiguration.isPerceptibleVehicles())
		{
			for (PerceptibleItem aVehicle: lstVehicle)
			{
				lstPerceptible.add(aVehicle);
			}
		}
		for (PerceptibleItem aLight: lstLight)
		{
			lstPerceptible.add(aLight);
		}
	}

	/**
	 * Start the scheduled task that moves all the vehicles based on the input to their sensors.
	 */
	void start()
	{
		moveTask = moveExecutor.scheduleAtFixedRate(this, 0, sleepMs, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Stop the scheduled task that moves all the vehicles.
	 */
	void stop()
	{
		moveTask.cancel(false);
	}
	
	/**
	 * Determine if the scheduled task is running
	 * 
	 * @return true if the scheduled task is running
	 */
	boolean isRunning()
	{
		return ((moveTask != null) && !moveTask.isDone());
	}

	
	/**
	 * Move all the vehicles on this dark plain. Note that I use a CountDownLatch as a gate
	 * so that the paint of all the vehicles occurs before the vehicle locations are updated.
	 */
	public void run()
	{
		/* Calculate new vehicle positions */
		for (Vehicle aVehicle: lstVehicle)
		{
			aVehicle.move(lstPerceptible);
		}

		/* Create a new gate */
		paintedGate = new CountDownLatch(1);

		/* This schedules a repaint on the event queue */
		repaint();

		/* Wait for the paint to occur before proceeding */
		try
		{
			paintedGate.await();
		} 
		catch (InterruptedException iexc) 
		{
			iexc.printStackTrace();
		}

		/* Discard the old gate */
		paintedGate = null;

		/* Update all the vehicle positions */
		for (Vehicle aVehicle: lstVehicle)
		{
			aVehicle.updateLocationDirection();
		}			
	}

	/**
	 * Paint this component.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		/* Paint all the lights */
		for (LightSource aLight: lstLight)
		{
			aLight.draw(g);
		}
		/* Paint all the vehicles */
		for (Vehicle aVehicle: lstVehicle)
		{
			aVehicle.draw(g);
		}
		
		/* 
		 * Wait for the vehicle move to occur before proceeding.
		 * This is almost certain to have occurred already, in
		 * which case all threads waiting on the gate proceed.
		 */
		if (paintedGate != null)
		{
			paintedGate.countDown();
		}
	}

	/**
	 * Utility for creating a random location somewhere on the dark plain.
	 * 
	 * @return the random location
	 */
	private Point2D createRandomPoint()
	{
		int x = (int) (Math.random() * xMax);
		int y = (int) (Math.random() * yMax);
		return new Point2D.Double(x, y);
	}
}