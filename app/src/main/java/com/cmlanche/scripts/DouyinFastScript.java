package com.cmlanche.scripts;

import android.graphics.Point;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.cmlanche.application.MyApplication;
import com.cmlanche.core.executor.builder.SFStepBuilder;
import com.cmlanche.core.executor.builder.SwipStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 抖音急速版脚本
 */
public class DouyinFastScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();
    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;
    // 是否检查到底部
    private boolean isCheckedBootom;
    private int bottomMargin = 200;

    private int autoType = Constant.AUTO_TYPE_SCAN;

    private boolean todaySignSuccess = false;
    private boolean advertTimeArrive = false;
    private boolean adverting = false;
    private long nextAdvertTime = 0;

    public DouyinFastScript(AppInfo appInfo) {
        super(appInfo);

    }

    @Override
    protected void executeScript() {
        if (!isTargetPkg()) {
            return;
        }
//        startAdvertTimeCount();
        long nowTime = System.currentTimeMillis();
        if (nowTime >= nextAdvertTime) {
            advertTimeArrive = true;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todaySign = "sign_" + TimeUtils.millis2String(nowTime, dateFormat);
//        LogUtils.dTag(TAG,todaySign);
        todaySignSuccess = SPUtils.getInstance().getBoolean(todaySign, true);

        if (autoType == Constant.AUTO_TYPE_SCAN) {
            LogUtils.dTag(TAG, "AUTO_TYPE_SCAN");

            if (!todaySignSuccess) {
                autoType = Constant.AUTO_TYPE_SIGN;
                return;
            }
            if (!isMainPage()) {
                LogUtils.dTag(TAG, "Not MainPage clickback");
                clickBack();
                return;
            }

            //todo 自动浏览视频
            int x = MyApplication.getAppInstance().getScreenWidth() / 2 + (int) (Math.random() * 100);
            int fromY = MyApplication.getAppInstance().getScreenHeight() - bottomMargin;
            int toY = 100 + (int) (Math.random() * 100);
            ;
            new SwipStepBuilder().setPoints(new Point(x, fromY), new Point(x, toY)).get().execute();

            if (advertTimeArrive) {
                autoType = Constant.AUTO_TYPE_ADVERT;
                closeAdvert();
                goPersonPage();
                return;
            }
            return;
        } else if (autoType == Constant.AUTO_TYPE_SIGN) {
            LogUtils.dTag(TAG, "AUTO_TYPE_SIGN");
            if (todaySignSuccess) {
                autoType = Constant.AUTO_TYPE_SCAN;
                return;
            }
            //todo 自动签到
            closeAdvert();
            goPersonPage();

            closeAdvert1();

            clickSignButton();

            closeAdvert2();

            clickBack();

            //todo 签到成功
            SPUtils.getInstance().put("sign_" + TimeUtils.millis2String(TimeUtils.getNowMills(), dateFormat), true);
            autoType = Constant.AUTO_TYPE_SCAN;

            clickBack();
            return;
        } else if (autoType == Constant.AUTO_TYPE_ADVERT) {
            LogUtils.dTag(TAG, "AUTO_TYPE_ADVERT");
            if (adverting) {
                closeAdvert3();
                return;
            }
            //todo 自动看广告
            closeAdvert();

            goPersonPage();

            closeAdvert1();

            clickAdvert();

//            closeAdvert3();

//            clickBack();
            //todo 看广告成功


//            clickBack();
            return;
        }

        if (!isCheckedWozhidaole) {
            // 检查是否有青少年模式
            NodeInfo nodeInfo = findByText("*为呵护未成年人健康*");
            if (nodeInfo != null) {
                nodeInfo = findByText("我知道了");
                if (nodeInfo != null) {
                    isCheckedWozhidaole = true;
                    ActionUtils.click(nodeInfo);
                }
            }
        }


    }

    @Override
    protected int getMinSleepTime() {
        switch (autoType) {
            case 1:
                return 5000;
            case 2:
                return 2000;
            case 3:
                return 5000;
        }
        return 5000;
    }

    @Override
    protected int getMaxSleepTime() {
        switch (autoType) {
            case 1:
                return 10000;
            case 2:
                return 2000;
            case 3:
                return 5000;
        }
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
    private void closeAdvert() {
        LogUtils.dTag(TAG, "closeAdvert");
        //关闭该页面各种弹出框
        NodeInfo nodeInfo = findById("kh");
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click closeAdvert");
            bottomMargin = MyApplication.getAppInstance().getScreenHeight() - nodeInfo.getRect().top + 10;
            isCheckedBootom = true;
//            Utils.sleep(1000);
        }
    }

    //个人中心页面弹出框关闭
    private void closeAdvert1() {
        LogUtils.dTag(TAG, "closeAdvert1");
        //关闭该页面各种弹出框
        NodeInfo nodeInfo = findByText("知道了");
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click 知道了");
            ActionUtils.click(nodeInfo);
        }
        LogUtils.dTag(TAG, "看广告视频再赚");
        //关闭该页面各种弹出框
        NodeInfo nodeInfo1 = findByText("看广告视频再赚");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "click 看广告视频再赚");
            ActionUtils.click(nodeInfo);
            adverting = true;
            startAdverting();
        }
    }

    //签到页面弹出框关闭
    private void closeAdvert2() {
        LogUtils.dTag(TAG, "closeAdvert2");
        //关闭该页面各种弹出框

    }

    //广告页面弹出框关闭
    private void closeAdvert3() {
        LogUtils.dTag(TAG, "closeAdvert3");
        //关闭该页面各种弹出框
        NodeInfo nodeInfo = findByText("继续观看，领取奖励");
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click 继续观看，领取奖励");
            ActionUtils.click(nodeInfo);
        }
    }

    //跳转个人中心
    private void goPersonPage() {
        LogUtils.dTag(TAG, "goPersonPage");
        NodeInfo nodeInfo1 = findById("cqt");//红包浮动框
        NodeInfo nodeInfo2 = findById("c32");//菜单栏来赚钱
//        if (nodeInfo1 != null) {
//            new SFStepBuilder().addStep(nodeInfo1).get().execute();
//            LogUtils.dTag(TAG, "click goPersonPage");
//            return;
////            Utils.sleep(1000);
//        }
        if (nodeInfo2 != null) {
            new SFStepBuilder().addStep(nodeInfo2).get().execute();
            LogUtils.dTag(TAG, "click goPersonPage");
            return;
//            Utils.sleep(1000);
        }
//        if(nodeInfo2 != null) {
//            new SFStepBuilder().addStep(nodeInfo2).get().execute();
//            LogUtils.dTag(TAG,"goPersonPage success1");
//            Utils.sleep(1000);
//        }
    }

    //签到
    private void clickSignButton() {
        LogUtils.dTag(TAG, "clickSignButton");
//        NodeInfo nodeInfo = findById("kh");
//        if(nodeInfo != null) {
//            new SFStepBuilder().addStep(nodeInfo).get().execute();
//            Utils.sleep(1000);
//        }
    }

    //返回
    private void clickBack() {
        LogUtils.dTag(TAG, "clickBack");
        ActionUtils.pressBack();
//        Utils.sleep(1000);
    }

    boolean goAdvertPage = false;

    //看广告
    private void clickAdvert() {
        LogUtils.dTag(TAG, "clickAdvert");
        NodeInfo nodeInfo1 = findByText("看广告赚金币");
        LogUtils.dTag(TAG, nodeInfo1 == null ? "找不到看广告赚金币" : "找到看广告赚金币");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "click clickAdvert");
            new SFStepBuilder().addStep(new Point(850, 1180)).get().execute();
            goAdvertPage = true;
//            clickBack();
            return;
//            Utils.sleep(1000);
        }
        if (nodeInfo1 == null) {
            if (goAdvertPage) {
                goAdvertPage = false;
                adverting = true;
                startAdverting();

            }
        }

    }

    private Disposable mDisposable;
    private Disposable mDisposable1;

    public void startAdverting() {
        LogUtils.dTag(TAG, "startAdverting");
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = Observable.interval(0, 1, TimeUnit.MINUTES).take(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    LogUtils.dTag(TAG, "startAdverting accept" + aLong);
                    if (aLong == 1) {
                        stopAdverting();
                    }
                }
            });
        }
    }


    public void stopAdverting() {
        LogUtils.dTag(TAG, "stopAdverting");
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            adverting = false;
            clickBack();
            autoType = Constant.AUTO_TYPE_SCAN;
            long nowTime = System.currentTimeMillis();
            nextAdvertTime = nowTime + 1000 * 60 * 11;
            advertTimeArrive = false;
        }
    }

//    public void startAdvertTimeCount() {
//        LogUtils.dTag(TAG,"startAdvertTimeCount");
//        if (mDisposable1 == null || mDisposable1.isDisposed()) {
//            mDisposable1 = Observable.interval(0, 11, TimeUnit.MINUTES).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
//                @Override
//                public void accept(Long aLong) throws Exception {
//                    LogUtils.dTag(TAG,"startAdvertTimeCount advertTimeArrive:"+advertTimeArrive);
//                    advertTimeArrive = true;
//                }
//            });
//        }
//    }

    public void stopAdvertTimeCount() {
        LogUtils.dTag(TAG, "stopAdverting");
        if (mDisposable1 != null && !mDisposable1.isDisposed()) {
            mDisposable1.dispose();
            advertTimeArrive = false;
        }
    }

    private boolean isMainPage() {
//        NodeInfo nodeInfo = findById("bbk");//左上角直播图标
//        LogUtils.dTag(TAG, nodeInfo == null ? "not find bbk" : "find bbk");
//        NodeInfo nodeInfo1 = findById("bzj");//最下面菜单栏
//        LogUtils.dTag(TAG, nodeInfo1 == null ? "not find bzj" : "find bzj");
//        NodeInfo nodeInfo2 = findById("amc");//点赞
//        LogUtils.dTag(TAG, nodeInfo2 == null ? "not find amc" : "find amc");
//        NodeInfo nodeInfo3 = findById("zj");//评论
//        LogUtils.dTag(TAG, nodeInfo3 == null ? "not find zj" : "find zj");
//        NodeInfo nodeInfo4 = findById("d65");//转发
//        LogUtils.dTag(TAG, nodeInfo4 == null ? "not find d65" : "find d65");
        NodeInfo nodeInfo5 = findById("cqt");//转发
        LogUtils.dTag(TAG, nodeInfo5 == null ? "not find cqt" : "find cqt");
        if (nodeInfo5 == null) {
            return false;
        }
        return true;
    }


}
