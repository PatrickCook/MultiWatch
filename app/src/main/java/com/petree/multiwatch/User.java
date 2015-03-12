package com.petree.multiwatch;

import com.petree.multiwatch.SmartChronometer;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;

/**
 * Created by patrickcook on 3/9/15.
 * A User has a timer, a start and stop button and name
 */

public class User {
    private SmartChronometer chrono;
    private Button start, reset;
    private EditText name;
    public User(SmartChronometer c, Button s, Button r, EditText n){
        chrono = c;
        start = s;
        reset = r;
        name = n;
        init();
    }

    public void init(){

        //make all components invisible at first
        name.setVisibility(View.GONE);
        start.setVisibility(View.GONE);
        reset.setVisibility(View.GONE);
        name.setVisibility(View.GONE);


        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chrono.reset();
                if (!chrono.isRunning())
                    start.setText("Start");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chrono.isRunning()) {
                    start.setText("Resume");
                    chrono.pause();

                }
                else {
                    start.setText("Stop");
                    chrono.chronoStart();
                }

            }
        });

    }
    public void show(){
        //make all components invisible at first
        name.setVisibility(View.VISIBLE);
        start.setVisibility(View.VISIBLE);
        reset.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
    }

    public SmartChronometer getChrono(){
            return chrono;
    }
    public String getChronoTime() {
        return (String)chrono.getText();
    }

    public Button getStart() {
        return start;
    }

    public Button getReset() {
        return reset;
    }

    public String getName() {

        return name.getText().toString();
    }
}
