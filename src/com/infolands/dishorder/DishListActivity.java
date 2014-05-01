package com.infolands.dishorder;

import java.util.Vector;

import com.infolands.dishorder.DishApplication.app_mode;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DishListActivity extends Activity {

  public static final int REQUEST_CODE_BIG_IMG = 10;
  public static final int REQUEST_CODE_MIXTURE = 11;
  
  private class LabelAdapter extends BaseAdapter {

    private Vector<DataItem.SubmenuItem> submenuList;
    public LabelAdapter(Vector<DataItem.SubmenuItem> list) {
      this.submenuList = list;
    }

    public void setList(Vector<DataItem.SubmenuItem> list) {
      this.submenuList = list;
      notifyDataSetChanged();
    }

    public int getCount() {
      return submenuList.size();
    }

    public Object getItem(int position) {
      return submenuList.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.labelitem, null);
      }
      DataItem.SubmenuItem o = (DataItem.SubmenuItem) submenuList.get(position);
      
      if (o != null) {
        Button labelBt = (Button) v.findViewById(R.id.labelItem);
        if (labelBt != null) {
          labelBt.setText(o.name);
          labelBt.getBackground().setAlpha(0);
          handleLabelSelect(labelBt, position);
        }
      }

      return v;
    }

    private void handleLabelSelect(final Button labelButton, final int pos) {

      labelButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          //submenuList.get(pos).id;
          //          if (oldSelectedItem != null) {
          //            oldSelectedItem.getBackground().setAlpha(0);
          //          }
          //          v.setBackgroundColor(Color.LTGRAY);
          //          oldSelectedItem = v;
          
          //更新当前子菜单项，同时更新菜品列表
          DishApplication app = (DishApplication)getApplicationContext();
          app.currSubMenu =  submenuList.get(pos).submenu_id;
          
          DishListActivity activity = (DishListActivity)(v.getContext());
          activity.getCurrDishsData();
        }
      });
    }
  }

  private class DishAdapter extends BaseAdapter {

    private Vector<DataItem.DishItem> dishList;

    public DishAdapter(Vector<DataItem.DishItem> list) {
      this.dishList = list;
    }

    public void setList(Vector<DataItem.DishItem> list) {
      this.dishList = list;
      notifyDataSetChanged();
    }

    public int getCount() {
      return dishList.size();
    }

    public Object getItem(int position) {
      return dishList.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dishitem, null);
      }
      DataItem.DishItem o = (DataItem.DishItem) dishList.get(position);
      if (o != null) {
        TextView nameView = (TextView) v.findViewById(R.id.dishName);
        TextView priceView = (TextView) v.findViewById(R.id.dishPrice);
        if (nameView != null) {
          nameView.setText(o.name);
          priceView.setText(o.price);
        }
        Button orderBt = (Button) v.findViewById(R.id.dishOrder);
        if (orderBt != null) {
          handleDishOrder(orderBt, o);
        }
      }

      return v;
    }

    private void handleDishOrder(final Button orderButton, final DataItem.DishItem dishItem) {

      orderButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          ((DishApplication)getApplicationContext()).setCurrOrderDetailItem(dishItem.dish_id);
          try {
            Intent intent = new Intent(DishListActivity.this,OrderActivity.class);  
            Bundle bundle = new Bundle();
            bundle.putString("dish_id", dishItem.dish_id);  
            intent.putExtra("order_item", bundle);  
            startActivityForResult(intent, REQUEST_CODE_BIG_IMG); 
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

  public class DishGridAdapter extends BaseAdapter {
    private Vector<DataItem.DishItem> dishList;
    
    public DishGridAdapter(Vector<DataItem.DishItem> list) {
      this.dishList = list;
    }

    public void setList(Vector<DataItem.DishItem> list) {
      this.dishList = list;
      notifyDataSetChanged();
    }

    public int getCount() {
      return dishList.size();
    }

    public Object getItem(int position) {
      return dishList.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dishgriditem, null);
      }
      DataItem.DishItem o =  dishList.get(position);
      
      if (o != null) {
        TextView nameView = (TextView) v.findViewById(R.id.dishName);
        TextView priceView = (TextView) v.findViewById(R.id.dishPrice);
        if (nameView != null) {
          nameView.setText(o.name);
          priceView.setText(o.price);
        }
        ImageView orderBt = (ImageView) v.findViewById(R.id.dishImgOrder);
        if (orderBt != null) {
          DishListActivity activity = (DishListActivity)(v.getContext());
          int id = activity.getResources().getIdentifier(activity.getPackageName()+":drawable/" + o.img, null,null);
          orderBt.setImageResource(id);
          
          handleDishOrder(orderBt, o);
        }
      }

      return v;
    }
 
    private void handleDishOrder(final ImageView orderButton, final DataItem.DishItem dishItem) {

      orderButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          ((DishApplication)getApplicationContext()).setCurrOrderDetailItem(dishItem.dish_id);
          try {
            Intent intent = new Intent(DishListActivity.this,OrderActivity.class);  
            Bundle bundle = new Bundle();
            bundle.putString("dish_id", dishItem.dish_id);  
            intent.putExtra("order_item", bundle);  
            startActivityForResult(intent, REQUEST_CODE_BIG_IMG); 
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }
  
  /**************************** submenuList ************************************
  ********************此变量是否需要一个menu_id作为外键？需要根据数据待确定***************
  *****************************************************************************/
  private Vector<DataItem.SubmenuItem> currSubmenuList = new Vector<DataItem.SubmenuItem>();
  private Vector<DataItem.DishItem> currDishList = new Vector<DataItem.DishItem>();
  
  private DishAdapter dishAdapter;
  private LabelAdapter labelAdapter;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dishlist);
    
  }
  
  @Override
  public void onStart() {
    super.onStart();
    
    getCurrDishsData();
    
    setTopButtons();
    setlabelAdapter();
    setDishsAdapter();
  }
  

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
     
      case REQUEST_CODE_BIG_IMG: {
          
        }
        break;
      case REQUEST_CODE_MIXTURE: {
        }
        break;
      default:
        break;
    }
  }
  
  //获取当前menu对应的submenu、当前menu&submenu对应的dishs
  private boolean getCurrDishsData(){
    DishApplication app = (DishApplication)getApplicationContext();
    //获取当前页面的Submenu List以及Dish List
    if (!currSubmenuList.isEmpty()) currSubmenuList.clear();
    for (int i=0; i<app.submenuList.size(); i++) {
      if (app.submenuList.get(i).menu_id.equalsIgnoreCase(app.currMenu)) {
        currSubmenuList.add(app.submenuList.get(i));
      }
    }
    
    for (int i=0; i<currSubmenuList.size(); i++) {
      app.currSubMenu = currSubmenuList.get(i).submenu_id;
      
      if (!currDishList.isEmpty()) currDishList.clear();
      for (int j=0; j<app.dishList.size(); j++) {
        if (app.dishList.get(j).submenu_id.equalsIgnoreCase(app.currSubMenu)
          &&app.dishList.get(j).menu_id.equalsIgnoreCase(app.currMenu)) {
          currDishList.add(app.dishList.get(j));
        }
      }
      
      //只有当submenu以及此submenu中有dish的时候，才返回true;
      if (!currDishList.isEmpty())
        return true;
    }
    
    return false;
  }
  private void setSettingSpinner(){
    Vector<CharSequence> settingList = new Vector<CharSequence>();
//    settingList.add(R.string.language);
//    settingList.add(R.string.orderedlist);
//    settingList.add(R.string.waitormode);
//    settingList.add(R.string.structuretype);
//    settingList.add(R.string.dishsearch);
  }
  private void setDishsAdapter() {

    dishAdapter = new DishAdapter(currDishList);

    //    ScrollView dishScrollView = (ScrollView) findViewById(R.id.dishScrollView);
    //    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //    final View dishListLayout = inflater.inflate(R.layout.dishitem, dishScrollView);
    //    ListView dishListView = (ListView) dishListLayout.findViewById(R.id.dishlist);
    ListView dishListView = (ListView) findViewById(R.id.dishList);
    dishListView.setAdapter(dishAdapter);
    dishListView.setVisibility(View.INVISIBLE);
    
    DishGridAdapter dishGridAdapter = new DishGridAdapter(currDishList);
    GridView dishGridView = (GridView) findViewById(R.id.dishGrid);
    dishGridView.setAdapter(dishGridAdapter);
    dishGridView.setVisibility(View.VISIBLE);

  }

  private void setlabelAdapter() {
    
    labelAdapter = new LabelAdapter(currSubmenuList);

    ListView labelListView = (ListView) findViewById(R.id.labelList);
    labelListView.setAdapter(labelAdapter);
  }

  private void setTopButtons() {
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


    setSettingSpinner();

    Button stypeBtn = (Button) findViewById(R.id.stypebt);
    stypeBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        GridView dishGridView = (GridView) findViewById(R.id.dishGrid);
        ListView dishListView = (ListView) findViewById(R.id.dishList);
        
        if (View.VISIBLE == dishGridView.getVisibility()) {
          dishGridView.setVisibility(View.INVISIBLE);
          dishListView.setVisibility(View.VISIBLE);
        }
        else {
          dishListView.setVisibility(View.INVISIBLE);
          dishGridView.setVisibility(View.VISIBLE);
        }
      }
    });
  }
  

  private static final int MENU_WAITOR_MODE = 1;
  private static final int MENU_CURTOMER_MODE = 2;
  private static final int MENU_CATEGORY = 3;
  private static final int MENU_SEARCH = 4;

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    
    menu.clear();
    DishApplication app = (DishApplication)getApplicationContext();
    if ( app_mode.MODE_CUSTOMER == app.currMode) {
      menu.add(Menu.NONE, MENU_WAITOR_MODE, Menu.NONE, R.string.waitormode).setIcon(
          android.R.drawable.ic_menu_manage);
      
    }
    else if (app_mode.MODE_WAITOR == app.currMode) {
      menu.add(Menu.NONE, MENU_CURTOMER_MODE, Menu.NONE, R.string.customermode)
          .setIcon(android.R.drawable.ic_menu_view);
    }
    
    menu.add(Menu.NONE, MENU_CATEGORY, Menu.NONE, R.string.structuretype).setIcon(
        android.R.drawable.ic_menu_edit);
    menu.add(Menu.NONE, MENU_SEARCH, Menu.NONE, R.string.dishsearch)
        .setIcon(android.R.drawable.ic_menu_search);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case MENU_WAITOR_MODE:
        onMenuWaitorMode();
        return true;
      case MENU_CURTOMER_MODE:
        onMenuCustomerMode();
        return true;
      case MENU_CATEGORY:
        onMenuCategory();
        return true;
      case MENU_SEARCH:
        onMenuSearch();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  
  private void onMenuWaitorMode() {
    DishApplication app = (DishApplication)getApplicationContext();
    app.currMode = app_mode.MODE_WAITOR;
    
  }
  
  private void onMenuCustomerMode(){}
  
  private void onMenuCategory(){}
  
  private void onMenuSearch(){}
  
  
}
