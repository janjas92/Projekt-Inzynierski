package jendrzyca.piotr.qrreader.di.modules;

import dagger.Module;
import dagger.Provides;
import jendrzyca.piotr.qrreader.di.scopes.PerActivity;
import jendrzyca.piotr.qrreader.di.scopes.PerApplication;
import jendrzyca.piotr.qrreader.utils.BitmapCache;

/**
 * Created by Piotr Jendrzyca on 11.12.2016.
 */

@Module
public class BitmapCacheModule {

    @Provides
    @PerApplication
    public BitmapCache provideBitmapCache() {
        return new BitmapCache();
    }

}
