package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppUtility {

    public static boolean hasCompletedIntro ;

    private static SharedPreferences sharedPreferences;

    private static String PRIVACY_POLICY_URL = "https://r4rohit002.wixsite.com/bulucu";


    public static void loadAppUtility(Context context)
    {
        sharedPreferences = context.getSharedPreferences("AppSharedPreference", Context.MODE_PRIVATE);
        loadUserData();

        Log.d("User Complete", hasCompletedIntro+"");
    }


    public static void loadUserData(){
        hasCompletedIntro = sharedPreferences.getBoolean("INTRO_COMPLETED", false);
    }

    public static void saveIntoComplete()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("INTRO_COMPLETED", true);
        editor.commit();
        hasCompletedIntro = sharedPreferences.getBoolean("INTRO_COMPLETED", false);

        Log.d("User Complete", hasCompletedIntro+"");
    }

    public static String getPrivacyPolicyUrl()
    {
        return PRIVACY_POLICY_URL;
    }


}
