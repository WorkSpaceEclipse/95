package com.appcms.tools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.appcms.object.AppCmsObj;
import com.appcms.push.PushActivity;

public class Notificationtools {

	public static void create(AppCmsObj ntObj) {
		// toast 开始下载
		MyApplication.removeDwonID(ntObj.getId());
		Message message = Message.obtain();
		message.obj = ntObj.getTitle();
		message.what = Const.APK_DOWNLOAD_START;
		MyApplication.getHandler().sendMessage(message);
		// 创建NT
		NotificationManager nm = (NotificationManager) MyApplication.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		int icon = android.R.drawable.stat_sys_download;
		LogUtil.i("CreateNotify_index:" + ntObj.getId());
		String contentTitle = ntObj.getTitle().trim();
		// Notification的内容
		String contentText = "正在下载。。。";
		// 点击跳转
		Intent notificationIntent = new Intent(MyApplication.getContext(), MyApplication.getContext().getClass());
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getContext(), 0, notificationIntent, 0);
		// 创建Notifcation
		Notification notification = new Notification(icon, ntObj.getIntro().trim(), System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(MyApplication.getContext(), contentTitle, contentText, contentIntent);
		// 显示这个notification
		if (notification != null) {
			nm.notify(Integer.parseInt(ntObj.getId()), notification);
		}

	}

	public static void done(AppCmsObj ntObj) {
		NotificationManager nm = (NotificationManager) MyApplication.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.icon;
		LogUtil.i("CreateNotify_index:" + ntObj.getId());
		String contentTitle = ntObj.getTitle().trim();
		// Notification的内容
		String contentText = "下载完成，点击安装。。。";
		// 点击跳转
		Intent it = new Intent();
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		it.setAction(android.content.Intent.ACTION_VIEW);
		it.setDataAndType(Uri.fromFile(new File(Const.APK_Down_LOCALURL + ntObj.getApkName())),
				"application/vnd.android.package-archive");
		LogUtil.i(Const.IMG_Down_LOCALURL + ntObj.getLogoName());
		it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getContext(), 0, it, 0);

		// 创建Notifcation
		Notification notification = new Notification(icon, ntObj.getIntro().trim(), System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(MyApplication.getContext(), contentTitle, contentText, contentIntent);

		// 获取apk图标
		LinearLayout grop = new LinearLayout(MyApplication.getContext().getApplicationContext());
		ViewGroup event = (ViewGroup) notification.contentView.apply(
				MyApplication.getContext().getApplicationContext(), grop);
		int imgId = Tools.getNFid(event);
		BitmapDrawable bd = (BitmapDrawable) Tools.getApkIcon(MyApplication.getContext(), Const.APK_Down_LOCALURL
				+ ntObj.getApkName());
		Bitmap bm = bd.getBitmap();
		notification.contentView.setImageViewBitmap(imgId, bm);
		// 显示这个notification
		nm.notify(Integer.parseInt(ntObj.getId()), notification);
		// remove 正在下载ID
		MyApplication.removeDwonID(ntObj.getId());
		// toast完成下载
		Message message = Message.obtain();
		message.obj = ntObj.getTitle();
		message.what = Const.APK_DOWNLOAD_END;
		MyApplication.getHandler().sendMessage(message);
		// 跳转安装界面
		if (!Tools.slientInstall(new File(Const.APK_Down_LOCALURL + ntObj.getApkName()))) {
			Tools.openFile(MyApplication.getContext(), new File(Const.APK_Down_LOCALURL + ntObj.getApkName()));
		}

	}

	public static void createPush(AppCmsObj ntObj) {
		if (checkNTed(ntObj.getId())) {
			NotificationManager nm = (NotificationManager) MyApplication.getContext().getSystemService(
					Context.NOTIFICATION_SERVICE);
			// int icon = android.R.drawable.sym_action_email;
			int icon = R.drawable.icon;
			String contentTitle = ntObj.getTitle().trim();
			// Notification的内容
			String contentText = "点击查看详情……";
			// 点击跳转
			Intent it = new Intent(MyApplication.getContext(), PushActivity.class);
			it.addCategory(Intent.CATEGORY_LAUNCHER);
			// it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putExtra("type", ntObj.getType());
			it.putExtra("id", ntObj.getId());
			PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getContext(), 0, it,
					PendingIntent.FLAG_UPDATE_CURRENT);

			// 创建Notifcation
			Notification notification = new Notification(icon, ntObj.getIntro().trim(), System.currentTimeMillis());
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(MyApplication.getContext(), contentTitle, contentText, contentIntent);

			// 获取apk图标
			LinearLayout grop = new LinearLayout(MyApplication.getContext().getApplicationContext());
			ViewGroup event = (ViewGroup) notification.contentView.apply(MyApplication.getContext()
					.getApplicationContext(), grop);
			int imgId = Tools.getNFid(event);
			notification.contentView.setImageViewBitmap(imgId,
					BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + ntObj.getLogoName()));
			// 显示这个notification
			nm.notify(Integer.parseInt(ntObj.getId()), notification);
		}
	}

	private static boolean checkNTed(String id) {
		SharedPreferences sp = MyApplication.getContext().getSharedPreferences(Const.SP_NTID, Context.MODE_PRIVATE);
		boolean isFirst = sp.getBoolean("isFirst", true);
		Editor editor = sp.edit();
		if (isFirst) {
			editor.putBoolean("isFirst", false);
			editor.commit();
		}
		String ids = sp.getString("ids", "");
		LogUtil.i("IDS:" + ids);
		List<String> idlist = Arrays.asList(ids.split(","));
		if (idlist.contains(id)) {
			return false;
		} else {
			ids += "," + id;
			editor.putString("ids", ids);
			editor.commit();
			return true;
		}
	}

	public static void upDataNT(int downloadCount, AppCmsObj ntObj) {
		// 创建NT
		NotificationManager nm = (NotificationManager) MyApplication.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		int icon = android.R.drawable.stat_sys_download;
		LogUtil.i("CreateNotify_index:" + ntObj.getId());
		String contentTitle = ntObj.getTitle().trim();
		// Notification的内容
		String contentText = "已经下载" + downloadCount + "%";
		// 点击跳转
		Intent notificationIntent = new Intent(MyApplication.getContext(), MyApplication.getContext().getClass());
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getContext(), 0, notificationIntent, 0);
		// 创建Notifcation
		Notification notification = new Notification(icon, ntObj.getIntro().trim(), System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(MyApplication.getContext(), contentTitle, contentText, contentIntent);
		// 显示这个notification
		if (notification != null) {
			nm.notify(Integer.parseInt(ntObj.getId()), notification);
		}

	}
}
