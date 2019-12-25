package com.cmlanche.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cmlanche.adapter.AppListAdapter;
import com.cmlanche.application.MyApplication;
import com.cmlanche.common.leancloud.CheckPayTask;
import com.cmlanche.common.leancloud.GetTaskListTask;
import com.cmlanche.jixieshou.R;
import com.cmlanche.model.AppInfo;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 任务类型列表
 */
public class TaskTypeListActivity extends AppCompatActivity {

    private ListView listView;
    private List<AppInfo> appInfos = new ArrayList<>();
    private AppListAdapter appListAdapter;
    private MaterialButton vipBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_type_list);
        findViewById(R.id.backImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        appListAdapter = new AppListAdapter(this, appInfos);
        listView = findViewById(R.id.typeListView);
        listView.setAdapter(appListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppInfo info = appListAdapter.getItem(i);
                if (info.isFree()) {
                    choose(info);
                } else {
                    if (MyApplication.getAppInstance().isVip()) {
                        choose(info);
                    } else {
                        Toast.makeText(TaskTypeListActivity.this, "您还不是VIP会员，无法使用此任务", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        vipBtn = findViewById(R.id.openVIPBtn);
        vipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getAppInstance().isVip()) {
                    Toast.makeText(TaskTypeListActivity.this, "老板，您已开通VIP会员", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TaskTypeListActivity.this, "本软件不售卖！", Toast.LENGTH_LONG).show();
                }
            }
        });

        new GetTaskListTask(this).execute();
        new CheckPayTask(this).execute();
    }

    public void updateList(List<AppInfo> list) {
        appInfos.clear();
        appInfos.addAll(list);
        appListAdapter.notifyDataSetChanged();
    }

    private void choose(AppInfo info) {
        Intent data = new Intent();
        data.putExtra("appInfo", JSON.toJSONString(info));
        setResult(1, data);
        finish();
    }

    public void feedback(boolean paySuccess) {
        if (paySuccess) {
            Toast.makeText(this, "会员开通成功", Toast.LENGTH_LONG).show();
        } else {
            // todo: 微信支付发起退款
            Toast.makeText(this, "会员开通失败，支付将自动退款", Toast.LENGTH_LONG).show();
        }
        updateVIPBtn(paySuccess);
    }

    public void updateVIPBtn(boolean vip) {
        if (vip) {
            vipBtn.setText("老板，您已开通VIP会员！");
        } else {
            vipBtn.setText("开通VIP");
        }
    }

}
