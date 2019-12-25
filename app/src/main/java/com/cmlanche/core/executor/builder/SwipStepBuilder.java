package com.cmlanche.core.executor.builder;

import android.graphics.Point;

import com.cmlanche.core.executor.SwipExecutor;
import com.cmlanche.core.executor.data.SwipScreenData;
import com.cmlanche.core.executor.data.busdata.SwipScreenInfo;

public class SwipStepBuilder extends StepBuilder<SwipExecutor> {

    public SwipStepBuilder() {
        super();
    }

    @Override
    protected SwipExecutor init() {
        SwipScreenData data = new SwipScreenData();
        data.setBusData(new SwipScreenInfo());
        SwipExecutor executor = new SwipExecutor(data);
        return executor;
    }

    @Override
    public SwipStepBuilder setTimeout(int timeout) {
        super.setTimeout(timeout);
        return this;
    }

    public SwipStepBuilder setPoints(Point start, Point end) {
        get().getData().getBusData().setStart(start);
        get().getData().getBusData().setEnd(end);
        return this;
    }
}
