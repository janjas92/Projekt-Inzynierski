package piotr.jendrzyca.mvp.mvp.activities;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.realm.Realm;
import jendrzyca.piotr.qrreader.mvp.Presenter;
import jendrzyca.piotr.qrreader.network.ApiService;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public class ScannerPresenter implements Presenter<ScannerView> {


    private final Retrofit retrofit;
    private final Realm realm;
    private final BitmapCache bitmapCache;
    private ScannerView view;

    private Subscription getEmployeeInfomration;

    @Inject
    public ScannerPresenter(Retrofit retrofit, BitmapCache bitmapCache, Realm realm) {
        this.retrofit = retrofit;
        this.realm = realm;
        this.bitmapCache = bitmapCache;
    }

    public void getEmployeeInformation(String hashCode) {

        view.showLoading();

        getEmployeeInfomration = retrofit.create(ApiService.class)
                .getSummonerId("qtiepiotr", ApiService.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.displayError(e.getMessage());
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        view.hideLoading();
                    }
                });
    }

    public void setBitmapCache(Bitmap bitmap) {
        bitmapCache.addBitmap("avatar", bitmap);
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if(getEmployeeInfomration != null && !getEmployeeInfomration.isUnsubscribed())
            getEmployeeInfomration.unsubscribe();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(ScannerView view) {
        this.view = view;
    }
}
