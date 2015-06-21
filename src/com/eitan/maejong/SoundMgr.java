package com.eitan.maejong;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundMgr
{
   private SoundPool _snd;
   private int _winOrQuit, _continue, _stuck, _select1, _match, _nomatch, _badSelect;
   private boolean _soundOn;
   
   public SoundMgr(Context context)
   {
      _snd = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
      _winOrQuit = _snd.load(context, R.raw.tuxok, 0);
      _continue = _snd.load(context, R.raw.areyousure, 0);
      _stuck = _snd.load(context, R.raw.youcannot, 0);
      _select1 = _snd.load(context, R.raw.paint1, 0);
      _match = _snd.load(context, R.raw.paint2, 0);
      _nomatch = _snd.load(context, R.raw.paint3, 0);
      _badSelect = _snd.load(context, R.raw.paint4, 0);

      _soundOn = Settings.soundOn(context);  // to do: perhaps make app into settings and remove Settings class
   }
   
   public void win() { play(_winOrQuit); }
   public void quit() { play(_winOrQuit); }
   public void stuck() { play(_stuck); }
   public void q() { play(_continue); }
   public void select() { play(_select1); }
   public void match() { play(_match); }
   public void nomatch() { play(_nomatch); }
   public void badSelect() { play(_badSelect); }
   
   void soundOn(boolean value)
   {
//      Log.d("Eitan", "Setting sound "+(value ? "on" : "off"));
      _soundOn = value;
   }
   
   private void play(int soundID)
   {
      if (_soundOn)
      {
         _snd.play(soundID, 1, 1, 0, 0, 1);
      }
   }
}

