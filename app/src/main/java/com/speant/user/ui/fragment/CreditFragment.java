package com.speant.user.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.UpdateCreditCallBack;
import com.speant.user.Common.customCarosuel.CarouselPagerAdapter;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.CreditStatusResponse;
import com.speant.user.Models.CreditUpdateResponse;
import com.speant.user.Models.UpdateTokenResponse;
import com.speant.user.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CommonFunctions.removeProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends Fragment implements UpdateCreditCallBack {


    @BindView(R.id.webview)
    WebView plaidLinkWebview;
    @BindView(R.id.myviewpager)
    public ViewPager pager;
    @BindView(R.id.txt_credit_total)
    AppCompatTextView txtCreditTotal;
    @BindView(R.id.txt_credit_status)
    AppCompatTextView txtCreditStatus;
    @BindView(R.id.lay_credit)
    LinearLayout layCredit;
    @BindView(R.id.txt_change_account)
    AppCompatTextView txtChangeAccount;

    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private String PLAID_CLIENT_ID = "5c63ddce08ed150010a9ba61";
    private String PLAID_SECRET = "0be6d5b19ba2482b79925503084fb7";
    private String PLAID_PUBLIC_KEY = "ba3695619012375d9ea588181e8ed0";
    private String accessToken, accountId;
    private Activity activity;
    private CarouselPagerAdapter adapter;
    public final static int LOOPS = 1000;
    public static int count = 3; //ViewPager items size
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 3;

    public CreditFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new CreditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit, container, false);
        sessionManager = new SessionManager(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        activity = getActivity();
        ButterKnife.bind(this, view);
        getCreditStatus();
        return view;
    }

    private void getCreditStatus() {
        CommonFunctions.showSimpleProgressDialog(activity, "Updating", false);
        Call<CreditStatusResponse> call = apiInterface.getcreditstate(sessionManager.getHeader());
        call.enqueue(new Callback<CreditStatusResponse>() {
            @Override
            public void onResponse(Call<CreditStatusResponse> call, Response<CreditStatusResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        layCredit.setVisibility(View.VISIBLE);
                        plaidLinkWebview.setVisibility(View.GONE);
                        Log.e("TAG", "onResponse: getCreditStatus" + response.body().getDetails().getAmount());
                        initViewPager();
                        if (response.body().getDetails().getAmount() != null) {
                            txtCreditTotal.setText("Credit Amount : " + sessionManager.getCurrency() + " " + response.body().getDetails().getAmount());
                            txtCreditStatus.setText("Credit Status : " + getCreditStatusValue(response.body().getDetails().getStatus()));
                        }
                    } else {
                        layCredit.setVisibility(View.GONE);
                        plaidLinkWebview.setVisibility(View.VISIBLE);
                        initKey();
                    }
                }
                removeProgressDialog();

            }

            @Override
            public void onFailure(Call<CreditStatusResponse> call, Throwable t) {
                removeProgressDialog();
            }
        });
    }


    private String getCreditStatusValue(String status) {
        switch (status) {
            case "0":
                return "Approval Pending";
            case "1":
                return "Request Approved";
            case "2":
                return "Request Declined";
        }
        return null;
    }

    //-------------------carousel view

    private void initViewPager() {

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 3) * 2);
        pager.setPageMargin(-pageMargin);

        adapter = new CarouselPagerAdapter(activity, getFragmentManager(), this, this::onCreditChange);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(3);
    }


    //--------------init card add---------------------------------
    private void intialise(String public_token) {
        // Use builder to create a client
        PlaidClient plaidClient = PlaidClient.newBuilder()
                .clientIdAndSecret(PLAID_CLIENT_ID, PLAID_SECRET)
                .publicKey(PLAID_PUBLIC_KEY) // optional. only needed to call endpoints that require a public key
                .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                .build();

        // Asynchronously do the same thing. Useful for potentially long-lived calls.
        plaidClient.service()
                .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(public_token))
                .enqueue(new Callback<ItemPublicTokenExchangeResponse>() {
                    @Override
                    public void onResponse(Call<ItemPublicTokenExchangeResponse> call, Response<ItemPublicTokenExchangeResponse> response) {
                        if (response.isSuccessful()) {
                            accessToken = response.body().getAccessToken();
                            Log.e("TAG", "onResponse:accessToken " + accessToken);
                            updateAccessToken();
                        }
                    }

                    @Override
                    public void onFailure(Call<ItemPublicTokenExchangeResponse> call, Throwable t) {
                        // handle the failure as needed
                    }
                });


    }

    private void updateAccessToken() {
        Call<UpdateTokenResponse> call = apiInterface.updateToken(sessionManager.getHeader(), accessToken, accountId);
        call.enqueue(new Callback<UpdateTokenResponse>() {
            @Override
            public void onResponse(Call<UpdateTokenResponse> call, Response<UpdateTokenResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        getCreditStatus();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateTokenResponse> call, Throwable t) {

            }
        });
    }


    private void initKey() {
        // Initialize Link
        HashMap<String, String> linkInitializeOptions = new HashMap<String, String>();
        linkInitializeOptions.put("key", PLAID_PUBLIC_KEY);
        linkInitializeOptions.put("product", "auth,transactions,assets");
        linkInitializeOptions.put("apiVersion", "v2"); // set this to "v1" if using the legacy Plaid API
        linkInitializeOptions.put("env", "sandbox");
        linkInitializeOptions.put("clientName", "Test App");
        linkInitializeOptions.put("selectAccount", "true");
        linkInitializeOptions.put("webhook", "http://requestb.in");
        linkInitializeOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html");

        // Generate the Link initialization URL based off of the configuration options.
        final Uri linkInitializationUrl = generateLinkInitializationUrl(linkInitializeOptions);

        // Modify Webview settings - all of these settings may not be applicable
        // or necessary for your integration.

        WebSettings webSettings = plaidLinkWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebView.setWebContentsDebuggingEnabled(true);

        // Initialize Link by loading the Link initialization URL in the Webview
        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());

        // Override the Webview's handler for redirects
        // Link communicates success and failure (analogous to the web's onSuccess and onExit
        // callbacks) via redirects.
        plaidLinkWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Parse the URL to determine if it's a special Plaid Link redirect or a request
                // for a standard URL (typically a forgotten password or account not setup link).
                // Handle Plaid Link redirects and open traditional pages directly in the  user's
                // preferred browser.
                Uri parsedUri = Uri.parse(url);
                if (parsedUri.getScheme().equals("plaidlink")) {
                    String action = parsedUri.getHost();
                    HashMap<String, String> linkData = parseLinkUriData(parsedUri);

                    if (action.equals("connected")) {
                        // User successfully linked
                        Log.e("Public token: ", linkData.get("public_token"));
                        Log.e("Account ID: ", linkData.get("account_id"));
                        Log.e("Account name: ", linkData.get("account_name"));
                        Log.e("Institution id: ", linkData.get("institution_id"));
                        Log.e("Institution name: ", linkData.get("institution_name"));
                        accountId = linkData.get("account_id");
                        intialise(linkData.get("public_token"));
                        // Reload Link in the Webview
                        // You will likely want to transition the view at this point.
                        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());
                    } else if (action.equals("exit")) {
                        // User exited
                        // linkData may contain information about the user's status in the Link flow,
                        // the institution selected, information about any error encountered,
                        // and relevant API request IDs.
                        // Log.e("User status in flow: ", linkData.get("status"));
                        // The request ID keys may or may not exist depending on when the user exited
                        // the Link flow.
                        // Log.e("Link request ID: ", linkData.get("link_request_id"));
                        // Log.e("API request ID: ", linkData.get("plaid_api_request_id"));
                        // Reload Link in the Webview
                        // You will likely want to transition the view at this point.

//                        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());
                        getCreditStatus();

                    } else if (action.equals("event")) {
                        // The event action is fired as the user moves through the Link flow
                        Log.e("Event name: ", linkData.get("event_name"));
                    } else {
                        Log.e("Link action detected: ", action);
                    }
                    // Override URL loading
                    return true;
                } else if (parsedUri.getScheme().equals("https") ||
                        parsedUri.getScheme().equals("http")) {
                    // Open in browser - this is most  typically for 'account locked' or
                    // 'forgotten password' redirects
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    // Override URL loading
                    return true;
                } else {
                    // Unknown case - do not override URL loading
                    return false;
                }
            }
        });
    }

    // Generate a Link initialization URL based on a set of configuration options
    public Uri generateLinkInitializationUrl(HashMap<String, String> linkOptions) {
        Uri.Builder builder = Uri.parse(linkOptions.get("baseUrl"))
                .buildUpon()
                .appendQueryParameter("isWebview", "true")
                .appendQueryParameter("isMobile", "true");
        for (String key : linkOptions.keySet()) {
            if (!key.equals("baseUrl")) {
                builder.appendQueryParameter(key, linkOptions.get(key));
            }
        }
        return builder.build();
    }

    // Parse a Link redirect URL querystring into a HashMap for easy manipulation and access
    public HashMap<String, String> parseLinkUriData(Uri linkUri) {
        HashMap<String, String> linkData = new HashMap<String, String>();
        for (String key : linkUri.getQueryParameterNames()) {
            linkData.put(key, linkUri.getQueryParameter(key));
        }
        return linkData;
    }

    @Override
    public void onCreditChange(String amount) {
        Call<CreditUpdateResponse> call = apiInterface.updateCredit(sessionManager.getHeader(), amount);
        call.enqueue(new Callback<CreditUpdateResponse>() {
            @Override
            public void onResponse(Call<CreditUpdateResponse> call, Response<CreditUpdateResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        getCreditStatus();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreditUpdateResponse> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.txt_credit_total, R.id.txt_credit_status, R.id.txt_change_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_change_account:
                layCredit.setVisibility(View.GONE);
                plaidLinkWebview.setVisibility(View.VISIBLE);
                initKey();
                break;
        }
    }
}
