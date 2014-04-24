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

  private String currMenu;
  private String currSubMenu;
  private Vector<DataItem.DiningItem> diningList;
  private Vector<DataItem.WaitorItem> waitorList;
  private Vector<DataItem.MenuItem> menuList;
  private Vector<DataItem.SubmenuItem> submenuList;
  private Vector<DataItem.DishItem> dishList;
  private Vector<DataItem.MixtureItem> mixtureList;
  
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

  private void createTables() {
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);

    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();
    // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致 
    ContentValues values = new ContentValues();

    for (int i = 0; i < 10; i++) {
      values.put("dining_id", "dining_id_" + i + 1);
      values.put("name", "dining_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("dining", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("waitor_id", "waitor_id_" + i + 1);
      values.put("name", "waitor_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("waitor", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("menu_id", "menu_id_" + i + 1);
      values.put("name", "menu_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("menu", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("submenu_id", "submenu_id_" + i + 1);
      values.put("name", "submenu_name_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("submenu", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("dish_id", i + 1);
      values.put("name", "dish_name_" + i + 1);
      values.put("price", "price_" + 120);
      values.put("submenu_id", "submenu_id_" + i + 1);
      values.put("menu_id", "menu_id_" + i + 1);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("dish", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("mixture_id", i + 1);
      values.put("name", "mixture_name_" + i + 1);
      values.put("price", "price_" + 10);
      values.put("img", "img_path_" + i + 1);
    }
    writeSession.insert("mixture", null, values);

    values.clear();
    for (int i = 0; i < 10; i++) {
      values.put("list_id", "list_id_" + i + 1);
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
      String dining_id = cursor.getString(cursor.getColumnIndex("dining_id"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String img = cursor.getString(cursor.getColumnIndex("img"));
      DataItem.DiningItem diningItem =  dataItem.new DiningItem(dining_id, name, img);
      diningList.add(diningItem);
    }
  }
  @Override
  public void onCreate() {
    createTables();

    initDataSet();
    
    readDining();
    readWaitor();
    
    readMenus();
    readSubmenus();
    readDishs();
    readMixture();
    
  }

  public Vector<DishItem> getDishList() {

    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase mDb = dbHelper.getReadableDatabase();

    Vector<DishItem> results = new Vector<DishItem>();

    String[] selectionArgs = new String[]{currSubMenu, currMenu};
    Cursor cursor = mDb.rawQuery("select * from table " + DishOrderDatabaseHelper.DISH_TABLE_NAME + " where "
        + DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU + "=?" + " AND " + DishOrderDatabaseHelper.COLUMN_DISH_MENU
        + "=?", selectionArgs);
    cursor.moveToFirst();

    while (cursor.getPosition() != cursor.getCount()) {
      DishItem item = new DishItem();
      item.id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_ID));
      item.name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_NAME));
      item.price = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_PRICE));
      results.add(item);
      cursor.moveToNext();
    }
    cursor.close();
    return results;
  }

  @Override
  public void onTerminate() {
  }
}
