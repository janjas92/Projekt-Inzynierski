package jendrzyca.piotr.qrreader.mvp.presenter;

import android.graphics.Bitmap;

import javax.inject.Inject;

import io.realm.Realm;
import jendrzyca.piotr.qrreader.mvp.model.EmployeeInfo;
import jendrzyca.piotr.qrreader.mvp.view.ScannerView;
import jendrzyca.piotr.qrreader.network.ApiError;
import jendrzyca.piotr.qrreader.network.ApiService;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public class ScannerPresenter implements Presenter<ScannerView> {


    private final Retrofit retrofit;
    private final Realm realm;
    private final BitmapCache bitmapCache;
    private ScannerView view;

    private Subscription getEmployeeInformation;
    private String apiKey;

    @Inject
    public ScannerPresenter(Retrofit retrofit, BitmapCache bitmapCache, Realm realm) {
        this.retrofit = retrofit;
        this.realm = realm;
        this.bitmapCache = bitmapCache;
    }

    public void getEmployeeInformation(String hashCode) {

        Timber.i("apikey: " + apiKey);
        Timber.i("hash employee: " + hashCode);
        getEmployeeInformation = retrofit.create(ApiService.class)
                .getEmployee(apiKey, hashCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmployeeInfo>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                        view.hideLoading();
                        view.displayError(ApiError.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(EmployeeInfo employeeInfo) {
                        Timber.i("first name: " + employeeInfo.getFirstName());
                        Timber.i("status: " + employeeInfo.getStatus());
                        view.displayEmployeeInfo(employeeInfo.getFirstName(), employeeInfo.getStatus(), hashCode, apiKey);
                        view.hideLoading();
                    }
                });
    }

    public void setBitmapCache(Bitmap bitmap) {
        bitmapCache.addBitmap("avatar", bitmap);
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (getEmployeeInformation != null && !getEmployeeInformation.isUnsubscribed())
            getEmployeeInformation.unsubscribe();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(ScannerView view) {
        this.view = view;
    }
}
