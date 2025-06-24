package hana.lipschutz.math_exercises;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GameHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Button btnClear;
    Button backToStartButton;
    private GameHistoryAdapter adapter;
    private ArrayList<GameResult> gameResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        recyclerView = findViewById(R.id.recyclerView);
        backToStartButton = findViewById(R.id.backToStartButton);
        btnClear = findViewById(R.id.btnClearData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadResults();
        adapter = new GameHistoryAdapter(gameResults);
        recyclerView.setAdapter(adapter);

        backToStartButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameHistoryActivity.this, OpeningActivity.class);
            startActivity(intent);
        });

        btnClear.setOnClickListener(v -> {
            // מחיקת כל הנתונים שנשמרו
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // איפוס הרשימה בתצוגה
            gameResults.clear();
            adapter.notifyDataSetChanged();
        });
    }

    private void loadResults() {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String json = prefs.getString("GAME_RESULTS", "[]");

        Type listType = new TypeToken<ArrayList<GameResult>>() {}.getType();
        gameResults = new Gson().fromJson(json, listType);
    }
}