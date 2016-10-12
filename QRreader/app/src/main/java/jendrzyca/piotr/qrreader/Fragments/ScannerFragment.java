package jendrzyca.piotr.qrreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.MainActivity;
import jendrzyca.piotr.qrreader.R;

/**
 * Created by Piotr Jendrzyca on 12.10.16.
 */

public class ScannerFragment extends Fragment {

    @BindView(R.id.decoratedScanner)
    DecoratedBarcodeView scannerView;

//    private BarcodeCallback callback = new BarcodeCallback() {
//        @Override
//        public void barcodeResult(BarcodeResult result) {
//            if (result != null) {
//                Toast.makeText(getContext(), result.getText(), Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        public void possibleResultPoints(List<ResultPoint> resultPoints) {
//        }
//    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scanner_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setting the scanner to decode continuously
        scannerView.setStatusText("");
        scannerView.decodeContinuous((MainActivity)getActivity());
        scannerView.resume();
    }

    @Override
    public void onDetach() {
        scannerView.pause();
        super.onDetach();

    }
}
