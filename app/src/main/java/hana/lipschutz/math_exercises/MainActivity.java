package hana.lipschutz.math_exercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Model game;
    private TextView playerNameTextView;
    private Button[][] buttons = new Button[3][3];
    private String playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerName = getIntent().getStringExtra("PLAYER_NAME");
        if (playerName == null || playerName.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            playerName = prefs.getString("PLAYER_NAME", "שחקן");
        }

        game = new Model();
        playerName = getIntent().getStringExtra("PLAYER_NAME");

        playerNameTextView = findViewById(R.id.playerNameTextView);
        playerNameTextView.setText("תורו של: " + playerName);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int index = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button btn = (Button) gridLayout.getChildAt(index);
                buttons[row][col] = btn;

                int finalRow = row;
                int finalCol = col;

                btn.setOnClickListener(v -> handlePlayerClick(finalRow, finalCol));
                index++;
            }
        }
    }

    private void handlePlayerClick(int row, int col) {
        if (game.isGameOver()) return;

        if (game.makePlayerMove(row, col)) {
            buttons[row][col].setText(game.PLAYER);

            if (game.checkWinner(game.PLAYER)) {
                game.setGameOver(true);
                showEndDialog(playerName + " ניצח!");
                return;
            }

            if (game.isFull()) {
                game.setGameOver(true);
                showEndDialog("תיקו!");
                return;
            }

            game.makeComputerMove();
            updateBoardFromModel();

            if (game.checkWinner(game.COMPUTER)) {
                game.setGameOver(true);
                showEndDialog("המחשב ניצח!");
            } else if (game.isFull()) {
                game.setGameOver(true);
                showEndDialog("תיקו!");
            }
        }
    }

    private void updateBoardFromModel() {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                buttons[row][col].setText(game.getSymbolAt(row, col));
    }

    private void showEndDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("סיום המשחק")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("שחק שוב", (dialog, which) -> resetGame())
                .setNegativeButton("סגור", (dialog, which) -> finish())
                .show();
    }

    private void resetGame() {
        game.resetGame();
        playerNameTextView.setText("תורו של: " + playerName);
        updateBoardFromModel();
    }
}