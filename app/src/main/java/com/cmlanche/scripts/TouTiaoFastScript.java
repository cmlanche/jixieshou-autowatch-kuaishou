package com.cmlanche.scripts;

import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.model.AppInfo;

public class TouTiaoFastScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();

    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;
    private boolean isFasting = false;

    public TouTiaoFastScript(AppInfo appInfo) {
        super(appInfo);
    }

    @Override
    protected void executeScript() {
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

        if (clickContent("万次播放")){
            isFasting = true;
            return;
        }

        if(isFasting){
            scrollUp();
            return;
        }

        if (goPersonPage()) return;

        if (clickContent("新人金币礼包")) ;

        if (clickContent("不再提示")) return;
        if (clickContent("知道了")) return;

        if (clickContent("看视频赚钱")) return;

    }

    //跳转个人中心
    private boolean goPersonPage() {
        if (clickId("acu")) {
            return true;
        }
        return false;
    }

    @Override
    protected int getMinSleepTime() {
        return 4000;
    }

    @Override
    protected int getMaxSleepTime() {
        return 6000;
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
