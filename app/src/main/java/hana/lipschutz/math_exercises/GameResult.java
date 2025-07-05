package hana.lipschutz.math_exercises;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_results")
public class GameResult {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String winner;
    private String dateTime;
    private int durationSeconds;
    private String screenshotPath; // נתיב לתמונה במידה ויש


    public GameResult(String winner, String dateTime, int durationSeconds) {
        this.winner = winner;
        this.dateTime = dateTime;
        this.durationSeconds = durationSeconds;
        this.screenshotPath = null;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getWinner() {
        return winner;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }
}
