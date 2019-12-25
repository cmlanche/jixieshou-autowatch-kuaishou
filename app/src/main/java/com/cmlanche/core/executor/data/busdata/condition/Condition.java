package com.cmlanche.core.executor.data.busdata.condition;

/**
 * 一个条件
 *
 * @author ：cmlanche
 * @date ：Created in 2019-08-16 15:29
 */
public class Condition<T> {

    /**
     * 条件值
     */
    private T value;

    /**
     * 条件策略
     */
    private String rule;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
