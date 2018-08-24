package com.common.util.util.apk_down;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import com.common.util.file.FileSizeUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 @author
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    private static final String TAG = "下载任务";
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;


    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;

    private int lastProgress;

    private File file = null;
    private Context mContext = null;

    public DownloadTask(Context context,DownloadListener listener) {
        this.mContext = context;
        this.listener = listener;
    }





    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
//        File file = null;
        try {
            // 记录已下载的文件长度
            long downloadedLength = 0;
            String downloadUrl = params[0];
            //http://120.78.158.67:8083/StarCare-app/care/appUpSoft/lastVersion?softPath=app-release-201805181648383984.apk
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("="));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            Log.d(TAG, "doInBackground: 总的内容长度-->"+contentLength);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                // 已下载字节和文件总字节相等，说明已经下载完成了
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 断点下载，指定从哪个字节开始下载
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                // 跳过已下载的字节
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if(isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        // 计算已下载的百分比
                        if(contentLength != -1){
                            int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                            Log.d(TAG, "doInBackground: 下载进度..实时.."+progress+"...总长度.."+contentLength);
                            publishProgress(progress);
                        }else {
                            Log.d(TAG, "doInBackground: 获取总长度失败，无法计算进度..当前已下载.."+ FileSizeUtil.formetFileSize((total+downloadedLength),FileSizeUtil.SIZETYPE_MB));
                        }
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                //下载完成之后安装文件
                ApkUtils.installApk(mContext,file);
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }


    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

}