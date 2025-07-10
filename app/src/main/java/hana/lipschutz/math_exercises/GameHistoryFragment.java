package hana.lipschutz.math_exercises;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppDatabase db;
    private Button btnClear, buttonInfo;
    private GameHistoryAdapter adapter;
    private List<GameResult> gameResults;

    public GameHistoryFragment() {
        // חובה קונסטרקטור ריק
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_game_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnClear = view.findViewById(R.id.btnClearData);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = AppDatabase.getInstance(requireContext());

        loadResults();

        btnClear.setOnClickListener(v -> clearHistory());

        return view;
    }

    private void loadResults() {
        new Thread(() -> {
            gameResults = db.gameResultDao().getAllResults();

            requireActivity().runOnUiThread(() -> {
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

            requireActivity().runOnUiThread(() -> adapter.notifyItemRemoved(position));
        }).start();
    }

    private void clearHistory() {
        new Thread(() -> {
            db.gameResultDao().deleteAll();
            gameResults.clear();

            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    private void showGameDetailsDialog(GameResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_game_details, null);

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
