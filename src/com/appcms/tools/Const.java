package com.appcms.tools;

import android.os.Environment;

public class Const {
	public static final String RECOMMOND_JINGXUAN_URL = "http://api.e4001.com/recommend.php?tag=jingxuan&p=";
	public static final String FENLEI_URL = "http://api.e4001.com/recommend.php?tag=fenlei";
	public static final String FENLEI_INFO_URL = "http://api.e4001.com/recommend.php?tag=fenlei&info=";
	public static final String TOPIC_LIST_URL = "http://api.e4001.com/topic.php?tag=fenlei";
	public static final String TOPIC_INFO_URL = "http://api.e4001.com/topic.php?tag=fenlei&info=";
	public static final String GAME_URL = "http://api.e4001.com/game.php?tag=fenlei&info=12&p=";
	public static final String SOFT_URL = "http://api.e4001.com/software.php?tag=fenlei&info=3&p=";
	public static final String APPINFO_URL = "http://api.e4001.com/app.php?tag=info&id=";
	public static final String APPINFO_IMG_URL = "http://api.e4001.com/app.php?tag=img&id=";
	public static final String SEARCH_URL = "http://api.e4001.com/search.php?tag=";
	public static final String PUSH_URL = "http://api.e4001.com/push.php";
	public static final String TOP_SOFT_URL = "http://api.e4001.com/top.php?tag=soft&p=";
	public static final String TOP_GAME_URL = "http://api.e4001.com/top.php?tag=game&p=";

	public static final String IMG_Down_LOCALURL = Environment.getExternalStorageDirectory() + "/appcms/img/";
	public static final String APK_Down_LOCALURL = Environment.getExternalStorageDirectory() + "/appcms/apk/";
	public static final String FENLEI_SOFT = "软件";
	public static final String FENLEI_GAME = "游戏";
	public static final String APPID = "id";
	public static final String SP_NTID = "ntid";// push过的ID

	public static final int RECOMMOND_UPDATEVIEW = 1001;// 推荐更新UI
	public static final int TOPIC_UPDATEVIEW_FENLEI = 1002;// 更新专题列表
	public static final int TOPIC_UPDATEVIEW_INFO = 1003;// 更新专题页
	public static final int FENLEI_UPDATEVIEW = 1005;// 更新分类UI
	public static final int GAME_UPDATEVIEW = 1006;// 更新游戏UI
	public static final int SOFT_UPDATEVIEW = 1007;// 更新软件UI
	public static final int BASE_GATAPPID = 1008;// 更新软件UI
	public static final int BASE_APP_UPDATEVIEW = 1009;// 更新APPui
	public static final int BASE_APP_IMG_UPDATEVIEW = 1012;// 更新APPimg
	public static final int FENLEI_INFO_UPDATEVIEW = 1010;// 更新分类UI
	public static final int SEARCH_UPDATEVIEW = 1011;// 更新搜索UI
	public static final int BangDan_UPDATEVIEW = 1013;// 更新搜索UI
	public static final int PUSH_IMG_UPDATEVIEW = 1014;// 更新pushUI
	public static final int PUSH_APP_UPDATEVIEW = 1015;// 更新pushUI
	public static final int APK_DOWNLOAD_START = 2000;
	public static final int APK_DOWNLOAD_END = 2001;
	public static final int PROGRESS_VISIBILTY = 3000;
	public static final int PROGRESS_GONE = 3001;
	public static final int JSONNULL = 3002;
	public static final int ERRORCOUNT = 1;// 每个fragment 获取数据最多次数
	public static final int ISUPDATA = 3004;// fragment正在更新
	public static final int SEARCHISNULL = 3005;// 搜索内容为空

	public static final String INDEX = "index";
	public static final int RECOMMEND_INDEX = 0;
	public static final int GAME_INDEX = 1;
	public static final int SOFT_INDEX = 2;
	public static final int TOPIC_LIST_INDEX = 3;
	public static final int TOPIC_LISTINFO_INDEX = 4;
	public static final int FENLEI_INDEX = 5;
	public static final int FENLEI_INFO_INDEX = 6;
	public static final int SEARCH_INDEX = 7;
	public static final int BangDan_INDEX = 8;

}
