package com.eitan.scores;

import java.util.ArrayList;
import java.util.HashSet;
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.eitan.maejong.R;

public class NameCaptureDialog extends Dialog implements OnClickListener
{
   private AutoCompleteTextView _textfield;
   private ArrayAdapter<String> _adapter;
   private HashSet<String> _backingSet = new HashSet<String>();
   
   public NameCaptureDialog(Context context)
   {
      super(context, android.R.style.Theme_Dialog);
      
      setTitle(R.string.highscorewinner);
      setContentView(R.layout.edit);

      _textfield = (AutoCompleteTextView) findViewById(R.id.name_text);
      _textfield.setOnEditorActionListener(new OnEditorActionListener()
         {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
               _textfield.dismissDropDown();
               return false;
            }
         });
      
      Button okBtn = (Button) findViewById(R.id.ok_btn);
      okBtn.setOnClickListener(this);
   }
   
   public void onWindowFocusChanged(boolean hasFocus)
   {
      super.onWindowFocusChanged(hasFocus);
      // not ready yet..
//      if (hasFocus)
//      {
//         if (!_backingSet.isEmpty())
//         {
//            _textfield.showDropDown();
//         }
//      }
   }


   
   private void addToBackingSet(String name, boolean addToAdapter)
   {
      if ("--".equals(name) || TopScoresMgr.anonymous.equals(name)) return;
      if (addToAdapter && !_backingSet.contains(name))
      {
         _adapter.add(name);
      }
      _backingSet.add(name);
   }

   public void setMatchEntries(ArrayList<ScoreEntry> scores)
   {
      for (ScoreEntry score : scores)
      {
         addToBackingSet(score.name(), false);
      }
      
      _adapter = new ArrayAdapter<String>(getContext(), 
                                          android.R.layout.simple_dropdown_item_1line, 
                                          new ArrayList<String>(_backingSet));
      _textfield.setAdapter(_adapter);
   }
   
   public void addMatchingEntry(String name)
   {
      addToBackingSet(name, true);
   }
   public void addMatchEntry(ScoreEntry entry) { addMatchingEntry(entry.name()); }
   
   public void setName(String name)
   {
      _textfield.setText(name);
      _textfield.selectAll();
   }
   
   public String getName()
   {
      String name = _textfield.getText().toString();
      if (name == null || name.length() == 0) { name = TopScoresMgr.anonymous; }
      _textfield.selectAll();
      return name;
   }
   public void onClick(View v) { dismiss(); }

}
