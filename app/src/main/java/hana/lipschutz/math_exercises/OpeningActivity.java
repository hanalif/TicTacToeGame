package hana.lipschutz.math_exercises;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class OpeningActivity extends AppCompatActivity {
    private EditText playerNameEditText;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        playerNameEditText = findViewById(R.id.playerNameEditText);
        startGameButton = findViewById(R.id.startGameButton);

        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        String savedName = prefs.getString("PLAYER_NAME", "");
        playerNameEditText.setText(savedName);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = playerNameEditText.getText().toString().trim();

                // אפשר לבדוק שהשדה לא ריק
                if (!playerName.isEmpty()) {
                    SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
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
    }
}