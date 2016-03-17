package com.example.yeats.apktransfer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class AppAdapter extends BaseAdapter {

    String TAG = AppAdapter.class.getName();

    private LayoutInflater myInflater;
    List<String> mApkSourcePathList = new ArrayList<>();
    List<String> mApkNameList = new ArrayList<>();
    List<String> mApkPathList = new ArrayList<>();
    List<Drawable> mAppIcon = new ArrayList<>();
    Context mContext;

    public AppAdapter(Context context , List<String> apkSourcePathList ,
                      List<String> apkNameList , List<String> apkPathList , List<Drawable> appIcon) {
        myInflater = LayoutInflater.from(context);
        mApkSourcePathList = apkSourcePathList;
        mApkNameList = apkNameList;
        mApkPathList = apkPathList;
        mAppIcon = appIcon;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mApkNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mApkNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myInflater.inflate(R.layout.list_item, null);
        ImageView appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
        TextView appName = (TextView) convertView.findViewById(R.id.appName);
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.check);
        checkBox.setTag(position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG,"position : "+ buttonView.getTag());
                Log.d(TAG,"isChecked : "+ buttonView.isChecked());
                if(buttonView.isChecked()) {
                    Uri uri = Uri.parse("file://" + mApkSourcePathList.get(buttonView.getVerticalScrollbarPosition()));
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sendIntent.setType("application/octet-stream");
                    mContext.startActivity(sendIntent);
                }
            }
        });
        appIcon.setImageDrawable(mAppIcon.get(position));
        appName.setText(mApkNameList.get(position));
        checkBox.setText("");
        return convertView;
    }
}
