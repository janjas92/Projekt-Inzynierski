package jendrzyca.piotr.qrreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wonderkiln.blurkit.BlurKit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.ActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Piotr Jendrzyca on 14.10.2016.
 */

public class EmployeeInfoActivity extends Activity {

    @BindView(R.id.tbFirstName)TextView firstName;
    @BindView(R.id.tbLastName)TextView lastName;
    @BindView(R.id.tbMessage)TextView welcomeMessage;
    @BindView(R.id.container)RelativeLayout container;
    @BindView(R.id.ivStatus)ImageView ivStatus;
    @BindView(R.id.tvStatus)TextView tvStatus;
    @BindView(R.id.tvRemaining)TextView remaining;


    @Inject
    Retrofit retrofit;

    @Inject
    BitmapCache bmCache;

    @Inject
    Context context;

    private String message;

    private final CountDownTimer timer = new CountDownTimer(5000,1000) {
        @Override
        public void onTick(long l) {
            remaining.setText("Potwierdzenie statusu za: "+ (l/1000) + "sek");
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
        setContentView(R.layout.dialog_layout);
        //keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
        BlurKit.init(this);

        Intent i = getIntent();
        String hashCode = i.getExtras().getString("hashCode");

        ActivityComponent component = DaggerActivityComponent.builder()
                .applicationComponent(QRreader.get().getApplicationComponent())
                .build();
        component.inject(this);

        Timber.i("retrofit: " + this.retrofit);
        Timber.i("bmCache: " + this.bmCache);

        firstName.setText(hashCode);
        lastName.setText("Piotr");
        tvStatus.setText("Sugerowany status to: WEJSCIE");

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
    }

    @Override
    protected void onStart() {
        blurryBg();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.start();
    }

    private void blurryBg() {
        Bitmap blurredBg = BlurKit.getInstance().blur(bmCache.getBitmap("avatar"), 5);
        container.setBackground(new BitmapDrawable(blurredBg));
    }
}
