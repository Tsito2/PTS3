package com.example.evalsport;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChronoActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private ImageView buttonGo;
    private int count;
    private int studentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);

        this.chronometer = findViewById(R.id.chrono);
        this.buttonGo = findViewById(R.id.ivButton);

        studentNumber = 4;
        count = 0;

        this.buttonGo.setOnClickListener(view -> {
            if (count == 0) {
                doStart();
            } else if (count < studentNumber) {
                doNext();
            } else {
                doStop();
            }
        });
    }

    private void doStart() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.chronometer.setBase(elapsedRealtime);
        this.chronometer.start();
        Context context = buttonGo.getContext();
        int id = context.getResources().getIdentifier("pause_button", "drawable", context.getPackageName());
        buttonGo.setImageDrawable(getResources().getDrawable(id));
        count++;
    }

    private void doNext() {
        count++;
    }

    private void doStop() {
        this.chronometer.stop();
        Context context = buttonGo.getContext();
        int id = context.getResources().getIdentifier("play_button", "drawable", context.getPackageName());
        buttonGo.setImageDrawable(getResources().getDrawable(id));
    }
}