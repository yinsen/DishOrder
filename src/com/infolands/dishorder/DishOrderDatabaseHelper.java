package com.infolands.dishorder;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;




  /**
   * DatabaseHelper extends SQLiteOpenHelper
   */
class DishOrderDatabaseHelper extends SQLiteOpenHelper {
    
    // DB NAME
    public static final String DATABASE_NAME = "dishorderDB";
    public static final int DATABASE_VERSION = 1;

    // DINING TABLES
    protected static final String DINING_TABLE_NAME = "dining";
    // DISH TABLES COLUMNS
    public static final String COLUMN_DINING_ID = "dining_id";
    public static final String COLUMN_DINING_NAME = "name";
    public static final String COLUMN_DINING_IMG = "img";
    private static final String DINING_TABLE_CREATE = "create table if not exists "
        + DINING_TABLE_NAME
        + "("+ COLUMN_DINING_ID + " TEXT primary key, "
        + COLUMN_DINING_NAME + " TEXT not null ," 
        + COLUMN_DINING_IMG + " TEXT not null)";
    
    
    // MENU TABLES
    protected static final String MENU_TABLE_NAME = "menu";
    // DISH TABLES COLUMNS
    public static final String COLUMN_MENU_ID = "menu_id";
    public static final String COLUMN_MENU_NAME = "name";
    public static final String COLUMN_MENU_IMG = "img";
    private static final String MENU_TABLE_CREATE = "create table if not exists "
        + MENU_TABLE_NAME
        + "("+ COLUMN_MENU_ID + " TEXT primary key, "
        + COLUMN_MENU_NAME + " TEXT not null ," 
        + COLUMN_MENU_IMG + " TEXT not null)";
    
    // SUBMENU TABLES
    protected static final String SUBMENU_TABLE_NAME = "submenu";
    // DISH TABLES COLUMNS
    public static final String COLUMN_SUBMENU_ID = "submenu_id";
    public static final String COLUMN_SUBMENU_NAME = "name";
    public static final String COLUMN_SUBMENU_IMG = "img";
    private static final String SUBMENU_TABLE_CREATE = "create table if not exists "
        + SUBMENU_TABLE_NAME
        + "("+ COLUMN_SUBMENU_ID + " TEXT primary key, "
        + COLUMN_SUBMENU_NAME + " TEXT not null ," 
        + COLUMN_SUBMENU_IMG + " TEXT not null)";
    
    
    // DISH TABLES
    protected static final String DISH_TABLE_NAME = "dish";
    // DISH TABLES COLUMNS
    public static final String COLUMN_DISH_ID = "dish_id";
    public static final String COLUMN_DISH_NAME = "name";
    public static final String COLUMN_DISH_PRICE = "price";
    public static final String COLUMN_DISH_SUBMENU = "submenu";
    public static final String COLUMN_DISH_MENU = "menu";
    public static final String COLUMN_DISH_IMG = "img";
    private static final String DISH_TABLE_CREATE = "create table if not exists "
        + DISH_TABLE_NAME
        + "("+ COLUMN_DISH_ID + " TEXT primary key, "
        + COLUMN_DISH_NAME + " TEXT not null ," 
        + COLUMN_DISH_PRICE + " TEXT not null ," 
        + COLUMN_DISH_SUBMENU + " TEXT not null ," 
        + COLUMN_DISH_MENU + " TEXT not null ," 
        + COLUMN_DISH_IMG + " TEXT not null)";
    
    protected static final String ORDERLIST_TABLE_NAME = "orderlist";
    public static final String COLUMN_ORDERLIST_ID = "list_id";
    public static final String COLUMN_ORDERLIST_TOTAL_PRICE = "price";
    public static final String COLUMN_ORDERLIST_WAITOR_ID = "waitorid";
    private static final String ORDERLIST_TABLE_CREATE = "create table if not exists "
        + ORDERLIST_TABLE_NAME
        + "("+ COLUMN_ORDERLIST_ID + " TEXT primary key, "
        + COLUMN_ORDERLIST_TOTAL_PRICE + " integer not null ," 
        + COLUMN_ORDERLIST_WAITOR_ID + " TEXT not null)";

    // ORDERDISHS TABLES
    protected static final String ORDERDISHS_TABLE_NAME = "orderdishs";
    // DISH TABLES COLUMNS
    public static final String COLUMN_ORDERDISHS_DISHNUM = "dish_num";
    public static final String COLUMN_ORDERDISHS_TABLE_NO = "table_no";
    public static final String COLUMN_ORDERDISHS_TASTE = "taste";
    public static final String COLUMN_ORDERDISHS_MIXTURE = "mixture";
    public static final String COLUMN_ORDERDISHS_COOKIE = "cookie";
    public static final String COLUMN_ORDERDISHS_WEIGHT = "weight";
    private static final String ORDERDISHS_TABLE_CREATE = "create table if not exists "
        + ORDERDISHS_TABLE_NAME
        + "("+ COLUMN_DISH_ID + " TEXT primary key, "
        + COLUMN_ORDERLIST_ID + " TEXT primary key," 
        + COLUMN_ORDERDISHS_DISHNUM + " integer not null ," 
        + COLUMN_ORDERDISHS_TABLE_NO + " integer not null ," 
        + COLUMN_ORDERDISHS_TASTE + " TEXT not null ," 
        + COLUMN_ORDERDISHS_MIXTURE + " TEXT not null ," 
        + COLUMN_ORDERDISHS_COOKIE + " TEXT not null ," 
        + COLUMN_ORDERDISHS_WEIGHT + " TEXT not null)";
    
    /** 
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数 
     * @param context   上下文对象 
     * @param name      数据库名称 
     * @param factory 
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态 
     */  
    public DishOrderDatabaseHelper(Context context) {  
        //必须通过super调用父类当中的构造函数  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    } 

    @Override
    public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub  
      System.out.println("create a database dish");  
      //execSQL用于执行SQL语句  
      db.execSQL(DISH_TABLE_CREATE);
      db.execSQL(ORDERLIST_TABLE_CREATE);
      db.execSQL(ORDERDISHS_TABLE_CREATE);
    }

    @Override  
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {  
        // TODO Auto-generated method stub  
        System.out.println("upgrade a database");  
    }
    
    
}

