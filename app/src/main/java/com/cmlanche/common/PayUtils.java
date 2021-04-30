package com.cmlanche.common;

import com.riversoft.weixin.pay.base.PaySetting;
import com.riversoft.weixin.pay.redpack.RedPacks;
import com.riversoft.weixin.pay.redpack.bean.RedPackRequest;
import com.riversoft.weixin.pay.redpack.bean.RedPackResponse;

public class PayUtils {

    public static void test() {
        PaySetting paySetting = new PaySetting();

        RedPackRequest redPackRequest = new RedPackRequest();
        redPackRequest.setActivityName("捡豆子Pro服务");
        redPackRequest.setAmount(100);
        redPackRequest.setBillNumber("1292063901201605150012300014");
        redPackRequest.setNumber(1);
        redPackRequest.setOpenId("oELhlt7Q-lRmLbRsPsaKeVX6pqjg");
        redPackRequest.setRemark("测试发红包");
        redPackRequest.setWishing("恭喜发财");
        redPackRequest.setClientIp("127.0.0.1");
        redPackRequest.setSendName("捡豆子app");


        RedPackResponse redPackResponse = RedPacks.with(paySetting).sendSingle(redPackRequest);
    }
}
