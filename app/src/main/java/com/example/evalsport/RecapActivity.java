package com.example.evalsport;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RecapActivity extends AppCompatActivity{

    Button buttonAnnuler;
    Button buttonValider;
    TextView listeRecap;
    private JSONObject json;
    HttpURLConnection conn;
    private String urlString = "https://la-projets.univ-lemans.fr/~inf2pj01/update.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        buttonAnnuler = findViewById(R.id.buttonAnnuler);
        buttonValider = findViewById(R.id.buttonValider);
        listeRecap = findViewById(R.id.listeModifs);

        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}