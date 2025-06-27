package hana.lipschutz.math_exercises;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameHistoryAdapter extends RecyclerView.Adapter<GameHistoryAdapter.GameViewHolder> {

    private List<GameResult> gameResults;
    private OnDeleteClickListener onDeleteClickListener;
    private OnItemClickListener onItemClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(GameResult result);
    }

    public GameHistoryAdapter(List<GameResult> gameResults,
                              OnDeleteClickListener deleteClickListener,
                              OnItemClickListener itemClickListener) {
        this.gameResults = gameResults;
        this.onDeleteClickListener = deleteClickListener;
        this.onItemClickListener = itemClickListener;
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

        // לחיצה על פריט
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(result);
            }
        });

        // לחיצה על כפתור מחיקה
        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameResults.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView winnerText, dateTimeText, durationText;
        Button deleteButton;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            winnerText = itemView.findViewById(R.id.winnerTextView);
            dateTimeText = itemView.findViewById(R.id.dateTimeTextView);
            durationText = itemView.findViewById(R.id.durationTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
