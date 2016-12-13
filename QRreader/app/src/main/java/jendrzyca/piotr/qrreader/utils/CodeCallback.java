package jendrzyca.piotr.qrreader.utils;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

/**
 * Created by Piotr Jendrzyca on 12.12.2016.
 *
 * Wrapper class to use lambda expression -> making code more readable
 */

public class CodeCallback implements BarcodeCallback {

    private ProcessResult processResult;

    public CodeCallback(ProcessResult processResult) {
        this.processResult = processResult;
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        processResult.barcodeResult(result);
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {
        return;
    }

    public interface ProcessResult {

        void barcodeResult(BarcodeResult result);
    }

}
