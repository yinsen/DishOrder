package com.infolands.dishorder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class OrderActivity extends Activity {
    
  private String mixture_id = "1";
  private int dish_num = 1;
  private String taste = "taste";
  private String cookie = "cookie";
  private String weight = "weight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order);
        
        Button backBtn = (Button) findViewById(R.id.backbt);
        backBtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            new Thread() {

              public void run() {
                try {
                  Instrumentation inst = new Instrumentation();
                  inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
                catch (Exception e) {
                  Log.e("Exception when onBack", e.toString());
                }
              }
            }.start();
          }
        });
        
        Intent intent = getIntent();  
        Bundle bundle = intent.getBundleExtra("dishname");  
        String dishName = bundle.getString("dishname"); 
        TextView orderDishName = (TextView) findViewById(R.id.orderdishname);
        orderDishName.setText(dishName);
        
        Button orderBtn = (Button) findViewById(R.id.orderbt);
        orderBtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(final View v) {
                
            //OrderActivity activity = (OrderActivity)(v.getContext());
            ((DishApplication)getApplicationContext()).OrderOneDish(mixture_id, taste, cookie, weight, dish_num);
            
            new Thread() {

              public void run() {
                try {
                  Instrumentation inst = new Instrumentation();
                  inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
                catch (Exception e) {
                  Log.e("Exception when onBack", e.toString());
                }
              }
            }.start();
          }
        });
        
        Button mixtureBtn = (Button) findViewById(R.id.mixturebt);
        mixtureBtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(final View v) {

            LayoutInflater factory = LayoutInflater.from(OrderActivity.this);
            
            final View mixtureView = factory.inflate(R.layout.mixture, null);
            new AlertDialog.Builder(OrderActivity.this)
                .setTitle(R.string.mixture)
                .setView(mixtureView)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                      
                      DishApplication app = (DishApplication)getApplicationContext();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }
                })
                .create()
                .show();
          }
        });
        
        Button remarksBtn = (Button) findViewById(R.id.remarksbt);
        remarksBtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(final View v) {

            LayoutInflater factory = LayoutInflater.from(OrderActivity.this);
            
            final View remarksView = factory.inflate(R.layout.remarks, null);
            new AlertDialog.Builder(OrderActivity.this)
                .setTitle(R.string.remarks)
                .setView(remarksView)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                      
                      DishApplication app = (DishApplication)getApplicationContext();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }
                })
                .create()
                .show();
          }
        });
        
        
    }

  
}