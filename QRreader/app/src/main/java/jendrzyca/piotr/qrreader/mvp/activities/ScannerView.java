package jendrzyca.piotr.qrreader.mvp.activities;

import jendrzyca.piotr.qrreader.mvp.View;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public interface ScannerView extends View {

    void displayEmployeeInfo();

    void showLoading();

    void hideLoading();

    void displayError(String err);
}
