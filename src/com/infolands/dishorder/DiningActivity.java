package com.infolands.dishorder;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


public class DiningActivity extends Activity {
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.dining);
      
      setDiningSelectButton();
  }

  private void setDiningSelectButton() {
    Button diningBt = (Button) findViewById(R.id.diningBt);
    diningBt.setOnClickListener(new View.OnClickListener() {
  
      @Override
      public void onClick(View v) {
        LayoutInflater factory = LayoutInflater.from(DiningActivity.this);
        
        final View diningView = factory.inflate(R.layout.diningselect, null);
        new AlertDialog.Builder(DiningActivity.this)
            .setTitle(R.string.dining)
            .setView(diningView)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  
                  DishApplication app = (DishApplication)getApplicationContext();
                  
                  try {
                    Intent intent = new Intent(DiningActivity.this,MenuActivity.class);  
                    startActivity(intent);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
            })
//            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    /* User clicked cancel so do some stuff */
//                }
//            })
            .create()
            .show();
      }
    });
  }
    
}