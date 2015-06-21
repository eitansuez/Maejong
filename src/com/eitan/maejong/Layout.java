package com.eitan.maejong;

import java.util.Collection;
import java.util.TreeMap;

public abstract class Layout
{
   private TreeMap<Position, Position> _positions = new TreeMap<Position, Position>();
   
   public Layout()
   {
      build();
      computePositionBounds();
   }
   protected int layoutWidth() { return layoutWidth(Tile.Width); }
   protected int layoutHeight() { return layoutHeight(Tile.Height); }
   protected int layoutWidth(double tileWidth) { return (int) (tileWidth/2 * numCols()); }
   protected int layoutHeight(double tileHeight) { return (int) (tileHeight/2 * numRows()); }

   public void computePositionBounds()
   {
      int left = (480 - layoutWidth()) / 2;
      int top = (320 - layoutHeight()) / 2;
      recomputePositionBounds(left, top);
   }
   public void recomputePositionBounds(int left, int top)
   {
      for (Position p : _positions.values())
      {
         p.calculateBounds(left, top);
      }
   }

   public abstract String name();
   
   private String _caption = null;
   public String caption() { return _caption; }
   public void setCaption(String caption) { _caption = caption; }
   
   protected abstract void build();
   protected abstract int numRows();
   protected abstract int numCols();
   protected abstract int numLayers();
   protected abstract boolean usesLargeImages();
   protected abstract int layoutSize();  // number of tiles.  perhaps should be renamed.
   
   
   protected void addRow(int layer, int row, int startCol, int numTiles)
   {
      int endCol = startCol + (numTiles * 2);
      for (int col=startCol; col<endCol; col+= 2)
      {
         addPosition(new Position(row, col, layer));
      }
   }
   protected void addColumn(int layer, int startRow, int col, int numTiles)
   {
      int endRow = startRow + (numTiles * 2);
      for (int row=startRow; row<endRow; row+= 2)
      {
         addPosition(new Position(row, col, layer));
      }
   }
   protected void addPosition(Position position)
   {
      _positions.put(position, position);
   }
   
   public Collection<Position> positions() { return _positions.values(); }
   
   public Position pos(Position p) { return _positions.get(p); }
   public Position pos(int row, int col, int layer)
   {
      return _positions.get(new Position(row, col, layer));
   }
   
   public String toString() { return caption(); }

}
