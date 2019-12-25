package com.cmlanche.core.executor.data;

/**
 *
 */
public enum ScrollRule {
    VERTICAL("vertical"),//down
    HORIZONAL("horizontal"),//right
    NONE("none"),
    LEFT("left"),
    UP("up"),
    RIGHT("right"),//兼容之前的HORIZONAL向右滑动
    DOWN("down");//兼容之前的vertical向下滑动

    private final String rule;


    ScrollRule(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }

    public static ScrollRule getRule(String rule) {
        for (ScrollRule o : ScrollRule.values()) {
            if (o.getRule().equals(rule)) {
                return o;
            }
        }
        if ("true".equals(rule)) {
            return VERTICAL;//兼容之前的true,false模式
        }
        return NONE;
    }

    /**
     * 获取反方向的Rule
     *
     * @return
     */
    public ScrollRule getInverseRule() {
        switch (this) {
            case VERTICAL:
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case HORIZONAL:
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            default:
                return this;
        }
    }

    /**
     * 是否是水平方向
     *
     * @return
     */
    public boolean isHorizonal() {
        return this == HORIZONAL || this == LEFT || this == RIGHT;
    }
}
