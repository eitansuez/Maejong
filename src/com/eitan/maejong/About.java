package com.eitan.maejong;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.eitan.general.DismissActivity;

public class About extends DismissActivity
{

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.about);
      TextView versionView = (TextView) findViewById(R.id.version_about_textview);
      CharSequence version = versionView.getText();
      versionView.setText("v" + version);
   }

}
