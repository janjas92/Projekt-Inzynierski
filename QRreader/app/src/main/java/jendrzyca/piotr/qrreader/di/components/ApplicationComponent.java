package jendrzyca.piotr.qrreader.di.components;

import android.content.Context;

import dagger.Component;
import jendrzyca.piotr.qrreader.di.modules.ApplicationModule;
import jendrzyca.piotr.qrreader.di.modules.BitmapCacheModule;
import jendrzyca.piotr.qrreader.di.modules.NetworkModule;
import jendrzyca.piotr.qrreader.di.scopes.PerApplication;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;

/**
 * Created by huddy on 17.10.2016.
 */
@PerApplication
@Component(modules = {ApplicationModule.class, BitmapCacheModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Retrofit retrofit();

    Context context();

    BitmapCache bitmapCache();
}
