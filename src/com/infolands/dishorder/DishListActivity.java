package com.infolands.dishorder;

import java.util.Vector;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

  private class LabelAdapter extends BaseAdapter {

    private View oldSelectedItem = null;
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
          activity.updateData();
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
          handleDishOrder(orderBt, position);
        }
      }

      return v;
    }

    private void handleDishOrder(final Button orderButton, final int pos) {

      orderButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          
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
          handleDishOrder(orderBt, position);
        }
      }

      return v;
    }
    
    private void handleDishOrder(final ImageView orderButton, final int pos) {

      orderButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          //dishList.get(pos).id;
          try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String url = "orderdetail://";
            intent.setData(Uri.parse(url));
            startActivity(intent);
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
  private Vector<DataItem.SubmenuItem> submenuList;
  private Vector<DataItem.DishItem> dishList;
  
  private DishAdapter dishAdapter;
  private LabelAdapter labelAdapter;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.dishlist);

    
    setTopButtons();
    setlabelAdapter();
    setDishsAdapter();
  }
  public void onStart() {
    super.onStart();
    
    updateData();
  }
  
  private void updateData(){
    //获取当前页面的Dish List以及Submenu List
    if (!dishList.isEmpty()) dishList.clear();
    DishApplication app = (DishApplication)getApplicationContext();
    for (int i=0; i<app.dishList.size(); i++) {
      if (app.dishList.get(i).submenu_id.equalsIgnoreCase(app.currSubMenu)
        &&app.dishList.get(i).menu_id.equalsIgnoreCase(app.currMenu)) {
        dishList.add(app.dishList.get(i));
      }
    }
    dishAdapter.setList(dishList);//更新adapter里的数据&页面内容
    
    if (!submenuList.isEmpty()) submenuList.clear();
    for (int i=0; i<app.submenuList.size(); i++) {
      if (app.submenuList.get(i).menu_id.equalsIgnoreCase(app.currMenu)) {
        submenuList.add(app.submenuList.get(i));
      }
    }
    labelAdapter.setList(submenuList);//更新adapter里的数据&页面内容
  }

  private void setDishsAdapter() {

    dishAdapter = new DishAdapter(dishList);

    //    ScrollView dishScrollView = (ScrollView) findViewById(R.id.dishScrollView);
    //    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //    final View dishListLayout = inflater.inflate(R.layout.dishitem, dishScrollView);
    //    ListView dishListView = (ListView) dishListLayout.findViewById(R.id.dishlist);
    ListView dishListView = (ListView) findViewById(R.id.dishList);
    dishListView.setAdapter(dishAdapter);
    dishListView.setVisibility(View.INVISIBLE);
    
    DishGridAdapter dishGridAdapter = new DishGridAdapter(dishList);
    GridView dishGridView = (GridView) findViewById(R.id.dishGrid);
    dishGridView.setAdapter(dishGridAdapter);
    dishGridView.setVisibility(View.VISIBLE);

  }

  private void setlabelAdapter() {
    
    labelAdapter = new LabelAdapter(submenuList);

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


    Button moreBtn = (Button) findViewById(R.id.morebt);
    moreBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
          
      }
    });

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
  
  
}
