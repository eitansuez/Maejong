package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class Crown extends Layout
{
   public String name() { return "crown"; }

   protected int numCols() { return 16; }
   protected int numRows() { return 8; }
   protected int numLayers() { return 2; }
   protected int layoutSize() { return 40; }

   protected boolean usesLargeImages() { return false; }
   
   protected void build()
   {
      for (int l = 0; l < 2; l++)
      {
         addRow(l, 0, 0, 1);
         addRow(l, 0, 7, 1);
         addRow(l, 0, 14, 1);

         addRow(l, 2, 1, 2);
         addRow(l, 2, 6, 2);
         addRow(l, 2, 11, 2);

         addRow(l, 4, 2, 6);
         
         addRow(l, 6, 3, 5);
      }
   }

}
