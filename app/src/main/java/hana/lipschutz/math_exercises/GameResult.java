package hana.lipschutz.math_exercises;

public class GameResult {
    private String winner;
    private String dateTime;
    private int durationSeconds;

    public GameResult(String winner, String dateTime, int durationSeconds) {
        this.winner = winner;
        this.dateTime = dateTime;
        this.durationSeconds = durationSeconds;
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
}
