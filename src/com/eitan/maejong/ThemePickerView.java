package com.eitan.maejong;

import java.util.HashMap;
import java.util.Iterator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class ThemePickerView extends View
{
   private Tile _selectedTile;
   private HashMap<String, Tile> _tiles = new HashMap<String, Tile>();
   private HashMap<String, Bitmap> _tileBitmaps = new HashMap<String, Bitmap>();
   private Layout _layout;
   private ThemePicker _context;
   private int _tileWidth, _tileHeight;
   private Paint _textPaint, _fillPaint;
   private RectF _fillRect;

   public ThemePickerView(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      _context = (ThemePicker) context;
      Resources resources = _context.getResources();
      
      _layout = new Layout()
      {
         public String name() { return "Theme Picker Layout"; }
         public String caption() { return name(); }
         protected void build()
         {
            addPosition(new Position(2, 2, 0));
            addPosition(new Position(2, 6, 0));
         }
         protected int layoutSize() { return 2; }
         protected int numCols() { return 10; }
         protected int numRows() { return 6; }
         protected int numLayers() { return 1; }
         protected boolean usesLargeImages() { return false; }
      };
      
      boolean firstpass = true;
      for (MaejongTheme theme : app().deck().themes())
      {
         Tile t = new Tile(theme.tileTypes()[0]);
         String themename = theme.name();
         _tiles.put(themename, t);

         String idname = String.format("com.eitan.maejong:drawable/%s_themetile", themename.toLowerCase());
//         Log.d("Eitan", "looking for "+idname);
         int resourceId = resources.getIdentifier(idname, null, null);
         Bitmap bm = BitmapFactory.decodeResource(resources, resourceId);
         if (firstpass)
         {
            _tileWidth = bm.getWidth() - Game.Perspective_run;
            _tileHeight = bm.getHeight() - Game.Perspective_rise;
            firstpass = false;
         }

         _tileBitmaps.put(themename, bm);
      }

      String currentTheme = app().currentTheme().name();
      _selectedTile = _tiles.get(currentTheme);

      _textPaint = new Paint();
      _textPaint.setAntiAlias(true);
      _textPaint.setStyle(Style.FILL);
      _textPaint.setTextSize(20);
      _textPaint.setTextAlign(Paint.Align.CENTER);

      _fillPaint = new Paint();
      _fillPaint.setAntiAlias(true);
      _fillPaint.setStyle(Style.FILL);
      _fillPaint.setColor(0xb0ffffff);
      
      _fillRect = new RectF();
   }
   
   protected void onSizeChanged(int w, int h, int oldw, int oldh)
   {
      super.onSizeChanged(w, h, oldw, oldh);
      
      int left = (w - _layout.layoutWidth(_tileWidth)) / 2;
      int top = (h - _layout.layoutHeight(_tileHeight)) / 2;
      
      for (Position p : _layout.positions())
      {
         p.calculateBounds(left, top, _tileWidth, _tileHeight);
      }

      Iterator<Position> pitr = _layout.positions().iterator();
      for (Tile t : _tiles.values())
      {
         t.position(pitr.next());
      }
      invalidate();
   }
   

   public boolean onTouchEvent(MotionEvent event)
   {
      if (event.getAction() != MotionEvent.ACTION_DOWN)
      {
         return super.onTouchEvent(event);
      }
      return select((int) event.getX(), (int) event.getY());
   }
   public boolean select(int x, int y)
   {
      Tile tile = tileAtPoint(x, y);
      boolean found = (tile != null);
      if (found)
      {
         select(tile);
      }
      return found;
   }
   public Tile tileAtPoint(int x, int y)
   {
      for (Tile t : _tiles.values())
      {
         if (t.position().bounds().contains(x, y))
         {
            return t;
         }
      }
      return null;
   }
   public void select(Tile tile)
   {
      _selectedTile = tile;
      invalidate();
      app().soundMgr().select();
      _context.setThemeAndFinish(_selectedTile.type().theme().name());
   }
   
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
   {
      setMeasuredDimension(480, 240);
   }

   protected void onDraw(Canvas canvas)
   {
      float height = _textPaint.getTextSize();
      for (Tile t : _tiles.values())
      {
         int x = t.position().bounds().left; int y = t.position().bounds().top;
         
         MaejongTheme theme = t.type().theme();
         String themename = theme.name();
         String themecaption = theme.caption();
         
         canvas.drawBitmap(_tileBitmaps.get(themename), x, y, Tile.TileDrawPaint);
         
         x = x + _tileWidth / 2;
         y = y + _tileHeight + 30;
         
         float width = _textPaint.measureText(themecaption);
         _fillRect.set(x-(width/2)-3, y-height-3, x+(width/2)+3, y+3);
         canvas.drawRoundRect(_fillRect, 5, 5, _fillPaint);
         
         canvas.drawText(themecaption, x, y, _textPaint);
      }
      _selectedTile.highlight(_context, canvas, true, false, false);
   }
   
   private MaejongApplication app() { return (MaejongApplication) _context.getApplication(); }
   
}
