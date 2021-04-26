package com.cmlanche.common;

import android.os.Build;

import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.StringUtil;

import java.lang.reflect.Method;

public class DeviceUtils {
    public static String getDeviceSN() {
        String serial = Build.SERIAL;
        if (!StringUtil.isEmpty(serial)) {
            return serial;
        }
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            Logger.i("通过反射获取设备序列号失败");
        }
        return serial;
    }
}
