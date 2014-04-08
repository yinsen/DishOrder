package com.infolands.dishorder;

import java.util.Vector;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
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

  private class LabelItem {

    public int id;
    public String name;

    public LabelItem(int no, String n) {
      id = no;
      name = n;
    }
  }

  private class DishItem {

    public int id;
    public String name;
    public String price;

    public DishItem(int no, String n, String p) {
      id = no;
      name = n;
      price = p;
    }
  }

  private class LabelAdapter extends BaseAdapter {

    private Vector<LabelItem> dataList;
    private View oldSelectedItem = null;

    public LabelAdapter(Vector<LabelItem> list) {
      this.dataList = list;
    }

    public void setList(Vector<LabelItem> list) {
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
        v = vi.inflate(R.layout.labelitem, null);
      }
      LabelItem o = (LabelItem) dataList.get(position);
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
          //dataList.get(pos).id;
          //          if (oldSelectedItem != null) {
          //            oldSelectedItem.getBackground().setAlpha(0);
          //          }
          //          v.setBackgroundColor(Color.LTGRAY);
          //          oldSelectedItem = v;
        }
      });
    }
  }

  private class DishAdapter extends BaseAdapter {

    private Vector<DishItem> dataList;

    public DishAdapter(Vector<DishItem> list) {
      this.dataList = list;
    }

    public void setList(Vector<DishItem> list) {
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
      DishItem o = (DishItem) dataList.get(position);
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

  public class DishGridAdapter extends BaseAdapter {

    private Vector<DishItem> dataList;
    
    public DishGridAdapter(Vector<DishItem> list) {
      this.dataList = list;
    }

    public void setList(Vector<DishItem> list) {
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
        v = vi.inflate(R.layout.dishgriditem, null);
      }
      DishItem o = (DishItem) dataList.get(position);
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
          //dataList.get(pos).id;
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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.dishlist);

    setTopButtons();
    setlabelAdapter();
    setDishsAdapter();
  }

  private void setDishsAdapter() {

    Vector<DishItem> dishList = new Vector<DishItem>();
    DishItem element0 = new DishItem(0, "name:chicken", "price:180");
    DishItem element1 = new DishItem(1, "name:chicken1", "price:181");
    DishItem element2 = new DishItem(2, "name:chicken2", "price:182");
    DishItem element3 = new DishItem(3, "name:chicken3", "price:183");
    DishItem element4 = new DishItem(4, "name:chicken4", "price:184");
    dishList.addElement(element0);
    dishList.addElement(element1);
    dishList.addElement(element2);
    dishList.addElement(element3);
    dishList.addElement(element4);
    DishAdapter dishAdapter = new DishAdapter(dishList);

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

    Vector<LabelItem> labelList = new Vector<LabelItem>();
    LabelItem element0 = new LabelItem(0, getResources().getString(R.string.hotdishs));
    LabelItem element1 = new LabelItem(1, getResources().getString(R.string.cooldishs));
    LabelItem element2 = new LabelItem(2, getResources().getString(R.string.stapledishs));
    LabelItem element3 = new LabelItem(3, getResources().getString(R.string.snacksdishs));
    LabelItem element4 = new LabelItem(4, getResources().getString(R.string.drinks));
    labelList.addElement(element0);
    labelList.addElement(element1);
    labelList.addElement(element2);
    labelList.addElement(element3);
    labelList.addElement(element4);

    LabelAdapter labelAdapter = new LabelAdapter(labelList);

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
