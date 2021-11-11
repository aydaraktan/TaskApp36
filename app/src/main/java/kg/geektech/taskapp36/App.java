package kg.geektech.taskapp36;

import android.app.Application;

import androidx.room.Room;

import kg.geektech.taskapp36.room.AppDataBase;

public class App extends Application {

    private AppDataBase dataBase;
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        dataBase= Room.databaseBuilder(this,AppDataBase.class,"database").allowMainThreadQueries().build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDataBase getDataBase() {
        return dataBase;
    }
}
