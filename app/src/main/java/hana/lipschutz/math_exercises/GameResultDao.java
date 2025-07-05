package hana.lipschutz.math_exercises;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameResultDao {

    @Insert
    void insert(GameResult gameResult);

    @Update
    void update(GameResult gameResult);

    @Delete
    void delete(GameResult gameResult);

    @Query("SELECT * FROM game_results ORDER BY id DESC")
    List<GameResult> getAllResults();

    @Query("DELETE FROM game_results")
    void deleteAll();
}
