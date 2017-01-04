package jendrzyca.piotr.qrreader.mvp.presenter;

import com.google.gson.JsonPrimitive;

import javax.inject.Inject;

import io.realm.Realm;
import jendrzyca.piotr.qrreader.mvp.view.EmployeeInfoView;
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
 * Created by Piotr Jendrzyca on 03.01.2017.
 */


public class EmployeeInfoPresenter implements Presenter<EmployeeInfoView> {
    private final Retrofit retrofit;
    private final Realm realm;
    private final BitmapCache bitmapCache;
    private EmployeeInfoView view;

    private Subscription postEmployeeInformation;

    @Inject
    public EmployeeInfoPresenter(Retrofit retrofit, Realm realm, BitmapCache bitmapCache) {
        this.realm = realm;
        this.bitmapCache = bitmapCache;
        this.retrofit = retrofit;
    }

    public void setEmployeeInformation(String apiKey, String hashCode, Integer status) {
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();

        postEmployeeInformation = retrofit.create(ApiService.class)
                .setEmployeeStatusAndTimestamp(apiKey,hashCode,timestamp,status)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonPrimitive>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                        view.displayError(ApiError.getErrorMessage(e));
                        view.hideLoading();
                    }

                    @Override
                    public void onNext(JsonPrimitive jsonObject) {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (postEmployeeInformation != null && !postEmployeeInformation.isUnsubscribed()) {
            postEmployeeInformation.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(EmployeeInfoView view) {
        this.view = view;
    }
}
