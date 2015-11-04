package com.android.appcms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appcms.tools.LogUtil;

public class BaseBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals("android.intent.action.BOOT_COMPLETED")
				|| action.equals("android.intent.action.USER_PRESENT")) {
			LogUtil.i("BaseBroadcast");
			context.startService(new Intent(context, BaseService.class));
		}
	}

}
