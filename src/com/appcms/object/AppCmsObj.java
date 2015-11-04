package com.appcms.object;

public class AppCmsObj {
	/**
	 * id--------------应用id logoName--------存储名称 logoUrl---------下载地址
	 * apkName---------存储名称 apkUrl----------下载地址,下载跳转地址 title/name------应用名称
	 * intro------------应用介绍 size------------应用大小 times-----------下载/浏览次数
	 * type------------类型() type------------公司
	 * tag-------------模块标签(精选，游戏，软件。。。。)
	 * type------------类型(app/应用,img/图片,info/信息,) push------------push开关，0关，1开。
	 * period-----------push间隔时间，单位秒。
	 */
	private String id;
	private String logoName;
	private String logoUrl;
	private String apkName;
	private String apkUrl;
	private String title;
	private String intro;
	private String size;
	private String times;
	private String company;
	private String tag;
	private String type;
	private String push;
	private String period;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
