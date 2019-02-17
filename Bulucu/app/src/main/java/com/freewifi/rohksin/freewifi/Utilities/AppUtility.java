package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppUtility {

    public static boolean hasCompletedIntro ;

    private static SharedPreferences sharedPreferences;


    public static void loadAppUtility(Context context)
    {
        sharedPreferences = context.getSharedPreferences("AppSharedPreference", Context.MODE_PRIVATE);
        loadUserData();
    }


    public static void loadUserData(){
        hasCompletedIntro = sharedPreferences.getBoolean("INTRO_COMPLETED", false);
    }

    public static void saveIntoComplete()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("INTRO_COMPLETED", true);
        hasCompletedIntro = true;
    }


}
