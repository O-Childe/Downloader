package com.example.l.okhttpdemo.downloader;

import android.content.Context;
import android.util.Log;

import com.example.l.okhttpdemo.model.DownloadModel;
import com.example.l.okhttpdemo.net.NetClient;
import com.example.l.okhttpdemo.view.DownloadView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by L on 2017/8/10.
 */

public class Downloader implements DownloadListener {

    private Context mContext;

    public DownloadView downloadView;
    private Call downCall;

    public Downloader(Context context, DownloadView downloadView) {
        this.mContext = context;
        this.downloadView = downloadView;
    }

    private Call newCall(DownloadModel downloadModel) {
        Log.e("okd", "start download!");
        Request request = new Request.Builder()
                .url(downloadModel.getUrl())
                .header("RANGE", "bytes=" + downloadModel.getDoneBytes(mContext) + "-")
                .build();

        Call call = NetClient.getClient().newBuilder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResp = chain.proceed(chain.request());

                        return originalResp.newBuilder()
                                .body(new DownloadResponseBody(originalResp.body(), Downloader.this))
                                .build();
                    }
                })
                .build()
                .newCall(request);

        return call;
    }

    public void download(final DownloadModel downloadModel) {
        downCall = newCall(downloadModel);
        downCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okd", "down fail!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                downloadModel.setResponseBody(response.body());
                writeToSDCard(downloadModel);
            }
        });
    }

    public void stop() {
        if(downCall != null) {
            downCall.cancel();
        }
    }

    public void writeToSDCard(DownloadModel downloadModel) {
        InputStream is = null;
        byte[] bytes = new byte[2048];
        int len = 0;
//        FileOutputStream fos = null;
        FileChannel fc = null;
        RandomAccessFile raf = null;
        String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            is = downloadModel.getResponseBody().byteStream();
            File result = new File(path, "test.apk");
            raf = new RandomAccessFile(result, "rwd");
            fc = raf.getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,
                    downloadModel.getDoneBytes(mContext),
                    downloadModel.getResponseBody().contentLength());
//            fos = new FileOutputStream(result);
            int sum = 0;
            while ((len = is.read(bytes)) != -1) {
                sum += len;
                mbb.put(bytes, 0, len);
                downloadModel.setDoneBytes(mContext, sum);
            }
//            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("okd", "download error!");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if(fc != null) {
                    fc.close();
                }

                if(null != raf) {
                    raf.close();
                }
            } catch (IOException e) {

            }
        }

        Log.e("okd", "download over!");
    }

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {
        Log.e("okd", "------------");
        Log.e("okd", "this bytes:" + bytesRead + "\nall bytes:" + contentLength + "\nprogress:" + 100 * bytesRead / contentLength + "%");
        Log.e("okd", "------------");
        downloadView.downloading(bytesRead, contentLength, done);
    }
}
