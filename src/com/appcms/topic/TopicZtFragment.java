package com.appcms.topic;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class TopicZtFragment extends BaseFragment {
	public static ListView lv;
	private static ArrayList<AppCmsObj> infos;
	private static ImageView ivTop;
	private static AppCmsObj fenleiTop;
	public static MyApplication application;
	public static int errorCount = 0;

	private int mCurIndex = -1;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private static boolean mHasLoadedOnce;
	private View view;

	public static TopicZtFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Const.INDEX, index);
		TopicZtFragment fragment = new TopicZtFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.topic_zhuanti, container, false);
			Bundle bundle = getArguments();
			if (bundle != null) {
				mCurIndex = bundle.getInt(Const.INDEX);
			}
		}
		lv = (ListView) view.findViewById(R.id.lv_topiczhuanti);
		ivTop = (ImageView) view.findViewById(R.id.iv_topiczhuanti_top);
		application = (MyApplication) getActivity().getApplication();
		isPrepared = true;
		lazyLoad();
		return view;
	}

	private static void initData() {
		JsonDownLoad.getJson(Const.TOPIC_INFO_URL + fenleiTop.getId(), false, new JsonCallBack() {
			@Override
			public void jsonBack(String jsonStr) {
				infos = JsonTools.getCms(jsonStr);
				parseJson(jsonStr);
			}
		});

	}

	public static void parseJson(final String jsonStr) {

		if (Tools.checkCurrentFragment(Const.TOPIC_UPDATEVIEW_INFO)) {
			MyApplication.getHandler().sendEmptyMessage(Const.TOPIC_UPDATEVIEW_INFO);
		}
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {

			@Override
			public void isDode(boolean result) {
				if (Tools.checkCurrentFragment(Const.TOPIC_UPDATEVIEW_INFO)) {
					MyApplication.getHandler().sendEmptyMessage(Const.TOPIC_UPDATEVIEW_INFO);
				}
			}
		});
	}

	public static void updataView() {
		LogUtil.i("TopicZT_updateLV");
		Bitmap bm = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + fenleiTop.getLogoName());
		if (bm != null) {
			ivTop.setImageBitmap(bm);
		}
		if (null != infos && infos.size() != 0) {
			mHasLoadedOnce = true;
			TopicZtLvAdapter adapter = new TopicZtLvAdapter(infos);
			lv.setAdapter(adapter);
			BaseActivity.updateFragment(Const.TOPIC_LISTINFO_INDEX);
		}
	}

	public static void getTop(AppCmsObj fenlei) {
		fenleiTop = fenlei;
		initData();
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		} else {
			initData();
		}

	}

}
