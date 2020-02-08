package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    boolean status;
    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        this.context = context;
        getSharedPreferences();
    }

    private void getSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(
                context.getString(
                        R.string.my_preference), Context.MODE_PRIVATE);
    }

    public void writePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.my_preference_key), "INIT_OK");
        editor.apply();
    }

    public boolean checkPreferences() {

        if(sharedPreferences.getString(
                context.getString(
                        R.string.my_preference_key), "null").equals("null")) {
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    public void writePreferences2() {
        SharedPreferences.Editor editor2 = sharedPreferences.edit();
        editor2.putString(context.getString(R.string.my_preference_key2), "INIT_OK");
        editor2.apply();
    }

    public boolean checkPreferences2() {

        if(sharedPreferences.getString(
                context.getString(
                        R.string.my_preference_key2), "null").equals("null")) {
            status = false;
        } else {
            status = true;
        }
        return status;
    }


    public void clearPreferences() {
        sharedPreferences.edit().clear().apply();
    }
}
