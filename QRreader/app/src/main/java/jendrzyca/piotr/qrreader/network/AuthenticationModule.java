package jendrzyca.piotr.qrreader.network;

import android.util.Base64;

import jendrzyca.piotr.qrreader.model.AccessToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Piotr Jendrzyca on 12.12.2016.
 */

public class AuthenticationModule {

    public static final String BASE_URL = "https://github.com/login/oauth/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    public static  <S> S service(Class<S> serviceClass) {
        return service(serviceClass, null, null);
    }

    public static <S> S service(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            //building request
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            //setting up logging interceptor
            httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S service(Class<S> serviceClass, AccessToken token) {
        if (token != null) {
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization",
                                token.getToken_type() + " " + token.getAccess_token())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            //setting up logging interceptor
            httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));

        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
