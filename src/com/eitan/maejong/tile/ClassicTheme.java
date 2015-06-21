package com.eitan.maejong.tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.eitan.general.StringUtil;
import com.eitan.maejong.Deck;
import com.eitan.maejong.MaejongTheme;
import com.eitan.maejong.R;
import com.eitan.maejong.TileType;

public class ClassicTheme extends MaejongTheme
{
   private static final int Imgwidth = 37, Imgheight = 51;
   private static final int Imgwidth_lg = 40, Imgheight_lg = 61;  // internal face:  36 x 57
   
   public ClassicTheme(Context context)
   {
      super(context);
      
      for (ClassicTileType type : ClassicTileType.values())
      {
         type.setTheme(this);
      }
      loadTileTypeImages(21, "", false);
      loadTileTypeImages(8, "_lg", true);
   }
   
   public String name() { return "classic"; }

   private void loadTileTypeImages(int numTiles, String suffix, boolean large)
   {
      int count = 0;
      for (ClassicTileType type : ClassicTileType.values())
      {
         if (count >= numTiles) break;
         
         String name = type.name().toLowerCase();
         if (StringUtil.endsWithDigit(name))
         {
            String idname = String.format("com.eitan.maejong:drawable/%s%s", name, suffix);
//            Log.d("Eitan", "looking for "+idname);
            int resourceId = _resources.getIdentifier(idname, null, null);
            Bitmap bm = BitmapFactory.decodeResource(_resources, resourceId);
            type.addBitmap(bm, large);
         }
         else
         {
            for (int i=1; i<=Deck.TilesPerType; i++)
            {
               String idname = String.format("com.eitan.maejong:drawable/%s%d%s", name, i, suffix);
               int resourceId = _resources.getIdentifier(idname, null, null);
               Bitmap bm = BitmapFactory.decodeResource(_resources, resourceId);
               type.addBitmap(bm, large);  // need new method: addLargeBitmap
            }
         }
         
         count += 1;
      }
   }

   public TileType[] tileTypes() { return ClassicTileType.values(); }

   public int imgHeight() { return (_usingLargeBitmaps) ? Imgheight_lg : Imgheight; }
   public int imgWidth() { return (_usingLargeBitmaps) ? Imgwidth_lg : Imgwidth; }

   public int backgroundResourceId() { return R.drawable.classic_background; }
   public int winAnimationResourceId() { return R.anim.win_classic; }
   
}
