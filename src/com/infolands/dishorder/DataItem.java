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

    public SubmenuItem() {
    }
    public SubmenuItem(String no, String n) {
      submenu_id = no;
      name = n;
    }
  }
  
  public class DishItem {

    public String dish_id;
    public String name;
    public String price;
    public String img;

    public DishItem() {
    }
    
    public DishItem(String no, String n, String p, String imgPath) {
      dish_id = no;
      name = n;
      price = p;
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
}
