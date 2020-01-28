package edu.wm.cs.cs301.abigaildanielandkatiebourque.UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Maze;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.Order;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.MazeFactory;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.StubOrder;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.Constants;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.MazeFileReader;
import edu.wm.cs.cs301.abigaildanielandkatiebourque.gui.MazeFileWriter;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;


public class GeneratingActivity extends AppCompatActivity implements Order{

    //inits
    private String maze0 = "maze0";
    private Boolean maze0F = false;
    private String maze1 = "maze1";
    private Boolean maze1F = false;
    private String maze2 = "maze2";
    private Boolean maze2F = false;
    private String maze3 = "maze3";
    private Boolean maze3F = false;

    private ProgressBar bar;
    protected Handler handler;
    private MazeFactory fac = new MazeFactory();
    private Integer skillLevel;
    private String build;
    private String driver;
    private StubOrder order;
    private String tag = "GeneratingActivity";
    private Order.Builder builder = Order.Builder.DFS;
    //static
    public static Maze config;
    private static String tags = "GeneratingActivity";

    MediaPlayer music;
    Button menu;
    Boolean change;


    /**
     * onCreate implements buttons for GeneratingActivity screen
     * and gets setting info from AMazeActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        //music
        music = MediaPlayer.create(this, R.raw.myboimusix);
        music.setLooping(true);
        music.start();

        //maze info: skill level, builder and driver
        Intent info = getIntent();
        String button = info.getStringExtra("button");
        change = true;
        String size_str = info.getStringExtra("size");
        skillLevel = Integer.valueOf(size_str);
        build = info.getStringExtra("builder");
        switch(build){
            case "DFS": builder = Order.Builder.DFS;
            case "Eller": builder = Order.Builder.Eller;
            case "Prim": builder = Order.Builder.Prim;
        }
        driver = info.getStringExtra("driver");

        //return to menu button
        menu = findViewById(R.id.gen_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(GeneratingActivity.this, AMazeActivity.class);
                music.stop();
                startActivity(menu);
                change = false;
                finish();
            }
        });

        //progress bar
        bar = findViewById(R.id.gen_bar);
        handler = new Handler();
        if(button.equals("explore") || skillLevel >= 4) {
            fac.order(this);
        }
        else if(button.equals("revisit")){

            String maze = null;

            if(getSkillLevel() == 0){
                maze = maze0;
            }
            if (getSkillLevel() == 1){
                maze = maze1;
            }
            if(getSkillLevel() == 2){
                maze = maze2;
            }
            if(getSkillLevel() == 3){
                maze = maze3;
            }

            try {
                FileInputStream file = openFileInput(maze);
                deliver(loadMazeConfigurationFromFile(file));
            } catch (FileNotFoundException e) {
                fac.order(this);
            }

        }
    }

    private Maze loadMazeConfigurationFromFile(FileInputStream file){
        MazeFileReader read = new MazeFileReader(file);
        return read.getMazeConfiguration();
    }

    public static void setConfig(Maze configur){
        if(configur != null) {
            Log.v(tags, "configuration occurring");
            config = configur;
        }
    }

    /**
     * method is called once the loading is completed
     * uses information passed on by the title screen
     * to switch to either manual or driver play screen
     */
    private void switchtoPlaying(){
        Intent manual = new Intent(this, PlayManuallyActivity.class);
        Intent auto = new Intent(this, PlayAnimationActivity.class);
        Log.v(tag, driver);
        if(change == true ) {
            if (driver.equals("Manual")) {
                music.stop();
                startActivity(manual);
                finish();
            } else {
                auto.putExtra("driver", driver);
                music.stop();
                startActivity(auto);
                finish();
            }
        }
    }

    public static Maze getMazeConfig(){
        return config;
    }

    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

    @Override
    public boolean isPerfect() {
        return false;
    }

    @Override
    public void deliver(Maze mazeConfig) {

        setConfig(mazeConfig);

        if(getSkillLevel() == 0){
            // copy the maze to file here
            File maze00 = new File(getFilesDir(), maze0);
            maze0F = true;
            MazeFileWriter.store(maze00, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[0], Constants.SKILL_PARTCT[0],
                   config.getRootnode(), config.getFloorplan(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        else if(getSkillLevel() == 1){
            File maze01 = new File(getFilesDir(), maze1);
            maze1F = true;
            MazeFileWriter.store(maze01, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[1], Constants.SKILL_PARTCT[1],
                   config.getRootnode(), config.getFloorplan(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        else if(getSkillLevel() == 2){
            File maze02 = new File(getFilesDir(), maze2);
            maze2F = true;
            MazeFileWriter.store(maze02, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[2], Constants.SKILL_PARTCT[2],
                    config.getRootnode(), config.getFloorplan(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        else if(getSkillLevel() == 3){
            File maze03 = new File(getFilesDir(), maze3);
            maze3F = true;
            MazeFileWriter.store(maze03, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[3], Constants.SKILL_PARTCT[3],
                    config.getRootnode(), config.getFloorplan(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        switchtoPlaying();
    }

    @Override
    public void updateProgress(final int percentage) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                Log.v(tag, "loading " + percentage);
                bar.setProgress(percentage);

            }
        });

    }
}

