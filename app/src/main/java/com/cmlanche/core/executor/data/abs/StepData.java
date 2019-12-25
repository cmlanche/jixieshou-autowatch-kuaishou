package com.cmlanche.core.executor.data.abs;

import com.alibaba.fastjson.JSONObject;

/**
 * 一个步骤数据
 *
 * @author ：cmlanche
 * @date ：Created in 2019-08-14 19:44
 */
public abstract class StepData<T extends ExplainJsonData<T>>
        implements ExplainJsonData<StepData<T>>, Cloneable {

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 业务数据
     */
    private T busData;

    /**
     * 用例标题
     */
    private String title;

    /**
     * 用例描述
     */
    private String description;

    /**
     * 是否禁用
     */
    private boolean isDisable;

    /**
     * 预期截图（录制过程中自动生成）
     */
    private String expectedScreenshot;

    /**
     * 预期截图（上传到某服务器并返回的url）
     */
    private String expectedScreenshotUrl;

    /**
     * 超时时间，单位毫秒
     */
    private int timeout;

    /**
     * 是否只执行一次
     */
    private boolean isRunOnce;

    /**
     * 备注
     */
    private String note;

    /**
     * 唯一标记值
     */
    private String identify;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public T getBusData() {
        return busData;
    }

    public void setBusData(T busData) {
        this.busData = busData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpectedScreenshot() {
        return expectedScreenshot;
    }

    public void setExpectedScreenshot(String expectedScreenshot) {
        this.expectedScreenshot = expectedScreenshot;
    }

    public String getExpectedScreenshotUrl() {
        return expectedScreenshotUrl;
    }

    public void setExpectedScreenshotUrl(String expectedScreenshotUrl) {
        this.expectedScreenshotUrl = expectedScreenshotUrl;
    }

    public boolean isRunOnce() {
        return isRunOnce;
    }

    public void setRunOnce(boolean runOnce) {
        isRunOnce = runOnce;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public StepData() {
        this.init();
    }

    @Override
    public StepData clone() {
        try {
            return (StepData) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * 初始化
     */
    protected void init() {
        setDisable(false);
        setTimeout(15000);
    }

    @Override
    public JSONObject toJsonData() {
        JSONObject obj = new JSONObject();
        obj.put("stepName", getStepName());
        obj.put("title", getTitle());
        obj.put("description", getDescription());
        obj.put("expectedScreenshot", getExpectedScreenshot());
        obj.put("timeout", getTimeout());
        obj.put("isDisable", isDisable());
        obj.put("busData", getBusData().toJsonData());
        obj.put("isRunOnce", isRunOnce());
        obj.put("note", getNote());
        obj.put("identify", getIdentify());
        return obj;
    }

    @Override
    public StepData<T> expainData(JSONObject json) {
        setStepName(json.getString("stepName"));
        setTitle(json.getString("title"));
        setDescription(json.getString("description"));
        setExpectedScreenshot(json.getString("expectedScreenshot"));
        setTimeout(json.getInteger("timeout"));
        setDisable(json.getBoolean("isDisable"));
        setBusData(explainBusData(json.getJSONObject("busData")));
        setRunOnce(json.getBoolean("isRunOnce"));
        setNote(json.getString("note"));
        setIdentify(json.getString("identify"));
        return this;
    }

    protected abstract T explainBusData(JSONObject json);
}
