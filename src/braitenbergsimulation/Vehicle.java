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
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * This class simulates a Braitenberg vehicle, a simple vehicle with two sensors
 * on the front and two stepping wheels in back. The way the sensors are
 * connected to the stepping wheels results in interesting behaviors. These
 * vehicles are based on the thought experiments described in Valentino
 * Braitenberg's book "Vehicles: Experiments in Synthetic Psychology".
 *
 * Note that I've implemented the connections between the sensors and the
 * stepping wheels as optimizations of the original proposed design, so, for
 * instance, there are no simulated neural networks. I did this to improve the
 * performance of the simulated vehicles. The behavior of the various vehicle
 * types is the same.
 *
 * @author Douglas B. Caulkins
 */
abstract class Vehicle implements PerceptibleItem {
    /* Useful constants */

    static final double HALFPI = Math.PI / 2;
    static final double TWOPI = Math.PI * 2;

    /* For generating the vehicle identifiers */
    private static int idgenerator = 0;

    /* The angle of a sensor */
    private final double sensorRangeAngle;
    /* The angle a sensor splays out from the vehicle direction */
    private final double sensorSplayAngle;
    /* The gap between the two sensors */
    private final int sensorGap;
    /* The angle increment is the angle change per wheel step */
    private final double angleIncrement;
    /* The maximum speed is the maximum distance a vehicle can move in one turn */
    private final int maxSpeed;
    /* The maximum angle is the maximum amount the vehicle direction can change in one turn */
    private final double maxAngle;
    /* If true, display the vehicle id on the rendered vehicle */
    private final boolean displayID;
    /* Set this to true for more information useful for tracking down problems */
    private final boolean displaySensors;
    /* Width of the dark plain */
    private final int xMax;
    /* Height of the dark plain */
    private final int yMax;

    /* wrapping around plain (vs. reflecting) */
    private final boolean isWrappingPlain;

    /* The vehicle identifier */
    protected int id;

    /* The vehicle sensors */
    private final Sensor rightSensor;
    private final Sensor leftSensor;

    /* How bright this vehicle is, in other words, how easily it is perceived */
    private final int intensity;

    /* The current location and direction */
    private Point2D currentLocation;
    private double currentDirection;

    /* The newly calculate location and direction */
    private Point2D newLocation;
    private double newDirection;

    /**
     * Braitenberg vehicle 2a Create a vehicle where the right sensor steps the
     * right wheel, the left sensor steps the left wheel. Excitatory - wheel
     * steps increase as light strength increases.
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     * @return the created vehicle
     */
    static Vehicle createUncrossedExcitatoryVehicle(ConfigurationPanel pnlConfig,
            Point2D loc, double dir) {
        return new UncrossedExcitatoryVehicle(pnlConfig, loc, dir);
    }

    /**
     * Braitenberg vehicle 2b Create a vehicle where the right sensor steps the
     * left wheel, the left sensor steps the right wheel. Excitatory - wheel
     * steps increase as light strength increases.
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     * @return the created vehicle
     */
    static Vehicle createCrossedExcitatoryVehicle(ConfigurationPanel pnlConfig,
            Point2D loc, double dir) {
        return new CrossedExcitatoryVehicle(pnlConfig, loc, dir);
    }

    /**
     * Braitenberg vehicle 3a Create a vehicle where the right sensor steps the
     * right wheel, the left sensor steps the left wheel. Inhibitory - wheel
     * steps decrease as light strength increases.
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     * @return the created vehicle
     */
    static Vehicle createUncrossedInhibitoryVehicle(ConfigurationPanel pnlConfig,
            Point2D loc, double dir) {
        return new UncrossedInhibitoryVehicle(pnlConfig, loc, dir);
    }

    /**
     * Braitenberg vehicle 3b Create a vehicle where the right sensor steps the
     * left wheel, the left sensor steps the right wheel. Inhibitory - wheel
     * steps decrease as light strength increases.
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     * @return the created vehicle
     */
    static Vehicle createCrossedInhibitoryVehicle(ConfigurationPanel pnlConfig,
            Point2D loc, double dir) {
        return new CrossedInhibitoryVehicle(pnlConfig, loc, dir);
    }

    /**
     * Braitenberg vehicle 4b Create a vehicle where the right sensor steps the
     * right wheel, the left sensor steps the left wheel. Threshold - wheel
     * steps increase as light strength increases until a threshold is met, then
     * wheel steps decrease as light strength increases.
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     * @return the created vehicle
     */
    static Vehicle createUncrossedThresholdVehicle(ConfigurationPanel pnlConfig,
            Point2D loc, double dir) {
        return new UncrossedThresholdVehicle(pnlConfig, loc, dir);
    }

    /**
     * Braitenberg vehicle 4b Create a vehicle where the right sensor steps the
     * left wheel, the left sensor steps the right wheel. Threshold - wheel
     * steps increase as light strength increases until a threshold is met, then
     * wheel steps decrease as light strength increases.
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     * @return the created vehicle
     */
    static Vehicle createCrossedThresholdVehicle(ConfigurationPanel pnlConfig,
            Point2D loc, double dir) {
        return new CrossedThresholdVehicle(pnlConfig, loc, dir);
    }

    /**
     * Constructor
     *
     * @param pnlConfig the panel containing various configured settings
     * @param loc the starting location
     * @param dir the starting direction
     */
    protected Vehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
        sensorRangeAngle = pnlConfig.getSensorRangeAngle();
        sensorSplayAngle = pnlConfig.getSensorSplayAngle();
        sensorGap = pnlConfig.getSensorGap();
        angleIncrement = pnlConfig.getAngleDelta();
        maxSpeed = pnlConfig.getMaxSpeed();
        maxAngle = pnlConfig.getMaxAngle();
        displayID = pnlConfig.isDisplayIds();
        displaySensors = pnlConfig.isDisplaySensors();
        intensity = pnlConfig.getLightIntensity();
        xMax = pnlConfig.getDarkPlainWidth();
        yMax = pnlConfig.getDarkPlainHeight();

        currentLocation = loc;
        currentDirection = dir;

        isWrappingPlain = pnlConfig.isWrappingPlain(); /* for now, vehicles wrap around plain */

        id = idgenerator++;
        rightSensor = new Sensor(pnlConfig, this, sensorRangeAngle);
        leftSensor = new Sensor(pnlConfig, this, sensorRangeAngle);
    }

    /**
     * Move the vehicle forward based on pulses from the two sensors.
     *
     * @param lstPerceptible the list of perceptible items
     */
    void move(List<PerceptibleItem> lstPerceptible) {
        /* 
         * Determine the current location and direction of the sensors and accumulate the light
         * strength gathered by each sensor
         */
        Point2D rightSensorLocation = getRightSensorLocation(sensorGap, currentLocation, currentDirection);
        Point2D leftSensorLocation = getLeftSensorLocation(sensorGap, currentLocation, currentDirection);
        double rightSensorDirection = getRightSensorDirection(currentDirection, sensorSplayAngle);
        double leftSensorDirection = getLeftSensorDirection(currentDirection, sensorSplayAngle);
        int rightLightPulses = rightSensor.determineTotalPulses(rightSensorLocation,
                rightSensorDirection, lstPerceptible);
        int leftLightPulses = leftSensor.determineTotalPulses(leftSensorLocation,
                leftSensorDirection, lstPerceptible);

        /* Translate the sensor strengths to wheel steps */
        Point2D steps = translatePulsesToSteps(rightLightPulses, leftLightPulses);
        /* Get the results from the "gypsy wagon" */
        double rightSteps = steps.getX();
        double leftSteps = steps.getY();

        /*
         * Braitenberg vehicles move forward by stepping the left wheel and the right wheel forward
         * by small increments as impulses from the sensors are received. Rather than actually
         * simulating the movement forward and the angle change for each left wheel and right wheel
         * step, I approximate the movements to generate the new position. This optimization works
         * well enough.
         */
        double totalSteps = rightSteps + leftSteps;
        double distanceTraveled = totalSteps / 2;
        if (distanceTraveled > maxSpeed) {
            distanceTraveled = maxSpeed;
        }
        double deltaAngle = (rightSteps - leftSteps) * angleIncrement;
        if (deltaAngle > maxAngle) {
            deltaAngle = maxAngle;
        }
        if (deltaAngle < -maxAngle) {
            deltaAngle = -maxAngle;
        }

        /* Set the new location and new direction */
        newLocation = getNewLocation(currentLocation, distanceTraveled,
                (currentDirection + (deltaAngle / 2)));
        
        if (!isWrappingPlain && isOffBoundingPlain(newLocation, xMax, yMax)) {
            // wrapping and hit a boundary... reset the location and compute the reflecting angle
            //
            double deltaX = newLocation.getX() - currentLocation.getX();
            double deltaY = newLocation.getY() - currentLocation.getY();
            if (newLocation.getX() < 0.0 || newLocation.getX() > xMax)
                deltaX = -deltaX;
            else
                deltaY = -deltaY;

            newLocation = currentLocation;
            newDirection = Math.atan2 (-deltaY, deltaX);
        }
        else {
            // either not wrapping or not on at a wall...
            //
            newLocation = wrapAroundLocation(newLocation, yMax, xMax);
            newDirection = currentDirection + deltaAngle;
        }
        
        if (newDirection > TWOPI) {
            newDirection = currentDirection - TWOPI;
        }
        if (currentDirection < 0) {
            newDirection = currentDirection + TWOPI;
        }
    }

    /**
     * Translate the light strength to wheel steps.
     *
     * @param rightPulses the right light strength
     * @param leftPulses the left light strength
     * @return a "gypsy wagon" holding the right and left wheel steps
     */
    protected abstract Point2D translatePulsesToSteps(int rightPulses, int leftPulses);

    /**
     * Render the vehicle as a triangle pointing in the direction of travel.
     * Also include the vehicle id.
     *
     * @param g the graphics object
     */
    void draw(Graphics g) {
        VehicleRenderer.drawVehicle(g, currentLocation, currentDirection, getVehicleColor());

        if (displayID) {
            g.drawString(Integer.toString(id), (int) currentLocation.getX(), (int) currentLocation.getY());
        }

        /* Render the sensors if debugging */
        if (displaySensors) {
            Point2D rightSensorLocation = getRightSensorLocation(sensorGap, currentLocation, currentDirection);
            Point2D leftSensorLocation = getLeftSensorLocation(sensorGap, currentLocation, currentDirection);
            double rightSensorDirection = getRightSensorDirection(currentDirection, sensorSplayAngle);
            double leftSensorDirection = getLeftSensorDirection(currentDirection, sensorSplayAngle);

            VehicleRenderer.drawSensor(g, rightSensorLocation, rightSensorDirection, sensorRangeAngle,
                    Color.WHITE);
            VehicleRenderer.drawSensor(g, leftSensorLocation, leftSensorDirection, sensorRangeAngle,
                    Color.WHITE);
        }
    }

    /**
     * Get the color to render this vehicle type
     *
     * @return the color of this vehicle type
     */
    protected abstract Color getVehicleColor();

    /**
     * Get this vehicle type
     *
     * @return the vehicle type
     */
    protected abstract String getVehicleType();

    /**
     * Get the right sensor location, based on the vehicle direction and
     * location
     *
     * @return the right sensor location
     */
    static Point2D getRightSensorLocation(int sensorGap, Point2D location, double direction) {
        Point2D sensorLocation;

        if (sensorGap == 0) {
            sensorLocation = new Point2D.Double(location.getX(), location.getY());
        } else {
            sensorLocation = getNewLocation(location, (sensorGap / 2), (direction
                    + HALFPI));
        }

        return sensorLocation;
    }

    /**
     * Get the left sensor location, based on the vehicle direction and location
     *
     * @return the left sensor location
     */
    static Point2D getLeftSensorLocation(int sensorGap, Point2D location, double direction) {
        Point2D sensorLocation;

        if (sensorGap == 0) {
            sensorLocation = new Point2D.Double(location.getX(), location.getY());
        } else {
            sensorLocation = getNewLocation(location, (sensorGap / 2), (direction
                    - HALFPI));
        }

        return sensorLocation;
    }

    /**
     * Get the right sensor direction, based on the vehicle direction
     *
     * @param direction the vehicle direction
     * @param angle the splay angle
     * @return the right sensor direction
     */
    static double getRightSensorDirection(double direction, double splayAngle) {
        return direction + splayAngle;
    }

    /**
     * Get the left sensor direction, based on the vehicle direction
     *
     * @param direction the vehicle direction
     * @param splayAngle the splay angle
     * @return the left sensor direction
     */
    static double getLeftSensorDirection(double direction, double splayAngle) {
        return direction - splayAngle;
    }

    /**
     * Get the x coordinate of the current position
     *
     * @return the x coordinate of the current position
     */
    public double getX() {
        return currentLocation.getX();
    }

    /**
     * Get the y coordinate of the current position
     *
     * @return the y coordinate of the current position
     */
    public double getY() {
        return currentLocation.getY();
    }

    /**
     * Get the y coordinate of the current position
     *
     * @return the y coordinate of the current position
     */
    public int getIntensity() {
        return intensity;
    }

    /**
     * Update the location and the direction
     */
    void updateLocationDirection() {
        currentLocation = newLocation;
        currentDirection = newDirection;
    }

    /**
     * Utility program for generating a new location given a current location, a
     * direction and a distance to travel.
     *
     * @param currentPoint the current location
     * @param distance the distance to travel
     * @param direction the direction to travel in
     * @return the new location
     */
    static Point2D getNewLocation(Point2D currentPoint, double distance, double direction) {
        double deltaX = (Math.sin(direction + HALFPI) * distance);
        double deltaY = (Math.cos(direction + HALFPI) * distance);

        double newX = currentPoint.getX() + deltaX;
        double newY = currentPoint.getY() + deltaY;

        return new Point2D.Double(newX, newY);
    }

    /**
     * Wrap around the current point if it's gone off the edge of the dark
     * plain.
     *
     * @param currentPoint the current location
     * @return the wrapped location, which may be the same as the original if it
     * wasn't wrapped
     */
    static private Point2D wrapAroundLocation(Point2D currentPoint, int height, int width) {
        double newX = currentPoint.getX();
        /* Beyond the east side */
        if (newX > width) {
            newX = newX - width;
        } /* Beyond the west side */ else if (newX < 0.0) {
            newX = width + newX;
        }

        double newY = currentPoint.getY();
        /* Beyond the south side */
        if (newY > height) {
            newY = newY - height;
        } /* Beyond the north side */ else if (newY < 0.00) {
            newY = height + newY;
        }

        return new Point2D.Double(newX, newY);
    }

    /**
     *
     * @param currentPoint computed "next" location...
     * @return true if the currentPoint is outside of bounding plain
     */
    static private boolean isOffBoundingPlain(Point2D aPoint, int width, int height) {
        double aX = aPoint.getX();
        double aY = aPoint.getY();
        return (aX < 0.0 || aX > width || aY < 0.0 || aY > height);
    }

    /**
     * A useful string representation of this vehicle
     */
    @Override
    public String toString() {
        return "ID: " + id + "Type: " + getVehicleType() + " Location: " + currentLocation + " Direction: "
                + Math.toDegrees(currentDirection);
    }

    /**
     * Compare a passed in object with this vehicle for equality. All I care
     * about is the vehicle id.
     *
     * @param o the object to compare for equality with this vehicle
     * @return true if this vehicle equals the passed in vehicle
     */
    @Override
    public boolean equals(Object o) {
        boolean equal = false;

        if (this == o) {
            equal = true;
        } else if (o instanceof Vehicle) {
            Vehicle curVehicle = (Vehicle) o;
            /* All that matters is the vehicle id */
            equal = id == curVehicle.id;
        }

        return equal;
    }

    /**
     * Generate a hashcode
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = 17;

        int c = id;
        result = 31 * result + c;

        return result;
    }

    /**
     * Braitenberg vehicle 2a - Cowardly, veers away from light, slow in the
     * dark.
     *
     * @author Douglas B. Caulkins
     */
    static class UncrossedExcitatoryVehicle extends Vehicle {

        /**
         * Constructor
         *
         * @param pnlConfig the panel containing various configured settings
         * @param loc the starting location
         * @param dir the starting direction
         */
        UncrossedExcitatoryVehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
            super(pnlConfig, loc, dir);
        }

        /**
         * Translate the light strength to wheel steps.
         *
         * @param rightPulses the right light strength
         * @param leftPulses the left light strength
         * @return a "gypsy wagon" holding the right and left wheel steps
         */
        @Override
        protected Point2D translatePulsesToSteps(int rightPulses, int leftPulses) {
            Point2D gypsyWagon;

            /* 
             * The right sensor steps the right wheel, the left sensor steps the left wheel.
             * Wheel steps increase as light strength increases.
             */
            gypsyWagon = new Point2D.Double(leftPulses, rightPulses);

            return gypsyWagon;
        }

        /**
         * Get the color to render this vehicle type
         *
         * @return the color of this vehicle type
         */
        @Override
        protected Color getVehicleColor() {
            return getVehicleColorStatic();
        }

        static Color getVehicleColorStatic() {
            return Color.BLUE;
        }

        /**
         * Get this vehicle type
         *
         * @return the vehicle type
         */
        protected String getVehicleType() {
            return getVehicleTypeStatic();
        }

        static String getVehicleTypeStatic() {
            return "2a - Cowardly";
        }
    }

    /**
     * Braitenberg vehicle 2b - Aggressive, steers towards light, speeding up.
     *
     * @author Douglas B. Caulkins
     */
    static class CrossedExcitatoryVehicle extends Vehicle {

        /**
         * Constructor
         *
         * @param pnlConfig the panel containing various configured settings
         * @param loc the starting location
         * @param dir the starting direction
         */
        CrossedExcitatoryVehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
            super(pnlConfig, loc, dir);
        }

        /**
         * Translate the light strength to wheel steps.
         *
         * @param rightPulses the right light strength
         * @param leftPulses the left light strength
         * @return a "gypsy wagon" holding the right and left wheel steps
         */
        @Override
        protected Point2D translatePulsesToSteps(int rightPulses, int leftPulses) {
            Point2D gypsyWagon;

            /* 
             * The right sensor steps the left wheel, the left sensor steps the right wheel 
             * Wheel steps increase as light strength increases.
             */
            gypsyWagon = new Point2D.Double(rightPulses, leftPulses);

            return gypsyWagon;
        }

        /**
         * Get the color to render this vehicle type
         *
         * @return the color of this vehicle type
         */
        @Override
        protected Color getVehicleColor() {
            return getVehicleColorStatic();
        }

        static Color getVehicleColorStatic() {
            return Color.RED;
        }

        /**
         * Get this vehicle type
         *
         * @return the vehicle type
         */
        protected String getVehicleType() {
            return getVehicleTypeStatic();
        }

        static String getVehicleTypeStatic() {
            return "2b - Aggressive";
        }
    }

    /**
     * Braitenberg vehicle 3a - Quietly Adoring, steers towards light, slows
     * down.
     *
     * @author Douglas B. Caulkins
     */
    static class UncrossedInhibitoryVehicle extends Vehicle {

        /**
         * Constructor
         *
         * @param pnlConfig the panel containing various configured settings
         * @param loc the starting location
         * @param dir the starting direction
         */
        UncrossedInhibitoryVehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
            super(pnlConfig, loc, dir);
        }

        /**
         * Translate the light strength to wheel steps.
         *
         * @param rightPulses the right light strength
         * @param leftPulses the left light strength
         * @return a "gypsy wagon" holding the right and left wheel steps
         */
        @Override
        protected Point2D translatePulsesToSteps(int rightPulses, int leftPulses) {
            int rightLightPulses = rightPulses;
            int leftLightPulses = leftPulses;
            Point2D gypsyWagon;

            /* 
             * The right sensor steps the right wheel, the left sensor steps the left wheel.
             * Wheel steps decrease as light strength increases.
             */
            rightLightPulses = Sensor.PULSEMAX - rightLightPulses;
            leftLightPulses = Sensor.PULSEMAX - leftLightPulses;
            gypsyWagon = new Point2D.Double(leftLightPulses, rightLightPulses);

            return gypsyWagon;
        }

        /**
         * Get the color to render this vehicle type
         *
         * @return the color of this vehicle type
         */
        @Override
        protected Color getVehicleColor() {
            return getVehicleColorStatic();
        }

        static Color getVehicleColorStatic() {
            return Color.GREEN.darker().darker();
        }

        /**
         * Get this vehicle type
         *
         * @return the vehicle type
         */
        protected String getVehicleType() {
            return getVehicleTypeStatic();
        }

        static String getVehicleTypeStatic() {
            return "3a - Adoring";
        }
    }

    /**
     * Braitenberg vehicle 3b - Exploring, steers away from light, speeds up.
     *
     * @author Douglas B. Caulkins
     */
    static class CrossedInhibitoryVehicle extends Vehicle {

        /**
         * Constructor
         *
         * @param pnlConfig the panel containing various configured settings
         * @param loc the starting location
         * @param dir the starting direction
         */
        CrossedInhibitoryVehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
            super(pnlConfig, loc, dir);
        }

        /**
         * Translate the light strength to wheel steps.
         *
         * @param rightPulses the right light strength
         * @param leftPulses the left light strength
         * @return a "gypsy wagon" holding the right and left wheel steps
         */
        @Override
        protected Point2D translatePulsesToSteps(int rightPulses, int leftPulses) {
            int rightLightPulses = rightPulses;
            int leftLightPulses = leftPulses;
            Point2D gypsyWagon;

            /* 
             * The right sensor steps the left wheel, the left sensor steps the right wheel.
             * Wheel steps decrease as light strength increases.
             */
            rightLightPulses = Sensor.PULSEMAX - rightLightPulses;
            leftLightPulses = Sensor.PULSEMAX - leftLightPulses;
            if (rightLightPulses < 1) {
                rightLightPulses = 1;
            }
            if (leftLightPulses < 1) {
                leftLightPulses = 1;
            }
            gypsyWagon = new Point2D.Double(rightLightPulses, leftLightPulses);

            return gypsyWagon;
        }

        /**
         * Get the color to render this vehicle type
         *
         * @return the color of this vehicle type
         */
        @Override
        protected Color getVehicleColor() {
            return getVehicleColorStatic();
        }

        static Color getVehicleColorStatic() {
            return Color.CYAN;
        }

        /**
         * Get this vehicle type
         *
         * @return the vehicle type
         */
        protected String getVehicleType() {
            return getVehicleTypeStatic();
        }

        static String getVehicleTypeStatic() {
            return "3b - Exploring";
        }
    }

    /**
     * Braitenberg vehicle 4b - Deciding
     *
     * @author Douglas B. Caulkins
     */
    static class UncrossedThresholdVehicle extends Vehicle {

        /**
         * Constructor
         *
         * @param pnlConfig the panel containing various configured settings
         * @param loc the starting location
         * @param dir the starting direction
         */
        UncrossedThresholdVehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
            super(pnlConfig, loc, dir);
        }

        /**
         * Translate the light strength to wheel steps.
         *
         * @param rightPulses the right light strength
         * @param leftPulses the left light strength
         * @return a "gypsy wagon" holding the right and left wheel steps
         */
        @Override
        protected Point2D translatePulsesToSteps(int rightPulses, int leftPulses) {
            int rightLightPulses = rightPulses;
            int leftLightPulses = leftPulses;
            Point2D gypsyWagon;

            /* 
             * The right sensor steps the right wheel, the left sensor steps the left wheel.
             * Threshold - wheel steps increase as light strength increases until a threshold is met, then
             * wheel steps decrease as light strength increases.
             */
            if (rightLightPulses > (Sensor.PULSEMAX / 2)) {
                rightLightPulses = Sensor.PULSEMAX - rightLightPulses;
            }
            if (leftLightPulses > (Sensor.PULSEMAX / 2)) {
                leftLightPulses = Sensor.PULSEMAX - leftLightPulses;
            }
            if (rightLightPulses < 1) {
                rightLightPulses = 1;
            }
            if (leftLightPulses < 1) {
                leftLightPulses = 1;
            }
            gypsyWagon = new Point2D.Double(leftLightPulses, rightLightPulses);

            return gypsyWagon;
        }

        /**
         * Get the color to render this vehicle type
         *
         * @return the color of this vehicle type
         */
        @Override
        protected Color getVehicleColor() {
            return getVehicleColorStatic();
        }

        static Color getVehicleColorStatic() {
            return Color.YELLOW;
        }

        /**
         * Get this vehicle type
         *
         * @return the vehicle type
         */
        protected String getVehicleType() {
            return getVehicleTypeStatic();
        }

        static String getVehicleTypeStatic() {
            return "4b - Deciding 1";
        }
    }

    /**
     * Braitenberg vehicle 4b - Deciding
     *
     * @author Douglas B. Caulkins
     */
    static class CrossedThresholdVehicle extends Vehicle {

        /**
         * Constructor
         *
         * @param pnlConfig the panel containing various configured settings
         * @param loc the starting location
         * @param dir the starting direction
         */
        CrossedThresholdVehicle(ConfigurationPanel pnlConfig, Point2D loc, double dir) {
            super(pnlConfig, loc, dir);
        }

        /**
         * Translate the light strength to wheel steps.
         *
         * @param rightPulses the right light strength
         * @param leftPulses the left light strength
         * @return a "gypsy wagon" holding the right and left wheel steps
         */
        @Override
        protected Point2D translatePulsesToSteps(int rightPulses, int leftPulses) {
            int rightLightPulses = rightPulses;
            int leftLightPulses = leftPulses;
            Point2D gypsyWagon;

            /* 
             * The right sensor steps the left wheel, the left sensor steps the right wheel.
             * Threshold - wheel steps increase as light strength increases until a threshold is met, then
             * wheel steps decrease as light strength increases.
             */
            if (rightLightPulses > (Sensor.PULSEMAX / 2)) {
                rightLightPulses = Sensor.PULSEMAX - rightLightPulses;
            }
            if (leftLightPulses > (Sensor.PULSEMAX / 2)) {
                leftLightPulses = Sensor.PULSEMAX - leftLightPulses;
            }
            if (rightLightPulses < 1) {
                rightLightPulses = 1;
            }
            if (leftLightPulses < 1) {
                leftLightPulses = 1;
            }
            gypsyWagon = new Point2D.Double(rightLightPulses, leftLightPulses);

            return gypsyWagon;
        }

        /**
         * Get the color to render this vehicle type
         *
         * @return the color of this vehicle type
         */
        @Override
        protected Color getVehicleColor() {
            return getVehicleColorStatic();
        }

        static Color getVehicleColorStatic() {
            return Color.PINK;
        }

        /**
         * Get this vehicle type
         *
         * @return the vehicle type
         */
        protected String getVehicleType() {
            return getVehicleTypeStatic();
        }

        static String getVehicleTypeStatic() {
            return "4b - Deciding 2";
        }
    }
}
