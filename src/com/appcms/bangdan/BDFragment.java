package com.appcms.bangdan;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

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

public class BDFragment extends BaseFragment {
	public static ArrayList<AppCmsObj> apps;
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
	private static TabHost tabHost;
	private static BDFragment context;
	private static TabSpec tabSoft;
	private static TabSpec tabGame;
	private static ListView lvSoft;
	private static ListView lvGame;
	private static ArrayList<AppCmsObj> appSoft;
	private static ArrayList<AppCmsObj> appsGame;
	private static boolean isUpdata = false;// 防止多次滑动更新分页
	private static BDLvAdapter adapter;

	// private static String tabUrl = Const.TOP_SOFT_URL;

	public static BDFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		BDFragment fragment = new BDFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.bangdan_tabhost, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		application = (MyApplication) getActivity().getApplication();
		this.context = this;
		isPrepared = true;
		initSoftData();
		initAppData();
		initView();
		initLinster();
		lazyLoad();

		return view;
	}

	private void initAppData() {
		JsonDownLoad.getJson(Const.TOP_GAME_URL + page, true, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				appsGame = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});

	}

	private void initSoftData() {
		JsonDownLoad.getJson(Const.TOP_SOFT_URL + page, true, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				appSoft = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});

	}

	private void initData(String topSoftUrl) {
		JsonDownLoad.getJson(topSoftUrl + page, true, new JsonCallBack() {
			@Override
			public void jsonBack(String jsonStr) {
				apps = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});

	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		tabHost = (TabHost) view.findViewById(R.id.tabhost_bangdan);
		tabHost.setup();
		View SoftView = inflater.inflate(R.layout.bangdan_softlv, tabHost.getTabContentView());
		View GameView = inflater.inflate(R.layout.bangdan_gamelv, tabHost.getTabContentView());
		lvSoft = (ListView) SoftView.findViewById(R.id.lv_bangdan_soft);
		lvGame = (ListView) GameView.findViewById(R.id.lv_bangdan_game);
		tabSoft = tabHost.newTabSpec("soft").setIndicator("软件榜单").setContent(R.id.lv_bangdan_soft);
		tabGame = tabHost.newTabSpec("game").setIndicator("游戏榜单").setContent(R.id.lv_bangdan_game);
		tabHost.addTab(tabSoft);
		tabHost.addTab(tabGame);
		TabWidget tabWidget = tabHost.getTabWidget();
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			// 修改Tabhost高度和宽度
			// tabWidget.getChildAt(i).getLayoutParams().height = 50;
			// tabWidget.getChildAt(i).getLayoutParams().width = 65;
			// 修改显示字体大小
			TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
			tv.setTextSize(15);
			tv.setTextColor(context.getResources().getColorStateList(android.R.color.black));
		}
	}

	private void initLinster() {
		// tabHost.setOnTabChangedListener(new OnTabChangeListener() {
		// @Override
		// public void onTabChanged(String tabId) {
		// apps = new ArrayList<AppCmsObj>();
		// page = 0;
		// if (tabId.equals("soft")) {
		// // tabUrl = Const.TOP_SOFT_URL;
		// } else if (tabId.equals("game")) {
		// // tabUrl = Const.TOP_GAME_URL;
		// }
		// }
		// });
		// lvSoft.setOnScrollListener(new OnScrollListener() {
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
		// if (!isUpdata && turnPage && view.getLastVisiblePosition() ==
		// view.getCount() - 1) {
		// isUpdata = true;
		// page++;
		// addData();
		// } else if (turnPage) {
		// MyApplication.getHandler().sendEmptyMessage(Const.ISUPDATA);
		// }
		// }
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int
		// visibleItemCount, int totalItemCount) {
		// turnPage = (firstVisibleItem + visibleItemCount == totalItemCount);
		// }
		// });
		// lvGame.setOnScrollListener(new OnScrollListener() {
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
		// if (!isUpdata && turnPage && view.getLastVisiblePosition() ==
		// view.getCount() - 1) {
		// isUpdata = true;
		// page++;
		// addData();
		// }
		// } else {
		// MyApplication.getHandler().sendEmptyMessage(Const.ISUPDATA);
		// }
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int
		// visibleItemCount, int totalItemCount) {
		// turnPage = (firstVisibleItem + visibleItemCount == totalItemCount);
		// }
		// });
	}

	protected void addData() {
		/*
		 * JsonDownLoad.getJson(tabUrl + page, false, new JsonCallBack() {
		 * 
		 * @Override public void jsonBack(String jsonStr) {
		 * apps.addAll(JsonTools.getCms(jsonStr)); parseJson(jsonStr); } });
		 */

	}

	public static void initData() {
		/*
		 * JsonDownLoad.getJson(tabUrl + page, true, new JsonCallBack() {
		 * 
		 * @Override public void jsonBack(String jsonStr) { apps =
		 * JsonTools.getCms(jsonStr); parseJson(jsonStr); } });
		 */
	}

	public static void parseJson(final String jsonStr) {
		if (Tools.checkCurrentFragment(Const.BangDan_UPDATEVIEW)) {
			MyApplication.getHandler().sendEmptyMessage(Const.BangDan_UPDATEVIEW);
		}
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				if (Tools.checkCurrentFragment(Const.BangDan_UPDATEVIEW)) {
					MyApplication.getHandler().sendEmptyMessage(Const.BangDan_UPDATEVIEW);
				}
				isUpdata = false;
			}
		});
	}

	public static void updataView() {
		LogUtil.i("Bangdan_updateLV");
		isUpdata = false;
		if (null != apps && apps.size() != 0 && !mHasLoadedOnce) {
			mHasLoadedOnce = true;
			BDLvAdapter adapterSoft = new BDLvAdapter(appSoft);
			BDLvAdapter adapterGame = new BDLvAdapter(appsGame);
			lvSoft.setAdapter(adapterSoft);
			lvGame.setAdapter(adapterGame);
		}
		try {
			adapter.notifyChange(apps);
			BaseActivity.updateFragment(Const.BangDan_INDEX);

		} catch (Exception e) {
			reUpdata();
		}

	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		} else {
			// initData();
			initData(Const.TOP_SOFT_URL);
			initData(Const.TOP_GAME_URL);
		}

	}

	protected static void reUpdata() {
		if (errorCount < Const.ERRORCOUNT) {
			LogUtil.i("BangDan第" + errorCount + "次更新UI");
		} else {
			errorCount = 0;
			MyApplication.getHandler().sendEmptyMessage(Const.ERRORCOUNT);
		}
		errorCount++;
	}
}
