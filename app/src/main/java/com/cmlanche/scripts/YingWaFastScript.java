package com.cmlanche.scripts;

import android.graphics.Point;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.executor.builder.SwipStepBuilder;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.model.AppInfo;

public class YingWaFastScript extends BaseScript {

    // 是否有检查"我知道了"
    private boolean isCheckedWozhidaole;

    public YingWaFastScript(AppInfo appInfo) {
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

        int x = MyApplication.getAppInstance().getScreenWidth() / 2;
        int margin = 100;
        int fromY = MyApplication.getAppInstance().getScreenHeight() - margin;
        int toY = margin;
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
        // 检测评论列表是否打开
        NodeInfo nodeInfo = findById("comment_header_count");
        if (nodeInfo != null) {
            return false;
        }
        // 检测是否在输入按钮上
        nodeInfo = findById("at_button");
        if (nodeInfo != null) {
            return false;
        }
        // 检测是否有滑屏页面
//        nodeInfo = findById("slide_play_view_pager");
//        if (nodeInfo == null) {
//            return false;
//        }
        return true;
    }
}
