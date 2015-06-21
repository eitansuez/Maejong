package com.eitan.maejong.tile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.eitan.maejong.MaejongTheme;
import com.eitan.maejong.Tile;
import com.eitan.maejong.TileType;

public enum PokemonTileType implements TileType
{
   Pikachu, Blastoise, Squirtle, Charmander, Charizard, Psyduck, Miowth, Bulbasaur,
   Parasect, Machamp, Butterfree, Arcanine, Beedrill, Jigglypuff, Nidoking, Oddish,
   Pidgeoto, Poliwhirl, Sandslash, Vulpix, Zubat;
   
   private Bitmap _bitmap, _largeBitmap;
   private boolean _usingLargeBitmap = false;
   private MaejongTheme _theme;
   
   public MaejongTheme theme() { return _theme; }
   public void setTheme(MaejongTheme theme) { _theme = theme; }

   public void addBitmap(Bitmap bitmap, boolean large)
   {
      if (large)
      {
         _largeBitmap = bitmap;
      }
      else
      {
         _bitmap = bitmap;
      }
   }

   public boolean hasManyImages() { return false; }
   public Bitmap nextImage() { return targetBitmap(); }

   private Bitmap targetBitmap() { return (_usingLargeBitmap) ? _largeBitmap : _bitmap; }
   
   public void useLargeBitmaps(boolean value) { _usingLargeBitmap = value; }

   public void draw(Canvas canvas, int x, int y, Paint paint, Tile tile)
   {
      Bitmap bitmap = tile.bitmap();
      if (bitmap == null) bitmap = targetBitmap();

      if (_usingLargeBitmap)
      {
         canvas.drawBitmap(_theme.tileImageLg(), x, y, paint);
         canvas.drawBitmap(bitmap, x, y, paint);
      }
      else
      {
         canvas.drawBitmap(_theme.tileImage(), x, y, paint);
         canvas.drawBitmap(bitmap, x+2, y+4, paint);
      }
   }

}
