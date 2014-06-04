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

import com.infolands.dishorder.DataItem.OrderDetailItem;
import com.infolands.dishorder.DataItem.OrderListItem;

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

  public static final String STATUS_UNCONFIRMED = "unconfirmed";
  public static final String STATUS_CONFIRMED = "confirmed";
  public static final String STATUS_PAYED = "payed";
  
  public app_mode currMode = app_mode.MODE_CUSTOMER;
  private String currTableNo = "100";
  public String currWaitorId = "";

  public String currMenu = "";
  public String currSubMenu = "";
  public String currDishId = "";
  
  public ArrayList<DataItem.DiningItem> diningList = new ArrayList<DataItem.DiningItem>();
  public ArrayList<DataItem.WaitorItem> waitorList = new ArrayList<DataItem.WaitorItem>();
  public ArrayList<DataItem.MenuItem> menuList = new ArrayList<DataItem.MenuItem>();
  public ArrayList<DataItem.SubmenuItem> submenuList = new ArrayList<DataItem.SubmenuItem>();
  public ArrayList<DataItem.DishItem> dishList = new ArrayList<DataItem.DishItem>();
  public ArrayList<DataItem.MixtureItem> mixtureList = new ArrayList<DataItem.MixtureItem>();
  
  public ArrayList<DataItem.OrderDetailItem> orderdetailList = new ArrayList<DataItem.OrderDetailItem>();
  public ArrayList<DataItem.OrderListItem> orderlistList = new ArrayList<DataItem.OrderListItem>();
  
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
  
  public String getCurrDishId() {
    return currDishId;
  }
  
  public void setCurrDishId(String dishId) {
    currDishId = dishId;
  }

  public String getCurrTableNo() {
    return currTableNo;
  }
  
  public void saveOrderedData(){
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();

    //2.1 存detaillist
    for (int i = 0; i < orderdetailList.size(); i++) {
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID, orderdetailList.get(i).dish_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID, orderdetailList.get(i).orderlist_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID, orderdetailList.get(i).mixture_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM, orderdetailList.get(i).dish_num);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE, orderdetailList.get(i).taste);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE, orderdetailList.get(i).cookie);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT, orderdetailList.get(i).weight);
//      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS, orderdetailList.get(i).status);
//      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TABLE_NO, orderdetailList.get(i).table_no);
//      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WAITOR_ID, orderdetailList.get(i).waitor_id);
//      values.put(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TOTAL_PRICE, orderdetailList.get(i).total_price);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, null, values);
      values.clear();
    }
    
    //2.2存orderlist（只有1条）
    for (int i = 0; i < orderlistList.size(); i++) {
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID, orderlistList.get(i).orderlist_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE, orderlistList.get(i).total_price);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID, orderlistList.get(i).waitor_id);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO, orderlistList.get(i).table_no);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS, orderlistList.get(i).status);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_ORDERLIST, null, values);
      values.clear();
    }
    
    writeSession.close();
  }
  
  public void loadOrderedData(){
    DataItem dataItem = new DataItem();
    
    //3.1 读listItem（只有1条）
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase readSession = dbHelper.getReadableDatabase();
    Cursor cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERLIST, 
        new String[]{DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS}, 
                   DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO+"=?"+" AND "+DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS+"=?", 
                   new String[]{currTableNo, STATUS_UNCONFIRMED}, null, null, null);
    //没有未下单的单子，就load已下单，但未结帐的列表
    if (cursor.getCount()<=0) {
      cursor.close();
      cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERLIST, 
          new String[]{DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS}, 
                     DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO+"=?"+" AND "+DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS+"=?", 
                     new String[]{currTableNo, STATUS_CONFIRMED}, null, null, null);
    }
    while (cursor.moveToNext()) {//正确的话，这里只有一条记录
      String orderlist_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID));
      String status = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS));
      String wid = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID));
      String tp = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE));
      DataItem.OrderListItem listItem = dataItem.new OrderListItem(orderlist_id, currTableNo, tp, wid, status);
      orderlistList.add(listItem);
    }
    cursor.close();
    
    //3.2 读detaillist
    if (orderlistList.size()>0) {
      cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, 
                                        new String[]{DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TABLE_NO
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WAITOR_ID
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TOTAL_PRICE
                                                   }, 
                                                   DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID+"=?", 
                                                   new String[]{orderlistList.get(0).orderlist_id}, null, null, null);
      
      while (cursor.moveToNext()) {
        String dish_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID));
        String orderlist_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID));
        String mixture_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID));
        
        int dish_num = cursor.getInt(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM));
        String taste = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE));
        String cookie = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE));
        String weight = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT));
  //      String status = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS));
  //      String wid = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WAITOR_ID));
  //      String tp = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TOTAL_PRICE));
        DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(dish_id, orderlist_id, mixture_id, taste, cookie, weight, dish_num);
        orderdetailList.add(detailItem);
      }
      cursor.close();
    }
    
    readSession.close();
  }
  
  //orderdetail表中只存储没有结帐的桌子的菜品列表，因同一桌子同时只会有一单未结帐，所以只需要以table_no区分，不需要orderList_no
  public void setCurrTableNo(String tn) {
    
    if (currTableNo.equalsIgnoreCase(tn)) {
      return;
    }
    currTableNo = tn;
    
    //1. 清除当前菜品的配置数据，恢复为默认值，防止之前桌子的点菜动作影响当前桌子
    setCurrDishId("0");
    
    //2. 将之前桌子已点菜品存入SQLite
    saveOrderedData();
    orderlistList.clear();
    orderdetailList.clear();
    
    //3. 将当前桌子已点菜品从SQLite中读入到List
    loadOrderedData();
  }
  
  
  private void insertTestValuesToTables() {
    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase writeSession = dbHelper.getWritableDatabase();

    //========================== for test================================
//    dbHelper.onCreate(writeSession);
//        writeSession.execSQL("drop table " + DishOrderDatabaseHelper.TABLE_ORDERDETAIL);
//        writeSession.execSQL("drop table " + DishOrderDatabaseHelper.TABLE_ORDERLIST);
    //========================== for test================================
    
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
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_ID, "waitor" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_PASSWORD, "123");
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_NAME, "waitor_name_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_WAITOR_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_WAITOR, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_MENU, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_ID, "menu_id_1"+i);
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_NAME, "menu_name_1"+i);
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_IMG, "d2");
      writeSession.insert(DishOrderDatabaseHelper.TABLE_MENU, null, values);
    }
    
    String submenu[]={"川府风味", "酒水", "广东风味", "精品美食", "养生炖汤","时尚凉菜","特色小吃","鲜美热菜","中餐","主食","新品推荐"};
    writeSession.delete(DishOrderDatabaseHelper.TABLE_SUBMENU, null, null);
    for (int i = 0; i < 11; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_ID, "" + i);
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_NAME, submenu[i]);
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_MENU_ID, "menu_id_11");
      values.put(DishOrderDatabaseHelper.COLUMN_SUBMENU_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_SUBMENU, null, values);
    }
    
    String Dish_chuanfu[]={
        "天府印象","硕果累累","牛油果茴香鹅肝","阳光藿香鲶鱼","河帮仔姜田鸡","生嗜排骨煲","天府水煮鱼","干烧野生大黄鱼","小炒黑山羊"
        //,"干捞水晶粉","青花椒牛蛙","清炒河虾仁","葱爆小河虾","美味双脆","四季香锅","干锅灰树菇"
    };
    String Dish_jiushui[]={
        "碧潭飘雪", "血玛丽", "菊花茶", "凯撒白啤", "龙徽干红", "手工花毛峰", "铁观音", "王朝干红"
    };
    writeSession.delete(DishOrderDatabaseHelper.TABLE_DISH, null, null);
    for (int j=0; j<9; j++ ) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU, "" + 0);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_MENU, "menu_id_11");
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_ID, "dish_id_"+0+j);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_NAME, Dish_chuanfu[j]);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_PRICE, "" + (280 + (2-j%4)*20));
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_IMG, "d" + (j%8));
      writeSession.insert(DishOrderDatabaseHelper.TABLE_DISH, null, values);
    }
    for (int j=0; j<8; j++ ) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU, "" + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_MENU, "menu_id_11");
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_ID, "dish_id_"+1+j);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_NAME, Dish_jiushui[j]);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_PRICE, "" + 20);
      values.put(DishOrderDatabaseHelper.COLUMN_DISH_IMG, "j1");
      writeSession.insert(DishOrderDatabaseHelper.TABLE_DISH, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_MIXTURE, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_MIXTURE_ID, i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_MENU_NAME, "mixture_name_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_MIXTURE_PRICE, "" + 10);
      values.put(DishOrderDatabaseHelper.COLUMN_MIXTURE_IMG, "img_path_" + i + 1);
      writeSession.insert(DishOrderDatabaseHelper.TABLE_MIXTURE, null, values);
    }
    
    writeSession.delete(DishOrderDatabaseHelper.TABLE_ORDERLIST, null, null);
    for (int i = 0; i < 10; i++) {
      values.clear();
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID, "list_id_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE, "" + 120);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID, "waitor_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO, "table_no_" + i + 1);
      values.put(DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS, "confirmed");
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
                                                                                , DishOrderDatabaseHelper.COLUMN_WAITOR_PASSWORD
                                                                                , DishOrderDatabaseHelper.COLUMN_WAITOR_NAME
                                                                                , DishOrderDatabaseHelper.COLUMN_WAITOR_IMG}, "", null, null, null, null);
    while (cursor.moveToNext()) {
      String id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_ID));
      String password = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_PASSWORD));
      String name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_NAME));
      String img = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_WAITOR_IMG));
      DataItem.WaitorItem waitorItem =  dataItem.new WaitorItem(id, password, name, img);
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

    cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERLIST, 
        new String[]{DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO
                   , DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS}, 
                   DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO+"=?"+" AND "+DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS+"=?", 
                   new String[]{currTableNo, STATUS_UNCONFIRMED}, null, null, null);
    //没有未下单的单子，就load已下单，但未结帐的列表
    if (cursor.getCount()<=0) {
      cursor.close();
      cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERLIST, 
          new String[]{DishOrderDatabaseHelper.COLUMN_ORDERLIST_ID
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO
                     , DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS}, 
                     DishOrderDatabaseHelper.COLUMN_ORDERLIST_TABLE_NO+"=?"+" AND "+DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS+"=?", 
                     new String[]{currTableNo, STATUS_CONFIRMED}, null, null, null);
    }
    while (cursor.moveToNext()) {//正确的话，这里只有一条记录
      String orderlist_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID));
      String status = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERLIST_STATUS));
      String wid = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERLIST_WAITOR_ID));
      String tp = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERLIST_TOTAL_PRICE));
      DataItem.OrderListItem listItem = dataItem.new OrderListItem(orderlist_id, currTableNo, tp, wid, status);
      orderlistList.add(listItem);
    }
    cursor.close();
    
    if (orderlistList.size()>0) {
      cursor = readSession.query(DishOrderDatabaseHelper.TABLE_ORDERDETAIL, 
                                        new String[]{DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE
                                                   , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TABLE_NO
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WAITOR_ID
  //                                                 , DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TOTAL_PRICE
                                                   }, 
                                                   DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID+"=?", 
                                                   new String[]{orderlistList.get(0).orderlist_id}, null, null, null);
      
      while (cursor.moveToNext()) {
        String dish_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISH_ID));
        String orderlist_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_ORDERLIST_ID));
        String mixture_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_MIXTURE_ID));
        
        int dish_num = cursor.getInt(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_DISHNUM));
        String taste = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TASTE));
        String cookie = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_COOKIE));
        String weight = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WEIGHT));
  //      String status = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_STATUS));
  //      String wid = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_WAITOR_ID));
  //      String tp = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_ORDERDETAIL_TOTAL_PRICE));
        DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(dish_id, orderlist_id, mixture_id, taste, cookie, weight, dish_num);
        orderdetailList.add(detailItem);
      }
      cursor.close();
    }
    
    readSession.close();
    
  }
  

  //修改用于插入新点菜品到已点菜品列表，并更新已点菜单数据结构。这里只放入List，菜点好了，下单时再存数据库
  public void OrderOneDish(String mixtureid, String _taste, String _cookie, String _weight, int dishnum) {
    DataItem dataItem = new DataItem();
    String tableno = getCurrTableNo();
    
    //生成或得到OrderListId
    String orderlistid = "";
    if (((DishApplication)getApplicationContext()).orderlistList.size() > 0) {
      orderlistid = orderlistList.get(0).orderlist_id;
    }
    else {
      orderlistid = tableno + "_" + Long.toString(System.currentTimeMillis());
    }
    DataItem.OrderDetailItem detailItem = dataItem.new OrderDetailItem(currDishId, orderlistid, mixtureid, _taste, _cookie, _weight, dishnum);
    orderdetailList.add(detailItem);
    
    //计算总价
    int total_price = 0;
    if (((DishApplication)getApplicationContext()).orderlistList.size() > 0) {
      total_price = Integer.parseInt(((DishApplication)getApplicationContext()).orderlistList.get(0).total_price);
    }
    for (int j=0; j<((DishApplication)getApplicationContext()).dishList.size(); j++) {
      if (currDishId != null && currDishId.equals(((DishApplication)getApplicationContext()).dishList.get(j).dish_id)) {
        total_price += Integer.parseInt(((DishApplication)getApplicationContext()).dishList.get(j).price);
        break;
      }
    }
    DataItem.OrderListItem listItem = dataItem.new OrderListItem(orderlistid, tableno, Integer.toString(total_price)
                                                                , currWaitorId
                                                                , STATUS_UNCONFIRMED);
    orderlistList.clear();
    orderlistList.add(listItem);
  }
  
  @Override
  public void onCreate() {
  //！！！！下面两行需要移到 数据更新 的处理流程中去！！！！！
    insertTestValuesToTables();
    initDataSet();

  }

  @Override
  public void onTerminate() {
  }
}
