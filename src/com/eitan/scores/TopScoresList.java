package com.eitan.scores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopScoresList
{
   private static int NUM_SCORES = 5;
   private static int MAXTIME = 5999;
   
   private LinkedList<ScoreEntry> _topScores = new LinkedList<ScoreEntry>();

   public TopScoresList(String scoresAsText)
   {
      initEntries(scoresAsText);
   }
   
   private void initEntries(String scoresAsText)
   {
      _topScores.clear();
      try
      {
         _topScores.addAll(restoreScores(scoresAsText));
         Collections.sort(_topScores);
      }
      catch (JSONException ex) {}

      for (int i=_topScores.size(); i<NUM_SCORES; i++)
      {
         _topScores.add(new ScoreEntry("--", MAXTIME));
      }
   }
   
   public LinkedList<ScoreEntry> entries() { return _topScores; }

   public void addScore(String name, int value)
   {
      addScore(new ScoreEntry(name, value));
   }
   public void addScore(ScoreEntry entry)
   {
      _topScores.offer(entry);
      Collections.sort(_topScores);
      if (_topScores.size() > NUM_SCORES)
      {
         _topScores.removeLast();
      }
   }
   
   public void clearScores() { initEntries(""); }
   
   public boolean qualifiesAsTopScore(int value) { return _topScores.getLast().worseThan(value); }
   
   public String scoresAsText() throws JSONException
   {
      JSONArray ra = new JSONArray();
      for (ScoreEntry entry : _topScores)
      {
         JSONObject jsobj = new JSONObject();
         jsobj.put("name", entry.name());
         jsobj.put("value", entry.value());
         ra.put(jsobj);
      }
      return ra.toString();
   }
   private Collection<ScoreEntry> restoreScores(String scoresText) throws JSONException
   {
      JSONArray ra = new JSONArray(scoresText);
      ArrayList<ScoreEntry> entries = new ArrayList<ScoreEntry>();
      for (int i=0; i<ra.length(); i++)
      {
         JSONObject jsobj = ra.getJSONObject(i);
         entries.add(new ScoreEntry(jsobj.getString("name"), jsobj.getInt("value")));
      }
      return entries;
   }
}
