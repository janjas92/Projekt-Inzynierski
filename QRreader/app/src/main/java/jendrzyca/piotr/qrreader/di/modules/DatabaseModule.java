package jendrzyca.piotr.qrreader.di.modules;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import jendrzyca.piotr.qrreader.di.scopes.PerActivity;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */

@Module
public class DatabaseModule {

    @Provides
    @PerActivity
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

}
