package hana.lipschutz.math_exercises;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OpeningActivity extends AppCompatActivity {
    private EditText playerNameEditText;
    private Button startGameButton, buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        playerNameEditText = findViewById(R.id.playerNameEditText);
        startGameButton = findViewById(R.id.startGameButton);
        buttonHistory = findViewById(R.id.buttonHistory);

        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String savedName = prefs.getString("PLAYER_NAME", "");
        playerNameEditText.setText(savedName);

        // בדיקה אם חזרנו מהיסטוריה
        if (getIntent().getBooleanExtra("FROM_HISTORY", false)) {
            showNameChangeDialog(savedName);
        }

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = playerNameEditText.getText().toString().trim();

                if (!playerName.isEmpty()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("PLAYER_NAME", playerName);
                    editor.apply();

                    Intent intent = new Intent(OpeningActivity.this, MainActivity.class);
                    intent.putExtra("PLAYER_NAME", playerName);
                    startActivity(intent);
                } else {
                    playerNameEditText.setError("יש להזין שם");
                }
            }
        });

        buttonHistory.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningActivity.this, GameHistoryActivity.class);
            startActivity(intent);
        });

        Button buttonInfo = findViewById(R.id.buttonInfo);
        buttonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningActivity.this, InfoActivity.class);
            startActivity(intent);
        });

    }

    private void showNameChangeDialog(String currentName) {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);

        new AlertDialog.Builder(this)
                .setTitle("האם לשנות שם שחקן?")
                .setMessage("השם הנוכחי הוא: " + currentName + "\nהאם ברצונך לשנות אותו?")
                .setPositiveButton("כן", (dialog, which) -> {
                    // מחיקה מה-SharedPreferences
                    prefs.edit().remove("PLAYER_NAME").apply();
                    // איפוס התיבה
                    playerNameEditText.setText("");
                })
                .setNegativeButton("לא", null)
                .show();
    }
}
