package jendrzyca.piotr.qrreader.di.components;

import dagger.Component;
import jendrzyca.piotr.qrreader.activities.EmployeeInfoActivity;
import jendrzyca.piotr.qrreader.activities.ScannerActivity;
import jendrzyca.piotr.qrreader.di.modules.BitmapCacheModule;
import jendrzyca.piotr.qrreader.di.modules.DatabaseModule;
import jendrzyca.piotr.qrreader.di.scopes.PerActivity;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;

/**
 * Created by huddy on 09.12.2016.
 */
@PerActivity
@Component(modules = {DatabaseModule.class}, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    void inject(ScannerActivity scannerActivity);

    void inject(EmployeeInfoActivity employeeInfoActivity);

    Retrofit retrofit();

    BitmapCache bitmapCache();
}