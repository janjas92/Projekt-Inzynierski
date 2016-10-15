package jendrzyca.piotr.qrreader.di.components;

import javax.inject.Singleton;

import dagger.Component;
import jendrzyca.piotr.qrreader.di.modules.ApplicationModule;
import jendrzyca.piotr.qrreader.di.modules.NetworkModule;
import retrofit2.Retrofit;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface NetworkComponent {
    Retrofit retrofit();
}
