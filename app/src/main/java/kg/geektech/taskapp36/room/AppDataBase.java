package kg.geektech.taskapp36.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geektech.taskapp36.model.Task;

@Database(entities={Task.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
