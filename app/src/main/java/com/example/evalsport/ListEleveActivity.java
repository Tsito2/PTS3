package com.example.evalsport;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListEleveActivity extends AppCompatActivity implements ElevesRecyclerViewAdapter.ItemClickListener {

    ElevesRecyclerViewAdapter adapter;
    public final String TAG = "ListEleveActivity";
    private List<String> selectionned;
    private TextView nbSelectionnedTextView;
    private Button chronoButton;
    private Button evalButton;
    private Button validateButton;
    private JSONArray jsonEleves;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleves);
        selectionned = new LinkedList<>();
        chronoButton = findViewById(R.id.chronoButton);
        evalButton = findViewById(R.id.validationButton);
        validateButton = findViewById(R.id.terminerButton);
        nbSelectionnedTextView = findViewById(R.id.nbSectionnedTextView);

        disable(chronoButton);
        disable(evalButton);

        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
            jsonEleves = new JSONArray(getIntent().getExtras().getString("eleves"));
            setTitle(getIntent().getExtras().getString("etape") + "/" + getIntent().getExtras().getString("sportTitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> listeEleves = new ArrayList<>();
        for (int i = 0; i < jsonEleves.length(); i++) {
            double note = 0;
            String noteString = "";
            String complete = "";
            JSONObject e = null;
            try {
                e = jsonEleves.getJSONObject(i);
                if (e.getJSONArray("notes").length() <= 0) {
                    complete = "N";
                } else if (e.getJSONArray("notes").length() == 19) {
                    complete = "C";
                } else {
                    complete = "I";
                }
                noteString = getNoteFromEleve(e);

                listeEleves.add(e.getString("prenomEleve") + " " + e.getString("nomEleve") + " - " + noteString + "/20" + " - " + complete);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }

        evalButton.setOnClickListener(view -> {
            Intent evaluationActivity = new Intent(this, EvaluationActivity.class);
            String eleve = cleanName(listeEleves.get(Integer.parseInt(selectionned.get(0))));
            evaluationActivity.putExtra("eleve", eleve);
            evaluationActivity.putExtra("eleves", jsonEleves.toString());
            evaluationActivity.putExtra("etape", getTitle());
            evaluationActivity.putExtra("json", json.toString());
            startActivity(evaluationActivity);
        });

        validateButton.setOnClickListener(view -> {
            Intent recapActivity = new Intent(this, RecapActivity.class);
            recapActivity.putExtra("etape", getTitle());
            recapActivity.putExtra("json", json.toString());
            startActivity(recapActivity);
        });

        chronoButton.setOnClickListener(view -> {
            Intent chronoActivity = new Intent(this, ChronoActivity.class);
            String[] elevesSelectiones = new String[selectionned.size()];
            for (int i= 0; i < selectionned.size(); i++){
                elevesSelectiones[i] = cleanName(listeEleves.get(Integer.parseInt(selectionned.get(i))));
            }

            chronoActivity.putExtra("eleves", elevesSelectiones);
            chronoActivity.putExtra("etape", getTitle());
            chronoActivity.putExtra("json", json.toString());
            startActivity(chronoActivity);
        });

        RecyclerView recyclerView = findViewById(R.id.rvClasses);
        LinearLayout layoutManager = findViewById(R.id.loClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable drawable = getResources().getDrawable(R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ElevesRecyclerViewAdapter(this, listeEleves);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public String cleanName(String eleve) {
        return eleve.substring(0, eleve.indexOf("-") - 1);
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.i(TAG, "View : " + v.toString() + "\nPosition : " + position);
        if (selectionned.contains(""+position)){
            selectionned.remove("" + position);
            v.setBackground(getResources().getDrawable(R.drawable.layout_bg));
        } else {
            if (selectionned.size() >= 4){
                return;
            }
            selectionned.add("" + position);
            v.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
        }
        nbSelectionnedTextView.setText(selectionned.size() + " / 4 ??l??ves selectionn??s" );
        checkButtons();

    }

    public static String getNoteFromEleve(JSONObject eleve){
        double note =0;
        String result = "0";
        try {
            for (int j = 0; j < eleve.getJSONArray("notes").length(); j++) {
                note += eleve.getJSONArray("notes").getJSONObject(j).getDouble("note");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (note == (int) note) {
            result = String.format("%d",(int)note);
        } else {
            result = String.format("%s",note);
        }
        return result;
    }
    public void checkButtons() {
        if (selectionned.size() == 0) {
            disable(chronoButton);
            disable(evalButton);
        }
        if (selectionned.size() == 1) {
            enable(chronoButton);
            enable(evalButton);
        }
        if (selectionned.size() == 2) {
            enable(chronoButton);
            disable(evalButton);
        }
        if (selectionned.size() == 3) {
            enable(chronoButton);
            disable(evalButton);
        }
        if (selectionned.size() == 4) {
            enable(chronoButton);
            disable(evalButton);
        }
    }

    public void disable(Button button) {
        button.setAlpha(0.5f);
        button.setEnabled(false);
    }

    public void enable(Button button) {
        button.setAlpha(1.0f);
        button.setEnabled(true);
    }
}
