package com.infolands.dishorder;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


public class MenuActivity extends Activity {
  
  public class MenuGridAdapter extends BaseAdapter {
    private ArrayList<DataItem.MenuItem> menuList;
    
    public MenuGridAdapter(ArrayList<DataItem.MenuItem> list) {
      this.menuList = list;
    }

    public void setList(ArrayList<DataItem.MenuItem> list) {
      this.menuList = list;
      notifyDataSetChanged();
    }

    public int getCount() {
      return menuList.size();
    }

    public Object getItem(int position) {
      return menuList.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.menugriditem, null);
      }
      DataItem.MenuItem o =  menuList.get(position);
      
      if (o != null) {
        TextView menuBt = (TextView) v.findViewById(R.id.menuBt);
        if (menuBt != null) {
          MenuActivity activity = (MenuActivity)(v.getContext());
          int id = activity.getResources().getIdentifier(activity.getPackageName()+":drawable/" + o.img, null,null);
          menuBt.setBackgroundResource(id);
          
          handleMenu(menuBt, o);
        }
      }

      return v;
    }
 
    private void handleMenu(final TextView menuButton, final DataItem.MenuItem menuItem) {

      menuButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          DishApplication app = ((DishApplication)getApplicationContext());
          app.setCurrMenu(menuItem.menu_id);
          
          int num=0;
          for (; num<app.dishList.size(); num++) {
            if (app.dishList.get(num).menu_id.equalsIgnoreCase(app.currMenu)) {
              break;
            }
          }
          if (num >= app.dishList.size())
            return;
          
          try {
            Intent intent = new Intent(MenuActivity.this,DishListActivity.class);  
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
        setContentView(R.layout.menu);
        
        setmenuAdapter();
        setLanguageButton();
    }
  
  private void setmenuAdapter() {
    DishApplication app = (DishApplication)getApplicationContext();
    MenuGridAdapter menuGridAdapter = new MenuGridAdapter(app.menuList);

    GridView menuGridView = (GridView) findViewById(R.id.menuGrid);
    menuGridView.setAdapter(menuGridAdapter);
    menuGridView.setVisibility(View.VISIBLE);
  }

  private void setLanguageButton() {
    Button languageBt = (Button) findViewById(R.id.languageBt);
    languageBt.setOnClickListener(new View.OnClickListener() {
  
      @Override
      public void onClick(View v) {
        new Thread() {
  
          public void run() {
            
            
          }
        }.start();
      }
    });
  }
    
}