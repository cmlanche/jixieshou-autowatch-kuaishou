package com.cmlanche.scripts;

import com.blankj.utilcode.util.LogUtils;
import com.cmlanche.core.executor.builder.SFStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.model.AppInfo;

public class DouyinFastAdvertScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();
    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;

    private int autoType = Constant.AUTO_TYPE_ADVERT;

    private boolean adverting = false;

    public DouyinFastAdvertScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {
        if (!isTargetPkg()) {
            return;
        }

        if (autoType == Constant.AUTO_TYPE_ADVERT) {

            if (goPersonPage()) ;

            boolean isAdvert = isAdverting();
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

            //todo 看广告
            if (clickAdvert()) return;

            if (!isPersonPage()) {
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

    private boolean isAdverting() {
        NodeInfo nodeInfo1 = findByText("后可领取");
        if (nodeInfo1 != null) {
            LogUtils.dTag(TAG, "找到后可领取");
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

        if(clickContent("知道了")) return true;

        if(clickContent("再看一个获取")) return true;

        if(clickContent("立即签到")) return true;

        return false;
    }

    //广告页面弹出框关闭
    private void closeAdvert3() {
        //关闭该页面各种弹出框
        if(clickContent("继续观看")) return ;
    }

    //跳转个人中心
    private boolean goPersonPage() {
        NodeInfo nodeInfo1 = findById("cqt");//红包浮动框
        if (nodeInfo1 != null) {
            new SFStepBuilder().addStep(nodeInfo1).get().execute();
            LogUtils.dTag(TAG, "click goPersonPage");
            return true;
        }
        return false;
    }

    //看广告
    private boolean clickAdvert() {
        LogUtils.dTag(TAG, "找广告");

        if(clickContent("看广告视频再赚")) return true;

        if(clickContent("开宝箱得金币")) return true;

        if(clickContent("看广告赚金币")) return true;

        if(clickContent("再看一个获取")) return true;

        ActionUtils.xiahua();
        return false;
    }

}
