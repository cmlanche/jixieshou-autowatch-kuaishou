package com.cmlanche.core.executor.data.busdata.condition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmlanche.core.executor.data.abs.ExplainJsonData;

import java.util.List;

/**
 * @author ：cmlanche
 * @date ：Created in 2019-09-04 17:01
 */
public class ListStringCondition extends Condition<List<String>>
        implements ExplainJsonData<ListStringCondition> {

    @Override
    public ListStringCondition expainData(JSONObject json) {
        if (json == null) {
            return null;
        }
        setRule(json.getString("rule"));
        setValue(json.getJSONArray("value").toJavaList(String.class));
        return this;
    }

    @Override
    public JSONObject toJsonData() {
        JSONObject obj = new JSONObject();
        obj.put("value", JSON.parseArray(JSON.toJSONString(getValue())));
        obj.put("rule", getRule());
        return obj;
    }

    public static class Builder {

        private ListStringCondition condition;

        public Builder() {
            this.condition = new ListStringCondition();
        }

        public Builder setValue(List<String> value) {
            this.condition.setValue(value);
            return this;
        }

        public Builder setRule(String rule) {
            this.condition.setRule(rule);
            return this;
        }

        public ListStringCondition get() {
            return condition;
        }
    }
}
