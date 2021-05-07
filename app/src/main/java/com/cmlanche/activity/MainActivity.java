package com.cmlanche.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cmlanche.adapter.TaskListAdapter;
import com.cmlanche.application.MyApplication;
import com.cmlanche.common.DeviceUtils;
import com.cmlanche.common.SPService;
import com.cmlanche.common.leancloud.CheckUpdateTask;
import com.cmlanche.core.service.MyAccessbilityService;
import com.cmlanche.core.utils.AccessibilityUtils;
import com.cmlanche.core.utils.Constant;
import com.cmlanche.core.utils.DianTaoUtil;
import com.cmlanche.core.utils.DouYinUtil;
import com.cmlanche.core.utils.FengShengUtil;
import com.cmlanche.core.utils.KuaiShouUtil;
import com.cmlanche.core.utils.SFUpdaterUtils;
import com.cmlanche.core.utils.TouTiaoUtil;
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
import androidx.core.app.ActivityCompat;

import static com.cmlanche.core.utils.Constant.PN_DOU_YIN;
import static com.cmlanche.core.utils.Constant.PN_KUAI_SHOU;
import static com.cmlanche.core.utils.Constant.PN_FENG_SHENG;
import static com.cmlanche.core.utils.Constant.PN_TOU_TIAO;
import static com.cmlanche.core.utils.Constant.PN_DIAN_TAO;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CardView cardView;
    private ListView taskListView;
    private FloatingActionButton fab;
    private TaskListAdapter taskListAdapter;
    private MaterialButton startBtn;
    private TextView descriptionView;
    private List<AppInfo> appInfos = new ArrayList<>();
    private boolean isInstallKuaiShou = false;
    private boolean isInstallFengSheng = false;
    private boolean isInstallDouyin = false;
    private boolean isInstallTouTiao = false;
    private boolean isInstallDianTao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getAppInstance().setMainActivity(this);

        SFUpdaterUtils.checkVersion(this);

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

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appInfos.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请选择一个任务", Toast.LENGTH_LONG).show();
                    return;
                }

                for(AppInfo appInfo : appInfos){
                    if(appInfo.getPkgName().equals(Constant.PN_KUAI_SHOU)){
                        if(!isInstallKuaiShou){
                            KuaiShouUtil.showDownLoadDialog(MainActivity.this);
                            return;
                        }

                    }
                    if(appInfo.getPkgName().equals(Constant.PN_FENG_SHENG)){
                        if(!isInstallFengSheng){
                            FengShengUtil.showDownLoadDialog(MainActivity.this);
                            return;
                        }

                    }else if(appInfo.getPkgName().equals(Constant.PN_DOU_YIN)){
                        if(!isInstallDouyin){
                            DouYinUtil.showDownLoadDialog(MainActivity.this);
                            return;
                        }

                    }else if(appInfo.getPkgName().equals(Constant.PN_TOU_TIAO)){
                        if(!isInstallTouTiao){
                            TouTiaoUtil.showDownLoadDialog(MainActivity.this);
                            return;
                        }

                    }else if(appInfo.getPkgName().equals(Constant.PN_DIAN_TAO)){
                        if(!isInstallDianTao){
                            DianTaoUtil.showDownLoadDialog(MainActivity.this);
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

                startService(new Intent(getApplicationContext(), MyAccessbilityService.class));
                MyApplication.getAppInstance().startTask(appInfos);
            }
        });

        TextView textView = findViewById(R.id.deviceNo);
        textView.setText("设备号：" + DeviceUtils.getDeviceSN());

        if(appInfos.isEmpty()){
//            AppInfo appInfo = new AppInfo();
//            appInfo.setAppName("快手极速版");
//            appInfo.setName("快手极速版");
//            appInfo.setFree(true);
//            appInfo.setPeriod(4l);
//            appInfo.setPkgName(Constant.PN_KUAI_SHOU);
//            List<AppInfo> appInfos = new ArrayList<>();
//            appInfos.add(appInfo);
//            TaskInfo taskInfo = new TaskInfo();
//            taskInfo.setAppInfos(appInfos);
//            SPService.put(SPService.SP_TASK_LIST, taskInfo);

            AppInfo appInfo = new AppInfo();
            appInfo.setAppName("丰声打卡");
            appInfo.setName("丰声打卡");
            appInfo.setFree(true);
            appInfo.setPeriod(24l);
            appInfo.setPkgName(Constant.PN_FENG_SHENG);
            List<AppInfo> appInfos = new ArrayList<>();
            appInfos.add(appInfo);
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setAppInfos(appInfos);
            SPService.put(SPService.SP_TASK_LIST, taskInfo);

//            AppInfo appInfo = new AppInfo();
//            appInfo.setAppName("抖音极速版");
//            appInfo.setName("抖音极速版");
//            appInfo.setFree(true);
//            appInfo.setPeriod(4l);
//            appInfo.setPkgName(PN_DOU_YIN);
//            List<AppInfo> appInfos = new ArrayList<>();
//            appInfos.add(appInfo);
//            TaskInfo taskInfo = new TaskInfo();
//            taskInfo.setAppInfos(appInfos);
//            SPService.put(SPService.SP_TASK_LIST, taskInfo);
        }
        this.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInstallFengSheng = FengShengUtil.isInstallPackage(PN_FENG_SHENG);
        isInstallKuaiShou = KuaiShouUtil.isInstallPackage(PN_KUAI_SHOU);
        isInstallDouyin = DouYinUtil.isInstallPackage(PN_DOU_YIN);
        isInstallTouTiao = TouTiaoUtil.isInstallPackage(PN_TOU_TIAO);
        isInstallDianTao = DianTaoUtil.isInstallPackage(PN_DIAN_TAO);
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

        // 检查更新
        new CheckUpdateTask(this).execute();
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
     * @param uuid 替换某任务
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

    /**
     * 分享的时候调用，动态申请权限
     */
    private void requestSharePermission() {
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }
    }

    private boolean checkPkgValid() {
        for(AppInfo appInfo: appInfos) {
            if(!isAppExist(appInfo.getPkgName())) {
                Toast.makeText(this, String.format("请先安装应用「%s」", appInfo.getAppName()), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    protected boolean isAppExist(String pkgName) {
        ApplicationInfo info;
        try {
            info = getPackageManager().getApplicationInfo(pkgName, 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }
        return info != null;
    }

    private void showExitDialog(){
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
}
