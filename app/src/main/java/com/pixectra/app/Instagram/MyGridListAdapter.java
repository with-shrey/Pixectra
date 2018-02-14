package com.pixectra.app.Instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pixectra.app.R;

import java.util.ArrayList;

public class MyGridListAdapter extends BaseAdapter {
	private ArrayList<String> imageThumbList;
	private LayoutInflater inflater;
	Context context;
	private ImageLoader imageLoader;

	public MyGridListAdapter(Context context, ArrayList<String> imageThumbList) {
	    this.context=context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageThumbList = imageThumbList;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return imageThumbList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.image_recycler_item, null);
        Holder holder = new Holder();
        holder.ivPhoto = (ImageView) view.findViewById(R.id.ListIcon);
        Glide.with(context).load(imageThumbList.get(position)).into(holder.ivPhoto);
        return view;
	}

	private class Holder {
		private ImageView ivPhoto;
	}

}
