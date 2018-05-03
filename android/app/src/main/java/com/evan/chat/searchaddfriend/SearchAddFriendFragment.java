package com.evan.chat.searchaddfriend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.evan.chat.R;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.view.CircleImageView;
import com.evan.chat.view.ScrollChildSwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/26
 * Time: 13:22
 */
public class SearchAddFriendFragment extends Fragment implements SearchAddFriendContract.View {

    private SearchAddFriendContract.Presenter presenter;
    private FriendsAdapter mAdapter;

    public SearchAddFriendFragment(){

    }

    public static SearchAddFriendFragment newInstance(){
        return new SearchAddFriendFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FriendsAdapter(new ArrayList<Friend>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchfriend_frag, container, false);

        ListView listView = view.findViewById(R.id.user_list);
        listView.setAdapter(mAdapter);

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setScrollUpChild(listView);
        swipeRefreshLayout.setEnabled(false);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void setPresenter(SearchAddFriendContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null)return;
        final SwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);
        //确保setRefreshing（）在布局完成后再调用。
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFriends(List<Friend> friends) {
        mAdapter.replaceData(friends);
    }

    @Override
    public void addFriendSuccess() {
        getActivity().finish();
    }

    @Override
    public File getFile() {
        File filesDir = Objects.requireNonNull(getActivity()).getFilesDir();

        if (filesDir.exists()) {
            return filesDir;
        } else {
            filesDir.mkdirs();
            return filesDir;
        }
    }

    private ListItemListener mItemListener = new ListItemListener() {

        @Override
        public void onAddClick(Friend friend) {
            presenter.addFriend(friend);
        }
    };

    private static class FriendsAdapter extends BaseAdapter {

        private List<Friend> friends;
        private ListItemListener mItemListener;

        public FriendsAdapter(List<Friend>friends, ListItemListener mItemListener){
            this.mItemListener = mItemListener;
            setList(friends);
        }

        public void replaceData(List<Friend> friends){
            setList(friends);
            notifyDataSetChanged();
        }

        private void setList(List<Friend> friends){
            this.friends = friends;
        }

        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Friend getItem(int position) {
            return friends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Friend friend = getItem(position);
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.add_friend_item,parent,false);
            }
            Button addButton = convertView.findViewById(R.id.add);
            TextView account = convertView.findViewById(R.id.nickname);
            TextView profile = convertView.findViewById(R.id.profile);
            CircleImageView head = convertView.findViewById(R.id.user_head);

            head.setImageBitmap(friend.getHead());
            account.setText(friend.getAccount());
            profile.setText(friend.getProfile());
            if ("NO".equals(friend.getRelationship())){
                addButton.setVisibility(View.GONE);
            }else {
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemListener.onAddClick(friend);
                    }
                });
            }
            return convertView;
        }
    }

    interface ListItemListener{
        void onAddClick(Friend friend);
    }
}
