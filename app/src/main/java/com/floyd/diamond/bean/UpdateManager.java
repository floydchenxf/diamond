package com.floyd.diamond.bean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.floyd.diamond.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hy on 2016/3/16.
 */
public class UpdateManager {
    private Context mContext; //上下文
    private String isShowVersion;//是否弹出版本控制的dialog

    private String apkUrl =
            "http://openbox.mobilem.360.cn/index/d/sid/3214092"; //apk下载地址
    private static final String savePath = "/sdcard/updateAPK/"; //apk保存到SD卡的路径
    private static final String saveFileName = savePath + "qmmt.apk"; //完整路径名

    private ProgressBar mProgress; //下载进度条控件
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private int progress; //下载进度
    private boolean cancelFlag = false; //取消下载标志位

    private String serverVersion; //从服务器获取的版本号

    private String clientVersion="1.2"; //客户端当前的版本号

    private String updateDescription="更新描述";
    private boolean forceUpdate = true; //是否强制更新

    private AlertDialog alertDialog1, alertDialog2; //表示提示对话框、进度条对话框
    private String message;

    /**
     * 构造函数
     */

    public UpdateManager(Context context,String serverVersion,String isShowVersion,String message) {
        this.mContext = context;
        this.serverVersion=serverVersion;
        this.isShowVersion=isShowVersion;
        this.message=message;
    }

    /**
     * 显示更新对话框
     */
    public void showNoticeDialog() {

        Log.e("server",serverVersion);

        //如果版本最新，则不需要更新
        if (serverVersion.equals(clientVersion))
            return;
        try {
            if (isShowVersion.equals(0+"")){
                return;
            }
        }catch (Exception e){

        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(message);
        dialog.setTitle("发现新版本，请立即更新哦~~");
//        dialog.setMessage(updateDescription);
        dialog.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
                showDownloadDialog();
            }
        });
        dialog.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        //是否强制更新
        if (forceUpdate == false) {
            dialog.setNegativeButton("待会更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                }
            });
        }
        alertDialog1 = dialog.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();
    }

    /**
     * 显示进度条对话框
     */
    public void showDownloadDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//        dialog.setTitle("正在更新");
        dialog.setMessage("正在更新...");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        dialog.setView(v);
        //如果是强制更新，则不显示取消按钮
        if (forceUpdate == false) {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                    cancelFlag = false;
                }
            });
        }
        alertDialog2 = dialog.create();
        alertDialog2.setCancelable(false);
        alertDialog2.show();

        //下载apk
        downloadAPK();
    }

    /**
     * 下载apk的线程
     */
    public void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if (numread <= 0) {
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelFlag); //点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新UI的handler
     */
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
////            switch (msg.what) {
////                case DOWNLOADING:
////                    mProgress.setProgress(progress);
////                    break;
////                case DOWNLOADED:
////                    if (alertDialog2 != null)
////                        alertDialog2.dismiss();
////                    installAPK();
////                    break;
////                case DOWNLOAD_FAILED:
////                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
////                    break;
////                default:
////                    break;
////            }
//        }
//    };

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOADING:
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOADED:
                    if (alertDialog2 != null)
                        alertDialog2.dismiss();
                    installAPK();
                    break;
                case DOWNLOAD_FAILED:
                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下载完成后自动安装apk
     */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}