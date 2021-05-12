package com.cmlanche.core.utils;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmlanche.jixieshou.R;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class BaseUtil {

    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void showDownLoadDialog(String packageName ,final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getAppName(packageName)+"未安装");
        builder.setMessage("点击确定前往应用商店下载"+getAppName(packageName));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccessibilityUtil.goToAppMarket(context,packageName);
                showRecommendDialog(packageName,context);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private static boolean needRecommend(String packageName) {
        boolean needRecommend = false;
        if(packageName.equals(Constant.PN_DIAN_TAO)){
            needRecommend = true;
        }else if(packageName.equals(Constant.PN_KUAI_SHOU)){
            needRecommend = true;
        }else if(packageName.equals(Constant.PN_DOU_YIN)){
            needRecommend = true;
        }
//        else if(packageName.equals(Constant.PN_YING_KE)){
//            needRecommend = true;
//        }
        return needRecommend;
    }

    private static String getDescribeText(Context context,String packageName) {
        String describeText = "";
        if(packageName.equals(Constant.PN_DIAN_TAO)){
            describeText = context.getResources().getString(R.string.diantao_describe);
        }else if(packageName.equals(Constant.PN_KUAI_SHOU)){
            describeText = context.getResources().getString(R.string.kuaishou_describe);
        }else if(packageName.equals(Constant.PN_DOU_YIN)){
            describeText = context.getResources().getString(R.string.douyin_describe);
        }
//        else if(packageName.equals(Constant.PN_YING_KE)){
//            describeText = context.getResources().getString(R.string.yingke_describe);
//        }
        return describeText;
    }

    private static String getRecommendCode(String packageName) {
        String recommendCode = "";
        if(packageName.equals(Constant.PN_DIAN_TAO)){
            recommendCode = "LRHN7T5O";
        }else if(packageName.equals(Constant.PN_KUAI_SHOU)){
            recommendCode = "446859698";
        }else if(packageName.equals(Constant.PN_DOU_YIN)){
            recommendCode = "8161779848";
        }
//        else if(packageName.equals(Constant.PN_YING_KE)){
//            recommendCode = "";
//        }
        return recommendCode;
    }

    private static String getAppName(String packageName) {
        String recommendCode = "";
        if(packageName.equals(Constant.PN_DIAN_TAO)){
            recommendCode = "点淘App";
        }else if(packageName.equals(Constant.PN_KUAI_SHOU)){
            recommendCode = "快手极速版";
        }else if(packageName.equals(Constant.PN_DOU_YIN)){
            recommendCode = "抖音极速版";
        }else if(packageName.equals(Constant.PN_YING_KE)){
            recommendCode = "映客直播极速版";
        }
        return recommendCode;
    }


    public static void showRecommendDialog(String packageName ,final Context context){
        if(needRecommend(packageName)){
            showRecommendDilaog(getDescribeText(context,packageName),getRecommendCode(packageName),context);
        }
    }

    public static void showRecommendDilaog(String describe,String recommendCode, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_recommend, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView tv_copy = inflate.findViewById(R.id.tv_copy);
        TextView tv_describe = inflate.findViewById(R.id.tv_describe);
        MaterialButton btn_recommend_code = inflate.findViewById(R.id.btn_recommend_code);
        btn_recommend_code.setText("推荐码:"+recommendCode);
        tv_describe.setText(describe);
        tv_copy.setOnClickListener(v -> {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", recommendCode);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Toast.makeText(context, "推荐码已复制,可粘贴填写", Toast.LENGTH_LONG).show();
        });
        btn_recommend_code.setOnClickListener(v -> {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", recommendCode);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Toast.makeText(context, "推荐码已复制,可粘贴填写", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
    }
}
