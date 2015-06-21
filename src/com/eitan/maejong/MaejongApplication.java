package com.eitan.maejong;

import java.util.Collection;
import java.util.TreeMap;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import com.eitan.maejong.layouts.Boat;
import com.eitan.maejong.layouts.Checkers;
import com.eitan.maejong.layouts.Classic;
import com.eitan.maejong.layouts.Crown;
import com.eitan.maejong.layouts.Isengard;
import com.eitan.maejong.layouts.Oh;
import com.eitan.maejong.layouts.Pyramid;
import com.eitan.maejong.layouts.Simple;
import com.eitan.maejong.layouts.ThreePyramids;
import com.eitan.maejong.layouts.TieFighter;
import com.eitan.scores.TopScoresMgr;

public class MaejongApplication extends Application implements OnSharedPreferenceChangeListener
{
   private TopScoresMgr _topScoresMgr;
   private SoundMgr _soundMgr;
   private Deck _deck;
   private TreeMap<String, Layout> _layouts = new TreeMap<String, Layout>();
   private Layout _currentLayout;
   private Game _game;
   
   public void onCreate()
   {
      super.onCreate();
      
      _deck = new Deck(MaejongApplication.this);

      Layout[] layoutsArray = new Layout[] { new Classic(), new Simple(), new Crown(), 
            new Oh(), new TieFighter(), new Checkers(), new Boat(), new Pyramid(), new Isengard(), 
            new ThreePyramids() };
      for (Layout layout : layoutsArray)
      {
         String name = layout.name();
         _layouts.put(name, layout);
         String idname = String.format("com.eitan.maejong:string/layout_%s", name);
         int id = getResources().getIdentifier(idname, null, null);
         String caption = getString(id);
         layout.setCaption(caption);
      }
      
      _topScoresMgr = new TopScoresMgr(this);
      selectLayout();
      _game = new Game(_deck, _currentLayout);
      restoreState(getSharedPreferences("Maejong", MODE_PRIVATE));
      
      _soundMgr = new SoundMgr(MaejongApplication.this);

      PreferenceManager.getDefaultSharedPreferences(MaejongApplication.this).
                              registerOnSharedPreferenceChangeListener(MaejongApplication.this);
   }
   
   public void saveState()
   {
      saveState(getSharedPreferences("Maejong", MODE_PRIVATE));
   }
   
   private void restoreState(SharedPreferences prefs)
   {
      _topScoresMgr.restoreScoresFromPrefs(_layouts.keySet(), prefs);
      _game.restoreState(prefs);
   }
   private void saveState(SharedPreferences prefs)
   {
      _topScoresMgr.saveScores(prefs);
      _game.saveState(prefs);
   }
    
   public void onTerminate()
   {
      super.onTerminate();
      PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
   }

   private String layoutNameFromPreferences()
   {
      return PreferenceManager.getDefaultSharedPreferences(this).getString("layout_option", "classic");
   }

   public void selectLayout()
   {
      String layoutName = layoutNameFromPreferences();
      _currentLayout = _layouts.get(layoutName);
      if (_currentLayout == null)  // precaution in case i rename a layout with an update
      {
         _currentLayout = _layouts.get(_layouts.firstKey());
      }
      _topScoresMgr.setConfig(layoutName);
   }
   
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
   {
      if ("theme_option".equals(key))
      {
         _game.substituteTiles();
      }
      else if ("layout_option".equals(key))
      {
         selectLayout();
         _game.setLayout(_currentLayout);
      }
   }
   
   public SoundMgr soundMgr() { return _soundMgr; }
   public Deck deck() { return _deck; }
   public TopScoresMgr scoresMgr() { return _topScoresMgr; }
   public Game game() { return _game; }
   
   public Collection<Layout> layouts() { return _layouts.values(); }
   public Layout currentLayout() { return _currentLayout; }
   
   public MaejongTheme currentTheme() { return _deck.currentTheme(); }
   
}
