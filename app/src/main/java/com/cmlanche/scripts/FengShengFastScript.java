package com.cmlanche.scripts;

import android.graphics.Point;
import android.util.Log;

import com.cmlanche.core.executor.builder.SFStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;

import java.util.Calendar;

public class FengShengFastScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();

    public FengShengFastScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {

        // 检测评论列表是否打开
        Calendar c = Calendar.getInstance();//
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        Log.d(TAG,"mHour:"+mHour +" mMinute:" + mMinute);
        if(mHour == 8 && mMinute == 30){

        }else if(mHour == 10 && mMinute == 30){

        }else if(mHour == 15 && mMinute == 49){

        }else{
            return;
        }
        NodeInfo info = findById("main_root_dl");
        if(null != info && !info.getClassName().equals("android.webkit.WebView")){
            Logger.d("null != nodeInfo" + info.getClassName());
            new SFStepBuilder().addStep(new Point(700,2280)).get().execute();
        }

        NodeInfo nodeInfo = findByText("打卡");
        if(null != nodeInfo && !nodeInfo.getClassName().equals("android.webkit.WebView")){
            Logger.d("null != nodeInfo" + nodeInfo.getClassName());
            new SFStepBuilder().addStep(nodeInfo).get().execute();
        }

        NodeInfo nodeInfo1 = findByText("打卡");
        if(null != nodeInfo1 && nodeInfo1.getClassName().equals("android.webkit.WebView")){
            Logger.d("null != nodeInfo" + nodeInfo1.getClassName());
            new SFStepBuilder().addStep(new Point(200,1500)).get().execute();
        }else {
            Logger.d("findByText(\"card_1_M\")失败");
        }

//        NodeInfo nodeInfo2 = findByText("打卡成功");
        NodeInfo nodeInfo3 = findByText("知道了");
        if(null != nodeInfo3){
            new SFStepBuilder().addStep(nodeInfo3).get().execute();
            Utils.sleep(1000);
            new SFStepBuilder().addStep(new Point(200,200)).get().execute();
        }else {
            Logger.d("findByText(\"打卡成功\")失败");
        }

        NodeInfo nodeInfo4 = findByText("确认");
        if(null != nodeInfo4){
            new SFStepBuilder().addStep(nodeInfo4).get().execute();
        }else {
            Logger.d("findByText(\"确认\")失败");
        }
    }

    @Override
    protected int getMinSleepTime() {
        return 10000;
    }

    @Override
    protected int getMaxSleepTime() {
        return 10000;
    }

    @Override
    public boolean isDestinationPage() {
        // 检查当前包名是否有本年应用
        if(!isTargetPkg()) {
            return false;
        }

        return true;
    }
}
