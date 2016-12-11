package jendrzyca.piotr.qrreader.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.R;
import jendrzyca.piotr.qrreader.di.components.DaggerFragmentComponent;
import jendrzyca.piotr.qrreader.di.components.FragmentComponent;
import jendrzyca.piotr.qrreader.utils.BitmapCache;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Piotr Jendrzyca on 14.10.2016.
 */

public class EmployeeInfoDialogFragment extends DialogFragment {

    @BindView(R.id.tbFirstName)TextView firstName;
    @BindView(R.id.tbLastName)TextView lastName;
    @BindView(R.id.tbMessage)TextView welcomeMessage;
    @BindView(R.id.avatar)ImageView avatar;

    @Inject
    Retrofit retrofit;

    @Inject
    BitmapCache bmCache;

    private String message;

    private ClosingCallback listener;


    public interface ClosingCallback {
        void onClose();
    }

    private final CountDownTimer timer = new CountDownTimer(5000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            dismiss();
            listener.onClose();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        FragmentComponent component = DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity)getActivity()).getActivityComponent())
                .build();

        component.inject(this);

        Timber.i("retrofit: " + this.retrofit);
        Timber.i("bmCache: " + this.bmCache);

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
        avatar.setImageBitmap(bmCache.getBitmap("avatar"));
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
        timer.start();
        super.show(manager, tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.start();
    }
}
