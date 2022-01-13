package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EvaluationActivity extends AppCompatActivity {
    private JSONObject json;
    private JSONArray jsonCriteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
           // jsonCriteres = json.getJSONArray("sports").getJSONArray("competences").getJSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}