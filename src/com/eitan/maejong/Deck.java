package com.eitan.maejong;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import android.content.Context;
import android.preference.PreferenceManager;
import com.eitan.maejong.tile.ClassicTheme;
import com.eitan.maejong.tile.PokemonTheme;

public class Deck
{
   public static final int TilesPerType = 4;
   
   private TreeMap<String, MaejongTheme> _themes = new TreeMap<String, MaejongTheme>();
   private HashMap<String, List<Tile>> _tilesets = new HashMap<String, List<Tile>>();
   private Context _context;
   private ArrayList<Tile> _sameFaceTiles;
   
   public Deck(Context context)
   {
      _context = context;
      MaejongTheme[] themes = new MaejongTheme[] { new ClassicTheme(context), new PokemonTheme(context) };
      for (MaejongTheme theme : themes)
      {
         _themes.put(theme.name(), theme);
      }
      generateSameFaceTiles();
   }
   
   private void generateSameFaceTiles()
   {
      TileType[] tileTypes = currentTheme().tileTypes();
      int size = tileTypes.length*TilesPerType;
      _sameFaceTiles = new ArrayList<Tile>(size);
      TileType tt = currentTheme().tileTypes()[0];
      for (int i=0; i<size; i++)
      {
         _sameFaceTiles.add(new Tile(tt));
      }
   }
   public List<Tile> sameFaceTiles() { return _sameFaceTiles; }

   
   private String themeNameFromPreferences()
   {
      return PreferenceManager.getDefaultSharedPreferences(_context).getString("theme_option", "classic");
   }
   
   public MaejongTheme currentTheme() { return theme(themeNameFromPreferences()); }
   private MaejongTheme theme(String name)
   {
      MaejongTheme theme = _themes.get(name);
      if (theme == null) { theme = _themes.get(_themes.firstKey()); } // precaution in case i rename a layout with an update
      return theme;
   }

   public MaejongTheme[] themes() { return _themes.values().toArray(new MaejongTheme[_themes.size()]); }

   public synchronized List<Tile> tiles()
   {
      MaejongTheme theme = currentTheme();
      String themeName = theme.name();
      if (!_tilesets.containsKey(themeName))
      {
         buildTileset(theme);
      }
      return _tilesets.get(themeName);
   }

   private void buildTileset(MaejongTheme theme)
   {
      TileType[] tileTypes = theme.tileTypes();
      List<Tile> tiles = new ArrayList<Tile>(tileTypes.length*TilesPerType);
      for (TileType type : tileTypes)
      {
         for (int j=0; j<TilesPerType; j++)
         {
            tiles.add(new Tile(type));
         }
      }
      _tilesets.put(theme.name(), tiles);
   }
   
   public void substituteTiles(Map<?, Tile> tiles)
   {
      List<Tile> newTiles = tiles();
      String oldThemename = null;

      for (Map.Entry<?, Tile> entry : tiles.entrySet())
      {
         Tile oldTile = entry.getValue();
         if (oldThemename == null)
         {
            oldThemename = oldTile.type().theme().name();
         }
         int index = _tilesets.get(oldThemename).indexOf(oldTile);
         Tile t = newTiles.get(index);
         t.position(oldTile.position());
         entry.setValue(t);
      }
   }
   
   public void substituteTiles(Collection<Tile> tiles)
   {
      List<Tile> newTiles = tiles();
      String oldThemename = null;

      List<Tile> updatedTiles = new ArrayList<Tile>();
      for (Tile t : tiles)
      {
         if (oldThemename == null)
         {
            oldThemename = t.type().theme().name();
         }
         int index = _tilesets.get(oldThemename).indexOf(t);
         Tile newTile = newTiles.get(index);
         newTile.position(t.position());
         updatedTiles.add(newTile);
      }
      tiles.clear();
      tiles.addAll(updatedTiles);
   }

   public void useLargeBitmaps(boolean value)
   {
      MaejongTheme theme = currentTheme();
      String themeName = theme.name();
      theme.useLargeBitmaps(value);
      
      if (_tilesets.containsKey(themeName))
      {
         List<Tile> tiles = _tilesets.get(themeName);
         for (Tile t : tiles)
         {
            t.takeBitmap();
         }
      }
   }

}
