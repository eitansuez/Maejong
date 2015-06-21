package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class Isengard extends Layout
{
   public String name() { return "isengard"; }

   protected int numCols() { return 18; }
   protected int numRows() { return 12; }
   protected int numLayers() { return 5; }
   protected int layoutSize() { return 72; }

   protected boolean usesLargeImages() { return false; }

   protected void build()
   {
      for (int r = 0; r<12; r+=2)
      {
         addRow(0, r, 0, 9);
      }
      addRow(1, 2, 5, 4);
      addRow(1, 5, 3, 1); addRow(1, 5, 13, 1);
      addColumn(1, 4, 5, 2);  addColumn(1, 4, 11, 2);
      addRow(1, 8, 5, 4);

      for (int l=1; l<5; l++)
      {
         addRow(l, 5, 8, 1);
      }
   }

}
