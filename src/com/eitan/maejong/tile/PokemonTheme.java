package com.eitan.maejong.tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.eitan.maejong.MaejongTheme;
import com.eitan.maejong.R;
import com.eitan.maejong.TileType;

public class PokemonTheme extends MaejongTheme
{
   protected static final int Imgwidth = 37, Imgheight = 51;
   protected static final int Imgwidth_lg = 63, Imgheight_lg = 68;

   public PokemonTheme(Context context)
   {
      super(context);
      makeTileTypes();
   }
   
   public String name() { return "pokemon"; }

   protected void makeTileTypes()
   {
      int i = 0;
      for (PokemonTileType type : PokemonTileType.values())
      {
         type.setTheme(this);
         
         String name = type.name().toLowerCase();
         String idname = String.format("com.eitan.maejong:drawable/%s", name);
//         Log.d("Eitan", "looking for "+idname);
         int resourceId = _resources.getIdentifier(idname, null, null);
         Bitmap bm = BitmapFactory.decodeResource(_resources, resourceId);
         type.addBitmap(bm, false);
         
         if (i<8)
         {
            idname = String.format("com.eitan.maejong:drawable/%s_lg", name);
            resourceId = _resources.getIdentifier(idname, null, null);
            bm = BitmapFactory.decodeResource(_resources, resourceId);
            type.addBitmap(bm, true);
         }
         
         i += 1;
      }
   }

   public TileType[] tileTypes() { return PokemonTileType.values(); }

   public int imgHeight() { return (_usingLargeBitmaps) ? Imgheight_lg : Imgheight; }
   public int imgWidth() { return (_usingLargeBitmaps) ? Imgwidth_lg : Imgwidth; }
   
   public int backgroundResourceId() { return R.drawable.pikachu_background; }
   public int winAnimationResourceId() { return R.anim.win; }
}
