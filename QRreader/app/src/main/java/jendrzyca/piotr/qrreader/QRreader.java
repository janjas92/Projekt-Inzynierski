package jendrzyca.piotr.qrreader;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import jendrzyca.piotr.qrreader.di.components.ApplicationComponent;
import jendrzyca.piotr.qrreader.di.components.DaggerApplicationComponent;
import jendrzyca.piotr.qrreader.di.modules.ApplicationModule;
import jendrzyca.piotr.qrreader.di.modules.NetworkModule;
import jendrzyca.piotr.qrreader.logging.ReleaseTree;
import timber.log.Timber;

/**
 * Created by huddy on 14.10.2016.
 */

public class QRreader extends Application {

    private static QRreader qRreader;

    public static QRreader get() {
        return qRreader;
    }
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //setting up timber

        //planting a debug tree
        if (BuildConfig.DEBUG) {
            //adding line number for debug mode
            Timber.plant(new Timber.DebugTree(){
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ':' + element.getLineNumber();
                }
            });
        }
        //planting a release tree
        else Timber.plant(new ReleaseTree());

        //initializing database
        initRealmConfiguration();

        //creating dependency tree
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .build();

        qRreader = QRreader.this;
    }

    //exposing app component
    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initRealmConfiguration() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
