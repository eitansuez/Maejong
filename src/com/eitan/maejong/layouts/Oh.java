package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;

/*
 * A simple test rectangular layout with two layers.
 */
public class Oh extends Layout
{
   public String name() { return "oh"; }
   
   protected int numCols() { return 10; }
   protected int numRows() { return 10; }
   protected int numLayers() { return 3; }
   protected int layoutSize() { return 36; }

   protected boolean usesLargeImages() { return false; }
   
   protected void build()
   {
      addRow(0, 0, 2, 3);
      addRow(1, 0, 1, 4);
      
      addRow(0, 4, 4, 1);
      addRow(1, 4, 4, 1);
      
      addRow(0, 8, 2, 3);
      addRow(1, 8, 1, 4);
      
      addColumn(1, 2, 0, 3);
      addColumn(0, 1, 0, 4);

      addColumn(1, 2, 8, 3);
      addColumn(0, 1, 8, 4);
      
      addRow(2, 0, 4, 1);
      addRow(2, 8, 4, 1);
      
      addColumn(2, 3, 0, 2);
      addColumn(2, 3, 8, 2);
   }

}
