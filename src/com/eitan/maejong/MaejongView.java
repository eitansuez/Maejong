package com.eitan.maejong;

import java.util.Collection;
import java.util.Iterator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

public class MaejongView extends View implements AnimationListener
{
   private Maejong _maejong;
   private SelectionMgr _selectionMgr;
   
   private Deck _deck;
   
   private static final int AnimationDuration = 3000;

   private boolean _animating = false;
   private Iterator<Tile> _animatingTileIterator;
   private LayoutAnimationCountdownTimer _timer;
   private Tile _currentAnimatingTile;

   public MaejongView(Context context, AttributeSet attrs)
   {
      super(context, attrs);

      _maejong = (Maejong) context;

      setFocusableInTouchMode(true);

      _selectionMgr = new SelectionMgr(_maejong, this);
   }
   public void setDeck(Deck deck)
   {
      _deck = deck;
      setBackgroundForTheme();
   }

   public void initTransition() { _selectionMgr.initTransition(); }
   
   public void setBackgroundForTheme()
   {
      setBackgroundResource(_deck.currentTheme().backgroundResourceId());
   }
   
   public Game game() { return _maejong.game(); }
   
   public void animateWon()
   {
      _deck.currentTheme().winAnimation().setAnimationListener(this);
      startAnimation(_deck.currentTheme().winAnimation());
   }
   
   public void onAnimationEnd(Animation animation)
   {
      postDelayed(new Runnable() {
         public void run()
         {
            _maejong.won2();
         }
      }, 500);
   }
   public void onAnimationRepeat(Animation animation) { }
   public void onAnimationStart(Animation animation) { }
   
   /**
    * At this point I wonder why the heck I even bother with this.  App is defined 
    * as landscape so know it will be 480x320.
    */
   protected void onSizeChanged(int w, int h, int oldw, int oldh)
   {
      super.onSizeChanged(w, h, oldw, oldh);
      if (isLandscape(w, h))
      {
//         Log.d("Eitan", String.format("MaejongView:  onSizeChanged to %dx%d", w, h));
         _maejong.initialize(w, h);
         invalidate();
      }
   }
   private boolean isLandscape(int w, int h) { return w > h; }
   
   protected void onDraw(Canvas canvas)
   {
      if (_animating)
      {
         if (_currentAnimatingTile != null)
         {
            boolean free = game().isPositionFree(_currentAnimatingTile.position());
            _currentAnimatingTile.draw(_maejong, canvas, false, false, free);
         }
      }
      else
      {
         game().draw(_maejong, canvas, _selectionMgr.selectedTiles(), _selectionMgr.badTile());
      }
   }
   

   public boolean onTouchEvent(MotionEvent event)
   {
      if (event.getAction() != MotionEvent.ACTION_DOWN)
      {
         return super.onTouchEvent(event);
      }
      if (game().cleared())
      {
         return super.onTouchEvent(event);
      }

      return _selectionMgr.select((int) event.getX(), (int) event.getY());
   }
   
   public void toast(String text, int durationCode)
   {
      Toast.makeText(_maejong, text, durationCode).show();
   }

   
   private void buildTileAnimationTimer()
   {
      int interval = (int) (AnimationDuration / _deck.tiles().size() * 0.7);
      _timer = new LayoutAnimationCountdownTimer(AnimationDuration, interval);
   }
   
   void animateLayout(Collection<Tile> tiles) { animateLayout(tiles, false); }
   synchronized void animateLayout(Collection<Tile> tiles, boolean reshuffling)
   {
      if (_timer == null) buildTileAnimationTimer();
      
      _animatingTileIterator = tiles.iterator();
      _animating = true;
      _timer.reset(reshuffling);
   }
   
   class LayoutAnimationCountdownTimer extends CountDownTimer
   {
      private boolean doneCalled = false;
      private boolean reshuffling = false;
      
      LayoutAnimationCountdownTimer(int duration, int interval)
      {
         super(duration, interval);
      }
      
      public void reset(boolean reshuffling)
      {
         this.reshuffling = reshuffling;
         doneCalled = false;
         start();
      }
      
      public void onTick(long millisUntilFinished)
      {
         if (_animatingTileIterator.hasNext())
         {
            _currentAnimatingTile = _animatingTileIterator.next();
            _currentAnimatingTile.invalidate(MaejongView.this);
         }
         else
         {
            _timer.cancel();
            done();
         }
      }

      public void onFinish() { done(); }

      private void done()
      {
         if (doneCalled) return;

         _animating = false;
         postInvalidate();

         // game timer stuff should occur only on a new game or game restore, not on a reshuffle:
         if (!reshuffling)
         {
            if (game().inMidGame())
            {
               game().restoreStartTime();
            }
            else
            {
               game().recordGameStart();
            }
            _maejong.startGameTimer();
         }
         doneCalled = true;
      }
   }
   
   public void cleanup()
   {
      if (_animating)
      {
         _timer.cancel();
         _animating = false;
      }
   }

}
