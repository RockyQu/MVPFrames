package com.tool.common.http.download.core;

import android.content.Context;
import com.tool.common.http.download.DownloadListener;
import com.tool.common.http.download.exception.DownloadException;
import com.tool.common.http.download.request.DownloadRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadCore implements CoreExecute {

    private OkHttpClient okHttpClient = null;

    private DownloadRequest downRequest;
    private DownloadListener downloadListener;

    public DownloadCore(Context application, DownloadRequest request, DownloadListener listener) {
        okHttpClient = DownloadClient.getInstance().getOkHttpClient();

        this.downRequest = request;
        this.downloadListener = listener;
    }

    @Override
    public String execute() {
        Request request = new Request.Builder()
                .url(downRequest.getUrl())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (downloadListener != null) {
                    downloadListener.onFailure(new DownloadException(e.getMessage(), e));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                long total = responseBody.contentLength();
                if (downloadListener != null) {
                    downloadListener.onStart(total);
                }

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = responseBody.byteStream();
                    File file = new File(downRequest.getSaveFilePath(), downRequest.getSaveFileName());
                    fos = new FileOutputStream(file);

                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;

                        // Downloading
                        if (downloadListener != null) {
                            downloadListener.onProgress(sum, total);
                        }
                    }
                    fos.flush();

                    // Download completed
                    if (downloadListener != null) {
                        downloadListener.onFinish();
                    }
                } catch (Exception e) {
                    if (downloadListener != null) {
                        downloadListener.onFailure(new DownloadException("File exception! (OkHttp onResponse)", e));
                    }
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException ignored) {
                        if (downloadListener != null) {
                            downloadListener.onFailure(new DownloadException("IO close failed!", ignored));
                        }
                    }
                }
            }
        });

        return downRequest.getId();
    }
}