package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;
import com.eitan.maejong.Position;

/*
 * A simple test rectangular layout with two layers.
 */
public class Classic extends Layout
{
   public String name() { return "classic"; }

   protected int numCols() { return 26; }
   protected int numRows() { return 12; }
   protected int numLayers() { return 4; }
   protected int layoutSize() { return 84; }

   protected boolean usesLargeImages() { return false; }

   protected void build()
   {
      int layer = 0;  // lay out first layer
      addRow(layer, 0, 4, 9);
      addRow(layer, 2, 6, 7);
      addRow(layer, 4, 2, 11);
      addRow(layer, 6, 2, 11);
      addRow(layer, 8, 6, 7);
      addRow(layer, 10, 4, 9);
      
      addPosition(new Position(5, 0, layer));
      addPosition(new Position(5, 24, layer));

      for (int row = 1; row < 5; row++)
      {
         addRow(1, row*2, 8, 5);
      }
      
      for (int row = 2; row < 4; row++)
      {
         addRow(2, row*2, 10, 3);
      }
      
      addPosition(new Position(5, 11, 3));
      addPosition(new Position(5, 13, 3));
   }

}
