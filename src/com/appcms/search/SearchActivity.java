package com.appcms.search;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
import com.umeng.analytics.MobclickAgent;

public class SearchActivity extends Activity {

	private EditText etSearch;
	private static ListView lv;
	private int errorCount = 0;
	private String tag;
	private ImageView ivBack;
	private ImageView ivSearch;
	public static ProgressBar bar;
	private static ArrayList<AppCmsObj> apps;
	private static SearchLvAdapter adapter;
	private static int page = 0;// 分页
	private boolean turnPage;
	private static boolean isUpdata = false;// 防止多次滑动更新分页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchactivity);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initView();
		initLinster();
		initData("塔防");
	}

	private void initData(String tag) {

		JsonDownLoad.getJson(Const.SEARCH_URL + tag + "&p=" + page, true, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				if (jsonStr.contains("null")) {
					MyApplication.getHandler().sendEmptyMessage(Const.JSONNULL);
				}
				apps = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});

	}

	private void initLinster() {
		etSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				try {
					tag = etSearch.getText().toString();
					if (tag != null && !tag.equals("") && (actionId == 0 || actionId == 3) && event != null) {
						bar.setVisibility(View.VISIBLE);
						initData();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		ivSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tag = etSearch.getText().toString();
				if (!tag.equals("")) {
					bar.setVisibility(View.VISIBLE);
					initData();
				} else {
					MyApplication.getHandler().sendEmptyMessage(Const.SEARCHISNULL);
				}
			}
		});

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

		LogUtil.i(Const.SEARCH_URL + page + "&p=" + page);
		JsonDownLoad.getJson(Const.SEARCH_URL + page + "&p=" + page, false, new JsonCallBack() {
			@Override
			public void jsonBack(String jsonStr) {
				apps.addAll(JsonTools.getCms(jsonStr));
				parseJson(jsonStr);
			}
		});

	}

	private void initView() {
		ivBack = (ImageView) findViewById(R.id.iv_search_back);
		ivSearch = (ImageView) findViewById(R.id.iv_search_search);
		etSearch = (EditText) findViewById(R.id.et_search_search);
		bar = (ProgressBar) findViewById(R.id.progressBar_serach);
		bar.setVisibility(View.GONE);
		lv = (ListView) findViewById(R.id.lv_search);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		JsonDownLoad.getJson(Const.SEARCH_URL + tag + "&p=" + page, true, new JsonCallBack() {

			@Override
			public void jsonBack(String jsonStr) {
				if (jsonStr.contains("null")) {
					MyApplication.getHandler().sendEmptyMessage(Const.JSONNULL);
				}
				apps = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});
	}

	private void parseJson(final String jsonStr) {
		MyApplication.getHandler().sendEmptyMessage(Const.SEARCH_UPDATEVIEW);
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				MyApplication.getHandler().sendEmptyMessage(Const.SEARCH_UPDATEVIEW);
			}
		});
	}

	public static void updataView() {
		LogUtil.i("search_updateLV");
		bar.setVisibility(View.GONE);
		if (null != apps && apps.size() != 0) {
			adapter = new SearchLvAdapter(apps);
			lv.setAdapter(adapter);
		}
		adapter.notifyChange(apps);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.i("back");
			bar.setVisibility(View.GONE);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
}
