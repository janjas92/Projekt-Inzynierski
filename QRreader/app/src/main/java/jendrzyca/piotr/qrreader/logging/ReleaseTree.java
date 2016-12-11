package jendrzyca.piotr.qrreader.logging;

import android.util.Log;

import timber.log.Timber;

/**
 * Created by huddy on 11.12.2016.
 */

public class ReleaseTree extends Timber.Tree {
    @Override
    protected boolean isLoggable(String tag, int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }

        //only warnings, errors and wtf's
        return true;
    }


    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if(isLoggable(tag,priority))
            Log.println(priority, tag, message);
    }
}
