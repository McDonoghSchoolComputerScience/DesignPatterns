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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class simulates a sensor that fires when a light source or other perceptible object is in
 * range, as defined by the range angle. Note that this class is immutable.
 *
 * @author Douglas B. Caulkins
 */
class Sensor 
{
	/* Light strength to pulse conversion factors. */
	private final double multiplier;
	/* The maximum number of pulses this receptor can generate per unit time period */
	static final int PULSEMAX = 100;

	/* The configuration panel */
	private final ConfigurationPanel pnlConfiguration;
	/* The parent vehicle */
	private final Vehicle parentVehicle;
	/* The range angle, in radians */
	private final double halfAngle;
	
	/**
	 * Constructor
	 * 
	 * @param pnlConfig the panel containing various configured settings 
	 * @param parnt the parent vehicle
	 * @param ang the range angle of the sensor, in radians
	 */
	Sensor(ConfigurationPanel pnlConfig, Vehicle parnt, double ang)
	{
		pnlConfiguration = pnlConfig;
		parentVehicle = parnt;
		halfAngle = ang/2;

		/* Light strength to pulse conversion factors. I determined these from experimentation. */
		multiplier = pnlConfig.getLightIntensity() / 3;
	}

	/**
	 * Given all possible light sources and perceptible objects, determine the number of pulses
	 * this sensor generates.
	 * 
	 * @param sensorLocation the current location of this sensor
	 * @param sensorDirection the current direction of this sensor
	 * @param lstPerceptible the list of perceptible items
	 * @return the number of pulses generated by the receptor
	 */
	int determineTotalPulses(Point2D sensorLocation, double sensorDirection, 
			List<PerceptibleItem> lstPerceptible)
	{
		int pulses = 0;

		/* 
		 * Determine the number of pulses this sensor generates for each light source. Accumulate
		 * the pulse count.
		 */
		for (PerceptibleItem curPerceptible: lstPerceptible)
		{
			/* Don't count pulses from yourself */
			if (!curPerceptible.equals(parentVehicle))
			{
				pulses = determinePulsesWrappedAround(sensorLocation, sensorDirection, curPerceptible) + pulses;
				/* If the pulses exceed the pulse max, we're done! */
				if (pulses > PULSEMAX)
				{
					pulses = PULSEMAX;
					break;
				}
			}
		}
		/* There is always at least some ambient light, enough to generate one pulse */
		if (pulses == 0)
		{
			pulses = 1;
		}
		
		return pulses;
	}

	/**
	 * Determine the strength of a light source for this current location, wrapping around the 
	 * light source relative to the frame. In other words, if this sensor is pointing towards the
	 * right side of the frame, wrap over the edge to the left side of the frame, and possibly
	 * generate pulses for a light source located there.
	 * 
	 * @param sensorLocation the current location of this sensor
	 * @param sensorDirection the current direction of this sensor
	 * @param curPerceptible the current perceptible item
	 * @return the number of pulses generated by the receptor
	 */
	int determinePulsesWrappedAround(Point2D sensorLocation, double sensorDirection, 
			PerceptibleItem curPerceptible)
	{
		/* 
		 * If the sensor receives pulses from the light source in the current frame, no need to look any
		 * further, we're done.
		 */
		int pulses = determinePulses(sensorLocation, sensorDirection, curPerceptible);
		if (pulses <= 0)
		{
			int maxPulse = 0;
			/* 
			 * Determine if the sensor receives pulses from the light source when it is placed in the
			 * corresponding location in each of the eight frames surrounding the current frame. The eight
			 * frames are offset by the various combinations of the current frame dimensions. 
			 */
			Point2D wrappedLocation;
			LightSource wrappedLightSource;
			int xMax = pnlConfiguration.getDarkPlainWidth();
			int yMax = pnlConfiguration.getDarkPlainHeight();
			for (int x = -xMax; x < (2 * xMax); x = x + xMax)
			{
				for (int y = -yMax; y < (2 * yMax); y = y + yMax)
				{
					/* We've already tried the current frame, so skip doing it */
					if (!((x == 0) && (y == 0)))
					{
						wrappedLocation = new Point2D.Double((curPerceptible.getX() + x), (curPerceptible.getY() + y));
						/* This is an adequate substitute for any type of perceptible item */
						wrappedLightSource = new LightSource(pnlConfiguration, wrappedLocation);
						pulses = determinePulses(sensorLocation, sensorDirection, wrappedLightSource);

						/* 
						 * If the sensor angle is large, it may sense the light source from multiple
						 * shifted frames. I want the closest one, which will generate the most pulses.
						 */
						if (pulses > maxPulse)
						{
							maxPulse = pulses;
						}
					}
				}
			}
			pulses = maxPulse;
		}

		return pulses;
	}

	/**
	 * For a given light source, determine the strength of the light given the distance from this
	 * sensor. This is returned as a number of pulses. The more light a sensor receives, the more
	 * pulses the sensor generates.
	 * 
	 * @param sensorLocation the current location of this sensor
	 * @param sensorDirection the current direction of this sensor
	 * @param curPerceptible the current perceptible item
	 * @return the number of pulses generated by the receptor
	 */
	private int determinePulses(Point2D sensorLocation, double sensorDirection, PerceptibleItem curPerceptible)
	{
		int pulses = 0;

		/* Calculate the angle from the current location to the light source */
		double angleToLight = Math.PI - Math.atan2(sensorLocation.getY() - curPerceptible.getY(), 
				sensorLocation.getX() - curPerceptible.getX());
		if (angleToLight < 0)
		{
			angleToLight = angleToLight + Vehicle.TWOPI;
		}
		/* Determine the angle range */
		double angleUpperRange = sensorDirection + halfAngle;
		if (angleUpperRange > Vehicle.TWOPI)
		{
			angleUpperRange = angleUpperRange - Vehicle.TWOPI;
		}
		double angleLowerRange = sensorDirection - halfAngle;
		if (angleLowerRange < 0)
		{
			angleLowerRange = angleLowerRange + Vehicle.TWOPI;
		}

		/* Compare the light source angle with the angle range */
		boolean inRange;			
		if (angleUpperRange > angleLowerRange)
		{
			inRange = (angleToLight >= angleLowerRange) && (angleToLight <= angleUpperRange);
		}
		/* The angle range is above and below 0 degrees */
		else
		{
			inRange = (angleToLight >= angleLowerRange) || (angleToLight <= angleUpperRange);
		}

		if (inRange)
		{
			/* Get the distance of the sensor from the light source */
			double distance = sensorLocation.distance(new Point2D.Double(curPerceptible.getX(), 
					curPerceptible.getY()));
			/* Adjust the distance for very close distances, which can be problematic */
			if (distance < 1)
			{
				distance = 1;
			}
			/* Light strength decreases inversely as the distance from the light increases */
			double strength = curPerceptible.getIntensity() * (1 / distance);
			/* 
			 * Translate the light strength into a number of pulses. The following formula is derived
			 * from experimentation with different values, and seems to work well. 
			 */
			pulses = (int) ((strength / pnlConfiguration.getLightIntensity()) * multiplier);
		}

		return pulses;
	}
	
	/*
	 * A panel that displays the range and the light point, and writes to the console
	 * whether the light source is in range or not.
	 */
	static class TestPanel extends JPanel
	{
		private static final long serialVersionUID = -9134593447368308081L;
		
		private static final int LIGHTSIZE = 3;

		private final ConfigurationPanel pnlConfig;
		private final Sensor testSensor;
		private final Point2D sensorLocation;
		private final double sensorDirection;
		private LightSource lightSource;
		private int test = 0;

		/**
		 * Constructor
		 */
		TestPanel()
		{
			setMinimumSize(new Dimension(300, 300));
			setPreferredSize(new Dimension(300, 300));
			setBackground(Color.BLACK);
			setForeground(Color.WHITE);			

			/* Create the test sensor */
			pnlConfig = new ConfigurationPanel();
			testSensor = new Sensor(pnlConfig, null, Math.toRadians(45)); //!!! bogus
			sensorLocation = new Point2D.Double(100, 100);
			sensorDirection = Math.toRadians(0);
		}
		
		/**
		 * A series of tests demonstrating the correctness of the range determination
		 */
		void performNextTest()
		{
			boolean inRange;

			switch (test)
			{
			case 0:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(105, 101));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 1:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(105, 99));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 2:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(95, 101));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 3:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(95, 99));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 4:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(101, 105));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 5:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(99, 105));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 6:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(101, 95));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 7:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(99, 95));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 8:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(120, 99));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			case 9:
				lightSource = new LightSource(pnlConfig, new Point2D.Double(99, 120));
				inRange = testSensor.determinePulsesWrappedAround(sensorLocation, sensorDirection, lightSource) > 1;
				System.out.println("Point: " + lightSource + " In range:" + inRange);
				break;
			default:
			}
			
			repaint();
			test++;
		}

		/**
		 * Paint the test panel
		 * 
		 * @param g the graphics object
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			/* Draw the sensor */
			VehicleRenderer.drawSensor(g, sensorLocation, sensorDirection, pnlConfig.getSensorRangeAngle(),
					Color.WHITE);
			if (lightSource != null)
			{
				g.drawOval(((int) lightSource.getX() - LIGHTSIZE), ((int) lightSource.getY() - LIGHTSIZE),
						LIGHTSIZE, LIGHTSIZE);
			}
		}		
	}
	
	/**
	 * Demo the correctness of the sensor range calculation
	 */
	static void createAndShowGUI() 
	{
		/* Set up a frame to render the sensor range and points */
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Create the test panel */
		final TestPanel pnlTest = new TestPanel();
		
		/* Create a button to run each test */
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = -8793923827562995923L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				pnlTest.performNextTest();
			}
		});
		
		/* Add the button and test panel to the frame */
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(pnlTest, BorderLayout.CENTER);
		contentPane.add(btnNext, BorderLayout.SOUTH);
		
		/* Display the frame */
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Application entry point for testing this class
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{        
				createAndShowGUI();
			}
		});
	}
}