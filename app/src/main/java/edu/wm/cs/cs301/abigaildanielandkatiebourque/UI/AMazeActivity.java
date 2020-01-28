package edu.wm.cs.cs301.abigaildanielandkatiebourque.UI;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.content.Intent;
import android.widget.SeekBar;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



public class AMazeActivity extends AppCompatActivity {

    //initializations
    MediaPlayer music;
    String tag = "AMazeActivity";
    Button revisit;

    /**
     * Function called when amazeactivity is displayed.
     * Implements layout features like buttons, a spinner and drop down menu.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        //music
        music = MediaPlayer.create(this, R.raw.myboimusix);
        music.setLooping(true);
        music.start();
        //spinner
        Spinner spin_build = findViewById(R.id.builder);
        //builder info for spinner
        ArrayAdapter<CharSequence> adapter_build = ArrayAdapter.createFromResource(this, R.array.builders, android.R.layout.simple_spinner_item);
        adapter_build.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_build.setAdapter(adapter_build);
        // driver info
        Spinner spin_drive = findViewById(R.id.driver);
        ArrayAdapter<CharSequence> adapter_drive = ArrayAdapter.createFromResource(this, R.array.drivers, android.R.layout.simple_spinner_item);
        adapter_drive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_drive.setAdapter(adapter_drive);

        //load old maze button
        revisit = findViewById(R.id.revisit_button);
        revisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchtoGeneratingRevisit();
            }
        });

    }

    /**
     * switchtoGenerating function displays the next activity
     * once builder, driver and skill level are selected.
     * @param view
     */
    public void switchtoGenerating(View view){
        // get the selected builder
        Spinner spin_build = findViewById(R.id.builder);
        String build = spin_build.getSelectedItem().toString();
        Log.v(tag, build);

        //get driver
        Spinner spin_drive = findViewById(R.id.driver);
        String drive = spin_drive.getSelectedItem().toString();
        Log.v(tag, drive);

        //get skill level
        SeekBar bar = findViewById(R.id.size_bar);
        Integer size = bar.getProgress();
        Log.v(tag, String.valueOf(size));

        Intent intent = new Intent(this, GeneratingActivity.class);

        //pass the user inputted preferences to  Generating
        intent.putExtra("builder", build);
        intent.putExtra("driver", drive);
        intent.putExtra("size", String.valueOf(size));
        intent.putExtra("button", "explore");

        //start activity
        music.stop();
        startActivity(intent);
    }

    public void switchtoGeneratingRevisit(){

        // get the integer selected
        SeekBar bar = findViewById(R.id.size_bar);
        Integer size = bar.getProgress();
        Log.v(tag, String.valueOf(size));
        // get the selected build
        Spinner spin_build = findViewById(R.id.builder);
        String build = spin_build.getSelectedItem().toString();
        Log.v(tag, build);
        // get the selected driver
        Spinner spin_drive = findViewById(R.id.driver);
        String drive = spin_drive.getSelectedItem().toString();
        Log.v(tag, drive);
        Intent intent = new Intent(this, GeneratingActivity.class);
        // these pass the user inputed values to GeneratingActivity
        intent.putExtra("builder", build);
        intent.putExtra("driver", drive);
        intent.putExtra("size", String.valueOf(size));
        intent.putExtra("button", "revisit");
        // start the activity
        music.stop();
        startActivity(intent);

    }

}

