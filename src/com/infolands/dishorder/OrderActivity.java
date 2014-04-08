package com.infolands.dishorder;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;




public class OrderActivity extends Activity {
    
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
                insertOrderedItem();
                
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
  
  

  //修改用于插入新点菜品到已点列表，最好是这里只放入List，菜点好了，下单时再存数据库
  public long insertOrderedItem(String address,String position) {
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_POI_ADDRESS, address);
    initialValues.put(KEY_POI_POSITION, position);
    return mDb.insert(ADDRESS_TABLE_NAME, null, initialValues);
  }
  
}