package com.speant.user.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.onAddOnChecked;
import com.speant.user.Models.AddOns;
import com.speant.user.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.ViewHolder> {
    Activity activity;
    List<AddOns> addOnsList;
    onAddOnChecked onAddOnChecked;
   SessionManager sessionManager;

    public AddOnAdapter(Activity activity, List<AddOns> addOnsList, onAddOnChecked onAddOnChecked) {
        this.activity = activity;
        this.addOnsList = addOnsList;
        this.onAddOnChecked = onAddOnChecked;
        Log.e("TAG", "AddOnAdapter:activity "+activity );
        sessionManager = new SessionManager(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtAddonName.setText(addOnsList.get(position).getName());
        holder.txtAddonPrice.setText(sessionManager.getCurrency()+" "+addOnsList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return addOnsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.chk_addon)
        AppCompatCheckBox chkAddon;
        @BindView(R.id.txt_addon_name)
        AppCompatTextView txtAddonName;
        @BindView(R.id.txt_addon_price)
        AppCompatTextView txtAddonPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chkAddon.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onAddOnChecked.addOnChecked(getAdapterPosition(), isChecked);
        }
    }
}
