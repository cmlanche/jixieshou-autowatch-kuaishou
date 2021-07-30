package com.cmlanche.scripts;

import android.graphics.Point;

import com.blankj.utilcode.util.LogUtils;
import com.cmlanche.application.MyApplication;
import com.cmlanche.common.PackageUtils;
import com.cmlanche.core.executor.builder.SwipStepBuilder;
import com.cmlanche.core.search.FindById;
import com.cmlanche.core.search.FindByText;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.Utils;
import com.cmlanche.model.AppInfo;

import java.util.Random;

public abstract class BaseScript implements IScript {
    private String TAG = this.getClass().getSimpleName();

    private AppInfo appInfo;
    private long startTime;

    public BaseScript(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    protected long getTimeout() {
        return appInfo.getPeriod() * 60 * 60 * 1000;
    }

    @Override
    public void execute() {
        startApp();
        resetStartTime();

        // 总时间
        while ((System.currentTimeMillis() - startTime < getTimeout())) {
            try {
                if (isPause()) {
                    Utils.sleep(2000);
                    continue;
                }
                executeScript();
            } catch (Exception e) {
                Logger.e("执行异常，脚本: " + appInfo.getName(), e);
            } finally {
                int t = getRandomSleepTime(getMinSleepTime(), getMaxSleepTime());
                Logger.i("休眠：" + t);
                Utils.sleep(t);
            }
        }
    }

    private boolean isPause() {
        return TaskExecutor.getInstance().isForcePause() ||
                TaskExecutor.getInstance().isPause();
    }

    @Override
    public AppInfo getAppInfo() {
        return appInfo;
    }

    @Override
    public void startApp() {
        PackageUtils.startApp(getAppInfo().getPkgName());
    }

    @Override
    public void resetStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 获取一个随机的休眠时间
     *
     * @param min
     * @param max
     * @return
     */
    private int getRandomSleepTime(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 通过id来查找控件
     *
     * @param id
     * @return
     */
    protected NodeInfo findById(String id) {
        return FindById.find(id);
    }

    protected NodeInfo findByText(String text) {
        return FindByText.find(text);
    }

    protected void runOnUiThread(Runnable runnable) {
        MyApplication.getAppInstance().getMainActivity().runOnUiThread(runnable);
    }

    /**
     * 当前页面是否是目标页面
     * @return
     */
    protected boolean isTargetPkg() {
        if(MyApplication.getAppInstance().getAccessbilityService().isWrokFine()) {
            if(!MyApplication.getAppInstance().getAccessbilityService().containsPkg(getAppInfo().getPkgName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取最大休眠时间
     *
     * @return
     */
    protected abstract int getMaxSleepTime();

    /**
     * 获取最小休眠时间
     *
     * @return
     */
    protected abstract int getMinSleepTime();

    /**
     * 执行脚本
     */
    protected abstract void executeScript();

    /**
     * 点击 content
     * @return
     */
    public boolean clickContent(String content) {
        NodeInfo nodeInfo = findByText(content);
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click: "+content);
            ActionUtils.click(nodeInfo);
            return true;
        }
        return false;
    }

    /**
     * 点击 x,y
     * @return
     */
    public boolean clickXY(int x, int y) {
        ActionUtils.click(x, y);
        return false;
    }

    /**
     * findContent
     * @return
     */
    public boolean findContent(String content) {
        NodeInfo nodeInfo = findByText(content);
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "find: "+content);
            return true;
        }
        return false;
    }
    /**
     * 点击 content
     * @return
     */
    public boolean clickId(String id) {
        NodeInfo nodeInfo = findById(id);
        if (nodeInfo != null) {
            LogUtils.dTag(TAG, "click: "+id);
            ActionUtils.click(nodeInfo);
            return true;
        }
        return false;
    }

    //返回
    public void clickBack() {
        LogUtils.dTag(TAG, "clickBack");
        ActionUtils.pressBack();
    }

    public void scrollUp(){
        int x = MyApplication.getAppInstance().getScreenWidth() / 2 + (int)(Math.random()*100);
        int margin = 100+ (int)(Math.random()*100);
        int fromY = MyApplication.getAppInstance().getScreenHeight() - margin;
        int toY = margin;
        new SwipStepBuilder().setPoints(new Point(x, fromY), new Point(x, toY)).get().execute();
    }
}
