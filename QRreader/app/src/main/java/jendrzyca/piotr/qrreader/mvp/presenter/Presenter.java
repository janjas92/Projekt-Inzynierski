package jendrzyca.piotr.qrreader.mvp.presenter;

import jendrzyca.piotr.qrreader.mvp.view.View;

/**
 * Created by Piotr Jendrzyca on 22.12.2016.
 */

public interface Presenter <T extends View> {

    void onResume();

    void onStart();

    void onStop();

    void onPause();

    void attachView(T view);
}
