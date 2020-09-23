package com.speant.user.ui.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.user.Models.OfferResponse;
import com.speant.user.R;
import com.speant.user.ui.dialogs.OffersBottomFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {
    Activity activity;
    List<OfferResponse.PromoList> promoList;

    public OffersAdapter(Activity activity, List<OfferResponse.PromoList> promoList) {
        this.activity = activity;
        this.promoList = promoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_offer_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtCoupCode.setText(promoList.get(i).getCode());
        viewHolder.txtCoupTitle.setText(promoList.get(i).getTitle());
        viewHolder.txtCoupDesc.setText(promoList.get(i).getDescription());
    }


    @Override
    public int getItemCount() {
        return promoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_coup)
        AppCompatTextView txtCoup;
        @BindView(R.id.txt_coup_code)
        AppCompatTextView txtCoupCode;
        @BindView(R.id.txt_coup_title)
        AppCompatTextView txtCoupTitle;
        @BindView(R.id.txt_coup_desc)
        AppCompatTextView txtCoupDesc;
        @BindView(R.id.txt_moredet)
        AppCompatTextView txtMoredet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* HotelItemsFragment hotelItemsFragment = new HotelItemsFragment();
                    Bundle args = new Bundle();
                    args.putInt("categoryId", categoryId);
                    hotelItemsFragment.setArguments(args);*/

                    //implement bottomSheet in adapter in this format
                    OffersBottomFragment bottomSheetFragment = new OffersBottomFragment(promoList.get(getAdapterPosition()));
                    bottomSheetFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), bottomSheetFragment.getTag());
                }
            });
        }
    }
}
