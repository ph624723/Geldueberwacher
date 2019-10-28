package com.example.emptytest.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.emptytest.R;
import com.example.emptytest.Test.EmpListAdapter;
import com.example.emptytest.datamanagement.Categories;
import com.example.emptytest.datamanagement.CostItem;
import com.example.emptytest.datamanagement.DatabaseHandler;
import com.example.emptytest.Generic.SortableList;
import com.example.emptytest.datamanagement.Timespans;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler dbHandler;
    EmpListAdapter empListAdapter;
    private CostItemArrayAdapter arrayAdapter;
    private SortableList<CostItem> costItems = new SortableList<>(CostItem.DateComparator);
    private Categories.Category selectedCategory;
    private Timespans.Timespan selectedTimespan;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getContentDescription().equals(getResources().getString(R.string.cat_key_official))){
                    selectedCategory = Categories.OFFICIAL;
                }else if (tab.getContentDescription().equals(getResources().getString(R.string.cat_key_necessary))){
                    selectedCategory = Categories.NECESSARY;
                }else if (tab.getContentDescription().equals(getResources().getString(R.string.cat_key_fun))){
                    selectedCategory = Categories.FUN;
                }else{
                    Log.d("ERROR", "Unknown tab was selected, no corresponding category");
                }
                refreshData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SwipeMenuListView listView = findViewById(R.id.listView);
        dbHandler = DatabaseHandler.getmInstance(getApplicationContext());
        selectedCategory = Categories.OFFICIAL;

        try{
            arrayAdapter = new CostItemArrayAdapter(costItems,this);
            listView.setAdapter(arrayAdapter);
        }catch(Exception e){
            Log.d("ERROR", e.getMessage());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(view, position, id);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                editItem.setWidth(findViewById(R.id.listView).getWidth() / 3);
                // set item title
                editItem.setIcon(R.drawable.edit_logo);
                //editItem.setTitle("Edit");
                // set item title fontsize
                //editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(findViewById(R.id.listView).getWidth() / 3);
                // set a icon
                deleteItem.setIcon(R.drawable.delete_logo);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        onEditItemClick(position);
                        break;
                    case 1:
                        // delete
                        onDeleteItemClick(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // set creator
        listView.setMenuCreator(creator);

        Spinner date_range_spinner = findViewById(R.id.timespan_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.date_ranges));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_range_spinner.setAdapter(dataAdapter);
        date_range_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        selectedTimespan = Timespans.ALL_TIME;
                        break;
                    case 1:
                        selectedTimespan = Timespans.THIS_MONTH;
                        break;
                    case 2:
                        selectedTimespan = Timespans.THIS_WEEK;
                        break;
                    case 3:
                        selectedTimespan = Timespans.LAST_MONTH;
                        break;
                    case 4:
                        selectedTimespan = Timespans.LAST_WEEK;
                        break;
                }
                refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });

        selectedTimespan = Timespans.ALL_TIME;

        refreshData();
    }

    public void onCreateItemClick(View view){
        Intent intent = new Intent(this, EditCostItemActivity.class);
        intent.putExtra(EditCostItemActivity.categoryKey,selectedCategory.getId());
        startActivityForResult(intent,EditCostItemActivity.createRequestCode);
    }

    public void refreshData(){
        new Handler().post(new Runnable() {
            @Override
            public void run(){
                costItems.removeAll(costItems);
                try{
                    costItems.addAll(dbHandler.getData(selectedCategory));
                }catch(Exception e){
                    //Some Exception handling pls
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onListItemClick(View view, int position, long id) {
        TextView infosView = view.findViewById(R.id.infos_textview);
        //infosView.setVisibility(infosView.isShown() ? View.GONE : View.VISIBLE);
        if (infosView.isShown()){
            //Fx.slide_up(this,infosView);
            infosView.setVisibility(View.GONE);
        }else{
            infosView.setVisibility(View.VISIBLE);
            Fx.slide_down(this,infosView);
        }
    }

    public void onEditItemClick(int position){
        int uid = arrayAdapter.getItem(position).getUid();
        Intent intent = new Intent(this, EditCostItemActivity.class);
        intent.putExtra(EditCostItemActivity.uidKey,uid);
        startActivityForResult(intent,EditCostItemActivity.editRequestCode);
    }

    public void onDeleteItemClick(int position){
        final int pos = position;
        AlertDialog.Builder alert = new AlertDialog.Builder(
                this);
        alert.setTitle(getResources().getString(R.string.deleteDialog_titleQuestion));
        alert.setMessage(getResources().getString(R.string.deleteDialog_explanation));
        alert.setPositiveButton(getResources().getString(R.string.deleteDialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onDeleteItemConfirm(pos);
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.deleteDialog_abort), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void onDeleteItemConfirm(int position){
        String name = arrayAdapter.getItem(position).subjectString();
        int uid = arrayAdapter.getItem(position).getUid();
        dbHandler.delete(uid);
        refreshData();
        Toast.makeText(this,getResources().getString(R.string.disclaimer_deleted), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==EditCostItemActivity.editRequestCode || requestCode==EditCostItemActivity.createRequestCode)
        {
            if (resultCode == EditCostItemActivity.changedResultCode){
                refreshData();
            }
        }
    }
}
