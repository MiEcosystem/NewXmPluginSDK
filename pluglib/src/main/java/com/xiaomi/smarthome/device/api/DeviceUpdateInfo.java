package com.xiaomi.smarthome.device.api;

public class DeviceUpdateInfo {
	public boolean mHasNewFirmware;
	public String mCurVersion;
	public String mNewVersion;
	public String mUpdateDes;
	/**
	 * ApiLevel: 19
	 * 是否强制更新
	 */
	public int mForce;
}
