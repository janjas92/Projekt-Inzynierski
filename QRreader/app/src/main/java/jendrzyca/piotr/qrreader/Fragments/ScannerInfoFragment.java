package jendrzyca.piotr.qrreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jendrzyca.piotr.qrreader.R;

/**
 * Created by Piotr Jendrzyca on 12.10.16.
 */

public class ScannerInfoFragment extends Fragment{

    @BindView(R.id.textView)TextView info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.scanner_info_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setInfo(String msg) {
        info.setText(msg);
    }
}
