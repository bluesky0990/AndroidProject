package com.nellem.practice04.realtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nellem.practice04.R;
import com.nellem.practice04.mains_oneperson.Oneperson_LvItem;

import java.util.ArrayList;

public class RealtimeChatAdapter extends BaseAdapter {
    public static ArrayList<Realtime_LvItem> itemlist = new ArrayList<>();

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int i) {
        return itemlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context c = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_realtime_chat_listview, viewGroup, false);
        }

        TextView tvChat = (TextView)view.findViewById(R.id.tvText);

        Realtime_LvItem listitem = itemlist.get(i);

        tvChat.setText(listitem.getMessage());

        return view;
    }

    public void addItem(String message) {
        Realtime_LvItem item = new Realtime_LvItem();
        item.setMessage(message);

        itemlist.add(item);
    }
}
