package com.eitan.maejong;

import com.eitan.maejong.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity
{
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
   }
   
   public static boolean soundOn(Context context)
   {
      return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sound_option", true);
   }
   public static boolean highlightFreeTiles(Context context)
   {
      return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("highlight_option", false);
   }
   public static boolean shakeToShuffleOn(Context context)
   {
      return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("shake_option", true);
   }
   
}
