package jendrzyca.piotr.qrreader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.model.AccessToken;
import jendrzyca.piotr.qrreader.network.AuthenticationModule;
import jendrzyca.piotr.qrreader.network.LoginService;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.DefaultRegisterView;
import shem.com.materiallogin.MaterialLoginView;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    private final String clientId = "4dbaff1d0cf07615cac0";
    private final String clientSecret = "50b3f99f70ea6a20daca6ac41c66d80db967a733";
    private final String redirectUri = "your://redirecturi";

    @BindView(R.id.login)
    MaterialLoginView loginView;

    @BindString(R.string.repeat_pass_error)
    String ERROR_INVALID_REPEAT_PASSWORD;

    @BindString(R.string.username_invalid)
    String ERROR_INVALID_USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ((DefaultLoginView)loginView.getLoginView()).setListener((loginUser, loginPass) -> {
            String username = loginUser.getEditText().getText().toString();
            String password = loginPass.getEditText().getText().toString();

            //logs
            Timber.i("password "+ password);
            Timber.i("username "+ username);

            //logowanko tutaj
//            Intent intent = new Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse(AuthenticationModule.BASE_URL + "/authorize" + "?client_id=" + clientId + "&redirect_uri=" + redirectUri));
//            startActivity(intent);

            Intent i = new Intent(this, ScannerActivity.class);
            startActivity(i);

        });

        ((DefaultRegisterView)loginView.getRegisterView()).setListener((registerUser, registerPass, registerPassRep) -> {

            registerPassRep.setErrorEnabled(false);
            registerPassRep.setErrorEnabled(false);
            registerUser.setErrorEnabled(false);

            String username = registerUser.getEditText().getText().toString();
            String password = registerPass.getEditText().getText().toString();
            String rePassword = registerPassRep.getEditText().getText().toString();

            Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]{5,}$");
            Matcher userMatcher = pattern.matcher(username);
            Matcher passwordMatcher = pattern.matcher(password);

            //check if given inputs are valid
            if(!password.equals(rePassword) || !passwordMatcher.find()){
                registerPassRep.setError(ERROR_INVALID_REPEAT_PASSWORD);
            }
            if (!userMatcher.find()) {
                registerUser.setError(ERROR_INVALID_USERNAME);
            }

            //logs
            Timber.i("username "+ username);
            Timber.i("password "+ password);
            Timber.i("re-password "+ rePassword);

            //rejestrowanko tutaj
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                LoginService loginService = AuthenticationModule.service(LoginService.class, clientId, clientSecret);
                Observable<AccessToken> call = loginService.login(code, "authorization_code");
                call.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<AccessToken>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(AccessToken token) {
                                Timber.i("Access token " + token.getAccess_token());
                            }
                        });
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }
}
