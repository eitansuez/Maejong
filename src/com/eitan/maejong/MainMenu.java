package com.eitan.maejong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.eitan.scores.TopScores;

public class MainMenu extends Activity implements OnClickListener
{
   private boolean exiting = true;

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.mainmenu);
      int[] ids = {R.id.startBtn, R.id.layoutBtn, R.id.themeBtn, 
              R.id.instructionsBtn, R.id.topScoresBtn, R.id.aboutBtn};
      for (int id : ids)
      {
         Button btn = (Button) findViewById(id);
         btn.setOnClickListener(this);
      }
   }
   
   protected void onPause()
   {
      super.onPause();
      
      if (exiting)
      {
         app().saveState();
      }
   }
   private MaejongApplication app() { return (MaejongApplication) getApplication(); }

   protected void onResume()
   {
      super.onResume();
      exiting = true;
   }
   

   public void onClick(View v)
   {
      exiting = false;
      switch (v.getId())
      {
         case R.id.startBtn:
            startActivity(new Intent(this, Maejong.class));
            break;
         case R.id.layoutBtn:
            startActivity(new Intent(this, LayoutPicker.class));
            break;
         case R.id.themeBtn:
            startActivity(new Intent(this, ThemePicker.class));
            break;
         case R.id.instructionsBtn:
            startActivity(new Intent(this, Instructions.class));
            break;
         case R.id.topScoresBtn:
            startActivity(new Intent(this, TopScores.class));
            break;
         case R.id.aboutBtn:
            startActivity(new Intent(this, About.class));
            break;
      }
   }

}
