package com.eitan.maejong;

import java.util.HashSet;
import java.util.Iterator;

public class SelectionMgr
{
   private SelectionState _currentState, _state0, _state1, _state2;
   private HashSet<Tile> _selectedTiles = new HashSet<Tile>();
   private Tile _badTile;
   private Pair<Tile> _matchedPair;

   private Maejong _maejong;
   private MaejongView _view;
   
   public SelectionMgr(Maejong context, MaejongView view)
   {
      _maejong = context;
      _view = view;
      
      _matchedPair = new Pair<Tile>(null, null);
      
      _state0 = new SelectionState() {
         public void entry()
         {
            _selectedTiles.clear();
            if (_maejong.game().cleared())
            {
               _maejong.won();
            }
            else if (_maejong.game().noMoreMoves())
            {
               if (_maejong.game().stuck())
               {
                  _maejong.lost();
               }
               else
               {
                  _maejong.lostButCanContinue();
               }
            }
         }
         public void select(Tile tile)
         {
            if (_maejong.game().isFree(tile))
            {
               _selectedTiles.add(tile);
               tile.invalidate(_view);
               _maejong.soundMgr().select();
               transition(_state1);
            }
            else
            {
               flashBadTile(tile);
            }
         }
      };
      _state1 = new SelectionState() {
         public void entry() { }
         public void select(Tile tile)
         {
            if (_selectedTiles.contains(tile))
            {
               tile.invalidate(_view);
               transition(_state0);
            }
            else if (_maejong.game().isFree(tile))
            {
               Tile selectedTile = _selectedTiles.iterator().next();
               if (selectedTile.matches(tile))
               {
                  _selectedTiles.add(tile);
                  tile.invalidate(_view);
                  transition(_state2);
               }
               else
               {
                  _selectedTiles.clear();
                  _selectedTiles.add(tile);
                  selectedTile.invalidate(_view);
                  tile.invalidate(_view);
                  _maejong.soundMgr().select();
               }
            }
            else
            {
               flashBadTile(tile);
            }
         }
      };
      _state2 = new SelectionState() {
         public void entry() {
            _view.postDelayed(new Runnable()
            {
               public void run()
               {
                  Iterator<Tile> itr = _selectedTiles.iterator();
                  _matchedPair.set(itr.next(), itr.next());
                  _maejong.soundMgr().match();
                  _maejong.game().matched(_matchedPair, _view);
                  _matchedPair.invalidate(_view);
                  transition(_state0);
               }
            }, 100);
         }
         public void select(Tile tile) { /* noop (never get here) */ }
      };
   }

   public boolean select(int x, int y)
   {
//      Log.d("Eitan", "select()::Entry");
      Tile tile = _maejong.game().tileAtPoint(x, y);
//      Log.d("Eitan", String.format("select()::got tile at point %s", tile));
      boolean found = (tile != null);
      if (found)
      {
         _currentState.select(tile);
//         Log.d("Eitan", "select()::returned from currentState.select(tile) call");
      }
      return found;
   }
   
   public void initTransition()
   {
      transition(_state0);
   }

   private void transition(SelectionState state)
   {
      _currentState = state;
      _currentState.entry();
   }

   private void flashBadTile(final Tile tile)
   {
      _maejong.soundMgr().badSelect();
      _badTile = tile;
      tile.invalidate(_view);
      _view.postDelayed(new Runnable()
      {
         public void run()
         {
            tile.invalidate(_view);
            _badTile = null;
         }
      }, 300);
   }
   
   public HashSet<Tile> selectedTiles() { return _selectedTiles; }
   public Tile badTile() { return _badTile; }
   
}
