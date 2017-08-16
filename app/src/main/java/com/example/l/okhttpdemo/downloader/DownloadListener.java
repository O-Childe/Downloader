package com.example.l.okhttpdemo.downloader;

/**
 * Created by L on 2017/8/10.
 */

public interface DownloadListener {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
