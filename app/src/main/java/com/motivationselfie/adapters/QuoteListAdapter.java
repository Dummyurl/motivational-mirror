package com.motivationselfie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.motivationselfie.R;
import com.motivationselfie.modals.QuotesAssests;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class QuoteListAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<QuotesAssests> list_fav_new;

    public QuoteListAdapter(Context ctx, ArrayList<QuotesAssests> list_fav_new) {
        this.ctx = ctx;
        this.list_fav_new = list_fav_new;
    }

    @Override
    public int getCount() {
        return list_fav_new.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.quotes_list_item, parent, false);
            holder.tv_quotes = (TextView) convertView.findViewById(R.id.tv_quotes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_quotes.setTag(position);

        holder.tv_quotes.setText(list_fav_new.get(position).getQuotes() + "\n" + list_fav_new.get(position).getAuthor());

        return convertView;
    }

    private class ViewHolder {
        TextView tv_quotes;
    }
}