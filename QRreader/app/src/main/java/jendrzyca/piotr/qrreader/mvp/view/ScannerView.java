package jendrzyca.piotr.qrreader.mvp.view;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public interface ScannerView extends View {

    void displayEmployeeInfo(String name, Integer status, String hashCode, String apiKey);

    void showLoading();

    void hideLoading();

    void displayError(String err);
}
