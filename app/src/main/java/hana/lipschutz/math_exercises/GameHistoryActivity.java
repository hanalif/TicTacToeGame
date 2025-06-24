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
    private Button btnClear, backToStartButton;
    private GameHistoryAdapter adapter;
    private ArrayList<GameResult> gameResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        recyclerView = findViewById(R.id.recyclerView);
        btnClear = findViewById(R.id.btnClearData);
        backToStartButton = findViewById(R.id.backToStartButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadResults();

        // יצירת האדפטר עם אפשרות למחיקה בודדת
        adapter = new GameHistoryAdapter(gameResults, position -> {
            gameResults.remove(position);
            adapter.notifyItemRemoved(position);
            saveResults(); // עדכון ב־SharedPreferences
        });

        recyclerView.setAdapter(adapter);

        btnClear.setOnClickListener(v -> {
            // איפוס ההיסטוריה כולל מחיקת השם
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            gameResults.clear();
            adapter.notifyDataSetChanged();
        });

        backToStartButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            String savedName = prefs.getString("PLAYER_NAME", "");

            Intent intent = new Intent(GameHistoryActivity.this, OpeningActivity.class);

            // רק אם נשאר שם, נשלח שהגענו מהיסטוריה
            if (!savedName.isEmpty()) {
                intent.putExtra("FROM_HISTORY", true);
            }

            startActivity(intent);
            finish(); // סגירת המסך הנוכחי
        });
    }

    private void loadResults() {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String json = prefs.getString("GAME_RESULTS", "[]");

        Type listType = new TypeToken<ArrayList<GameResult>>() {}.getType();
        gameResults = new Gson().fromJson(json, listType);
    }

    private void saveResults() {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String updatedJson = new Gson().toJson(gameResults);
        prefs.edit().putString("GAME_RESULTS", updatedJson).apply();
    }
}
