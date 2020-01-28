package edu.wm.cs.cs301.abigaildanielandkatiebourque.generation;

import android.os.Handler;
import android.util.Log;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.UI.GeneratingActivity;

/**
 * StubOrder is a stub for Order object to operate the MazeFactory in a testing environment.
 * StubOrder contains all the functions Order has.
 *
 * @author abbiedaniel and katiebourque
 *
 */

public class StubOrder implements Order {


    private static int skillLevel;
    private static Builder builder;
    private int percentdone = 0;
    static Maze maze;
    String tag = "Maze Order";
    Handler handler;
    static Maze config;





    public StubOrder() {
        this.skillLevel = 0;
        this.builder = Builder.DFS;
    }


    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    public static void setSkillLevel(int level){
        skillLevel = level;
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

    public static void setBuilder(String build){
        switch(build){
            case "DFS": builder = Order.Builder.DFS;
            case "Eller": builder = Order.Builder.Eller;
            case "Prim": builder = Order.Builder.Prim;
        }
    }


    @Override
    public boolean isPerfect() {
        return false;
    }

    @Override
    public void deliver(Maze mazeConfig) {
        // WARNING: DO NOT REMOVE, USED FOR GRADING PROJECT ASSIGNMENT
        if (Floorplan.deepdebugWall)
        {   // for debugging: dump the sequence of all deleted walls to a log file
            // This reveals how the maze was generated
            mazeConfig.getFloorplan().saveLogFile(Floorplan.deepedebugWallFileName);
        }// TODO send the maze Configuration to playing states
        Log.v(tag, "creates Maze Configuration");
        GeneratingActivity.setConfig(mazeConfig);
    }


    @Override
    public void updateProgress(int percentage) {
        if (percentdone < percentage && percentage <= 100) {
            percentdone = percentage;
    }}

    public int getPercentDone(){
        return percentdone;
    }


    public Maze getMaze() {
        return config;
    }

}
