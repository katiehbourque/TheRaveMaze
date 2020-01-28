package edu.wm.cs.cs301.abigaildanielandkatiebourque.gui;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Distance;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Maze;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.CardinalDirection;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot.Turn;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot.Direction;


/**
 * This class implements an robot driver algorithm Wizard
 *
 * @author KatieBourque and AbigailDaniel
 *
 */


public class Wizard implements RobotDriver {

    /**
     * Initialize height and width for the dimensions of the maze,
     * initialize the robot, the odometer to keep track of pathLength,
     * distance to keep track of the distance to the end of the maze,
     * direction to tell the robot when to turn, and initialize a controller object.
     */

    protected int height;
    protected int width;
    protected int odometer;
    protected BasicRobot robot;
    protected CardinalDirection direction;
    protected Distance distance;
    protected StatePlaying control;

    //constructor sets everything to 0
    public Wizard() {
        width = 0;
        height = 0;
        distance = null;
        odometer = 0;
    }

    /**
     *  sets robot as the BasicRobot object
     */
    @Override
    public void setRobot(Robot r) {
        robot = (BasicRobot) r;
    }

    /**
     * @return the robot
     */
    public BasicRobot getRobot() {
        this.setRobot(robot);
        return this.robot;
    }

    /**
     * sets the dimensions of the maze
     */
    @Override
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     *
     * @return the width of the maze
     */
    public int getWidth() {
        this.setDimensions(width, height);
        return this.width;
    }

    /**
     *
     * @return the height of the maze
     */
    public int getHeight() {
        this.setDimensions(width, height);
        return this.height;
    }

    /**
     * set the distance to exit
     */
    @Override
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     *
     * @return the distance
     */
    public Distance getDistance() {
        this.setDistance(distance);
        return this.distance;
    }

    /**
     * checks to see if any sensors have failed and been repaired,
     * and then updates the driver which sensors have been repaired
     * and are operational
     */
    @Override
    public void triggerUpdateSensorInformation() {
        if (robot.b_fail == true) {
            if (robot.backward == true) {
                robot.hasOperationalSensor(Direction.BACKWARD);
            }
        }
        if (robot.f_fail == true) {
            if (robot.forward == true) {
                robot.hasOperationalSensor(Direction.FORWARD);
            }
        }
        if (robot.l_fail == true) {
            if (robot.left == true) {
                robot.hasOperationalSensor(Direction.LEFT);
            }
        }
        if (robot.r_fail == true) {
            if (robot.right == true) {
                robot.hasOperationalSensor(Direction.RIGHT);
            }
        }
    }


    private void wizardMover(CardinalDirection nDirr, CardinalDirection robotDirr) {
        switch(robotDirr) {

            case East:
                switch(nDirr) {
                    case East:
                        robot.move(1, false);
                        break;
                    case North:
                        robot.rotate(Turn.RIGHT);
                        robot.move(1, false);
                        break;
                    case South:
                        robot.rotate(Turn.LEFT);
                        robot.move(1, false);
                        break;
                    case West:
                        robot.rotate(Turn.AROUND);
                        robot.move(1, false);
                        break;
                    default:
                        break;
                }
                break;


            case North:
                switch(nDirr) {
                    case East:
                        robot.rotate(Turn.LEFT);
                        robot.move(1, false);
                        break;
                    case North:
                        robot.move(1, false);
                        break;
                    case South:
                        robot.rotate(Turn.AROUND);
                        robot.move(1, false);
                        break;
                    case West:
                        robot.rotate(Turn.RIGHT);
                        robot.move(1, false);
                        break;
                    default:
                        break;

                }
                break;


            case South:
                switch(nDirr) {
                    case West:
                        robot.rotate(Turn.LEFT);
                        robot.move(1, false);
                        break;
                    case South:
                        robot.move(1, false);
                        break;
                    case North:
                        robot.rotate(Turn.AROUND);
                        robot.move(1, false);
                        break;
                    case East:
                        robot.rotate(Turn.RIGHT);
                        robot.move(1, false);
                        break;
                    default:
                        break;

                }
                break;



            case West:
                switch(nDirr) {
                    case West:
                        robot.move(1, false);
                        break;
                    case North:
                        robot.rotate(Turn.LEFT);
                        robot.move(1, false);
                        break;
                    case South:
                        robot.rotate(Turn.RIGHT);
                        robot.move(1, false);
                        break;
                    case East:
                        robot.rotate(Turn.AROUND);
                        robot.move(1, false);
                        break;
                    default:
                        break;
                }
                break;

        }
    }



    private CardinalDirection closerNeighbor() throws Exception {
        Maze mazeConfig = robot.getMaze().getMazeConfiguration();
        int[] neighborCordinates = mazeConfig.getNeighborCloserToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]);
        int dx = neighborCordinates[0] - robot.getCurrentPosition()[0];
        int dy = neighborCordinates[1] - robot.getCurrentPosition()[1];
        direction = CardinalDirection.getDirection(dx, dy);
        return direction;
    }

    private void robotExit() {
        if(robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT) == false && robot.canSeeThroughTheExitIntoEternity(Direction.LEFT) == false) {
            robot.move(1, false);
        }
        else if(robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT) == true) {
            robot.rotate(Turn.RIGHT);
            robot.move(1, false);
        }
        else {
            robot.rotate(Turn.LEFT);
            robot.move(1, false);
        }
    }

    /**
     * Drive robot to the exit using the right wizard algorithm
     * @return true if the exit is reached
     * @return false if it is not
     */
    @Override
    public boolean drive2Exit() throws Exception {
        CardinalDirection nDirr = null;
        CardinalDirection botDirr = null;
        while(robot.isAtExit() == false && robot.getBatteryLevel() > 0) {
            botDirr = robot.getCurrentDirection();
            nDirr = closerNeighbor();
            wizardMover(nDirr, botDirr);
        }
        if(robot.isAtExit() == true) {
            return true;
        }
        return false;
    }


       public boolean drive2ExitAnimation() throws Exception{
           CardinalDirection nDirr = null;
           CardinalDirection botDirr = null;
           botDirr = robot.getCurrentDirection(); //robots current direction
           nDirr = closerNeighbor();  // gets direction of the adj cell that has the closest path to the exit
           wizardMover(nDirr, botDirr);      //move robot
           if(robot.isAtExit() == true) {
               robotExit();
               return true;
           }
           return false;
       }


    /**
     * @return the amount of energy used by the driver
     */
    @Override
    public float getEnergyConsumption() {
        return 3000 - robot.getBatteryLevel();
    }

    /**
     * @return the path length of the driver
     */
    @Override
    public int getPathLength() {
        odometer = robot.getOdometerReading();
        return odometer;
    }

}
