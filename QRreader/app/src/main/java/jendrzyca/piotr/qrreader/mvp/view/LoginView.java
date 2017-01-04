package jendrzyca.piotr.qrreader.mvp.view;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public interface LoginView extends View {
    void showLoading();

    void hideLoading();

    void displayError(String err);

    void loginSuccessful(String apiKey);
}
