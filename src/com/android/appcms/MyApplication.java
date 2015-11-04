package com.android.appcms;

import java.io.File;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.android.game.GameFragment;
import com.android.soft.SoftFragment;
import com.appcms.appinfo.AppInfoActivity;
import com.appcms.bangdan.BDFragment;
import com.appcms.fenlei.FenleiFragment;
import com.appcms.fenlei.FenleiInfoLVFragment;
import com.appcms.push.PushActivity;
import com.appcms.recommend.RecFragment;
import com.appcms.search.SearchActivity;
import com.appcms.tools.Const;
import com.appcms.tools.LogUtil;
import com.appcms.topic.TopicListFragment;
import com.appcms.topic.TopicZtFragment;

public class MyApplication extends Application {

	private static Context context;
	private static Handler handler;
	private static ArrayList<String> downingIndex;// 下载APPid列表
	public static String InfoIndex;
	public static int currentFragment;// 当前fragment

	@Override
	public void onCreate() {
		context = this;
		downingIndex = new ArrayList<String>();
		InfoIndex = "";
		currentFragment = -1;
		getHandler();
		File imgFiles = new File(Const.IMG_Down_LOCALURL);
		File apkFiles = new File(Const.APK_Down_LOCALURL);
		if (!imgFiles.exists()) {
			imgFiles.mkdirs();
		}
		if (!apkFiles.exists()) {
			apkFiles.mkdirs();
		}
	}

	public static Handler getHandler() {
		if (handler == null) {
			LogUtil.i("Application创建Handler");
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case Const.RECOMMOND_UPDATEVIEW:// '推荐'更新UI
						RecFragment.updataView();
						break;
					case Const.TOPIC_UPDATEVIEW_FENLEI:// 专题列表更新UI
						TopicListFragment.updataView();
						break;
					case Const.TOPIC_UPDATEVIEW_INFO:// 专题页更新UI
						TopicZtFragment.updataView();
						break;
					case Const.FENLEI_UPDATEVIEW:// 分类更新UI
						FenleiFragment.updataView();
						break;
					case Const.FENLEI_INFO_UPDATEVIEW:// 分类更新UI
						FenleiInfoLVFragment.updataView();
						break;
					case Const.GAME_UPDATEVIEW:// 游戏更新UI
						GameFragment.updataView();
						break;
					case Const.SOFT_UPDATEVIEW:// 软件更新UI
						SoftFragment.updataView();
						break;
					case Const.BangDan_UPDATEVIEW:// 榜单更新UI
						BDFragment.updataView();
						break;
					case Const.SEARCH_UPDATEVIEW:// 软件更新UI
						SearchActivity.updataView();
						break;
					case Const.BASE_GATAPPID:// 软件更新UI
						String id = (String) msg.obj;
						if (!id.equals("")) {
							Intent it = new Intent(context, AppInfoActivity.class);
							it.putExtra(Const.APPID, (String) msg.obj);
							it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							context.startActivity(it);
						} else {
							Toast.makeText(context, "获取应用信息出错,请稍后再试。。。", Toast.LENGTH_SHORT).show();

						}
						break;
					case Const.BASE_APP_UPDATEVIEW:// 软件更新UI
						// BaseActivity.bar.setVisibility(View.GONE);
						AppInfoActivity.updateView();
						break;
					case Const.BASE_APP_IMG_UPDATEVIEW:// 软件更新UI
						// BaseActivity.bar.setVisibility(View.GONE);
						AppInfoActivity.updateImgView();
						break;
					case Const.PUSH_APP_UPDATEVIEW:// 软件更新UI
						// BaseActivity.bar.setVisibility(View.GONE);
						PushActivity.updateView();
						break;
					case Const.PUSH_IMG_UPDATEVIEW:// 软件更新UI
						// BaseActivity.bar.setVisibility(View.GONE);
						PushActivity.updateImgView();
						break;
					case Const.APK_DOWNLOAD_START:
						Toast.makeText(context, "开始下载" + msg.obj, Toast.LENGTH_SHORT).show();
						break;
					case Const.APK_DOWNLOAD_END:
						Toast.makeText(context, msg.obj + "下载完毕。", Toast.LENGTH_SHORT).show();
						break;
					case Const.PROGRESS_VISIBILTY:
						BaseActivity.bar.setVisibility(View.VISIBLE);
						break;
					case Const.PROGRESS_GONE:
						BaseActivity.bar.setVisibility(View.GONE);
						break;
					case Const.JSONNULL:
						Toast.makeText(context, "未找到相关信息……", Toast.LENGTH_SHORT).show();
						break;
					case Const.ERRORCOUNT:
						BaseActivity.bar.setVisibility(View.GONE);
						// Toast.makeText(context, "加载出错，请重试……",
						// Toast.LENGTH_SHORT).show();
						break;
					case Const.ISUPDATA:
						Toast.makeText(context, "拼命加载ing……", Toast.LENGTH_SHORT).show();
						break;
					case Const.SEARCHISNULL:
						Toast.makeText(context, "搜索内容不能为空。", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			};

		}
		return handler;
	}

	public void exit() {
		// Toast.makeText(getApplicationContext(), "程序出错即将退出",
		// Toast.LENGTH_SHORT).show();
		// System.exit(0);
	}

	public boolean downStart(String appId) {
		if (downingIndex.contains(appId)) {
			LogUtil.i("false:" + appId);
			return false;
		} else {
			LogUtil.i("true:" + appId);
			downingIndex.add(appId);
			return true;
		}
	}

	public static Context getContext() {
		return context;
	}

	public static void removeDwonID(String id) {
		downingIndex.remove(id);
	}

}
