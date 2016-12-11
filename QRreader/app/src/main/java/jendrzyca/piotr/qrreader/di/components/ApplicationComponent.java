package jendrzyca.piotr.qrreader.di.components;

import android.content.Context;

import dagger.Component;
import jendrzyca.piotr.qrreader.di.modules.ApplicationModule;
import jendrzyca.piotr.qrreader.di.modules.NetworkModule;
import jendrzyca.piotr.qrreader.di.scopes.PerApplication;
import retrofit2.Retrofit;

/**
 * Created by huddy on 17.10.2016.
 */
@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    //    void inject(EmployeeInfoDialogFragment dialogFragment);
    Retrofit retrofit();

    Context context();
    //    void inject(MainActivity mainActivity);
}
