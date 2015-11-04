package com.appcms.search;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.android.netools.ApkDownLoad;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;

public class SearchLvAdapter extends BaseAdapter {

	private ArrayList<AppCmsObj> apps;
	private LayoutInflater inflater;

	public SearchLvAdapter(ArrayList<AppCmsObj> apps) {
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
			convertView = inflater.inflate(R.layout.recommend_lvitem, null);
			convertView.setClickable(true);
			holder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_reclvitem_applogo);
			holder.tvAppname = (TextView) convertView.findViewById(R.id.tv_reclvitem_appname);
			holder.tvTimesSize = (TextView) convertView.findViewById(R.id.tv_reclvitem_appsize);
			holder.tvIntro = (TextView) convertView.findViewById(R.id.tv_reclvitem_appintro);
			holder.btDown = (Button) convertView.findViewById(R.id.bt_reclvitem_appdown);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApkDownLoad.downApk(MyApplication.getContext(), apps.get(position));
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = Message.obtain();
				msg.what = Const.BASE_GATAPPID;
				msg.obj = apps.get(position).getId();
				MyApplication.getHandler().sendMessage(msg);
			}
		});
		AppCmsObj app = apps.get(position);
		Bitmap bm = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + apps.get(position).getLogoName());
		if (bm != null) {
			holder.ivLogo.setImageBitmap(bm);
		} else {
			holder.ivLogo.setImageResource(R.drawable.icon);
		}
		holder.tvAppname.setText(app.getTitle());
		holder.tvTimesSize.setText(app.getSize());
		holder.tvIntro.setText("ÏÂÔØ´ÎÊý£º" + app.getTimes());
		return convertView;
	}

	class ViewHolder {
		public ImageView ivLogo;
		public TextView tvAppname;
		public TextView tvTimesSize;
		public TextView tvIntro;
		public Button btDown;
	}

	public void notifyChange(ArrayList<AppCmsObj> apps2) {
		if (apps2 != null) {
			this.apps = (ArrayList<AppCmsObj>) apps2.clone();
			notifyDataSetChanged();
		}
	}

}
