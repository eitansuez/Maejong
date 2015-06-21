package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class Boat extends Layout
{
   public String name() { return "boat"; }

   protected int numCols() { return 26; }
   protected int numRows() { return 12; }
   protected int numLayers() { return 4; }
   protected int layoutSize() { return 84; }

   protected boolean usesLargeImages() { return false; }

   protected void build()
   {
      addRow(0, 10, 5, 8);
      addRow(1, 10, 7, 6);
      addRow(0, 8, 0, 13);
      addRow(1, 8, 2, 11);
      
      addColumn(0, 0, 12, 4);
      addColumn(1, 0, 12, 4);
      
      addRow(0, 0, 6, 2);  addRow(0, 0, 16, 2);
      addRow(0, 2, 4, 3);  addRow(0, 2, 16, 3);
      addRow(0, 4, 2, 4);  addRow(0, 4, 16, 4);
      
      addRow(1, 0, 8, 1);  addRow(1, 0, 16, 1);
      addRow(1, 2, 6, 2);  addRow(1, 2, 16, 2);
      addRow(1, 4, 4, 3);  addRow(1, 4, 16, 3);
      
      addRow(2, 2, 8, 1);  addRow(2, 2, 16, 1);
      addRow(2, 4, 6, 2);  addRow(2, 4, 16, 2);
      
      addRow(3, 4, 8, 1);  addRow(3, 4, 16, 1);
   }

}
