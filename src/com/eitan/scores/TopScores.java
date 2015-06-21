package com.eitan.scores;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemSelectedListener;
import com.eitan.maejong.Layout;
import com.eitan.maejong.MaejongApplication;
import com.eitan.maejong.R;

public class TopScores extends Activity implements OnItemSelectedListener
{
   private LayoutInflater _inflater;
   private Spinner _selector;
   private HashMap<String, ListView> _configs = new HashMap<String, ListView>();
   private FrameLayout _framelayout;
   
   private MaejongApplication app() { return (MaejongApplication) getApplication(); }

   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      setTitle(R.string.topscores_label);
      setContentView(R.layout.scores);
      
      _selector = (Spinner) findViewById(R.id.score_selector);

      ArrayAdapter<Layout> adapter = new ArrayAdapter<Layout>(this, android.R.layout.simple_spinner_item, 
            new ArrayList<Layout>(app().layouts()));
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      _selector.setAdapter(adapter);
      
      _inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
      _framelayout = (FrameLayout) findViewById(R.id.scores_content);
      
      _selector.setOnItemSelectedListener(this);
      
      Button clearScoresBtn = (Button) findViewById(R.id.clearscores_btn);
      clearScoresBtn.setOnClickListener(new OnClickListener() {
         public void onClick(View v)
         {
            clearScores();
         }
      });
      Button dismissBtn = (Button) findViewById(R.id.dismiss_btn);
      dismissBtn.setOnClickListener(new OnClickListener() {
         public void onClick(View v)
         {
            finish();
         }
      });
   }

   private String _currentConfig;
   
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
   {
      if (_currentConfig != null)
      {
         viewForConfig(_currentConfig).setVisibility(View.INVISIBLE);
      }
      _currentConfig = ((Layout) _selector.getSelectedItem()).name();
      viewForConfig(_currentConfig).setVisibility(View.VISIBLE);
   }
   
   private synchronized ListView viewForConfig(String config)
   {
      if (!_configs.containsKey(config))
      {
         setupListView(config);
      }
      return _configs.get(config);
   }

   public void onNothingSelected(AdapterView<?> parent) { }


   private void clearScores()
   {
      String config = ((Layout) _selector.getSelectedItem()).name();
      scoresMgr().clearScores(config);
      ((ArrayAdapter<?>) _configs.get(config).getAdapter()).notifyDataSetChanged();
   }
   
   private TopScoresMgr scoresMgr() { return ((MaejongApplication) getApplication()).scoresMgr(); }
    
   
   private void setupListView(String config)
   {
      TopScoresList scores = scoresMgr().scores(config);

      ArrayAdapter<ScoreEntry> listadapter = 
         new ArrayAdapter<ScoreEntry>(this, R.layout.adapter, R.id.nameTextView, scores.entries())
      {
         public View getView(int position, View convertView, ViewGroup parent)
         {
            View view;
  
            if (convertView == null)
            {
               view = _inflater.inflate(R.layout.adapter, parent, false);
            }
            else
            {
               view = convertView;
            }
  
            ScoreEntry entry = (ScoreEntry) getItem(position);
            
            TextView text = (TextView) view.findViewById(R.id.nameTextView);
            text.setText(entry.name());

            text = (TextView) view.findViewById(R.id.scoreTextView);
            text.setText(entry.valueFormatted());
  
            return view;
         }

         public boolean areAllItemsEnabled() { return false; }
         public boolean isEnabled(int position) { return false; }
         
      };
      
      ListView listview = new ListView(this);
      listview.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
      listview.setAdapter(listadapter);
      
      _configs.put(config, listview);
      _framelayout.addView(listview);
   }
   
   
   protected void onStart()
   {
      super.onStart();

      String gameConfig = scoresMgr().gameConfig();
      for (int i=0; i<_selector.getCount(); i++)
      {
         if (gameConfig.equals(((Layout)_selector.getItemAtPosition(i)).name()))
         {
            _selector.setSelection(i, true);
            break;
         }
      }
   }

   public boolean onTouchEvent(MotionEvent event)
   {
      Intent data = new Intent();
      data.putExtra("dismissedwithbackkey", false);
      setResult(RESULT_OK, data);
      finish();
      return true;
   }
}
