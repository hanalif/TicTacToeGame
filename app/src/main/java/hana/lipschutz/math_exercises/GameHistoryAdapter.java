package hana.lipschutz.math_exercises;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameHistoryAdapter extends RecyclerView.Adapter<GameHistoryAdapter.GameViewHolder> {

    private List<GameResult> gameResults;

    public GameHistoryAdapter(List<GameResult> gameResults) {
        this.gameResults = gameResults;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_result, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameResult result = gameResults.get(position);
        holder.winnerText.setText("ניצח: " + result.getWinner());
        holder.dateTimeText.setText("תאריך: " + result.getDateTime());
        holder.durationText.setText("משך: " + result.getDurationSeconds() + " שניות");
    }

    @Override
    public int getItemCount() {
        return gameResults.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView winnerText, dateTimeText, durationText;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            winnerText = itemView.findViewById(R.id.winnerTextView);
            dateTimeText = itemView.findViewById(R.id.dateTimeTextView);
            durationText = itemView.findViewById(R.id.durationTextView);
        }
    }
}
