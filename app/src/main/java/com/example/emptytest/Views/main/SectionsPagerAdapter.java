package com.example.emptytest.Views.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.emptytest.Generic.SortableList;
import com.example.emptytest.datamanagement.Categories;
import com.example.emptytest.datamanagement.CostItem;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a TabViewFragment (defined as a static inner class below).
        return TabViewFragment.newInstance(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Categories.getCategory(position).getName();
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}