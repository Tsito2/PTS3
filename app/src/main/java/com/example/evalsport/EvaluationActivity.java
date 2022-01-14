package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.evalsport.models.Critere;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class EvaluationActivity extends AppCompatActivity implements CritereRecyclerViewAdapter.ItemClickListener{
    private JSONObject json;
    private final String TAG = "Evaluation";
    private JSONArray jsonEleves;
    private JSONObject eleve;
    private JSONArray jsonGcriteres;
    private CritereRecyclerViewAdapter adapter;
    private CritereRecyclerViewAdapter adapter2;
    private CritereRecyclerViewAdapter adapter3;
    private CritereRecyclerViewAdapter adapter4;
    private CritereRecyclerViewAdapter adapter5;
    private Button retourButton;
    private Button evalButton;
    private TextView titTextView;
    private TextView noteTextView;
    private List<TextView> allComp;
    private TextView comp1;
    private TextView comp2;
    private TextView comp3;
    private TextView comp4;
    private TextView comp5;
    private String eleveNameSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        retourButton = findViewById(R.id.retourButton);
        titTextView = findViewById(R.id.titreTextView);
        evalButton = findViewById(R.id.evalButton);
        noteTextView = findViewById(R.id.noteTextView);

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
        json = null;
        jsonEleves = null;
        try {
            setTitle(getIntent().getExtras().getString("etape") + "/" + "Evaluation");
            eleveNameSurname = getIntent().getExtras().getString("eleve");
            titTextView.setText("Evaluation - " + eleveNameSurname);
            json = new JSONObject(getIntent().getExtras().getString("json"));
            jsonEleves = new JSONArray(getIntent().getExtras().getString("eleves"));
            eleve = getEleveFromNameSurname(eleveNameSurname);
            Log.e(TAG, "Eleve object : " + eleve);
            jsonGcriteres = json.getJSONArray("sports").getJSONObject(0).getJSONArray("gcriteres");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateNote();
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

        List<Critere> listeCriteres1 = null;
        List<Critere> listeCriteres2= null;
        List<Critere> listeCriteres3= null;
        List<Critere> listeCriteres4= null;
        List<Critere> listeCriteres5= null;

        try {
            listeCriteres1 = jArrayToCritereList(jsonGcriteres.getJSONObject(0).getJSONArray("criteres"));
            listeCriteres2 = jArrayToCritereList(jsonGcriteres.getJSONObject(1).getJSONArray("criteres"));
            listeCriteres3 = jArrayToCritereList(jsonGcriteres.getJSONObject(2).getJSONArray("criteres"));
            listeCriteres4 = jArrayToCritereList(jsonGcriteres.getJSONObject(3).getJSONArray("criteres"));
            listeCriteres5 = jArrayToCritereList(jsonGcriteres.getJSONObject(4).getJSONArray("criteres"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

        adapter2 = new CritereRecyclerViewAdapter(this, listeCriteres2);
        adapter2.setClickListener(this);
        recyclerView2.setAdapter(adapter2);

        //RECYBLER 3

        RecyclerView recyclerView3 = findViewById(R.id.rvCritere3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView3.addItemDecoration(dividerItemDecoration);

        adapter3 = new CritereRecyclerViewAdapter(this, listeCriteres3);
        adapter3.setClickListener(this);
        recyclerView3.setAdapter(adapter3);

        //RECYBLER 4

        RecyclerView recyclerView4 = findViewById(R.id.rvCritere4);
        recyclerView4.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView4.addItemDecoration(dividerItemDecoration);

        adapter4 = new CritereRecyclerViewAdapter(this, listeCriteres4);
        adapter4.setClickListener(this);
        recyclerView4.setAdapter(adapter4);

        //RECYBLER 5

        RecyclerView recyclerView5 = findViewById(R.id.rvCritere5);
        recyclerView5.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration.setDrawable(drawable);
        recyclerView5.addItemDecoration(dividerItemDecoration);

        adapter5 = new CritereRecyclerViewAdapter(this, listeCriteres5);
        adapter5.setClickListener(this);
        recyclerView5.setAdapter(adapter5);

        retourButton.setOnClickListener(view -> {
            finish();
        });

        evalButton.setOnClickListener(view -> {
            finish();
        });
    }

    public List<Critere> jArrayToCritereList(JSONArray jsonArray){
        List<Critere> list = new LinkedList<>();
        for (int i=0; i < jsonArray.length(); i++){
            try {
                JSONObject e = jsonArray.getJSONObject(i);
                String description = e.getString("descriptionCritere");
                double noteEleve = getCritereNoteFromEleve(e.getInt("idCritere"));
                double note = e.getDouble("points");
                list.add(new Critere( description, note, noteEleve));
            } catch (JSONException e) {
                e.printStackTrace();
                list.add(null);
            }
        }
        return list;
    }


    private double getCritereNoteFromEleve(int idCritere) throws JSONException {
        for (int i =0; i < eleve.getJSONArray("notes").length();i++){
            JSONObject e = null;
            e = eleve.getJSONArray("notes").getJSONObject(i);
            if (e != null && e.getInt("idCritere") == idCritere){
                return e.getDouble("note");
            }
        }
        return 0d;
    }

    private JSONObject getEleveFromNameSurname(String nameSurname) throws JSONException{
        for (int i =0; i < jsonEleves.length();i++){
            JSONObject e = null;
            e = jsonEleves.getJSONObject(i);
            if(e != null && e.getString("prenomEleve").equals(nameSurname.split(" ")[0]) && e.getString("nomEleve").equals(nameSurname.split(" ")[1])){
                return e;
            }
        }
        return null;
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.e(TAG, "Position : "+position);
        noteTextView.setText("updated");
    }

    public void updateNote(){
        noteTextView.setText(ListEleveActivity.getNoteFromEleve(eleve) + " / 20");
    }
}