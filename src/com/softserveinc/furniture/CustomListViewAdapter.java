package com.softserveinc.furniture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<FurnitureListItem> {

	Context context;

	private List<FurnitureListItem> items = new ArrayList<FurnitureListItem>();
	private List<Integer> selectedItems = new ArrayList<Integer>();

	public CustomListViewAdapter(Context context, int resourceId,
			List<FurnitureListItem> items) {
		super(context, resourceId, items);
		this.context = context;
		this.items = items;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.furnitureTitle);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.furnitureImage);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (context instanceof CameraActivity) {
			holder.txtTitle.setVisibility(View.GONE);
		}
		holder.txtTitle.setText(items.get(position).getTitle());
		holder.imageView.setImageResource(items.get(position).getImageId());
		
		if (selectedItems.contains(position)){
			holder.imageView.setBackgroundResource(R.drawable.frame);
		} else {
			holder.imageView.setBackgroundResource(Color.TRANSPARENT);
		}

		return convertView;
	}

	public void addOrRemoveSelectedItem(int selectedItem) {
		if (selectedItems.contains(selectedItem)){
			selectedItems.remove((Integer)selectedItem);
		} else {
			selectedItems.add(selectedItem);
		}
		notifyDataSetChanged();
	}
}