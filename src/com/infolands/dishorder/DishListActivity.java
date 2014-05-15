package com.infolands.dishorder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infolands.dishorder.DishApplication.app_mode;

public class DishListActivity extends Activity {

  public static final int REQUEST_CODE_BIG_IMG = 10;
  public static final int REQUEST_CODE_MIXTURE = 11;
  
  private class LabelAdapter extends BaseAdapter {

    private ArrayList<DataItem.SubmenuItem> submenuList;
    public LabelAdapter() {
    }
    public LabelAdapter(ArrayList<DataItem.SubmenuItem> list) {
      this.submenuList = list;
    }

    public void setList(ArrayList<DataItem.SubmenuItem> list) {
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
      
      TextView labelBt = (TextView) v.findViewById(R.id.labelItem);
      DataItem.SubmenuItem o = (DataItem.SubmenuItem) submenuList.get(position);
      if (o != null) {
        
        if (labelBt != null) {
          labelBt.setText(o.name);
          handleLabelSelect(labelBt, position);
          
          if (o.isFocus)
            labelBt.setBackgroundResource(R.drawable.background);
          else
            labelBt.setBackgroundResource(R.drawable.labelbg);
        }
      }

      return v;
    }

    private void handleLabelSelect(final TextView labelButton, final int pos) {

      labelButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          //更新当前子菜单项，同时更新菜品列表
          DishApplication app = (DishApplication)getApplicationContext();
          app.currSubMenu =  submenuList.get(pos).submenu_id;
          
          for (int i=0; i<app.submenuList.size(); i++) {
            submenuList.get(i).isFocus = false;
          }
          submenuList.get(pos).isFocus = true;
          
          DishListActivity activity = (DishListActivity)(v.getContext());
          activity.labelAdapter.notifyDataSetChanged();
          activity.updateDishListPerSubmenu();
          //((ViewGroup)(v.getParent())).setBackgroundResource(R.drawable.labelbg);
          
        }
      });
    }
  }

  private class DishAdapter extends BaseAdapter {

    private ArrayList<DataItem.DishItem> dishList;

    public DishAdapter() {
    }
    public DishAdapter(ArrayList<DataItem.DishItem> list) {
      this.dishList = list;
    }

    public void setList(ArrayList<DataItem.DishItem> list) {
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
          priceView.setText("(" + o.price + getResources().getString(R.string.pricestr) + ")");
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
          ((DishApplication)getApplicationContext()).currDishId = dishItem.dish_id;
          try {
            Intent intent = new Intent(DishListActivity.this,OrderActivity.class);  
            Bundle bundle = new Bundle();
            bundle.putString("dishname", dishItem.name);  
            intent.putExtra("dishname", bundle); 
            startActivityForResult(intent, REQUEST_CODE_BIG_IMG); 
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

  public class DishGridAdapter extends BaseAdapter {
    private ArrayList<DataItem.DishItem> dishList;
    
    public DishGridAdapter() {
    }
    public DishGridAdapter(ArrayList<DataItem.DishItem> list) {
      this.dishList = list;
    }

    public void setList(ArrayList<DataItem.DishItem> list) {
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
          priceView.setText("(" + o.price + getResources().getString(R.string.pricestr) + ")");
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
          ((DishApplication)getApplicationContext()).currDishId = dishItem.dish_id;
          try {
            Intent intent = new Intent(DishListActivity.this,OrderActivity.class); 
            Bundle bundle = new Bundle();
            bundle.putString("dishname", dishItem.name);  
            intent.putExtra("dishname", bundle); 
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
  private enum DisplayMode {
    MODE_GRID,
    MODE_LIST,
    
    MODE_NONE
  }
  private DisplayMode displayMode=DisplayMode.MODE_GRID;
  
  private ArrayList<DataItem.SubmenuItem> currSubmenuList = new ArrayList<DataItem.SubmenuItem>();
  private ArrayList<DataItem.DishItem> currDishList = new ArrayList<DataItem.DishItem>();
  
  private DishAdapter dishAdapter = new DishAdapter();
  private DishGridAdapter dishGridAdapter = new DishGridAdapter();
  private LabelAdapter labelAdapter = new LabelAdapter();
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dishlist);
    
    setTopButtons();
    
    initDataList();
  }
  
  @Override
  public void onStart() {
    super.onStart();
    
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
  private void initDataList(){
    DishApplication app = (DishApplication)getApplicationContext();
    
    updateSubmenuListPerMenu();

    //初始化本menu的活动submenu
    if (currSubmenuList.size()>0) {
      app.currSubMenu = currSubmenuList.get(0).submenu_id;
      currSubmenuList.get(0).isFocus = true;
      updateDishListPerSubmenu();
    }
    
    return;
  }
  
  private void updateSubmenuListPerMenu(){
    DishApplication app = (DishApplication)getApplicationContext();
    //获取当前页面的Submenu List以及Dish List
    if (!currSubmenuList.isEmpty()) currSubmenuList.clear();
    for (int i=0; i<app.submenuList.size(); i++) {
      if (app.submenuList.get(i).menu_id.equalsIgnoreCase(app.currMenu)) {
        currSubmenuList.add(app.submenuList.get(i));
      }
    }
    
    labelAdapter.setList(currSubmenuList);
    ListView labelListView = (ListView) findViewById(R.id.labelList);
    labelListView.setAdapter(labelAdapter);
    
  }
  
  private void updateDishListPerSubmenu(){
    
    if (!currDishList.isEmpty()) currDishList.clear();
    
    DishApplication app = (DishApplication)getApplicationContext();
    for (int j=0; j<app.dishList.size(); j++) {
      if (app.dishList.get(j).submenu_id.equalsIgnoreCase(app.currSubMenu)
        &&app.dishList.get(j).menu_id.equalsIgnoreCase(app.currMenu)) {
        currDishList.add(app.dishList.get(j));
      }
    }
    
    TextView noDishView = (TextView) findViewById(R.id.noDish);
    
    ListView dishListView = (ListView) findViewById(R.id.dishList);
    dishAdapter.setList(currDishList);
    dishListView.setAdapter(dishAdapter);
    
    GridView dishGridView = (GridView) findViewById(R.id.dishGrid);
    dishGridAdapter.setList(currDishList);
    dishGridView.setAdapter(dishGridAdapter);
    
    if (currDishList.size()<=0) {
      noDishView.setVisibility(View.VISIBLE);
      dishListView.setVisibility(View.INVISIBLE);
      dishGridView.setVisibility(View.INVISIBLE);
    }
    else {
      switch (displayMode){
        case MODE_GRID:
          {
            noDishView.setVisibility(View.INVISIBLE);
            dishListView.setVisibility(View.INVISIBLE);
            dishGridView.setVisibility(View.VISIBLE);
          }
          break;
        case MODE_LIST:
          {
            noDishView.setVisibility(View.INVISIBLE);
            dishListView.setVisibility(View.VISIBLE);
            dishGridView.setVisibility(View.INVISIBLE);
          }
          break;
        default:
          
          break;
      }
    }
  }

  private void setTableNoView(){
    TextView tableno = (TextView) findViewById(R.id.tableNo);
    Button tableBt = (Button) findViewById(R.id.tableBt);
    tableno.setText(((DishApplication)getApplicationContext()).getCurrTableNo());
    tableBt.setText(((DishApplication)getApplicationContext()).getCurrTableNo());
    tableBt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new NumberPickerDialog(v.getContext(), new NumberPickerDialog.OnNumberChangedListener() {
          
          @Override
          public void numberChanged(int number, int mode) {
            ((DishApplication)getApplicationContext()).setCurrTableNo(Integer.toString(number));
            Button tableBt = (Button) findViewById(R.id.tableBt);
            tableBt.setText(Integer.toString(number));
          }
        }, 0, 999, 0, 1).show();
      }
    });
    
    if (app_mode.MODE_WAITOR == ((DishApplication)getApplicationContext()).currMode) {
      tableBt.setVisibility(View.VISIBLE);
      tableno.setVisibility(View.INVISIBLE);
    }
    else {
      tableBt.setVisibility(View.INVISIBLE);
      tableno.setVisibility(View.VISIBLE);
    }
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
    
    setTableNoView();
    
    Button detailBt = (Button) findViewById(R.id.detailBt);
    detailBt.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(DishListActivity.this, OrderedDetailActivity.class);
        startActivity(intent);
      }
    });
    

    Button stypeBtn = (Button) findViewById(R.id.stypebt);
    stypeBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        TextView noDishView = (TextView) findViewById(R.id.noDish);
        GridView dishGridView = (GridView) findViewById(R.id.dishGrid);
        ListView dishListView = (ListView) findViewById(R.id.dishList);
        
        if (View.VISIBLE == dishGridView.getVisibility()) {
          noDishView.setVisibility(View.INVISIBLE);
          dishGridView.setVisibility(View.INVISIBLE);
          dishListView.setVisibility(View.VISIBLE);
        }
        else {
          noDishView.setVisibility(View.INVISIBLE);
          dishListView.setVisibility(View.INVISIBLE);
          dishGridView.setVisibility(View.VISIBLE);
        }
      }
    });
  }
  

  private static final int MENU_WAITOR_MODE = 1;
  private static final int MENU_CURTOMER_MODE = 2;
  private static final int MENU_SEARCH = 4;
  private static final int MENU_SETTING = 5;
  private static final int MENU_UPDATING_DATA = 6;

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    
    menu.clear();
    DishApplication app = (DishApplication)getApplicationContext();
    if ( app_mode.MODE_CUSTOMER == app.currMode) {
      menu.add(Menu.NONE, MENU_WAITOR_MODE, Menu.NONE, R.string.waitormode).setIcon(
          android.R.drawable.ic_menu_edit);
      
    }
    else if (app_mode.MODE_WAITOR == app.currMode) {
      menu.add(Menu.NONE, MENU_CURTOMER_MODE, Menu.NONE, R.string.customermode)
          .setIcon(android.R.drawable.ic_menu_save);
      menu.add(Menu.NONE, MENU_UPDATING_DATA, Menu.NONE, R.string.updatedata).setIcon(
          android.R.drawable.ic_menu_manage);
    }
    
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
      case MENU_SEARCH:
        onMenuSearch();
        return true;
      case MENU_SETTING:
        onMenuSetting();
        return true;
      case MENU_UPDATING_DATA:
        onMenuUpdatingData();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  
  private void onMenuWaitorMode() {
    LayoutInflater factory = LayoutInflater.from(this);
    
    final View loginView = factory.inflate(R.layout.login, null);
    final EditText userInput = (EditText)loginView.findViewById(R.id.username_edit);
    final EditText passwordInput = (EditText)loginView.findViewById(R.id.password_edit);
    new AlertDialog.Builder(this)
        .setTitle(R.string.alert_dialog_text_entry)
        .setView(loginView)
        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              
              DishApplication app = (DishApplication)getApplicationContext();
              int i=0;
              for(; i<app.waitorList.size(); i++){
                String user = userInput.getText().toString().trim();
                String pwd = passwordInput.getText().toString().trim();
                
                if( user !=null && user.equals(app.waitorList.get(i).waitor_id)
                 && pwd !=null && pwd.equals(app.waitorList.get(i).password)){
                  
                  app.currMode = app_mode.MODE_WAITOR;
                  setTableNoView();
                  
                  break;
                }
                else {
                  
                }
              }
              
              if ( i >= app.waitorList.size()){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
              }
                /* User clicked OK so do some stuff */
            }
        })
        .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked cancel so do some stuff */
            }
        })
        .create()
        .show();
    
    
  }
  
  private void onMenuCustomerMode(){
    DishApplication app = (DishApplication)getApplicationContext();
    app.currMode = app_mode.MODE_CUSTOMER;
    
    setTableNoView();
  }
  
  private void onMenuSearch(){
    //onSearchRequested();
    Intent intent = new Intent(DishListActivity.this, SearchResultActivity.class);
    startActivity(intent);
  }
  
  private void onMenuUpdatingData(){
    Intent intent = new Intent(DishListActivity.this, UpdateDataActivity.class);
    startActivity(intent);
  }
  private void onMenuSetting(){}
  
}
