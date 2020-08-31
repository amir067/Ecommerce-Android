package com.example.android.ecommerce;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.ecommerce.adapters.ChatRecyclerViewAdapter;
import com.example.android.ecommerce.viewmodel.ChatViewModel;

public class ChatFragment extends Fragment implements View.OnClickListener {
    public static final String SENDER_TOKEN_KEY = "sender_token";
    public static final String RECEIVER_TOKEN_KEY = "receiver_token";
    public static final String PRODUCT_ID_KEY = "product_id";

    private String senderToken;
    private String receiverToken;
    private String pid;

    private EditText msgEditText;
    private ChatViewModel chatViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        senderToken = args.getString(SENDER_TOKEN_KEY);
        receiverToken = args.getString(RECEIVER_TOKEN_KEY);
        pid = args.getString(PRODUCT_ID_KEY);

        RecyclerView chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(senderToken);
        chatRecyclerView.setAdapter(adapter);
        msgEditText = view.findViewById(R.id.msgEditText);
        view.findViewById(R.id.sendMsgBtn).setOnClickListener(this);

        chatViewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(ChatViewModel.class);

        chatViewModel.getChats(senderToken, receiverToken, pid).observe(getViewLifecycleOwner(), adapter::setItems);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMsgBtn:
                sendMsg();
                break;
        }
    }

    public void sendMsg() {
        String msg = msgEditText.getText().toString();
        chatViewModel.sendMsg(senderToken, receiverToken, pid, msg);
        msgEditText.setText("");
    }
}