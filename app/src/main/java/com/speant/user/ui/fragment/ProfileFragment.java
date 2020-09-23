package com.speant.user.ui.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.FragmentDismissCallBack;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.ProfileUpdateResponse;
import com.speant.user.R;
import com.speant.user.ui.AboutUsActivity;
import com.speant.user.ui.CardListActivity;
import com.speant.user.ui.FaqActivity;
import com.speant.user.ui.FavouritesActivity;
import com.speant.user.ui.HelpActivity;
import com.speant.user.ui.HistoryActivity;
import com.speant.user.ui.HomeActivity;
import com.speant.user.ui.OffersActivity;
import com.speant.user.ui.RechargeWalletActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.speant.user.Common.SessionManager.KEY_USER_EMAIL;
import static com.speant.user.Common.SessionManager.KEY_USER_MOBILE;
import static com.speant.user.Common.SessionManager.KEY_USER_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends androidx.fragment.app.Fragment implements FragmentDismissCallBack, LoginSuccessCallBack {

    @BindView(R.id.prof_edit_txt)
    TextView profEditTxt;
    @BindView(R.id.prof_user_name_txt)
    TextView profUserNameTxt;
    @BindView(R.id.prof_user_mbl_txt)
    TextView profUserMblTxt;
    @BindView(R.id.prof_manage_addr_rel)
    RelativeLayout profManageAddrRel;
    @BindView(R.id.prof_paymrnt_rel)
    RelativeLayout profPaymrntRel;
    @BindView(R.id.prof_your_order_rel)
    RelativeLayout profYourOrderRel;
    @BindView(R.id.prof_fav_rel)
    RelativeLayout profFavRel;
    @BindView(R.id.prof_referal_rel)
    RelativeLayout profReferalRel;
    @BindView(R.id.prof_offers_rel)
    RelativeLayout profOffersRel;
    @BindView(R.id.prof_help_rel)
    RelativeLayout profHelpRel;
    @BindView(R.id.prof_logout_rel)
    RelativeLayout profLogoutRel;
    Unbinder unbinder;
    ProfileEditFragment profileEditFragment;
    Activity context;
    @BindView(R.id.prof_about)
    RelativeLayout profAbout;
    @BindView(R.id.prof_faq)
    RelativeLayout profFaq;
    @BindView(R.id.edt_profile_lay)
    RelativeLayout edtProfileLay;
    HashMap<String, String> userDetails;
    @BindView(R.id.prof_user_email_txt)
    TextView profUserEmailTxt;
    @BindView(R.id.prof_your_fav_rel)
    RelativeLayout profYourFavRel;
    @BindView(R.id.prof_wallet_rel)
    RelativeLayout profWalletRel;
    @BindView(R.id.lay_profile_det)
    LinearLayout layProfileDet;
    @BindView(R.id.btn_login_txt)
    AppCompatTextView btnLoginTxt;
    @BindView(R.id.lay_login)
    LinearLayout layLogin;
    private AlertDialog alertDialog;

    SessionManager sessionManager;
    APIInterface apiInterface;
    private LoginBottomFragment loginBottomFragment;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDetails();
    }

    private void setDetails() {
        if (sessionManager.isLoggedIn()) {
            layProfileDet.setVisibility(View.VISIBLE);
            layLogin.setVisibility(View.GONE);
            userDetails = sessionManager.getUserDetails();
            if (userDetails.get(KEY_USER_NAME).isEmpty()) {
                profUserNameTxt.setText(getString(R.string.guest_user));
            } else {
                Log.e("Giri ", "setDetails:KEY_USER_NAME " + userDetails.get(KEY_USER_NAME));
                profUserNameTxt.setText(userDetails.get(KEY_USER_NAME));
            }
            Log.e("Giri ", "setDetails:KEY_USER_MOBILE " + userDetails.get(KEY_USER_MOBILE));
            Log.e("Giri ", "setDetails:KEY_USER_EMAIL " + userDetails.get(KEY_USER_EMAIL));
            profUserMblTxt.setText(userDetails.get(KEY_USER_MOBILE));
            profUserEmailTxt.setText(userDetails.get(KEY_USER_EMAIL));
        /*Picasso
                .get()
                .load(userDetails.get(KEY_USER_IMAGE))
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(userProfImg);*/
        } else {
            layProfileDet.setVisibility(View.GONE);
            layLogin.setVisibility(View.VISIBLE);
        }
    }

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }


    @OnClick({R.id.user_prof_img, R.id.prof_edit_txt, R.id.prof_manage_addr_rel, R.id.prof_paymrnt_rel,
            R.id.prof_your_order_rel, R.id.prof_fav_rel, R.id.prof_referal_rel, R.id.prof_offers_rel,
            R.id.prof_help_rel, R.id.prof_logout_rel, R.id.prof_about, R.id.prof_faq, R.id.edt_profile_lay,
            R.id.prof_your_fav_rel, R.id.prof_wallet_rel, R.id.btn_login_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_prof_img:
                alertEditProfile();
                break;
            case R.id.prof_edit_txt:
                profileEditFragment = new ProfileEditFragment(context, this);
                profileEditFragment.showNow(getChildFragmentManager(), profileEditFragment.getTag());
                break;
            case R.id.edt_profile_lay:
//                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                break;
            case R.id.prof_manage_addr_rel:
                break;
            case R.id.btn_login_txt:
                loginBottomFragment = new LoginBottomFragment(context, this::onDismissFragment);
                loginBottomFragment.showNow(getFragmentManager(), "loginFragment");
                break;
            case R.id.prof_paymrnt_rel:
               /* startActivity(new Intent(getActivity(), AddPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), CardListActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    setSnackBar();
                }

                break;
            case R.id.prof_your_order_rel:
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), HistoryActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    setSnackBar();
                }
                break;
            case R.id.prof_wallet_rel:
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), RechargeWalletActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    setSnackBar();
                }
                break;
            case R.id.prof_referal_rel:
                if (sessionManager.isLoggedIn()) {
                    alertReferal();
                } else {
                    setSnackBar();
                }
                break;
            case R.id.prof_offers_rel:
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), OffersActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    setSnackBar();
                }
                break;
            case R.id.prof_your_fav_rel:
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), FavouritesActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    setSnackBar();
                }
                break;
            case R.id.prof_about:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.prof_faq:
                startActivity(new Intent(getActivity(), FaqActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.prof_help_rel:
//                alertHelp();
                startActivity(new Intent(getActivity(), HelpActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.prof_logout_rel:
                if (sessionManager.isLoggedIn()) {
                    alertLogout();
                } else {
                    setSnackBar();
                }
                break;
        }
    }


    public void setSnackBar() {
        //getDecorView() uses rootview to display the snackbar
        Snackbar snackbar = Snackbar.make(context.getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.login_to_view), Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();
    }


    public void alertReferal() {

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogSlideAnim_leftright_Fullscreen);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.dialogue_referal, null);
        // Set above view in alert dialog.
        builder.setView(view);

        ImageView refer_back_img = view.findViewById(R.id.refer_back_img);
        Button refer_invite_btn = view.findViewById(R.id.invite_btn);


        refer_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        refer_invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Boga");
                i.putExtra(Intent.EXTRA_TEXT, "Hi, Download the Boga App today! Download App from here -\" + \"https://google.com/\"");
                startActivity(Intent.createChooser(i, "Share via"));
            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void alertEditProfile() {

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.btm_sheet_edit_prof, null);
        // Set above view in alert dialog.
        builder.setView(view);

        View edit_view = view.findViewById(R.id.edit_view);

        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        builder.setCancelable(true);

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void alertHelp() {

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.dialogue_help, null);
        // Set above view in alert dialog.
        builder.setView(view);

        ImageView help_back_img = view.findViewById(R.id.help_back_img);
        RelativeLayout aboutUs = view.findViewById(R.id.about_us_lay);

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });

        help_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });


        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void alertLogout() {

        new AlertDialog.Builder(context)
                .setMessage(getString(R.string.are_you_want_to_logout))
                .setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("HomeActivity", "Signout");
                        ((HomeActivity) getActivity()).logout();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("HomeActivity", "Aborting mission...");
                    }
                })
                .show();

    }

    @Override
    public void dismiss(ProfileUpdateResponse.UpdateData updatedData) {
        Log.e("Giri ", "onDismiss:ProfileFragmentDialog");
        Log.e("Giri ", "setDetails:KEY_USER_NAME " + userDetails.get(KEY_USER_NAME));
        profileEditFragment.dismiss();
        profUserNameTxt.setText(updatedData.getName());
        profUserMblTxt.setText(updatedData.getPhone());
        profUserEmailTxt.setText(updatedData.getEmail());
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
        setDetails();
    }
}
