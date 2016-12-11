package jendrzyca.piotr.qrreader.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.ActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.di.modules.DatabaseModule;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements EmployeeInfoDialogFragment.ClosingCallback {

    public final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.scanner)
    DecoratedBarcodeView scanner;

    @Inject
    Retrofit retrofit;

    @Inject
    Realm realm;

    @Inject
    BitmapCache bmCache;

    private ActivityComponent component;

    private CountDownTimer changeTime = new CountDownTimer(100000000, 1000) {
        @Override
        public void onTick(long l) {
            setDateAndTime();
        }

        @Override
        public void onFinish() {
            start();
        }
    };

    private BarcodeCallback callback = new BarcodeCallback() {

        @Override
        public void barcodeResult(BarcodeResult result) {
            scanner.pause();

            FragmentManager fragmentManager = getSupportFragmentManager();
            Bitmap preview = result.getBitmap();

            //adding preview to bitmap cache
            bmCache.addBitmap("avatar", preview);

            Timber.i("Cache bitmap" + bmCache.getBitmap("avatar"));

            EmployeeInfoDialogFragment dialog = EmployeeInfoDialogFragment.newInstance(result.getText(), MainActivity.this);

            // Hide after some seconds
            dialog.show(fragmentManager, "test");
            Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onClose() {
        scanner.resume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //creating component and injecting dependencies
        Timber.i("AppComponent" + QRreader.get().getApplicationComponent());

        component = DaggerActivityComponent.builder()
                .databaseModule(new DatabaseModule())
                .applicationComponent(QRreader.get().getApplicationComponent())
                .build();

        component.inject(this);


        Timber.i("Retrofit: " + this.retrofit);

        //setting the scanner to use front camera
        CameraSettings settings = scanner.getBarcodeView().getCameraSettings();
        //CameraManager cameraManager = scanner.getViewFinder();
        settings.setRequestedCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);

        //enabling new settings
        scanner.getBarcodeView().setCameraSettings(settings);
        //hiding decorations
        scanner.getViewFinder().setVisibility(View.INVISIBLE);
        //clearing scanners display name
        scanner.setStatusText("");
        //setting scanner to run continuously
        scanner.decodeContinuous(callback);
    }

    private void setDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String date = dateFormat.format(calendar.getTime());
        date.toLowerCase();
        date = date.replace(date.charAt(0), date.toUpperCase().charAt(0));
        String time = timeFormat.format(calendar.getTime());

        tvDate.setText(date);
        tvTime.setText(time);
    }


    public ActivityComponent getActivityComponent() {
        return this.component;
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.resume();
        changeTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.pause();
        changeTime.cancel();
    }


}
