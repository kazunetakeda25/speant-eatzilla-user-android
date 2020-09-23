package com.speant.user.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.speant.user.Common.callBacks.onCardSelect;
import com.speant.user.Models.CardData;
import com.speant.user.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardListAdapter extends RecyclerSwipeAdapter<CardListAdapter.ViewHolder> {
    private Activity activity;
    private boolean onBind;
    private int selectedPosition = -1;
    onCardSelect oncardSelect;
    List<CardData> cardDataList;

    public CardListAdapter(Activity activity, List<CardData> cardDataList, onCardSelect onCardSelect) {
        this.activity = activity;
        this.oncardSelect = onCardSelect;
        this.cardDataList = cardDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_adpater, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBind = true;
        holder.txtCardNum.setText("•••• •••• ••••  "+cardDataList.get(position).getLast_four());
        if (selectedPosition == position) {
            holder.chkCard.setChecked(true);
        } else {
            holder.chkCard.setChecked(false);
        }
        mItemManger.bindView(holder.itemView, position);
        onBind = false;

    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.lay_swipe;
    }

    public void setSelectedPosition() {
        for(int i=0;i<cardDataList.size();i++) {
            Log.e("TAG", "CardListAdapter:selectedPosition before"+i );
            if(cardDataList.get(i).getIs_default().equals("1")) {
                Log.e("TAG", "CardListAdapter:selectedPosition "+i );
                selectedPosition = i;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.lay_linear_delete)
        LinearLayout layLinearDelete;
        @BindView(R.id.txt_card_num)
        AppCompatTextView txtCardNum;
        @BindView(R.id.img_brand)
        AppCompatImageView imgBrand;
        @BindView(R.id.chk_brand_type)
        AppCompatCheckBox chkCard;
        @BindView(R.id.lay_swipe)
        SwipeLayout laySwipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chkCard.setOnCheckedChangeListener(this);

            laySwipe.setShowMode(SwipeLayout.ShowMode.LayDown);
            laySwipe.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    int position = getAdapterPosition();
                    String card_id = cardDataList.get(position).getId();
                    oncardSelect.onCardDelete(position, card_id);
                    layout.close();
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.e("TAG", "onCheckedChanged:" + selectedPosition);
            if (!onBind) {
                selectedPosition = getAdapterPosition();
                Log.e("TAG", "onCheckedChanged:Adapter " + selectedPosition);
                Log.e("TAG", "onCheckedChanged:Adapter " + getAdapterPosition());
                notifyDataSetChanged();
                oncardSelect.onCardSelected(selectedPosition);
            }
        }


    }
}
