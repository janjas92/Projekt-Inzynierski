package jendrzyca.piotr.qrreader.mvp.model;

import com.google.gson.annotations.SerializedName;



/**
 * Created by Piotr Jendrzyca on 12.12.2016.
 */
public class ApiKey {

    @SerializedName("apiKey")
    public String apiKey;

    public ApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
