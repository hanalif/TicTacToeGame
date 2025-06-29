package hana.lipschutz.math_exercises;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        adapter = new GameHistoryAdapter(
                gameResults,
                position -> {
                    // מחיקה בלחיצה על כפתור מחיקה
                    gameResults.remove(position);
                    adapter.notifyItemRemoved(position);
                    saveResults();
                },
                this::showGameDetailsDialog // פעולה בלחיצה על פריט
        );

        recyclerView.setAdapter(adapter);

        btnClear.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            gameResults.clear();
            adapter.notifyDataSetChanged();
        });

        backToStartButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            String savedName = prefs.getString("PLAYER_NAME", "");
            Intent intent = new Intent(GameHistoryActivity.this, OpeningActivity.class);
            if (!savedName.isEmpty()) {
                intent.putExtra("FROM_HISTORY", true);
            }
            startActivity(intent);
            finish();
        });

        Button buttonInfo = findViewById(R.id.buttonInfo);
        buttonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(GameHistoryActivity.this, InfoActivity.class);
            startActivity(intent);
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

    private void showGameDetailsDialog(GameResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_game_details, null);

        TextView winnerText = dialogView.findViewById(R.id.dialogWinnerText);
        TextView dateTimeText = dialogView.findViewById(R.id.dialogDateTimeText);
        TextView durationText = dialogView.findViewById(R.id.dialogDurationText);
        Button closeBtn = dialogView.findViewById(R.id.dialogCloseButton);

        winnerText.setText("ניצח: " + result.getWinner());
        dateTimeText.setText("תאריך: " + result.getDateTime());
        durationText.setText("משך: " + result.getDurationSeconds() + " שניות");

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        closeBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
