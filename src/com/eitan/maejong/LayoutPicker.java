package com.eitan.maejong;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class LayoutPicker extends Activity
{
   private LayoutButton _layoutBtn;
   private ImageButton _prevBtn, _nextBtn;
   private ArrayList<Layout> _layouts;
   private TextView _currentLayoutNameTextview;
   
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.layout_picker);
      
      _prevBtn = (ImageButton) findViewById(R.id.prev_layout_btn);
      _nextBtn = (ImageButton) findViewById(R.id.next_layout_btn);
      _layoutBtn = (LayoutButton) findViewById(R.id.layout_btn);
      _currentLayoutNameTextview = (TextView) findViewById(R.id.layout_name_textview);

      _layouts = new ArrayList<Layout>();
      _layouts.addAll(app().layouts());
      
      setLayout(app().currentLayout());
      
      _prevBtn.setOnClickListener(new OnClickListener() {
         public void onClick(View v)
         {
            int idx = _layouts.indexOf(_layoutBtn.getLayout());
            int prevIdx = ( idx - 1 );
            if (prevIdx < 0) prevIdx += _layouts.size();
            setLayout(_layouts.get(prevIdx));
         }
      });
      _nextBtn.setOnClickListener(new OnClickListener() {
         public void onClick(View v)
         {
            int idx = _layouts.indexOf(_layoutBtn.getLayout());
            int nextIdx = ( idx + 1 ) % _layouts.size();
            setLayout(_layouts.get(nextIdx));
         }
      });
      _layoutBtn.setOnClickListener(new OnClickListener() {
         public void onClick(View v)
         {
            String layoutName = _layoutBtn.getLayout().name();
            PreferenceManager.getDefaultSharedPreferences(LayoutPicker.this).edit().
                                          putString("layout_option", layoutName).commit();
            finish();
         }
      });
      
   }
   
   private void setLayout(Layout layout)
   {
      _layoutBtn.setLayout(layout);
      String text = String.format(getString(R.string.layout_caption), _layouts.indexOf(layout)+1, 
            _layouts.size(), layout.caption(), layout.layoutSize());
      _currentLayoutNameTextview.setText(text);
   }

   private MaejongApplication app() { return (MaejongApplication) getApplication(); }
}
