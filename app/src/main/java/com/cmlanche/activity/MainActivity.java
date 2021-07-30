package com.cmlanche.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.cmlanche.adapter.TaskListAdapter;
import com.cmlanche.application.MyApplication;
import com.cmlanche.common.SPService;
import com.cmlanche.core.service.MyAccessbilityService;
import com.cmlanche.core.utils.AccessibilityUtils;
import com.cmlanche.core.utils.BaseUtil;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.core.utils.SFUpdaterUtils;
import com.cmlanche.floatwindow.PermissionUtil;
import com.cmlanche.jixieshou.R;
import com.cmlanche.model.AppInfo;
import com.cmlanche.model.TaskInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import static com.cmlanche.core.utils.Constant.PN_DIAN_TAO;
import static com.cmlanche.core.utils.Constant.PN_DOU_YIN;
import static com.cmlanche.core.utils.Constant.PN_FENG_SHENG;
import static com.cmlanche.core.utils.Constant.PN_KUAI_SHOU;
import static com.cmlanche.core.utils.Constant.PN_TOU_TIAO;
import static com.cmlanche.core.utils.Constant.PN_YING_KE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CardView cardView;
    private EditText edit_baby;
    private ListView taskListView;
    private FloatingActionButton fab;
    private TaskListAdapter taskListAdapter;
    private MaterialButton startBtn;
    private MaterialButton btnShare;
    private TextView descriptionView;
    private List<AppInfo> appInfos = new ArrayList<>();
    private boolean isInstallKuaiShou = false;
    private boolean isInstallFengSheng = false;
    private boolean isInstallDouyin = false;
    private boolean isInstallTouTiao = false;
    private boolean isInstallDianTao = false;
    private boolean isInstallYingKe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getAppInstance().setMainActivity(this);

        SFUpdaterUtils.checkVersion(this);

        edit_baby = findViewById(R.id.edit_baby);
        cardView = findViewById(R.id.newTaskCardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddNewTaskActivity();
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddNewTaskActivity();
            }
        });
        taskListView = findViewById(R.id.taskListView);
        taskListView.setAdapter(taskListAdapter = new TaskListAdapter(this, appInfos));
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoEditTaskActivity(taskListAdapter.getItem(i));
            }
        });
        descriptionView = findViewById(R.id.description);

        btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareAppDilaog();
            }
        });

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appInfos.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请选择一个任务", Toast.LENGTH_LONG).show();
                    return;
                }

                for (AppInfo appInfo : appInfos) {
                    if (appInfo.getPkgName().equals(Constant.PN_KUAI_SHOU)) {
                        if (!isInstallKuaiShou) {
                            BaseUtil.showDownLoadDialog(PN_KUAI_SHOU, MainActivity.this);
                            return;
                        }

                    }
                    if (appInfo.getPkgName().equals(Constant.PN_YING_KE)) {
                        if (!isInstallYingKe) {
                            BaseUtil.showDownLoadDialog(PN_YING_KE, MainActivity.this);
                            return;
                        }

                    }
                    if (appInfo.getPkgName().equals(Constant.PN_FENG_SHENG)) {
                        if (!isInstallFengSheng) {
                            BaseUtil.showDownLoadDialog(PN_FENG_SHENG, MainActivity.this);
                            return;
                        }

                    } else if (appInfo.getPkgName().equals(Constant.PN_DOU_YIN)) {
                        if (!isInstallDouyin) {
                            BaseUtil.showDownLoadDialog(PN_DOU_YIN, MainActivity.this);
                            return;
                        }

                    } else if (appInfo.getPkgName().equals(Constant.PN_TOU_TIAO)) {
                        if (!isInstallTouTiao) {
                            BaseUtil.showDownLoadDialog(PN_TOU_TIAO, MainActivity.this);
                            return;
                        }

                    } else if (appInfo.getPkgName().equals(Constant.PN_DIAN_TAO)) {
                        if (!isInstallDianTao) {
                            BaseUtil.showDownLoadDialog(PN_DIAN_TAO, MainActivity.this);
                            return;
                        }

                    }
                }


                if (!PermissionUtil.checkFloatPermission(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "没有悬浮框权限，为了保证任务能够持续，请授权", Toast.LENGTH_LONG).show();
                    try {
                        PermissionUtil.requestOverlayPermission(MainActivity.this);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                // 判断是否开启辅助服务
                if (!AccessibilityUtils.isAccessibilitySettingsOn(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "请打开「捡豆子」的辅助服务", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    return;
                }

                MyApplication.getAppInstance().setBaby(edit_baby.getEditableText().toString());
                startService(new Intent(getApplicationContext(), MyAccessbilityService.class));
                MyApplication.getAppInstance().startTask(appInfos);
            }
        });

        TextView textView = findViewById(R.id.deviceNo);
        textView.setText("设备号：" + EncryptUtils.encryptMD5ToString(DeviceUtils.getMacAddress()));

        TaskInfo hisTaskInfo = SPService.get(SPService.SP_TASK_LIST, TaskInfo.class);
        if (hisTaskInfo == null || hisTaskInfo.getAppInfos() == null || hisTaskInfo.getAppInfos().isEmpty()) {
            List<AppInfo> appInfos = new ArrayList<>();

            AppInfo appInfo = new AppInfo();
            appInfo.setAppName("抖音极速版");
            appInfo.setName("抖音极速版-刷广告");
            appInfo.setFree(true);
            appInfo.setPeriod(4l);
            appInfo.setPkgName(Constant.PN_DOU_YIN);
            appInfos.add(appInfo);

            appInfo = new AppInfo();
            appInfo.setAppName("今日头条极速版");
            appInfo.setName("今日头条极速版-刷广告");
            appInfo.setFree(true);
            appInfo.setPeriod(4l);
            appInfo.setPkgName(Constant.PN_TOU_TIAO);
            appInfos.add(appInfo);

            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setAppInfos(appInfos);
            SPService.put(SPService.SP_TASK_LIST, taskInfo);
        }
        this.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInstallFengSheng = BaseUtil.isInstallPackage(PN_FENG_SHENG);
        isInstallKuaiShou = BaseUtil.isInstallPackage(PN_KUAI_SHOU);
        isInstallDouyin = BaseUtil.isInstallPackage(PN_DOU_YIN);
        isInstallTouTiao = BaseUtil.isInstallPackage(PN_TOU_TIAO);
        isInstallDianTao = BaseUtil.isInstallPackage(PN_DIAN_TAO);
        isInstallYingKe = BaseUtil.isInstallPackage(PN_YING_KE);
    }

    private void initData() {
        TaskInfo taskInfo = SPService.get(SPService.SP_TASK_LIST, TaskInfo.class);
        if (taskInfo == null || taskInfo.getAppInfos() == null || taskInfo.getAppInfos().isEmpty()) {
            cardView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            appInfos.addAll(taskInfo.getAppInfos());
            taskListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1) {
            // 100是新增任务
            AppInfo appInfo = JSON.parseObject(data.getStringExtra("appInfo"), AppInfo.class);
            cardView.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            appInfos.add(appInfo);
            taskListAdapter.notifyDataSetChanged();
            saveTaskList();
        }

        // 编辑任务成功
        if (requestCode == 101) {
            // 101是更新
            if (resultCode == 1) {
                AppInfo appInfo = JSON.parseObject(data.getStringExtra("appInfo"), AppInfo.class);
                // 1是删除
                deleteAppInfo(appInfo);
                if (appInfos.isEmpty()) {
                    cardView.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                }
                saveTaskList();
            } else if (resultCode == 2) {
                AppInfo appInfo = JSON.parseObject(data.getStringExtra("appInfo"), AppInfo.class);
                // 2是编辑
                AppInfo editedAppInfo = JSON.parseObject(data.getStringExtra("editedAppInfo"), AppInfo.class);
                updateAppInfo(editedAppInfo.getUuid(), appInfo);
                saveTaskList();
            }
        }
    }

    private void gotoAddNewTaskActivity() {
        startActivityForResult(new Intent(this, NewOrEditTaskActivity.class), 100);
    }

    private void gotoEditTaskActivity(AppInfo appInfo) {
        Intent i = new Intent(this, NewOrEditTaskActivity.class);
        i.putExtra("appInfo", JSON.toJSONString(appInfo));
        startActivityForResult(i, 101);
    }

    /**
     * 删除某个任务
     *
     * @param appInfo
     */
    private void deleteAppInfo(AppInfo appInfo) {
        for (int i = 0; i < appInfos.size(); i++) {
            if (appInfo.getUuid().equals(appInfos.get(i).getUuid())) {
                appInfos.remove(i);
                taskListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 删除某个任务
     *
     * @param uuid    替换某任务
     * @param appInfo
     */
    private void updateAppInfo(String uuid, AppInfo appInfo) {
        for (int i = 0; i < appInfos.size(); i++) {
            AppInfo curr = appInfos.get(i);
            if (uuid.equals(curr.getUuid())) {
                curr.setFree(appInfo.isFree());
                curr.setPkgName(appInfo.getPkgName());
                curr.setPeriod(appInfo.getPeriod());
                curr.setIcon(appInfo.getIcon());
                curr.setName(appInfo.getName());
                taskListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 保存任务
     */
    private void saveTaskList() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setAppInfos(appInfos);
        SPService.put(SPService.SP_TASK_LIST, taskInfo);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected boolean isAppExist(String pkgName) {
        ApplicationInfo info;
        try {
            info = getPackageManager().getApplicationInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }
        return info != null;
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("注意");
        builder.setMessage("确定要退出捡豆任务吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);//正常退出App
                Toast.makeText(getApplicationContext(), "退出捡豆子", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showShareAppDilaog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_share_app, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        MaterialButton btnColse = inflate.findViewById(R.id.btn_colse);
        MaterialButton btn_copy = inflate.findViewById(R.id.btn_copy);
        btnColse.setOnClickListener(v -> dialog.dismiss());
        btn_copy.setOnClickListener(v -> {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", "https://marm-core.sf-express.com/app-download/2e69975d7e0348009687c4cbf7bcf954");
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Toast.makeText(this, "下载链接已复制,可粘贴微信QQ进行分享", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
    }

}
