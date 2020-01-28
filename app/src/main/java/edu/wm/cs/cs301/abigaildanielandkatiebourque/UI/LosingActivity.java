package edu.wm.cs.cs301.abigaildanielandkatiebourque.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.wm.cs.cs301.abigaildanielandkatiebourque.R;


public class LosingActivity extends AppCompatActivity {
    String tag = "LosingActivity";
    Button menu;

    /**
     * onCreate sets the losing message that will be displayed and sets the path length and battery level variables
     * the function also reacts when return to menu is clicked, and returns user to AMazeActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        Intent losing_message = getIntent();
        String path_length = losing_message.getStringExtra("path_length");
        String battery_level = losing_message.getStringExtra("battery_level");
        String shortest_path_length = losing_message.getStringExtra("shortest_path");

        //stores values of each
        TextView path_l = findViewById(R.id.path_length);
        path_l.setText(String.valueOf(path_length));

        TextView battery_l = findViewById(R.id.battery_level);
        battery_l.setText(String.valueOf(battery_level));


        //return to menu button
        menu = findViewById(R.id.gen_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(tag, "Return to Menu clicked");
                Intent menu = new Intent(LosingActivity.this, AMazeActivity.class);
                startActivity(menu);
                finish();
            }
        });

    }


}



