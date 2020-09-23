package com.speant.user.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.speant.user.Common.callBacks.OnAddOnSelectComplete;
import com.speant.user.Common.callBacks.onAddOnChecked;
import com.speant.user.Models.AddOns;
import com.speant.user.Models.FoodQuantity;
import com.speant.user.R;
import com.speant.user.ui.adapter.AddOnAdapter;
import com.speant.user.ui.adapter.QuantityAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.speant.user.Common.CONST.ADDON_DATA;
import static com.speant.user.Common.CONST.QUANTITY_DATA;
import static org.greenrobot.eventbus.EventBus.TAG;

@SuppressLint("ValidFragment")
public class AddOnBottomFragment extends BottomSheetDialogFragment implements onAddOnChecked {

    @BindView(R.id.recycler_addon)
    RecyclerView recyclerAddon;
    @BindView(R.id.lay_addon)
    LinearLayout layAddon;
    @BindView(R.id.txt_apply_addon)
    AppCompatTextView txtApplyAddon;
    @BindView(R.id.recycler_quantity)
    RecyclerView recyclerQuantity;
    @BindView(R.id.lay_quantity)
    LinearLayout layQuantity;
    private View view;
    private List<AddOns> addOnsList = new ArrayList<>();
    private List<AddOns> selectedAddOnsList = new ArrayList<>();
    private List<FoodQuantity> foodQuantityList = new ArrayList<>();
    private FoodQuantity foodQuantity;
    private OnAddOnSelectComplete onAddOnSelectComplete;
    Activity activity;
    QuantityAdapter quantityAdapter;
    public AddOnBottomFragment() {

    }

    private void setValues() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            addOnsList.addAll(bundle.getParcelableArrayList(ADDON_DATA));
            foodQuantityList.addAll(bundle.getParcelableArrayList(QUANTITY_DATA));

            Log.e(TAG, "setValues: " + addOnsList.size());
            if (recyclerAddon.getAdapter() != null) {
                recyclerAddon.getAdapter().notifyDataSetChanged();
            }
            Log.e(TAG, "setValues: " + foodQuantityList.size());
            if (recyclerQuantity.getAdapter() != null) {
                quantityAdapter.setSelectedPosition();
                recyclerQuantity.getAdapter().notifyDataSetChanged();
            }

            if (foodQuantityList.isEmpty()) {
                layQuantity.setVisibility(View.GONE);
                layAddon.setVisibility(View.VISIBLE);
            } else if (addOnsList.isEmpty()) {
                layQuantity.setVisibility(View.VISIBLE);
                layAddon.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //use getActivity if the picker is called  from activity
            onAddOnSelectComplete = (OnAddOnSelectComplete) getActivity();
        } catch (final ClassCastException e) {
            throw new ClassCastException(" must implement OnCompleteListener" + e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addon_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        this.activity = getActivity();
        setRecycler();
        return view;
    }

    private void setRecycler() {
        Log.e(TAG, "setRecycler:addOnsList " + addOnsList.size());
        Log.e(TAG, "setRecycler:addOnsList " + foodQuantityList.size());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        recyclerAddon.setLayoutManager(linearLayoutManager);
        AddOnAdapter addOnAdapter = new AddOnAdapter(activity, addOnsList, this);
        recyclerAddon.setAdapter(addOnAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        recyclerQuantity.setLayoutManager(linearLayoutManager1);
        quantityAdapter = new QuantityAdapter(activity, foodQuantityList, this);
        recyclerQuantity.setAdapter(quantityAdapter);

        setValues();
    }


    @Override
    public void addOnChecked(int position, boolean isChecked) {
        if (isChecked) {
            selectedAddOnsList.add(addOnsList.get(position));
            Log.e(TAG, "addOnChecked:selectedAddOnsList add " + selectedAddOnsList.size());
        } else {
            selectedAddOnsList.remove(addOnsList.get(position));
            Log.e(TAG, "addOnChecked:selectedAddOnsList remove " + selectedAddOnsList.size());
        }
    }

    @Override
    public void quantityChecked(int position) {
        foodQuantity = foodQuantityList.get(position);
    }

    @OnClick({R.id.txt_apply_addon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_apply_addon:
                if(selectedAddOnsList.isEmpty()){
                    onAddOnSelectComplete.onAddOnComplete(null, foodQuantity);
                }else{
                    onAddOnSelectComplete.onAddOnComplete(selectedAddOnsList, foodQuantity);
                }

                break;
        }
    }
}
