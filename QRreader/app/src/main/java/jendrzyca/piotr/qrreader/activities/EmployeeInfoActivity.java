package jendrzyca.piotr.qrreader.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.ActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerFragmentComponent;
import jendrzyca.piotr.qrreader.di.components.FragmentComponent;
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
    @BindView(R.id.avatar)ImageView avatar;

    @Inject
    Retrofit retrofit;

    @Inject
    BitmapCache bmCache;

    private String message;

    private final CountDownTimer timer = new CountDownTimer(5000,1000) {
        @Override
        public void onTick(long l) {

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

        ButterKnife.bind(this);

        Intent i = getIntent();

        String hashCode = i.getExtras().getString("hashCode");

        firstName.setText(hashCode);


        ActivityComponent component = DaggerActivityComponent.builder()
                .applicationComponent(QRreader.get().getApplicationComponent())
                .build();
        component.inject(this);

        Timber.i("retrofit: " + this.retrofit);
        Timber.i("bmCache: " + this.bmCache);

        //set text views
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.start();
    }
}
