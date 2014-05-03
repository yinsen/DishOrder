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

import java.util.ArrayList;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DishApplication extends Application {

  public enum app_mode {
    MODE_CUSTOMER,
    MODE_WAITOR,
    MODE_ADMIN,
    
    MODE_NONE
  }
  public app_mode currMode = app_mode.MODE_CUSTOMER;
  public String currMenu = "";
  public String currSubMenu = "";
  
  public DataItem item = new DataItem();
  public DataItem.OrderDetailItem currOrderDetailItem = item.new OrderDetailItem();
  
  public String currTableNo;
  public ArrayList<DataItem.DiningItem> diningList = new ArrayList<DataItem.DiningItem>();
  public ArrayList<DataItem.WaitorItem> waitorList = new ArrayList<DataItem.WaitorItem>();
  public ArrayList<DataItem.MenuItem> menuList = new ArrayList<DataItem.MenuItem>();
  public ArrayList<DataItem.SubmenuItem> submenuList = new ArrayList<DataItem.SubmenuItem>();
  public ArrayList<DataItem.DishItem> dishList = new ArrayList<DataItem.DishItem>();
  public ArrayList<DataItem.MixtureItem> mixtureList = new ArrayList<DataItem.MixtureItem>();
  
  public ArrayList<DataItem.OrderListItem> orderlistList = new ArrayList<DataItem.OrderListItem>();
  public ArrayList<DataItem.OrderDetailItem> orderdetailList = new ArrayList<DataItem.OrderDetailItem>();
  
  public void setCurrMenu(String m) {
    currMenu = m;
  }

  public String getCurrMenu() {
    return currMenu;
  }

  public void setCurrSubMenu(String sm) {
    currSubMenu = sm;
  }

  public String getCurrSubMenu() {
    return currSubMenu;
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
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID, orderdetailList.get(i).dish_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID, orderdetailList.get(i).orderlist_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID, orderdetailList.get(i).mixture_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM, orderdetailList.get(i).dish_num);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE, orderdetailList.get(i).taste);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE, orderdetailList.get(i).cookie);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT, orderdetailList.get(i).weight);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS, orderdetailList.get(i).status);
    }
    writeSession.insert(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, null, values);
    
    writeSession.close();
    orderdetailList.clear();
    
    
    //3. 将当前桌子已点菜品从SQLite中读入到List
    currTableNo = tn;
    
    SQLiteDatabase readSession = dbHelper.getReadableDatabase();
    Cursor cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, 
                                      new String[]{DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT
                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS}, 
                                      "table_no=?", new String[]{currTableNo}, null, null, null);
    DataItem dataItem = new DataItem();
    while (cursor.moveToNext()) {
      String dish_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID));
      String orderlist_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID));
      String mixture_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID));
      
      int dish_num = cursor.getInt(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM));
      String taste = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE));
      String cookie = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE));
      String weight = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT));
      String status = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS));
      DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(dish_id, orderlist_id, mixture_id, taste, cookie, weight, currTableNo, status, dish_num);
      orderdetailList.add(detailItem);
    }
    cursor.close();
    readSession.close();
  }

  public String getCurrTableNo() {
    return currTableNo;
  }
  
  
  private void createTestTables() {
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    
    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();
    // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致 
    ContentValues values = new ContentValues();

    writeSession.delete(DishOrderDatabaseHelper.TABLE_DINING, null, null);
    for (int i = 0; i < 10; i++) {
      values.put(DishOrderDatabaseHelper.COLUMN_DINING_ID, "dining_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_DINING_NAME, "dining_name_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_DINING_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_DINING, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_WAITOR, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_ID, "waitor_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_NAME, "waitor_name_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_WAITOR, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_MENU, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_ID, "menu_id_1"+i);
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_NAME, "menu_name_1"+i);
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_IMG, "d3");
      writeSession.insert(DishOrderDatabaseHelper.TABLE_MENU, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_SUBMENU, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_ID, "submenu_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_NAME, "submenu_name_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_MENU_ID, "menu_id_11");
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_SUBMENU, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_DISH, null, null);
    for (int i = 0; i < 10; i++) {
      for (int j=i; j<10; j++ ) {
        values.clear();
        values.put(DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU, "submenu_id_" + i + 1);
        values.put(DishOrderDatabaseHelper.COLUMN_DISH_MENU, "menu_id_11");
        values.put(DishOrderDatabaseHelper.COLUMN_DISH_ID, "dish_id_"+ i + "_" + j + 1);
        values.put(DishOrderDatabaseHelper.COLUMN_DISH_NAME, "dish_name_" + j + 1);
        values.put(DishOrderDatabaseHelper.COLUMN_DISH_PRICE, "price_" + 120);
        values.put(DishOrderDatabaseHelper.COLUMN_DISH_IMG, "d" + (j+2));
        writeSession.insert(DishOrderDatabaseHelper.TABLE_DISH, null, values);
      }
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_MIXTURE, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_MIXTURE_ID, i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_NAME, "mixture_name_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_MIXTURE_PRICE, "price_" + 10);
      values.put(DishOrderDatabaseHelper.COLUMN_MIXTURE_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_MIXTURE, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_ORDERLIST, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID, "list_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE, "price_" + 10);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID, "waitor_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO, "table_no_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_ORDERLIST, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID, "dish_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID, "list_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID, "mixture_id" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT, "big");
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE, "taste");
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE, "cookie");
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM, 2);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, null, values);
    }
    
    writeSession.close();
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
    Cursor cursor = readSession.query(DishOrderDatabaseHelper.TABLE_DINING, new String[]{DishOrderDatabaseHelper.COLUMN_DINING_ID
                                                                                       , DishOrderDatabaseHelper.COLUMN_DINING_NAME
                                                                                       , DishOrderDatabaseHelper.COLUMN_DINING_IMG}, "", null, null, null, null);
    // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false  
    DataItem dataItem = new DataItem();
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DINING_ID));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DINING_NAME));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DINING_IMG));
      DataItem.DiningItem diningItem = dataItem.new DiningItem(id, name, img);
      diningList.add(diningItem);
    }
    cursor.close();
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_WAITOR, new String[]{DishOrderDatabaseHelper.COLUMN_WAITOR_ID
                                                                                , DishOrderDatabaseHelper.COLUMN_WAITOR_NAME
                                                                                , DishOrderDatabaseHelper.COLUMN_WAITOR_IMG}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_ID));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_NAME));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_IMG));
      DataItem.WaitorItem waitorItem =  dataItem.new WaitorItem(id, name, img);
      waitorList.add(waitorItem);
    }
    cursor.close();
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_MENU, new String[]{DishOrderDatabaseHelper.COLUMN_MENU_ID
                                                                              , DishOrderDatabaseHelper.COLUMN_MENU_NAME
                                                                              , DishOrderDatabaseHelper.COLUMN_MENU_IMG}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MENU_ID));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MENU_NAME));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MENU_IMG));
      DataItem.MenuItem menuItem =  dataItem.new MenuItem(id, name, img);
      menuList.add(menuItem);
    }
    cursor.close();
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_SUBMENU, new String[]{DishOrderDatabaseHelper.COLUMN_SUBMENU_ID
                                                                                 , DishOrderDatabaseHelper.COLUMN_SUBMENU_NAME
                                                                                 , DishOrderDatabaseHelper.COLUMN_SUBMENU_MENU_ID
                                                                                 , DishOrderDatabaseHelper.COLUMN_SUBMENU_IMG}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_SUBMENU_ID));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_SUBMENU_NAME));
      String menu_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_SUBMENU_MENU_ID));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_SUBMENU_IMG));
      DataItem.SubmenuItem submenuItem =  dataItem.new SubmenuItem(id, name, menu_id, img);
      submenuList.add(submenuItem);
    }
    cursor.close();
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_DISH, new String[]{DishOrderDatabaseHelper.COLUMN_DISH_ID
                                                                              , DishOrderDatabaseHelper.COLUMN_DISH_NAME
                                                                              , DishOrderDatabaseHelper.COLUMN_DISH_PRICE
                                                                              , DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU
                                                                              , DishOrderDatabaseHelper.COLUMN_DISH_MENU
                                                                              , DishOrderDatabaseHelper.COLUMN_DISH_IMG}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_ID));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_NAME));
      String price = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_PRICE));
      String submenu_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU));
      String menu_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_MENU));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_IMG));
      DataItem.DishItem dishItem =  dataItem.new DishItem(id, name, price, submenu_id, menu_id, img);
      dishList.add(dishItem);
    }
    cursor.close();
    
    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_MIXTURE, new String[]{DishOrderDatabaseHelper.COLUMN_MIXTURE_ID
                                                                                 , DishOrderDatabaseHelper.COLUMN_MIXTURE_NAME
                                                                                 , DishOrderDatabaseHelper.COLUMN_MIXTURE_PRICE
                                                                                 , DishOrderDatabaseHelper.COLUMN_MIXTURE_IMG}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MIXTURE_ID));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MIXTURE_NAME));
      String price = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MIXTURE_PRICE));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_MIXTURE_IMG));
      DataItem.MixtureItem mixtureItem =  dataItem.new MixtureItem(id, name, price, img);
      mixtureList.add(mixtureItem);
    }
    cursor.close();
    readSession.close();
  }
  @Override
  public void onCreate() {

    createTestTables();
    initDataSet();
  }
  
  public void updateDateDisplay() {
    

  }

//  public ArrayList<DataItem.DishItem> getDishList() {
//
//    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
//    SQLiteDatabase mDb = dbHelper.getReadableDatabase();
//
//    ArrayList<DataItem.DishItem> results = new ArrayList<DataItem.DishItem>();
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
