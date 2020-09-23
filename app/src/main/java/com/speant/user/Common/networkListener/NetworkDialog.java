package com.speant.user.Common.networkListener;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NetworkDialog extends BottomSheetDialogFragment {
    @BindView(R.id.txt_ok)
    AppCompatTextView txtOk;
    @BindView(R.id.lay)
    RelativeLayout lay;
    Unbinder unbinder;
    private View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_network, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.txt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_ok:
                getActivity().finishAffinity();
                getDialog().dismiss();
                break;
        }
    }
}
