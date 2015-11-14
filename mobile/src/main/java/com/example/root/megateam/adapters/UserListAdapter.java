package com.example.root.megateam.adapters;
import java.util.List;

/**
 * Created by root on 11/14/15.
 */
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.megateam.R;
import com.example.root.megateam.UsersActivity;
import com.example.root.megateam.model.Person;


public class UserListAdapter extends WearableListView.Adapter {

    private List<Person> pData;

    private static class ItemViewHolder extends WearableListView.ViewHolder {
        TextView text;
        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public UserListAdapter(List<Person> person) {
        pData = person;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_user_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
        // Get item
        Person item = pData.get(position);

        // Update TextView
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        itemViewHolder.text.setText(item.getNickname());
    }

    @Override
    public int getItemCount() {
        return pData.size();
    }
}
