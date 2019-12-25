package com.cmlanche.common.leancloud;

import cn.leancloud.AVObject;

public interface AVListener {

    void success(AVObject obj);

    void fail(String error);
}
