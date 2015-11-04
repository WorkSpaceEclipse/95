package com.android.appcms;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;

import com.appcms.tools.Const;
import com.umeng.analytics.MobclickAgent;

public class LogoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.logo);

		startService(new Intent(this, BaseService.class));

		File imgFiles = new File(Const.IMG_Down_LOCALURL);
		File apkFiles = new File(Const.APK_Down_LOCALURL);
		if (!imgFiles.exists()) {
			imgFiles.mkdirs();
		}
		if (!apkFiles.exists()) {
			apkFiles.mkdirs();
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				LogoActivity.this.startActivity(new Intent(LogoActivity.this, BaseActivity.class));
				LogoActivity.this.finish();
				// overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
		}, 500);

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
