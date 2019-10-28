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
import android.widget.ListView;
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
import com.google.android.material.tabs.TabLayout;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler dbHandler;
    EmpListAdapter empListAdapter;
    private CostItemArrayAdapter arrayAdapter;
    private SortableList<CostItem> costItems = new SortableList<>(CostItem.DateComparator);
    private Categories.Category selectedCategory;

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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        refreshData();
    }

    public void onCreateItemClick(View view){
        Intent intent = new Intent(this, EditCostItemActivity.class);
        intent.putExtra(EditCostItemActivity.categoryKey,selectedCategory.getId());
        startActivityForResult(intent,EditCostItemActivity.createRequestCode);
    }

    public void addData(int num){
        ListView listView = findViewById(R.id.listView);
        int datacount = listView.getCount() +1 ;

        for (int i=0; i<num;i++){
            Date d = new Date();
            d.setYear((int)(Math.random()*20+100));
            d.setMonth((int)(Math.random()*12+1));
            d.setDate((int)(Math.random()*28+1));
            CostItem item = new CostItem((datacount++) + ". Item", Math.random()*500-250, Categories.NECESSARY, d);
            dbHandler.insertCostItem(item);
            refreshData();
        }
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
        Log.d("Tag", "Received activity finished with codes: " + requestCode+","+resultCode);

        if(requestCode==EditCostItemActivity.editRequestCode || requestCode==EditCostItemActivity.createRequestCode)
        {
            if (resultCode == EditCostItemActivity.changedResultCode){
                Log.d("Tag", "Refreshing data");
                refreshData();
            }
        }
    }
}
