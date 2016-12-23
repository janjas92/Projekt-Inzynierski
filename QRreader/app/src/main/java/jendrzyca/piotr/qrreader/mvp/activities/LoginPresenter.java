package jendrzyca.piotr.qrreader.mvp.activities;

import android.content.Context;
import android.util.Base64;

import javax.inject.Inject;

import jendrzyca.piotr.qrreader.model.ApiKey;
import jendrzyca.piotr.qrreader.mvp.Presenter;
import jendrzyca.piotr.qrreader.network.ApiService;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        getApiKey = retrofit.create(ApiService.class).getApiKey(Base64.encodeToString((username + password).getBytes(), Base64.NO_WRAP))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiKey>() {
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
                    public void onNext(ApiKey apiKey) {
                        view.hideLoading();
                        view.loginSuccessful();
                    }
                });
        view.showLoading();
    }

    @Override
    public void onCreate() {

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
