package com.cmlanche.scripts;

import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.model.AppInfo;

public class TouTiaoAdvertScript extends BaseScript {

    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;
    private String TAG = this.getClass().getSimpleName();
    private boolean adverting = false;

    public TouTiaoAdvertScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {
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

        if (clickId("permission_allow_button")) return;

        if (clickContent("仅使用期间允许")) return;

        if (clickContent("立即预约")) return;

        if (clickContent("新人金币礼包")) ;

        if (clickContent("领福利")) return;
        if (clickContent("开心收下")) return;


//        if (clickContent("打开签到提醒")) return;

        if (clickContent("再看一个获得")) return;

        if (clickContent("视频再领")) return;

        clickBack();

        //[750,1890][1038,2181]
        if (clickXY(850,2000)) return;

    }

    //广告页面弹出框关闭
    private void closeAdvert3() {
        //关闭该页面各种弹出框
        if(clickContent("继续观看")) return ;
    }

    private boolean isAdverting() {
        if(findContent("s") && findContent("关闭")){
            return true;
        }
        return false;
    }

    //跳转个人中心
    private boolean goPersonPage() {
        if(clickId("as0")){
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
        if(!isTargetPkg()) {
            return false;
        }
        return true;
    }
}
