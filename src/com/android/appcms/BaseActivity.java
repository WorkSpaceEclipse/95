package com.android.appcms;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.game.GameFragment;
import com.android.soft.SoftFragment;
import com.appcms.bangdan.BDFragment;
import com.appcms.fenlei.FenleiFragment;
import com.appcms.fenlei.FenleiInfoLVFragment;
import com.appcms.recommend.RecFragment;
import com.appcms.search.SearchActivity;
import com.appcms.tools.Const;
import com.appcms.tools.LogUtil;
import com.appcms.topic.TopicListFragment;
import com.appcms.topic.TopicZtFragment;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity implements OnTouchListener {
	private TextView tvRecommend;
	private TextView tvGame;
	private TextView tvSoft;
	private TextView tvTopic;
	// private TextView tvManager;
	private TextView tvJingxuan;
	private TextView tvFenlei;
	private TextView currentTV;
	private long mExitTime;
	private FenleiInfoLVFragment fenleiInfoLVFragment;
	private TextView etSearch;
	private TextView tvBangDan;
	private BDFragment bdFragment;
	public static SoftFragment softFragment;// 软件
	public static GameFragment gameFragment;// 游戏
	public static FenleiFragment fenleiFragment;// 分类
	public static TopicZtFragment topicZtFragment;// 专题页
	public static TopicListFragment topicListFragment;// 专题列表
	public static RecFragment recFragment;// 推荐与精选同一个fragment
	public static BaseViewPageAdapter pagerAdapter;
	public static ProgressBar bar;
	public static ArrayList<Fragment> fragments;
	public static BaseViewPage pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baseactivity);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
		initView();
	}

	private void initView() {
		etSearch = (TextView) findViewById(R.id.tv_base_search);
		tvRecommend = (TextView) findViewById(R.id.tv_recommend);
		tvGame = (TextView) findViewById(R.id.tv_game);
		tvSoft = (TextView) findViewById(R.id.tv_soft);
		tvTopic = (TextView) findViewById(R.id.tv_topic);
		// tvManager = (TextView) findViewById(R.id.tv_manager);
		tvJingxuan = (TextView) findViewById(R.id.tv_jingxuan);
		tvFenlei = (TextView) findViewById(R.id.tv_fenlei);
		tvBangDan = (TextView) findViewById(R.id.tv_base_bangdan);
		bar = (ProgressBar) findViewById(R.id.progressBar_base);
		bar.setVisibility(View.GONE);
		tvRecommend.setOnTouchListener(this);
		tvRecommend.setPressed(true);
		tvGame.setOnTouchListener(this);
		tvSoft.setOnTouchListener(this);
		tvTopic.setOnTouchListener(this);
		// tvManager.setOnTouchListener(this);
		tvJingxuan.setOnTouchListener(this);
		tvFenlei.setOnTouchListener(this);
		tvBangDan.setOnTouchListener(this);
		pager = (BaseViewPage) findViewById(R.id.vp_base);
		pager.setScrollble(false);
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			recFragment = RecFragment.newInstance(Const.RECOMMEND_INDEX);
			gameFragment = GameFragment.newInstance(Const.GAME_INDEX);
			softFragment = SoftFragment.newInstance(Const.SOFT_INDEX);
			topicListFragment = TopicListFragment.newInstance(Const.TOPIC_LIST_INDEX);
			topicZtFragment = TopicZtFragment.newInstance(Const.TOPIC_LISTINFO_INDEX);
			fenleiFragment = FenleiFragment.newInstance(Const.FENLEI_INDEX);
			fenleiInfoLVFragment = FenleiInfoLVFragment.newInstance(Const.FENLEI_INFO_INDEX);
			bdFragment = BDFragment.newInstance(Const.BangDan_INDEX);
			fragments.add(recFragment);
			fragments.add(gameFragment);
			fragments.add(softFragment);
			fragments.add(topicListFragment);
			fragments.add(topicZtFragment);
			fragments.add(fenleiFragment);
			fragments.add(fenleiInfoLVFragment);
			fragments.add(bdFragment);
		}
		pagerAdapter = new BaseViewPageAdapter(getSupportFragmentManager(), fragments);
		pager.setOffscreenPageLimit(fragments.size());
		pager.setAdapter(pagerAdapter);

		/**
		 * 跳转搜索act
		 */
		etSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(BaseActivity.this, SearchActivity.class));
			}
		});
		MyApplication.currentFragment = Const.RECOMMOND_UPDATEVIEW;
		currentTV = tvRecommend;
		changeTVPressedTure(currentTV);
		pager.setCurrentItem(Const.RECOMMEND_INDEX);
		// String tag = "";
		// try {
		// tag = getIntent().getExtras().getString("type");
		// LogUtil.i("oncreate_tag:" + tag);
		// if (tag.equals("app")) {
		// Intent it = new Intent(this, AppInfoActivity.class);
		// it.putExtra(Const.APPID, getIntent().getExtras().getString("id"));
		// it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
		// Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		// startActivity(it);
		// } else {
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.i("bse_onstart");
		changeTVPressedTure(currentTV);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bar.setVisibility(View.GONE);
			switch (v.getId()) {
			// 改变底部标签颜色
			case R.id.tv_recommend:
				MyApplication.currentFragment = Const.RECOMMOND_UPDATEVIEW;
				currentTV = tvRecommend;
				changeTVPressedTure(tvRecommend);
				pager.setCurrentItem(Const.RECOMMEND_INDEX);
				break;
			case R.id.tv_game:
				MyApplication.currentFragment = Const.GAME_UPDATEVIEW;
				changeTVPressedTure(tvGame);
				currentTV = tvGame;
				pager.setCurrentItem(Const.GAME_INDEX);
				break;
			case R.id.tv_soft:
				MyApplication.currentFragment = Const.SOFT_UPDATEVIEW;
				changeTVPressedTure(tvSoft);
				currentTV = tvSoft;
				pager.setCurrentItem(Const.SOFT_INDEX);
				break;
			case R.id.tv_topic:
				MyApplication.currentFragment = Const.TOPIC_UPDATEVIEW_FENLEI;
				changeTVPressedTure(tvTopic);
				currentTV = tvTopic;
				pager.setCurrentItem(Const.TOPIC_LIST_INDEX);
				break;
			// case R.id.tv_manager:
			// changeTVPressedTure(tvManager);
			// break;
			case R.id.tv_jingxuan:
				MyApplication.currentFragment = Const.RECOMMOND_UPDATEVIEW;
				changeTVPressedTure(tvJingxuan);
				currentTV = tvJingxuan;
				pager.setCurrentItem(Const.RECOMMEND_INDEX);
				break;
			case R.id.tv_fenlei:
				MyApplication.currentFragment = Const.FENLEI_UPDATEVIEW;
				changeTVPressedTure(tvFenlei);
				currentTV = tvFenlei;
				pager.setCurrentItem(Const.FENLEI_INDEX);
				break;
			case R.id.tv_base_bangdan:
				MyApplication.currentFragment = Const.BangDan_UPDATEVIEW;
				changeTVPressedTure(tvBangDan);
				currentTV = tvBangDan;
				pager.setCurrentItem(Const.BangDan_INDEX);
				break;

			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {

		}
		return false;
	}

	private void changeTVPressedTure(TextView chTv) {
		ArrayList<TextView> tvs = null;
		if (tvs == null) {
			tvs = new ArrayList<TextView>();
			tvs.add(tvRecommend);
			tvs.add(tvGame);
			tvs.add(tvSoft);
			tvs.add(tvTopic);
			// tvs.add(tvManager);
			tvs.add(tvJingxuan);
			tvs.add(tvFenlei);
			tvs.add(tvBangDan);
		}
		for (TextView tv : tvs) {
			if (tv == chTv) {
				tv.setPressed(true);
			} else {
				tv.setPressed(false);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("onkeyDown=bakc");
		MyApplication.getHandler().sendEmptyMessage(Const.PROGRESS_GONE);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pager.getCurrentItem() == Const.TOPIC_LISTINFO_INDEX) {
				pager.setCurrentItem(Const.TOPIC_LIST_INDEX);
				MyApplication.currentFragment = Const.TOPIC_UPDATEVIEW_FENLEI;
			} else if (pager.getCurrentItem() == Const.FENLEI_INFO_INDEX) {
				pager.setCurrentItem(Const.FENLEI_INDEX);
				MyApplication.currentFragment = Const.FENLEI_UPDATEVIEW;
			} else if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.i("BASE_ONDESTORY");
	}

	public static void updateFragment(int index) {
		pager.setCurrentItem(index);
		bar.setVisibility(View.GONE);
	}

	@Override
	protected void onRestart() {
		bar.setVisibility(View.GONE);
		super.onRestart();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
