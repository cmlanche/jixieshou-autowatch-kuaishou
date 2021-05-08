package com.cmlanche.scripts;

import android.graphics.Point;
import android.util.Log;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.executor.builder.SFStepBuilder;
import com.cmlanche.core.executor.builder.SwipStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.model.AppInfo;

/**
 * 点淘急速版脚本
 */
public class DianTaoFastScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();

    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;

    public DianTaoFastScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {
        Log.d(TAG,"executeScript()");
        NodeInfo info = findById("homepage_container");
        if(null != info){
            Log.d(TAG,"FIND homepage_container");
            new SFStepBuilder().addStep(new Point(200,700)).get().execute();
        }

        NodeInfo info1 = findById("gold_countdown_layout");
        if(null != info1){
            Log.d(TAG,"FIND gold_countdown_layout");
            NodeInfo info2 = findById("gold_turns_container");
            if(null == info2){
                Log.d(TAG,"点击金蛋");
                new SFStepBuilder().addStep(info1).get().execute();
            }else {
                Log.d(TAG,"FIND gold_turns_container");
            }
        }

        NodeInfo info2 = findByText("开抢中");
        if(null != info2){
            Log.d(TAG,"FIND 开抢中");
            new SFStepBuilder().addStep(new Point(1000,200)).get().execute();
        }
        NodeInfo info3 = findByText("设为最爱参与");
        if(null != info3){
            Log.d(TAG,"FIND 设为最爱参与");
            new SFStepBuilder().addStep(new Point(1000,200)).get().execute();
        }

//        if(!isCheckedWozhidaole) {
//            // 检查是否有青少年模式
//            NodeInfo nodeInfo = findByText("*为呵护未成年人健康*");
//            if(nodeInfo != null) {
//                nodeInfo = findByText("我知道了");
//                if(nodeInfo != null) {
//                    isCheckedWozhidaole = true;
//                    ActionUtils.click(nodeInfo);
//                }
//            }
//        }
//
        int x = MyApplication.getAppInstance().getScreenWidth() / 2 + (int)(Math.random()*100);
        int margin = 100+ (int)(Math.random()*100);
        int fromY = MyApplication.getAppInstance().getScreenHeight() - margin;
        int toY = margin;
        new SwipStepBuilder().setPoints(new Point(x, fromY), new Point(x, toY)).get().execute();
    }

    @Override
    protected int getMinSleepTime() {
        return 45000;
    }

    @Override
    protected int getMaxSleepTime() {
        return 45000;
    }

    @Override
    public boolean isDestinationPage() {
        // 检查当前包名是否有本年应用
        if(!isTargetPkg()) {
            return false;
        }
//        // 检测评论列表是否打开
//        NodeInfo nodeInfo = findById("comment_header_count");
//        if (nodeInfo != null) {
//            return false;
//        }
//        // 检测是否在输入按钮上
//        nodeInfo = findById("at_button");
//        if (nodeInfo != null) {
//            return false;
//        }
//        // 检测是否有滑屏页面
//        nodeInfo = findById("slide_play_view_pager");
//        if (nodeInfo == null) {
//            return false;
//        }
        return true;
    }
}
