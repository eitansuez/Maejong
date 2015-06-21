package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class TieFighter extends Layout
{
   public String name() { return "tiefighter"; }

   protected int numCols() { return 22; }
   protected int numRows() { return 12; }
   protected int numLayers() { return 4; }
   protected int layoutSize() { return 84; }

   protected boolean usesLargeImages() { return false; }
   
   protected void build()
   {
      addRow(0, 5, 0, 3); addRow(1, 5, 0, 3);
      addRow(0, 5, 16, 3); addRow(1, 5, 16, 3);
      addRow(2, 5, 0, 1);
      addRow(2, 5, 20, 1);
      
      addRow(0, 0, 4, 1); addRow(1, 0, 4, 1);
      addRow(0, 1, 2, 1); addRow(1, 1, 2, 1);
      addRow(0, 3, 0, 1); addRow(1, 3, 0, 1);
      addRow(0, 7, 0, 1); addRow(1, 7, 0, 1);
      addRow(0, 9, 2, 1); addRow(1, 9, 2, 1);
      addRow(0, 10, 4, 1); addRow(1, 10, 4, 1);

      addRow(0, 0, 16, 1); addRow(1, 0, 16, 1);
      addRow(0, 1, 18, 1); addRow(1, 1, 18, 1);
      addRow(0, 3, 20, 1); addRow(1, 3, 20, 1);
      addRow(0, 7, 20, 1); addRow(1, 7, 20, 1);
      addRow(0, 9, 18, 1); addRow(1, 9, 18, 1);
      addRow(0, 10, 16, 1); addRow(1, 10, 16, 1);
      
      addRow(0, 2, 8, 3);  addRow(1, 2, 8, 3);
      addRow(0, 4, 6, 5); addRow(1, 4, 6, 5);
      addRow(0, 6, 6, 5); addRow(1, 6, 6, 5);
      addRow(0, 8, 8, 3);  addRow(1, 8, 8, 3);
      
      addRow(0, 0, 10, 1);  addRow(1, 0, 10, 1);
      addRow(0, 10, 10, 1);  addRow(1, 10, 10, 1);
      
      addRow(2, 2, 10, 1);
      addRow(2, 8, 10, 1);
      addRow(2, 4, 8, 3);
      addRow(2, 6, 8, 3);
      
      addRow(3, 4, 10, 1);
      addRow(3, 6, 10, 1);
   }

}
