package com.eitan.maejong;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;
import com.eitan.general.ShakeListener;
import com.eitan.general.ShakeMgr;
import com.eitan.general.TimeUtil;
import com.eitan.scores.NameCaptureDialog;
import com.eitan.scores.TopScores;
import com.eitan.scores.TopScoresMgr;

/**
 * Maia, Arik, & Ezra's Mahjong Solitaire game
 * 
 * @author Eitan Suez
 * 
 */
public class Maejong extends Activity implements OnSharedPreferenceChangeListener, ShakeListener
{
   private MaejongView _view;

   private ShakeMgr _shakeMgr;
   private boolean _shakeOn;
   
   private boolean _ignoreOnKeyUpOnce = false;
   
   private TextView _timerTextField;
   private CountDownTimer _timer;
   
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.maejong);
      _view = (MaejongView) findViewById(R.id.maejongView);
      _view.setDeck(app().deck());
      
      _timerTextField = (TextView) findViewById(R.id.timerTextField);

      registerForContextMenu(_view);
      
      _shakeMgr = new ShakeMgr(Maejong.this);

      _timer = new CountDownTimer(60*60*1000, 1000)
      {
         public void onTick(long millisUntilFinished)
         {
            updateTimerTextField();
         }
         public void onFinish()
         {
            _timerTextField.setTextColor(Color.RED);
            updateTimerTextField();
         }
      };
   }
   
   private void updateTimerTextField()
   {
      _timerTextField.setText(game().currentGameTime());
   }
   
   protected void onStart()
   {
      super.onStart();
      PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
      _shakeMgr.register();
      _shakeOn = Settings.shakeToShuffleOn(Maejong.this);
   }
   
   protected void onPause()
   {
      super.onPause();
      game().recordGameStop();
      _view.cleanup();
      
      _timer.cancel();
   }
   
   protected void onStop()
   {
      _shakeMgr.deregister();
      PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
      super.onStop();
   }

   
   private MaejongApplication app() { return (MaejongApplication) getApplication(); }
   public SoundMgr soundMgr() { return app().soundMgr(); }
   public Game game() { return app().game(); }
   

   public void onShake()
   {
      if (!_shakeOn) return;
      
      _view.post(new Runnable() {
         public void run()
         {
//            Log.d("Eitan", "on shake heard..reshuffling tiles");
            _view.toast(getString(R.string.shuffled_msg), Toast.LENGTH_SHORT);
            reshuffle();
         }
      });
   }
  
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
   {
      if ("highlight_option".equals(key))
      {
         _view.invalidate();
      }
      else if ("sound_option".equals(key))
      {
         soundMgr().soundOn(Settings.soundOn(this));
      }
      else if ("shake_option".equals(key))
      {
         _shakeOn = Settings.shakeToShuffleOn(this);
      }
   }
   
   // =====
   
   void initialize(final int w, final int h)
   {
//      Log.d("Eitan", "in Maejong.initialize()");
      game().initialize(w, h);
      newGame(false);
   }

   // =====

   void newGame() { newGame(true); }
   private void newGame(boolean clear)
   {
      _timerTextField.setText("00:00 ");
      _timer.cancel();  // in case one is running
      
//      Log.d("Eitan", "Maejong: Starting new game or restoring saved game");
      game().reset(_view, clear);

      _view.initTransition();
      _view.invalidate();
   }
   
	void reshuffle()
	{
		game().shuffleTiles(_view);
		_view.invalidate();
	}
   
   // =====

   static final int WON_DIALOG = 1;
   static final int LOST_DIALOG = 2;
   static final int CONTINUE_DIALOG = 3;
   public static final int NAME_CAPTURE_DLG = 4;
   
   protected Dialog onCreateDialog(int id)
   {
      switch (id) {
         case NAME_CAPTURE_DLG: {
            final NameCaptureDialog ncd = new NameCaptureDialog(this);
            TopScoresMgr scoresMgr = app().scoresMgr();
            ncd.setMatchEntries(scoresMgr.allEntries());
            ncd.setName(scoresMgr.lastRecordedName());
            ncd.setOnDismissListener(new OnDismissListener() {
               public void onDismiss(DialogInterface dialog)
               {
                  String name = ncd.getName();
//                  Log.d("Eitan", "Got name: "+name);
                  int seconds = game().gameTimeSeconds();
                  app().scoresMgr().addScore(name, seconds);
                  ncd.addMatchingEntry(name);
                  showTopScores();
               }
            });
            return ncd;
         }
         case WON_DIALOG: {
            return new AlertDialog.Builder(this).
            setTitle(R.string.congrats).
            setMessage(_dlgmsg).
            setPositiveButton(R.string.another, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int which)
                  {
                     newGame();
                  }
               }).
            setNeutralButton(R.string.dismiss_caption, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int which)
                  {
                     dialog.dismiss();
                  }
               }).
            setNegativeButton(R.string.back, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int which)
                  {
                     finish();
                  }
               }).create();
         }
         case LOST_DIALOG: {
            return new AlertDialog.Builder(this).
            setTitle(R.string.nomoremoves).setMessage(R.string.stuck_msg).
            setNeutralButton(R.string.newgame_label, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int which)
                  {
                     newGame();
                  }
               }).
            setNegativeButton(R.string.dismiss_caption, new DialogInterface.OnClickListener()
            {
               public void onClick(DialogInterface dialog, int which)
               {
                  dialog.dismiss();
               }
            }).create();
         }
         case CONTINUE_DIALOG: {
            return new AlertDialog.Builder(this).
            setTitle(R.string.nomoremoves).setMessage(R.string.stuck_msg).
            setPositiveButton(R.string.reshuffle_label, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int which)
                  {
                     reshuffle();
                  }
               }).
            setNeutralButton(R.string.newgame_label, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int which)
                  {
                     newGame();
                  }
               }).
            setNegativeButton(R.string.dismiss_caption, new DialogInterface.OnClickListener()
            {
               public void onClick(DialogInterface dialog, int which)
               {
                  dialog.dismiss();
               }
            }).create();
         }
     }
     return null;
   }

   public void won()
   {
      game().recordGameStop();
      updateTimerTextField();
      _timer.cancel();
      _view.animateWon();
   }
   public void won2()
   {
      soundMgr().win();
      if (!saveTopScore())
      {
         _dlgmsg = String.format(getString(R.string.clearedrecord_msg), game().gameTime());
         showDialog(WON_DIALOG);
      }
   }
   public void lost()
   {
      soundMgr().stuck();
      showDialog(LOST_DIALOG);
   }
   public void lostButCanContinue()
   {
      soundMgr().q();
      showDialog(CONTINUE_DIALOG);
   }
   
   private String _dlgmsg = null;
   private String _dlgtitle = null;
   protected void onPrepareDialog(int id, Dialog dialog)
   {
      if (_dlgmsg != null && dialog instanceof AlertDialog)
      {
         ((AlertDialog) dialog).setMessage(_dlgmsg);
         _dlgmsg = null;
      }
      if (_dlgtitle != null)
      {
         dialog.setTitle(_dlgtitle);
         _dlgtitle = null;
      }
   }


   
   // =====
   
   public boolean onCreateOptionsMenu(Menu menu)
   {
      super.onCreateOptionsMenu(menu);
      getMenuInflater().inflate(R.menu.menu, menu);
      return true;
   }
   public void onCreateContextMenu(ContextMenu menu, View v,
         ContextMenuInfo menuInfo)
   {
      super.onCreateContextMenu(menu, v, menuInfo);
      getMenuInflater().inflate(R.menu.contextmenu, menu);
   }
   
   private static final int Settings_RequestCode = 11;
//   private static final int Instructions_RequestCode = 12;
//   private static final int About_RequestCode = 13;
   private static final int TopScores_RequestCode = 14;
   
   public boolean onContextItemSelected(MenuItem item) { return onOptionsItemSelected(item); }
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case R.id.settings:
            startActivityForResult(new Intent(this, Settings.class), Settings_RequestCode);
            return true;
         case R.id.newgame:
            newGame();
            return true;
         case R.id.reshuffle:
            reshuffle();
            return true;
      }
      return false;
   }
   
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
//      Log.d("Eitan", "Setting ignore onkeyup");
      boolean dismissedwithbackkey = true;
      if (data != null)
      {
         dismissedwithbackkey = data.getBooleanExtra("dismissedwithbackkey", true);
      }
      if (dismissedwithbackkey)
      {
         _ignoreOnKeyUpOnce = true;
      }
      
      if (requestCode == TopScores_RequestCode && game().cleared())
      {
         _dlgmsg = getString(R.string.whatnext);
         showDialog(WON_DIALOG);
      }
      else if (requestCode == Settings_RequestCode && !game().cleared())
      {
         game().restoreStartTime();
         _timer.start();
      }
   }
   
   // perhaps delay start..
   private static final int delayTimerStartMillis = 100;
   void startGameTimer()
   {
      Date timeToRun = new Date(System.currentTimeMillis()+delayTimerStartMillis);
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
              public void run() {
//      Log.d("Eitan", "Starting game timer");
                 _timer.start();
              }
          }, timeToRun);
   }

   public boolean onKeyDown(int keyCode, KeyEvent event)
   {
//      Log.d("Eitan", "Maejong:: Heard onKeyDown event");
      if (keyCode == KeyEvent.KEYCODE_BACK)
      {
         return true;  // wait for keyup event
      }
      return super.onKeyDown(keyCode, event);
   }

   public boolean onKeyUp(int keyCode, KeyEvent event)
   {
//      Log.d("Eitan", "Maejong:: Heard onKeyUp event");
//    Log.i("Eitan", String.format("caught on key up, key code is %d", keyCode));
      if (keyCode == KeyEvent.KEYCODE_BACK)
      {
         if (_ignoreOnKeyUpOnce)
         {
//            Log.d("Eitan", "Re-setting ignore onkeyup");
            _ignoreOnKeyUpOnce = false;  // reset
         }
         else
         {
   //       Log.i("Eitan", String.format("down time: %d, event time: %d", event.getDownTime(), event.getEventTime()));
            if (event.getEventTime() - event.getDownTime() < 1000)
            {
               Pair<Tile> addedBack = game().undo(_view);
               if (addedBack != null)
               {
                  addedBack.invalidate(_view);
                  return true;
               }
            }
            super.onKeyDown(keyCode, event); // quit
         }
      }
      return super.onKeyUp(keyCode, event);
   }
   

   public boolean saveTopScore()
   {
      int seconds = game().gameTimeSeconds();
      if (app().scoresMgr().qualifiesAsTopScore(seconds))
      {
         _dlgtitle = String.format(getString(R.string.newhighscore_msg), TimeUtil.formatElapsedTime(seconds));
         showDialog(Maejong.NAME_CAPTURE_DLG);
         return true;
      }
      return false;
   }
   
   private void showTopScores()
   {
      Intent intent = new Intent(this, TopScores.class);
      startActivityForResult(intent, TopScores_RequestCode);
   }
   

}
