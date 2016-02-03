package com.xiaomi.smarthome.common.plug.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PhoneInfo {
	public static volatile boolean isArm64 = false;

	public static void initial() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				getCpuInfo();
			}
		}).start();
	}

	static void getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr);
			String str = localBufferedReader.readLine();
			if (str.contains("aarch64")) {
				isArm64 = true;
			}
			localBufferedReader.close();
		} catch (IOException e) {
		}
	}

}
