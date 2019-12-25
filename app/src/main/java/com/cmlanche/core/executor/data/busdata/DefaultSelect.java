package com.cmlanche.core.executor.data.busdata;

/**
 * 找到控件时，默认选择第几个的策略
 *
 * @author ：cmlanche
 * @date ：Created in 2019-11-06 17:57
 */
public enum DefaultSelect {

    /**
     * 不选择，认为找不到，这是默认值
     */
    none,

    /**
     * 第一个
     */
    first,

    /**
     * 第二个
     */
    sencond,

    /**
     * 第三个
     */
    third,
    fourth,
    fifth,
    sixth,
    seventh,
    eighth,
    nighth,
    tenth,

    /**
     * 最后一个
     */
    last,

    /**
     * 倒数第二个
     */
    last_2,

    /**
     * 倒数第三个
     */
    last_3,

    /**
     * 中间的
     */
    half,

    /**
     * 随机选择
     */
    random,
}
