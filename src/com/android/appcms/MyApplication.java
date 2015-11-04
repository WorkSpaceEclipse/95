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
	private static ArrayList<String> downingIndex;// ����APPid�б�
	public static String InfoIndex;
	public static int currentFragment;// ��ǰfragment

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
			LogUtil.i("Application����Handler");
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case Const.RECOMMOND_UPDATEVIEW:// '�Ƽ�'����UI
						RecFragment.updataView();
						break;
					case Const.TOPIC_UPDATEVIEW_FENLEI:// ר���б����UI
						TopicListFragment.updataView();
						break;
					case Const.TOPIC_UPDATEVIEW_INFO:// ר��ҳ����UI
						TopicZtFragment.updataView();
						break;
					case Const.FENLEI_UPDATEVIEW:// �������UI
						FenleiFragment.updataView();
						break;
					case Const.FENLEI_INFO_UPDATEVIEW:// �������UI
						FenleiInfoLVFragment.updataView();
						break;
					case Const.GAME_UPDATEVIEW:// ��Ϸ����UI
						GameFragment.updataView();
						break;
					case Const.SOFT_UPDATEVIEW:// �������UI
						SoftFragment.updataView();
						break;
					case Const.BangDan_UPDATEVIEW:// �񵥸���UI
						BDFragment.updataView();
						break;
					case Const.SEARCH_UPDATEVIEW:// �������UI
						SearchActivity.updataView();
						break;
					case Const.BASE_GATAPPID:// �������UI
						String id = (String) msg.obj;
						if (!id.equals("")) {
							Intent it = new Intent(context, AppInfoActivity.class);
							it.putExtra(Const.APPID, (String) msg.obj);
							it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							context.startActivity(it);
						} else {
							Toast.makeText(context, "��ȡӦ����Ϣ����,���Ժ����ԡ�����", Toast.LENGTH_SHORT).show();

						}
						break;
					case Const.BASE_APP_UPDATEVIEW:// �������UI
						// BaseActivity.bar.setVisibility(View.GONE);
						AppInfoActivity.updateView();
						break;
					case Const.BASE_APP_IMG_UPDATEVIEW:// �������UI
						// BaseActivity.bar.setVisibility(View.GONE);
						AppInfoActivity.updateImgView();
						break;
					case Const.PUSH_APP_UPDATEVIEW:// �������UI
						// BaseActivity.bar.setVisibility(View.GONE);
						PushActivity.updateView();
						break;
					case Const.PUSH_IMG_UPDATEVIEW:// �������UI
						// BaseActivity.bar.setVisibility(View.GONE);
						PushActivity.updateImgView();
						break;
					case Const.APK_DOWNLOAD_START:
						Toast.makeText(context, "��ʼ����" + msg.obj, Toast.LENGTH_SHORT).show();
						break;
					case Const.APK_DOWNLOAD_END:
						Toast.makeText(context, msg.obj + "������ϡ�", Toast.LENGTH_SHORT).show();
						break;
					case Const.PROGRESS_VISIBILTY:
						BaseActivity.bar.setVisibility(View.VISIBLE);
						break;
					case Const.PROGRESS_GONE:
						BaseActivity.bar.setVisibility(View.GONE);
						break;
					case Const.JSONNULL:
						Toast.makeText(context, "δ�ҵ������Ϣ����", Toast.LENGTH_SHORT).show();
						break;
					case Const.ERRORCOUNT:
						BaseActivity.bar.setVisibility(View.GONE);
						// Toast.makeText(context, "���س��������ԡ���",
						// Toast.LENGTH_SHORT).show();
						break;
					case Const.ISUPDATA:
						Toast.makeText(context, "ƴ������ing����", Toast.LENGTH_SHORT).show();
						break;
					case Const.SEARCHISNULL:
						Toast.makeText(context, "�������ݲ���Ϊ�ա�", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			};

		}
		return handler;
	}

	public void exit() {
		// Toast.makeText(getApplicationContext(), "����������˳�",
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
