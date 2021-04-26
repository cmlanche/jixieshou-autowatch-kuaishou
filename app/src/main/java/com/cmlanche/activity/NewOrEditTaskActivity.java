package com.cmlanche.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cmlanche.common.Constants;
import com.cmlanche.core.utils.StringUtil;
import com.cmlanche.jixieshou.R;
import com.cmlanche.model.AppInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import cn.leancloud.AVObject;

/**
 * 新建或编辑任务界面
 */
public class NewOrEditTaskActivity extends BaseActivity {

    private TextView taskTypeName;
    private TextInputEditText periodEdit;
    private AppInfo appInfo;
    private AppInfo editedAppInfo;
    private boolean isEdit;
    private MaterialButton deleteBtn;
    private MaterialButton sureBtn;
    private TextView title;

    @Override
    protected void initData(Bundle savedInstanceState) {
        String info = getIntent().getStringExtra("appInfo");
        if (StringUtil.isEmpty(info)) {
            this.isEdit = false;
        } else {
            this.isEdit = true;
            this.editedAppInfo = JSON.parseObject(info, AppInfo.class);
            this.appInfo = editedAppInfo;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_task);
        findViewById(R.id.backImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.typeLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(NewOrEditTaskActivity.this, TaskTypeListActivity.class), 100);
            }
        });

        taskTypeName = findViewById(R.id.name);
        periodEdit = findViewById(R.id.periodEdit);
        sureBtn = findViewById(R.id.sureBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查数据合法性
                if (appInfo == null) {
                    Toast.makeText(getApplicationContext(), "请选择一个任务", Toast.LENGTH_LONG).show();
                    return;
                }
                String period = periodEdit.getText().toString();
                if (StringUtil.isEmpty(period)) {
                    Toast.makeText(getApplicationContext(), "请输入执行时长", Toast.LENGTH_LONG).show();
                    return;
                }
                appInfo.setPeriod(Integer.parseInt(period));

                Intent data = new Intent();
                data.putExtra("appInfo", JSON.toJSONString(appInfo));
                if (isEdit) {
                    data.putExtra("editedAppInfo", JSON.toJSONString(editedAppInfo));
                    setResult(2, data);
                } else {
                    setResult(1, data);
                }
                finish();
            }
        });

        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("appInfo", JSON.toJSONString(appInfo));
                setResult(1, data);
                finish();
            }
        });
        deleteBtn.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        title = findViewById(R.id.title);
        if (isEdit) {
            initAppInfo(appInfo);
            title.setText("编辑任务");
            sureBtn.setText("更新");
        }

//        AVObject testObject = new AVObject("task_list");
//        AppInfo appInfo = new AppInfo();
//        appInfo.setName("快手极速版");
//        appInfo.setFree(true);
//        appInfo.setPeriod(1l);
//        appInfo.setPkgName(Constants.pkg_kuaishou_fast);
//        appInfo.setUuid("1111");
//        testObject.put("name", appInfo.getName());
//        testObject.put("isFree", appInfo.isFree());
//        testObject.put("period", appInfo.getPeriod());
//        testObject.put("pkgName", appInfo.getPkgName());
//        testObject.saveInBackground().blockingSubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1) {
            // 免费任务
            String info = data.getStringExtra("appInfo");
            appInfo = JSON.parseObject(info, AppInfo.class);
            this.initAppInfo(appInfo);
        }
    }

    private void initAppInfo(AppInfo appInfo) {
        this.taskTypeName.setText(appInfo.getName());
        findViewById(R.id.sp).setVisibility(View.VISIBLE);
        findViewById(R.id.periodLayout).setVisibility(View.VISIBLE);
        this.periodEdit.setText(String.valueOf(appInfo.getPeriod()));
        this.periodEdit.setSelection(this.periodEdit.getText().length());
    }
}
