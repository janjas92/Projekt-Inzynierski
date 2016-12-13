package jendrzyca.piotr.qrreader.network;

import jendrzyca.piotr.qrreader.model.AccessToken;
import jendrzyca.piotr.qrreader.model.ApiKey;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Piotr Jendrzyca on 12.12.2016.
 *
 * Retrofit interface containing login and registering method
 */

public interface LoginService {

    @FormUrlEncoded
    @POST("access_token")
    Observable<AccessToken> login(@Field("code") String code,
                                  @Field("grant_type") String grantType);
}
