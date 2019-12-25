package com.cmlanche.core.executor;

import com.cmlanche.core.executor.data.SwipScreenData;
import com.cmlanche.core.executor.data.busdata.ControlInfo;
import com.cmlanche.core.utils.ActionUtils;

public class SwipExecutor extends StepExecutor<SwipScreenData> {

    public SwipExecutor(SwipScreenData data) {
        super(data);
    }

    @Override
    public boolean execute() {
        int fromX = getData().getBusData().getStart().x;
        int fromY = getData().getBusData().getStart().y;
        int toX = getData().getBusData().getEnd().x;
        int toY = getData().getBusData().getEnd().y;
        return ActionUtils.swipe(fromX, fromY, toX, toY, 30);
    }

}
