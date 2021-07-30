package com.cmlanche.scripts;

import android.content.Context;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.LogUtils;
import com.cmlanche.application.MyApplication;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.utils.ActionUtils;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.model.AppInfo;

public class DouyinFastShopingScript extends BaseScript {
    private String TAG = this.getClass().getSimpleName();
    private Context context;

    private int autoType = Constant.AUTO_TYPE_ADVERT;
    private int x;
    private int y;

    public DouyinFastShopingScript(AppInfo appInfo) {
        super(appInfo);
    }

    public DouyinFastShopingScript(Context context, AppInfo appInfo) {
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
                x = nodeInfo.getRect().right - 100;
                y = nodeInfo.getRect().top + 246;
                loadBitmapFromView();
                if(checkButtonIsEnable(x,y)){
                    LogUtils.dTag(TAG, "点击抢购 X:" + (nodeInfo.getRect().right - 100) + " Y:" + (nodeInfo.getRect().top + 246));
                    ActionUtils.click(nodeInfo.getRect().right - 100, nodeInfo.getRect().top + 246);
                    clickBaby = true;
                }
                clickBaby = true;
                return;
            }
        }
    }

    private boolean checkButtonIsEnable(int x, int y) {
        return false;
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

    public Bitmap loadBitmapFromView() {
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(MyApplication.getAppInstance().getScreenWidth(), MyApplication.getAppInstance().getScreenHeight(), Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(screenshot);
//        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
//        v.draw(canvas);// 将 view 画到画布上
        int color = screenshot.getPixel(x,y);
        LogUtils.dTag(TAG,"color:"+color);
        screenshot.recycle();
        return screenshot;
    }
}
