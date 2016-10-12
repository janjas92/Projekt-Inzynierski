package jendrzyca.piotr.qrreader;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

import jendrzyca.piotr.qrreader.Fragments.ScannerFragment;
import jendrzyca.piotr.qrreader.Fragments.ScannerInfoFragment;

public class MainActivity extends AppCompatActivity implements BarcodeCallback{

    ScannerInfoFragment infoFragment;
    ScannerFragment scannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting fragment manager
        FragmentManager manager = getSupportFragmentManager();

        //binding fragments
        scannerFragment = (ScannerFragment) manager.findFragmentById(R.id.fragmentScanner);
        infoFragment = (ScannerInfoFragment) manager.findFragmentById(R.id.fragmentInfo);

    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if (result != null) {
            infoFragment.setInfo(result.getText());
        }
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {

    }
}
