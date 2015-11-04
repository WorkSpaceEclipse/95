package com.appcms.recommend;

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

public class RecFragment extends BaseFragment {
	public static ArrayList<AppCmsObj> apps;
	public static ListView lv;
	public static MyApplication application;
	public static int errorCount = 0;

	private int mCurIndex = -1;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private static boolean mHasLoadedOnce = false;
	private View view;
	private static int page = 0;// 分页
	private boolean turnPage;
	private static boolean isUpdata = false;// 防止多次滑动更新分页
	private static RecommendLvAdapter adapter;

	public static RecFragment newInstance(int index) {
		// 静态工厂方法需要一个int型的值来初始化fragment的参数， 然后返回新的fragment到调用者
		// 方便调用，构时不易出错
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		RecFragment fragment = new RecFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.recommend_lv, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		lv = (ListView) view.findViewById(R.id.lv_recommend);
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

		LogUtil.i(Const.RECOMMOND_JINGXUAN_URL + page);
		JsonDownLoad.getJson(Const.RECOMMOND_JINGXUAN_URL + page, false, new JsonCallBack() {
			@Override
			public void jsonBack(String jsonStr) {
				apps.addAll(JsonTools.getCms(jsonStr));
				parseJson(jsonStr);
			}
		});

	}

	public static void initData() {
		LogUtil.i(Const.RECOMMOND_JINGXUAN_URL + page);
		JsonDownLoad.getJson(Const.RECOMMOND_JINGXUAN_URL + page, true, new JsonCallBack() {
			@Override
			public void jsonBack(String jsonStr) {
				apps = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});
	}

	public static void parseJson(final String jsonStr) {
		if (Tools.checkCurrentFragment(Const.RECOMMOND_UPDATEVIEW)) {
			MyApplication.getHandler().sendEmptyMessage(Const.RECOMMOND_UPDATEVIEW);
		}
		// downImg参数不能用APPS。会导致重新下载所有appimg，要下载新加载的apps
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				if (Tools.checkCurrentFragment(Const.RECOMMOND_UPDATEVIEW)) {
					MyApplication.getHandler().sendEmptyMessage(Const.RECOMMOND_UPDATEVIEW);
				}
				isUpdata = false;
			}
		});
	}

	public static void updataView() {
		isUpdata = false;
		LogUtil.i("rec_updateLV");
		if (null != apps && apps.size() != 0 && !mHasLoadedOnce) {
			mHasLoadedOnce = true;
			adapter = new RecommendLvAdapter(apps);
			lv.setAdapter(adapter);
		}
		try {
			adapter.notifyChange(apps);
			BaseActivity.updateFragment(Const.RECOMMEND_INDEX);

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
			LogUtil.i("Recommend第" + errorCount + "次更新UI");
			initData();
		} else {
			page = 0;
			errorCount = 0;
			MyApplication.getHandler().sendEmptyMessage(Const.ERRORCOUNT);
		}
		errorCount++;
	}
}
