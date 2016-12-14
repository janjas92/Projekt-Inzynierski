package jendrzyca.piotr.qrreader.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import jendrzyca.piotr.qrreader.QRreader;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.ActivityComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerActivityComponent;
import jendrzyca.piotr.qrreader.di.modules.DatabaseModule;
import jendrzyca.piotr.qrreader.model.Properties;
import jendrzyca.piotr.qrreader.network.EmployeeService;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import jendrzyca.piotr.qrreader.utils.CodeCallback;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class ScannerActivity extends AppCompatActivity {

    public final String TAG = ScannerActivity.class.getSimpleName();

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.scanner)
    DecoratedBarcodeView scanner;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progress;

    @Inject
    Retrofit retrofit;

    @Inject
    Realm realm;

    @Inject
    BitmapCache bmCache;

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
        scanner.pause();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Bitmap preview = result.getBitmap();

        //adding preview to bitmap cache
        bmCache.addBitmap("avatar", preview);

        String hashCode = result.getText();
        progress.setVisibility(View.VISIBLE);
        progress.show();
        //retrofit
        Subscription summonerName = retrofit.create(EmployeeService.class)
                .getSummonerId("qtiepiotr", EmployeeService.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonObject -> changeType(jsonObject))
                .subscribe(value -> next(value));

        Intent i = new Intent(ScannerActivity.this, EmployeeInfoActivity.class);
        i.putExtra("hashCode", hashCode);

        startActivityForResult(i, 1);
    }

    private void next(Map<String, Properties> stringPropertiesMap) {
        Timber.i(stringPropertiesMap.get("qtiepiotr")+"");
        Properties p = stringPropertiesMap.get("qtiepiotr");
        Timber.i(p.getName());
        Timber.i(p.getId()+"");
        Timber.i(p.getProfileIconId() + "");

        progress.hide();
        progress.setVisibility(View.INVISIBLE);


    }

    private Map<String, Properties> changeType(JsonObject jo) {
        Type mapType = new TypeToken<Map<String,Properties>>(){}.getType();
        Gson gson = new Gson();
        Map<String, Properties> result = gson.fromJson(jo, mapType);
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                scanner.resume();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        Timber.i("Retrofit: " + this.retrofit);
        Timber.i("bmCache: " + this.bmCache);


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
        updateDateAndTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.pause();
        updateDateAndTime.cancel();
    }


}
