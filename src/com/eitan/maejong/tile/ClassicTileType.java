package com.eitan.maejong.tile;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.eitan.maejong.MaejongTheme;
import com.eitan.maejong.Tile;
import com.eitan.maejong.TileType;

/*
 * Note:  problem refactoring code between tile types:  java doesn't support abstract enums.
 */
public enum ClassicTileType implements TileType
{
   Dragon1, Dragon2, Dragon3, 
   Season, Flower,
   Wind1, Wind2, Wind3, Wind4,
   Char1, Char2, Char3, Char4, Char5, Char6, Char7, Char8, Char9,
   Ball1, Ball2, Ball3; // need only 21 for largest layout (DefaultLayout), so:
                        // , Ball4, Ball5, Ball6, Ball7, Ball8, Ball9,
   // Bamboo1, Bamboo2, Bamboo3, Bamboo4, Bamboo5, Bamboo6, Bamboo7, Bamboo8, Bamboo9;
   
   private MaejongTheme _theme;
   public MaejongTheme theme() { return _theme; }
   public void setTheme(MaejongTheme theme) { _theme = theme; }

   private ArrayList<Bitmap> _bitmaps = new ArrayList<Bitmap>(4);  // which is better:  default list size of 1 or 4?

   public void addBitmap(Bitmap bitmap, boolean large)
   {
      if (large)
      {
         _largeBitmaps.add(bitmap);
      }
      else
      {
         _bitmaps.add(bitmap);
      }
   }

   private ArrayList<Bitmap> _largeBitmaps = new ArrayList<Bitmap>(4);

   private boolean _usingLargeBitmaps = false;
   public void useLargeBitmaps(boolean value) { _usingLargeBitmaps = value; }
   private List<Bitmap> targetBitmaps() { return (_usingLargeBitmaps) ? _largeBitmaps : _bitmaps; }
   
   public boolean hasManyImages() { return targetBitmaps().size() > 1; }
   public Bitmap nextImage() { return targetBitmaps().get(nextImgIndex()); }
   
   private int _nextImgIndex = 0;
   private int nextImgIndex()
   {
      int thisIndex = _nextImgIndex;
      _nextImgIndex += 1;
      _nextImgIndex %= targetBitmaps().size();
      return thisIndex;
   }
   /*
    * Called only for tile types with a single image
    */
   public void draw(Canvas canvas, int x, int y, Paint paint, Tile tile)
   {
      Bitmap bitmap = tile.bitmap();
      if (bitmap == null) bitmap = targetBitmaps().get(0);

      if (_usingLargeBitmaps)
      {
         canvas.drawBitmap(bitmap, x, y, paint);
      }
      else
      {
         canvas.drawBitmap(_theme.tileImage(), x, y, paint);
         canvas.drawBitmap(bitmap, x+2, y+5, paint);
      }
   }

}
