package com.eitan.maejong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.View;

public class Tile implements Comparable<Tile>
{
   public static double Width = 37;
   public static double Height = 45;

   private TileType _type;
   private Position _position;
   private Bitmap _bitmap = null;  // normally null but can be a different bitmap per instance (e.g. seasons & flowers) 
   
   public Tile(TileType t)
   {
      _type = t;
      takeBitmap();
   }
   public synchronized void takeBitmap()
   {
      if (_type.hasManyImages())
      {
         _bitmap = _type.nextImage();
      }
   }
   
   public TileType type() { return _type; }
   
   public Position position() { return _position; }
   public void position(Position pos) { _position = pos; }

   public void invalidate(View view)
   {
      Rect bounds = _position.bounds();
      view.invalidate(bounds.left-1, bounds.top-1, bounds.right+1 + Game.Perspective_run, bounds.bottom+1 + Game.Perspective_rise);
   }
   
   public void draw(Context context, Canvas canvas, boolean selected, boolean bad, boolean free)
   {
      int x = _position.bounds().left; int y = _position.bounds().top;
      _type.draw(canvas, x, y, TileDrawPaint, this);
      highlight(context, canvas, selected, bad, free);
   }
   
   public Bitmap bitmap() { return _bitmap; }
   
   public void highlight(Context context, Canvas canvas, boolean selected, boolean bad, boolean free)
   {
      Pair<Paint> paints = selectPaints(context, selected, bad, free);
      if (paints != null)
      {
         canvas.drawRoundRect(_position.boundsF(), 5, 5, paints.first());
         canvas.drawRoundRect(_position.boundsF(), 5, 5, paints.second());
      }
   }
   
   private Pair<Paint> selectPaints(Context context, boolean selected, boolean bad, boolean free)
   {
      if (bad) { return BadPaints; }
      else if (selected) { return SelectedPaints; }
      else if (free && Settings.highlightFreeTiles(context)) { return FreePaints; }
      return null;
   }
   
   public boolean matches(Tile tile) { return _type.equals(tile.type()); }
   
   public String toString()
   {
      return String.format("%s %s", _type, _position);
   }
   
   public int compareTo(Tile another)
   {
      return new Integer(_type.ordinal()).compareTo(another.type().ordinal());
   }


   private static final Paint SelectedPaint, FreePaint, BadPaint;
   private static final Paint SelectedStrokePaint, FreeStrokePaint, BadStrokePaint;
   public static final Paint TileDrawPaint;
   private static final Pair<Paint> SelectedPaints, FreePaints, BadPaints;
   static
   {
      SelectedPaint = new Paint();
      SelectedPaint.setAntiAlias(true);
      SelectedPaint.setStyle(Style.FILL);
      SelectedPaint.setColor(0x400000ff); // 0x80c4c3d0);

      SelectedStrokePaint = new Paint();
      SelectedStrokePaint.setAntiAlias(true);
      SelectedStrokePaint.setStyle(Style.STROKE);
      SelectedStrokePaint.setColor(0xff0000ff); // 0x80c4c3d0);
      
      SelectedPaints  = new Pair<Paint>(SelectedPaint, SelectedStrokePaint);

      FreePaint = new Paint();
      FreePaint.setAntiAlias(true);
      FreePaint.setStyle(Style.FILL);
      FreePaint.setColor(0x4000ff00); // 9BDBE6);

      FreeStrokePaint = new Paint();
      FreeStrokePaint.setAntiAlias(true);
      FreeStrokePaint.setStyle(Style.STROKE);
      FreeStrokePaint.setColor(0xff00ff00); // 9BDBE6);

      FreePaints  = new Pair<Paint>(FreePaint, FreeStrokePaint);
      
      BadPaint = new Paint();
      BadPaint.setAntiAlias(true);
      BadPaint.setStyle(Style.FILL);
      BadPaint.setColor(0x40ff0000); // d96464);
      
      BadStrokePaint = new Paint();
      BadStrokePaint.setAntiAlias(true);
      BadStrokePaint.setStyle(Style.STROKE);
      BadStrokePaint.setColor(0xffff0000); // d96464);
      
      BadPaints = new Pair<Paint>(BadPaint, BadStrokePaint);

      TileDrawPaint = new Paint();
      TileDrawPaint.setAntiAlias(true);
      TileDrawPaint.setStyle(Style.FILL);
   }
   
   
}
