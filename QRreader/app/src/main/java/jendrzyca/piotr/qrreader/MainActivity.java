package jendrzyca.piotr.qrreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scanner)DecoratedBarcodeView scanner;

    BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

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
