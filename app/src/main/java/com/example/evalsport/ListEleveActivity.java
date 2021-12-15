package com.example.evalsport;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListEleveActivity extends AppCompatActivity implements ElevesRecyclerViewAdapter.ItemClickListener {

    ElevesRecyclerViewAdapter adapter;
    public final String TAG = "ListEleveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleves);

        JSONObject json = null;
        JSONArray jsonEleves = null;
        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
            jsonEleves = json.getJSONArray("classes").getJSONObject(1).getJSONArray("eleves");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> listeEleves = new ArrayList<>();
        for (int i=0; i < jsonEleves.length(); i++) {
            JSONObject e = null;
            try {
                e = jsonEleves.getJSONObject(i);
                listeEleves.add(e.getString("prenomEleve") + " " + e.getString("nomEleve"));
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }
/*
        listeEleves.add("Elève A");
        listeEleves.add("Elève B");
        listeEleves.add("Elève C");
        listeEleves.add("Elève D");
        listeEleves.add("Elève E");

 */



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

    @Override
    public void onItemClick(View v, int position) {
        Log.i(TAG, "View : " + v.toString() + "\nPosition : " + position);
    }
}
