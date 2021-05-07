package com.cmlanche.core.search;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.search.node.Dumper;
import com.cmlanche.core.search.node.NodeInfo;
import com.cmlanche.core.search.node.TreeInfo;
import com.cmlanche.core.utils.Utils;

public class FindById {
    static String TAG = "FindById";
    public static NodeInfo find(String id) {
        AccessibilityNodeInfo[] roots = MyApplication.getAppInstance().getAccessbilityService().getRoots();
        if (roots == null) {
            Log.i(Utils.tag, "roots is null.");
        }

        Log.i(Utils.tag, "roots size: " + roots.length);
        for (int i = 0; i < roots.length; i++) {
            AccessibilityNodeInfo root = roots[i];
            if (root != null) {
                Log.i(Utils.tag, String.format("%d. root package: %s", i + 1, Utils.getRootPackageName(root)));
            } else {
                Log.e(Utils.tag, "error: root is null, index: " + i);
            }
        }

        TreeInfo treeInfo = new Dumper(roots).dump();

        if (treeInfo != null && treeInfo.getRects() != null) {
            for (NodeInfo rect : treeInfo.getRects()) {
                if (isMatch(rect, id)) {
                    return rect;
                }
            }
        }
        return null;
    }

    private static boolean isMatch(NodeInfo nodeInfo, String id) {
        if (nodeInfo == null) {
            return false;
        }
        String rid = nodeInfo.getId();
        Log.d(TAG,"isMatch:"+rid);
        return Utils.textMatch(id, rid);
    }

}
