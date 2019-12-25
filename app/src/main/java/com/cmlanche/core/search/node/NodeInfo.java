package com.cmlanche.core.search.node;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cmlanche.core.utils.Utils;

import org.json.JSONObject;

/**
 */
public class NodeInfo {

    /**
     * 控件可见区域（只会控件在界面可见部分的区域，不可见的会被忽略）
     */
    private Rect visiableRect;
    /**
     * 控件的真实区域
     */
    private Rect rect;

    /**
     * 控件node文本内容
     */
    private String text = "";

    /**
     * 控件node文本内容，忽略ContentDesc
     */
    private String textIgnoreContentDesc = "";
    /**
     * 控件node文本内容,没有经过过滤
     */
    private String textOriginal = "";

    /**
     * 是否是叶子节点
     */
    private boolean leaf;

    /**
     * 当前节点所处同级位置索引
     * todo 有重复，需要去掉一个
     */
    private int position;
    private int index;

    private boolean isPassword;
    private boolean editable;
    private String contentDesc;
    private boolean clickable;
    private boolean enabled;
    private boolean focusable;
    private boolean focused;
    private boolean longClickable;
    private boolean selected;
    private boolean scrollable;
    private String apptype;
    private String bounds;

    /**
     * 简单xpath
     */
    private String xpath = "";

    /**
     * 是否可以被checkable的控件
     */
    private boolean checkable = false;

    /**
     * 如果是checkbox, 是否是勾选状态
     */
    private boolean checked = false;

    /**
     * 用于存储web控件信息
     */
    private JSONObject webNode;

    /**
     * className
     */
    private String className = "";

    /**
     *
     */
    private String id = "";
    /**
     * 当前my rect对应的node的hash值(仅仅用于测试)
     */
    private int nodeHash;
    private String packageName = "";
    private boolean isTextFromDes = false;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeInfo() {

    }

    public NodeInfo(Rect visiableRect, String text, String xpath) {
        this.setVisiableRect(visiableRect);
        this.setText(text);
        this.setXpath(xpath);
    }

    public Rect getVisiableRect() {
        return visiableRect;
    }

    public void setVisiableRect(Rect visiableRect) {
        this.visiableRect = visiableRect;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextIgnoreContentDesc() {
        return textIgnoreContentDesc;
    }

    public void setTextIgnoreContentDesc(String textIgnoreContentDesc) {
        this.textIgnoreContentDesc = textIgnoreContentDesc;
    }

    public String getTextOriginal() {
        return textOriginal;
    }

    public void setTextOriginal(String textOriginal) {
        this.textOriginal = textOriginal;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public JSONObject getWebNode() {
        return webNode;
    }

    public void setWebNode(JSONObject webNode) {
        this.webNode = webNode;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isLongClickable() {
        return longClickable;
    }

    public void setLongClickable(boolean longClickable) {
        this.longClickable = longClickable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public int getNodeHash() {
        return nodeHash;
    }

    public void setNodeHash(int nodeHash) {
        this.nodeHash = nodeHash;
    }

    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isTextFromDes() {
        return isTextFromDes;
    }

    public void setTextFromDes(boolean textFromDes) {
        isTextFromDes = textFromDes;
    }

    /**
     * 与另外一个比对，看哪个更有价值
     *
     * @param nodeInfo
     * @return true表示自己更有价值
     */
    public boolean valueable(NodeInfo nodeInfo) {
        if (Utils.isNotEmpty(text)
                || Utils.isNotEmpty(id)) {
            return true;
        }

        if (Utils.isNotEmpty(nodeInfo.getText()) || Utils.isNotEmpty(nodeInfo.getId())) {
            return false;
        }

        if (!className.equals("android.view.View")) {
            return true;
        }
        if (!nodeInfo.getClassName().equals("android.view.View")) {
            return false;
        }

        if (visiableRect.contains(nodeInfo.getVisiableRect())) {
            return false;
        }
        if (nodeInfo.getVisiableRect().contains(visiableRect)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", textOriginal='" + textOriginal + '\'' +
                ", className='" + className + '\'' +
                ", xpath='" + xpath + '\'' +
                ", bounds='" + bounds + '\'' +
                ", packageName='" + packageName + '\'' +
                "visiableRect=" + visiableRect +
                ", rect=" + rect +
                ", apptype='" + apptype + '\'' +
                '}';
    }

    /**
     * 计算元素节点的真实区域
     *
     * @param node
     * @param screenWidth
     * @param screenHeight
     */
    public void caculateRects(AccessibilityNodeInfo node, int screenWidth, int screenHeight) {
        if (node == null) {
            return;
        }
        this.nodeHash = node.hashCode();
        // 获取node的真实矩形区域
        Rect nodeRect = new Rect();
        node.getBoundsInScreen(nodeRect);
//        Log.i(Utils.tag, "node rect = " + nodeRect);
        this.setRect(nodeRect);

        // 计算可见的矩形区域
        Rect nodeRectClone = new Rect(nodeRect.left, nodeRect.top, nodeRect.right, nodeRect.bottom);
        Rect displayRect = new Rect();
        displayRect.top = 0;
        displayRect.left = 0;
        displayRect.right = screenWidth;
        displayRect.bottom = screenHeight;
        nodeRectClone.intersect(displayRect);
        this.setVisiableRect(nodeRectClone);
    }
}
