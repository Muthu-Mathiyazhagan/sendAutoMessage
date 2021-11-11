package com.geniobits.autosendwhatappgroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class PrefHelper {
    private final Context context;
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final String ISON = "ison";

    public PrefHelper(Context context){
        this.context = context;
        pref = context.getSharedPreferences("MySP", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setIsOn(boolean isOn) {
        editor.putBoolean(ISON, isOn);
        editor.commit();
    }

    public boolean getOn(){
        return pref.getBoolean(ISON, false);
    }


}
