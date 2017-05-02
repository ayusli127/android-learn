package com.example.administrator.servicebestpractice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.BoringLayout;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/2.
 */

public class DownloadTask extends AsyncTask <String,Integer,Integer>{
    private static final String TAG = "DownloadTask";
    public static final int TYPE_SUCCESS = 0 ;
    public static final int TYPE_FAILED = 1 ;
    public static final int TYPE_PAUSED = 2 ;
    public static final int TYPE_CANCELED = 3 ;

    private DownLoadListener listener ;
    private Boolean isCanceled = false;
    private Boolean isPaused = false;
    private int lastProgress;
    public DownloadTask(DownLoadListener listener){
        this.listener = listener;
    }
    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;
        long downloadedLength = 0 ;
        String downloadUrl = params[0];
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file =  new File(directory + fileName);
        if(file.exists()){
            downloadedLength = file.length();
        }
        try {
            long contentLength = getContentLength(downloadUrl);
            if(contentLength == 0 ){
                Log.d(TAG, "doInBackground: TYPE_FAILED cause contentLength = 0");
                return TYPE_FAILED;
            }else if(contentLength == downloadedLength){
                return  TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().addHeader("RANGE","bytes="+downloadedLength+"-").url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if(response != null){
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file,"rw");
                saveFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0 ;
                int len ;
                while ((len = is.read(b))!= -1){
                    if(isCanceled){
                        return  TYPE_CANCELED;
                    }else if(isPaused){
                        Log.d(TAG, "doInBackground: manual paused");
                        return TYPE_PAUSED;
                    }else{
                        total += len;
                        saveFile.write(b,0,len);
                        int progress = (int)((total+downloadedLength)*100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return  TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null) {
                    is.close();
                }
                if(saveFile != null){
                    saveFile.close();
                }
                if(isCanceled && file != null){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //publicProgress 之后
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    //后台任务执行完的一些UI操作
    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }
    public void pauseDownload(){
        isPaused = true;
    }
    public void cancelDownload(){
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if(response != null && response.isSuccessful()){
            long contextLength = response.body().contentLength();
            response.body().close();
            return  contextLength;
        }
        return 0;
    }
}
