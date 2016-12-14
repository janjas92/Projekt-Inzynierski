package jendrzyca.piotr.qrreader.di.modules;

import android.app.Application;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import jendrzyca.piotr.qrreader.di.scopes.PerApplication;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */
@Module (includes = ApplicationModule.class)
public class NetworkModule {

    final String BASE_URL = "https://euw.api.pvp.net/api/lol/";

    @Provides
    @PerApplication
    public HttpLoggingInterceptor provideInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    @Provides
    @PerApplication
    public Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 *  1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @PerApplication
    public OkHttpClient provideOkHttpClient(Cache cache, HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @PerApplication
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
