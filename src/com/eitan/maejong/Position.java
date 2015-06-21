package com.eitan.maejong;

import org.json.JSONArray;
import org.json.JSONException;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class Position implements Comparable<Position>
{
   private int row, col, layer;
   private Rect _bounds;
   private RectF _boundsF;
   
   public Position(int r, int c, int l) { row = r; col = c; layer = l; }
   
   public int row() { return row; }
   public int col() { return col; }
   public int layer() { return layer; }
   
   public Position onLeft() { return new Position(row, col-1, layer); }
   public Position onRight() { return new Position(row, col+1, layer); }
   public Position onLeftTile() { return new Position(row, col-2, layer); }
   public Position onRightTile() { return new Position(row, col+2, layer); }

   public Position onTop() { return new Position(row-1, col, layer); }
   public Position onBottom() { return new Position(row+1, col, layer); }
   public Position onTopTile() { return new Position(row-2, col, layer); }
   public Position onBottomTile() { return new Position(row+2, col, layer); }
   
   public Position above() { return new Position(row, col, layer+1); }
   public Position below() { return new Position(row, col, layer-1); }
   
   public Rect bounds() { return _bounds; }
   public RectF boundsF() { return _boundsF; }
   public void calculateBounds(int left, int top)
   {
      calculateBounds(left, top, Tile.Width, Tile.Height);
   }
   public void calculateBounds(int left, int top, double width, double height)
   {
      double gridWidth = width / 2;
      double gridHeight = height / 2;
      
      Point p = new Point(left + (int) (col * gridWidth), 
                          top  + (int) (row * gridHeight));
      int offsetx = layer * Game.Perspective_run;
      int offsety = layer * Game.Perspective_rise;
      int x = p.x - offsetx;
      int y = p.y - offsety;
      _bounds = new Rect(x, y, x + (int) width, y + (int) height);
      _boundsF = new RectF(_bounds.left+4, _bounds.top+3, _bounds.right-1, _bounds.bottom-2);
   }
   
   public String toString()
   {
      return String.format("(%d,%d,%d)", row, col, layer);
   }

   public boolean equals(Object o)
   {
      if (o == null) return false;
      if (!(o instanceof Position)) return false;
      Position other = (Position) o;
      return row == other.row() && col == other.col() && layer == other.layer();
   }
   
   public int hashCode()
   {
      return (row * 31 + col) * 13 + layer;
   }

   @Override
   public int compareTo(Position another)
   {
      int value = new Integer(layer).compareTo(another.layer());
      if (value == 0)
      {
         value = new Integer(col+row).compareTo(another.col()+another.row());
         if (value == 0)
         {
            value = new Integer(row).compareTo(another.row());
         }
      }
      return value;
   }
   
   public String toJSON()
   {
      return new JSONArray().put(row).put(col).put(layer).toString();
   }
   public static Position fromJSON(String json) throws JSONException
   {
      JSONArray ra = new JSONArray(json);
      return new Position(ra.getInt(0), ra.getInt(1), ra.getInt(2));
   }
   
}
