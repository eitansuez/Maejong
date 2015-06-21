package com.eitan.maejong;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ThemePicker extends Activity
{
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.theme_picker);
   }
   
   public void setThemeAndFinish(String themeName)
   {
       PreferenceManager.getDefaultSharedPreferences(ThemePicker.this).edit().
             putString("theme_option", themeName).commit();
       finish();
   }

}
