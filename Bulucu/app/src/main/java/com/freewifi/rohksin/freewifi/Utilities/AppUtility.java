package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppUtility {

    public static boolean hasCompletedIntro ;

    private static SharedPreferences sharedPreferences;

    private static String PRIVACY_POLICY_URL = "https://r4rohit002.wixsite.com/bulucu";


    private static Context context;

    public static void loadAppUtility(Context _context)
    {
        context = _context;
        sharedPreferences = _context.getSharedPreferences("AppSharedPreference", Context.MODE_PRIVATE);
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

    public static String getString(int resId)
    {
        return context.getResources().getString(resId);
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void setNotifySericeStatus(boolean status)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NOTIFY_ME_STATUS", status);
        editor.commit();
    }

    public static boolean getNotifyServiceStatus()
    {
        return sharedPreferences.getBoolean("NOTIFY_ME_STATUS", false);
    }



}
