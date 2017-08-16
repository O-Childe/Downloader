package com.example.l.okhttpdemo.view.activity;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.l.okhttpdemo.R;
import com.example.l.okhttpdemo.presenter.DownloaderPresenter;
import com.example.l.okhttpdemo.view.DownloadView;

public class MainActivity extends AppCompatActivity implements DownloadView {
    public final int REQUEST_UPDATE = 372;

    public FrameLayout parent;
    public CardView cardView, cardViewMenu;
    public ProgressBar progressBar;
    public TextView progressTxt, stateTxt, stopTxt;
    public ImageView menuImg, stateImg, stopImg;
    private boolean isCollapse = true;

    private DownloaderPresenter downloaderPresenter = null;

    private float animOffset = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is a Download Demo!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        downloaderPresenter = new DownloaderPresenter(this, this);

        initView();
    }

    public void initView() {
        parent = (FrameLayout) findViewById(R.id.downloader);
        cardView = (CardView) findViewById(R.id.cardview);
        cardViewMenu = (CardView) findViewById(R.id.cardview_menu);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressTxt = (TextView) findViewById(R.id.downText);
        stateTxt = (TextView) findViewById(R.id.downStateText);
        stopTxt = (TextView) findViewById(R.id.downStopTxt);
        menuImg = (ImageView) findViewById(R.id.menuImg);
        stateImg = (ImageView) findViewById(R.id.downStateImg);
        stopImg = (ImageView) findViewById(R.id.downStopImg);


        LayoutTransition transition = new LayoutTransition();
        ObjectAnimator animMenuIn = ObjectAnimator.ofFloat(cardViewMenu, "scaleY", 0f, 1f);
        animMenuIn.setDuration(100);

        ObjectAnimator animMenuOut = ObjectAnimator.ofFloat(cardViewMenu, "scaleY", 1f, 0f);
        animMenuIn.setDuration(100);

//        parent.setPivotX(0);
        cardViewMenu.setPivotY(0);
        transition.setAnimator(LayoutTransition.APPEARING, animMenuIn);
        transition.setAnimator(LayoutTransition.DISAPPEARING, animMenuOut);
        parent.setLayoutTransition(transition);

        cardViewMenu.setVisibility(View.GONE);
        progressTxt.setText("0kb");
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMenuImg();
            }
        });
        stateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStateBtn();
            }
        });
        stateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStateBtn();
            }
        });
        stopTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadStop();
            }
        });
        stopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadStop();
            }
        });

    }

    public void onClickMenuImg() {
        if (isCollapse) {
            isCollapse = false;
            menuImg.setImageResource(R.drawable.ic_expand_less_black);
            cardView.setCardElevation(10f);
            cardViewMenu.setVisibility(View.VISIBLE);
            spreadMenu();
        } else {
            isCollapse = true;
            menuImg.setImageResource(R.drawable.ic_expand_more_black);
            cardView.setCardElevation(5f);
            cardViewMenu.setVisibility(View.GONE);
            collapseMenu();
        }
    }

    public void onClickStateBtn() {
        String state = stateTxt.getText().toString();
        if (!TextUtils.isEmpty(state)) {
            if (state.equals("START")) {
                downloadStart();
            } else if (state.equals("RESUME")) {
                downloadResume();
            } else if (state.equals("PAUSE")) {
                downloadPause();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void downloadStart() {
        stateImg.setImageResource(R.drawable.ic_pause_black);
        stateTxt.setText(R.string.pause);
        downloaderPresenter.downloadStart("http://dl.37wan.cn/upload/1_1002671_0/awalongzhiwang_1.apk");
    }

    @Override
    public void downloadStop() {
        stateImg.setImageResource(R.drawable.ic_play_arrow_black);
        stateTxt.setText(R.string.start);
        progressBar.setProgress(0);
        progressTxt.setText("0kb");
        downloaderPresenter.downloadStop();
    }

    @Override
    public void downloadPause() {
        stateImg.setImageResource(R.drawable.ic_play_arrow_black);
        stateTxt.setText(R.string.resume);
        downloaderPresenter.downloadPause();
    }

    @Override
    public void downloadResume() {
        stateImg.setImageResource(R.drawable.ic_pause_black);
        stateTxt.setText(R.string.pause);
        downloaderPresenter.downloadResume();
    }

    /**
     * Presenter监听进度用
     *
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    @Override
    public void downloading(final long bytesRead, final long contentLength, boolean done) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int progress = (int) (100 * bytesRead / contentLength);
                if(progress != progressBar.getProgress()) {
                    progressBar.setProgress(progress);
                }
                progressTxt.setText(bytesRead / 1000 + "kb/" + contentLength / 1000 + "kb");
            }
        });
    }

    public void collapseMenu() {
        translateView(cardView, -animOffset);
    }

    public void spreadMenu() {
        translateView(cardView, animOffset);
    }

    public void translateView(final View v, final float offset) {

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY(), offset);
        anim.setDuration(200);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                parent.setBackgroundColor(offset > 0 ? Color.parseColor("#30000000") : Color.parseColor("#00000000"));
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        anim.start();

    }

    @Override
    public void onBackPressed() {

    }
}
