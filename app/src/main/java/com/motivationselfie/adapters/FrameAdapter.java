package com.motivationselfie.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.motivationselfie.R;
import com.motivationselfie.utils.Preference;

@SuppressWarnings("ALL")
public class FrameAdapter extends BaseAdapter {
    private Context ctx;
    private TypedArray imgs;
    private Bitmap bitmap;

    public FrameAdapter(Context ctx, TypedArray imgs, Bitmap bitmap) {
        this.ctx = ctx;
        this.imgs = imgs;
        this.bitmap = bitmap;
    }

    @Override
    public int getCount() {
        return 35;
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.view_frames, parent, false);
            holder.img_frames = (ImageView) convertView.findViewById(R.id.img_frames);
            holder.lock = (ImageView) convertView.findViewById(R.id.lock);
            holder.img_picture = (ImageView) convertView.findViewById(R.id.img_picture);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img_frames.setTag(position);
        holder.lock.setTag(position);
        holder.img_picture.setTag(position);

        if(!Preference.getInAppFromPref(ctx)){
            if (position >= 0 && position < 3) {
                holder.lock.setVisibility(View.INVISIBLE);
            } else {
                holder.lock.setVisibility(View.VISIBLE);
            }
        }else{
            holder.lock.setVisibility(View.INVISIBLE);
        }

        imgs.getResourceId(position, -1);
        holder.img_frames.setImageResource(imgs.getResourceId(position, -1));
        holder.img_picture.setImageBitmap(bitmap);
        //imgs.recycle();

        return convertView;
    }

    private class ViewHolder {
        ImageView img_frames;
        ImageView lock;
        ImageView img_picture;
    }
}