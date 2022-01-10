package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RecapActivity extends AppCompatActivity{

    Button buttonAnnuler;
    Button buttonValider;
    TextView listeRecap;
    private JSONObject json;

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

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://la-projets.univ-lemans.fr/~inf2pj01/update.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Reponse", "Une réponse je crois");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ReponseError", "Une erreur de réponse je crois");
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("json",json.toString());
                        Log.e("Tag","Oui");
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(RecapActivity.this);
                requestQueue.add(stringRequest);

            }
        });
    }


}