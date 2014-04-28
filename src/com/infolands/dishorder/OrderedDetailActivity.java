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

    public String dish_id;
    public int number;
    public String name;
    public String price;
    public String description;

    public OrderedItem(String dishid, String n, String p, String des, int num ) {
      dish_id = dishid;
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
        TextView idView = (TextView) v.findViewById(R.id.itemNo);
        
        TextView nameView = (TextView) v.findViewById(R.id.itemName);
        TextView discriptionView = (TextView) v.findViewById(R.id.itemDescription);
        TextView priceView = (TextView) v.findViewById(R.id.itemPrice);
        TextView numberView = (TextView) v.findViewById(R.id.itemNums);
        if (nameView != null) {
          idView.setText(String.valueOf(position));
          nameView.setText(o.name);
          priceView.setText(o.price);
          discriptionView.setText(o.description);
          numberView.setText(o.number);
        }
        Button addBt = (Button) v.findViewById(R.id.itemAdd);
        if (addBt != null) {
          addDishNum(addBt, o.dish_id);
        }
        
        Button reduceBt = (Button) v.findViewById(R.id.itemReduce);
        if (reduceBt != null) {
          reduceDishNum(reduceBt, o.dish_id);
        }
        
        Button deleteBt = (Button) v.findViewById(R.id.itemDelete);
        if (deleteBt != null) {
          deleteOrderItem(deleteBt, o.dish_id);
        }
      }

      return v;
    }

    private void addDishNum(final Button addButton, final String dishid) {

      addButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          
          int len = ((DishApplication)getApplicationContext()).orderdetailList.size();
          for (int i=0; i<len; i++) {
            if (dishid.equalsIgnoreCase(((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_id)) {
              ((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_num ++;
              break;
            }
          }
        }
      });
    }
    private void reduceDishNum(final Button reduceButton, final String dishid) {

      reduceButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          
          int len = ((DishApplication)getApplicationContext()).orderdetailList.size();
          for (int i=0; i<len; i++) {
            if (dishid.equalsIgnoreCase(((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_id)) {
              if (((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_num > 1)
                ((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_num --;
              break;
            }
          }
        }
      });
    }
    private void deleteOrderItem(final Button deleteButton, final String dishid) {

      deleteButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          
          int len = ((DishApplication)getApplicationContext()).orderdetailList.size();
          for (int i=0; i<len; i++) {
            if (dishid.equalsIgnoreCase(((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_id)) {
              ((DishApplication)getApplicationContext()).orderdetailList.remove(i);
              break;
            }
          }
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
    
  }


  @Override
  public void onStart (){
    setDishsAdapter();
    
  }
  
  private void setDishsAdapter() {

    Vector<OrderedItem> dishList = new Vector<OrderedItem>();
    int numOrdered = ((DishApplication)getApplicationContext()).orderdetailList.size();
    int numDishs = ((DishApplication)getApplicationContext()).dishList.size();
    
    for (int i = 0; i < numOrdered; i++){
      String dish_id = ((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_id;
      String dish_description = ((DishApplication)getApplicationContext()).orderdetailList.get(i).taste 
                              +((DishApplication)getApplicationContext()).orderdetailList.get(i).cookie
                              +((DishApplication)getApplicationContext()).orderdetailList.get(i).weight
                              +((DishApplication)getApplicationContext()).orderdetailList.get(i).mixture_id;
      int dish_nums = ((DishApplication)getApplicationContext()).orderdetailList.get(i).dish_num;
      
      String dish_name = "";
      String dish_price = "";
      
      for (int j=0; j < numDishs; j++) {
        if (dish_id.equalsIgnoreCase(((DishApplication)getApplicationContext()).dishList.get(j).dish_id)) {
          dish_name = ((DishApplication)getApplicationContext()).dishList.get(j).name;
          dish_price = ((DishApplication)getApplicationContext()).dishList.get(j).price;
          break;
        }
      }
      
      
      OrderedItem element = new OrderedItem(dish_id, dish_name, dish_price, dish_description, dish_nums );
      dishList.addElement(element);
    }
    
    
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
  
}
