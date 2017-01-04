package jendrzyca.piotr.qrreader.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wonderkiln.blurkit.BlurKit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.ActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.mvp.presenter.EmployeeInfoPresenter;
import jendrzyca.piotr.qrreader.mvp.view.EmployeeInfoView;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import timber.log.Timber;

/**
 * Created by Piotr Jendrzyca on 14.10.2016.
 */

public class EmployeeInfoActivity extends Activity implements EmployeeInfoView {

    @BindView(R.id.tvFirstName)
    TextView firstName;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.ivSuggestedStatus)
    ImageView ivSuggestedStatus;
    @BindView(R.id.ivStatus)
    ImageView ivChangeStatus;
    @BindView(R.id.tvRemaining)
    TextView remaining;
    @BindView(R.id.loadBar)
    SpinKitView loadBar;

    @Inject
    EmployeeInfoPresenter presenter;

    @Inject
    BitmapCache bmCache;

    private ActivityComponent component;


    private String name;
    private String hash;
    private String apiKey;
    private Integer status;

    private final CountDownTimer timer = new CountDownTimer(25000, 1000) {
        @Override
        public void onTick(long l) {
            remaining.setText("Potwierdzenie statusu za: " + (l / 1000) + "sek");
        }

        @Override
        public void onFinish() {
            setResult(1);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);
        //portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
        BlurKit.init(this);

        Intent i = getIntent();
        apiKey = i.getExtras().getString("apiKey");
        name = i.getExtras().getString("name");
        hash = i.getExtras().getString("hash");
        status = i.getExtras().getInt("status");

        setImageViewBackground(status == 1);


        //injecting presenter dependencies
        component = DaggerActivityComponent.builder()
                .applicationComponent(QRreader.get().getApplicationComponent())
                .build();
        component.inject(this);

        presenter.attachView(this);

        Timber.i("presenter: " + this.presenter);

        //setting up listeners for imageviews
        ivSuggestedStatus.setOnClickListener(view -> finish());
        ivChangeStatus.setOnClickListener(view -> {
            presenter.setEmployeeInformation(apiKey, hash, status);
            timer.cancel();
        });

        firstName.setText(name);

        //blurring background
        blurryBg();

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        presenter.onPause();
    }

    @Override
    protected void onStart() {
        blurryBg();
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.start();
        presenter.onResume();

    }

    private void blurryBg() {
        Bitmap blurredBg = BlurKit.getInstance().blur(bmCache.getBitmap("avatar"), 5);
        container.setBackground(new BitmapDrawable(blurredBg));
    }

    private void setImageViewBackground(Boolean b) {
        if (b) {
            ivChangeStatus.setImageResource(R.drawable.in_small);
            ivSuggestedStatus.setImageResource(R.drawable.out_big);
        }
    }

    @Override
    public void showLoading() {
        remaining.setVisibility(View.INVISIBLE);
        loadBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadBar.setVisibility(View.INVISIBLE);
        setResult(1);
        finish();
    }

    @Override
    public void displayError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }
}
