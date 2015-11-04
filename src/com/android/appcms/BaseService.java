package com.android.appcms;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.netools.ImgDownLoad;
import com.android.netools.ImgDownLoad.ImgDownLoaded;
import com.android.netools.JsonDownLoad;
import com.android.netools.JsonDownLoad.JsonCallBack;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;
import com.appcms.tools.JsonTools;
import com.appcms.tools.LogUtil;
import com.appcms.tools.Notificationtools;

public class BaseService extends Service {
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			LogUtil.i("appcmsService");
			final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
			executorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					JsonDownLoad.getJson(Const.PUSH_URL, new JsonCallBack() {

						@Override
						public void jsonBack(final String jsonstr) {
							ImgDownLoad.downImg(JsonTools.getCms(jsonstr), new ImgDownLoaded() {
								@Override
								public void isDode(boolean result) {
									if (result) {
										ArrayList<AppCmsObj> apps = JsonTools.getCms(jsonstr);
										for (AppCmsObj app : apps) {
											if (app.getPush().equals("1")) {
												Notificationtools.createPush(app);
											}
										}
									}
								}
							});
						}
					});
				}
			}, 2, 20 * 60, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
