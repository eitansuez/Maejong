package com.eitan.maejong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface TileType
{
   String name();
   int ordinal();
   
   MaejongTheme theme();
   
   boolean hasManyImages();
   Bitmap nextImage();

   void addBitmap(Bitmap bitmap, boolean large);
   
   void useLargeBitmaps(boolean value);

   /*
    * Called only for tile types with a single image
    */
   void draw(Canvas canvas, int x, int y, Paint paint, Tile tile);
}
