package android.nguyenphuocthienan.listtaskdao.database;

import android.nguyenphuocthienan.listtaskdao.Task;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM task")
    List<Task> getListTask();

    // check xem bản ghi data đã tồn tại chưa
    @Query("SELECT * FROM task WHERE name = :name")
    List<Task> checkTask(String name);

    @Update
    void updateTask(Task task);
}
