package com.infolands.dishorder;


import com.infolands.dishorder.DataItem.OrderDetailItem;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


public class OrderActivity extends Activity {
    
  private String dish_id;
  private String orderlist_id;
  private String mixture_id;
  private int dish_num;
  private String taste;
  private String cookie;
  private String weight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        
        Button orderBtn = (Button) findViewById(R.id.orderbt);
        orderBtn.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            new Thread() {

              public void run() {
                
                //保存点的菜品
                OrderOneDish();
                
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
        
        CheckBox flagView = (CheckBox) v.findViewById(R.id.subflag);
    }

    //修改用于插入新点菜品到已点列表，这里只放入List，菜点好了，下单时再存数据库
    public void OrderOneDish(String dishid, String mixtureid, String t, String c, String w, String st, int dishnum) {
      DataItem dataItem = new DataItem();
      String tableno = ((DishApplication)getApplicationContext()).currTableNo;
      
      String orderlistid = "";
      if (((DishApplication)getApplicationContext()).orderdetailList.size() > 0) {
        orderlistid = ((DishApplication)getApplicationContext()).orderdetailList.get(0).orderlist_id;
      }
      else {
        orderlistid = tableno + "_" + Long.toString(System.currentTimeMillis());
      }
      DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(dishid, orderlistid, mixtureid, t, c, w, tableno, st, dishnum);
      ((DishApplication)getApplicationContext()).orderdetailList.add(detailItem);
    }
  
}