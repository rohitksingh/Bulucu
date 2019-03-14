package com.freewifi.rohksin.freewifi.Utilities;

import android.content.Context;
import android.content.res.Resources;

import com.freewifi.rohksin.freewifi.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class LangUtility {


    private static Map<Integer, Integer> germanTranslator;

    private static Context context;
    private static Resources resources;

    public static void load(Context _context)
    {
        context = _context;
        resources = _context.getResources();
        setUpAllLanguage();
    }

    /*******************************************************************************************
     *                   Public Methods
     ******************************************************************************************/

    public static String getTranslation(int text)
    {

        Map<Integer, Integer> langaugeTranlator  = getLanguageTranlator(AppUtility.getUserLanguage());
        if(langaugeTranlator == null)
            return getResText(text);
        else {
            if(langaugeTranlator.get(text)==null){
                return getResText(text);
            }else {
                return getResText(langaugeTranlator.get(text));
            }
        }
    }

    /*********************************************************************************************
     *                      Private Helper methods
     ********************************************************************************************/


    private static Map<Integer, Integer> getLanguageTranlator(int languageCode)
    {

        if(languageCode==R.string.GERMAN_)
            return germanTranslator;
        else return null;
    }


    private static String getResText(int resId)
    {
        return resources.getString(resId);
    }


    /*            SET UP ALL TRANSLATOR   */

    private static void setUpAllLanguage()
    {
        setUpGerman();
        setUpGreek();
    }

    private static void setUpGerman()
    {
        germanTranslator = new LinkedHashMap<Integer, Integer>();
        germanTranslator.put(R.string.ENGLISH_OPEN_NETWORK, R.string.GERMAN_OPEN_NETWORK);
        germanTranslator.put(R.string.ENGLISH_TERMS, R.string.GERMAN_TERMS);

    }

    private static void setUpGreek()
    {
        germanTranslator = new LinkedHashMap<Integer, Integer>();
        germanTranslator.put(R.string.GREEK_OPEN_NETWORK, R.string.GERMAN_OPEN_NETWORK);
        germanTranslator.put(R.string.GREEk_TERMS, R.string.GERMAN_TERMS);
    }

}
