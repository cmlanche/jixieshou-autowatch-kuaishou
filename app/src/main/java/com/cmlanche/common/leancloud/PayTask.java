package com.cmlanche.common.leancloud;

import android.os.AsyncTask;

import com.cmlanche.activity.TaskTypeListActivity;
import com.cmlanche.common.DeviceUtils;
import com.cmlanche.core.utils.Logger;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

import static com.cmlanche.common.leancloud.AVUtils.tb_pay;

/**
 * 支付任务
 */
public class PayTask extends AsyncTask<Boolean, Integer, Boolean> {

    private TaskTypeListActivity activity;

    public PayTask(TaskTypeListActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        try {
            String serial = DeviceUtils.getDeviceSN();
            AVQuery<AVObject> query = new AVQuery<>(tb_pay);
            query.whereEqualTo("serial", serial);
            AVObject obj = query.getFirst();
            if (obj != null) {
                obj.put("payed", true);
                obj.save();
                return true;
            }
            return false;
        } catch (Exception e) {
            Logger.e("支付异常：" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        activity.feedback(result);
    }
}
