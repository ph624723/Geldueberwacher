package com.example.emptytest.Views.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.emptytest.Generic.SortableList;
import com.example.emptytest.R;
import com.example.emptytest.Views.CostItemArrayAdapter;
import com.example.emptytest.Views.EditCostItemActivity;
import com.example.emptytest.datamanagement.Categories;
import com.example.emptytest.datamanagement.CostItem;
import com.example.emptytest.datamanagement.DatabaseHandler;

import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Categories.Category category;
    private DatabaseHandler dbHandler;

    public static TabViewFragment newInstance(int catId) {
        TabViewFragment fragment = new TabViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, catId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int catId = getArguments().getInt(ARG_SECTION_NUMBER);
        category = Categories.getCategory(catId);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        return root;
    }
}