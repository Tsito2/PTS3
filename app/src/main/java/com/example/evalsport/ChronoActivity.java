package com.example.evalsport;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class ChronoActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private ImageView buttonGo;
    private TextView tvEleve1;
    private TextView tvEleve2;
    private TextView tvEleve3;
    private TextView tvEleve4;
    private String[] studentsTime;
    private String[] studentsPassed;
    private Button btnReset;
    String name1;
    String name2;
    String name3;
    String name4;
    private int count;
    private int studentNumber;
    private int position;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);

        this.chronometer = findViewById(R.id.chrono);
        this.buttonGo = findViewById(R.id.ivButton);
        this.btnReset = findViewById(R.id.resetButton);
        this.tvEleve1 = findViewById(R.id.tvEleve1);
        this.tvEleve2 = findViewById(R.id.tvEleve2);
        this.tvEleve3 = findViewById(R.id.tvEleve3);
        this.tvEleve4 = findViewById(R.id.tvEleve4);
        animation = AnimationUtils.loadAnimation(this, R.anim.zoomout);

        studentsPassed = new String[]{
                "Jean",
                "Pierre",
                "Roger",
                "Corentin"
        };

        studentNumber = studentsPassed.length;
        count = 0;
        position = 0;
        studentsTime = new String[studentNumber];

        tvEleve1.setVisibility(View.INVISIBLE);
        tvEleve2.setVisibility(View.INVISIBLE);
        tvEleve3.setVisibility(View.INVISIBLE);
        tvEleve4.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        this.buttonGo.setOnClickListener(view -> {
            buttonGo.startAnimation(animation);
            if (count == 0) {
                doStart();
            } else if (count < studentNumber) {
                doNext();
            } else {
                doStop();
            }
        });

        if (studentNumber >= 4) {
            tvEleve4.setOnClickListener(v -> setPosition(tvEleve4));
        }
        if (studentNumber >= 3) {
            tvEleve3.setOnClickListener(v -> setPosition(tvEleve3));
        }
        if (studentNumber >= 2) {
            tvEleve2.setOnClickListener(v -> setPosition(tvEleve2));
        }
        if (studentNumber >= 1) {
            tvEleve1.setOnClickListener(v -> setPosition(tvEleve1));
        }

        btnReset.setOnClickListener(v -> resetTimes());
    }

    private void doStart() {
        position = 0;
        tvEleve1.setVisibility(View.INVISIBLE);
        tvEleve2.setVisibility(View.INVISIBLE);
        tvEleve3.setVisibility(View.INVISIBLE);
        tvEleve4.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.chronometer.setBase(elapsedRealtime);
        this.chronometer.start();
        Context context = buttonGo.getContext();
        int id = context.getResources().getIdentifier("pause_button", "drawable", context.getPackageName());
        buttonGo.setImageDrawable(getResources().getDrawable(id));
        count++;
    }

    private void doNext() {
        updateTable();
        count++;
    }

    private void doStop() {
        updateTable();
        this.chronometer.stop();
        Context context = buttonGo.getContext();
        int id = context.getResources().getIdentifier("play_button", "drawable", context.getPackageName());
        System.out.println(Arrays.deepToString(studentsTime));
        buttonGo.setImageDrawable(getResources().getDrawable(id));
        showTexts();
        count = 0;
    }

    private void updateTable() {
        long millis = SystemClock.elapsedRealtime() - chronometer.getBase();
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        String s = String.format("%02d:%02d.%03d", minutes, seconds, millis - seconds * 1000);
        studentsTime[count - 1] = s;
    }

    private void showTexts() {
        if (studentNumber >= 4) {
            tvEleve4.setText(studentsPassed[3]);
            name4 = tvEleve4.getText().toString();
            tvEleve4.setVisibility(View.VISIBLE);
        }
        if (studentNumber >= 3) {
            tvEleve3.setText(studentsPassed[2]);
            name3 = tvEleve3.getText().toString();
            tvEleve3.setVisibility(View.VISIBLE);
        }
        if (studentNumber >= 2) {
            tvEleve2.setText(studentsPassed[1]);
            name2 = tvEleve2.getText().toString();
            tvEleve2.setVisibility(View.VISIBLE);
        }
        if (studentNumber >= 1) {
            tvEleve1.setText(studentsPassed[0]);
            name1 = tvEleve1.getText().toString();
            tvEleve1.setVisibility(View.VISIBLE);
        }
        btnReset.setVisibility(View.VISIBLE);
    }

    private void setPosition(TextView t) {
        int flag = 0;
        for (final String name : studentsPassed) {
            if (t.getText().toString().equals(name)) {
                flag = 1;
            }
        }
        if (flag == 1) {
            String name = t.getText().toString();
            if (position == 0) {
                t.setText("1er - " + name + " - " + studentsTime[position]);
            } else if (position == 1) {
                t.setText("2ème - " + name + " - " + studentsTime[position]);
            } else if (position == 2) {
                t.setText("3ème - " + name + " - " + studentsTime[position]);
            } else if (position == 3) {
                t.setText("4ème - " + name + " - " + studentsTime[position]);
            }
            position++;
        }
    }

    private void resetTimes() {
        position = 0;
        tvEleve1.setText(name1);
        tvEleve2.setText(name2);
        tvEleve3.setText(name3);
        tvEleve4.setText(name4);
    }
}