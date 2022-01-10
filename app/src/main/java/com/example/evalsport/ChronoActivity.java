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
    private Integer[] studentsScore;
    private long[] studentsMillis;
    private Button btnReset;
    private Button btnValidate;
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
        this.btnValidate = findViewById(R.id.validateButton);
        this.tvEleve1 = findViewById(R.id.tvEleve1);
        this.tvEleve2 = findViewById(R.id.tvEleve2);
        this.tvEleve3 = findViewById(R.id.tvEleve3);
        this.tvEleve4 = findViewById(R.id.tvEleve4);

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
        studentsScore = new Integer[studentNumber];
        studentsMillis = new long[studentNumber];

        tvEleve1.setVisibility(View.INVISIBLE);
        tvEleve2.setVisibility(View.INVISIBLE);
        tvEleve3.setVisibility(View.INVISIBLE);
        tvEleve4.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);
        btnValidate.setVisibility(View.INVISIBLE);

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

        this.btnValidate.setOnClickListener(view -> {
            writeJson();
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
        btnValidate.setVisibility(View.INVISIBLE);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.chronometer.setBase(elapsedRealtime);
        this.chronometer.start();
        Context context = buttonGo.getContext();
        int id = context.getResources().getIdentifier("first_button", "drawable", context.getPackageName());
        buttonGo.setImageDrawable(getResources().getDrawable(id));
        count++;
    }

    private void doNext() {
        updateTable();
        count++;
        Context context = buttonGo.getContext();
        if (count == 1) {
            int id = context.getResources().getIdentifier("first_button", "drawable", context.getPackageName());
            buttonGo.setImageDrawable(getResources().getDrawable(id));
        } else if (count == 2) {
            int id = context.getResources().getIdentifier("second_button", "drawable", context.getPackageName());
            buttonGo.setImageDrawable(getResources().getDrawable(id));
        } else if (count == 3) {
            int id = context.getResources().getIdentifier("third_button", "drawable", context.getPackageName());
            buttonGo.setImageDrawable(getResources().getDrawable(id));
        } else {
            int id = context.getResources().getIdentifier("fourth_button", "drawable", context.getPackageName());
            buttonGo.setImageDrawable(getResources().getDrawable(id));
        }
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
        studentsMillis[count - 1] = millis;
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        String s = String.format("%02d:%02d.%03d", minutes, seconds, millis - (seconds + (minutes * 60)) * 1000);
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
                int score = noter(0);
                t.setText("1er - " + name + " - " + studentsTime[position] + " - " + score + "/8");
                studentsScore[position] = score;
            } else if (position == 1) {
                int score = noter(1);
                t.setText("2ème - " + name + " - " + studentsTime[position] + " - " + score + "/8");
                studentsScore[position] = score;
            } else if (position == 2) {
                int score = noter(2);
                t.setText("3ème - " + name + " - " + studentsTime[position] + " - " + score + "/8");
                studentsScore[position] = score;
            } else if (position == 3) {
                int score = noter(3);
                t.setText("4ème - " + name + " - " + studentsTime[position] + " - " + score + "/8");
                studentsScore[position] = score;
            }
            position++;
        }
        if (position == studentNumber) {
            btnValidate.setVisibility(View.VISIBLE);
        }
    }

    private void resetTimes() {
        position = 0;
        tvEleve1.setText(name1);
        tvEleve2.setText(name2);
        tvEleve3.setText(name3);
        tvEleve4.setText(name4);
        btnValidate.setVisibility(View.INVISIBLE);
    }

    private int noter(int position) {
        long time = studentsMillis[position];
        if (time > 24500) {
            return 0;
        } else if (time >= 23000) {
            return 1;
        } else if (time >= 21600) {
            return 2;
        } else if (time >= 20200) {
            return 3;
        } else if (time >= 19000) {
            return 4;
        } else if (time >= 17900) {
            return 5;
        } else if (time >= 17000) {
            return 6;
        } else if (time >= 16400) {
            return 7;
        } else {
            return 8;
        }
    }

    private void writeJson() {

    }
}