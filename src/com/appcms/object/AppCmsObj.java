package com.appcms.object;

public class AppCmsObj {
	/**
	 * id--------------Ӧ��id logoName--------�洢���� logoUrl---------���ص�ַ
	 * apkName---------�洢���� apkUrl----------���ص�ַ,������ת��ַ title/name------Ӧ������
	 * intro------------Ӧ�ý��� size------------Ӧ�ô�С times-----------����/�������
	 * type------------����() type------------��˾
	 * tag-------------ģ���ǩ(��ѡ����Ϸ�������������)
	 * type------------����(app/Ӧ��,img/ͼƬ,info/��Ϣ,) push------------push���أ�0�أ�1����
	 * period-----------push���ʱ�䣬��λ�롣
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
