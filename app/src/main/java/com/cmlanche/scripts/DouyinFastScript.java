package com.cmlanche.scripts;

import android.graphics.Point;
import android.util.Log;

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
import com.umeng.socialize.utils.UmengText;

import java.text.DateFormat;
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
    private boolean advertTimeArrive = true;
    private boolean adverting = false;

   public DouyinFastScript(AppInfo appInfo) {
       super(appInfo);

   }

    @Override
    protected void executeScript() {
        if(!isTargetPkg()) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todaySign = "sign_"+ TimeUtils.millis2String(TimeUtils.getNowMills(),dateFormat);
//        LogUtils.dTag(TAG,todaySign);
        todaySignSuccess = SPUtils.getInstance().getBoolean(todaySign, true);

        if(autoType == Constant.AUTO_TYPE_SCAN){
            LogUtils.dTag(TAG,"AUTO_TYPE_SCAN");
            if(!todaySignSuccess){
                autoType = Constant.AUTO_TYPE_SIGN;
                return;
            }
            if(advertTimeArrive){
                autoType = Constant.AUTO_TYPE_ADVERT;
                return;
            }
            //todo 自动浏览视频
            int x = MyApplication.getAppInstance().getScreenWidth() / 2+ (int)(Math.random()*100);
            int fromY = MyApplication.getAppInstance().getScreenHeight() - bottomMargin;
            int toY = 100+ (int)(Math.random()*100);;
            new SwipStepBuilder().setPoints(new Point(x, fromY), new Point(x, toY)).get().execute();
            return;
        }else if(autoType == Constant.AUTO_TYPE_SIGN){
            LogUtils.dTag(TAG,"AUTO_TYPE_SIGN");
            if(todaySignSuccess){
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
            SPUtils.getInstance().put("sign_"+ TimeUtils.millis2String(TimeUtils.getNowMills(),dateFormat), true);
            autoType = Constant.AUTO_TYPE_SCAN;

            clickBack();
            return;
        }else if(autoType == Constant.AUTO_TYPE_ADVERT){
            LogUtils.dTag(TAG,"AUTO_TYPE_ADVERT");
            if(!advertTimeArrive){
                autoType = Constant.AUTO_TYPE_SCAN;
                return;
            }
            if(adverting){
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

        if(!isCheckedWozhidaole) {
            // 检查是否有青少年模式
            NodeInfo nodeInfo = findByText("*为呵护未成年人健康*");
            if(nodeInfo != null) {
                nodeInfo = findByText("我知道了");
                if(nodeInfo != null) {
                    isCheckedWozhidaole = true;
                    ActionUtils.click(nodeInfo);
                }
            }
        }


    }


    @Override
    protected int getMinSleepTime() {
       switch (autoType){
           case 1:
               return 5000;
           case 2:
               return 2000;
           case 3:
               return 2000;
       }
        return 5000;
    }

    @Override
    protected int getMaxSleepTime() {
        switch (autoType){
            case 1:
                return 5000;
            case 2:
                return 2000;
            case 3:
                return 2000;
        }
        return 5000;
    }

    @Override
    public boolean isDestinationPage() {
        // 检查当前包名是否有本年应用
        if(!isTargetPkg()) {
            return false;
        }

        return true;
    }

    //浏览短视频页面弹出框关闭
    private void closeAdvert(){
        LogUtils.dTag(TAG,"closeAdvert");
        //关闭该页面各种弹出框
        NodeInfo nodeInfo = findById("kh");
        if(nodeInfo != null) {
            LogUtils.dTag(TAG,"closeAdvert success");
            bottomMargin = MyApplication.getAppInstance().getScreenHeight() - nodeInfo.getRect().top + 10;
            isCheckedBootom = true;
//            Utils.sleep(1000);
        }
    }

    //个人中心页面弹出框关闭
    private void closeAdvert1(){
        LogUtils.dTag(TAG,"closeAdvert1");
        //关闭该页面各种弹出框
//        NodeInfo nodeInfo = findById("kh");
//        if(nodeInfo != null) {
//            new SFStepBuilder().addStep(nodeInfo).get().execute();
//            Utils.sleep(1000);
//        }
    }

    //签到页面弹出框关闭
    private void closeAdvert2(){
        LogUtils.dTag(TAG,"closeAdvert2");
        //关闭该页面各种弹出框

    }

    //广告页面弹出框关闭
    private void closeAdvert3(){
        LogUtils.dTag(TAG,"closeAdvert3");
        //关闭该页面各种弹出框

    }

    //跳转个人中心
    private void goPersonPage() {
        LogUtils.dTag(TAG,"goPersonPage");
        NodeInfo nodeInfo1 = findById("cqt");//红包浮动框
        NodeInfo nodeInfo2 = findById("c32");//菜单栏来赚钱
        if(nodeInfo1 != null) {
            new SFStepBuilder().addStep(nodeInfo1).get().execute();
            LogUtils.dTag(TAG,"goPersonPage success");
            Utils.sleep(1000);
        }
//        if(nodeInfo2 != null) {
//            new SFStepBuilder().addStep(nodeInfo2).get().execute();
//            LogUtils.dTag(TAG,"goPersonPage success1");
//            Utils.sleep(1000);
//        }
    }

    //签到
    private void clickSignButton() {
        LogUtils.dTag(TAG,"clickSignButton");
//        NodeInfo nodeInfo = findById("kh");
//        if(nodeInfo != null) {
//            new SFStepBuilder().addStep(nodeInfo).get().execute();
//            Utils.sleep(1000);
//        }
    }

    //返回
    private void clickBack() {
        LogUtils.dTag(TAG,"clickBack");
        ActionUtils.pressBack();
//        Utils.sleep(1000);
    }

    //看广告
    private void clickAdvert() {
        LogUtils.dTag(TAG,"clickAdvert");
        NodeInfo nodeInfo1 = findByText("看广告赚金币");
        LogUtils.dTag(TAG,nodeInfo1 == null ? "找不到看广告赚金币" : "找到看广告赚金币");
//        NodeInfo nodeInfo2 = findById("去领取");
//        LogUtils.dTag(TAG,nodeInfo2 == null ? "找不到去领取" : "找到去领取");
//        NodeInfo nodeInfo3 = findById("每10分钟完成一次广告任务，单日最高可赚20000金币");
//        LogUtils.dTag(TAG,nodeInfo3 == null ? "找不到每10分钟完成一次广告任务，单日最高可赚20000金币" : "找到每10分钟完成一次广告任务，单日最高可赚20000金币");
        if(nodeInfo1 != null) {
            LogUtils.dTag(TAG,"clickAdvert success");
            new SFStepBuilder().addStep(new Point(850,1180)).get().execute();
//            Utils.sleep(1000);
            NodeInfo nodeInfo4 = findByText("看广告赚金币");
            LogUtils.dTag(TAG,nodeInfo4 == null ? "找不到看广告赚金币" : "找到看广告赚金币");
            if(null != nodeInfo4){
                clickBack();
                return;
            }
            adverting = true;
            startAdverting();
//            Utils.sleep(1000);
        }

    }

    private Disposable mDisposable;
    public void startAdverting() {
        LogUtils.dTag(TAG,"startAdverting");
        if (mDisposable == null || mDisposable.isDisposed()) {
            mDisposable = Observable.interval(0, 1, TimeUnit.MINUTES).take(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    LogUtils.dTag(TAG,"startAdverting accept"+aLong);
                    if(aLong == 1){
                        stopAdverting();
                    }
                }
            });
        }
    }

    public void stopAdverting() {
        LogUtils.dTag(TAG,"stopAdverting");
        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
            adverting = false;
            clickBack();
            autoType = Constant.AUTO_TYPE_SCAN;
            advertTimeArrive = false;
        }
    }

}
