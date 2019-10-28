package com.example.emptytest.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.emptytest.R;
import com.example.emptytest.datamanagement.CostItem;

import java.util.ArrayList;

public class CostItemArrayAdapter extends ArrayAdapter<CostItem> implements View.OnClickListener{

        private ArrayList<CostItem> dataSet;
        Context mContext;

        // View lookup cache
        private static class ViewHolder {
            TextView txtSubject;
            TextView txtValue;
            TextView txtDate;
            TextView txtInfos;
        }

        public CostItemArrayAdapter(ArrayList<CostItem> data, Context context) {
            super(context, R.layout.list_item, data);
            this.dataSet = data;
            this.mContext=context;
        }

        @Override
        public void onClick(View v) {
            TextView  betragView = v.findViewById(R.id.betrag_textview);
            betragView.setText(v.getTag()+"");

           // int position=(Integer) v.getTag();
           // Object object= getItem(position);
           // CostItem item=(CostItem) object;


        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            CostItem costItem = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                viewHolder.txtSubject = (TextView) convertView.findViewById(R.id.betreff_textview);
                viewHolder.txtValue = (TextView) convertView.findViewById(R.id.betrag_textview);
                viewHolder.txtDate = (TextView) convertView.findViewById(R.id.date_textview);
                viewHolder.txtInfos = (TextView) convertView.findViewById(R.id.infos_textview);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            //result.startAnimation(animation);
            lastPosition = position;

            viewHolder.txtSubject.setText(costItem.subjectString());
            viewHolder.txtValue.setText(costItem.valueString());
            viewHolder.txtDate.setText(costItem.dateString());
            viewHolder.txtInfos.setText(costItem.infosString().equals("") ? mContext.getResources().getString(R.string.disclaimer_no_infos) : costItem.infosString());

            //viewHolder.info.setOnClickListener(this);
            //viewHolder.info.setTag(position);

            // Return the completed view to render on screen
            return convertView;
        }
    }

