package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 *    TOD0
 *    1) make hasCompletedIntro private
 *    2) make Context instance variable
 *
 */

public class AppUtility {

    public static boolean hasCompletedIntro ;

    private static SharedPreferences sharedPreferences;

    private static String PRIVACY_POLICY_URL = "https://r4rohit002.wixsite.com/bulucu";
    private static Context context;


    public static void load(Context _context)
    {
        sharedPreferences = _context.getSharedPreferences("AppSharedPreference", Context.MODE_PRIVATE);
        context = _context;
        loadUserData();
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

    public static void setUserLanguage(int language_code)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("USER_LANGUAGE", language_code);
        editor.commit();
    }

    public static int getUserLanguage()
    {
        return sharedPreferences.getInt("USER_LANGUAGE", -1);
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



}
