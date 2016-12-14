package jendrzyca.piotr.qrreader.model;

/**
 * Created by Piotr Jendrzyca on 12.12.2016.
 */

public class AccessToken {

    private String access_token;
    private String token_type;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if ( ! Character.isUpperCase(token_type.charAt(0))) {
            token_type =
                    Character
                            .toString(token_type.charAt(0))
                            .toUpperCase() + token_type.substring(1);
        }

        return token_type;
    }
}