package com.eitan.general;

public class StringUtil
{
   public static boolean endsWithDigit(String t)
   {
      char lastChar = t.charAt(t.length()-1);
      return Character.isDigit(lastChar);
   }

   public static String capitalize(String text)
   {
      return Character.toUpperCase(text.charAt(0)) + text.substring(1);
   }
}
