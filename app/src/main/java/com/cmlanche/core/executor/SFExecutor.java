package com.cmlanche.core.executor;

import com.cmlanche.core.executor.data.SFScreenData;
import com.cmlanche.core.utils.ActionUtils;

public class SFExecutor extends StepExecutor<SFScreenData> {

    public SFExecutor(SFScreenData data) {
        super(data);
    }

    @Override
    public boolean execute() {
        int x = getData().getBusData().getStart().x;
        int y = getData().getBusData().getStart().y;
        return ActionUtils.click(x, y);
    }

}
