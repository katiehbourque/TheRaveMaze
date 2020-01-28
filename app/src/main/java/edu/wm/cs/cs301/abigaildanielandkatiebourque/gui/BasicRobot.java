package edu.wm.cs.cs301.abigaildanielandkatiebourque.gui;

import java.lang.AssertionError;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.CardinalDirection;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Floorplan;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Wallboard;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Constants.UserInput;

/**
 * BasicRobot.java implements Robot
 *
 * @author ABIGAIL DANIEL and KATIE BOURQUE
 *
 */


public class BasicRobot implements Robot {
    //inits
    public boolean backward;
    public boolean forward;
    public boolean left;
    public boolean right;
    public boolean b_fail;
    public boolean f_fail;
    public boolean l_fail;
    public boolean r_fail;

    private boolean room;
    private boolean nomove;

    private StatePlaying control;
    private Floorplan floorplan;

    private static int odometer = 0;
    private static float batterylevel;


    /**
     * Constructor sets battery level, sets sensors to true, and clears odometer.
     */
    public BasicRobot() {
        batterylevel = 3000;
        backward = true;
        forward = true;
        left = true;
        right = true;
        b_fail = false;
        f_fail = false;
        l_fail = false;
        r_fail = false;
        room = false;
        nomove = false;
        odometer = 0;

    }

    /**
     * Provides the current position as (x,y) coordinates for the maze cell as an array of length 2 with [x,y].
     * @postcondition 0 <= x < width, 0 <= y < height of the maze.
     * @return array of length 2, x = array[0], y=array[1]
     * @throws Exception if position is outside of the maze
     */
    @Override
    public int[] getCurrentPosition() throws Exception {
        return control.getCurrentPosition();
    }

    /**
     * Provides the current cardinal direction.
     * @return cardinal direction is robot's current direction in absolute terms
     */
    @Override
    public CardinalDirection getCurrentDirection() {
        float bat = this.getBatteryLevel();
        return control.getCurrentDirection();
    }

    /**
     * Provides the robot with a reference to the controller to cooperate with.
     * The robot memorizes the controller such that this method is most likely called only once
     * and for initialization purposes. The controller serves as the main source of information
     * for the robot about the current position, the presence of walls, the reaching of an exit.
     * The controller is assumed to be in the playing state.
     * @param controller is the communication partner for robot
     * @precondition controller != null, controller is in playing state and has a maze
     */
    @Override
    public void setMaze(StatePlaying controller) {
        control = controller;
    }

    public StatePlaying getMaze() {
        return control;
    }


    /**
     * Returns the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for distance2Obstacle may use less energy than a move forward operation.
     * If battery level <= 0 then robot stops to function and hasStopped() is true.
     * @return current battery level, level is > 0 if operational.
     */
    @Override
    public float getBatteryLevel() {
        return batterylevel;
    }

    /**
     * Sets the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for distance2Obstacle may use less energy than a move forward operation.
     * If battery level <= 0 then robot stops to function and hasStopped() is true.
     * @param level is the current battery level
     * @precondition level >= 0
     */
    @Override
    public void setBatteryLevel(float level) {
        batterylevel = level;
    }

    /**
     * Gets the distance traveled by the robot.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     * The counter can be reset to 0 with resetOdomoter().
     * @return the distance traveled measured in single-cell steps forward
     */
    @Override
    public int getOdometerReading() {
        float battery = this.getBatteryLevel();
        return odometer;
    }

    /**
     * Resets the odomoter counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
    @Override
    public void resetOdometer() {
        float battery = this.getBatteryLevel();
        odometer = 0;
    }

    /**
     * Gives the energy consumption for a full 360 degree rotation.
     * Scaling by other degrees approximates the corresponding consumption.
     * @return energy for a full rotation
     */
    @Override
    public float getEnergyForFullRotation() {
        int energy = 12;
        return energy;
    }

    /**
     * Gives the energy consumption for moving forward for a distance of 1 step.
     * For simplicity, we assume that this equals the energy necessary
     * to move 1 step backwards and that scaling by a larger number of moves is
     * approximately the corresponding multiple.
     * @return energy for a single step forward
     */
    @Override
    public float getEnergyForStepForward() {
        int energy = 5;
        return energy;
    }

    /**
     * Tells if a sensor can identify the exit in the given direction relative to
     * the robot's current forward direction from the current position.
     * @return true if the exit of the maze is visible in a straight line of sight
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     */
    @Override
    public boolean isAtExit() {
        float bat = this.getBatteryLevel();
        floorplan = control.getMazeConfiguration().getFloorplan();

        return floorplan.isExitPosition(control.getCurrentPosition()[0], control.getCurrentPosition()[1]);
    }

    /**
     * Tells if a sensor can identify the exit in the given direction relative to
     * the robot's current forward direction from the current position.
     * @return true if the exit of the maze is visible in a straight line of sight
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     */
    @Override
    public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
        float bat = batterylevel;

        //temporary batterylevel
        if (batterylevel > 0) {
            bat = batterylevel-1;
        }

        this.setBatteryLevel(batterylevel + 1);
        //if valid exit and enough battery,
        if (batterylevel >= 1 && distanceToObstacle(direction) == Integer.MAX_VALUE && hasOperationalSensor(direction) == true) {
            this.setBatteryLevel(bat - 1);
            if (bat < 1) {
                nomove = true;

            }
            return true;

        }
        //not enough battery complete move
        else if (bat < 1) {
            nomove = true;
            return false;
        }
        //else return
        else {
            this.setBatteryLevel(bat);
            return false;
        }
    }

    /**
     * Tells if current position is inside a room.
     * @return true if robot is inside a room, false otherwise
     * @throws UnsupportedOperationException if not supported by robot
     */
    @Override
    public boolean isInsideRoom() throws UnsupportedOperationException {
        float bat = this.getBatteryLevel();
        floorplan = control.getMazeConfiguration().getFloorplan();
        //if in room, return robot is inside a room
        if (hasRoomSensor() == true) {
            return floorplan.isInRoom(control.getCurrentPosition()[0], control.getCurrentPosition()[1]);
        }
        //else return false
        return false;
    }

    /**
     * Tells if the robot has a room sensor.
     * Set as false in constructor, so should be false unless it is changed.
     */
    @Override
    public boolean hasRoomSensor() {
        float bat = this.getBatteryLevel();
        return room;
    }

    /**
     * Tells if the robot has stopped for reasons like lack of energy, hitting an obstacle, etc.
     * Set as false in constructor, so should be false unless nomove was updated by move/rotate/jump function
     * @return true if the robot has stopped, false otherwise
     */
    @Override
    public boolean hasStopped() {
        float bat = this.getBatteryLevel();
        return nomove;
    }

    /**
     * Tells the distance to an obstacle (a wall)
     * in the given direction.
     * The direction is relative to the robot's current forward direction.
     * Distance is measured in the number of cells towards that obstacle,
     * e.g. 0 if the current cell has a wallboard in this direction,
     * 1 if it is one step forward before directly facing a wallboard,
     * Integer.MaxValue if one looks through the exit into eternity.
     * @param direction specifies the direction of the sensor
     * @return number of steps towards obstacle if obstacle is visible
     * in a straight line of sight, Integer.MAX_VALUE otherwise
     * @throws UnsupportedOperationException if the robot does not have
     * an operational sensor for this direction
     */
    @Override
    public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
        floorplan = control.getMazeConfiguration().getFloorplan();

        //sets cardinal direction //else error
        CardinalDirection currdir = this.getCurrentDirection();
        float bat = this.getBatteryLevel();

        if (this.hasOperationalSensor(direction) && batterylevel >= 1) {
            this.setBatteryLevel(batterylevel - 1);
            nomove = hasStopped();

            switch(direction){
                case FORWARD:
                    break;
                case BACKWARD:
                    currdir = currdir.oppositeDirection();
                    break;
                case RIGHT:
                    currdir = currdir.oppositeDirection();
                    currdir = currdir.rotateClockwise();
                    break;
                case LEFT:
                    currdir = currdir.rotateClockwise();
                    break;
                default:
                    throw new AssertionError(direction);
            }

            int x = control.getCurrentPosition()[0];
            int y = control.getCurrentPosition()[1];
            int distance = 0;

            while(!false) {
                //if robot is not in the maze or on exterior wall
                if (x >= floorplan.width || y >= floorplan.height || y < 0 || x < 0) {
                   //assertTrue(bat - 1 == this.getBatteryLevel());
                    return Integer.MAX_VALUE;
                }

                else {
                    switch(currdir) {
                        //checks if there is a wall in each direction
                        //then changes distance and x and y coordinates appropriately
                        case North:
                            if (floorplan.hasWall(x, y, CardinalDirection.North)) {
                               // assertTrue(bat - 1 == this.getBatteryLevel());
                                return distance;
                            }
                            distance++;
                            y--;
                            break;

                        case South:
                            if (floorplan.hasWall(x, y, CardinalDirection.South)) {
                               // assertTrue(bat - 1 == this.getBatteryLevel());
                                return distance;
                            }
                            distance++;
                            y++;
                            break;

                        case West:
                            if (floorplan.hasWall(x, y, CardinalDirection.West)) {
                                //assertTrue(bat - 1 == this.getBatteryLevel());
                                return distance;
                            }
                            distance++;
                            x--;
                            break;


                        case East:
                            if (floorplan.hasWall(x, y, CardinalDirection.East)) {
                               // assertTrue(bat - 1 == this.getBatteryLevel());
                                return distance;
                            }
                            distance++;
                            x++;
                            break;

                        //else error in direction
                        default:
                            throw new AssertionError(currdir);
                    }
                }
            }
        }

        //if not enough battery
        else {
            if (batterylevel < 1) {
               // this.setBatteryLevel(-1);
                //control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
            }
            //else exception
            throw new UnsupportedOperationException();

        }
    }


    /**
     * Tells if the robot has an operational distance sensor for the given direction.
     * The interface is generic and may be implemented with robots
     * that are more or less equipped with sensor or have sensors that
     * are subject to failures and repairs.
     * The purpose is to allow for a flexible robot driver to adapt
     * its driving strategy according the features it
     * finds supported by a robot.
     * @param direction specifies the direction of the sensor
     * @return true if robot has operational sensor, false otherwise
     */
    @Override
    public boolean hasOperationalSensor(Direction direction) {
        float bat = this.getBatteryLevel();
        //checks which direction  for sensor
        //returns the sensor (that is already set to true in constructor) and checks battery level
        if (direction == Direction.BACKWARD) {
           // assertTrue(bat == this.getBatteryLevel());
            return this.backward;
        }
        else if(direction == Direction.FORWARD) {
           // assertTrue(bat == this.getBatteryLevel());
            return this.forward;
        }
        else if(direction == Direction.LEFT) {
            //assertTrue(bat == this.getBatteryLevel());
            return this.left;
        }
        else if(direction == Direction.RIGHT) {
            //assertTrue(bat == this.getBatteryLevel());
            return this.right;
        }
        else {
            //assertTrue(bat == this.getBatteryLevel());
            throw new AssertionError(direction);
        }
    }

    /**
     * Makes the robot's distance sensor for the given direction fail.
     * Subsequent calls to measure the distance to an obstacle in
     * this direction will return with an exception.
     * If the robot does not have a sensor in this direction,
     * the method does not have any effect.
     * Only distance sensors can fail, the room sensor and exit
     * sensor if installed are always operational.
     * @param direction specifies the direction of the sensor
     */
    @Override
    public void triggerSensorFailure(Direction direction) {
        float bat = this.getBatteryLevel();

        switch(direction) {
            //changes sensor to false aka fails
            case FORWARD:
             //   assertTrue(bat == this.getBatteryLevel());
                forward = false;
                f_fail = true;
                return;

            case BACKWARD:
               // assertTrue(bat == this.getBatteryLevel());
                backward = false;
                b_fail = true;
                return;

            case LEFT:
               // assertTrue(bat == this.getBatteryLevel());
                left = false;
                l_fail = true;
                return;

            case RIGHT:
                //assertTrue(bat == this.getBatteryLevel());
                right = false;
                r_fail = true;
                return;

            default:
                //assertTrue(bat == this.getBatteryLevel());
                return;
        }


    }


    /**
     * Makes the robot's distance sensor for the given direction
     * operational again.
     * A method call for an already operational sensor has no effect
     * but returns true as the robot has an operational sensor
     * for this direction.
     * A method call for a sensor that the robot does not have
     * has not effect and the method returns false.
     * @param direction specifies the direction of the sensor
     * @return true if robot has operational sensor, false otherwise
     */
    @Override
    public boolean repairFailedSensor(Direction direction) {
        float bat = this.getBatteryLevel();

        switch(direction) {
            //changes sensors to true aka functioning
            case FORWARD:
               // assertTrue(bat == this.getBatteryLevel());
                forward = true;
                return true;

            case BACKWARD:
                //assertTrue(bat == this.getBatteryLevel());
                backward = true;
                return true;

            case LEFT:
                //assertTrue(bat == this.getBatteryLevel());
                left = true;
                return true;

            case RIGHT:
                //assertTrue(bat == this.getBatteryLevel());
                right = true;
                return true;

            default:
                return false;
        }

    }

    /**
     * Turn robot on the spot for amount of degrees.
     * If robot runs out of energy, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * @param turn direction  and relative to current forward direction.
     */
    @Override
    public void rotate(Turn turn) {
        float bat = this.getBatteryLevel() - 3;
        int odometer = this.getOdometerReading();
       // assertNotNull(turn);
        switch(turn){

            //to rotate right
            case RIGHT:
                if (batterylevel >= 3) {
                    this.setBatteryLevel(batterylevel - 3);
                    control.keyDown(UserInput.Right, 0);
                  //  assertTrue(bat == this.getBatteryLevel());
                    //assertTrue(odometer == this.getOdometerReading());
                }
                else if(batterylevel <3) {
                    nomove = true;
                   // control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
                }
                break;

            //rotate left
            case LEFT:
                if (batterylevel >= 3) {
                    this.setBatteryLevel(batterylevel - 3);
                    control.keyDown(UserInput.Left, 0);
                    //assertTrue(bat == this.getBatteryLevel());
                  //  assertTrue(odometer == this.getOdometerReading());
                }
                else if(bat <3) {
                    nomove = true;
                    //control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
                }
                break;

            //rotate 180;
            case AROUND:
                if (batterylevel >= 6) {
                    this.setBatteryLevel(batterylevel - 6);
                    control.keyDown(UserInput.Right, 0);
                    control.keyDown(UserInput.Right, 0);
                  //  assertTrue(bat - 3 == this.getBatteryLevel());
                    //assertTrue(odometer == this.getOdometerReading());
                }
                else if(bat <3) {
                    nomove = true;
                 //   control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
                }
                break;

            //can't turn that direction
            default:
                throw new AssertionError(turn);

        }
    }


    /**
     * Moves robot forward a given number of steps. A step matches a single cell.
     * If the robot runs out of energy somewhere on its way, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * If the robot hits an obstacle like a wall, it depends on the mode of operation
     * what happens. If an algorithm drives the robot, it remains at the position in front
     * of the obstacle and also hasStopped() == true as this is not supposed to happen.
     * This is also helpful to recognize if the robot implementation and the actual maze
     * do not share a consistent view on where walls are and where not.
     * If a user manually operates the robot, this behavior is inconvenient for a user,
     * such that in case of a manual operation the robot remains at the position in front
     * of the obstacle but hasStopped() == false and the game can continue.
     * @param distance is the number of cells to move in the robot's current forward direction
     * @param manual is true if robot is operated manually by user, false otherwise
     * @precondition distance >= 0
     */
    @Override
    public void move(int distance, boolean manual) {
        if (manual == true && batterylevel >= 5 ) {
            distance = 1;
        }

        float batt = batterylevel;
        //valid move and enough battery
        if (batterylevel >= 5) {

            while (distance > 0) {
                int odo = this.getOdometerReading();
                this.setBatteryLevel(batt + 1);

                if (this.distanceToObstacle(Direction.FORWARD) > 0 && batterylevel >= 5) {
                    this.setBatteryLevel(batt - 5);
                    odometer++;
                    nomove = false;
                  //  assertFalse(this.getBatteryLevel() == batt);
                    //assertTrue(this.getBatteryLevel() == batt -5);
                    //assertFalse(hasStopped());
                   // assertTrue(odo +1 == this.getOdometerReading());
                    control.keyDown(UserInput.Up, 0);
                    distance--;
                }

                else if(batterylevel < 5) {
                    nomove = true;
                    //assertTrue(this.getBatteryLevel() == batt);
                  //  assertTrue(hasStopped());
                   // assertTrue(odo == this.getOdometerReading());
                    this.setBatteryLevel(-1);
                    //control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
                }

                else {
                    nomove = true;
                    //assertTrue(hasStopped());
                    //assertTrue(odo == this.getOdometerReading());
                    //assertTrue(this.getBatteryLevel() == batt);
                    distance--;
                }
            }
        }

        //not enough battery
        else if (batterylevel < 5) {
            nomove = true;
            //assertTrue(this.getBatteryLevel() == batt);
            //assertTrue(hasStopped());

            this.setBatteryLevel(-1);
            //control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
            return;
        }

        //invalid move
        else {
            //assertTrue(this.getBatteryLevel() == batt);
            nomove = true;
            //assertTrue(hasStopped());
            return;
        }
    }


    /**
     * Makes robot move in a forward direction even if there is a wall
     * in front of it. In this sense, the robot jumps over the wall
     * if necessary. The distance is always 1 step and the direction
     * is always forward.
     * @throws Exception is thrown if the chosen wall is an exterior wall
     * and the robot would land outside of the maze that way.
     * The current location remains set at the last position,
     * same for direction but the game is supposed
     * to end with a failure.
     */
    @Override
    public void jump() throws Exception {
        floorplan = control.getMazeConfiguration().getFloorplan();
        //assertNotNull(floorplan);
        float bat = this.getBatteryLevel();

        int x = control.getCurrentPosition()[0];
        int y = control.getCurrentPosition()[1];

        CardinalDirection currdir = this.getCurrentDirection();
        Wallboard wall = new Wallboard(x, y, currdir);

        if (this.distanceToObstacle(Direction.FORWARD) == 1) {
            //jump
            if (floorplan.hasWall(x, y,  currdir)) {
                //enough battery
                if (batterylevel >= 50) {
                    move(1, false);
                    //assertTrue(bat == batterylevel - 50); //battery level should change with move}
                }
                //not enough battery
                else if(batterylevel < 50) {
                   // assertTrue(this.getBatteryLevel() == bat);
                    //assertTrue(hasStopped());
                    this.setBatteryLevel(-1);
                    //control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
                }
            }
            //if wall part of border, raise exception
            else if (floorplan.isPartOfBorder(wall)) {
                throw new Exception();
            }
        }
    }





}
