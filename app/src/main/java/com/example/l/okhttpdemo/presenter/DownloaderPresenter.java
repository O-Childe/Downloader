package com.example.l.okhttpdemo.presenter;

import android.content.Context;

import com.example.l.okhttpdemo.downloader.Downloader;
import com.example.l.okhttpdemo.model.DownloadModel;
import com.example.l.okhttpdemo.view.DownloadView;


/**
 * Created by L on 2017/8/9.
 */

public class DownloaderPresenter implements Presenter {

    private Context mContext;
    private DownloadView downloadView;
    private Downloader downloader;

    public DownloaderPresenter(Context context, DownloadView downloadView) {
        this.mContext = context;
        this.downloadView = downloadView;
        this.downloader = new Downloader(mContext, downloadView);
    }

    public void downloadStart(String url) {
        downloader.download(new DownloadModel(url));
    }

    public void downloadResume() {

    }

    public void downloadPause() {

    }

    public void downloadStop() {
        downloader.stop();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

}
