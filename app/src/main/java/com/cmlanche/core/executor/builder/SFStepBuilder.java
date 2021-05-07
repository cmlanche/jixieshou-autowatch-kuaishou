package com.cmlanche.core.executor.builder;

import android.graphics.Point;

import com.cmlanche.core.executor.SFExecutor;
import com.cmlanche.core.executor.data.SFScreenData;
import com.cmlanche.core.executor.data.busdata.SfClockInInfo;
import com.cmlanche.core.search.node.NodeInfo;

public class SFStepBuilder extends StepBuilder<SFExecutor> {

    public SFStepBuilder() {
        super();
    }

    @Override
    protected SFExecutor init() {
        SFScreenData data = new SFScreenData();
        data.setBusData(new SfClockInInfo());
        SFExecutor executor = new SFExecutor(data);
        return executor;
    }

    @Override
    public SFStepBuilder setTimeout(int timeout) {
        super.setTimeout(timeout);
        return this;
    }

    public SFStepBuilder setPoints(Point start, Point end) {
        get().getData().getBusData().setStart(start);
        get().getData().getBusData().setEnd(end);
        return this;
    }

    public SFStepBuilder addStep(Point start) {
        get().getData().getBusData().setStart(start);
        return this;
    }

    public SFStepBuilder addStep(NodeInfo nodeInfo) {
        get().getData().getBusData().setStart(new Point(nodeInfo.getRect().centerX(), nodeInfo.getRect().centerY()));
        return this;
    }
}
