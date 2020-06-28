package com.nellem.practice04.mains_oneperson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nellem.practice04.R;

import java.util.ArrayList;

public class OnepersonAdapter extends BaseAdapter {
    ArrayList<Oneperson_LvItem> itemlist = new ArrayList<>();

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
            view = inflater.inflate(R.layout.activity_mains_oneperson_listitem, viewGroup, false);
        }

        TextView no = (TextView)view.findViewById(R.id.tvWriter);
        TextView region = (TextView)view.findViewById(R.id.tvRegion);
        TextView title = (TextView)view.findViewById(R.id.tvTitle);
        TextView writer = (TextView)view.findViewById(R.id.tvWriter);
        TextView date = (TextView)view.findViewById(R.id.tvDate);

        Oneperson_LvItem listitem = itemlist.get(i);

        no.setText(listitem.getNo());
        region.setText(listitem.getRegion());
        title.setText(listitem.getTitle());
        writer.setText(listitem.getWriter());
        date.setText(listitem.getDate());

        return view;
    }

    public void addItem(String no, String region, String title, String writer, String date) {
        Oneperson_LvItem item = new Oneperson_LvItem();
        item.setNo(no);
        item.setRegion(region);
        item.setTitle(title);
        item.setWriter(writer);
        item.setDate(date);

        itemlist.add(item);
    }
}
