package com.cmlanche.scripts;

import android.graphics.Point;
import android.util.Log;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.executor.builder.SFStepBuilder;
import com.cmlanche.core.executor.builder.SwipStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;

import org.w3c.dom.Node;

/**
 * 点淘急速版脚本
 */
public class YingKeFastScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();

    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;

    public YingKeFastScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {
        if(!isTargetPkg()) {
            return;
        }
        Log.d(TAG,"executeScript()");

        NodeInfo info6 = findById("tt_splash_skip_btn");
        if(null != info6){
            Log.d(TAG,"FIND tt_splash_skip_btn");
            new SFStepBuilder().addStep(info6).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND tt_splash_skip_btn");
        }

        NodeInfo info = findById("b4v");
        if(null != info){
            Log.d(TAG,"FIND b4v");
            new SFStepBuilder().addStep(info).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND b4v");
        }

        NodeInfo info1 = findById("ap5");
        if(null != info1){
            Log.d(TAG,"FIND ap5");
            new SFStepBuilder().addStep(info1).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND ap5");
        }

        NodeInfo info2 = findByText("拆大额红包");
        if(null != info2){
            Log.d(TAG,"FIND 开抢中");
            new SFStepBuilder().addStep(new Point(100*((int)(Math.random()*10)),800)).get().execute();
            Utils.sleep(1000);
        }

        NodeInfo info3 = findById("ksad_end_close_btn");
        if(null != info3){
            Log.d(TAG,"FIND ksad_end_close_btn");
            new SFStepBuilder().addStep(info3).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND ksad_end_close_btn");
        }

        NodeInfo info4 = findById("gh");
        if(null != info4){
            Log.d(TAG,"FIND gh");
            new SFStepBuilder().addStep(info4).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND gh");
        }

        NodeInfo info7 = findByText("关闭");
        if(null != info7){
            Log.d(TAG,"FIND 关闭");
            new SFStepBuilder().addStep(info7).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND 关闭");
        }

        NodeInfo info5 = findById("bvd");
        if(null != info5){
            Log.d(TAG,"FIND bvd");
            new SFStepBuilder().addStep(info5).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND bvd");
        }


        NodeInfo info8 = findById("tt_video_ad_close_layout");
        if(null != info8){
            Log.d(TAG,"FIND tt_video_ad_close_layout");
            new SFStepBuilder().addStep(info8).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND tt_video_ad_close_layout");
        }

        NodeInfo info9 = findByText("放弃奖励");
        if(null != info9){
            Log.d(TAG,"FIND 放弃奖励");
            new SFStepBuilder().addStep(info9).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND 放弃奖励");
        }


        NodeInfo info10 = findById("cancel");
        if(null != info10){
            Log.d(TAG,"FIND cancel");
            new SFStepBuilder().addStep(info10).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND cancel");
        }


        NodeInfo info11 = findById("content");
        if(null != info11 && info11.getClassName().equals("android.widget.FrameLayout")){
            Log.d(TAG,"FIND content");
            NodeInfo info12 = findById("tt_top_dislike");
            NodeInfo info13 = findById("tt_reward_browser_webview");
            if(null == info8 && null == info12){
                if(null != info13){
                    new SFStepBuilder().addStep(new Point(950,200)).get().execute();
                    Utils.sleep(1000);
                }
            }
        }else {
            Log.d(TAG,"Not FIND cancel");
        }


        NodeInfo info12 = findById("tt_insert_dislike_icon_img");
        if(null != info12){
            Log.d(TAG,"FIND tt_insert_dislike_icon_img");
            new SFStepBuilder().addStep(info12).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND cancel");
        }

        NodeInfo info13 = findByText("恭喜获得奖励");
        if(null != info13){
            Log.d(TAG,"FIND 恭喜获得奖励");
            new SFStepBuilder().addStep(new Point(100,160)).get().execute();
            Utils.sleep(1000);
        }else {
            Log.d(TAG,"Not FIND 恭喜获得奖励");
        }
        ActionUtils.pressBack();
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
//        int x = MyApplication.getAppInstance().getScreenWidth() / 2 + (int)(Math.random()*100);
//        int margin = 100+ (int)(Math.random()*100);
//        int fromY = MyApplication.getAppInstance().getScreenHeight() - margin;
//        int toY = margin;
//        new SwipStepBuilder().setPoints(new Point(x, fromY), new Point(x, toY)).get().execute();
    }

    @Override
    protected int getMinSleepTime() {
        return 3000;
    }

    @Override
    protected int getMaxSleepTime() {
        return 3000;
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
