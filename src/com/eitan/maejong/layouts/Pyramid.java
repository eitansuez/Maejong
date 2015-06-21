package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class Pyramid extends Layout
{
   public String name() { return "pyramid"; }

   protected int numCols() { return 16; }
   protected int numRows() { return 12; }
   protected int numLayers() { return 4; }
   protected int layoutSize() { return 84; }

   protected boolean usesLargeImages() { return false; }

   protected void build()
   {
      for (int l=0; l<3; l++)
      {
         for (int r = 0; r< 12; r+=2)
         {
            int r0 = r/2;
            addRow(l, r, 5 - r0 + l, 3+r0-l);
         }
      }
      
      addRow(3, 4, 7, 1);
      addRow(3, 6, 6, 2);
   }

}
