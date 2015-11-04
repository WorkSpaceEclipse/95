package com.appcms.topic;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;

public class TopicListAdapter extends BaseAdapter {

	private ArrayList<AppCmsObj> apps;
	private LayoutInflater inflater;

	public TopicListAdapter(ArrayList<AppCmsObj> apps) {
		if (apps != null) {
			this.apps = apps;
		}
		inflater = LayoutInflater.from(MyApplication.getContext());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.topic_fenlei_lvitem, null);
			holder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_topicitem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap bm = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + apps.get(position).getLogoName());
		if (bm != null) {
			holder.ivLogo.setImageBitmap(bm);
		} else {
			holder.ivLogo.setImageResource(R.drawable.icon);
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView ivLogo;
	}
}
