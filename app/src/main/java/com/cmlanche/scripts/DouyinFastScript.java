package com.cmlanche.scripts;

import android.graphics.Point;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.executor.builder.SwipStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.model.AppInfo;

/**
 * 抖音急速版脚本
 */
public class DouyinFastScript extends BaseScript {

    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;
    // 是否检查到底部
    private boolean isCheckedBootom;
    private int bottomMargin = 200;


    public DouyinFastScript(AppInfo appInfo) {
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

        if(!isCheckedBootom) {
            NodeInfo nodeInfo = findById("kh");
            if(nodeInfo != null) {
                bottomMargin = MyApplication.getAppInstance().getScreenHeight() - nodeInfo.getRect().top + 10;
                isCheckedBootom = true;
            }
        }

        int x = MyApplication.getAppInstance().getScreenWidth() / 2;
        int fromY = MyApplication.getAppInstance().getScreenHeight() - bottomMargin;
        int toY = 100;

        new SwipStepBuilder().setPoints(new Point(x, fromY), new Point(x, toY)).get().execute();
    }

    @Override
    protected int getMinSleepTime() {
        return 10000;
    }

    @Override
    protected int getMaxSleepTime() {
        return 20000;
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
