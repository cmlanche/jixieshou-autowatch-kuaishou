package com.cmlanche.core.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.search.node.NodeInfo;

public class ActionUtils {

    /**
     * 点击某点
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean click(int x, int y) {
        if (Build.VERSION.SDK_INT >= 24) {
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path path = new Path();
            path.moveTo(x, y);
            GestureDescription gestureDescription = builder
                    .addStroke(new GestureDescription.StrokeDescription(path, 100, 50))
                    .build();
            return MyApplication.getAppInstance().getAccessbilityService().dispatchGesture(gestureDescription,
                    new AccessibilityService.GestureResultCallback() {
                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            super.onCompleted(gestureDescription);
                        }
                    }, null);
        }
        return false;
    }

    /**
     * 点击某个区域的中间位置
     *
     * @param rect
     */
    public static boolean click(NodeInfo rect) {
        return click(rect.getRect().centerX(), rect.getRect().centerY());
    }

    /**
     * 从某点滑动到某点
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     */
    public static boolean swipe(int fromX, int fromY, int toX, int toY, int steps) {
        if (Build.VERSION.SDK_INT >= 24) {
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path path = new Path();
            path.moveTo(fromX, fromY);
            path.lineTo(toX, toY);
            GestureDescription gestureDescription = builder
                    .addStroke(new GestureDescription.StrokeDescription(path, 100, 200))
                    .build();
            return MyApplication.getAppInstance().getAccessbilityService().dispatchGesture(gestureDescription,
                    new AccessibilityService.GestureResultCallback() {
                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            super.onCompleted(gestureDescription);
                        }
                    }, null);
        }
        return true;
    }

    /**
     * 按一次返回键
     *
     * @return
     */
    public static boolean pressBack() {
        return MyApplication.getAppInstance().getAccessbilityService()
                .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }
}
