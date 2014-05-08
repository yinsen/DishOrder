package com.infolands.dishorder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UpdateDataActivity extends Activity {

  private class UpdateOption {
    public String introduce = "";
    public int stringId;
    
    public UpdateOption(String intr, int id) {
      introduce = intr;
      stringId = id;
    }
  }
  
  private class UpdateOptionsAdapter extends BaseAdapter {

    public UpdateOptionsAdapter(ArrayList<UpdateOption> list) {
      updateOptionsList = list;
    }

    public void setList(ArrayList<UpdateOption> list) {
      updateOptionsList = list;
      notifyDataSetChanged();
    }

    public int getCount() {
      return updateOptionsList.size();
    }

    public Object getItem(int position) {
      return updateOptionsList.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.updateitem, null);
      }
      UpdateOption o = (UpdateOption) updateOptionsList.get(position);
      if (o != null) {
        TextView updateintroduce = (TextView) v.findViewById(R.id.updateintroduce);
        Button download = (Button) v.findViewById(R.id.downloadBt);
        if (updateintroduce != null) {
          updateintroduce.setText(o.introduce);
          handleDownload(download, o);
        }
      }

      return v;
    }

    private void handleDownload(final Button downloadButton, final UpdateOption updateOption) {

      downloadButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          //根据本item对应哪个说明字符串，来确定本item对应下载图片还是文字数据
          if (updateOption.stringId == R.string.updatecharsdata) {
            
          }
          else {
            
          }
        }
      });
    }
  }

  private ArrayList<UpdateOption> updateOptionsList = new ArrayList<UpdateOption>();;
  private UpdateOptionsAdapter updateOptionsAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.update);
    UpdateOption option1 = new UpdateOption(this.getResources().getString(R.string.updatecharsdata), R.string.updatecharsdata);
    updateOptionsList.add(option1);
    UpdateOption option2 = new UpdateOption(this.getResources().getString(R.string.updateimgdata), R.string.updateimgdata);
    updateOptionsList.add(option2);

    updateOptionsAdapter = new UpdateOptionsAdapter(updateOptionsList);

    ListView dishResultView = (ListView) findViewById(R.id.updateList);
    dishResultView.setAdapter(updateOptionsAdapter);

    setTopButtons();
  }

  private void setTopButtons() {
    Button backBtn = (Button) findViewById(R.id.backbt2);
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
  }

}