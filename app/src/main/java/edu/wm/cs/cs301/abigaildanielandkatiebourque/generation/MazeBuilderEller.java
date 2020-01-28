package edu.wm.cs.cs301.abigaildanielandkatiebourque.generation;

import java.util.ArrayList;
import java.util.Random;

/**
 * The MazeBuilder implements Runnable such that it can be run a separate thread.
 * The MazeFactory has a MazeBuilder and handles the thread management.
 * MazeBuilderEller builds a maze using Eller's algorithm.
 *
 *
 * @author abigaildaniel and katiebourque
 */


public class MazeBuilderEller extends MazeBuilder implements Runnable {

    public MazeBuilderEller() {
        super();
        System.out.println("MazeBuilderEller uses Eller's algorithms to generate maze.");
    }

    public MazeBuilderEller(boolean det) {
        super(det);
        System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze");
    }

    protected int[][] setnum;
    public Random rando = new Random();
    public boolean decide;
    public ArrayList<Integer> sets = new ArrayList<Integer>();
    public ArrayList<Integer> columns = new ArrayList<Integer>();


    /**
     * This method generates pathways into the maze by using Eller's algorithm to generate
     * a maze using a matrix representation of the maze
     * in order to keep track of cell and wall locations.
     */
    @Override
    protected void generatePathways() {
        //matrix representing maze cells
        setnum = new int[height][width];

        //populate all matrix cells with 0
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < height; j++) {
                setnum[i][j] = 0;
            }
        }

        //populate first row
        int index = 0;
        int val = 1;
        while (index < width) {
            setnum[0][index] = val;
            val++;
            index++;
        }

        //Eller's Algorithm:
        for (int row = 0; row < height - 1; row++) {
            //step 1: check sets, randomly decide which wall to break, join sets, break wall
            DecideRightWalls(row);
            //step 2: check sets, randomly decide to break bottom wall, join sets, break wall
            DecideBottomWalls(row);
            //step 3: number row, check sets, randomly decide to break wall, join sets, break wall
            NextRow(row);
        }
        //step 4: completes last row
        LastRow(height - 1);
    }


    /**
     * Step 1 in Eller's Algorithm: Tears down right-walls, moving from left to right
     *
     * If current cell and cell to the right are members of the same set, do not destroy wall.
     * If they are not members of the same set, random decide to destroy walls.
     * If decides to destroy wall, join sets.
     *
     *   @param current row
     */
    private void DecideRightWalls(int row) {
        //for each cell in the row
        for (int c = 0; c < width; c++) {
            //create a reference to wallboard at that cell location
            Wallboard wall = new Wallboard(c, row, CardinalDirection.East);
            //check if cell and neighbor are not seperate by a border and they are not in the same set
            if (floorplan.canTearDown(wall) && setnum[row][c] != setnum[row][c+1]) {
                //randomly decide
                decide = rando.nextBoolean();
                if (decide == true) {
                    //if yes, delete wall, set adj cell to same set
                    floorplan.deleteWallboard(wall);
                    setnum[row][c+1] = setnum[row][c];
                }
            }
        }
    }


    /**
     * Step 2 in Eller's Algorithm: Tears down bottom-walls, moving from left to right
     *
     * If a cell is the only member of its set, destroy a bottom-wall, and join sets.
     * If a cell is the only member of its set without a bottom-wall, call BottomWall().
     *
     *  @param current row
     */
    private void DecideBottomWalls(int row) {
        int value = setnum[row][0];
        //goes through each cell in the current row
        for (int c = 0; c < height; c++) {
            value = setnum[row][c];

            //checks if member is in the set list --> adds member to sets list
            if (sets.contains(value) == false) {
                sets.add(value);
                //checks if there are other members in the set --> add column number to columns list
                for (int location = c; location < height; location++) {
                    if (value == setnum[row][location]) {
                        columns.add(location);
                    }}}

            //if only member of its set
            if (columns.size() == 1) {
                //make reference to wall board to delete
                Wallboard wall = new Wallboard(columns.get(0), row, CardinalDirection.South);
                //delete wall board
                floorplan.deleteWallboard(wall);
                //cell below becomes of the same set
                setnum[row+1][c] = setnum[row][c];
                columns.clear();
            }
            //if there is more than one member of this set
            else if (columns.size() >= 2) {
                BottomWall(row);
                columns.clear();
            }
        }
        //reset sets and columns list
        sets.clear();
    }


    /**
     * Randomly decides to tear down bottom a wall or not.
     * Makes sure that each set has at least one cell without a bottom-wall.
     *
     *  @param current row
     */
    private void BottomWall(int row) {

        boolean atleastOne = false;
        //while there is not bottom wall for a set
        while (atleastOne == false) {

            //for each element in columns list
            for (int loc = 0; loc < columns.size(); loc++) {
                //create wall board reference to corresponding matrix location
                Wallboard wall = new Wallboard (columns.get(loc), row, CardinalDirection.South);
                //randomly decide
                decide = rando.nextBoolean();
                if (decide == true) {
                    //if yes, delete wall and join sets
                    floorplan.deleteWallboard(wall);
                    setnum[row+1][columns.get(loc)]= setnum[row][columns.get(loc)];
                }	}

            //checks if there is at least one broken down bottom wall in a set
            int count = 0;
            //for each element in columns list
            for (int index = 0; index < columns.size(); index++) {
                //if cell below it has been visited before
                if (setnum[row+1][columns.get(index)] != 0) {
                    count++;
                }}

            //then change atleastOne to either true or false
            if (count > 0) {
                atleastOne = true;
            }
            else {
                atleastOne = false;
            }
        }

    }


    /**
     * Step 3 in Eller's Algorithm: Create and populate next row
     *
     * Finds largest set number in current row to determine which set number to start from for next row.
     * Repopulates next row from initial 0s.
     *
     *  @param current row
     */
    private void NextRow(int row) {

        int next = setnum[row][0];
        //gets last number from current row
        for (int c = 0; c < width; c++) {
            if (setnum[row][c] > next) {
                next = setnum[row][c];
            }	}

        next = next + 1;
        //populates next row , starting from the greatest number of the current row + 1
        for (int c = 0; c < width; c++) {
            if (setnum[row+1][c] == 0) {
                setnum[row+1][c] = (next);
                next++;
            }	}

    }

    /**
     * Step 4 in Eller's Algorithm: Complete the last row of the maze
     *
     * If the cell to the right of the current cell is not part of the same set,
     * break right wall and join sets.
     *
     * @param current row
     */
    private void LastRow(int row) {
        //for each cell in the row
        for (int c = 0; c < width-1; c++) {
            // check if cell and neighbor are not in the same set
            if (setnum[row][c] != setnum[row][c+1]) {
                //create reference to wallboard in corresponding matrix location
                Wallboard wall = new Wallboard(c, row, CardinalDirection.East);
                // delete wallboard
                floorplan.deleteWallboard(wall);
                //join sets
                setnum[row][c+1] = setnum[row][c];
            }
        }
    }


}
