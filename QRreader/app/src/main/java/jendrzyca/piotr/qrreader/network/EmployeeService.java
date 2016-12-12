package jendrzyca.piotr.qrreader.network;

import com.google.gson.JsonObject;

import java.security.Timestamp;

import jendrzyca.piotr.qrreader.model.Employee;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */

public interface EmployeeService {

    public final String API_KEY = "RGAPI-64ED5E8D-88A5-479F-88A8-F5C3656EBDBF";
    //tu trzeba bedzie polaczyc te dwie rzeczy najlepiej
    @GET("{employee}")
    Observable<Employee> getEmployee(@Path("employee")String sum);

    @POST("{time}")
    Observable<Employee> sendTimeStamp(@Path("time")Timestamp ts);

    //test methods rito api
    @GET("euw/v1.4/summoner/by-name/{name}?")
    Observable<JsonObject> getSummonerId(@Path("name") String name, @Query("api_key") String apiKey);

}
