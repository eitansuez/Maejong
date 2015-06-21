package com.eitan.maejong;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import com.eitan.general.DismissActivity;

public class Instructions extends DismissActivity
{

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.instructions);
      TextView instructionsView = (TextView) findViewById(R.id.instructionsText);
      String source = getString(R.string.instructions_text);
      CharSequence html = Html.fromHtml(source);
      instructionsView.setText(html);
   }

}
