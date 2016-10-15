package jendrzyca.piotr.qrreader.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.journeyapps.barcodescanner.camera.CameraManager;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.R;

public class MainActivity extends AppCompatActivity implements EmployeeInfoDialogFragment.ClosingCallback {

    @BindView(R.id.scanner)
    DecoratedBarcodeView scanner;
    @BindView(R.id.tvDate)TextView tvDate;
    @BindView(R.id.tvTime)TextView tvTime;

    BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            scanner.pause();

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

        setDateAndTime();


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
        date = date.replace(date.charAt(0), date.toUpperCase().charAt(0));
        String time = timeFormat.format(calendar.getTime());

        tvDate.setText(date);
        tvTime.setText(time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.pause();
    }


}
