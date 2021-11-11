package kg.geektech.taskapp36;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Context context) {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void saveBoardState() {
        preferences.edit().putBoolean("isShown", true).apply();
    }

    public boolean isBoardShown() {
        return preferences.getBoolean("isShown", false);
    }


    public void trueBoolean() {
        preferences.edit().putBoolean("lol", true).apply();
    }

    public void falseBoolean() {
        preferences.edit().putBoolean("lol", false).apply();
    }

    public boolean cancel() {
        return preferences.getBoolean("lol", false);
    }
    public void setTxt(String a){
        preferences.edit().putString("pol",a).apply();
    }
    public String getTxt(){
        return preferences.getString("pol",null);
    }

    public void setImage(String a){
        preferences.edit().putString("tol",a).apply();
    }
    public String getImage(){
        return preferences.getString("tol",null);
    }
}
