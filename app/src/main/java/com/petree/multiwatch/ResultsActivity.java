package com.petree.multiwatch;

/**
 * Created by patrickcook on 3/10/15.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import java.io.*;
import java.util.ArrayList;
import android.view.Gravity;


public class ResultsActivity extends Activity {
    ArrayList<TextView> textViews = new ArrayList<TextView>();
    ArrayList<String> userInfo = new ArrayList<String>();
    LinearLayout sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        sc = (LinearLayout)findViewById(R.id.resultsLayout);



        final Button switchAct = (Button) findViewById(R.id.back);
        switchAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        final Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        refresh();
    }

    public void refresh(){
        readFromFile();
        removeDuplicates(userInfo);
        //clears all previous textViews
        sc.removeAllViews();

        final int N = userInfo.size(); // total number of textviews to add
        for (int i = N-1; i>=0; i--) {
            // create a new textview
            final TextView rowTextView = new TextView(this);
            final View line = new View(this);
            line.setMinimumWidth(sc.getWidth());
            line.setMinimumHeight(5);
            line.setBackgroundColor(Color.GRAY);
            // set some properties of rowTextView or something
            rowTextView.setText(userInfo.get(i));
            rowTextView.setTextSize(30);
            rowTextView.setGravity(Gravity.CENTER);

            // add the textview to the linearlayout
            sc.addView(rowTextView);
            sc.addView(line);
            // save a reference to the textview for later
            textViews.add(rowTextView);

        }
     }
    public void readFromFile() {
        try {
            InputStream inputStream = openFileInput("test1.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    userInfo.add(receiveString);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }
    public void removeDuplicates(ArrayList<String> strings) {
        int size = strings.size();
        // not using a method in the check also speeds up the execution
        for (int i = 0; i < size - 1; i++) {
            // start from the next item after strings[i]
            // since the ones before are checked
            for (int j = i + 1; j < size; j++) {
                // no need for if ( i == j ) here
                if (!strings.get(j).equals(strings.get(i)))
                    continue;
                strings.remove(j);
                // decrease j because the array got re-indexed
                j--;
                // decrease the size of the array
                size--;
            } // for j
        } // for i
    }
}