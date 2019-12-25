package com.cmlanche.core.utils;

/**
 * Created by cmlanche on 2018/9/13.
 * 节点文本
 */
public class NodeText {
    /**
     * 节点文本
     */
    private String text;

    /**
     * 文本来源是否是contentDescription
     */
    private boolean isFromContentDescription;

    public NodeText(String text, boolean isFromContentDescription) {
        this.text = text;
        this.isFromContentDescription = isFromContentDescription;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFromContentDescription() {
        return isFromContentDescription;
    }

    public void setFromContentDescription(boolean fromContentDescription) {
        isFromContentDescription = fromContentDescription;
    }
}
