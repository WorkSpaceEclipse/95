package com.android.netools;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.Looper;

import com.appcms.object.ApkDownObject;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;
import com.appcms.tools.LogUtil;
import com.appcms.tools.Notificationtools;
import com.appcms.tools.Tools;

public abstract class ApkDownLoad {

	public static void downApk(Context baseAppActivity, final AppCmsObj app) {
		ExecutorService executorService = NetTools.getExeService();
		Runnable runnable = new Runnable() {
			public void run() {
				Looper.prepare();
				HttpClient client = NetTools.getSaveHttpClient();
				HttpGet get = null;
				try {
					ApkDownObject object = getDownApk(app);
					get = new HttpGet(object.getApkUrl());
					LogUtil.i("apkUrl:" + object.getApkUrl());
					HttpResponse response = client.execute(get);
					long apkLength = response.getEntity().getContentLength();
					File apkFile = new File(Const.APK_Down_LOCALURL + object.getApkName());
					AppCmsObj ntObj = new AppCmsObj();
					ntObj.setId(object.getId());
					ntObj.setApkName(object.getApkName());
					ntObj.setTitle(object.getTitle());
					ntObj.setIntro("正在下载" + ntObj.getTitle());
					if (apkFile.length() == apkLength) {
						Notificationtools.done(ntObj);
					} else {
						LogUtil.i("下载开始。。。。");
						apkFile.delete();
						Tools.writeFileToSDwithNotify(response.getEntity().getContent(), apkLength, apkFile, ntObj);
					}
					get.abort();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Looper.loop();
			}
		};
		executorService.execute(runnable);
	}

	private static ApkDownObject getDownApk(AppCmsObj apps) {
		/*
		 * 把APPCMS对象转换成DOWNAPK对象
		 */
		try {
			ApkDownObject object = new ApkDownObject();
			object.setId(apps.getId());
			object.setApkUrl(apps.getApkUrl());
			object.setApkName(apps.getApkName());
			object.setTitle(apps.getTitle());
			return object;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
