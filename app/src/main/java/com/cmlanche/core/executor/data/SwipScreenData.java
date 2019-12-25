package com.cmlanche.core.executor.data;

import com.alibaba.fastjson.JSONObject;
import com.cmlanche.core.executor.data.abs.StepData;
import com.cmlanche.core.executor.data.busdata.SwipScreenInfo;

/**
 * @author ：cmlanche
 * @date ：Created in 2019-09-11 18:07
 */
public class SwipScreenData extends StepData<SwipScreenInfo> {

    public SwipScreenData() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        setStepName(StepNames.SWIP_SCREEN);
        setTitle("滑动屏幕");
        setDescription("往指定方向，每间隔一定时间滑动几次");
    }

    @Override
    protected SwipScreenInfo explainBusData(JSONObject json) {
        return new SwipScreenInfo().expainData(json);
    }
}
