package com.android.netools;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.os.Looper;

import com.appcms.object.AppCmsObj;
import com.appcms.object.ImgDownObject;
import com.appcms.tools.Const;
import com.appcms.tools.LogUtil;
import com.appcms.tools.Tools;

public class ImgDownLoad {

	private static ExecutorService executorService;

	public static void downImg(final ArrayList<AppCmsObj> imgs, final ImgDownLoaded imgDownLoaded) {
		executorService = NetTools.getExeService();
		Runnable runnable = new Runnable() {
			private String logoUrl;
			ArrayList<ImgDownObject> dws = getDownImgs(imgs);

			@Override
			public void run() {
				Looper.prepare();
				HttpClient client = NetTools.getSaveHttpClient();
				HttpGet get = null;
				HttpResponse response = null;
				try {

					for (int i = 0; i < dws.size(); i++) {
						logoUrl = dws.get(i).getDLUrl();
						get = new HttpGet(logoUrl);
						response = client.execute(get);
						File logoFile = new File(Const.IMG_Down_LOCALURL + dws.get(i).getDLName());
						if (logoFile.length() != response.getEntity().getContentLength()) {
							logoFile.delete();
							Tools.writeFileToSD(response.getEntity().getContent(), logoFile);
							// LogUtil.i("下载:" + dws.get(i).getDLName());
						} else {
							// LogUtil.i("重用:" + dws.get(i).getDLName());
						}
						get.abort();
						if (i % 5 == 0) {
							LogUtil.i("i:" + i);
							imgDownLoaded.isDode(true);
						}

					}
					imgDownLoaded.isDode(true);
				} catch (Exception e) {
					e.printStackTrace();
					imgDownLoaded.isDode(false);
					LogUtil.i("下载出错:" + logoUrl);
				}
				Looper.loop();
			}
		};
		executorService.execute(runnable);
	}

	public interface ImgDownLoaded {
		public void isDode(boolean result);
	}

	private static ArrayList<ImgDownObject> getDownImgs(ArrayList<AppCmsObj> apps) {
		/**
		 * 把Appcmsojb数组转换成Downimgobj数组进行下载。
		 */
		try {
			ArrayList<ImgDownObject> dws = new ArrayList<ImgDownObject>();
			for (int i = 0; i < apps.size(); i++) {
				ImgDownObject downObject = new ImgDownObject();
				downObject.setDLUrl(apps.get(i).getLogoUrl());
				downObject.setDLName(apps.get(i).getLogoName());
				dws.add(downObject);
			}
			return dws;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	public static void downImg(final String downloadPath, final ArrayList<AppCmsObj> imgs,
			final ImgDownLoaded imgDownLoaded) {
		executorService = NetTools.getExeService();
		Runnable runnable = new Runnable() {
			private String logoUrl;
			ArrayList<ImgDownObject> dws = getDownImgs(imgs);

			@Override
			public void run() {
				Looper.prepare();
				HttpClient client = NetTools.getSaveHttpClient();
				synchronized (downloadPath) {

					HttpGet get = null;
					HttpResponse response = null;
					try {

						for (int i = 0; i < dws.size(); i++) {
							logoUrl = dws.get(i).getDLUrl();
							get = new HttpGet(logoUrl);
							response = client.execute(get);
							File logoFile = new File(downloadPath + "/" + dws.get(i).getDLName());
							LogUtil.i(logoFile.getAbsolutePath());
							if (logoFile.length() != response.getEntity().getContentLength()) {
								logoFile.delete();
								Tools.writeFileToSD(response.getEntity().getContent(), logoFile);
								// LogUtil.i("下载:" + dws.get(i).getDLName());
							} else {
								// LogUtil.i("重用:" + dws.get(i).getDLName());
							}
							get.abort();
							if (i % 3 == 0) {
								imgDownLoaded.isDode(true);
							}
							imgDownLoaded.isDode(true);
						}

					} catch (Exception e) {
						e.printStackTrace();
						imgDownLoaded.isDode(false);
						LogUtil.i("下载出错:" + logoUrl);
					}
					Looper.loop();

				}
			}
		};

		executorService.execute(runnable);

	}
}
