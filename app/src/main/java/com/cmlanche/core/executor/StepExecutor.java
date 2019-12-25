package com.cmlanche.core.executor;


import com.cmlanche.core.executor.data.abs.StepData;

/**
 * @author ：cmlanche
 * @date ：Created in 2019-10-12 15:32
 */
public abstract class StepExecutor<T extends StepData> {

    private T data;

    public StepExecutor(T data) {
        this.data = data;
    }

    public abstract boolean execute();

    public T getData() {
        return data;
    }
}
