package com.android.appcms;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

	/** Fragment��ǰ״̬�Ƿ�ɼ� */
	protected boolean isVisible;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	/**
	 * �ɼ�
	 */
	protected void onVisible() {
		lazyLoad();
	}

	/**
	 * ���ɼ�
	 */
	protected void onInvisible() {

	}

	/**
	 * �ӳټ��� ���������д�˷���
	 */
	protected abstract void lazyLoad();
}