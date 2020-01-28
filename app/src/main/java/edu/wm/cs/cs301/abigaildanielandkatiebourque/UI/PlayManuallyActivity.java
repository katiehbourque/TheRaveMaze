package edu.wm.cs.cs301.abigaildanielandkatiebourque.UI;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.CardinalDirection;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Maze;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Constants;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.StatePlaying;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.MazePanel;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.os.Vibrator;
import android.content.Context;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CompoundButton;




public class PlayManuallyActivity extends AppCompatActivity {

    ToggleButton map, path, wall;
    Button menu, right, left, forward, zoomout, zoomin;

    Integer pathLength = 0;
    Double batteryLevel = 0.0;

    String tag = "PlayManualActivity";
    StatePlaying currentState = new StatePlaying();
    Maze config = GeneratingActivity.getMazeConfig();
    String dir;
    Vibrator vibrator;


    /**
     onCreate implements buttons for menu, map, path, wall, move right, move left, move forward, go to winning screen, and go to losing screen
     @param savedInstanceState
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        currentState.setMazeConfiguration(GeneratingActivity.getMazeConfig());
        currentState.start((MazePanel) findViewById(R.id.maze_panel));
        final TextView direction = findViewById(R.id.direction);
        direction.setText("East");
        final MediaPlayer music = MediaPlayer.create(this, R.raw.myboimusix);
        music.setLooping(true);
        music.start();

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        menu = findViewById(R.id.gen_menu);
        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent menu = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
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
                } else {
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle turned off");
                }
            }
        });

        path = findViewById(R.id.toggle_path);
        path.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0);
                    Log.v(tag, "Path toggle turned on");
                } else {
                    Log.v(tag, "Path toggle turned off");
                }
            }
        });


        zoomin = findViewById(R.id.zoom_in);
        zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentState.keyDown(Constants.UserInput.ZoomIn, 0 );
                Log.v(tag, "Map zoom in clicked");
            }
        });

        zoomout = findViewById(R.id.zoom_out);
        zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentState.keyDown(Constants.UserInput.ZoomOut, 0 );
                Log.v(tag, "Map zoom out clicked");
            }
        });


        wall = findViewById(R.id.toggle_walls);
        wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0);
                    Log.v(tag, "Wall toggle turned on");
                } else {
                    Log.v(tag, "Wall toggle turned off");
                }
            }
        });

        forward = findViewById(R.id.up_key);
        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int x = currentState.getCurrentPosition()[0];
                int y = currentState.getCurrentPosition()[1];

                currentState.keyDown(Constants.UserInput.Up, 0 );
                int newx = currentState.getCurrentPosition()[0];
                int newy = currentState.getCurrentPosition()[1];
                if((x != newx) || (y != newy)){
                    batteryLevel += 5.0;
                    pathLength += 3;
                }
                if(!config.isValidPosition(newx, newy)){
                    music.stop();
                    switchToWinning();
                }
                CardinalDirection direc = currentState.getCurrentDirection();
                direction.setText(String.valueOf(direc));
                Log.v(tag, String.valueOf(direc));
                Log.v(tag, "forward key pressed");
            }
        });

        right = findViewById(R.id.right_key);
        right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentState.keyDown(Constants.UserInput.Right, 0 );
                batteryLevel += 3.0;
                CardinalDirection direc = currentState.getCurrentDirection();
                direction.setText(String.valueOf(direc));
                Log.v(tag, String.valueOf(direc));
                Log.v(tag, "right key pressed");
            }
        });

        left = findViewById(R.id.left_key);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentState.keyDown(Constants.UserInput.Left, 0 );
                batteryLevel += 3.0;
                CardinalDirection direc = currentState.getCurrentDirection();
                direction.setText(String.valueOf(direc));
                Log.v(tag, String.valueOf(direc));
                Log.v(tag, "left key pressed");
            }
        });
    }
    public void switchToWinning(){
        vibrator.vibrate(1000);
        Log.v(tag, "vibrate on");
        Log.v(tag, "Go2Winning screen");
        Intent winning = new Intent(PlayManuallyActivity.this, WinningActivity.class);
        winning.putExtra("battery_level", String.valueOf(batteryLevel));
        winning.putExtra("path_length", String.valueOf(pathLength));
        startActivity(winning);
        finish();

    }


}