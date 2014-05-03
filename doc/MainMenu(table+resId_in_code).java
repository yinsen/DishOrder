package com.infolands.dishorder;


import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;




public class MainMenu extends Activity {
  
  public static final int MENU_RES_ID = 0x7f05a000;
    
  private Vector<TextView> menuButton = new Vector<TextView>();
  
  private TableLayout table = null;
  
  

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        table = (TableLayout) (this.findViewById(R.id.mainmenu));
        
        // 根据服务器配置的menu数，计算table的行数和列数
        int rowNum = 1;
        int colNum = 1;
        int menuNum = ((DishApplication)getApplicationContext()).menuList.size(); 
        
        double num = Math.sqrt(menuNum);
        colNum = rowNum = (int)num;
        if (colNum < num) {
          colNum ++;
        }
        if (colNum * rowNum < menuNum) {
          rowNum ++;
        }
        
        DishApplication app = (DishApplication)getApplicationContext();
        for(int i=0; i<rowNum; i++){
             TableRow row=new TableRow(this);
             TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.FILL_PARENT);
             rowParams.weight = 1;
             row.setLayoutParams(rowParams);
             for(int j=0; j<colNum && i*colNum+j < menuNum; j++){//添加列，每个列元素都用一个LinearLayout包装
               TextView element = new TextView(this); 
               String name = app.menuList.get(i*colNum + j).name;
               element.setId(MENU_RES_ID + i*colNum+j);//所有动态设置的id都从'a'开始
               element.setText(name);
               element.setTextSize(30);
               element.setTextColor(0xFFFF0000); 
               int id = getResources().getIdentifier(getPackageName()+":drawable/" + app.menuList.get(i*colNum + j).img, null,null);
               element.setBackgroundResource(id);
               
               LinearLayout col = new LinearLayout(this);
               LinearLayout.LayoutParams colParams = new LinearLayout.LayoutParams(160, 100, 1);
               col.setLayoutParams(colParams);
               col.addView(element);  
               
               row.addView(col);
             }  
             table.addView(row);//添加行  
         }  
        
        for ()
        menuButton = (TextView) this.findViewById(R.id.hotbt);
        hotButton.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
            try {
              ((DishApplication)getApplicationContext()).currMenu = "menu_id_11";
              
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_VIEW);
              String url = "dishlist://";
              intent.setData(Uri.parse(url));
              
              startActivity(intent);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
    }
}