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
    protected static final String TABLE_DINING = "dining";
    // DINING TABLES COLUMNS
    public static final String COLUMN_DINING_ID = "dining_id";
    public static final String COLUMN_DINING_NAME = "name";
    public static final String COLUMN_DINING_IMG = "img";
    private static final String CREATE_TABLE_DINING = "create table if not exists "
        + TABLE_DINING
        + "("+ COLUMN_DINING_ID + " TEXT primary key, "
        + COLUMN_DINING_NAME + " TEXT not null ," 
        + COLUMN_DINING_IMG + " TEXT not null)";
    
    // WAITOR TABLES
    protected static final String TABLE_WAITOR = "waitor";
    // WAITOR TABLES COLUMNS
    public static final String COLUMN_WAITOR_ID = "waitor_id";
    public static final String COLUMN_WAITOR_PASSWORD = "password";
    public static final String COLUMN_WAITOR_NAME = "name";
    public static final String COLUMN_WAITOR_IMG = "img";
    private static final String CREATE_TABLE_WAITOR = "create table if not exists "
        + TABLE_WAITOR
        + "("+ COLUMN_WAITOR_ID + " TEXT primary key, "
        + COLUMN_WAITOR_PASSWORD + " TEXT not null, "
        + COLUMN_WAITOR_NAME + " TEXT not null, " 
        + COLUMN_WAITOR_IMG + " TEXT not null)";
    
    
    // MENU TABLES
    protected static final String TABLE_MENU = "menu";
    // MENU TABLES COLUMNS
    public static final String COLUMN_MENU_ID = "menu_id";
    public static final String COLUMN_MENU_NAME = "name";
    public static final String COLUMN_MENU_IMG = "img";
    private static final String CREATE_TABLE_MENU = "create table if not exists "
        + TABLE_MENU
        + "("+ COLUMN_MENU_ID + " TEXT primary key, "
        + COLUMN_MENU_NAME + " TEXT not null ," 
        + COLUMN_MENU_IMG + " TEXT not null)";
    
    // SUBMENU TABLES
    protected static final String TABLE_SUBMENU = "submenu";
    // SUBMENU TABLES COLUMNS
    public static final String COLUMN_SUBMENU_ID = "submenu_id";
    public static final String COLUMN_SUBMENU_NAME = "name";
    public static final String COLUMN_SUBMENU_MENU_ID = "menu_id";
    public static final String COLUMN_SUBMENU_IMG = "img";
    private static final String CREATE_TABLE_SUBMENU = "create table if not exists "
        + TABLE_SUBMENU
        + "("+ COLUMN_SUBMENU_ID + " TEXT primary key, "
        + COLUMN_SUBMENU_NAME + " TEXT not null ," 
        + COLUMN_SUBMENU_MENU_ID + " TEXT not null ," 
        + COLUMN_SUBMENU_IMG + " TEXT not null ,"
        + "FOREIGN KEY (" +COLUMN_SUBMENU_MENU_ID+ ") REFERENCES TABLE_MENU (COLUMN_MENU_ID))";
    
    // DISH TABLES
    protected static final String TABLE_DISH = "dish";
    // DISH TABLES COLUMNS
    public static final String COLUMN_DISH_ID = "dish_id";
    public static final String COLUMN_DISH_NAME = "name";
    public static final String COLUMN_DISH_PRICE = "price";
    public static final String COLUMN_DISH_SUBMENU = "submenu_id";
    public static final String COLUMN_DISH_MENU = "menu_id";
    public static final String COLUMN_DISH_IMG = "img";
    private static final String CREATE_TABLE_DISH = "create table if not exists "
        + TABLE_DISH
        + "("+ COLUMN_DISH_ID + " TEXT primary key, "
        + COLUMN_DISH_NAME + " TEXT not null ," 
        + COLUMN_DISH_PRICE + " TEXT not null ," 
        + COLUMN_DISH_SUBMENU + " TEXT not null ," 
        + COLUMN_DISH_MENU + " TEXT not null ," 
        + COLUMN_DISH_IMG + " TEXT not null ," 
        + "FOREIGN KEY (" +COLUMN_DISH_SUBMENU +") REFERENCES TABLE_SUBMENU (COLUMN_SUBMENU_ID) ,"
        + "FOREIGN KEY (" +COLUMN_DISH_MENU +") REFERENCES TABLE_MENU (COLUMN_MENU_ID))";
    
    
    // MIXTURE TABLES
    protected static final String TABLE_MIXTURE = "mixture";
    // MIXTURE TABLES COLUMNS
    public static final String COLUMN_MIXTURE_ID = "mixture_id";
    public static final String COLUMN_MIXTURE_NAME = "name";
    public static final String COLUMN_MIXTURE_PRICE = "price";
    public static final String COLUMN_MIXTURE_IMG = "img";
    private static final String CREATE_TABLE_MIXTURE = "create table if not exists "
        + TABLE_MIXTURE
        + "("+ COLUMN_MIXTURE_ID + " TEXT primary key, "
        + COLUMN_MIXTURE_NAME + " TEXT not null ,"
        + COLUMN_MIXTURE_PRICE + " TEXT not null ,"
        + COLUMN_MIXTURE_IMG + " TEXT not null)";
    
    
    // ORDERLIST TABLES
    protected static final String TABLE_ORDERLIST = "orderlist";
    // ORDERLIST TABLES COLUMNS
    public static final String COLUMN_ORDERLIST_ID = "orderlist_id";
    public static final String COLUMN_ORDERLIST_TOTAL_PRICE = "total_price";
    public static final String COLUMN_ORDERLIST_WAITOR_ID = "waitor_id";
    public static final String COLUMN_ORDERLIST_TABLE_NO = "table_no";
    private static final String CREATE_TABLE_ORDERLIST = "create table if not exists "
        + TABLE_ORDERLIST
        + "("+ COLUMN_ORDERLIST_ID + " TEXT primary key, "
        + COLUMN_ORDERLIST_TOTAL_PRICE + " integer not null ,"
        + COLUMN_ORDERLIST_WAITOR_ID + " TEXT not null ,"
        + COLUMN_ORDERLIST_TABLE_NO + " TEXT not null ,"
        + "FOREIGN KEY (" +COLUMN_ORDERLIST_WAITOR_ID +") REFERENCES TABLE_WAITOR (COLUMN_WAITOR_ID))";

    // ORDERDETAIL TABLES
    protected static final String TABLE_ORDERDETAIL = "orderdetail";
    // ORDERDETAIL TABLES COLUMNS
    public static final String COLUMN_ORDERDETAIL_DISH_ID = "dish_id";
    public static final String COLUMN_ORDERDETAIL_ORDERLIST_ID = "orderlist_id";
    public static final String COLUMN_ORDERDETAIL_MIXTURE_ID = "mixture_id";
    public static final String COLUMN_ORDERDETAIL_DISHNUM = "dish_num";
    public static final String COLUMN_ORDERDETAIL_TASTE = "taste";
    public static final String COLUMN_ORDERDETAIL_COOKIE = "cookie";
    public static final String COLUMN_ORDERDETAIL_WEIGHT = "weight";
    public static final String COLUMN_ORDERDETAIL_TABLE_NO = "table_no";
    public static final String COLUMN_ORDERDETAIL_STATUS = "status";
    private static final String CREATE_TABLE_ORDERDETAIL = "create table if not exists "
        + TABLE_ORDERDETAIL
        + "("+ COLUMN_ORDERDETAIL_DISH_ID + " TEXT not null , "
        + COLUMN_ORDERDETAIL_ORDERLIST_ID + " TEXT not null ," 
        + COLUMN_ORDERDETAIL_MIXTURE_ID + " TEXT ,"
        + COLUMN_ORDERDETAIL_WEIGHT + " TEXT ,"
        + COLUMN_ORDERDETAIL_TASTE + " TEXT ," 
        + COLUMN_ORDERDETAIL_COOKIE + " TEXT ,"
        + COLUMN_ORDERDETAIL_TABLE_NO + " TEXT ,"
        + COLUMN_ORDERDETAIL_STATUS + " TEXT ,"
        + COLUMN_ORDERDETAIL_DISHNUM + " integer not null ,"
        + "primary key (" + COLUMN_ORDERDETAIL_DISH_ID + "," + COLUMN_ORDERDETAIL_ORDERLIST_ID +"), " 
        + "FOREIGN KEY (" +COLUMN_ORDERDETAIL_DISH_ID+ ") REFERENCES TABLE_DISH (COLUMN_DISH_ID) ,"
        + "FOREIGN KEY (" +COLUMN_ORDERDETAIL_ORDERLIST_ID+ ") REFERENCES TABLE_ORDERLIST (COLUMN_ORDERLIST_ID) ,"
        + "FOREIGN KEY (" +COLUMN_ORDERDETAIL_MIXTURE_ID+ ") REFERENCES TABLE_MIXTURE (COLUMN_MIXTURE_ID))";
    
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
      db.execSQL(CREATE_TABLE_DINING);
      db.execSQL(CREATE_TABLE_WAITOR);
      db.execSQL(CREATE_TABLE_MENU);
      db.execSQL(CREATE_TABLE_SUBMENU);
      db.execSQL(CREATE_TABLE_DISH);
      db.execSQL(CREATE_TABLE_MIXTURE);
      db.execSQL(CREATE_TABLE_ORDERLIST);
      db.execSQL(CREATE_TABLE_ORDERDETAIL);
    }

    @Override  
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {  
        // TODO Auto-generated method stub  
        System.out.println("upgrade a database");  
    }
}

