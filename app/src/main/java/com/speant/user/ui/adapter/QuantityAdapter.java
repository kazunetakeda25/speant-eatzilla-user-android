package com.speant.user.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.onAddOnChecked;
import com.speant.user.Models.FoodQuantity;
import com.speant.user.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static org.greenrobot.eventbus.EventBus.TAG;

public class QuantityAdapter extends RecyclerView.Adapter<QuantityAdapter.ViewHolder> {
    private final SessionManager sessionManager;
    Activity activity;
    List<FoodQuantity> foodQuantityList;
    onAddOnChecked onAddOnChecked;

    private boolean onBind;
    private int selectedPosition = -1;

    public QuantityAdapter(Activity activity, List<FoodQuantity> foodQuantityList, onAddOnChecked onAddOnChecked) {
        this.activity = activity;
        this.foodQuantityList = foodQuantityList;
        this.onAddOnChecked = onAddOnChecked;
        Log.e("TAG", "QuantityAdapter:activity "+activity );
        Log.e("TAG", "QuantityAdapter:foodQuantityList "+foodQuantityList.size());
        sessionManager = new SessionManager(activity);
    }

    public void setSelectedPosition(){
        for (int i = 0; i < foodQuantityList.size(); i++) {
            Log.e(TAG, "QuantityAdapter:getPivot BEFORE"+foodQuantityList.get(i).getPivot().getIs_default());
            if (foodQuantityList.get(i).getPivot().getIs_default().equals("1")) {
                selectedPosition = i;
                Log.e(TAG, "QuantityAdapter:selectedPosition BEFORE "+selectedPosition );
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quantity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBind = true;
        holder.txtName.setText(foodQuantityList.get(position).getName());
        holder.txtQuanPrice.setText(sessionManager.getCurrency()+" "+foodQuantityList.get(position).getPivot().getPrice());
        if (selectedPosition == position) {
            holder.chkType.setChecked(true);
            onAddOnChecked.quantityChecked(selectedPosition);
        } else {
            holder.chkType.setChecked(false);
        }

        onBind = false;
    }


    @Override
    public int getItemCount() {
        return foodQuantityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.txt_name)
        AppCompatTextView txtName;
        @BindView(R.id.chk_type)
        AppCompatCheckBox chkType;
        @BindView(R.id.txt_quan_price)
        AppCompatTextView txtQuanPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chkType.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!onBind) {
                selectedPosition = getAdapterPosition();
                onAddOnChecked.quantityChecked(selectedPosition);
                Log.e("TAG", "QuantityAdapter:selectedPosition aFTER "+selectedPosition );
                notifyDataSetChanged();

            }
        }
    }
}
