package com.appcms.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.appcms.MyApplication;
import com.appcms.object.AppCmsObj;
import com.umeng.analytics.MobclickAgent;

public class Tools {
	private static int totalSize;

	// д�ļ���SD��
	public static void writeFileToSD(InputStream fis, File file) {
		try {
			File temp = new File(Const.IMG_Down_LOCALURL + System.currentTimeMillis());

			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
			temp.renameTo(file.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeFileToSDwithNotify(InputStream fis, long apkLength, File apkFile, AppCmsObj ntObj) {
		Notificationtools.create(ntObj);
		try {
			File temp = new File(Const.IMG_Down_LOCALURL + System.currentTimeMillis());

			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int len = 0;
			int downloadCount = 0;
			while ((len = fis.read(buffer)) != -1) {
				totalSize += len;

				// Ϊ�˷�ֹƵ����֪ͨ����Ӧ�óԽ����ٷֱ�����10��֪ͨһ��
				if ((downloadCount == 0) || (int) (totalSize * 100 / apkLength) - 1 > downloadCount) {
					downloadCount += 1;
					Notificationtools.upDataNT(downloadCount, ntObj);
				}
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
			temp.renameTo(apkFile.getAbsoluteFile());
			Notificationtools.done(ntObj);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Drawable getApkIcon(Context context, String apkPath) {
		// ��ȡapkͼ��
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				return appInfo.loadIcon(pm);
			} catch (OutOfMemoryError e) {
				LogUtil.i("ApkIconLoader" + e.toString());
			}
		}
		return null;
	}

	public static String getApkName(Context context, String apkPath) {
		// ��ȡapkͼ��
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				return appInfo.loadLabel(pm).toString();
			} catch (OutOfMemoryError e) {
				LogUtil.i("ApkIconLoader" + e.toString());
			}
		}
		return null;
	}

	public static int getNFid(ViewGroup gp) {

		final int count = gp.getChildCount();

		for (int i = 0; i < count; ++i) {
			if (gp.getChildAt(i) instanceof ImageView) {
				return ((ImageView) gp.getChildAt(i)).getId();
			} else if (gp.getChildAt(i) instanceof ViewGroup)
				return getNFid((ViewGroup) gp.getChildAt(i));
		}
		return 0;
	}

	public static void openFile(Context context, File file) {
		MobclickAgent.onEvent(MyApplication.getContext(), "SystemInstall");
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static boolean checkCurrentFragment(int Index) {
		/*
		 * Ҫ���µ�UI�Ƿ��ǵ�ǰ��ʾ��UI
		 */
		if (MyApplication.currentFragment == Index) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean slientInstall(File file) {
		// �Զ���װ
		boolean result = false;
		Process process = null;
		OutputStream out = null;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.writeBytes("chmod 777 " + file.getPath() + "\n");
			dataOutputStream.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " + file.getPath());
			// �ύ����
			dataOutputStream.flush();
			// �ر�������
			dataOutputStream.close();
			out.close();
			int value = process.waitFor();

			// ����ɹ�
			if (value == 0) {
				MobclickAgent.onEvent(MyApplication.getContext(), "SlientInstall");
				result = true;
			} else if (value == 1) { // ʧ��
				result = false;
			} else { // δ֪���
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean checkNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					LogUtil.i("�������");
					return true;
				}
			}
		}
		LogUtil.i("���粻����");
		return false;
	}
}
