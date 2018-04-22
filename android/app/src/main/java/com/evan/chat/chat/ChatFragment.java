package com.evan.chat.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.evan.chat.R;
import com.evan.chat.data.source.model.Chat;
import com.evan.chat.view.ScrollChildSwipeRefreshLayout;
import com.evan.chat.view.DialogView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/13
 * Time: 22:30
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter presenter;

    private ChatListAdapter mChatListAdapter;

    public ChatFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatListAdapter = new ChatListAdapter(new ArrayList<Chat>(0));
    }

    static ChatFragment newInstance(){
        return new ChatFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_frag,container,false);

        ListView listView = root.findViewById(R.id.chats_list);
        listView.setAdapter(mChatListAdapter);

        final EditText sendContext = root.findViewById(R.id.chat);

        Button sendButton = root.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sendContext.getText().toString().trim().equals("") && presenter.send(sendContext.getText().toString()))
                    sendContext.setText("");
            }
        });

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //todo
            }
        });
//        swipeRefreshLayout.setScrollUpChild();
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void addDialog(Chat chat) {
        mChatListAdapter.addData(chat);
    }

    private static class ChatListAdapter extends BaseAdapter {

        private List<Chat>mChats;

        ChatListAdapter(List<Chat>chats){
            setList(chats);
        }

        void addData(Chat chat){
            mChats.add(chat);
            notifyDataSetChanged();
        }

        public void replaceData(List<Chat>chats){
            setList(chats);
            notifyDataSetChanged();
        }

        private void setList(List<Chat>chats){
            mChats = checkNotNull(chats);
        }

        @Override
        public int getCount() {
            return mChats.size();
        }

        @Override
        public Chat getItem(int position) {
            return mChats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DialogView rowView = (DialogView) convertView;
            final Chat chat = getItem(position);
            if (rowView == null){
                rowView = new DialogView(parent.getContext());
            }

            rowView.setUser(chat.getSender());
            rowView.setContent(chat.getContent());
            rowView.setUserHead(R.mipmap.logo);

            return rowView;
        }
    }
}
