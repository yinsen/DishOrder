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
import android.widget.ImageView;
import android.widget.TextView;


public class BigImgActivity extends Activity {
    
  private String mixture_id = "1";
  private int dish_num = 1;
  private String taste = "taste";
  private String cookie = "cookie";
  private String weight = "weight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bigimg);
        
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
        
        int size = ((DishApplication)getApplicationContext()).dishList.size();
        String dishId = ((DishApplication)getApplicationContext()).currDishId;
        String dishName = "";
        for (int i=0; i<size; i++){
          if (((DishApplication)getApplicationContext()).dishList.get(i).dish_id.equals(dishId)){
            dishName = ((DishApplication)getApplicationContext()).dishList.get(i).name;
            
            break;
          }
        }
        TextView orderDishName = (TextView) findViewById(R.id.orderdishname);
        orderDishName.setText(dishName);
        
        Button detailBtn = (Button) findViewById(R.id.detailBt);
        detailBtn.setOnClickListener(new View.OnClickListener() {

          public void onClick(View v) {
            try {
              Intent intent = new Intent(BigImgActivity.this,OrderActivity.class);  
              startActivity(intent); 
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        
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
        
        
        ImageView bigImg = (ImageView) findViewById(R.id.dishimg);
        bigImg.setBackgroundResource(R.drawable.bigimg);
    }
  
}