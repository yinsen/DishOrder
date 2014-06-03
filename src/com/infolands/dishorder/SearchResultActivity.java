package com.infolands.dishorder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultActivity extends Activity {

  private class DishSearchAdapter extends BaseAdapter implements Filterable {

    private class DishSearchFilter extends Filter {

      @Override
      protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        dishSearchList.clear();
        if (prefix != null && prefix.length() > 0) {
          DishApplication app = (DishApplication) getApplicationContext();
          for (int i = 0; i < app.dishList.size(); i++) {
            if (app.dishList.get(i).name.startsWith(prefix.toString())) {
              dishSearchList.add(app.dishList.get(i));
            }
          }

          results.values = dishSearchList;
          results.count = dishSearchList.size();
        }
        else {
          DishApplication app = (DishApplication) getApplicationContext();
          for (int i = 0; i < app.dishList.size(); i++) {
              dishSearchList.add(app.dishList.get(i));
          }
        }

        return results;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        
        dishSearchAdapter.setList(dishSearchList);
        
        //noinspection unchecked
        if (results.count > 0) {
          notifyDataSetChanged();
          
        }
        else {
          notifyDataSetInvalidated();
        }
      }
    }

    private DishSearchFilter mFilter;

    public DishSearchAdapter(ArrayList<DataItem.DishItem> list) {
      dishSearchList = list;
    }

    public void setList(ArrayList<DataItem.DishItem> list) {
      dishSearchList = list;
      notifyDataSetChanged();
    }

    public Filter getFilter() {
      if (mFilter == null) {
        mFilter = new DishSearchFilter();
      }
      return mFilter;
    }

    public int getCount() {
      return dishSearchList.size();
    }

    public Object getItem(int position) {
      return dishSearchList.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.searchitem, null);
      }
      DataItem.DishItem o = (DataItem.DishItem) dishSearchList.get(position);
      if (o != null) {
        TextView nameView = (TextView) v.findViewById(R.id.searchDishItem);
        if (nameView != null) {
          nameView.setText(o.name);
          nameView.setBackgroundColor(Color.DKGRAY);
          handleDishOrder(nameView, o);
        }
      }

      return v;
    }

    private void handleDishOrder(final TextView orderButton, final DataItem.DishItem dishItem) {

      orderButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
          ((DishApplication) getApplicationContext()).currDishId = dishItem.dish_id;
          try {
            Intent intent = new Intent(SearchResultActivity.this, OrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("dish_id", dishItem.dish_id);
            intent.putExtra("order_item", bundle);
            startActivity(intent);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }
  
  private ArrayList<DataItem.DishItem> dishSearchList = new ArrayList<DataItem.DishItem>();;
  private DishSearchAdapter dishSearchAdapter;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.searchlist);

    // Get the intent, verify the action and get the query
    //      Intent intent = getIntent();
    //      if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
    //        String query = intent.getStringExtra(SearchManager.QUERY);
    //        doSearch(query);
    //      }

    DishApplication app = (DishApplication) getApplicationContext();
    for (int i = 0; i < app.dishList.size(); i++) {
        dishSearchList.add(app.dishList.get(i));
    }
    dishSearchAdapter = new DishSearchAdapter(dishSearchList);
    ListView dishResultView = (ListView) findViewById(R.id.searchResultList);
    dishResultView.setAdapter(dishSearchAdapter);
    
    setTopButtons();
  }

  private void setTopButtons() {

    EditText input = (EditText) findViewById(R.id.searchbox);
    input.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {
        final String query = s.toString().trim();
        doSearch(query);
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

    });

  }

  private void doSearch(final String q) {

    
    
    dishSearchAdapter.getFilter().filter(q);

    

  }

}