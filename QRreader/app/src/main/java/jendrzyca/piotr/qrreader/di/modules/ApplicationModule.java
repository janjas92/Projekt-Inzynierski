package jendrzyca.piotr.qrreader.di.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jendrzyca.piotr.qrreader.di.scopes.PerApplication;

/**
 * Created by huddy on 14.10.2016.
 */
@Module
public class ApplicationModule {

    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {
        return this.application;
    }

    @Provides
    @PerApplication
    public Context provideContext() {
        return application.getApplicationContext();
    }
}
