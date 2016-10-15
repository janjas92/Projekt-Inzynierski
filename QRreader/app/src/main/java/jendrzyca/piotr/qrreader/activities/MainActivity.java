package jendrzyca.piotr.qrreader.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.R;

public class MainActivity extends AppCompatActivity implements EmployeeInfoDialogFragment.ClosingCallback {

    @BindView(R.id.scanner)
    DecoratedBarcodeView scanner;

    BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            scanner.pause();

            EmployeeInfoDialogFragment dialog = EmployeeInfoDialogFragment.newInstance(result.getText(), MainActivity.this);

            // Hide after some seconds
            dialog.show(fragmentManager, "test");
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onClose() {
        scanner.resume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //clearing scanners display name
        scanner.setStatusText("");
        //setting scanner to run continuously
        scanner.decodeContinuous(callback);
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
