package com.eitan.scores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.eitan.maejong.R;

public class TopScoresMgr
{
   private HashMap<String, TopScoresList> _scoresMap = new HashMap<String, TopScoresList>();
   private String _config;
   private String _lastRecordedName = "";

   public static String anonymous = "anonymous";

   public TopScoresMgr(Context context)
   {
      anonymous = context.getString(R.string.anonymous);
   }
   
   public String gameConfig() { return _config; }
   public void setConfig(String config) { _config = config; }

   public void addScore(ScoreEntry entry)
   {
      scores().addScore(entry);
      updateLastRecordedName(entry);
   }
   public void addScore(String name, int seconds) { addScore(new ScoreEntry(name, seconds)); }

   private void updateLastRecordedName(ScoreEntry entry)
   {
      String name = entry.name();
      if (name == null || name.length() == 0 ||
          "--".equals(name) || anonymous.equals(name)) return;
      _lastRecordedName = name;
   }
   public String lastRecordedName() { return _lastRecordedName; }

   public boolean qualifiesAsTopScore(int value) { return scores().qualifiesAsTopScore(value); }
   public void clearScores() { clearScores(_config); }
   
   public ArrayList<ScoreEntry> allEntries()
   {
      ArrayList<ScoreEntry> allEntries = new ArrayList<ScoreEntry>();
      for (TopScoresList tsl : _scoresMap.values())
      {
         allEntries.addAll(tsl.entries());
      }
      return allEntries;
   }
   
   private TopScoresList scores() { return scores(_config); }

   synchronized TopScoresList scores(String config)
   {
      String key = keyForConfig(config);
      TopScoresList list = _scoresMap.get(key);
      if (list == null)
      {
         _scoresMap.put(key, new TopScoresList(""));
      }
      return _scoresMap.get(key);
   }

   void clearScores(String key) { scores(key).clearScores(); }
   
   private String keyForConfig(String config) { return "scores_"+config; }
   
   // -- persistence related --
   
   public void restoreScoresFromPrefs(Set<String> configurations, SharedPreferences prefs)
   {
      for (String config : configurations)
      {
         String key = keyForConfig(config);
         String scoresAsText = prefs.getString(key, "");
//         Log.d("Eitan", "restoring scores for key "+key+" from "+scoresAsText);
         TopScoresList tsl = new TopScoresList(scoresAsText);
         _scoresMap.put(key, tsl);
      }
   }
   public void saveScores(SharedPreferences prefs)
   {
      try
      {
         Editor editor = prefs.edit();
         Map<String, ?> detritusmap = prefs.getAll();
         for (String key : _scoresMap.keySet())
         {
            String scoresAsText = _scoresMap.get(key).scoresAsText();
//            Log.d("Eitan", "saving scores "+scoresAsText+" into key "+key);
            editor.putString(key, scoresAsText);
            detritusmap.remove(key);
         }
         // make sure old/unused game configs (layouts) don't linger.
         for (String key : detritusmap.keySet())
         {
            editor.remove(key);
         }
         editor.commit();
      }
      catch (JSONException ex)
      {
         ex.printStackTrace();
      }
   }
   
}
