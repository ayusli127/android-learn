package com.example.administrator.servicebestpractice;

/**
 * Created by Administrator on 2017/5/2.
 */

public interface DownLoadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
