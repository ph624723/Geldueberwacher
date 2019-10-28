package com.example.emptytest.Test;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.emptytest.R;

public class EmpListAdapter extends CursorAdapter {

    public EmpListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        //cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView subjectView = view.findViewById(R.id.betreff_textview);
        TextView valueView = view.findViewById(R.id.betrag_textview);
        TextView dateView = view.findViewById(R.id.date_textview);

        String subjectString = cursor.getString(cursor.getColumnIndex("ItemSubject"));
        String valueString = cursor.getString(cursor.getColumnIndex("ItemValue"));
        String dateString = cursor.getString(cursor.getColumnIndex("ItemDate"));

        subjectView.setText(subjectString);
        valueView.setText(valueString);
        dateView.setText(dateString);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        //return cursorInflater.inflate(R.layout.list_item, parent, false);
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
}