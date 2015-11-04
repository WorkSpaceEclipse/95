package com.appcms.tools;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appcms.object.AppCmsObj;

public class JsonTools {

	public static ArrayList<AppCmsObj> getCms(String jsonstr) {
		ArrayList<AppCmsObj> cmsList = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			JSONObject data = json.getJSONObject("data");
			cmsList = new ArrayList<AppCmsObj>();
			LogUtil.i("Ω‚ŒˆCMS");
			JSONArray jsonArray = data.getJSONArray("apps");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Iterator iterator = jsonObject.keys();
				AppCmsObj cms = new AppCmsObj();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (key.equals("id")) {
						cms.setId(jsonObject.getString(key));
					} else if (key.equals("logoUrl")) {
						cms.setLogoUrl(jsonObject.getString(key));
						cms.setLogoName(jsonObject.getString("logoUrl").substring(
								jsonObject.getString("logoUrl").lastIndexOf("/") + 1,
								jsonObject.getString("logoUrl").length()));
					} else if (key.equals("apkUrl")) {
						cms.setApkUrl(jsonObject.getString(key));
						if (key.contains(".apk")) {
							cms.setApkUrl(jsonObject.getString("apkUrl"));
							cms.setApkName(jsonObject.getString("apkUrl").substring(
									jsonObject.getString("apkUrl").lastIndexOf("/") + 1,
									jsonObject.getString("apkUrl").length()));

						} else {
							cms.setApkUrl(jsonObject.getString("apkUrl"));
							cms.setApkName(jsonObject.getString("apkUrl").substring(
									jsonObject.getString("apkUrl").lastIndexOf("=") + 1,
									jsonObject.getString("apkUrl").length())
									+ ".apk");
						}
					} else if (key.equals("title")) {
						cms.setTitle(jsonObject.getString(key));
					} else if (key.equals("intro")) {
						cms.setIntro(jsonObject.getString(key));
					} else if (key.equals("size")) {
						cms.setSize(jsonObject.getString(key));
					} else if (key.equals("times")) {
						cms.setTimes(jsonObject.getString(key));
					} else if (key.equals("company")) {
						cms.setCompany(jsonObject.getString(key));
					} else if (key.equals("tag")) {
						cms.setTag(jsonObject.getString(key));
					} else if (key.equals("type")) {
						cms.setType(jsonObject.getString(key));
					} else if (key.equals("push")) {
						cms.setPush(jsonObject.getString(key));
					} else if (key.equals("period")) {
						cms.setPeriod(jsonObject.getString(key));
					}
				}
				cmsList.add(cms);
			}
		} catch (JSONException e) {
			Log.i("info", "Ω‚Œˆ≥ˆ¥Ì");
			e.printStackTrace();
		}
		return cmsList;
	}

}
