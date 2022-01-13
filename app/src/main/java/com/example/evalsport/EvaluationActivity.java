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
import java.util.LinkedList;
import java.util.List;

public class EvaluationActivity extends AppCompatActivity implements CritereRecyclerViewAdapter.ItemClickListener{
    private JSONObject json;
    private JSONArray jsonGcriteres;
    private CritereRecyclerViewAdapter adapter;
    private Button retourButton;
    private TextView titTextView;
    private TextView noteTextView;
    private List<TextView> allComp;
    private TextView comp1;
    private TextView comp2;
    private TextView comp3;
    private TextView comp4;
    private TextView comp5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        retourButton = findViewById(R.id.retourButton);
        titTextView = findViewById(R.id.titreTextView);
        noteTextView = findViewById(R.id.noteTextView);
        //noteTextView.setText(ListEleveActivity.getNoteFromEleve());
        allComp = new LinkedList<>();
        comp1 = findViewById(R.id.comp1);
        comp2 = findViewById(R.id.comp2);
        comp3 = findViewById(R.id.comp3);
        comp4 = findViewById(R.id.comp4);
        comp5 = findViewById(R.id.comp5);
        allComp.add(comp1);
        allComp.add(comp2);
        allComp.add(comp3);
        allComp.add(comp4);
        allComp.add(comp5);

        try {
            setTitle(getIntent().getExtras().getString("etape") + "/" + "Evaluation");
            titTextView.setText("Evaluation - " + getIntent().getExtras().getString("eleve"));
            json = new JSONObject(getIntent().getExtras().getString("json"));
            jsonGcriteres = json.getJSONArray("sports").getJSONObject(0).getJSONArray("gcriteres");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enlever les chrono criteres!
        jsonGcriteres.remove(0);
        for (int i=0; i < jsonGcriteres.length(); i++){
            try {
                JSONObject e = jsonGcriteres.getJSONObject(i);
                allComp.get(i).setText(e.getString("descriptionGCriteres"));
            } catch (JSONException jsonException) {
                allComp.get(i).setText("Erreur groupe competence");
                jsonException.printStackTrace();
            }
        }

        List<String> listeCriteres1 = null;
        List<String> listeCriteres2= null;
        List<String> listeCriteres3= null;
        List<String> listeCriteres4= null;
        List<String> listeCriteres5= null;

        try {
            listeCriteres1 = copyArrayInList(jsonGcriteres.getJSONObject(0).getJSONArray("criteres"));
            listeCriteres2 = copyArrayInList(jsonGcriteres.getJSONObject(1).getJSONArray("criteres"));
            listeCriteres3 = copyArrayInList(jsonGcriteres.getJSONObject(2).getJSONArray("criteres"));
            listeCriteres4 = copyArrayInList(jsonGcriteres.getJSONObject(3).getJSONArray("criteres"));
            listeCriteres5 = copyArrayInList(jsonGcriteres.getJSONObject(4).getJSONArray("criteres"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        adapter = new CritereRecyclerViewAdapter(this, listeCriteres1);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //RECYBLER 2

        RecyclerView recyclerView2 = findViewById(R.id.rvCritere2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView2.addItemDecoration(dividerItemDecoration);

        adapter = new CritereRecyclerViewAdapter(this, listeCriteres2);
        adapter.setClickListener(this);
        recyclerView2.setAdapter(adapter);

        //RECYBLER 3

        RecyclerView recyclerView3 = findViewById(R.id.rvCritere3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView3.addItemDecoration(dividerItemDecoration);

        adapter = new CritereRecyclerViewAdapter(this, listeCriteres3);
        adapter.setClickListener(this);
        recyclerView3.setAdapter(adapter);

        //RECYBLER 4

        RecyclerView recyclerView4 = findViewById(R.id.rvCritere4);
        recyclerView4.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView4.addItemDecoration(dividerItemDecoration);

        adapter = new CritereRecyclerViewAdapter(this, listeCriteres4);
        adapter.setClickListener(this);
        recyclerView4.setAdapter(adapter);

        //RECYBLER 5

        RecyclerView recyclerView5 = findViewById(R.id.rvCritere5);
        recyclerView5.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView5.addItemDecoration(dividerItemDecoration);

        adapter = new CritereRecyclerViewAdapter(this, listeCriteres5);
        adapter.setClickListener(this);
        recyclerView5.setAdapter(adapter);


        retourButton.setOnClickListener(view -> {
            finish();
        });
    }

    public List<String> copyArrayInList(JSONArray jsonArray){
        List<String> list = new LinkedList<>();
        for (int i=0; i < jsonArray.length(); i++){
            try {
                list.add(capitalize(jsonArray.getJSONObject(i).getString("descriptionCritere")));
            } catch (JSONException e) {
                e.printStackTrace();
                list.add("Erreur critere");
            }
        }
        return list;
    }



    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}