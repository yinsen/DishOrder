package com.infolands.dishorder;

import java.util.ArrayList;

import com.infolands.dishorder.DishApplication.app_mode;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderedDetailActivity extends Activity {

  private class OrderedItem {

    public String dish_id = "";
    public int number = 0;
    public String name = "";
    public String price = "";
    public String description = "";

    public OrderedItem(String dishid, String n, String p, String des, int num ) {
      dish_id = dishid;
      number = num;
      name = n;
      price = p;
      description = des;
    }
  }

  private class OrderedDetailAdapter extends BaseAdapter {

    private ArrayList<OrderedItem> dataList;

    public OrderedDetailAdapter(ArrayList<OrderedItem> list) {
      this.dataList = list;
    }

    public void setList(ArrayList<OrderedItem> list) {
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
        v = vi.inflate(R.layout.ordereddetailitem, null);
      }
      OrderedItem o = (OrderedItem) dataList.get(position);
      if (o != null) {
        TextView idView = (TextView) v.findViewById(R.id.itemNo);
        TextView nameView = (TextView) v.findViewById(R.id.itemName);
        TextView discriptionView = (TextView) v.findViewById(R.id.itemDescription);
        TextView priceView = (TextView) v.findViewById(R.id.itemPrice);
        TextView numberView = (TextView) v.findViewById(R.id.itemNums);
        if (idView != null) {
          idView.setText(String.valueOf(position));
          nameView.setText(o.name);
          priceView.setText(o.price);
          discriptionView.setText(o.description);
          numberView.setText(Integer.toString(o.number));
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
          
          int len2 = detailList.size();
          for (int i=0; i<len2; i++) {
            if (dishid.equalsIgnoreCase(detailList.get(i).dish_id)) {
              detailList.get(i).number ++;
              break;
            }
          }
          detailAdapter.setList(detailList);
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
          
          int len2 = detailList.size();
          for (int i=0; i<len2; i++) {
            if (dishid.equalsIgnoreCase(detailList.get(i).dish_id) && detailList.get(i).number > 1) {
              detailList.get(i).number --;
              break;
            }
          }
          detailAdapter.setList(detailList);
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
          
          int len2 = detailList.size();
          for (int i=0; i<len2; i++) {
            if (dishid.equalsIgnoreCase(detailList.get(i).dish_id)) {
              detailList.remove(i);
              break;
            }
          }
          detailAdapter.setList(detailList);
        }
      });
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.ordereddetaillist);

    initTopButtons();
  }


  @Override
  public void onStart (){
    super.onStart();
    
    setDishsAdapter();
    
  }
  
  ArrayList<OrderedItem> detailList = new ArrayList<OrderedItem>();
  OrderedDetailAdapter detailAdapter;
  private void setDishsAdapter() {
    
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
      detailList.add(element);
    }
    
    detailAdapter = new OrderedDetailAdapter(detailList);

    ListView dishListView = (ListView) findViewById(R.id.orderDetailList);
    dishListView.setAdapter(detailAdapter);
  }

  private String statusStr = "";
  private void initTopButtons() {
    if (((DishApplication)getApplicationContext()).orderlistList.size()<=0){
      statusStr = "";
    }
    else if (((DishApplication)getApplicationContext()).STATUS_UNCONFIRMED.equals(
        ((DishApplication)getApplicationContext()).orderlistList.get(0).status)){
      statusStr = getResources().getString(R.string.statusunconfirmed);
    }
    else if (((DishApplication)getApplicationContext()).STATUS_CONFIRMED.equals(
        ((DishApplication)getApplicationContext()).orderlistList.get(0).status)){
      statusStr = getResources().getString(R.string.statusconfirmed);
    }
    else if (((DishApplication)getApplicationContext()).STATUS_PAYED.equals(
        ((DishApplication)getApplicationContext()).orderlistList.get(0).status)){
      statusStr = getResources().getString(R.string.statuspayed);
    }
    TextView tableText = (TextView) findViewById(R.id.detailtableno);
    tableText.setText(getResources().getString(R.string.tableno) 
                                            + ((DishApplication)getApplicationContext()).getCurrTableNo()
                                            + "(" + statusStr + ")");
    tableText.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

      }
    });
    
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

    Button okBtn = (Button) findViewById(R.id.okBt);
    if (app_mode.MODE_WAITOR == ((DishApplication)getApplicationContext()).currMode) {
      okBtn.setVisibility(View.VISIBLE);
    }
    else {
      okBtn.setVisibility(View.INVISIBLE);
    }
    okBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (((DishApplication)getApplicationContext()).orderlistList.size()<=0){
          Toast.makeText(getApplicationContext(), getResources().getString(R.string.noorderitem), Toast.LENGTH_LONG).show();
        }
        else {
          ((DishApplication)getApplicationContext()).orderlistList.get(0).status = DishApplication.STATUS_CONFIRMED;
          
  //        for (int i=0; i<((DishApplication)getApplicationContext()).orderdetailList.size(); i++){
  //          ((DishApplication)getApplicationContext()).orderdetailList.get(i).status = DishApplication.STATUS_CONFIRMED;
  //        }
          
          ((DishApplication)getApplicationContext()).saveOrderedData();
          
          Toast.makeText(getApplicationContext(), getResources().getString(R.string.orderconfirmedok), Toast.LENGTH_LONG).show();
          
          if (((DishApplication)getApplicationContext()).STATUS_UNCONFIRMED.equals(
              ((DishApplication)getApplicationContext()).orderlistList.get(0).status)){
            statusStr = getResources().getString(R.string.statusconfirmed);
          }
          else if (((DishApplication)getApplicationContext()).STATUS_CONFIRMED.equals(
              ((DishApplication)getApplicationContext()).orderlistList.get(0).status)) {
            statusStr = getResources().getString(R.string.statuspayed);
          }
          TextView tableText = (TextView) findViewById(R.id.detailtableno);
          tableText.setText(getResources().getString(R.string.tableno) 
                                                  + ((DishApplication)getApplicationContext()).getCurrTableNo()
                                                  + "(" + statusStr + ")");
        }
      }
    });
  }
  
}
