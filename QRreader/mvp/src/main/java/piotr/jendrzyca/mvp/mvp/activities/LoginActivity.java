package piotr.jendrzyca.mvp.mvp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.di.modules.DatabaseModule;
import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.MaterialLoginView;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @Inject
    LoginPresenter presenter;

    @BindView(R.id.login)
    MaterialLoginView loginView;

    @BindView(R.id.loadBar)
    SpinKitView loadBar;

    @BindString(R.string.repeat_pass_error)
    String ERROR_INVALID_REPEAT_PASSWORD;

    @BindString(R.string.username_invalid)
    String ERROR_INVALID_USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        DaggerActivityComponent.builder()
                .applicationComponent(((QRreader) getApplication()).getApplicationComponent())
                .databaseModule(new DatabaseModule())
                .build()
                .inject(this);

        presenter.attachView(this);

        ((DefaultLoginView) loginView.getLoginView()).setListener((loginUser, loginPass) -> {
            String username = loginUser.getEditText().getText().toString();
            String password = loginPass.getEditText().getText().toString();

            //logs
            Timber.i("password " + password);
            Timber.i("username " + username);
            Timber.i("presenter " + presenter);

            presenter.login(username, password);

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {
        loginView.setVisibility(View.INVISIBLE);
        loadBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loginView.setVisibility(View.VISIBLE);
        loadBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccessful() {
        Intent i = new Intent(this, ScannerActivity.class);
        startActivity(i);
    }
}
