package com.infolands.dishorder;


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
        
        
        CheckBox flagView = (CheckBox) v.findViewById(R.id.subflag);
    }
  
  

  //修改用于插入新点菜品到已点列表，这里只放入List，菜点好了，下单时再存数据库
  public long insertOrderedItem(String address,String position) {
    
    orderdetailList
    // 创建一个DatabaseHelper对象  
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(OrderActivity.this);  
    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();  
    ContentValues values = new ContentValues();
    values.put(DishOrderDatabaseHelper.COLUMN_DISH_ID, address);
    values.put(KEY_POI_POSITION, position);
    writeSession.insert("dining", null, values);
    
    return mDb.insert(ADDRESS_TABLE_NAME, null, initialValues);
  }
  
}