package jendrzyca.piotr.qrreader.model;

/**
 * Created by huddy on 12.12.2016.
 */
public class ApiKey {
    private static ApiKey ourInstance = new ApiKey();

    public static ApiKey getInstance() {
        return ourInstance;
    }

    private ApiKey() {
    }
}
