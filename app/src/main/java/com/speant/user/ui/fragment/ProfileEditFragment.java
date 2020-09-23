package com.speant.user.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.FragmentDismissCallBack;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.ProfileUpdateResponse;
import com.speant.user.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.SessionManager.KEY_USER_EMAIL;
import static com.speant.user.Common.SessionManager.KEY_USER_MOBILE;
import static com.speant.user.Common.SessionManager.KEY_USER_NAME;
import static com.speant.user.Common.SessionManager.KEY_USER_PASSWORD;
import static com.speant.user.Common.SessionManager.KEY_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEditFragment extends BottomSheetDialogFragment {
    private APIInterface apiInterface;
    @BindView(R.id.edt_phone)
    AppCompatEditText edtPhone;
    @BindView(R.id.edt_email)
    AppCompatEditText edtEmail;
    @BindView(R.id.btn_update)
    AppCompatTextView btnUpdate;
    Unbinder unbinder;
    HashMap<String, String> userDetails;
    @BindView(R.id.edt_username)
    AppCompatEditText edtUsername;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    SessionManager sessionManager;
    FragmentDismissCallBack fragmentDismissCallBack;
    Context context;
    public ProfileEditFragment() {

    }

    @SuppressLint("ValidFragment")
    public ProfileEditFragment(Context context, FragmentDismissCallBack fragmentDismissCallBack) {
        this.fragmentDismissCallBack = fragmentDismissCallBack;
        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getUserDetails();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.context = context;
    }

    private void init(HashMap<String, String> userDetails) {
        if (userDetails.get(KEY_USER_NAME).isEmpty()) {
            edtUsername.setText("");
        } else {
            edtUsername.setText(userDetails.get(KEY_USER_NAME));
        }
        edtPhone.setText(userDetails.get(KEY_USER_MOBILE));
        edtEmail.setText(userDetails.get(KEY_USER_EMAIL));
        edtPwd.setText(userDetails.get(KEY_USER_PASSWORD));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        unbinder = ButterKnife.bind(this, view);
        init(userDetails);
        return view;
    }


    @OnClick(R.id.btn_update)
    public void onViewClicked() {
        if (validate()) {
            updateProfile();
        }

    }

    private boolean validate() {
        if (edtPwd.getText().toString().isEmpty()) {
            CommonFunctions.shortToast(getActivity(), "Enter Password");
            return false;
        }else if (edtPwd.getText().toString().length() < 8) {
            CommonFunctions.shortToast(getActivity(), "Minimum Password Length should be 8");
            return false;
        } else if (edtUsername.getText().toString().trim().isEmpty()) {
            CommonFunctions.shortToast(getActivity(), "Enter Username");
            return false;
        } else if (edtEmail.getText().toString().isEmpty()) {
            CommonFunctions.shortToast(getActivity(), "Enter Email");
            return false;
        } else if (edtPhone.getText().toString().isEmpty()) {
            CommonFunctions.shortToast(getActivity(), "Enter Mobile Number");
            return false;
        }else if (!CommonFunctions.isValidEmail(edtEmail.getText().toString().trim())) {
            CommonFunctions.shortToast(getActivity(), "Enter Valid Email");
            return false;
        }
        return true;
    }

    private void updateProfile() {

        HashMap<String, String> map = new HashMap<>();
        map.put(CONST.Params.password, edtPwd.getText().toString().trim());
        map.put(CONST.Params.id, userDetails.get(KEY_USER_ID));
        map.put(CONST.Params.email, edtEmail.getText().toString().trim());
        map.put(CONST.Params.name, edtUsername.getText().toString().trim());
        map.put(CONST.Params.profile_image, " ");
        Call<ProfileUpdateResponse> call = apiInterface.editProfile(map);
        call.enqueue(new Callback<ProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
                if(response.code() == 200) {
                    ProfileUpdateResponse.UpdateData lo = response.body().getData();
                    Log.e("Giri ", "onResponse:getUserName " + lo.getName());
                    Log.e("Giri ", "onResponse:getPhone " + lo.getEmail());
                    Log.e("Giri ", "onResponse:getEmail " + lo.getPhone());
                    Log.e("Giri ", "onResponse:password " + edtPwd.getText().toString().trim());
                    sessionManager.createLoginSession(lo.getId(), lo.getAuthToken(), lo.getName(),
                            lo.getEmail(), lo.getPhone(), lo.getProfile_image(), edtPwd.getText().toString().trim());

                    CommonFunctions.shortToast(getActivity(), "Profile Updated  Successfully");
                    fragmentDismissCallBack.dismiss(lo);
                }else if(response.code() == 401){
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
                Log.e("Giri ", "onFailure: EditProfile Failure" + t);
            }
        });

    }
}
