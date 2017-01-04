package jendrzyca.piotr.qrreader.network;

import com.google.gson.JsonPrimitive;

import jendrzyca.piotr.qrreader.mvp.model.ApiKey;
import jendrzyca.piotr.qrreader.mvp.model.EmployeeInfo;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */

public interface ApiService {

    String baseUrl = "http://recap.pl/api/v1/";

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET("user/{apiKey}/{hash}")
    Observable<EmployeeInfo> getEmployee(
            @Path("apiKey") String apiKey,
            @Path("hash") String hash
    );

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @POST("user/{apiKey}/{hash}/{timestamp}/{status}")
    Observable<JsonPrimitive> setEmployeeStatusAndTimestamp(
            @Path("apiKey") String apiKey,
            @Path("hash") String hash,
            @Path("timestamp") String timestamp,
            @Path("status") Integer status
    );

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET("user/{hash}")
    Observable<ApiKey> getApiKey(
            @Path("hash") String hash
    );

}
