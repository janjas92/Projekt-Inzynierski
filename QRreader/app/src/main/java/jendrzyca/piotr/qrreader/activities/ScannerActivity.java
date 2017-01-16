package jendrzyca.piotr.qrreader.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.ActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.di.modules.DatabaseModule;
import jendrzyca.piotr.qrreader.mvp.presenter.ScannerPresenter;
import jendrzyca.piotr.qrreader.mvp.view.ScannerView;
import jendrzyca.piotr.qrreader.network.ApiError;
import jendrzyca.piotr.qrreader.utils.CodeCallback;
import timber.log.Timber;

public class ScannerActivity extends AppCompatActivity implements ScannerView {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.scanner)
    DecoratedBarcodeView scanner;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progress;

    @Inject
    ScannerPresenter presenter;

    private ActivityComponent component;

    private CountDownTimer updateDateAndTime = new CountDownTimer(100000000, 1000) {
        @Override
        public void onTick(long l) {
            setDateAndTime();
        }

        @Override
        public void onFinish() {
            start();
        }
    };

    private CodeCallback callback = new CodeCallback(result -> processBarcodeResult(result));


    private void processBarcodeResult(BarcodeResult result) {
        showLoading();
        scanner.pause();
        //getting bitmap image
        Bitmap preview = result.getBitmap();

        //setting bitmap cache
        presenter.setBitmapCache(preview);

        String hashCode = result.getText();

        //getting employee information
        presenter.getEmployeeInformation(hashCode);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //requesting camera permissions for api 23 +
        getCameraPermission();
        //keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        //creating component and injecting dependencies
        Timber.i("AppComponent" + QRreader.get().getApplicationComponent());

        component = DaggerActivityComponent.builder()
                .databaseModule(new DatabaseModule())
                .applicationComponent(QRreader.get().getApplicationComponent())
                .build();

        component.inject(this);

        presenter.attachView(this);
        //passing api key to presenter
        String apiKey = getIntent().getExtras().getString("key");
        Timber.i("apikey: " + apiKey);
        presenter.setApiKey(apiKey);

        Timber.i("Presenter: " + this.presenter);

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

    @Override
    protected void onResume() {
        super.onResume();
        scanner.resume();
        updateDateAndTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.pause();
        updateDateAndTime.cancel();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getCameraPermission() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 123);
        }
    }


    @Override
    public void displayEmployeeInfo(String name, Integer status, String hashCode, String apiKey) {
        Intent i = new Intent(this, EmployeeInfoActivity.class);
        i.putExtra("name", name);
        i.putExtra("status", status);
        i.putExtra("hash", hashCode);
        i.putExtra("apiKey", apiKey);

        startActivityForResult(i, 1);
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
        progress.show();
    }

    @Override
    public void hideLoading() {
        progress.hide();
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayError(String err) {
        scanner.resume();
        if (err.equals(ApiError.ERROR_NON_200_RESPONSE))
            Toast.makeText(this, "Nieprawidłowy kod QR", Toast.LENGTH_LONG).show();
        else Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
