package edu.wm.cs.cs301.abigaildanielandkatiebourque.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;


import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;


public class WinningActivity extends AppCompatActivity {
    String tag = "WinningActivity";
    Button menu;


    /**
     onCreate sets the winning message that will be displayed and sets the path length and battery level variables
     @param savedInstanceState
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        Intent winning_message = getIntent();
        String path_length = winning_message.getStringExtra("path_length");
        String battery_level = winning_message.getStringExtra("battery_level");

        TextView pathLength = findViewById(R.id.path_length);
        pathLength.setText(String.valueOf(path_length));

        TextView batteryLevel = findViewById(R.id.battery_level);
        batteryLevel.setText(String.valueOf(battery_level));


        menu = findViewById(R.id.gen_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(tag, "Return to Menu clicked");
                Intent menu = new Intent(WinningActivity.this, AMazeActivity.class);
                startActivity(menu);
                finish();
            }
        });

    }
}

