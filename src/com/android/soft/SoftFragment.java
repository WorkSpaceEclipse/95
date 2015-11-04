package com.android.soft;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

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

public class SoftFragment extends BaseFragment {
	public static ArrayList<AppCmsObj> apps;
	public static ListView lv;
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
	private static SoftLvAdapter adapter;

	public static SoftFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		SoftFragment fragment = new SoftFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.soft_lv, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		lv = (ListView) view.findViewById(R.id.lv_soft);
		application = (MyApplication) getActivity().getApplication();
		isPrepared = true;
		initLinster();
		lazyLoad();
		return view;
	}

	private void initLinster() {

		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (!isUpdata && turnPage && view.getLastVisiblePosition() == view.getCount() - 1) {
						isUpdata = true;
						page++;
						// initData();
						addData();
					} else if (turnPage) {
						MyApplication.getHandler().sendEmptyMessage(Const.ISUPDATA);
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				turnPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

	}

	protected void addData() {

		JsonDownLoad.getJson(Const.SOFT_URL + page, false, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				apps.addAll(JsonTools.getCms(jsonStr));
				parseJson(jsonStr);
			}
		});

	}

	public static void initData() {
		JsonDownLoad.getJson(Const.SOFT_URL + page, true, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				apps = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});
	}

	public static void parseJson(final String jsonStr) {
		if (Tools.checkCurrentFragment(Const.SOFT_UPDATEVIEW)) {
			MyApplication.getHandler().sendEmptyMessage(Const.SOFT_UPDATEVIEW);
		}
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				isUpdata = false;
				if (Tools.checkCurrentFragment(Const.SOFT_UPDATEVIEW)) {
					MyApplication.getHandler().sendEmptyMessage(Const.SOFT_UPDATEVIEW);
				}
			}
		});
	}

	public static void updataView() {
		LogUtil.i("Soft_updateLV");
		isUpdata = false;
		if (!mHasLoadedOnce && null != apps && apps.size() != 0) {
			mHasLoadedOnce = true;
			adapter = new SoftLvAdapter(apps);
			lv.setAdapter(adapter);
		}
		try {
			adapter.notifyChange(apps);
			BaseActivity.updateFragment(Const.SOFT_INDEX);
		} catch (Exception e) {
			reUpdata();
			e.printStackTrace();
		}

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
			LogUtil.i("Soft第" + errorCount + "次更新UI");
			initData();
		} else {
			apps = new ArrayList<AppCmsObj>();
			errorCount = 0;
			MyApplication.getHandler().sendEmptyMessage(Const.ERRORCOUNT);
		}
		errorCount++;
	}
}
