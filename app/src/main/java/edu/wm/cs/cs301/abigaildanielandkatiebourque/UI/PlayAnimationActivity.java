package edu.wm.cs.cs301.abigaildanielandkatiebourque.UI;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Maze;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.CardinalDirection;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.MazePanel;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.BasicRobot;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Constants;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.RobotDriver;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.WallFollower;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.StatePlaying;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Wizard;

import android.media.MediaPlayer;
import android.widget.Button;
import android.os.Vibrator;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import android.view.View;
import android.widget.CompoundButton;
import android.os.Handler;
import android.widget.TextView;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;


public class PlayAnimationActivity extends AppCompatActivity {

    //inits
    ToggleButton map, path, wall, start;
    Button map_m, map_p, menu;
    String tag = "PlayAnimationActivity";

    StatePlaying currentState = new StatePlaying();
    RobotDriver driver;
    BasicRobot robot = new BasicRobot();
    Maze maze = GeneratingActivity.getMazeConfig();
    private boolean exit = false;
    private boolean drive = false;
    Handler notice = new Handler();
    Integer shortest_path_length = 0;
    ProgressBar bar;
    String autodrive;
    MediaPlayer music;
    Vibrator vibrator;

    /**
     onCreate implements buttons for menu, path, wall, and map
     @param savedInstanceState
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);
        Intent title = getIntent();
        autodrive = title.getStringExtra("driver");
        currentState.setMazeConfiguration(GeneratingActivity.getMazeConfig());
        currentState.start((MazePanel) findViewById(R.id.maze_panel));
        robot.setMaze(currentState);
        music = MediaPlayer.create(this, R.raw.myboimusix);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        final TextView direction = findViewById(R.id.direction);
        music.setLooping(true);
        direction.setText("East");
        music.start();
        Log.v(tag, autodrive);

        if (autodrive.equals("Wizard")) {
            driver = new Wizard();
        }
        else if (autodrive.equals("Wall Follower")) {
            driver = new WallFollower();
        }

        Log.v(tag, String.valueOf(driver));
        driver.setRobot(robot);
        driver.setDimensions(maze.getWidth(), maze.getHeight());
        driver.setDistance(maze.getMazedists());
        currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0);
        currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
        currentState.keyDown(Constants.UserInput.ToggleSolution, 0);
        bar = findViewById(R.id.progressBattery);
        bar.setProgress(3000);

        int x = 0;
        while(x < 20){
            currentState.keyDown(Constants.UserInput.ZoomIn, 0);
            x++;
        }

        menu = findViewById(R.id.gen_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
                music.stop();
                startActivity(menu);
                finish();
            }
        });

        map = findViewById(R.id.toggle_map);
        map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle turned on");
                }
                else {
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle turned off");
                }
            }
        });
        map_p = findViewById(R.id.zoom_in);
        map_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentState.keyDown(Constants.UserInput.ZoomIn, 0 );
                Log.v(tag, "Map zoom in clicked");
            }
        });


        map_m = findViewById(R.id.zoom_out);
        map_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentState.keyDown(Constants.UserInput.ZoomOut, 0);
                Log.v(tag, "Map zoom out clicked");
            }
        });

        path = findViewById(R.id.toggle_path);
        path.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0);
                    Log.v(tag, "Path toggle turned on");
                }
                else {
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0);
                    Log.v(tag, "Path toggle turned off");
                }
            }
        });

        wall = findViewById(R.id.toggle_walls);
        wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0);
                    Log.v(tag, "Wall toggle turned on");
                }
                else {
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0);
                    Log.v(tag, "Wall toggle turned off");
                }
            }
        });
/*
    runOnUiThread(new Runnable(){
 */
        start = findViewById(R.id.toggleDriver);
        start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean isChecked) {
                Runnable driving = new Runnable() {
                    @Override
                    public void run() {
                        if(drive) {
                            try {
                                exit = driver.drive2ExitAnimation(); //update progress bar
                                CardinalDirection direction = currentState.getCurrentDirection();
                                Log.v(tag, String.valueOf(direction));

                                bar.setProgress((int) robot.getBatteryLevel());

                                if (exit) {
                                    vibrator.vibrate(1000);
                                    Log.v(tag, "vibrate on");
                                    Intent winning = new Intent(PlayAnimationActivity.this, WinningActivity.class);
                                    winning.putExtra("battery_level", String.valueOf(driver.getEnergyConsumption()));
                                    winning.putExtra("shortest_path", String.valueOf(shortest_path_length));
                                    winning.putExtra("path_length", String.valueOf(robot.getOdometerReading()));
                                    Log.v(tag, String.valueOf(driver.getEnergyConsumption()));
                                    Log.v(tag, String.valueOf(robot.getOdometerReading()));
                                    music.stop();
                                    startActivity(winning);
                                    drive = false;
                                    finish();
                                }
                                if(robot.hasStopped() || robot.getBatteryLevel() < 5){
                                    Intent losing = new Intent(PlayAnimationActivity.this, LosingActivity.class);
                                    losing.putExtra("path_length", String.valueOf(robot.getOdometerReading()));
                                    losing.putExtra("battery_level", String.valueOf(driver.getEnergyConsumption()));
                                    music.stop();
                                    startActivity(losing);
                                    drive = false;
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            notice.postDelayed(this, 50);
                        }

                    }
                };

                if (isChecked) {
                    drive = true;
                    notice.post(driving);
                    Log.v(tag, "Driver turned on");
                }
                else{
                    //pause button
                    drive = false;
                    Log.v(tag, "Driver stopped");
                }
            }
        });
    }


}