package com.example.l.okhttpdemo.model;


import android.content.Context;

import okhttp3.ResponseBody;

/**
 * Created by L on 2017/8/10.
 */

public class DownloadModel {
    public DownloadModel(String url) {
        this.url = url;
    }

    public long getDoneBytes(Context mContext) {
        return DownloadModelSP.getDownloadData(mContext);
    }

    public void setDoneBytes(Context mContext, long done) {
        DownloadModelSP.saveDownloadData(mContext, done);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    private String url;
    private ResponseBody responseBody;
}
