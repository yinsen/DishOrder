/*
 * Copyright (C) 2007 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.infolands.dishorder;

import java.util.Vector;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.infolands.dishorder.DataItem.DiningItem;
import com.infolands.dishorder.DataItem.DishItem;

public class DishApplication extends Application {

  public String currMenu = "";
  public String currSubMenu = "";
  
  public DataItem item = new DataItem();
  public DataItem.OrderDetailItem currOrderDetailItem = item.new OrderDetailItem();
  
  public String currTableNo;
  public Vector<DataItem.DiningItem> diningList = new Vector<DataItem.DiningItem>();
  public Vector<DataItem.WaitorItem> waitorList = new Vector<DataItem.WaitorItem>();
  public Vector<DataItem.MenuItem> menuList = new Vector<DataItem.MenuItem>();
  public Vector<DataItem.SubmenuItem> submenuList = new Vector<DataItem.SubmenuItem>();
  public Vector<DataItem.DishItem> dishList = new Vector<DataItem.DishItem>();
  public Vector<DataItem.MixtureItem> mixtureList = new Vector<DataItem.MixtureItem>();
  
  public Vector<DataItem.OrderListItem> orderlistList = new Vector<DataItem.OrderListItem>();
  public Vector<DataItem.OrderDetailItem> orderdetailList = new Vector<DataItem.OrderDetailItem>();
  
  public void setCurrMenu(String m) {
    currMenu = m;
  }

  public String getCurrMenu() {
    return currMenu;
  }

  public void setCurrSubMenu(String sm) {
    currSubMenu = sm;
  }

  public DataItem.OrderDetailItem getCurrOrderDetailItem() {
    return currOrderDetailItem;
  }
  
  public void setCurrOrderDetailItem(String dishId) {
    currOrderDetailItem.dish_id = dishId;
    currOrderDetailItem.cookie = "";
    currOrderDetailItem.mixture_id = "";
    currOrderDetailItem.taste = "";
    currOrderDetailItem.weight = "";
    currOrderDetailItem.dish_num = 0;
    currOrderDetailItem.status = DataItem.OrderDetailItem.STATUS_UNCONFIRMED;
  }

  public String getCurrSubMenu() {
    return currSubMenu;
  }
  
  //orderdetail表中只存储没有结帐的桌子的菜品列表，因同一桌子同时只会有一单未结帐，所以只需要以table_no区分，不需要orderList_no
  public void setCurrTableNo(String tn) {
    
    if (currTableNo.equalsIgnoreCase(tn)) {
      return;
    }
    
    //1. 清除当前菜品的配置数据，恢复为默认值，防止之前桌子的点菜动作影响当前桌子
    setCurrOrderDetailItem("0");
    
    
    //2. 将之前桌子已点菜品存入SQLite
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();

    for (int i = 0; i < orderdetailList.size(); i++) {
      values.put("dish_id", orderdetailList.get(i).dish_id);
      values.put("orderlist_id", orderdetailList.get(i).orderlist_id);
      values.put("mixture_id", orderdetailList.get(i).mixture_id);
      values.put("dish_num", orderdetailList.get(i).dish_num);
      values.put("taste", orderdetailList.get(i).taste);
      values.put("cookie", orderdetailList.get(i).cookie);
      values.put("weight", orderdetailList.get(i).weight);
      values.put("status", orderdetailList.get(i).status);
    }
    writeSession.insert(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, null, values);
    orderdetailList.clear();
    
    
    //3. 将当前桌子已点菜品从SQLite中读入到List
    currTableNo = tn;
    
    SQLiteDatabase readSession = dbHelper.getReadableDatabase();
    Cursor cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, 
                                      new String[]{"dish_id", "orderlist_id", "mixture_id", "dish_num", "taste", "cookie", "weight", "status"}, 
                                      "table_no=?", new String[]{currTableNo}, null, null, null);
    DataItem dataItem = new DataItem();
    while (cursor.moveToNext()) {
      String dish_id = cursor.getString(cursor.getColumnIndex("dish_id"));
      String orderlist_id = cursor.getString(cursor.getColumnIndex("orderlist_id"));
      String mixture_id = cursor.getString(cursor.getColumnIndex("mixture_id"));
      
      int dish_num = cursor.getInt(cursor.getColumnIndex("dish_num"));
      String taste = cursor.getString(cursor.getColumnIndex("taste"));
      String cookie = cursor.getString(cursor.getColumnIndex("cookie"));
      String weight = cursor.getString(cursor.getColumnIndex("weight"));
      String status = cursor.getString(cursor.getColumnIndex("status"));
      DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(dish_id, orderlist_id, mixture_id, taste, cookie, weight, currTableNo, status, dish_num);
      orderdetailList.add(detailItem);
    }
  }

  public String getCurrTableNo() {
    return currTableNo;
  }
  
  
  private void createTestTables() {
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);

    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();
    // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致 
    ContentValues values = new ContentValues();

    for (int i = 0; i < 10; i++) {
      values.put("id", "dining_id_" + i + 1);
      values.put("name", "dining_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("dining", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("id", "waitor_id_" + i + 1);
      values.put("name", "waitor_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("waitor", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("id", "menu_id_" + i + 1);
      values.put("name", "menu_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("menu", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("id", "submenu_id_" + i + 1);
      values.put("name", "submenu_name_" + i + 1);
      values.put("menu_id", "menu_id" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("submenu", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("id", i + 1);
      values.put("name", "dish_name_" + i + 1);
      values.put("price", "price_" + 120);
      values.put("submenu_id", "submenu_id_" + i + 1);
      values.put("menu_id", "menu_id_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("dish", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("id", i + 1);
      values.put("name", "mixture_name_" + i + 1);
      values.put("price", "price_" + 10);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("mixture", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("id", "list_id_" + i + 1);
      values.put("price", "price_" + 10);
      values.put("waitor_id", "waitor_" + i + 1);
      values.put("table_no", "table_no_" + i + 1);
    }
    writeSession.insert("orderlist", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("dish_id", "dish_id_" + i + 1);
      values.put("list_id", "list_id_" + i + 1);
      values.put("mixture_id", "mixture_id" + i + 1);
      values.put("weight", "big");
      values.put("taste", "taste");
      values.put("cookie", "cookie");
      values.put("dish_num", 2);
    }
    writeSession.insert("orderdetail", null, values);
  }

  private void initDataSet(){
    
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase readSession = dbHelper.getReadableDatabase();
    // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象  
    // 第一个参数String：表名  
    // 第二个参数String[]:要查询的列名  
    // 第三个参数String：查询条件  
    // 第四个参数String[]：查询条件的参数  
    // 第五个参数String:对查询的结果进行分组  
    // 第六个参数String：对分组的结果进行限制  
    // 第七个参数String：对查询的结果进行排序  
    Cursor cursor = readSession.query(DishOrderDatabaseHelper.TABLE_DINING, new String[]{"id", "name", "img"}, "", null, null, null, null);
    // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false  
    DataItem dataItem = new DataItem();
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex("id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.DiningItem diningItem = dataItem.new DiningItem(id, name, img);
      diningList.add(diningItem);
    }
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_WAITOR, new String[]{"id", "name", "img"}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex("id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.WaitorItem waitorItem =  dataItem.new WaitorItem(id, name, img);
      waitorList.add(waitorItem);
    }
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_MENU, new String[]{"id", "name", "img"}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex("id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.MenuItem menuItem =  dataItem.new MenuItem(id, name, img);
      menuList.add(menuItem);
    }
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_SUBMENU, new String[]{"id", "name", "menu_id", "img"}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex("id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String menu_id = cursor.getString(cursor.getColumnIndex("menu_id"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.SubmenuItem submenuItem =  dataItem.new SubmenuItem(id, name, menu_id, img);
      submenuList.add(submenuItem);
    }
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_DISH, new String[]{"id", "name", "price", "img"}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex("id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String price = cursor.getString(cursor.getColumnIndex("price"));
      String submenu_id = cursor.getString(cursor.getColumnIndex("submenu_id"));
      String menu_id = cursor.getString(cursor.getColumnIndex("menu_id"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.DishItem dishItem =  dataItem.new DishItem(id, name, price, submenu_id, menu_id, img);
      dishList.add(dishItem);
    }
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_MIXTURE, new String[]{"id", "name", "price", "img"}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex("id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String price = cursor.getString(cursor.getColumnIndex("price"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.MixtureItem mixtureItem =  dataItem.new MixtureItem(id, name, price, img);
      mixtureList.add(mixtureItem);
    }
    
  }
  @Override
  public void onCreate() {
    createTestTables();

    initDataSet();
  }
  
  public void updateDateDisplay() {
    

  }

//  public Vector<DataItem.DishItem> getDishList() {
//
//    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
//    SQLiteDatabase mDb = dbHelper.getReadableDatabase();
//
//    Vector<DataItem.DishItem> results = new Vector<DataItem.DishItem>();
//
//    String[] selectionArgs = new String[]{currSubMenu, currMenu};
//    Cursor cursor = mDb.rawQuery("select * from table " + DishOrderDatabaseHelper.TABLE_DISH + " where "
//        + DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU + "=?" + " AND " + DishOrderDatabaseHelper.COLUMN_DISH_MENU
//        + "=?", selectionArgs);
//    cursor.moveToFirst();
//
//    DataItem dataItem = new DataItem();
//    while (cursor.getPosition() != cursor.getCount()) {
//      DataItem.DishItem item = dataItem.new DishItem();
//      item.dish_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_ID));
//      item.name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_NAME));
//      item.price = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_PRICE));
//      results.add(item);
//      cursor.moveToNext();
//    }
//    cursor.close();
//    return results;
//  }

  @Override
  public void onTerminate() {
  }
}
