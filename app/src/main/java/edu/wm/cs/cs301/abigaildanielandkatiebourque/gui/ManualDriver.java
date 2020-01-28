package edu.wm.cs.cs301.abigaildanielandkatiebourque.gui;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot.Direction;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Distance;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Constants.UserInput;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Robot.Turn;



public class ManualDriver implements RobotDriver {

    Robot robot;
    private int width;
    private int height;
    private Distance dist;
    private int odometer;


    public ManualDriver() {

        width = 0;
        height= 0;
        //robot = null;
        dist = null;
        odometer = 0;
    }

    @Override
    public void setRobot(Robot r) {
        //assertNotNull(r);
        robot = r;

    }

    @Override
    public void setDimensions(int width, int height) {
        // this is set the SimpleKeyListener whenever a key is pressed
        //assertNotNull(width);// makes sure that actual integers are passed in both assert() statements
        //assertNotNull(height);
        this.width = width;
        this.height = height;

    }

    @Override
    public void setDistance(Distance distance) {
        // this is set in the SimpleKeyListener whenever a key is pressed
        dist = distance;
        //dist = ((BasicRobot)robot).control.getMazeConfiguration().getMazedists();
    }

    @Override
    public boolean drive2Exit() throws Exception {
        // since it is manual the robot has to be at the exit and facing it
		/*if(robot.canSeeExit(Direction.FORWARD) && robot.isAtExit()) {
			return true;
		}*/
        return false;
    }

//    @Override
    public boolean drive2ExitAnimation() throws Exception{
        return false;
    }

    @Override
    public float getEnergyConsumption() {

        return 3000 - robot.getBatteryLevel();
    }

    @Override
    public int getPathLength() {
        // receive the information for how far the robot has traveled from the robot
        odometer = robot.getOdometerReading();

        return odometer;
    }


    /**
     * takes a key from simplekeylistener and tells the robot what to do with it
     * @param key
     * @return
     */
    public boolean keyDown(UserInput key) {
        switch(key) {
            case Down: // battery should lose 11 since its a half rotation and a movement
                robot.rotate(Turn.AROUND);
                robot.move(1, true);
                break;

            case Left:
                robot.rotate(Turn.LEFT);
                break;

            case Right:
                robot.rotate(Turn.RIGHT);
                break;

            case Up:
                robot.move(1, true);
                break;
            default:
                break;

        }
        return true;
    }


    /**
     * @return the value of the width variable in the ManualDriver
     */
    public int getWidth() {
        return width;
    }


    /**
     * @return the value of the height variable in the ManualDriver
     */
    public int getHeight() {
        return height;
    }


    /**
     * @return the value of dist variable in the ManualDriver
     */
    public Distance getDist() {
        return dist;

    }

    public void triggerUpdateSensorInformation(){

    }
}
