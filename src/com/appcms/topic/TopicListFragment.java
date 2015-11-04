package com.appcms.topic;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class TopicListFragment extends BaseFragment {
	public static ArrayList<AppCmsObj> tops;
	public static ListView lv;
	private static ArrayList<AppCmsObj> infos;
	public static MyApplication application;
	public static int errorCount = 0;

	private int mCurIndex = -1;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private static boolean mHasLoadedOnce;
	private View view;

	public static TopicListFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		TopicListFragment fragment = new TopicListFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.topic_fenlei, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		lv = (ListView) view.findViewById(R.id.lv_topic);
		application = (MyApplication) getActivity().getApplication();
		isPrepared = true;
		lazyLoad();
		return view;
	}

	private static void initData() {
		JsonDownLoad.getJson(Const.TOPIC_LIST_URL, true, new JsonCallBack() {
			@Override
			public void jsonBack(String jsonStr) {
				tops = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});
	}

	public static void parseJson(final String jsonStr) {
		if (Tools.checkCurrentFragment(Const.TOPIC_UPDATEVIEW_FENLEI)) {
			MyApplication.getHandler().sendEmptyMessage(Const.TOPIC_UPDATEVIEW_FENLEI);
		}
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				if (Tools.checkCurrentFragment(Const.TOPIC_UPDATEVIEW_FENLEI)) {
					MyApplication.getHandler().sendEmptyMessage(Const.TOPIC_UPDATEVIEW_FENLEI);
				}
			}
		});
	}

	public static void updataView() {
		if (null != tops) {
			mHasLoadedOnce = true;
			LogUtil.i("ZTList_updateLV");
			TopicListAdapter adapter = new TopicListAdapter(tops);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					MyApplication.currentFragment = Const.TOPIC_UPDATEVIEW_INFO;
					TopicZtFragment.getTop(tops.get(arg2));
				}
			});
			BaseActivity.updateFragment(Const.TOPIC_LIST_INDEX);
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
			errorCount = 0;
			MyApplication.getHandler().sendEmptyMessage(Const.ERRORCOUNT);
		}
		errorCount++;
	}
}
