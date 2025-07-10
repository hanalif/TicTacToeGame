package hana.lipschutz.math_exercises;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MenuActivity extends AppCompatActivity {

    Button btnHistory, btnInfo, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnHistory = findViewById(R.id.btn_history);
        btnInfo = findViewById(R.id.btn_info);
        btnBack = findViewById(R.id.btn_back);

        // ברירת מחדל: היסטוריית משחקים
        replaceFragment(new GameHistoryFragment());

        btnHistory.setOnClickListener(v -> replaceFragment(new GameHistoryFragment()));

        btnInfo.setOnClickListener(v -> replaceFragment(new InfoFragment()));

        btnBack.setOnClickListener(v -> {
            showReturnDialog();
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private void showReturnDialog() {
        new AlertDialog.Builder(this)
                .setTitle("חזרה למסך הראשי")
                .setMessage("האם ברצונך לשנות את שם השחקן?")
                .setPositiveButton("כן", (dialog, which) -> {
                    Intent intent = new Intent(MenuActivity.this, OpeningActivity.class);
                    intent.putExtra("FROM_HISTORY", true); // אומר ל-OpeningActivity להציג דיאלוג נוסף
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("לא", (dialog, which) -> {
                    Intent intent = new Intent(MenuActivity.this, OpeningActivity.class);
                    intent.putExtra("FROM_HISTORY", false);
                    startActivity(intent);
                    finish();
                })
                .show();
    }


}
