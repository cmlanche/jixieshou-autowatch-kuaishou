package com.cmlanche.core.executor.data.busdata;

import com.alibaba.fastjson.JSONObject;
import com.cmlanche.core.executor.data.ScrollRule;
import com.cmlanche.core.executor.data.TechType;
import com.cmlanche.core.executor.data.abs.ExplainJsonData;
import com.cmlanche.core.executor.data.busdata.condition.ListStringCondition;
import com.cmlanche.core.executor.data.busdata.condition.StringCondition;

import java.util.List;

/**
 * 控件信息
 */
public class ControlInfo implements ExplainJsonData<ControlInfo> {

    /**
     * 控件包名
     */
    private StringCondition pkgName;

    /**
     * 控件文本
     */
    private StringCondition text;

    /**
     * id
     */
    private StringCondition id;

    /**
     * class
     */
    private StringCondition clazz;

    /**
     * 多种xpath
     */
    private ListStringCondition xpath;

    /**
     * 默认选择第几个
     */
    private DefaultSelect defaultSelect = DefaultSelect.none;

    /**
     * 往某个方向的滚动次数
     */
    private int scrollTimes = 5;

    /**
     * 当前步骤采用什么技术
     */
    private TechType techType = TechType.UIA_Native;

    /**
     * 滚动控件到中间
     */
    private boolean scrollToCenter = false;

    /**
     * 滚动策略
     */
    private ScrollRule scrollRule = ScrollRule.NONE;

    /**
     * 是否是快速查找，如果是false，则表示稳定查找，会查找多次验证位置是否一致
     * 如果每次查找的结果很可能不一样，那么此值需要设置为true，
     * 如defaultSelect为random的话，此值会默认设置为true
     */
    private boolean fast = false;

    public StringCondition getPkgName() {
        return pkgName;
    }

    public void setPkgName(StringCondition pkgName) {
        this.pkgName = pkgName;
    }

    public StringCondition getText() {
        return text;
    }

    public void setText(StringCondition text) {
        this.text = text;
    }

    public StringCondition getId() {
        return id;
    }

    public void setId(StringCondition id) {
        this.id = id;
    }

    public StringCondition getClazz() {
        return clazz;
    }

    public void setClazz(StringCondition clazz) {
        this.clazz = clazz;
    }

    public ListStringCondition getXpath() {
        return xpath;
    }

    public void setXpath(ListStringCondition xpath) {
        this.xpath = xpath;
    }

    public DefaultSelect getDefaultSelect() {
        return defaultSelect;
    }

    public void setDefaultSelect(DefaultSelect defaultSelect) {
        this.defaultSelect = defaultSelect;
    }

    public int getScrollTimes() {
        return scrollTimes;
    }

    public void setScrollTimes(int scrollTimes) {
        this.scrollTimes = scrollTimes;
    }

    public TechType getTechType() {
        return techType;
    }

    public void setTechType(TechType techType) {
        this.techType = techType;
    }

    public boolean isScrollToCenter() {
        return scrollToCenter;
    }

    public void setScrollToCenter(boolean scrollToCenter) {
        this.scrollToCenter = scrollToCenter;
    }

    public ScrollRule getScrollRule() {
        return scrollRule;
    }

    public void setScrollRule(ScrollRule scrollRule) {
        this.scrollRule = scrollRule;
    }

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    /**
     * 获取控件名称
     *
     * @return
     */
    public String getControlName() {
        if (getText() != null && !"ignore".equals(getText().getRule())) {
            return getText().getValue();
        }
        if (getId() != null && !"ignore".equals(getId().getRule())) {
            return getId().getValue();
        }
        if (getClazz() != null && !"ignore".equals(getClazz().getRule())) {
            return getClazz().getValue();
        }
        return toJsonData().toJSONString();
    }

    @Override
    public ControlInfo expainData(JSONObject json) {
        if (json == null) {
            return null;
        }
        setText(new StringCondition().expainData(json.getJSONObject("text")));
        setId(new StringCondition().expainData(json.getJSONObject("id")));
        setClazz(new StringCondition().expainData(json.getJSONObject("clazz")));
        setPkgName(new StringCondition().expainData(json.getJSONObject("pkgName")));
        setXpath(new ListStringCondition().expainData(json.getJSONObject("xpath")));
        setDefaultSelect(json.getObject("defaultSelect", DefaultSelect.class));
        setScrollTimes(json.getInteger("scrollTimes"));
        setTechType(json.getObject("techType", TechType.class));
        setScrollToCenter(json.getBoolean("scrollToCenter"));
        setScrollRule(json.getObject("scrollRule", ScrollRule.class));
        setFast(json.getBoolean("fast"));
        return this;
    }

    @Override
    public JSONObject toJsonData() {
        JSONObject obj = new JSONObject();
        if (getText() != null) {
            obj.put("text", getText().toJsonData());
        }
        if (getId() != null) {
            obj.put("id", getId().toJsonData());
        }
        if (getClazz() != null) {
            obj.put("clazz", getClazz().toJsonData());
        }
        if (getPkgName() != null) {
            obj.put("pkgName", getPkgName().toJsonData());
        }
        if (getXpath() != null) {
            obj.put("xpath", getXpath().toJsonData());
        }
        obj.put("defaultSelect", getDefaultSelect());
        obj.put("scrollTimes", getScrollTimes());
        obj.put("techType", getTechType());
        obj.put("scrollToCenter", isScrollToCenter());
        obj.put("scrollRule", getScrollRule());
        obj.put("fast", isFast());
        return obj;
    }

    /**
     * 控件信息构建器
     */
    public static class Builder {

        private ControlInfo info;

        public Builder() {
            this.info = new ControlInfo();
        }

        public Builder setText(String text, String rule) {
            this.info.setText(getStringCondition(text, rule));
            return this;
        }

        public Builder setId(String id, String rule) {
            this.info.setId(getStringCondition(id, rule));
            return this;
        }

        public Builder setClazz(String clazz, String rule) {
            this.info.setClazz(getStringCondition(clazz, rule));
            return this;
        }

        public Builder setXpathes(List<String> xpathes, String rule) {
            this.info.setXpath(new ListStringCondition.Builder().setValue(xpathes).setRule(rule).get());
            return this;
        }

        public Builder setPkgName(String pkgName, String rule) {
            this.info.setPkgName(getStringCondition(pkgName, rule));
            return this;
        }

        public Builder setScrollTimes(int scrollTimes) {
            this.info.setScrollTimes(scrollTimes);
            return this;
        }

        public Builder setTechType(TechType techType) {
            this.info.setTechType(techType);
            return this;
        }

        public Builder setScrollToCenter(boolean scrollToCenter) {
            this.info.setScrollToCenter(scrollToCenter);
            return this;
        }

        public Builder setScrollRule(ScrollRule scrollRule) {
            this.info.setScrollRule(scrollRule);
            return this;
        }

        public Builder setDefaultSelect(DefaultSelect select) {
            this.info.setDefaultSelect(select);
            switch (select) {
                case half:
                case random:
                    this.info.setFast(true);
                    break;
            }
            return this;
        }

        public Builder setFast(boolean fast) {
            this.info.setFast(fast);
            return this;
        }

        private StringCondition getStringCondition(String value, String rule) {
            return new StringCondition.Builder().setValue(value).setRule(rule).get();
        }

        public ControlInfo get() {
            return this.info;
        }
    }
}
