package com.cmlanche.core.search.node;

import android.graphics.Rect;
import android.util.Log;
import android.util.Xml;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cmlanche.application.MyApplication;
import com.cmlanche.core.utils.AccessibilityNodeInfoHelper;
import com.cmlanche.core.utils.Logger;
import com.cmlanche.core.utils.NodeText;
import com.cmlanche.core.utils.Utils;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 控件树dump工具
 */
public class Dumper {

    private AccessibilityNodeInfo[] roots;

    /**
     * xml序列化对象
     */
    private XmlSerializer serializer;

    private int screenW;
    private int screenH;

    /**
     * 是否过滤特殊字符
     */
    private boolean isNeedFilter = false;
    /**
     * 是否包含界面外的控件
     */
    private boolean includeControlsOutsideScreen = false;

    /**
     * 最终dump的控件树
     */
    private TreeInfo treeInfo;

    /**
     * 所有web节点，通常只有一个，不排除有多个
     */
    private List<AccessibilityNodeInfo> webviewNodes = new ArrayList<AccessibilityNodeInfo>();

    /**
     * 构造函数
     *
     * @param roots
     */
    public Dumper(AccessibilityNodeInfo[] roots) {
        this.roots = roots;
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            // 获取屏幕大小
            screenW = MyApplication.getAppInstance().getScreenWidth();
            screenH = MyApplication.getAppInstance().getScreenHeight();
            Log.e(Utils.tag, "orginal screen width: " + screenW + "   screen height: " + screenH);

            // 初始化xml序列化器
            serializer = Xml.newSerializer();
        } catch (Exception e) {
            Log.e(Utils.tag, e.getMessage(), e);
        }
    }

    /**
     * 设置是否需要过滤特殊字符
     *
     * @param isNeedFilter
     * @return
     */
    public Dumper withNeedFilter(boolean isNeedFilter) {
        this.isNeedFilter = isNeedFilter;
        return this;
    }

    /**
     * 设置是否包括在屏幕外的控件
     *
     * @param includeOutsideSceenControl
     * @return
     */
    public Dumper withIncludeOutsideSceenControl(boolean includeOutsideSceenControl) {
        this.includeControlsOutsideScreen = includeOutsideSceenControl;
        return this;
    }

    /**
     * 执行dump控件树的操作
     *
     * @return
     */
    public TreeInfo dump() {
        try {
            Logger.d("use new dumper");
            long st = System.currentTimeMillis();
            StringWriter stringWriter = new StringWriter();
            serializer.setOutput(stringWriter);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "hierarchy");

            if (treeInfo == null) {
                treeInfo = new TreeInfo();
            }
            // 解析所有节点
            Logger.d("window info roots size = " + roots.length);
            for (int i = 0; i < roots.length; i++) {
                AccessibilityNodeInfo root = roots[i];
                Logger.d(String.format("%d.dump root package: %s", i + 1, Utils.getRootPackageName(root)));
                if (root != null && Utils.isVisiableToUser(root, screenW, screenH)) {
                    dumpNodeRec(root, i, null);
                } else {
                    Logger.e("remove root: " + root);
                }
            }

            // 解析WebView中的节点，可能会有多个WebView
            List<TreeNode<NodeInfo>> treeRootNodes = getWebViewTreeNodes();
            for (int i = 0; i < treeRootNodes.size(); i++) {
                Logger.d("dump web elements: " + treeRootNodes.get(i).getData());
                dumpWebNodeRec(treeRootNodes.get(i), i, null);
            }

            serializer.endTag("", "hierarchy");
            serializer.endDocument();
            String windowinfoStr = stringWriter.toString();
            treeInfo.setWindowinfo(windowinfoStr);
            Logger.d("dump window tree cost: " + (System.currentTimeMillis() - st));
            return treeInfo;
        } catch (Exception e) {
            Logger.d(e.getMessage(), e);
        }
        return null;
    }

    /**
     * dump一个Node节点的信息
     *
     * @param node        节点
     * @param index       当前node索引
     * @param parentXpath 父控件简单xpath
     * @throws IOException
     */
    private void dumpNodeRec(AccessibilityNodeInfo node, int index, String parentXpath) throws IOException {
        NodeInfo myrect = convertAccessbilityNode(node);

        int count = node.getChildCount();

        myrect.setLeaf(count == 0);

        // add xpath support.
        String myXpath = Integer.toString(index);
        if (parentXpath != null && !parentXpath.equals("")) {
            myXpath = String.format("%s-%s", parentXpath, myXpath);
        }
        myrect.setXpath(myXpath);
        addRect(myrect);

        serializer.startTag("", "node");
        writeToSerializer(myrect, myXpath, index);


        // Logger.d(Utils.tag, "node child count = " + count);
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                String className = child.getClassName().toString();
                if (Utils.isStandardWebView(className)) {
                    webviewNodes.add(child);
                    continue;
                }
                if (includeControlsOutsideScreen || Utils.isVisiableToUser(child, screenW, screenH)) {
                    dumpNodeRec(child, i, myXpath);
                    child.recycle();
                }
            }
        }

        serializer.endTag("", "node");
    }

    /**
     * dump web元素
     *
     * @param node
     * @param index
     * @param parentXpath
     * @throws IOException
     */
    private void dumpWebNodeRec(TreeNode<NodeInfo> node, int index, String parentXpath) throws IOException {
        NodeInfo myrect = node.getData();

        int count = node.getChildren() != null ? node.getChildren().size() : 0;

        myrect.setLeaf(count == 0);

        // add xpath support.
        String myXpath = Integer.toString(index);
        if (parentXpath != null && !parentXpath.equals("")) {
            myXpath = String.format("%s-%s", parentXpath, myXpath);
        }
        myrect.setXpath(myXpath);
        // 7.0+以上会识别图片，而6.0一下，则是android.view.View
        // 图片是不应该有文本的
        if (myrect.getClassName().equals("android.widget.Image")) {
            myrect.setClassName("android.view.View");
            myrect.setText("");
            myrect.setTextOriginal("");
            myrect.setTextIgnoreContentDesc("");
        }
        addRect(myrect);

        serializer.startTag("", "node");
        writeToSerializer(myrect, myXpath, index);

        // Logger.d(Utils.tag, "node child count = " + count);
        for (int i = 0; i < count; i++) {
            TreeNode<NodeInfo> child = node.getChildren().get(i);
            if (child != null) {
                dumpWebNodeRec(child, i, myXpath);
            }
        }

        serializer.endTag("", "node");
    }

    /**
     * 将一个myRect信息写入虚拟化器
     *
     * @param nodeInfo
     * @param myXpath
     * @param index
     * @throws IOException
     */
    private void writeToSerializer(NodeInfo nodeInfo, String myXpath, int index) throws IOException {
        serializer.attribute("", "text", nodeInfo.getText());
        serializer.attribute("", "name", nodeInfo.getId());
        serializer.attribute("", "class", nodeInfo.getClassName());
        serializer.attribute("", "bounds", nodeInfo.getBounds());
        serializer.attribute("", "xpath", myXpath);
        serializer.attribute("", "position", Integer.toString(index));
        serializer.attribute("", "index", Integer.toString(index));
        serializer.attribute("", "password", String.valueOf(nodeInfo.isPassword()));
        serializer.attribute("", "editable", Boolean.toString(nodeInfo.isEditable()));
        serializer.attribute("", "package", nodeInfo.getPackageName());
        serializer.attribute("", "content-desc", nodeInfo.getContentDesc());
        serializer.attribute("", "checkable", Boolean.toString(nodeInfo.isCheckable()));
        serializer.attribute("", "checked", Boolean.toString(nodeInfo.isChecked()));
        serializer.attribute("", "clickable", Boolean.toString(nodeInfo.isClickable()));
        serializer.attribute("", "enabled", Boolean.toString(nodeInfo.isEnabled()));
        serializer.attribute("", "focusable", Boolean.toString(nodeInfo.isFocusable()));
        serializer.attribute("", "focused", Boolean.toString(nodeInfo.isFocused()));
        serializer.attribute("", "long-clickable", Boolean.toString(nodeInfo.isLongClickable()));
        serializer.attribute("", "selected", Boolean.toString(nodeInfo.isSelected()));
        serializer.attribute("", "scrollable", Boolean.toString(nodeInfo.isScrollable()));
        serializer.attribute("", "isTextFromDes", Boolean.toString(nodeInfo.isTextFromDes()));
        serializer.attribute("", "apptype", nodeInfo.getApptype());
    }

    /**
     * 获取所有web元素节点
     *
     * @return
     */
    private List<TreeNode<NodeInfo>> getWebViewTreeNodes() {
        List<TreeNode<NodeInfo>> treeNodes = new ArrayList<TreeNode<NodeInfo>>();
        for (AccessibilityNodeInfo webNode : webviewNodes) {
            List<NodeInfo> container = new ArrayList<NodeInfo>();
            dumpWebViewItem(webNode, container);

            if (container.size() > 0) {
                TreeNode<NodeInfo> root = new TreeNode<NodeInfo>(container.get(0));
                for (int i = 1; i < container.size(); i++) {
                    addToTree(root, container.get(i));
                }
                treeNodes.add(root);
            }
        }
        return treeNodes;
    }

    /**
     * 递归dump web元素
     *
     * @param node
     * @param container
     */
    private void dumpWebViewItem(AccessibilityNodeInfo node, List<NodeInfo> container) {
        if (node != null) {
            container.add(convertAccessbilityNode(node));
            int childCount = node.getChildCount();
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) {
                    if (includeControlsOutsideScreen || Utils.isVisiableToUser(child, screenW, screenH)) {
                        dumpWebViewItem(node.getChild(i), container);
                        child.recycle();
                    }
                }
            }
        }
    }

    /**
     * 添加一个元素节点
     *
     * @param rect
     */
    private void addRect(NodeInfo rect) {
        if (treeInfo != null) {
            treeInfo.addRect(rect);
        }
    }

    /**
     * 将一个Node转化成MyRect
     *
     * @param node
     * @return
     */
    private NodeInfo convertAccessbilityNode(AccessibilityNodeInfo node) {
        NodeInfo myrect = new NodeInfo();

        CharSequence packaeName = node.getPackageName() == null ? "" : node.getPackageName();
        myrect.setPackageName(packaeName.toString());
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            //compatible with recorder, use "name" instead of "resource-id"
            String name = Utils.pureId(Utils.safeCharSeqToString(node.getViewIdResourceName()));
            myrect.setId(name);
        } else {
            myrect.setId("");
        }

        // 获取文本
        NodeText nodeText = Utils.getNodeText(node, false);
        String textIgnoreContentDesc = Utils.getNodeText(node, false, true).getText();
        String text = Utils.getCompatibleText(isNeedFilter, nodeText.getText());

        myrect.setText(text);
        myrect.setTextIgnoreContentDesc(textIgnoreContentDesc);
        myrect.setTextOriginal(nodeText.getText());

        myrect.setPassword(node.isPassword());

        String className = Utils.safeCharSeqToString(node.getClassName());
        //add editable
        boolean isEdit = false;
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isEdit = node.isEditable();
        }
        if (!isEdit) {
            if (className.equals("android.widget.EditText") || className.toLowerCase().contains("edit")) {
                isEdit = true;
            }
        }
        myrect.setEditable(isEdit);
        myrect.setClassName(className);

        String packageName = Utils.safeCharSeqToString(node.getPackageName());
        myrect.setPackageName(packageName);

        String content_desc = node.getContentDescription() == null ? "" : node.getContentDescription().toString();
        content_desc = Utils.getSafeText(content_desc, false);
        myrect.setContentDesc(content_desc);
        myrect.setCheckable(node.isCheckable());
        myrect.setChecked(node.isChecked());
        myrect.setClickable(node.isClickable());
        myrect.setEnabled(node.isEnabled());
        myrect.setFocusable(node.isFocusable());
        myrect.setFocused(node.isFocused());
        myrect.setLongClickable(node.isLongClickable());
        myrect.setSelected(node.isSelected());
        Rect visiableRect = AccessibilityNodeInfoHelper.getVisibleBoundsInScreen(node, screenW, screenH, myrect);
        myrect.setBounds(visiableRect.toShortString());
        myrect.setScrollable(node.isScrollable());
        myrect.setApptype("auto");
        return myrect;
    }

    /**
     * 添加一个节点到控件树中
     *
     * @param nodeInfo
     * @return 是否添加成功
     */
    private boolean addToTree(TreeNode<NodeInfo> root, final NodeInfo nodeInfo) {
        return root.addToTree(nodeInfo, new TreeNode.Detector<NodeInfo>() {
            @Override
            public TreeNode.RelationShip getRelationship(TreeNode<NodeInfo> currNode, NodeInfo newData) {
                if (newData.getVisiableRect().height() <= 8
                        || newData.getVisiableRect().width() <= 8) {
                    return TreeNode.RelationShip.IGNORED;
                }
                Rect c = currNode.getData().getVisiableRect();
                Rect cp = null;
                if (currNode.getParent() != null) {
                    cp = currNode.getParent().getData().getVisiableRect();
                }
                Rect n = newData.getVisiableRect();
                if (c.equals(n)) {
                    if (currNode.getData().valueable(newData)) {
                        return TreeNode.RelationShip.IGNORED;
                    } else {
                        return TreeNode.RelationShip.REPLACE;
                    }
                }
                if (n.contains(c) && (cp == null || cp.contains(n))) {
                    return TreeNode.RelationShip.PARENT;
                }
                if (c.contains(n)) {
                    if (currNode.getChildren() != null) {
                        // 如果当前节点的子节点还有包含新节点的话，则认为不清楚关系
                        for (TreeNode<NodeInfo> node : currNode.getChildren()) {
                            if (node.getData().getVisiableRect().contains(n)) {
                                return TreeNode.RelationShip.UNKNOW;
                            }
                        }
                    }
                    return TreeNode.RelationShip.CHILD;
                }

                if (Math.abs(c.centerY() - n.centerY()) <= 10) {
                    return TreeNode.RelationShip.BROTHER;
                }
                return TreeNode.RelationShip.UNKNOW;
            }
        });
    }
}
