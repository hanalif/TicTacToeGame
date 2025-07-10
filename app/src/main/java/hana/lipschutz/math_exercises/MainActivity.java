package hana.lipschutz.math_exercises;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Model game;
    private AppDatabase db;
    private TextView playerNameTextView;
    private ImageView imageViewProfile;
    private Button[][] buttons = new Button[3][3];
    private String playerName;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getInstance(this);

        // קבלת שם שחקן מה-Intent או מה-SharedPreferences
        playerName = getIntent().getStringExtra("PLAYER_NAME");
        if (playerName == null || playerName.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            playerName = prefs.getString("PLAYER_NAME", "שחקן");
        } else {
            SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
            prefs.edit().putString("PLAYER_NAME", playerName).apply(); // שימור השם
        }

        startTime = System.currentTimeMillis();
        game = new Model();

        playerNameTextView = findViewById(R.id.playerNameTextView);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        loadProfileImage();

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

    private void loadProfileImage() {
        File file = new File(getFilesDir(), "profile.jpg");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageViewProfile.setImageBitmap(bitmap);
        } else {
            // תמונת ברירת מחדל במידה ואין תמונה
            imageViewProfile.setImageResource(R.drawable.profile_avatar);
        }
    }

    private void handlePlayerClick(int row, int col) {
        if (game.isGameOver()) return;

        if (game.makePlayerMove(row, col)) {
            buttons[row][col].setText(Model.PLAYER);

            if (game.checkWinner(Model.PLAYER)) {
                game.setWinner(playerName);
                game.setGameOver(true);
                saveCurrentResult();
                showEndDialog(playerName + " ניצח!");
                return;
            }

            if (game.isFull()) {
                game.setWinner("תיקו");
                game.setGameOver(true);
                saveCurrentResult();
                showEndDialog("תיקו!");
                return;
            }

            // עדכון לכותרת ותמונה של המחשב
            playerNameTextView.setText("תורו של: מחשב");
            imageViewProfile.setImageResource(R.drawable.computer_avatar);

            // השהייה קטנה לפני מהלך המחשב כדי להציג את הכותרת והתמונה
            new android.os.Handler().postDelayed(() -> {
                game.makeComputerMove();
                updateBoardFromModel();

                if (game.checkWinner(Model.COMPUTER)) {
                    game.setWinner("מחשב");
                    game.setGameOver(true);
                    saveCurrentResult();
                    showEndDialog("המחשב ניצח!");
                } else if (game.isFull()) {
                    game.setWinner("תיקו");
                    game.setGameOver(true);
                    saveCurrentResult();
                    showEndDialog("תיקו!");
                } else {
                    // החזרת התור לשחקן
                    playerNameTextView.setText("תורו של: " + playerName);
                    loadProfileImage();
                }
            }, 800);
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
                .setNegativeButton("סגור", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void resetGame() {
        game.resetGame();
        startTime = System.currentTimeMillis();
        playerNameTextView.setText("תורו של: " + playerName);
        updateBoardFromModel();
    }

    private void saveCurrentResult() {
        long endTime = System.currentTimeMillis();
        int durationSeconds = (int) ((endTime - startTime) / 1000);
        saveResultToDatabase(game.getWinner(), durationSeconds);
    }

    private void saveResultToDatabase(String winner, int durationSeconds) {
        new Thread(() -> {
            String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            GameResult result = new GameResult(winner, dateTime, durationSeconds);

            db.gameResultDao().insert(result);
        }).start();
    }

}
