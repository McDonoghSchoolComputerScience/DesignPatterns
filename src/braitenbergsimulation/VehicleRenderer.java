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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Polygon;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import braitenbergsimulation.Vehicle.CrossedExcitatoryVehicle;
import braitenbergsimulation.Vehicle.CrossedInhibitoryVehicle;
import braitenbergsimulation.Vehicle.CrossedThresholdVehicle;
import braitenbergsimulation.Vehicle.UncrossedExcitatoryVehicle;
import braitenbergsimulation.Vehicle.UncrossedInhibitoryVehicle;
import braitenbergsimulation.Vehicle.UncrossedThresholdVehicle;

/**
 * This class returns various renderings and images of the Braitenberg vehicles.
 * 
 * @author Douglas B. Caulkins
 */
public class VehicleRenderer 
{
	/* Point the vehicles west in the icons */
	private static final double STANDARDDIRECTION = Math.PI;
	/* The rendered length of a vehicle */
	private static final int VEHICLELENGTH = 15;
	/* The rendered angle of the vehicle apex */
	private static final double VEHICLEANGLE  = Math.toRadians(15);
	/* The range of a sensor, for rendering the sensor */
	private static final int SENSORRANGEDISTANCE = 100;
	
	private static final SensorVehiclePanel pnlSensorVehicle = new SensorVehiclePanel();

	/**
	 * Get a panel displaying a key for the various vehicles.
	 * 
	 * @return the panel displaying a vehicle key
	 */
	static JPanel getVehicleKeyPanel()
	{
		GridbagPanel pnlKey = new GridbagPanel();

		/* Braitenberg vehicle 2a */
		Color vehicleColor = UncrossedExcitatoryVehicle.getVehicleColorStatic();
		String vehicleType = UncrossedExcitatoryVehicle.getVehicleTypeStatic();
		JLabel lblVehicle = createIconLabel(vehicleColor, vehicleType);		
		pnlKey.add(lblVehicle, 0, 0, 1, 1, GridBagConstraints.WEST);

		/* Braitenberg vehicle 2b */
		vehicleColor = CrossedExcitatoryVehicle.getVehicleColorStatic();
		vehicleType = CrossedExcitatoryVehicle.getVehicleTypeStatic();
		lblVehicle = createIconLabel(vehicleColor, vehicleType);		
		pnlKey.add(lblVehicle, 1, 0, 1, 1, GridBagConstraints.WEST);

		/* Braitenberg vehicle 3a */
		vehicleColor = UncrossedInhibitoryVehicle.getVehicleColorStatic();
		vehicleType = UncrossedInhibitoryVehicle.getVehicleTypeStatic();
		lblVehicle = createIconLabel(vehicleColor, vehicleType);		
		pnlKey.add(lblVehicle, 2, 0, 1, 1, GridBagConstraints.WEST);

		/* Braitenberg vehicle 3b */
		vehicleColor = CrossedInhibitoryVehicle.getVehicleColorStatic();
		vehicleType = CrossedInhibitoryVehicle.getVehicleTypeStatic();
		lblVehicle = createIconLabel(vehicleColor, vehicleType);		
		pnlKey.add(lblVehicle, 3, 0, 1, 1, GridBagConstraints.WEST);

		/* Braitenberg vehicle 4b */
		vehicleColor = UncrossedThresholdVehicle.getVehicleColorStatic();
		vehicleType = UncrossedThresholdVehicle.getVehicleTypeStatic();
		lblVehicle = createIconLabel(vehicleColor, vehicleType);		
		pnlKey.add(lblVehicle, 4, 0, 1, 1, GridBagConstraints.WEST);

		/* Braitenberg vehicle 4b */
		vehicleColor = CrossedThresholdVehicle.getVehicleColorStatic();
		vehicleType = CrossedThresholdVehicle.getVehicleTypeStatic();
		lblVehicle = createIconLabel(vehicleColor, vehicleType);		
		pnlKey.add(lblVehicle, 5, 0, 1, 1, GridBagConstraints.WEST);

		return pnlKey;
	}
	
	/**
	 * Create a label containing the colored vehicle icon and the vehicle type text
	 * 
	 * @param vehicleColor the color of the vehicle
	 * @param vehicleText the vehicle type, a brief description
	 * @return the label with the vehicle icon and type text
	 */
	private static JLabel createIconLabel(Color vehicleColor, String vehicleText)
	{
		Icon vehicleIcon = new VehicleIcon(VEHICLELENGTH, VEHICLELENGTH, vehicleColor);
		return new JLabel(vehicleText, vehicleIcon, SwingConstants.LEFT);
	}
	
	/**
	 * Get the panel rendering a standard vehicle and the sensor range
	 * 
	 * @param pnlVehicleSettings the configured settings
	 * @return the panel rendering the vehicle and the sensor range
	 */
	static JPanel getSensorVehiclePanel(VehicleSettingsPanel pnlVehicleSettings)
	{
		pnlSensorVehicle.setConfiguration(pnlVehicleSettings);
		
		return pnlSensorVehicle;
	}

	/**
	 * Render the vehicle as a colored triangle pointing in the direction of travel
	 * 
	 * @param g the graphics
	 * @param location the vehicle location
	 * @param direction the current direction of the vehicle
	 * @param vehicleColor the vehicle color
	 */
	static void drawVehicle(Graphics g, Point2D location, double direction, Color vehicleColor)
	{
		/* Create the triangle */
		Polygon triangle = new Polygon();
		
		/* Add the vertex, the current location of the sensor */
		triangle.addPoint((int) location.getX(), (int) location.getY());

		/* Add the vertex of the upper range point */
		double angleUpperRange = direction + VEHICLEANGLE + Math.PI;
		Point2D newVertex = Vehicle.getNewLocation(location, VEHICLELENGTH, angleUpperRange);
		triangle.addPoint((int) newVertex.getX(), (int) newVertex.getY());

		/* Add the vertex of the lower range point */
		double angleLowerRange = direction - VEHICLEANGLE + Math.PI;
		newVertex = Vehicle.getNewLocation(location, VEHICLELENGTH, angleLowerRange);
		triangle.addPoint((int) newVertex.getX(), (int) newVertex.getY());

		Color savedForeground = g.getColor();
		g.setColor(vehicleColor);

		/* Draw the triangle */
		g.drawPolygon(triangle);

		g.setColor(savedForeground);
	}

	/**
	 * Render the range as an arc
	 * 
	 * @param g the graphics object
	 * @param sensorLocation the current range location
	 * @param sensorDirection the current range direction, in radians
	 */
	static void drawSensor(Graphics g, Point2D sensorLocation, double sensorDirection, double rangeAngle,
			Color sensorColor) 
	{
		Color savedForeground = g.getColor();
		g.setColor(sensorColor);

		/* Get the sensor coordinates */
		int sensorX = (int) sensorLocation.getX();
		int sensorY = (int) sensorLocation.getY();

		/* Determine and draw the upper range point coordinates */
		double halfAngle = rangeAngle / 2;
		double angleUpperRange = sensorDirection + halfAngle;
		Point2D newVertex = Vehicle.getNewLocation(sensorLocation, SENSORRANGEDISTANCE, angleUpperRange);
		int upperX = (int) newVertex.getX();
		int upperY = (int) newVertex.getY();
		g.drawLine(sensorX, sensorY, upperX, upperY);

		/*  Determine and draw the lower range point coordinates */
		double angleLowerRange = sensorDirection - halfAngle;
		newVertex = Vehicle.getNewLocation(sensorLocation, SENSORRANGEDISTANCE, angleLowerRange);
		int lowerX = (int) newVertex.getX();
		int lowerY = (int) newVertex.getY();
		g.drawLine(sensorX, sensorY, lowerX, lowerY);

		/* Draw the arc */
		int originX = sensorX - SENSORRANGEDISTANCE;
		int originY = sensorY - SENSORRANGEDISTANCE;
		int squareSide = SENSORRANGEDISTANCE * 2;
		g.drawArc(originX, originY, squareSide, squareSide, (int) Math.toDegrees(angleLowerRange), 
				(int) Math.toDegrees(rangeAngle));

		g.setColor(savedForeground);
	}

	/**
	 * Create a panel diagramming an uncrossed excitatory vehicle
	 * 
	 * @return the panel with the uncrossed excitatory vehicle diagram
	 */
	static JPanel getUncrossedExcitatoryPanel()
	{
		return new UncrossedExcitatoryPanel();
	}

	/**
	 * Create a panel diagramming an crossed excitatory vehicle
	 * 
	 * @return the panel with the crossed excitatory vehicle diagram
	 */
	static JPanel getCrossedExcitatoryPanel()
	{
		return new CrossedExcitatoryPanel();
	}

	/**
	 * Create a panel diagramming an uncrossed inhibitory vehicle
	 * 
	 * @return the panel with the uncrossed inhibitory vehicle diagram
	 */
	static JPanel getUncrossedInhibitoryPanel()
	{
		return new UncrossedInhibitoryPanel();
	}

	/**
	 * Create a panel diagramming an crossed inhibitory vehicle
	 * 
	 * @return the panel with the crossed inhibitory vehicle diagram
	 */
	static JPanel getCrossedInhibitoryPanel()
	{
		return new CrossedInhibitoryPanel();
	}

	/**
	 * Create a panel diagramming an uncrossed threshold vehicle
	 * 
	 * @return the panel with the uncrossed threshold vehicle diagram
	 */
	static JPanel getUncrossedThresholdPanel()
	{
		return new UncrossedThresholdPanel();
	}

	/**
	 * Create a panel diagramming an crossed threshold vehicle
	 * 
	 * @return the panel with the crossed threshold vehicle diagram
	 */
	static JPanel getCrossedThresholdPanel()
	{
		return new CrossedThresholdPanel();
	}
	
	/**
	 * A class for creating an icon containing a rendered vehicle
	 */
	private static class VehicleIcon implements Icon
	{
	    private final int width;
	    private final int height;
	    private final Color vehicleColor;
	    
	    /**
	     * Constructor
	     * 
	     * @param wdth the width of the vehicle icon
	     * @param hght the height of the vehicle icon
	     * @param clr the vehicle color
	     */
	    private VehicleIcon(int wdth, int hght, Color clr)
	    {
	    	width = wdth;
	    	height = hght;
	    	vehicleColor = clr;
	    }
	    
	    /**
	     * Draw the vehicle as a triangle with the proper vehicle color
	     * 
	     * @param c the component
	     * @param g the graphics
	     * @param x the x coordinate
	     * @param y the y coordinate
	     */
	    public void paintIcon(Component c, Graphics g, int x, int y) 
	    {
	    	Point2D apex = new Point2D.Double((x + 1), (y + (height / 2)));
	    	drawVehicle(g, apex, STANDARDDIRECTION, vehicleColor);
	    }
	    
	    public int getIconWidth()
	    {
	        return width;
	    }
	    
	    public int getIconHeight()
	    {
	        return height;
	    }
	}
	
	/*
	 * This class renders the vehicle and the sensor range
	 */
	private static class SensorVehiclePanel extends JPanel
	{
		private static final long serialVersionUID = -4368372612519979878L;

		private final int HEIGHT = 250;
		private final int WIDTH = 250;
		
		private VehicleSettingsPanel pnlVehicleSettings;
		
		/**
		 * Constructor
		 */
		private SensorVehiclePanel()
		{
			setMinimumSize(new Dimension(HEIGHT, WIDTH));
			setPreferredSize(new Dimension(HEIGHT, WIDTH));

			/* Set the basic colors of the dark plain */
			setBackground(Color.BLACK);
			setForeground(Color.WHITE);

			Border bdrLine = BorderFactory.createLineBorder(Color.WHITE);
			setBorder(bdrLine);
		}
		
		/**
		 * Set the current vehicle settings
		 * 
		 * @param pnlVehicleSttngs the vehicle settings
		 */
		private void setConfiguration(VehicleSettingsPanel pnlVehicleSttngs)
		{
			pnlVehicleSettings = pnlVehicleSttngs;
		}
		
		/**
		 * Paint this component.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			/* Draw the vehicle in the middle of the panel */
			Point2D apex = new Point2D.Double((WIDTH / 2), (HEIGHT / 2));
	    	drawVehicle(g, apex, Math.PI, Color.BLUE);
	    	
	    	if (pnlVehicleSettings != null)
	    	{
	    		int sensorGap = pnlVehicleSettings.getSensorGap();
	    		double rangeAngle = pnlVehicleSettings.getSensorRangeAngle();
	    		double splayAngle = pnlVehicleSettings.getSensorSplayAngle();
	    		Point2D rightSensorLocation = Vehicle.getRightSensorLocation(sensorGap, apex,
	    				STANDARDDIRECTION);
	    		Point2D leftSensorLocation = Vehicle.getLeftSensorLocation(sensorGap, apex,
	    				STANDARDDIRECTION);
	    		double rightSensorDirection = Vehicle.getRightSensorDirection(STANDARDDIRECTION, splayAngle);
	    		double leftSensorDirection = Vehicle.getLeftSensorDirection(STANDARDDIRECTION, splayAngle);
				drawSensor(g, rightSensorLocation, rightSensorDirection, rangeAngle, Color.YELLOW);
				drawSensor(g, leftSensorLocation, leftSensorDirection, rangeAngle, Color.WHITE);
	    	}
		}
	}
	
	/*
	 * This class renders the vehicle chassis to illustrate the wiring of each vehicle
	 */
	private static class ChassisVehiclePanel extends JPanel
	{
		private static final long serialVersionUID = -7729679189110154712L;

		private final int HEIGHT = 200;
		private final int WIDTH = 250;
		
		protected final Color WIRECOLOR = Color.YELLOW;
		protected final Color INHIBITORYCOLOR = Color.RED;
		protected final Color THRESHOLDCOLOR = Color.BLUE;
		
		/**
		 * Constructor
		 */
		private ChassisVehiclePanel()
		{
			setMinimumSize(new Dimension(WIDTH, HEIGHT));
			setPreferredSize(new Dimension(WIDTH, HEIGHT));

			/* Set the basic colors of the dark plain */
			setBackground(Color.BLACK);
			setForeground(Color.WHITE);

			Border bdrLine = BorderFactory.createLineBorder(Color.WHITE);
			setBorder(bdrLine);
		}
		
		/**
		 * Paint a diagrammatic vehicle.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			/* Vehicle triangle sides */
			g.drawLine(10, 100, 180, 50);
			g.drawLine(10, 100, 180, 150);
			g.drawLine(230, 60, 230, 140);
			
			/* The top wheel */
			Polygon topWheel = new Polygon();
			topWheel.addPoint(180, 40);
			topWheel.addPoint(180, 60);
			topWheel.addPoint(240, 60);
			topWheel.addPoint(240, 40);
			g.drawPolygon(topWheel);
			
			/* The bottom wheel */
			Polygon bottomWheel = new Polygon();
			bottomWheel.addPoint(180, 140);
			bottomWheel.addPoint(180, 160);
			bottomWheel.addPoint(240, 160);
			bottomWheel.addPoint(240, 140);
			g.drawPolygon(bottomWheel);
			
			/* Sensors */
			g.drawArc(30, 50, 26, 26, 90, -180);
			g.drawLine(43, 50, 43, 76);
			g.drawArc(30, 124, 26, 26, 90, -180);
			g.drawLine(43, 124, 43, 150);
			
			/* Connectors */
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setColor(WIRECOLOR);
			g2.setStroke(new BasicStroke(3));

			/* Upper sensor wire */
			CubicCurve2D cc = new CubicCurve2D.Double();
			cc.setCurve(56, 63, 79, 63, 79, 80, 100, 80);
			g2.draw(cc);
			
			/* Lower sensor wire */
			cc = new CubicCurve2D.Double();
			cc.setCurve(56, 137, 79, 137, 79, 120, 100, 120);
			g2.draw(cc);

			/* Upper wheel wire */
			QuadCurve2D qc = new QuadCurve2D.Double();
			qc.setCurve(180, 80, 210, 80, 210, 60);
			g2.draw(qc);
			
			/* Lower wheel wire */
			qc = new QuadCurve2D.Double();
			qc.setCurve(180, 120, 210, 120, 210, 140);
			g2.draw(qc);
		}
		
		protected void drawUncrossedWires(Graphics g)
		{
			g.drawLine(100, 80, 180, 80);
			g.drawLine(100, 120, 180, 120);			
		}
		
		protected void drawCrossedWires(Graphics g)
		{
			g.drawLine(100, 80, 180, 120);
			g.drawLine(100, 120, 180, 80);			
		}
	}
	
	private static class UncrossedExcitatoryPanel extends ChassisVehiclePanel
	{
		private static final long serialVersionUID = 8868603957052209431L;

		/**
		 * Paint the vehicle with uncrossed wires.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			drawUncrossedWires(g);
		}
	}
	
	private static class CrossedExcitatoryPanel extends ChassisVehiclePanel
	{
		private static final long serialVersionUID = 540206288100423446L;

		/**
		 * Paint the vehicle with crossed wires.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			drawCrossedWires(g);
		}
	}
	
	private static class UncrossedInhibitoryPanel extends ChassisVehiclePanel
	{
		private static final long serialVersionUID = 1729053593831152388L;

		/**
		 * Paint the vehicle with uncrossed inhibiting wires.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			g.setColor(INHIBITORYCOLOR);
			drawUncrossedWires(g);
		}
	}
	
	private static class CrossedInhibitoryPanel extends ChassisVehiclePanel
	{
		private static final long serialVersionUID = -5472689342588357748L;

		/**
		 * Paint the vehicle with crossed inhibiting wires.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			g.setColor(INHIBITORYCOLOR);
			drawCrossedWires(g);
		}
	}
	
	private static class UncrossedThresholdPanel extends ChassisVehiclePanel
	{
		private static final long serialVersionUID = -3379510066370287816L;

		/**
		 * Paint the vehicle with uncrossed threshold wires.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			g.setColor(THRESHOLDCOLOR);
			drawUncrossedWires(g);
		}
	}
	
	private static class CrossedThresholdPanel extends ChassisVehiclePanel
	{
		private static final long serialVersionUID = 360635710524601923L;

		/**
		 * Paint the vehicle with uncrossed threshold wires.
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			g.setColor(THRESHOLDCOLOR);
			drawCrossedWires(g);
		}
	}
	
	/**
	 * Demo the icons
	 */
	static void createAndShowGUI() 
	{
		/* Set up a frame to render the sensor range and points */
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pnlTest = new JPanel();
		ChassisVehiclePanel pnlChassisVehicle = new UncrossedExcitatoryPanel();
		pnlTest.add(pnlChassisVehicle);
		
		/* Add the button and test panel to the frame */
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(pnlTest, BorderLayout.CENTER);
		
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
