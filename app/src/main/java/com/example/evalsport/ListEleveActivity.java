package com.example.evalsport;

import android.content.Intent;
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
    private JSONArray jsonEleves;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleves);
        selectionned = new LinkedList<>();
        chronoButton = findViewById(R.id.chronoButton);
        evalButton = findViewById(R.id.evalButton);
        nbSelectionnedTextView = findViewById(R.id.nbSectionnedTextView);

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
                for (int j = 0; j < e.getJSONArray("notes").length(); j++) {
                    note += e.getJSONArray("notes").getJSONObject(j).getDouble("note");
                }
                listeEleves.add(e.getString("prenomEleve") + " " + e.getString("nomEleve") + " - " + note + "/20" + " - " + complete);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }

        evalButton.setOnClickListener(view -> {
            Intent evaluationActivity = new Intent(this, EvaluationActivity.class);
            String eleve = cleanName(listeEleves.get(Integer.parseInt(selectionned.get(0))));
            evaluationActivity.putExtra("eleve", eleve);
            evaluationActivity.putExtra("etape", getTitle());
            evaluationActivity.putExtra("json", json.toString());
            startActivity(evaluationActivity);
        });

        chronoButton.setOnClickListener(view -> {
            Intent chronoActivity = new Intent(this, ChronoActivity.class);
            String[] elevesSelectiones = new String[selectionned.size()];
            for (int i= 0; i < selectionned.size();i++){
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

        nbSelectionnedTextView.setText(selectionned.size() + " / 4 élèves selectionnés" );

    }
}
