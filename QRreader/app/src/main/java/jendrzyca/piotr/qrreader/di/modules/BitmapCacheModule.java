package jendrzyca.piotr.qrreader.di.modules;

import dagger.Module;
import dagger.Provides;
import jendrzyca.piotr.qrreader.di.scopes.PerActivity;
import jendrzyca.piotr.qrreader.utils.BitmapCache;

/**
 * Created by huddy on 11.12.2016.
 */
@Module
public class BitmapCacheModule {

    @Provides
    @PerActivity
    public BitmapCache provideBitmapCache() {
        return new BitmapCache();
    }

}
