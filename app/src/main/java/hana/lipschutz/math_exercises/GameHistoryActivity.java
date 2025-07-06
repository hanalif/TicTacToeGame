package hana.lipschutz.math_exercises;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppDatabase db;
    private Button btnClear, backToStartButton, buttonInfo;
    private GameHistoryAdapter adapter;
    private List<GameResult> gameResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        recyclerView = findViewById(R.id.recyclerView);
        btnClear = findViewById(R.id.btnClearData);
        backToStartButton = findViewById(R.id.backToStartButton);
        buttonInfo = findViewById(R.id.buttonInfo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);

        loadResults();

        btnClear.setOnClickListener(v -> clearHistory());

        backToStartButton.setOnClickListener(v -> showReturnDialog());

        buttonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(GameHistoryActivity.this, InfoActivity.class);
            startActivity(intent);
        });
    }

    private void showReturnDialog() {
        new AlertDialog.Builder(this)
                .setTitle("חזרה למסך הראשי")
                .setMessage("האם ברצונך לשנות את שם השחקן?")
                .setPositiveButton("כן", (dialog, which) -> {
                    // רק שולחים דגל, לא מוחקים פה
                    Intent intent = new Intent(GameHistoryActivity.this, OpeningActivity.class);
                    intent.putExtra("FROM_HISTORY", true);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("לא", (dialog, which) -> {
                    Intent intent = new Intent(GameHistoryActivity.this, OpeningActivity.class);
                    intent.putExtra("FROM_HISTORY", false);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void loadResults() {
        new Thread(() -> {
            gameResults = db.gameResultDao().getAllResults();

            runOnUiThread(() -> {
                adapter = new GameHistoryAdapter(
                        gameResults,
                        position -> deleteResult(position),
                        this::showGameDetailsDialog
                );
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private void deleteResult(int position) {
        new Thread(() -> {
            db.gameResultDao().delete(gameResults.get(position));
            gameResults.remove(position);

            runOnUiThread(() -> adapter.notifyItemRemoved(position));
        }).start();
    }

    private void clearHistory() {
        new Thread(() -> {
            db.gameResultDao().deleteAll();
            gameResults.clear();

            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
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
