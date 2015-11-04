package com.appcms.fenlei;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.android.appcms.BaseActivity;
import com.android.appcms.BaseFragment;
import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.android.netools.ImgDownLoad;
import com.android.netools.ImgDownLoad.ImgDownLoaded;
import com.android.netools.JsonDownLoad;
import com.android.netools.JsonDownLoad.JsonCallBack;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;
import com.appcms.tools.JsonTools;
import com.appcms.tools.LogUtil;
import com.appcms.tools.Tools;

public class FenleiFragment extends BaseFragment {
	public static ArrayList<AppCmsObj> fens;
	public static ArrayList<Fragment> fragments;
	private static ExpandableListView ex;
	public static MyApplication application;
	public static int errorCount = 0;

	private int mCurIndex = -1;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private static boolean mHasLoadedOnce;
	private View view;
	private static int page = 0;// 分页
	private boolean turnPage;
	private static boolean isUpdata = false;// 防止多次滑动更新分页
	private static FenleiEXAdapter adapter;

	public static FenleiFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		FenleiFragment fragment = new FenleiFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fenlei_exlv_parent, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		ex = (ExpandableListView) view.findViewById(R.id.ex_fenlei);
		ex.setGroupIndicator(null);
		ex.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				return true;
			}
		});
		application = (MyApplication) getActivity().getApplication();
		isPrepared = true;
		lazyLoad();
		return view;
	}

	private static void initData() {

		JsonDownLoad.getJson(Const.FENLEI_URL, true, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				parseJson(jsonStr);
			}
		});

	}

	public static void parseJson(final String jsonStr) {
		fens = JsonTools.getCms(jsonStr);
		if (Tools.checkCurrentFragment(Const.FENLEI_UPDATEVIEW)) {
			MyApplication.getHandler().sendEmptyMessage(Const.FENLEI_UPDATEVIEW);
		}
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				isUpdata = false;
				LogUtil.i("FENLEI_UPDATEVIEW");
				if (Tools.checkCurrentFragment(Const.FENLEI_UPDATEVIEW)) {
					MyApplication.getHandler().sendEmptyMessage(Const.FENLEI_UPDATEVIEW);
				}
			}
		});
	}

	public static void updataView() {
		LogUtil.i("fensize：" + fens.size());
		if (!mHasLoadedOnce && null != fens && fens.size() != 0) {
			LogUtil.i("Fenlei_updateLV");
			mHasLoadedOnce = true;
			adapter = new FenleiEXAdapter(fens);
			ex.setAdapter(adapter);
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				ex.expandGroup(i);
			}
		}
		adapter.notifyChange(fens);
		BaseActivity.updateFragment(Const.FENLEI_INDEX);
		isUpdata = false;
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		} else {
			initData();
		}

	}

	protected static void reUpdata() {
		if (errorCount < Const.ERRORCOUNT) {
			LogUtil.i("Fenlei第" + errorCount + "次更新UI");
			initData();
		} else {
			errorCount = 0;
			MyApplication.getHandler().sendEmptyMessage(Const.ERRORCOUNT);
		}
		errorCount++;
	}
}
