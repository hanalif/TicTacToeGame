package hana.lipschutz.math_exercises;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private Spinner videoSpinner;
    private Button buttonToHistory, buttonToOpening;

    private String[] titles = {
            "מהו איקס עיגול?",
            "אסטרטגיות בסיסיות",
            "איך לנצח תמיד",
            "איקס עיגול - כיף ומשחקים"
    };

    private String[] videoUrls = {
            "https://www.youtube.com/watch?v=3qzcAMShotQ", // דוגמאות, עדכן ללינקים אמיתיים
            "https://www.youtube.com/watch?v=Hz51B-aQUeE",
            "https://www.youtube.com/watch?v=WUX0hB1u0vE",
            "https://www.youtube.com/watch?v=QNFQvX-MQgI"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        videoSpinner = findViewById(R.id.videoSpinner);

        // יצירת אדפטר מותאם עם layout מותאם אישית
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,         // העיצוב של ה-item הראשי
                titles
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item); // העיצוב של התפריט הנפתח

        videoSpinner.setAdapter(adapter);

        videoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean firstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstSelection) {
                    firstSelection = false;
                    return;
                }

                String url = videoUrls[position];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

                // לאחר הפעלה, לאפס את הבחירה
                videoSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // לא נדרש
            }
        });

        buttonToOpening = findViewById(R.id.buttonToOpening);
        buttonToOpening.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this, OpeningActivity.class);
            startActivity(intent);
            finish();
        });

        buttonToHistory = findViewById(R.id.buttonToHistory);
        buttonToHistory.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this, GameHistoryActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
