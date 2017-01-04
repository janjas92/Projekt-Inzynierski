package jendrzyca.piotr.qrreader.mvp.presenter;

import android.content.Context;
import android.util.Base64;

import javax.inject.Inject;

import jendrzyca.piotr.qrreader.mvp.model.ApiKey;
import jendrzyca.piotr.qrreader.mvp.view.LoginView;
import jendrzyca.piotr.qrreader.network.ApiError;
import jendrzyca.piotr.qrreader.network.ApiService;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public class LoginPresenter implements Presenter<LoginView> {

    private Retrofit retrofit;
    private Context context;
    private LoginView view;

    private Subscription getApiKey;

    @Inject
    public LoginPresenter(Context context, Retrofit retrofit) {
        this.retrofit = retrofit;
        this.context = context;
    }

    public void login(String username, String password) {
        getApiKey = retrofit.create(ApiService.class).getApiKey(Base64.encodeToString(("test" + "ED96M8UP").getBytes(), Base64.NO_WRAP))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiKey>() {
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
                    public void onNext(ApiKey apiKey) {
                        Timber.i("api key"+ apiKey.getApiKey());
                        view.loginSuccessful(apiKey.getApiKey());
                    }
                });
        view.showLoading();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if(getApiKey != null && !getApiKey.isUnsubscribed())
            getApiKey.unsubscribe();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(LoginView view) {
        this.view = view;
    }
}
