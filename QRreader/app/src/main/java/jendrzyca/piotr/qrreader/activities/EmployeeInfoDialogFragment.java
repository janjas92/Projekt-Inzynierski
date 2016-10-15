package jendrzyca.piotr.qrreader.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.R;

/**
 * Created by huddy on 14.10.2016.
 */

public class EmployeeInfoDialogFragment extends DialogFragment {

    @BindView(R.id.tbFirstName)TextView firstName;
    @BindView(R.id.tbLastName)TextView lastName;
    @BindView(R.id.tbMessage)TextView welcomeMessage;
    @BindView(R.id.avatar)ImageView avatar;

    String message;

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
//        String message = getArguments().getString("hashCode");
//        //firstName.setText(message);
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                .setView(R.layout.dialog_layout)
//                .setPositiveButton("x", (dialogInterface, i) -> dialogInterface.dismiss())
//                .create();
//        return dialog;
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        message = getArguments().getString("hashCode");
        dialog.requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container, false);
        ButterKnife.bind(this, view);
        firstName.setText(message);
        return view;
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
