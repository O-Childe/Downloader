package com.example.l.okhttpdemo.view;

/**
 * Created by L on 2017/8/9.
 */

public interface DownloadView {
    void downloadStart();
    void downloadStop();
    void downloadPause();
    void downloadResume();
    void downloading(long bytesRead, long contentLength, boolean done);
}
