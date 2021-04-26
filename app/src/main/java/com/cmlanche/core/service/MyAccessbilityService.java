package com.cmlanche.core.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.bus.BusEvent;
import com.cmlanche.core.bus.BusManager;
import com.cmlanche.core.bus.EventType;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.StringUtil;
import com.cmlanche.core.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cmlanche.core.bus.EventType.accessiblity_connected;

public class MyAccessbilityService extends AccessibilityService {

    private int noRootCount = 0;
    private static final int maxNoRootCount = 3;
    private boolean isWork = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Logger.d("MyAccessbilityService event: " + event);
    }

    @Override
    public void onInterrupt() {
        Logger.e("MyAccessbilityService 服务被Interrupt");
    }

    public AccessibilityNodeInfo[] getRoots() {
        AccessibilityNodeInfo activeRoot = getRootInActiveWindow();
        String activeRootPkg = Utils.getRootPackageName(activeRoot);

        Map<String, AccessibilityNodeInfo> map = new HashMap<>();
        if(activeRoot != null){
            map.put(activeRootPkg, activeRoot);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            List<AccessibilityWindowInfo> windows = getWindows();
            for (AccessibilityWindowInfo w : windows) {
                if(w.getRoot() == null || getPackageName().equals(Utils.getRootPackageName(w.getRoot()))) {
                    continue;
                }
                String rootPkg = Utils.getRootPackageName(w.getRoot());
                if(getPackageName().equals(rootPkg)) {
                    continue;
                }
                if(rootPkg.equals(activeRootPkg)) {
                    continue;
                }
                map.put(rootPkg, w.getRoot());
            }
        }
        if (map.isEmpty()) {
            noRootCount++;
        } else {
            if(!isWork) {
                MyApplication.getAppInstance().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BusManager.getBus().post(new BusEvent<>(EventType.roots_ready));
                    }
                });
            }
            isWork = true;
            noRootCount = 0;
        }
        if (noRootCount >= maxNoRootCount) {
            isWork = false;
            MyApplication.getAppInstance().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusManager.getBus().post(new BusEvent<>(EventType.no_roots_alert));
                }
            });
        }
        return map.values().toArray(new AccessibilityNodeInfo[0]);
    }

    public boolean containsPkg(String pkg) {
        if(StringUtil.isEmpty(pkg)) {
            return false;
        }
        AccessibilityNodeInfo[] roots = getRoots();
        for(AccessibilityNodeInfo root: roots) {
            if(pkg.equals(Utils.getRootPackageName(root))) {
                return true;
            }
        }
        return false;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        Logger.d("MyAccessbilityService on create");

        BusManager.getBus().post(new BusEvent<>(EventType.set_accessiblity, this));
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.d("MyAccessbilityService on start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("MyAccessbilityService on start command");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("MyAccessbilityService on unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Logger.d("MyAccessbilityService on rebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Logger.d("MyAccessbilityService on task removed");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Logger.d("MyAccessbilityService connected");
        BusManager.getBus().post(new BusEvent<>(accessiblity_connected));
        isWork = true;
    }

    public boolean isWrokFine() {
        return isWork;
    }
}
