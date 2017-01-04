package jendrzyca.piotr.qrreader.mvp.view;

/**
 * Created by huddy on 03.01.2017.
 */

public interface EmployeeInfoView extends View {
    void showLoading();

    void hideLoading();

    void displayError(String err);
}
