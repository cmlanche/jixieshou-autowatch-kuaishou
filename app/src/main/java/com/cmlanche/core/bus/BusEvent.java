package com.cmlanche.core.bus;

public class BusEvent<T> {

    private int type;

    private T data;

    public BusEvent(int type) {
        this(type, null);
    }

    public BusEvent(int type, T data) {
        this.type = type;
        this.data = data;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
