package com.infolands.dishorder;

import java.util.Vector;

import com.infolands.dishorder.DataItem.DishItem;

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

public class OrderedDetailActivity extends Activity {

  private class OrderedItem {

    public int id;
    public int number;    
    public String name;
    public String price;
    public String description;

    public OrderedItem(int no, int num, String n, String p, String des) {
      id = no;
      number = num;
      name = n;
      price = p;
      description = des;
    }
  }

  private class OrderedDetailAdapter extends BaseAdapter {

    private Vector<OrderedItem> dataList;

    public OrderedDetailAdapter(Vector<OrderedItem> list) {
      this.dataList = list;
    }

    public void setList(Vector<OrderedItem> list) {
      this.dataList = list;
      notifyDataSetChanged();
    }

    public int getCount() {
      return dataList.size();
    }

    public Object getItem(int position) {
      return dataList.get(position);
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
      OrderedItem o = (OrderedItem) dataList.get(position);
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
          //dataList.get(pos).id;
        }
      });
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.ordereddetaillist);

    setTopButtons();
    setDishsAdapter();
  }

  private void setDishsAdapter() {

    Vector<OrderedItem> dishList = new Vector<OrderedItem>();
    OrderedItem element0 = new OrderedItem(0, 1, "chicken", "180",  "");
    OrderedItem element1 = new OrderedItem(1, 2, "chicken3", "180",  "");
    OrderedItem element2 = new OrderedItem(2, 1, "chicken4", "180",  "");
    OrderedItem element3 = new OrderedItem(3, 1, "chicken5", "180",  "");
    OrderedItem element4 = new OrderedItem(4, 1, "chicken6", "180",  "");
    dishList.addElement(element0);
    dishList.addElement(element1);
    dishList.addElement(element2);
    dishList.addElement(element3);
    dishList.addElement(element4);
    OrderedDetailAdapter dishAdapter = new OrderedDetailAdapter(dishList);

    //    ScrollView dishScrollView = (ScrollView) findViewById(R.id.dishScrollView);
    //    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //    final View dishListLayout = inflater.inflate(R.layout.dishitem, dishScrollView);
    //    ListView dishListView = (ListView) dishListLayout.findViewById(R.id.dishlist);
    ListView dishListView = (ListView) findViewById(R.id.dishList);
    dishListView.setAdapter(dishAdapter);
    dishListView.setVisibility(View.INVISIBLE);
  }

  private void setTopButtons() {
    Button backBtn = (Button) findViewById(R.id.backBt);
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


    Button moreBtn = (Button) findViewById(R.id.okBt);
    moreBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

      }
    });
  }
  
  

  public Vector<DataItem.DishItem> getDishList() {

    DishOrderDatabaseHelper dbHelper = new DishOrderDatabaseHelper(this);
    SQLiteDatabase mDb = dbHelper.getReadableDatabase();

    Vector<DataItem.DishItem> results = new Vector<DataItem.DishItem>();

    String[] selectionArgs = new String[]{currSubMenu, currMenu};
    Cursor cursor = mDb.rawQuery("select * from table " + DishOrderDatabaseHelper.TABLE_DISH + " where "
        + DishOrderDatabaseHelper.COLUMN_DISH_SUBMENU + "=?" + " AND " + DishOrderDatabaseHelper.COLUMN_DISH_MENU
        + "=?", selectionArgs);
    cursor.moveToFirst();

    DataItem dataItem = new DataItem();
    while (cursor.getPosition() != cursor.getCount()) {
      DataItem.DishItem item = dataItem.new DishItem();
      item.dish_id = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_ID));
      item.name = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_NAME));
      item.price = cursor.getString(cursor.getColumnIndex(DishOrderDatabaseHelper.COLUMN_DISH_PRICE));
      results.add(item);
      cursor.moveToNext();
    }
    cursor.close();
    return results;
  }
  
  public boolean deleteAddress(String rowId) {
    if(mDb==null)
        return false; 
    
    return mDb.delete(ADDRESS_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
  }
}
