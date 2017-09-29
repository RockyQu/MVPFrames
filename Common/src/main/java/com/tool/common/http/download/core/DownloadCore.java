package com.tool.common.http.download.core;

import android.content.Context;
import android.text.TextUtils;

import com.logg.Logg;
import com.tool.common.di.component.AppComponent;
import com.tool.common.http.download.exception.DownloadException;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.response.DownloadResponse;
import com.tool.common.utils.AppUtils;

import java.io.File;
import java.io.FileNotFoundException;
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
    private DownloadResponse downResponse;

    public DownloadCore(Context application, DownloadRequest request, DownloadResponse response) {
        okHttpClient = AppUtils.obtainAppComponentFromContext(application).getOkHttpClient();

        this.downRequest = request;
        this.downResponse = response;
    }

    @Override
    public void execute() {
        final String url = downRequest.getUrl();
        if (TextUtils.isEmpty(url)) {
            if (downResponse != null) {
                downResponse.onFailure(new DownloadException("The download address is empty!"));
            }
            return;
        }

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (downResponse != null) {
                    downResponse.onFailure(new DownloadException("Download failed! (OkHttp onFailure)", e));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                long total = responseBody.contentLength();
                if (downResponse != null) {
                    downResponse.onStart(total);
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
                        if (downResponse != null) {
                            downResponse.onProgress(sum, total);
                        }
                    }
                    fos.flush();

                    // Download completed
                    if (downResponse != null) {
                        downResponse.onFinish(downRequest);
                    }
                } catch (Exception e) {
                    if (downResponse != null) {
                        downResponse.onFailure(new DownloadException("File exception! (OkHttp onResponse)", e));
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
                        if (downResponse != null) {
                            downResponse.onFailure(new DownloadException("IO close failed!", ignored));
                        }
                    }
                }
            }
        });

    }
}