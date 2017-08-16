package com.example.l.okhttpdemo.net;

import okhttp3.OkHttpClient;


/**
 * Created by L on 2017/8/9.
 */

public class NetClient extends OkHttpClient {
    private volatile static NetClient client = null;

    private NetClient() {}


    public static NetClient getClient() {
        if (client == null) {
            synchronized (NetClient.class) {
                if (client == null) {
                    client = new NetClient();
                }
            }
        }
        return client;
    }
}
