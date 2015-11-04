package com.android.manager;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

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

public class ManagerFragment extends BaseFragment {
	public static ArrayList<AppCmsObj> fens;
	public static ArrayList<Fragment> fragments;
	private static ExpandableListView ex;
	public static MyApplication application;
	public static int errorCount = 0;

	private int mCurIndex = -1;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private boolean mHasLoadedOnce;
	private View view;

	public static ManagerFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		ManagerFragment fragment = new ManagerFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.manager_exlv, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		ex = (ExpandableListView) view.findViewById(R.id.manager_fenlei);
		ex.setGroupIndicator(null);
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
		MyApplication.getHandler().sendEmptyMessage(Const.FENLEI_UPDATEVIEW);
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				if (result && Tools.checkCurrentFragment(Const.FENLEI_UPDATEVIEW)) {
					MyApplication.getHandler().sendEmptyMessage(Const.FENLEI_UPDATEVIEW);
				} else {
					errorCount++;
					if (errorCount == 5) {
						application.exit();
					}
					LogUtil.i("Fenlei第" + errorCount + "次更新UI");
					initData();
				}
			}
		});
	}

	public static void updataView() {
		LogUtil.i("fensize：" + fens.size());
		if (fens.size() != 0) {
			LogUtil.i("Fenlei_updateLV");
			ManagerEXAdapter adapter = new ManagerEXAdapter(fens);
			ex.setAdapter(adapter);
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				ex.expandGroup(i);
			}
			BaseActivity.updateFragment(Const.FENLEI_INDEX);
		}
	}

	@Override
	protected void lazyLoad() {

		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		} else {
			mHasLoadedOnce = true;
			initData();
		}

	}

}
