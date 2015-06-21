package com.eitan.maejong.layouts;

import com.eitan.maejong.Layout;
import com.eitan.maejong.Position;

/*
 * A simple test rectangular layout with two layers.
 */
public class Checkers extends Layout
{
   public String name() { return "checkers"; }

   protected int numCols() { return 18; }
   protected int numRows() { return 10; }
   protected int numLayers() { return 2; }
   protected int layoutSize() { return 68; }

   protected boolean usesLargeImages() { return false; }
   
   protected void build()
   {
      for (int r = 0; r<10; r+=2)
      {
         addRow(0, r, 0, 9);
      }
      for (int r = 0; r<10; r+=4)
      {
         for (int c = 0; c < 18; c+=4)
         {
            addPosition(new Position(r, c, 1));
         }
      }
      for (int r=2; r<10; r+=4)
      {
         for (int c = 2; c< 18; c+=4)
         {
            addPosition(new Position(r, c, 1));
         }
      }
   }

}
