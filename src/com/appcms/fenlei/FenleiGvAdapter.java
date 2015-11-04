package com.appcms.fenlei;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;

public class FenleiGvAdapter extends BaseAdapter {

	private ArrayList<AppCmsObj> gvList;
	private LayoutInflater inflater;

	public FenleiGvAdapter(ArrayList<AppCmsObj> mgvList) {
		if (mgvList != null) {
			this.gvList = mgvList;
		}
		inflater = LayoutInflater.from(MyApplication.getContext());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gvList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return gvList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.fenlei_gridviewitem_child, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_fenlei_gridviewitem_child);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_fenlei_gridviewitem_child);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap bm = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + gvList.get(position).getLogoName());
		if (bm != null) {
			holder.iv.setImageBitmap(bm);
		} else {
			holder.iv.setImageResource(R.drawable.icon);
		}
		holder.tv.setText(gvList.get(position).getTitle());
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
	}
}
