package jendrzyca.piotr.qrreader.network;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Piotr Jendrzyca on 23.12.2016.
 */

public class ApiError {

    public static final String ERROR_NON_200_RESPONSE = "Użytkownik o podaj nazwie nie istnieje";
    public static final String ERROR_NETWORK = "Nie można połączyć się z serwerem, sprawdź połączenie internetowe i sprobuj ponownie";
    public static final String ERROR_UNEXPECTED = "Coś poszło nie tak, proszę sprawdzić połączenie z internetem i spróbować jeszcze raz";


    public static String getErrorMessage(Throwable t) {

        if(t instanceof HttpException){
            return ERROR_NON_200_RESPONSE;
        } else if (t instanceof IOException) {
            return ERROR_NETWORK;
        }
        else return ERROR_UNEXPECTED;

    }
}
