package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.evalsport.models.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListRecyclerViewAdapter.ItemClickListener {

    ListRecyclerViewAdapter adapter;
    public final String TAG = "ListActivity";
    private JSONArray jsonSports;
    private JSONObject json;
    private ArrayList<String> listeClasses;
    private JSONArray jsonClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        jsonClasses = null;
        jsonSports = null;
        json = null;
        try {
            jsonClasses = new JSONArray(getIntent().getExtras().getString("classes"));
            jsonSports = new JSONArray(getIntent().getExtras().getString("sports"));
            json = new JSONObject(getIntent().getExtras().getString("json"));
            setTitle("EvalSport - "+json.getString("prenomProfesseur") + "." + json.getString("nomProfesseur"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("In list activity : " + jsonClasses);
        listeClasses = new ArrayList<>();
        if (jsonClasses != null) {
            for (int i=0;i<jsonClasses.length();i++){
                try {
                    listeClasses.add(jsonClasses.getJSONObject(i).getString("nomClasse"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("After parsing : " + listeClasses);

        RecyclerView recyclerView = findViewById(R.id.rvClasses);
        LinearLayout layoutManager = findViewById(R.id.loClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        Drawable drawable = getResources().getDrawable(R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ListRecyclerViewAdapter(this, listeClasses);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.i(TAG, "View : " + v.toString() + "\nPosition : " + position);
        Intent sportActivityIntent = new Intent(this, SportActivity.class);
        sportActivityIntent.putExtra("sports", jsonSports.toString());
        String className = listeClasses.get(position);
        JSONArray eleves = null;
        try {
            eleves = jsonClasses.getJSONObject(position).getJSONArray("eleves");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (eleves == null) {
            eleves = new JSONArray();
        }
        sportActivityIntent.putExtra("etape", getTitle());
        sportActivityIntent.putExtra("eleves", eleves.toString());
        sportActivityIntent.putExtra("className", className);
        sportActivityIntent.putExtra("json", json.toString());
        startActivity(sportActivityIntent);
    }


}