package com.infolands.dishorder;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class OrderActivity extends Activity {
    
  private String dish_id;
  private String orderlist_id;
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
        
        Bundle bundle = getIntent().getBundleExtra("order_item");  
        dish_id = bundle.getString("dish_id"); 
        
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
        
        Button orderBtn = (Button) findViewById(R.id.orderbt);
        orderBtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(final View v) {
                
            OrderActivity activity = (OrderActivity)(v.getContext());
            activity.OrderOneDish();
            
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
        
    }

    //修改用于插入新点菜品到已点列表，这里只放入List，菜点好了，下单时再存数据库
    public void OrderOneDish() {
      DataItem dataItem = new DataItem();
      String tableno = ((DishApplication)getApplicationContext()).currTableNo;
      
      //生成或得到OrderListId
      String orderlistid = "";
      if (((DishApplication)getApplicationContext()).orderdetailList.size() > 0) {
        orderlistid = ((DishApplication)getApplicationContext()).orderdetailList.get(0).orderlist_id;
      }
      else {
        orderlistid = tableno + "_" + Long.toString(System.currentTimeMillis());
      }
      
      DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(dish_id, orderlistid, mixture_id, taste, cookie, weight
                                                                         , DataItem.OrderDetailItem.STATUS_UNCONFIRMED, tableno, dish_num);
      ((DishApplication)getApplicationContext()).orderdetailList.add(detailItem);
    }
  
}