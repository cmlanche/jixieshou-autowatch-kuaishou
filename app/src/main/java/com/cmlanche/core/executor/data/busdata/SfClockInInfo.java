package com.cmlanche.core.executor.data.busdata;

import android.graphics.Point;

import com.alibaba.fastjson.JSONObject;
import com.cmlanche.core.executor.data.ScrollRule;
import com.cmlanche.core.executor.data.abs.ExplainJsonData;
import com.cmlanche.core.search.node.NodeInfo;

/**
 * @author ：cmlanche
 * @date ：Created in 2019-09-11 18:07
 */
public class SfClockInInfo implements ExplainJsonData<SfClockInInfo> {

    /**
     * 间隔时间，单位毫秒
     */
    private int interval = 1000;

    /**
     * 滚动方向
     */
    private int times = 3;

    /**
     * 滚动方向
     */
    private ScrollRule scrollRule = ScrollRule.LEFT;

    /**
     * 起始点
     */
    private Point start;

    private NodeInfo nodeInfo;

    /**
     * 结束点
     */
    private Point end;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public ScrollRule getScrollRule() {
        return scrollRule;
    }

    public void setScrollRule(ScrollRule scrollRule) {
        this.scrollRule = scrollRule;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    @Override
    public JSONObject toJsonData() {
        JSONObject obj = new JSONObject();
        obj.put("interval", getInterval());
        obj.put("times", getTimes());
        obj.put("scrollRule", getScrollRule());
        return obj;
    }

    @Override
    public SfClockInInfo expainData(JSONObject json) {
        setInterval(json.getInteger("interval"));
        setTimes(json.getInteger("times"));
        setScrollRule(json.getObject("scrollRule", ScrollRule.class));
        return this;
    }
}
