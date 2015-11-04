package com.appcms.fenlei;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;

public class FenleiEXAdapter extends BaseExpandableListAdapter {

	private HashMap<String, ArrayList<AppCmsObj>> map;
	private ArrayList<String> parentList;
	private LayoutInflater inflater;

	public FenleiEXAdapter(ArrayList<AppCmsObj> fens) {
		if (fens != null) {
			map = new HashMap<String, ArrayList<AppCmsObj>>();
			ArrayList<AppCmsObj> softs = new ArrayList<AppCmsObj>();
			ArrayList<AppCmsObj> games = new ArrayList<AppCmsObj>();
			for (AppCmsObj fen : fens) {
				if (fen.getType().equals("soft")) {
					softs.add(fen);
				} else if (fen.getType().equals("game")) {
					games.add(fen);
				}
			}
			map.put(Const.FENLEI_GAME, games);
			map.put(Const.FENLEI_SOFT, softs);
		}
		if (parentList == null) {
			parentList = new ArrayList<String>();
			parentList.add(Const.FENLEI_GAME);
			parentList.add(Const.FENLEI_SOFT);
		}
		inflater = LayoutInflater.from(MyApplication.getContext());
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return map.get(parentList.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		final String key = parentList.get(groupPosition);
		ViewHolderChild holderChild = null;
		if (convertView == null) {
			holderChild = new ViewHolderChild();
			LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fenlei_gridview_child, null);
			holderChild.gridView = (FenleiMyGridView) convertView.findViewById(R.id.gv_fenlei_gridview_child);
			convertView.setTag(holderChild);
		} else {
			holderChild = (ViewHolderChild) convertView.getTag();
		}
		FenleiGvAdapter adapter = new FenleiGvAdapter(map.get(key));
		holderChild.gridView.setAdapter(adapter);
		holderChild.gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MyApplication.currentFragment = Const.FENLEI_INFO_UPDATEVIEW;
				FenleiInfoLVFragment.getInfoID(map.get(key).get(arg2).getId());
				// BaseActivity.updateFragment(Const.FENLEI_INFO_INDEX);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parentList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolderParent holderParent = null;
		if (convertView == null) {
			holderParent = new ViewHolderParent();
			convertView = inflater.inflate(R.layout.fenlei_exlvitem_parent, null);
			holderParent.tvParent = (TextView) convertView.findViewById(R.id.tv_fenlei_exitem_parent);
			convertView.setTag(holderParent);
		} else {
			holderParent = (ViewHolderParent) convertView.getTag();
		}
		holderParent.tvParent.setText(parentList.get(groupPosition));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class ViewHolderParent {
		TextView tvParent;
	}

	class ViewHolderChild {
		FenleiMyGridView gridView;
	}

	public void notifyChange(ArrayList<AppCmsObj> fens) {
		if (fens != null) {
			map.clear();
			ArrayList<AppCmsObj> softs = new ArrayList<AppCmsObj>();
			ArrayList<AppCmsObj> games = new ArrayList<AppCmsObj>();
			for (AppCmsObj fen : fens) {
				if (fen.getType().equals("soft")) {
					softs.add(fen);
				} else if (fen.getType().equals("game")) {
					games.add(fen);
				}
			}
			map.put(Const.FENLEI_GAME, games);
			map.put(Const.FENLEI_SOFT, softs);
			notifyDataSetChanged();
		}
	}
}
