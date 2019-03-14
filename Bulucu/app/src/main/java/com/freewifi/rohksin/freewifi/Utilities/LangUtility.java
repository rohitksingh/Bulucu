package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;

import com.freewifi.rohksin.freewifi.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class LangUtility {


    private static Map<Integer, Integer> germanTranslator;

    private static Context context;

    public static void setUpAllLanguage(Context _context)
    {
        context = _context;
        setUpGerman();
    }

    public static String getTranlation(int text, int lang_code)
    {
        int resId = getLanguageTranlator(lang_code).get(text);
        return context.getResources().getString(resId);
    }

    private static void setUpGerman()
    {
        germanTranslator = new LinkedHashMap<Integer, Integer>();
        germanTranslator.put(R.string.ENGLISH_OPEN_NETWORK, R.string.GERMAN_OPEN_NETWORK);

    }


    private static Map<Integer, Integer> getLanguageTranlator(int languageCode)
    {
        if(languageCode==R.string.GERMAN_)
            return germanTranslator;
        else return null;
    }

}
