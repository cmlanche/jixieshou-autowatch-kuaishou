package com.cmlanche.core.executor.data.abs;

import com.alibaba.fastjson.JSONObject;

/**
 * json数据解释器
 */
public interface ExplainJsonData<T> {

    /**
     * 将对象转化为json字符串
     */
    JSONObject toJsonData();

    /**
     * 将json数据解释为一个实体对象
     *
     * @param json
     * @return
     */
    T expainData(JSONObject json);
}
