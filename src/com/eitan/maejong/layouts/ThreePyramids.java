package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class ThreePyramids extends Layout
{
   public String name() { return "threepyramids"; }

   protected int numCols() { return 26; }
   protected int numRows() { return 10; }
   protected int numLayers() { return 4; }
   protected int layoutSize() { return 64; }

   protected boolean usesLargeImages() { return false; }

   protected void build()
   {
      for (int r = 2; r<8; r+=2)
      {
         addRow(0, r, 0, 4);  addRow(0, r, 18, 4);
      }
      for (int r = 3; r< 7; r+=2)
      {
         addRow(1, r, 1, 3);  addRow(1, r, 19, 3);
      }
      addRow(2, 4, 2, 2);  addRow(2, 4, 20, 2);
      
      addRow(0, 0, 3, 1);  addRow(0, 0, 12, 1);  addRow(0, 0, 21, 1);
      addRow(0, 8, 3, 1);  addRow(0, 8, 12, 1);  addRow(0, 8, 21, 1);
      
      addRow(0, 2, 10, 3);
      addRow(0, 4, 8, 5);
      addRow(0, 6, 10, 3);
      
      addRow(1, 2, 12, 1);  addRow(1, 4, 10, 3);
      addRow(1, 6, 12, 1);
      addRow(2, 4, 12, 1);  addRow(3, 4, 12, 1);
   }

}
