package com.example.evalsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.evalsport.models.Sport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SportActivity extends AppCompatActivity implements SportsRecyclerViewAdapter.ItemClickListener {

    SportsRecyclerViewAdapter adapter;
    public final String TAG = "SportActivity";
    private JSONObject json;
    private JSONArray eleves = null;
    private JSONArray jsonSports = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        try {
            jsonSports = new JSONArray(getIntent().getExtras().getString("sports"));
            eleves = new JSONArray(getIntent().getExtras().getString("eleves"));
            json = new JSONObject(getIntent().getExtras().getString("json"));
            setTitle(getIntent().getExtras().getString("etape") + "/" + getIntent().getExtras().getString("className"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("In listSports activity : " + jsonSports);

        ArrayList<Sport> listSports = new ArrayList<>();
        if (jsonSports != null) {
            for (int i=0;i<jsonSports.length();i++){
                try {
                    listSports.add(new Sport(jsonSports.getJSONObject(i).getString("nomSport"), R.drawable.course_haie));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("After parsing : " + listSports);
        /*listSports.add(new Sport("Escalade", R.drawable.escalade));
        listSports.add(new Sport("Course haie", R.drawable.course_haie));
        listSports.add(new Sport("Natation", R.drawable.natation));
        listSports.add(new Sport("Saut en longueur", R.drawable.saut));
        listSports.add(new Sport("Basketball", R.drawable.basket));
        listSports.add(new Sport("Football", R.drawable.football));*/

        RecyclerView recyclerView = findViewById(R.id.rvSports);
        LinearLayout linearLayout = findViewById(R.id.loSports);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);

/*
        val layoutManager FlexboxLayoutManager( context: this)
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        layoutManager.flexDirection = FlexDirection. ROW layoutManager.flexWrap = FlexWrap.WRAP
        my recycler.layout Manager = = layout Manager
*/

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayout.getOrientation());
        Drawable drawable = getResources().getDrawable(R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new SportsRecyclerViewAdapter(this, listSports);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG, "View : " + view.toString() + "\nPosition : " + position);
        Intent elevesActivityIntent = new Intent(this, ListEleveActivity.class);
        String sportTitle = "";
        try {
             sportTitle = jsonSports.getJSONObject(position).getString("nomSport");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        elevesActivityIntent.putExtra("sportTitle", sportTitle);
        elevesActivityIntent.putExtra("etape", getTitle());
        elevesActivityIntent.putExtra("eleves", eleves.toString());
        elevesActivityIntent.putExtra("json", json.toString());
        startActivity(elevesActivityIntent);
    }

}
