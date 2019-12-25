package com.cmlanche.core.executor.data.busdata.condition;

import com.alibaba.fastjson.JSONObject;
import com.cmlanche.core.executor.data.abs.ExplainJsonData;

/**
 * 字符串条件
 *
 * @author ：cmlanche
 * @date ：Created in 2019-09-04 16:59
 */
public class StringCondition extends Condition<String>
        implements ExplainJsonData<StringCondition> {

    @Override
    public StringCondition expainData(JSONObject json) {
        if (json == null) {
            return null;
        }
        setValue(json.getString("value"));
        setRule(json.getString("rule"));
        return this;
    }

    @Override
    public JSONObject toJsonData() {
        JSONObject obj = new JSONObject();
        obj.put("value", getValue());
        obj.put("rule", getRule());
        return obj;
    }

    public static class Builder {

        private StringCondition condition;

        public Builder() {
            this.condition = new StringCondition();
        }

        public Builder setValue(String v) {
            this.condition.setValue(v);
            return this;
        }

        public Builder setRule(String rule) {
            this.condition.setRule(rule);
            return this;
        }

        public StringCondition get() {
            return condition;
        }
    }
}
