package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class Simple extends Layout
{
   public String name() { return "simple"; }
   
   protected int numCols() { return 12; }
   protected int numRows() { return 8; }
   protected int numLayers() { return 2; }
   protected int layoutSize() { return 32; }

   protected boolean usesLargeImages() { return true; }
   
   protected void build()
   {
      for (int row = 0; row < 4; row++)
      {
         addRow(0, row*2, 0, 6);
      }
      
      for (int row = 1; row < 3; row++)
      {
         addRow(1, row*2, 2, 4);
      }
   }

}
