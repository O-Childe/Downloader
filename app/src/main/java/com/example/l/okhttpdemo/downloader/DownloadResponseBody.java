package com.example.l.okhttpdemo.downloader;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * Created by L on 2017/8/10.
 */

public class DownloadResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private DownloadListener downloadListener;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, DownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(bufferedSource == null) {
            bufferedSource = Okio.buffer(new ForwardingSource(responseBody.source()) {
                long totalBody = 0L;
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBody += bytesRead != -1 ? bytesRead : 0;
                    downloadListener.onProgress(totalBody, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            });
        }
        return bufferedSource;
    }
}
