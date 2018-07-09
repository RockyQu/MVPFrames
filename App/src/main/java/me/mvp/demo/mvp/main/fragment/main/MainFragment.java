package me.mvp.demo.mvp.main.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import me.logg.Logg;
import me.mvp.demo.R;
import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.frame.IPresenter;

/**
 *
 */
public class MainFragment extends BaseFragment {

    /**
     * Create Fragment
     *
     * @return
     */
    public static Fragment create(int index) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        Logg.e("MainFragment");

        //        Downloader.getInstance().create("http://download.alicdn.com/wireless/taobao4android/latest/702757.apk")
////                .setSaveFilePath(ProjectUtils.OTHER + "aaaaa")
//                .setListener(new DownloaderSampleListener() {
//
//                    @Override
//                    protected void onConnection(boolean isContinue, long progress, long total) {
//                        Logg.e(progress + "/" + total);
//                    }
//
//                    @Override
//                    protected void onProgress(long progress, long total, int speed) {
//                        int current = (int) (progress * 1.0f / total * 100);
//                        Logg.e(progress + "/" + total);
//                    }
//
//                    @Override
//                    protected void onFailure(DownloadException exception) {
//                        exception.printStackTrace();
//                    }
//
//                    @Override
//                    protected void onPaused() {
//                        Logg.e("onPaused");
//                    }
//
//                    @Override
//                    protected void onComplete(String filePath) {
//                        Logg.e("onFinish " + filePath);
//                    }
//                })
//                .start();
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }
}