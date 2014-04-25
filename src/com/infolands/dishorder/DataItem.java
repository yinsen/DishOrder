package com.infolands.dishorder;

public class DataItem {
  
  public class DiningItem {

    public String dining_id;
    public String name;
    public String img;

    public DiningItem() {
    }
    public DiningItem(String no, String n, String imgPath) {
      dining_id = no;
      name = n;
      img = imgPath;
    }
  }
  
  public class WaitorItem {

    public String waitor_id;
    public String name;
    public String img;

    public WaitorItem() {
    }
    public WaitorItem(String no, String n, String imgPath) {
      waitor_id = no;
      name = n;
      img = imgPath;
    }
  }
  
  public class MenuItem {

    public String menu_id;
    public String name;
    public String img;

    public MenuItem() {
    }
    public MenuItem(String no, String n, String imgPath) {
      menu_id = no;
      name = n;
      img = imgPath;
    }
  }
  
  public class SubmenuItem {

    public String submenu_id;
    public String name;
    public String menu_id;
    public String img;

    public SubmenuItem() {
    }
    public SubmenuItem(String no, String n, String mid, String imgPath) {
      submenu_id = no;
      name = n;
      menu_id = mid;
      img = imgPath;
    }
  }
  
  public class DishItem {

    public String dish_id;
    public String name;
    public String price;
    public String img;
    public String submenu_id;
    public String menu_id;

    public DishItem() {
    }
    
    public DishItem(String no, String n, String p, String sid, String mid, String imgPath) {
      dish_id = no;
      name = n;
      price = p;
      submenu_id = sid;
      menu_id = mid;
      img = imgPath;
    }
  }
  
  public class MixtureItem {

    public String mixture_id;
    public String name;
    public String price;
    public String img;

    public MixtureItem() {
    }
    
    public MixtureItem(String no, String n, String p, String imgPath) {
      mixture_id = no;
      name = n;
      price = p;
      img = imgPath;
    }
  }
  
  public class OrderListItem {

    public String orderlist_id;
    public String waitor_id;
    public String total_price;
    public String table_no;
    public String listStatus;

    public OrderListItem() {
    }
    
    public OrderListItem(String no, String tableno, String totalprice, String wid, String st) {
      orderlist_id = no;
      table_no = tableno;
      total_price = totalprice;
      waitor_id = wid;
      listStatus = st;
    }
  }
  
  public class OrderDetailItem {

    public String dish_id;
    public String orderlist_id;
    public String mixture_id;
    public int dish_num;
    public String taste;
    public String cookie;
    public String weight;

    public OrderDetailItem() {
    }
    
    public OrderDetailItem(String dishid, String orderlistid, String mixtureid, String t, String c, String w, int dishnum) {
      dish_id = dishid;
      orderlist_id = orderlistid;
      mixture_id = mixtureid;
      taste = t;
      cookie = c;
      weight = w;
      dish_num = dishnum;
    }
  }
}
