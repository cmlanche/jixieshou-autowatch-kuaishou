package com.cmlanche.common.leancloud;

import android.os.Build;

import cn.leancloud.AVObject;

public class AVUtils {

    // 支付表
    public static final String tb_pay = "tb_pay";
    // 自动更新表
    public static final String tb_update = "tb_update";

    public static void regist(String serial) {
        AVObject user_pay = new AVObject(AVUtils.tb_pay);
        user_pay.put("serial", serial);
        user_pay.put("payed", false);
        user_pay.put("model", Build.MODEL);
        user_pay.put("brand", Build.BRAND);
        user_pay.put("sdk", Build.VERSION.SDK_INT);
        user_pay.save();
    }
}
