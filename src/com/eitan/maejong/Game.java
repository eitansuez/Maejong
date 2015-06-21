package com.eitan.maejong;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;
import com.eitan.general.TimeUtil;

public class Game
{
   public static final int Perspective_run = 6;
   public static final int Perspective_rise = 6;

   private Layout _layout;
   protected Deck _deck;
   private int _left = 0, _top = 0;
   protected TreeMap<Position, Tile> _tiles = new TreeMap<Position, Tile>();
   private Stack<Tile> _removedTiles = new Stack<Tile>();
   private LinkedHashSet<Position> _freePositions = new LinkedHashSet<Position>();

   private long _startTime, _gameTime;
   private Random _random = new Random();
   
   public Game(Deck deck, Layout layout)
   {
      _deck = deck;
      _layout = layout;
   }
   
   private boolean _layoutChanged = false;
   public boolean layoutChanged() { return _layoutChanged; }
   public void setLayout(Layout layout)
   {
      if (_layout == layout) return;
      _layout = layout;
      _layoutChanged = true;
   }
   
   public void initialize(int viewWidth, int viewHeight)
   {
//      Log.d("Eitan", "Initializing Game");
      _deck.useLargeBitmaps(_layout.usesLargeImages());
      _left = (viewWidth - _layout.layoutWidth()) / 2;
      _top = (viewHeight - _layout.layoutHeight()) / 2;
      _layout.recomputePositionBounds(_left, _top);
   }

   public void substituteTiles()
   {
      _deck.substituteTiles(_tiles);
      _deck.substituteTiles(_removedTiles);
   }
   
   public void matched(Pair<Tile> pair, View view)
   {
      matched(pair, true, view);
   }
   public void matched(Pair<Tile> pair, boolean invalidate, View view)
   {
      removeTile(pair.first(), invalidate, view);
      removeTile(pair.second(), invalidate, view);

      if (cleared())
      {
         _removedTiles.clear();  // prevent undo after game's done.
      }
   }
   private void removeTile(Tile tile, boolean invalidate, View view)
   {
//      Log.d("Eitan", String.format("Removing Tile %s", tile));
      _removedTiles.push(tile);
      Position position = tile.position();
      _tiles.remove(position);

      updateFreePositionsAfter(tile, tile.position(), true, invalidate, view);
   }
   
   private void updateFreePositionsAfter(Tile tile, Position p, boolean removal, boolean invalidate, View view)
   {
      if (removal) _freePositions.remove(p); else _freePositions.add(p);

      HashSet<Position> positionsToCheck = new HashSet<Position>();

      Position leftTileP = p.onLeftTile();
      positionsToCheck.add(leftTileP);
      positionsToCheck.add(leftTileP.onBottom());
      positionsToCheck.add(leftTileP.onTop());

      Position rightTileP = p.onRightTile();
      positionsToCheck.add(rightTileP);
      positionsToCheck.add(rightTileP.onBottom());
      positionsToCheck.add(rightTileP.onTop());

      Position pBelow = p.below();
      Position pBelowLeft = pBelow.onLeft();
      Position pBelowRight = pBelow.onRight();
      positionsToCheck.add(pBelow);
      positionsToCheck.add(pBelowLeft);
      positionsToCheck.add(pBelowRight);

      positionsToCheck.add(pBelow.onBottom());
      positionsToCheck.add(pBelowLeft.onBottom());
      positionsToCheck.add(pBelowRight.onBottom());

      positionsToCheck.add(pBelow.onTop());
      positionsToCheck.add(pBelowLeft.onTop());
      positionsToCheck.add(pBelowRight.onTop());

      for (Position p2check : positionsToCheck)
      {
         Tile t = tileAt(p2check);
         if (t == null) continue;
         if (removal)
         {
            if (isFree(t))
            {
               _freePositions.add(p2check);
               if (invalidate) t.invalidate(view);
            }
         }
         else
         {
            if (!isFree(t))
            {
               _freePositions.remove(p2check);
               if (invalidate) t.invalidate(view);
            }
         }
      }
   }

   public Pair<Tile> undo(View view)
   {
      if (_removedTiles.isEmpty()) return null;
      return new Pair<Tile>(addBackTile(view), addBackTile(view));
   }
   private Tile addBackTile(View view)
   {
      Tile t = _removedTiles.pop();
      placeTile(t.position(), t);
      updateFreePositionsAfter(t, t.position(), false, true, view);
      return t;
   }
   
   public boolean cleared() { return _tiles.isEmpty(); }
   public boolean inMidGame()
   {
      return !( cleared() || (_tiles.size() == _layout.layoutSize()) );
   }
   
   public boolean stuck() { return _freePositions.size() == 1; }
   public boolean noMoreMoves()
   {
      HashSet<Position> remainder = new HashSet<Position>();
      remainder.addAll(_freePositions);
      for (Position p : _freePositions)
      {
         remainder.remove(p);
         for (Position other : remainder)
         {
            if (tileAt(p).matches(tileAt(other)))
            {
               return false;
            }
         }
      }
      return true;
   }
   
   private void clear()
   {
      _tiles.clear();
      _removedTiles.clear();
   }
   public void reset(MaejongView view, boolean clear)
   {
      if (clear || _layoutChanged)
      {
         clear();
      }
      
      if (inMidGame())
      {
         view.toast(view.getContext().getString(R.string.continuing_prevgame), Toast.LENGTH_SHORT);
         view.animateLayout(_tiles.values());
//         restoreStartTime();
      }
      else
      {
         placeTiles();
         view.animateLayout(_tiles.values());
//         recordGameStart();
      }
      _layoutChanged = false;
   }

   void recordGameStart()
   {
//      Log.d("Eitan", "Recording game start");
      _startTime = System.currentTimeMillis();
   }

   void recordGameStop()
   {
      _gameTime = System.currentTimeMillis() - _startTime;
//      Log.d("Eitan", "Stopped:  Game time recorded as "+_gameTime);
   }

   String currentGameTime()
   {
      int seconds = (int) ((System.currentTimeMillis() - _startTime) / 1000);
      return TimeUtil.formatElapsedTimeForTimer(seconds);
   }

   void restoreStartTime() { _startTime = System.currentTimeMillis() - _gameTime; }
   
   int gameTimeSeconds() { return (int) (_gameTime/1000); }
   String gameTime()
   {
      return TimeUtil.formatElapsedTime(_gameTime);
   }
   
   protected void placeTile(Position position, Tile tile)
   {
      tile.position(position);
      _tiles.put(position, tile);
   }

   public void shuffleTiles(MaejongView view)
   {
      placeTiles();
      view.animateLayout(_tiles.values(), true);
   }
   public void placeTiles()
   {
      HashSet<Position> positions;
      ArrayList<Tile> tiles;
      if (_tiles.isEmpty())
      {
         tiles = new ArrayList<Tile>(_deck.tiles());
         positions = new HashSet<Position>(_layout.positions());
      }
      else
      {
         tiles = new ArrayList<Tile>(_tiles.values());
         positions = new HashSet<Position>(_tiles.keySet());
      }
      Collections.sort(tiles);
      
      Stack<Tile> savedRemovedTiles = new Stack<Tile>();
      savedRemovedTiles.addAll(_removedTiles);

      HashSet<Pair<Position>> pairs = selectPairs(positions);
      
      _tiles.clear();
      Iterator<Tile> itr = tiles.iterator();
      for (Pair<Position> pair : pairs)
      {
         placeTile(pair.first(), itr.next());
         placeTile(pair.second(), itr.next());
      }
      calculateFreePositions();
      
      _removedTiles.clear();
      _removedTiles.addAll(savedRemovedTiles);
   }
   private HashSet<Pair<Position>> selectPairs(HashSet<Position> positions)
   {
      HashSet<Pair<Position>> pairs = new HashSet<Pair<Position>>();
      do {
         pairs.clear();
         layoutWithSameTile(positions);
         
         while (!cleared())
         {
            Pair<Position> pair = pickTwoRandomFreePositions();
//            Log.d("Eitan", String.format("%d tiles", _tiles.size()));
            matched(new Pair<Tile>(_tiles.get(pair.first()), _tiles.get(pair.second())), false, null);
//            Log.d("Eitan", String.format("after match:  %d tiles remaining", _tiles.size()));
            if (stuck())
            {
//               Log.d("Eitan", "Detected stuck while generating deck;  breaking..");
               break;
            }
            pairs.add(pair);
         }
      } while (stuck());

//      Log.d("Eitan", String.format("Generated %d pairs", pairs.size()));
      return pairs;
   }
   
   private void layoutWithSameTile(HashSet<Position> positions)
   {
//      Log.d("Eitan", "Laying out same-tile deck for pairs generation purposes..");
      _tiles.clear();
      Iterator<Tile> itr = _deck.sameFaceTiles().iterator();
      for (Position p : positions)
      {
         placeTile(p, itr.next());
      }
      calculateFreePositions();
   }
   
   private Pair<Position> pickTwoRandomFreePositions()
   {
      ArrayList<Position> tempList = new ArrayList<Position>(_freePositions);
      int size = tempList.size();
      int n1 = _random.nextInt(size);
      Position p1 = tempList.remove(n1);
      int n2 = _random.nextInt(size-1);
      Position p2 = tempList.remove(n2);
      return new Pair<Position>(_layout.pos(p1), _layout.pos(p2));
   }
   
   private void calculateFreePositions()
   {
      _freePositions.clear();
      for (Tile t : _tiles.values())
      {
         if (t == null) continue;
         if (isFree(t))
         {
            _freePositions.add(t.position());
         }
      }
   }
   
   boolean isPositionFree(Position p) { return _freePositions.contains(p); }
   
   public void draw(Context context, Canvas canvas, HashSet<Tile> selectedTiles, Tile badTile)
   {
      for (Tile t : _tiles.values())
      {
         t.draw(context, canvas, selectedTiles.contains(t), t == badTile, _freePositions.contains(t.position()));
      }
   }

   
   // ============================================================

   public Tile tileAtPoint(int x, int y)
   {
      x -= _left;  y -= _top;
      
      int layer = _layout.numLayers() - 1;
      x += layer * Perspective_run;
      y += layer * Perspective_rise;

      Tile tile = null;
      Position p = null;
      double gridWidth = Tile.Width / 2;
      double gridHeight = Tile.Height / 2;
      
      do
      {
         int col = (int) (x / gridWidth);
         int row = (int) (y / gridHeight);
         p = _layout.pos(row, col, layer);
         if (p == null) p = _layout.pos(row, col-1, layer);
         if (p == null) p = _layout.pos(row-1, col, layer);
         if (p == null) p = _layout.pos(row-1, col-1, layer);
         if (p != null)
         {
            tile = tileAt(p);
         }
         
         layer -= 1;
         x -= Perspective_run;
         y -= Perspective_rise;
      }
      while ((tile == null) && (layer >= 0));
      
      return tile;
   }
   private Tile tileAt(Position position)
   {
      return _tiles.get(position);
   }
   
   public boolean isFree(Tile tile)
   {
//      Log.d("Eitan", String.format("Checking if %s is free", tile));
      return isOpenAbove(tile.position().above()) &&
             ( isOpenOnSide(tile.position().onLeftTile()) || isOpenOnSide(tile.position().onRightTile()) );
   }
   private boolean isOpenAbove(Position position)
   {
      Position pBottom = position.onBottom();
      Position pTop = position.onTop();
      return tileAt(position) == null  &&
             tileAt(pBottom) == null  &&
             tileAt(pTop) == null  &&
             tileAt(position.onRight()) == null && 
             tileAt(position.onLeft()) == null && 
             tileAt(pBottom.onRight()) == null &&
             tileAt(pBottom.onLeft()) == null &&
             tileAt(pTop.onRight()) == null &&
             tileAt(pTop.onLeft()) == null;
   }
   private boolean isOpenOnSide(Position position)
   {
//      Log.d("Eitan", String.format("Is position %s free?", position));
      return tileAt(position) == null && 
             tileAt(position.onBottom()) == null &&
             tileAt(position.onTop()) == null;
   }
   
   
   
   
   // code relating to saving/restoring state of game..
   // ============================================================
   
   public void saveState(SharedPreferences prefs)
   {
      try
      {
         _saveState(prefs);
      }
      catch (JSONException ex)
      {
         ex.printStackTrace();
      }
   }
   private void _saveState(SharedPreferences prefs) throws JSONException
   {
      Editor editor = prefs.edit();
//      Log.d("Eitan", String.format("Tiles size is %d and layout size is %d", _tiles.size(), layoutSize()));
      if (!inMidGame())
      {
//         Log.d("Eitan", "Clearing saved state");
         editor.remove("gameTime").remove("tiles").remove("removedTiles").commit();
         return;
      }
      
      editor.putLong("gameTime", _gameTime);
      
      saveTiles(editor, "tiles", _tiles.values());
      saveTilesOrdered(editor, "removedTiles", _removedTiles);
      editor.commit();
   }
   private void saveTiles(Editor editor, String key, Collection<Tile> tiles) throws JSONException
   {
      JSONObject tilesJsobj = new JSONObject();
      for (Tile t : tiles)
      {
         tilesJsobj.put(t.position().toJSON(), _deck.tiles().indexOf(t));
      }
      editor.putString(key, tilesJsobj.toString());
   }
   private void saveTilesOrdered(Editor editor, String key, Collection<Tile> tiles) throws JSONException
   {
      JSONArray tilesJsra = new JSONArray();
      for (Tile t : tiles)
      {
         JSONObject tilesJsobj = new JSONObject();
         tilesJsobj.put("p", t.position().toJSON());
         tilesJsobj.put("t", _deck.tiles().indexOf(t));
         tilesJsra.put(tilesJsobj);
      }
      editor.putString(key, tilesJsra.toString());
   }
   
   public boolean restoreState(SharedPreferences prefs)
   {
      try
      {
         return _restoreState(prefs);
      }
      catch (JSONException ex)
      {
         ex.printStackTrace();
         clear();
      }
      return false;
   }
   
   private boolean _restoreState(SharedPreferences prefs) throws JSONException
   {
      String tilesString = prefs.getString("tiles", "");
      if (tilesString.length() == 0) return false;
      
      _gameTime = prefs.getLong("gameTime", 0);
      
      JSONObject tilesJso = new JSONObject(tilesString);
      JSONArray names = tilesJso.names();
      for (int i=0; i<names.length(); i++)
      {
         String positionJson = names.getString(i);
         Position p = _layout.pos(Position.fromJSON(positionJson));
         if (p == null)
         {
            // the state of the saved data is inconsistent
            _tiles.clear();
            return false;
         }
         int tileIndex = tilesJso.getInt(positionJson);
         Tile t = _deck.tiles().get(tileIndex);
         placeTile(p, t);
      }
      calculateFreePositions();
      
      tilesString = prefs.getString("removedTiles", "");
      JSONArray tilesJsra = new JSONArray(tilesString);
      for (int i=0; i<tilesJsra.length(); i++)
      {
         JSONObject jso = tilesJsra.getJSONObject(i);
         String positionJson = jso.getString("p");
         int tileIndex = jso.getInt("t");
         Position p = _layout.pos(Position.fromJSON(positionJson));
         Tile t = _deck.tiles().get(tileIndex);
         t.position(p);
         _removedTiles.push(t);
      }

      return true;
   }
   
   
}
