package com.speant.user.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.speant.user.ui.HotelDetailActivity;
import com.speant.user.ui.HotelItemsDetailActivity;
import com.speant.user.Models.MenuPojo;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context mContext;
    List<MenuPojo.Menus> menusList = new ArrayList<>();
    String activityNameStr="";

    private static final String TAG = "MenuAdapter";

    public MenuAdapter(HotelDetailActivity mContext, List<MenuPojo.Menus> menusList) {
        this.menusList = menusList;
        this.mContext = mContext;
    }

    public MenuAdapter(HotelItemsDetailActivity activityHotelItemsDetail, List<MenuPojo.Menus> menusList, String itemDetail) {
        this.menusList = menusList;
        this.mContext = mContext;
        this.activityNameStr = itemDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.menuItemName.setText(menusList.get(position).getMenuName());
        holder.menuCountTxt.setText(""+menusList.get(position).getFoodCount());

        holder.menuItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityNameStr.equalsIgnoreCase("ItemDetail")){
                    ((HotelItemsDetailActivity)mContext).setCollapsing();
                }else {
                    ((HotelDetailActivity)mContext).setCollapsing();
                }

            }
        });
        Log.e(TAG, "onBindViewHolder: "+menusList.get(position).getMenuName() );

    }

    @Override
    public int getItemCount() {
        return menusList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menu_item_name)
        TextView menuItemName;
        @BindView(R.id.menu_count_txt)
        TextView menuCountTxt;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
