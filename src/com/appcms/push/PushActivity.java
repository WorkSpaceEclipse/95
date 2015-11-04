package com.appcms.push;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.appcms.BaseActivity;
import com.android.appcms.MyApplication;
import com.android.appcms.R;
import com.android.netools.ApkDownLoad;
import com.android.netools.ImgDownLoad;
import com.android.netools.ImgDownLoad.ImgDownLoaded;
import com.android.netools.JsonDownLoad;
import com.android.netools.JsonDownLoad.JsonCallBack;
import com.appcms.object.AppCmsObj;
import com.appcms.tools.Const;
import com.appcms.tools.JsonTools;
import com.appcms.tools.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class PushActivity extends Activity {

	private static AppCmsObj app;
	private static MyApplication application;
	private static int errorCount = 0;
	private static TextView tvTitle;
	private static TextView tvSize;
	private static ImageView ivlogo;
	private static TextView tvIntro;
	private static String appId;
	private static PushActivity context;
	private static LinearLayout hscrollViewLin;
	private static ImageView ivDown;
	private static ArrayList<String> photoAdd = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.appinfo_tabhost);
		application = (MyApplication) getApplication();
		context = this;
		try {
			appId = getIntent().getExtras().getString(Const.APPID);
			LogUtil.i("appid:" + appId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (!appId.equals("")) {

			JsonDownLoad.getJson(Const.APPINFO_URL + appId, false, new JsonCallBack() {
				// 获取应用信息
				@Override
				public void jsonBack(String jsonStr) {
					parseJson(jsonStr);
				}
			});
		}
		initView();
	}

	protected static void parseImgJson(String jsonstr) {

		ArrayList<AppCmsObj> apps = JsonTools.getCms(jsonstr);
		if (null != app) {

			File file = new File(Const.IMG_Down_LOCALURL + app.getTitle() + "/");
			if (!file.exists()) {
				file.mkdirs();
			}
			ImgDownLoad.downImg(file.getAbsolutePath(), apps, new ImgDownLoaded() {
				// 下载app介绍图片
				@Override
				public void isDode(boolean result) {
					MyApplication.getHandler().sendEmptyMessage(Const.PUSH_IMG_UPDATEVIEW);
				}
			});
		}
	}

	private void initView() {
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost_appinfo);
		tabHost.setup();
		LayoutInflater inflater = LayoutInflater.from(this);
		View JJView = inflater.inflate(R.layout.appinfo_item_jianjie, tabHost.getTabContentView());
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("应用简介")
				.setContent(R.id.linearLayout_appinfo_thitem_tab1));
		TabWidget tabWidget = tabHost.getTabWidget();
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			// 修改Tabhost高度和宽度
			// tabWidget.getChildAt(i).getLayoutParams().height = 50;
			// tabWidget.getChildAt(i).getLayoutParams().width = 65;
			// 修改显示字体大小
			TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
			tv.setTextSize(15);
			tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
		}
		hscrollViewLin = (LinearLayout) findViewById(R.id.horizontalScrollView_linearlayout_appinfo_item_jianjie);
		tvIntro = (TextView) JJView.findViewById(R.id.tv_appifno_jianjie_intro);
		tvTitle = (TextView) findViewById(R.id.tv_baseinfo_title);
		tvSize = (TextView) findViewById(R.id.tv_baseinfo_size);
		ivlogo = (ImageView) findViewById(R.id.iv_baseinfo_logo);
		ivDown = (ImageView) findViewById(R.id.iv_baseinfo_download);
	}

	public static void parseJson(final String jsonStr) {
		ArrayList<AppCmsObj> apps = JsonTools.getCms(jsonStr);
		app = apps.get(0);
		app.setId(appId);

		MyApplication.getHandler().sendEmptyMessage(Const.PUSH_APP_UPDATEVIEW);
		JsonDownLoad.getJson(Const.APPINFO_IMG_URL + appId, false, new JsonCallBack() {
			// 获取app介绍图片
			@Override
			public void jsonBack(String jsonstr) {
				parseImgJson(jsonstr);
			}
		});
		ImgDownLoad.downImg(JsonTools.getCms(jsonStr), new ImgDownLoaded() {
			// 获取icon
			@Override
			public void isDode(boolean result) {
				MyApplication.getHandler().sendEmptyMessage(Const.PUSH_APP_UPDATEVIEW);
			}
		});
	}

	public static void updateView() {
		tvSize.setText(app.getSize());
		tvTitle.setText(app.getTitle());
		tvIntro.setText(Html.fromHtml(app.getIntro().trim()));
		Bitmap bm = BitmapFactory.decodeFile(Const.IMG_Down_LOCALURL + app.getLogoName());
		if (bm != null) {
			ivlogo.setImageBitmap(bm);
		} else {
			ivlogo.setImageResource(R.drawable.icon);
		}
		ivDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.downStart(appId)) {
					ApkDownLoad.downApk(MyApplication.getContext(), app);
				}
			}
		});
	}

	private static View getImageView(String absolutePath) {
		LinearLayout layout = null;
		try {
			Bitmap bitmap = decodeBitmapFromFile(absolutePath, 400, 400);
			layout = new LinearLayout(MyApplication.getContext());
			layout.setLayoutParams(new LayoutParams(450, 450));
			layout.setGravity(Gravity.CENTER);

			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new LayoutParams(400, 400));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageBitmap(bitmap);
			layout.addView(imageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return layout;
	}

	private static Bitmap decodeBitmapFromFile(String absolutePath, int reqWidth, int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(absolutePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(absolutePath, options);

		return bm;
	}

	private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

	static int count = 0;

	public static void updateImgView() {
		try {
			LogUtil.i("ADDVIEWHorizontalScrollView");
			File photosFile = new File(Const.IMG_Down_LOCALURL + app.getTitle() + "/");
			if (photosFile.exists() && photosFile.isDirectory()) {
				if (photosFile.length() > 0) {
					for (File photoFile : photosFile.listFiles()) {
						if (!photoAdd.contains(photoFile.getName())) {
							View view = getImageView(photoFile.getAbsolutePath());
							if (null != view) {
								hscrollViewLin.addView(view);
								photoAdd.add(photoFile.getName());
							}

						}
					}
				}
			} else {
				if (count < 3 && !app.equals("")) {
					JsonDownLoad.getJson(Const.APPINFO_IMG_URL + appId, true, new JsonCallBack() {

						@Override
						public void jsonBack(String jsonstr) {
							parseImgJson(jsonstr);
						}
					});
				}
				LogUtil.i("count:" + count);
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			photoAdd.clear();
			startActivity(new Intent(this, BaseActivity.class));
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
