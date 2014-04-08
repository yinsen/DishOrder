package com.infolands.dishorder;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;




public class MainMenu extends Activity {
    
  private ImageView hotButton = null;
  private ImageView coolButton = null;
  private ImageView stapleButton = null;
  private ImageView snacksButton = null;

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        hotButton = (ImageView) this.findViewById(R.id.hotbt);
        hotButton.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
            try {
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