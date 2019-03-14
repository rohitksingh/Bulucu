package com.freewifi.rohksin.freewifi.Utilities;

import com.freewifi.rohksin.freewifi.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class LanguageUtility {


    private static Map<Integer, Integer> germanTranslator;

    public static void setUpAllLanguage()
    {
        setUpGerman();
    }


    private static void setUpGerman()
    {
        germanTranslator = new LinkedHashMap<Integer, Integer>();
        germanTranslator.put(R.string.ENGLISH_OPEN_NETWORK, R.string.GERMAN_OPEN_NETWORK);
    }


    private Map<Integer, Integer> getLanguageTranlator(int languageCode)
    {
        if(languageCode==R.string.GERMAN_)
            return germanTranslator;
        else return null;
    }

}
