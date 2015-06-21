package com.eitan.general;

public class TimeUtil
{
   public static String formatElapsedTime(int seconds)
   {
      int minutes = seconds / 60;
      if (minutes == 0)
      {
         return seconds + " sec.";
      }
      else
      {
         seconds = seconds % 60;
         String secs = ( seconds < 10 ? "0" : "" ) + seconds;
         return minutes + ":" + secs + " min.";
      }
   }
   
   public static String formatElapsedTimeForTimer(int seconds)
   {
      int minutes = seconds / 60;
      seconds = seconds % 60;
      return String.format("%02d:%02d ", minutes, seconds);  // extra space to correct a clipping issue
   }
   /**
    * @param duration -- elapsed time in milliseconds
    * @return elapsed time as a string, formatted either as "MM:SS min." or as "SS sec."
    */
   public static String formatElapsedTime(long elapasedTimeMs)
   {
      int seconds = (int) (elapasedTimeMs / 1000);
      return formatElapsedTime(seconds);
   }
}
