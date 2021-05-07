package com.cmlanche.core.executor.data;

import com.alibaba.fastjson.JSONObject;
import com.cmlanche.core.executor.data.abs.StepData;
import com.cmlanche.core.executor.data.busdata.SfClockInInfo;
import com.cmlanche.core.executor.data.busdata.SwipScreenInfo;

/**
 * @author ：cmlanche
 * @date ：Created in 2019-09-11 18:07
 */
public class SFScreenData extends StepData<SfClockInInfo> {

    public SFScreenData() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        setStepName(StepNames.SF_COLOCK_IN);
        setTitle("丰声打卡");
        setDescription("指定时间去打卡");
    }

    @Override
    protected SfClockInInfo explainBusData(JSONObject json) {
        return new SfClockInInfo().expainData(json);
    }
}
