package com.eitan.scores;

import com.eitan.general.TimeUtil;

public class ScoreEntry implements Comparable<ScoreEntry>
{
   private String name;
   private int value;
   
   public ScoreEntry(String n, int v)
   {
      name = n;
      value = v;
   }
   public String name() { return name; }
   public int value() { return value; }
   
   public String valueFormatted() // assume value is time
   {
      return TimeUtil.formatElapsedTime(value);
   }

   public int compareTo(ScoreEntry another)
   {
      return new Integer(value).compareTo(another.value());
   }
   public boolean worseThan(int someValue)
   {
      return value > someValue;
   }
   
   public String toString()
   {
      return String.format("%s: %s", name, valueFormatted());
   }
   

}
