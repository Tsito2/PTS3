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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListRecyclerViewAdapter.ItemClickListener {

    ListRecyclerViewAdapter adapter;
    public final String TAG = "ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(getIntent().getExtras().getString("classes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("In list activity : " + jsonArray);

        ArrayList<String> listeClasses = new ArrayList<>();
        JSONArray jArray = jsonArray;
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                try {
                    listeClasses.add(jArray.getJSONObject(i).getString("nomClasse"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("After parsing : " + listeClasses);
        /*ArrayList<String> classNames = new ArrayList<>();
        classNames.add("Sixième A");
        classNames.add("Sixième B");
        classNames.add("Cinquième A");
        classNames.add("Quatrième B");
        classNames.add("Troisième C");

         */

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
        startActivity(sportActivityIntent);
    }


}