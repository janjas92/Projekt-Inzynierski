package jendrzyca.piotr.qrreader.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by huddy on 14.10.2016.
 */

public class EmployeeInfoDialogFragment extends DialogFragment {

    private ClosingCallback listener;

    public interface ClosingCallback {
        void onClose();
    }

    final Handler handler  = new Handler();
    final Runnable runnable = () -> {
        dismiss();
        listener.onClose();
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("hashCode");
        return new AlertDialog.Builder(getActivity())
                .setTitle("Leaving launcher")
                .setMessage(message)
                .setPositiveButton("x", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }

    public static EmployeeInfoDialogFragment newInstance(String code, ClosingCallback listener) {
        EmployeeInfoDialogFragment dialog = new EmployeeInfoDialogFragment();
        dialog.listener = listener;
        Bundle b = new Bundle();
        b.putString("hashCode", code);
        dialog.setArguments(b);

        return dialog;
    }



    @Override
    public void show(FragmentManager manager, String tag) {
        handler.postDelayed(runnable, 5000);
        super.show(manager, tag);
    }
}
