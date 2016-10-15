package jendrzyca.piotr.qrreader.network;

import java.security.Timestamp;

import jendrzyca.piotr.qrreader.model.Employee;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */

public interface APIservice {

    //tu trzeba bedzie polaczyc te dwie rzeczy najlepiej
    @GET("{employee}")
    Observable<Employee> getEmployee(@Path("employee")String sum);

    @POST("{time}")
    Observable<Employee> sendTimeStamp(@Path("time")Timestamp ts);
}
