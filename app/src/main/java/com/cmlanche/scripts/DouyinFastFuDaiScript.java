package com.cmlanche.scripts;

import com.blankj.utilcode.util.LogUtils;
import com.cmlanche.application.MyApplication;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;

public class DouyinFastFuDaiScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();

    private int autoType = Constant.AUTO_TYPE_ADVERT;

    public DouyinFastFuDaiScript(AppInfo appInfo) {
        super(appInfo);
    }

    boolean showMarker = false;
    boolean clickBaby = false;

    @Override
    protected void executeScript() {
        if (!isTargetPkg()) {
            return;
        }

        if (autoType == Constant.AUTO_TYPE_ADVERT) {
//            NodeInfo node1= findByText("dfadfa");
//            NodeInfo node2= findById("fdsafdsaf");
//            return;
            if(!showMarker){
                LogUtils.dTag(TAG, "点击购物车");
                ActionUtils.click(650, 2300);
                showMarker = true;
                return;
            }

            if(clickBaby){
                NodeInfo node1= findByText("带货口碑");
                if(node1 != null){
                    LogUtils.dTag(TAG, "点击返回");
                    ActionUtils.pressBack();
                    showMarker = false;
                    clickBaby = false;
                }else {
                    NodeInfo node2= findByText("返回，按钮");
                    if(node2 != null){
                        LogUtils.dTag(TAG, "点击提交订单");
                        //todo 提交订单
                        ActionUtils.click(1000, 2300);
                        return;
                    }

                    NodeInfo node3= findByText("商品详情");
                    if(node3 != null){
                        LogUtils.dTag(TAG, "选规格");
                        //todo 选规格
                        ActionUtils.click(1000, 2300);
                        return;
                    }
                }
                return;
            }

            NodeInfo nodeInfo = findByText(MyApplication.getAppInstance().getBaby());
            if (nodeInfo != null) {
                LogUtils.dTag(TAG, "点击抢购 X:" + (nodeInfo.getRect().right - 100) + " Y:" + (nodeInfo.getRect().top + 246));
                ActionUtils.click(nodeInfo.getRect().right - 100, nodeInfo.getRect().top + 246);
                clickBaby = true;
                Utils.sleep(60);
                return;
            }
        }
    }

    @Override
    protected int getMinSleepTime() {
        return 10;
    }

    @Override
    protected int getMaxSleepTime() {
        return 10;
    }

    @Override
    public boolean isDestinationPage() {
        // 检查当前包名是否有本年应用
        if (!isTargetPkg()) {
            return false;
        }
        return true;
    }

}
