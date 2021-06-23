package com.cmlanche.scripts;

import android.graphics.Point;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.cmlanche.core.executor.builder.SFStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;

import java.text.SimpleDateFormat;

public class DouyinFastAdvertScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();
    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;

    private int autoType = Constant.AUTO_TYPE_ADVERT;

    private boolean adverting = false;
    private boolean todayChoujiangSuccess = false;

    public DouyinFastAdvertScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {
        if (!isTargetPkg()) {
            return;
        }

        if (autoType == Constant.AUTO_TYPE_ADVERT) {

            goPersonPage();

            boolean isAdvert = checkAdverting();
            if (isAdvert) {
                closeAdvert3();
                adverting = isAdvert;
                return;
            }
            if (adverting && !isAdvert) {
                clickBack();
                adverting = isAdvert;
                return;
            }

            //todo 关闭弹框
            if (closeAdvert()) return;

            //抽奖模块
            if (!todayChoujiangSuccess) {
                LogUtils.dTag(TAG, "未抽奖 下划");
                if (clickChouJiang()) return;
                ActionUtils.xiahua();
            }else {
                LogUtils.dTag(TAG, "已抽奖 上划");
                ActionUtils.shanghua();
            }

            //todo 看广告
            if (clickAdvert()) return;

            if(!isPersonPage()){
                clickBack();
            }
            return;
        }

        if (!isCheckedWozhidaole) {
            // 检查是否有青少年模式
            NodeInfo nodeInfo = findByText("*为呵护未成年人健康*");
            if (nodeInfo != null) {
                nodeInfo = findByText("我知道了");
                if (nodeInfo != null) {
                    isCheckedWozhidaole = true;
                    LogUtils.dTag(TAG, "click 我知道了");
                    ActionUtils.click(nodeInfo);
                }
            }
        }

    }

    private boolean clickChouJiang() {
        NodeInfo node1 = findByText("每日抽手机");
        if (node1 != null) {
            LogUtils.dTag(TAG, "每日抽手机");
        }
        NodeInfo node2 = findByText("每日");
        if (node2 != null) {
            LogUtils.dTag(TAG, "每日");
        }
        NodeInfo node3 = findByText("抽手机");
        if (node3 != null) {
            LogUtils.dTag(TAG, "抽手机");
        }
        NodeInfo node4 = findByText("换手机");
        if (node4 != null) {
            LogUtils.dTag(TAG, "换手机");
        }
        NodeInfo node5 = findByText("去抽奖");
        if (node5 != null) {
            LogUtils.dTag(TAG, "去抽奖");
        }
        NodeInfo node6 = findByText("抽金币");
        if (node6 != null) {
            LogUtils.dTag(TAG, "抽金币");
        }


        LogUtils.dTag(TAG, "clickChouJiang()");
        NodeInfo nodeInfo1 = findByText("每日抽手机");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "每日抽手机");
            ActionUtils.click(nodeInfo1);
            return true;
        }

        NodeInfo nodeInfo2 = findByText("今日剩余");
        if (nodeInfo2 != null) {
            LogUtils.dTag(TAG, "今日剩余");
            ActionUtils.click(nodeInfo2);
//            return;
        }

        NodeInfo nodeInfo10 = findByText("今日次数已用完");
        if (nodeInfo10 != null) {
            LogUtils.dTag(TAG, "今日次数已用完");
            long nowTime = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String todaySign = "choujiang_" + TimeUtils.millis2String(nowTime, dateFormat);
            SPUtils.getInstance().put(todaySign, true);
            todayChoujiangSuccess = true;
            clickBack();
            return true;
        }

        NodeInfo nodeInfo3 = findByText("保留碎片");
        if (nodeInfo3 != null) {
            LogUtils.dTag(TAG, "保留碎片");
            ActionUtils.click(nodeInfo3);
            return true;
        }

        NodeInfo nodeInfo9 = findByText("领取奖励");
        if (nodeInfo9 != null) {
            LogUtils.dTag(TAG, "领取奖励");
            ActionUtils.click(nodeInfo9);
            return true;
        }

        NodeInfo nodeInfo5 = findByText("看视频奖励翻倍");
        if (nodeInfo5 != null) {
            LogUtils.dTag(TAG, "看视频奖励翻倍");
            ActionUtils.click(nodeInfo5);
            return true;
        }


        NodeInfo nodeInfo6 = findByText("看视频领取奖励");
        if (nodeInfo6 != null) {
            LogUtils.dTag(TAG, "看视频领取奖励");
            ActionUtils.click(nodeInfo6);
            return true;
        }

        NodeInfo nodeInfo7 = findByText("做任务获得抽奖次数");
        if (nodeInfo7 != null) {
            LogUtils.dTag(TAG, "做任务获得抽奖次数");
            ActionUtils.click(nodeInfo7);
            return true;
        }

        NodeInfo nodeInfo11 = findByText("增加抽奖次数");
        NodeInfo nodeInfo4 = findByText("去完成");
        if (nodeInfo4 != null && nodeInfo11 != null) {
            LogUtils.dTag(TAG, "去完成");
            ActionUtils.click(nodeInfo4);
            return true;
        }

        NodeInfo nodeInfo8 = findByText("已完成");
        if (nodeInfo8 != null && nodeInfo11 != null) {
            LogUtils.dTag(TAG, "已完成 上划");
            ActionUtils.shanghua();
            return true;
        }
        return false;
    }

    private boolean isPersonPage() {
        NodeInfo nodeInfo1 = findByText("现金收益");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "找到现金收益");
            return true;
        }

        NodeInfo nodeInfo2 = findByText("赚钱任务");
        if (nodeInfo2 != null) {
            LogUtils.dTag(TAG, "找到赚钱任务");
            return true;
        }
        return false;
    }

    private boolean checkAdverting() {
        NodeInfo nodeInfo1 = findByText("后可领取金币");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "找到后可领取金币");
            return true;
        }
        return false;
    }


    @Override
    protected int getMinSleepTime() {
        return 5000;
    }

    @Override
    protected int getMaxSleepTime() {
        return 5000;
    }

    @Override
    public boolean isDestinationPage() {
        // 检查当前包名是否有本年应用
        if (!isTargetPkg()) {
            return false;
        }

        return true;
    }

    //浏览短视频页面弹出框关闭
    private boolean closeAdvert() {
        LogUtils.dTag(TAG, "closeAdvert()");
        NodeInfo nodeInfo0 = findByText("知道了");
        if (nodeInfo0 != null) {
            LogUtils.dTag(TAG, "click 知道了");
            ActionUtils.click(nodeInfo0);
            return true;
        }

        //关闭该页面各种弹出框
//        NodeInfo nodeInfo = findById("kh");
//        if (nodeInfo != null) {
//            LogUtils.dTag(TAG, "click closeAdvert");
//            return true;
//        }

        NodeInfo nodeInfo1 = findByText("立即签到");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "click 立即签到");
            ActionUtils.click(nodeInfo1);
            return true;
        }

        NodeInfo nodeInfo2 = findByText("再看一个获取");
        if (nodeInfo2 != null) {
            LogUtils.dTag(TAG, "click 再看一个获取");
            ActionUtils.click(nodeInfo2);
            return true;
        }

        return false;
    }

    //广告页面弹出框关闭
    private void closeAdvert3() {
        //关闭该页面各种弹出框
        NodeInfo nodeInfo = findByText("继续观看");
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click 继续观看，领取奖励");
            ActionUtils.click(nodeInfo);
        }
    }

    //跳转个人中心
    private void goPersonPage() {
        NodeInfo nodeInfo1 = findById("cqt");//红包浮动框
        if (nodeInfo1 != null) {
            new SFStepBuilder().addStep(nodeInfo1).get().execute();
            LogUtils.dTag(TAG, "click goPersonPage");
            return;
        }
    }


    //返回
    private void clickBack() {
        LogUtils.dTag(TAG, "clickBack");
        ActionUtils.pressBack();

    }

    //看广告
    private boolean clickAdvert() {
        LogUtils.dTag(TAG, "clickAdvert()");

//        new SFStepBuilder().addStep(new Point(1000, 2300)).get().execute();

        NodeInfo nodeInfo = findByText("看广告视频再赚");
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click 看广告视频再赚");
            ActionUtils.click(nodeInfo);
            adverting = true;
            return true;
        }

        NodeInfo nodeInfo0 = findByText("开宝箱得金币");
        if (nodeInfo0 != null) {
            LogUtils.dTag(TAG, "click 开宝箱得金币");
            ActionUtils.click(nodeInfo0);
            return true;
        }

        NodeInfo nodeInfo1 = findByText("看广告赚金币");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "click 看广告赚金币");
            ActionUtils.click(nodeInfo1);
            return true;
        }

        NodeInfo nodeInfo2 = findByText("再看一个获取");
        if (nodeInfo2 != null) {
            LogUtils.dTag(TAG, "click 再看一个获取");
            ActionUtils.click(nodeInfo2);
            return true;
        }
        ActionUtils.xiahua();
        return false;
    }

    private boolean isMainPage() {
        LogUtils.dTag(TAG, "isMainPage");
        NodeInfo nodeInfo5 = findById("cqt");//转发
        LogUtils.dTag(TAG, nodeInfo5 == null ? "not find cqt" : "find cqt");
        if (nodeInfo5 == null) {
            return false;
        }
        return true;
    }


}
