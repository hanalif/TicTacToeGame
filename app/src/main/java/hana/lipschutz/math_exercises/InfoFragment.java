package hana.lipschutz.math_exercises;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    private Spinner videoSpinner;
    private Button buttonToHistory, buttonToOpening;
    private static final String TAG = "InfoFragment";

    private String[] titles = {
            "מהו איקס עיגול?",
            "אסטרטגיות בסיסיות",
            "איך לנצח תמיד",
            "איקס עיגול - כיף ומשחקים"
    };

    private String[] videoUrls = {
            "https://www.youtube.com/watch?v=3qzcAMShotQ",
            "https://www.youtube.com/watch?v=Hz51B-aQUeE",
            "https://www.youtube.com/watch?v=WUX0hB1u0vE",
            "https://www.youtube.com/watch?v=QNFQvX-MQgI"
    };

    public InfoFragment() {
        // חובה קונסטרקטור ריק
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info, container, false);

        videoSpinner = view.findViewById(R.id.videoSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                titles
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

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
                videoSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // לא נדרש
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart נקרא");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume נקרא");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause נקרא");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop נקרא");
    }
}
