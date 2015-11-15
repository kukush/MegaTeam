package com.example.root.megateam.adapter;

/**
 * Created by root on 11/15/15.
 */

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.megateam.Person;
import com.example.root.megateam.R;

import java.util.List;

/**
 * Created by root on 11/15/15.
 */

public  class UserAdapter extends WearableListView.Adapter {
    private List<Person> pData;
    //private String[] mDataset;
   // private final Context mContext;
   // private final LayoutInflater mInflater;

    // Provide a suitable constructor (depends on the kind of dataset)
   // public UserAdapter(Context context, String[] dataset) {
        //mContext = context;
        //mInflater = LayoutInflater.from(context);
      //  mDataset = dataset;
    //}
    public UserAdapter(List<Person> persons) {
        pData = persons;
    }
    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
   /* @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate our custom layout for list items
        return new ItemViewHolder(mInflater.inflate(R.layout.user_item, null));
    }*/

    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item, viewGroup, false));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        // retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        // replace text contents
        view.setText(pData.get(position).getNickname());
        // replace list item's metadata
        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return pData.size();
    }
}