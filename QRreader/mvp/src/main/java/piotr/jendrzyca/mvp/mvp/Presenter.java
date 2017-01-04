package piotr.jendrzyca.mvp.mvp;

/**
 * Created by huddy on 22.12.2016.
 */

public interface Presenter <T extends View> {

    void onCreate();

    void onStart();

    void onStop();

    void onPause();

    void attachView(T view);
}
