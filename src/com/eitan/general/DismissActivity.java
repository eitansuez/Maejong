package com.eitan.general;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;

public class DismissActivity extends Activity
{

   public boolean onTouchEvent(MotionEvent event)
   {
      Intent data = new Intent();
      data.putExtra("dismissedwithbackkey", false);
      setResult(RESULT_OK, data);
      finish();
      return true;
   }
   
}
