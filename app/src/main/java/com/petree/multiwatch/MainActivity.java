package com.petree.multiwatch;

import android.app.Activity;

import java.util.ArrayList;

import android.content.Intent;
import java.io.*;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.view.*;

import android.widget.*;


public class MainActivity extends Activity {
    ArrayList<User> userDataBase = new ArrayList<User>();

    private int users = 0;
    private boolean startAllisOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initializeApp();


    }

    /**
     * this method handles all buttons and chronometers.
     * it is responsible for generating all onClick() methods for each "person"
     */

    private void initializeApp() {
        //creates users
        userDataBase.add(new User((SmartChronometer) findViewById(R.id.chrono1), (Button) findViewById(R.id.start1),
                (Button) findViewById(R.id.reset1), (EditText) findViewById(R.id.text1)));
        userDataBase.add(new User((SmartChronometer) findViewById(R.id.chrono2), (Button) findViewById(R.id.start2),
                (Button) findViewById(R.id.reset2), (EditText) findViewById(R.id.text2)));
        userDataBase.add(new User((SmartChronometer) findViewById(R.id.chrono3), (Button) findViewById(R.id.start3),
                (Button) findViewById(R.id.reset3), (EditText) findViewById(R.id.text3)));
        userDataBase.add(new User((SmartChronometer) findViewById(R.id.chrono4), (Button) findViewById(R.id.start4),
                (Button) findViewById(R.id.reset4), (EditText) findViewById(R.id.text4)));

        //handles adding new users and showing hiding components of each user
        final Button addUser = (Button) findViewById(R.id.add);
        addUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (users < 4) {
                    users++;
                    for (int i = 0; i < users; i++) {
                        userDataBase.get(i).show();
                    }
                }
            }
        });
        //handles starting all stopwatches at the same time and stopping them
        final Button startAll = (Button) findViewById(R.id.startAll);
        startAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAllisOn ^= true;
                for (int i = 0; i < users; i++) {
                    SmartChronometer temp = userDataBase.get(i).getChrono();
                    Button tempB = userDataBase.get(i).getStart();
                    if (startAllisOn) {
                        startAll.setText("Stop All");
                        temp.chronoStart();
                        tempB.setText("Stop");
                    } else {
                        startAll.setText("Start All");
                        temp.pause();
                        tempB.setText("Resume");
                    }
                }
            }
        });
        //handles switching to results page
        final Button switchAct = (Button) findViewById(R.id.viewResults);
        switchAct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ResultsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        final Button saveAll = (Button) findViewById(R.id.save);
        saveAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveInfo();
            }
        });

    }
    public void saveInfo(){
        for (int i = 0;i<users;i++){
            User user = userDataBase.get(i);
            String name = user.getName();
            String time = user.getChronoTime();
            writeToFile(name+": "+time+"\n");
        }
    }
    public void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("test1.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }




}
