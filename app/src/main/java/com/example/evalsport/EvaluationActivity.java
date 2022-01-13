package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EvaluationActivity extends AppCompatActivity implements CritereRecyclerViewAdapter.ItemClickListener{
    private JSONObject json;
    private JSONArray jsonCriteres;
    private CritereRecyclerViewAdapter adapter;
    private Button retourButton;
    private TextView titTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        retourButton = findViewById(R.id.buttonCancel);
        titTextView = findViewById(R.id.titreTextView);
        try {
            setTitle(getIntent().getExtras().getString("etape") + "/" + "Evaluation");
            titTextView.setText("Evaluation - " + getIntent().getExtras().getString("eleve"));
            json = new JSONObject(getIntent().getExtras().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
           // jsonCriteres = json.getJSONArray("sports").getJSONArray("competences").getJSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<String> listeCriteres = new ArrayList<>();
        listeCriteres.add("Courir vite");
        listeCriteres.add("Pas tomber");

        /*
        JSONArray jsonCriteres =  json.getJSONArray("sports").getJSONArray("competences").get;
        LinkedList<String> competences =

        listeEleves.add("GG durant");
        listeEleves.add("Bobby Smith");
        */


        RecyclerView recyclerView = findViewById(R.id.rvCriteres);
        LinearLayout layoutManager = findViewById(R.id.loClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable drawable = getResources().getDrawable(R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new CritereRecyclerViewAdapter(this, listeCriteres);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        retourButton.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}