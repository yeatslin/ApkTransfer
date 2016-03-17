package com.example.yeats.apktransfer;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;

public class MainActivity extends Activity {

    String TAG = "ApkTransfer";

    ListView mListView;
    List<String> apkSourcePathList = new ArrayList<>();
    List<String> apkNameList = new ArrayList<>();
    List<String> apkPathList = new ArrayList<>();
    List<Drawable> appIcon = new ArrayList<>();
    AppAdapter mAppAdapter;
    CheckBox mCheckBox;
    File dstFile;
    String copyPath = "/sdcard/copy/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createCopyFolder();
        loadingApkInfo();
        initLayout();
    }

    private void initLayout() {
        mListView = (ListView) findViewById(R.id.list_view);
        mCheckBox = (CheckBox)findViewById(R.id.check);
        mAppAdapter = new AppAdapter(this,apkSourcePathList,apkNameList,apkPathList,appIcon);
        mListView.setAdapter(mAppAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adpterView, View view, int position, long id) {
                copy(new File(apkSourcePathList.get(position)), new File(apkPathList.get(position)));
                Toast.makeText(getApplicationContext(), "copy " + apkNameList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadingApkInfo() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        try {
            for(ApplicationInfo packageInfo : packages) {
                if(packageInfo.sourceDir.startsWith("/data/app/")) {
                    Log.d(TAG, "Installed package :" + packageInfo.packageName);
                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
                    File srcFile = new File(packageInfo.sourceDir);
                    if(srcFile.exists()) {
                        ApplicationInfo app = pm.getApplicationInfo(packageInfo.packageName,0);
                        String appName = pm.getApplicationLabel(app).toString();
                        Log.d(TAG,"appName : "+appName);
                        String path = copyPath + appName;
                        Log.d(TAG, "path :" + path);
                        apkSourcePathList.add(packageInfo.sourceDir);
                        apkPathList.add(path);
                        apkNameList.add(appName);
                        appIcon.add(packageInfo.loadIcon(getPackageManager()));
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG,"exception e : "+e);
            Toast.makeText(getApplicationContext(),
                    "please set write permission for AppTransfer in Settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void copy(File src, File dst) {
        try {
            if(dstFile == null) {
                dstFile = new File(copyPath);
                if(!dstFile.exists()) {
                    dstFile.mkdir();
                }
            }
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            Log.d(TAG,"exception e : "+e);
            Toast.makeText(getApplicationContext(),
                    "copy file failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void createCopyFolder() {
        dstFile = new File(copyPath);
        if(!dstFile.exists()) {
            dstFile.mkdir();
        }
    }
}
