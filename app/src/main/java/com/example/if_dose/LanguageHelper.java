package com.example.if_dose;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageHelper extends AppCompatActivity {

    public void setLocale(Resources res, String lang , SharedPreferences editor) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration Configuration = new Configuration();
        Configuration.locale = locale;
        res.updateConfiguration(Configuration,res.getDisplayMetrics());

        //SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();

        SharedPreferences.Editor editora = editor.edit();
        editora.putString("My_lang", lang);
        editora.apply();

    }


   public void loadLocale(Resources res, SharedPreferences prefs){

        //SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);

        String language= prefs.getString("My_lang","");
        setLocale(res,language,prefs);
    }


/* public void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration Configuration = new Configuration();
        Configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(Configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();

    }


 */



}



