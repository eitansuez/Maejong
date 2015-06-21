package com.eitan.general;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * An AutoCompleteTextView that tries to get around the limitation of a threshold minimum value of 1
 * 
 * @author Eitan Suez
 */
public class MyAutoCompleteTextView extends AutoCompleteTextView
{

   public MyAutoCompleteTextView(Context context)
   {
      super(context);
   }
   public MyAutoCompleteTextView(Context context, AttributeSet attrs)
   {
      super(context, attrs);
   }
   public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
   {
      super(context, attrs, defStyle);
   }
   
   
   public boolean enoughToFilter()
   {
      return true;
   }
   
}
