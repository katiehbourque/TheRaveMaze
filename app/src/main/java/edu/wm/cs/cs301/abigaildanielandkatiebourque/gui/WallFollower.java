package edu.wm.cs.cs301.abigaildanielandkatiebourque.gui;


import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Distance;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Maze;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Floorplan;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.CardinalDirection;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot.Turn;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot.Direction;

/**
 *
 *
 * This class implements an algorithm that guides the robot through
 * the maze by following left walls.
 *
 * @author KatieBourque and AbigailDaniel
 *
 */

public class WallFollower implements RobotDriver {

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
    protected Maze maze;
    protected CardinalDirection direction;
    protected Distance distance;
    protected Floorplan floorplan;

    public WallFollower() {
        height = 0;
        width = 0;
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
        return this.robot;
    }

    /**
     * sets the dimensions of the maze
     */
    @Override
    public void setDimensions(int width, int height) {
        this.height = height;
        this.width = width;

    }

    /**
     *
     * @return the height of the maze
     */
    public int getHeight() {

        if (height == 0 && width == 0) {
            int h = maze.getHeight();
            int w = maze.getWidth();
            this.setDimensions(w, h);
        }
        return this.height;
    }

    /**
     *
     * @return the width of the maze
     */
    public int getWidth() {
        if (width == 0 && height == 0) {
            int w = maze.getWidth();
            int h = maze.getHeight();
            this.setDimensions(w, h);
        }
        return this.width;
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
        if (distance == null) {
            distance = maze.getMazedists();
            this.setDistance(distance);
        }
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

    /**
     * This algorithm tells the robot to move forward, following the right wall.
     * The robot will rotate right and move forward if there is space on the right.
     * If there is no space to move right or forward, the robot will either turn
     * and move left, or turn around and move forward.
     */
    private void leftWallFollower(int rightDist, int forwardDist, int leftDist) {
        if (forwardDist > 0 && leftDist == 0) {
            robot.move(1, false);
        }
        else if (leftDist > 0) {
            robot.rotate(Turn.LEFT);
            robot.move(1, false);
        }
        else if (forwardDist == 0 && leftDist == 0) {
            if (rightDist > 0) {
                robot.rotate(Turn.RIGHT);
                robot.move(1, false);
            }
            else if (rightDist == 0) {
                robot.rotate(Turn.AROUND);
                robot.move(1, false);
            }
        }
    }

    /**
     * Drive robot to the exit using the right wall follower algorithm
     * @return true if the exit is reached
     * @return false if it is not
     */
    @Override
    public boolean drive2Exit() throws Exception {
        while (robot.getBatteryLevel() > 0 && robot.isAtExit() == false) {
            int rightDist = robot.distanceToObstacle(Direction.RIGHT);
            int forwardDist = robot.distanceToObstacle(Direction.FORWARD);
            int leftDist = robot.distanceToObstacle(Direction.LEFT);
            leftWallFollower(rightDist, forwardDist, leftDist);
        }
        if (robot.isAtExit() == true) {
            if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT) == true) {
                robot.rotate(Turn.RIGHT);
                robot.move(1, false);
            }
            else if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT) == true) {
                robot.rotate(Turn.LEFT);
                robot.move(1, false);
            }
            else {
                robot.move(1, false);
            }
            return true;
        }
        return false;
    }

    /**
     * @return the amount of energy used by the driver
     */
    @Override
    public float getEnergyConsumption() {
        float consumption = 3000 - robot.getBatteryLevel();
        return consumption;
    }

    /**
     * @return the path length of the driver
     */
    @Override
    public int getPathLength() {
        odometer = robot.getOdometerReading();
        return odometer;
    }

    @Override
    public boolean drive2ExitAnimation() throws Exception {
        int rightDist = robot.distanceToObstacle(Direction.RIGHT);
        int forwardDist = robot.distanceToObstacle(Direction.FORWARD);
        int leftDist = robot.distanceToObstacle(Direction.LEFT);
        leftWallFollower(rightDist, forwardDist, leftDist);
        if (robot.isAtExit() == true) {
            if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT) == true) {
                robot.rotate(Turn.RIGHT);
                robot.move(1, false);
            }
            else if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT) == true) {
                robot.rotate(Turn.LEFT);
                robot.move(1, false);
            }
            else {
                robot.move(1, false);
            }
            return true;
        }
        return false;
    }

}
