package com.eitan.maejong;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public abstract class MaejongTheme
{
   protected Resources _resources;
   
   public MaejongTheme(Context context)
   {
      _resources = context.getResources();
      _tileImage = BitmapFactory.decodeResource(_resources, R.drawable.tile);
      _tileImageLg = BitmapFactory.decodeResource(_resources, R.drawable.tile_lg);
      _animation = AnimationUtils.loadAnimation(context, winAnimationResourceId());
   }
   
   
   private Animation _animation;
   public Animation winAnimation() { return _animation; }

   
   public abstract String name();
   
   private String _caption = null;
   public synchronized String caption()
   {
      if (_caption == null)
      {
         String idname = String.format("com.eitan.maejong:string/theme_%s", name());
         int id = _resources.getIdentifier(idname, null, null);
         _caption = _resources.getString(id);
      }
      return _caption;
   }
   
   public abstract TileType[] tileTypes();
   public abstract int imgWidth();
   public abstract int imgHeight();
   public abstract int backgroundResourceId();
   public abstract int winAnimationResourceId();

   
   protected Bitmap _tileImage, _tileImageLg;
   public Bitmap tileImage() { return _tileImage; }
   public Bitmap tileImageLg() { return _tileImageLg; }

   
   protected boolean _usingLargeBitmaps = false;
   public void useLargeBitmaps(boolean value)
   {
      _usingLargeBitmaps = value;
      TileType[] tileTypes = tileTypes();

      for (TileType tileType : tileTypes)
      {
         tileType.useLargeBitmaps(value);
      }

      Tile.Width = imgWidth();
      Tile.Height = imgHeight();
   }
   
}
