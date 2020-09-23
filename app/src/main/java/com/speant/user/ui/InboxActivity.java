package com.speant.user.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.ChatHistory;
import com.speant.user.Models.ChatList;
import com.speant.user.R;
import com.speant.user.ui.adapter.InboxAdapter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.SessionManager.KEY_USER_ID;


public class InboxActivity extends AppCompatActivity {


    @BindView(R.id.et_send_message)
    AppCompatEditText etSendMessage;
    @BindView(R.id.iv_send)
    AppCompatImageView ivSend;
    @BindView(R.id.bottom_layout)
    RelativeLayout bottomLayout;

    public String TAG = getClass().getSimpleName();

    Animation myAnim;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.iv_back)
    AppCompatImageView ivBack;
    @BindView(R.id.top_lay)
    LinearLayout topLay;

    private WebSocketClient mWebSocketClient;


    ArrayList<ChatList> chatMessageArrayList = new ArrayList<>();
    private InboxAdapter adapter;

    ProgressDialog progressUtils;


    private SessionManager sessionManager;

    String deliverBoyId;

    String orderId, from_type;

    private APIInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        ButterKnife.bind(this);

        progressUtils = new ProgressDialog(this);
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        ivSend.setAnimation(myAnim);


        sessionManager = new SessionManager(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);


        chatMessageArrayList.clear();

        if (getIntent() != null) {
            Intent intent = getIntent();

            deliverBoyId = intent.getStringExtra(Global.DELIVERYBOY_ID);
            orderId = intent.getStringExtra(Global.ORDER_ID);
            from_type = intent.getStringExtra(Global.FROM_TYPE);
        }

        Log.e(TAG, "onCreate: " + from_type);


        if (orderId != null) {

            if (from_type.equalsIgnoreCase("DeliveryBoy")) {
                callChatApi("1");

            } else {

                callChatApi("2");
            }
        }


        connectWebSocket();


    }

    private void callChatApi(String type) {

        Call<ChatHistory> call = apiInterface.getChat(orderId, type);

        call.enqueue(new Callback<ChatHistory>() {
            @Override
            public void onResponse(Call<ChatHistory> call, Response<ChatHistory> response) {

                if (response.isSuccessful()) {

                    Log.e(TAG, "onResponse:ChatDetails " + response);
                    showHistoryChat(response.body());

                    if(response.body().getData().size() > 0) {
                        if (deliverBoyId == null || deliverBoyId.isEmpty()) {
                            if (response.body().getData().get(0).getFrom_id().equals(sessionManager.getSocketUniqueId())) {
                                // If the current user is the sender of the message
                                deliverBoyId = response.body().getData().get(0).getProvider_id();
                            } else if (response.body().getData().get(0).getTo_id().equals(sessionManager.getSocketUniqueId())) {
                                deliverBoyId = response.body().getData().get(0).getUser_id();
                            }
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ChatHistory> call, Throwable t) {

            }
        });


    }

    private void sendMessage() {
        JSONObject messageObj = new JSONObject();
        final ChatList chatMessage = new ChatList();


        try {

            messageObj.put("type", "message");

            messageObj.put("message", etSendMessage.getText().toString());
            messageObj.put("from_id", "User_" + sessionManager.getUserDetails().get(KEY_USER_ID));
            messageObj.put("user_id", sessionManager.getUserDetails().get(KEY_USER_ID));
            messageObj.put("request_id", orderId);

            if (from_type.equalsIgnoreCase("DeliveryBoy")) {
                messageObj.put("to_id", "Provider_" + deliverBoyId);
                messageObj.put("provider_id", deliverBoyId);
                messageObj.put("provider_type", 1);
            } else {

                messageObj.put("to_id", "Admin_" + "1");
                messageObj.put("provider_id", 1);
                messageObj.put("provider_type", 2);

            }


            mWebSocketClient.send(String.valueOf(messageObj));


            chatMessage.setFrom_id("User_" + sessionManager.getUserDetails().get(KEY_USER_ID));
            chatMessage.setMessage(etSendMessage.getText().toString());

            chatMessage.setUser_id(sessionManager.getUserDetails().get(KEY_USER_ID));
            chatMessage.setRequest_id(orderId);
            chatMessage.setType("message");


            if (from_type.equalsIgnoreCase("DeliveryBoy")) {
                chatMessage.setProvider_id(String.valueOf(deliverBoyId));
                chatMessage.setTo_id("Provider_" + deliverBoyId);
            } else {
                chatMessage.setProvider_id("1");
                chatMessage.setTo_id("Admin_" + "1");
            }


            chatMessageArrayList.add(chatMessage);
            showChat(chatMessageArrayList);


            etSendMessage.setText(null);


            Log.e(TAG, "onClick: " + messageObj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void connectWebSocket() {


        Log.e(TAG, "connectWebSocket:is true ");

        String url = Global.SOCKET_URL;

        URI uri;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "connectWebSocket: " + e.toString());
            return;
        }


        mWebSocketClient = new

                WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.i("Websocket", "Opened");

                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("type", "init");
                            jsonObject.put("id", "User_" + sessionManager.getUserDetails().get(KEY_USER_ID));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mWebSocketClient.send(String.valueOf(jsonObject));

                        sessionManager.putSocketUinqueId("User_" + sessionManager.getUserDetails().get(KEY_USER_ID));

                    }

                    @Override
                    public void onMessage(String s) {
                        final String message = s;
                        final ChatList chatMessage = new ChatList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Log.e(TAG, "run:getConnection " + mWebSocketClient.getConnection());
                                Log.e(TAG, "run:getReadyState " + mWebSocketClient.getReadyState());


                                Log.e(TAG, "run:Receive  " + message);

                                try {

                                    JSONObject jsonObject = new JSONObject(message);
                                    chatMessage.setFrom_id(jsonObject.optString("from_id"));
                                    chatMessage.setMessage(jsonObject.optString("message"));
                                    chatMessage.setProvider_id(jsonObject.optString("provider_id"));
                                    chatMessage.setUser_id(jsonObject.optString("user_id"));
                                    chatMessage.setRequest_id(jsonObject.optString("request_id"));
                                    chatMessage.setType(jsonObject.optString("type"));
                                    chatMessage.setTo_id(jsonObject.optString("to_id"));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                chatMessageArrayList.add(chatMessage);
                                Log.e(TAG, "run:CheckUser " + chatMessageArrayList);
                                Log.e(TAG, "sendMessage:afterSend " + chatMessageArrayList.size());

                                showChat(chatMessageArrayList);


                            }
                        });
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.i("Websocket", "Closed " + s);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Websocket", "Error " + e.getMessage());
                    }
                };


        mWebSocketClient.connect();


    }

    @Override
    protected void onResume() {
        super.onResume();
        connectWebSocket();
    }

    @SuppressLint("WrongConstant")
    private void showChat(ArrayList<ChatList> chatMessageArrayList) {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        adapter = new InboxAdapter(this, chatMessageArrayList);
        rvChat.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    public void showHistoryChat(ChatHistory details) {


        if (details.getData().size() > 0) {


            for (int i = 0; i < details.getData().size(); i++) {
                ChatList chatList = new ChatList();

                chatList.setFrom_id(details.getData().get(i).getFrom_id());
                chatList.setMessage(details.getData().get(i).getMessage());
                chatList.setProvider_id(details.getData().get(i).getProvider_id());
                chatList.setUser_id(details.getData().get(i).getUser_id());
                chatList.setRequest_id(details.getData().get(i).getRequest_id());
                chatList.setType(details.getData().get(i).getType());
                chatList.setTo_id(details.getData().get(i).getTo_id());

                chatMessageArrayList.add(chatList);


            }

            Log.e(TAG, "showHistoryChat: " + chatMessageArrayList.size());

            showChat(chatMessageArrayList);
        }


    }

    @OnClick({R.id.iv_back, R.id.iv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:
                if (etSendMessage.getText().toString().length() > 0 && etSendMessage.getText().toString().trim().matches("")) {

                    Toast.makeText(this, "Empty spaces are not allowed!!!", Toast.LENGTH_SHORT).show();
                    etSendMessage.setText(null);


                } else if (etSendMessage.getText().toString().isEmpty()) {

                    Toast.makeText(this, "Please Type Message Here!!!", Toast.LENGTH_SHORT).show();


                } else {

                    view.startAnimation(myAnim);


                    if (mWebSocketClient.getReadyState().toString().equals("OPEN")) {

                        if (from_type.equalsIgnoreCase("DeliveryBoy")) {
                            if (deliverBoyId != null && orderId != null) {

                                sendMessage();
                            }
                        } else {
                            if (orderId != null) {

                                sendMessage();
                            }
                        }


                    } else {

                        Toast.makeText(this, "Connection Not Established!!!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }
}
