package com.eitan.maejong;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class LayoutButton extends ImageButton
{
   private Layout _layout;
   private Bitmap _tile, _tileLg;
   
   private static double _scale = 0.6;
   
   public LayoutButton(Context context)
   {
      super(context);
      init(context);
   }
   public LayoutButton(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      init(context);
   }
   private void init(Context context)
   {
      Resources resources = context.getResources();
      _tile = BitmapFactory.decodeResource(resources, R.drawable.tile);
      _tileLg = BitmapFactory.decodeResource(resources, R.drawable.tile_lg);
   }

   public void setLayout(Layout layout)
   {
      _layout = layout;
      invalidate();
   }

   public Layout getLayout() { return _layout; }

   protected void onDraw(Canvas canvas)
   {
      super.onDraw(canvas);
      
      if (_layout == null) return;
      
      Bitmap b = Bitmap.createBitmap(480, 320, Bitmap.Config.ARGB_8888);
      Canvas c = new Canvas(b);

      Bitmap tile = _layout.usesLargeImages() ? _tileLg : _tile;
      
      for (Position p : _layout.positions())
      {
         int left = p.bounds().left; int top = p.bounds().top;
         c.drawBitmap(tile, left, top, Tile.TileDrawPaint);
      }
      
      // assumption here is that onMeasure() is called before onDraw()
      Bitmap scaled = Bitmap.createScaledBitmap(b, _widthInner, _heightInner, false);
      canvas.drawBitmap(scaled, _left, _top, Tile.TileDrawPaint);
   }
   
   private int _width = 240, _height = 160;
   // these inner dimensions are a means of ensuring that button has 'padding'
   private int _widthInner = 240, _heightInner = 160;
   private int _left, _top;
   
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
   {
      _width = (int) (480 * _scale);  _height = (int) (320 * _scale);
      _widthInner = (int) (_width * 0.8);  _heightInner = (int) (_height * 0.8);
      _left = (_width - _widthInner) / 2;  _top = (_height - _heightInner) / 2;
      setMeasuredDimension(_width, _height);
   }
   
   
}
