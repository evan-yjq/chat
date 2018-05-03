package com.evan.chat.friends;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.*;
import com.evan.chat.PublicData;
import com.evan.chat.R;
import com.evan.chat.chat.ChatActivity;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.searchaddfriend.SearchAddFriendActivity;
import com.evan.chat.view.CircleImageView;
import com.evan.chat.view.ScrollChildSwipeRefreshLayout;

import java.io.File;
import java.util.*;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:50
 */
public class FriendsFragment extends Fragment implements FriendsContract.View {

    private FriendsContract.Presenter presenter;

    private FriendsExpandableListAdapter mExpandableListAdapter;

    private LinearLayout mNoFriendView;
    private LinearLayout mFriendsView;

    private ExpandableListView expandableListView;

    public FriendsFragment(){

    }

    public static FriendsFragment newInstance(){
        return new FriendsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExpandableListAdapter = new FriendsExpandableListAdapter(new ArrayList<Friend>(0),friendListItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friends_frag,container,false);

        mNoFriendView = root.findViewById(R.id.noFriends);
        mFriendsView = root.findViewById(R.id.friendsLL);
        expandableListView = root.findViewById(R.id.expandable_friends_list);
        expandableListView.setAdapter(mExpandableListAdapter);

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadFriends(true);
            }
        });

        swipeRefreshLayout.setScrollUpChild(expandableListView);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_display:
                showPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void showPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), Objects.requireNonNull(getActivity()).findViewById(R.id.menu_display));
        popup.getMenuInflater().inflate(R.menu.menu_add, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_friend:
                        showAddFriends();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null)return;
        final ScrollChildSwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showNoFriends() {
        mFriendsView.setVisibility(View.GONE);
        mNoFriendView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFriends(List<Friend> friends) {
        mExpandableListAdapter.replaceData(friends);
        mFriendsView.setVisibility(View.VISIBLE);
        mNoFriendView.setVisibility(View.GONE);
    }

    @Override
    public File getFile(){
        File filesDir = Objects.requireNonNull(getActivity()).getFilesDir();

        if (filesDir.exists()) {
            return filesDir;
        } else {
            filesDir.mkdirs();
            return filesDir;
        }
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(Objects.requireNonNull(getView()),msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddFriends() {
        Intent intent = new Intent(getContext(),SearchAddFriendActivity.class);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }

    private static class FriendsExpandableListAdapter extends BaseExpandableListAdapter{

        private Map<String,List<Friend>>mFriends;
        private List<String>classification;
        private FriendListItem friendListItem;

        public FriendsExpandableListAdapter(List<Friend>friends,FriendListItem friendListItem){
            mFriends = new HashMap<>();
            classification = new ArrayList<>();
            setFriends(friends);
            this.friendListItem = friendListItem;
        }

        public void replaceData(List<Friend>friends){
            mFriends.clear();
            classification.clear();
            setFriends(friends);
            notifyDataSetChanged();
        }

        private void setFriends(List<Friend>friends){
            for (Friend friend : friends) {
                String classify = friend.getClassification();
                if (classify == null) classify = "默认";
                List<Friend>f = null;
                if (classification.contains(classify)) {
                    f = mFriends.get(classify);
                }else{
                    classification.add(classify);
                }
                if (f == null) f = new ArrayList<>();
                f.add(friend);
                mFriends.put(classify,f);
            }
        }

        @Override
        public int getGroupCount() {
            return mFriends.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mFriends.get(classification.get(i)).size();
        }

        @Override
        public List<Friend> getGroup(int i) {
            return mFriends.get(classification.get(i));
        }

        @Override
        public Friend getChild(int i, int i1) {
            return mFriends.get(classification.get(i)).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null){
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.classify_item,viewGroup,false);
            }
            TextView title = rowView.findViewById(R.id.title);
            title.setText(classification.get(i));
            return rowView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null){
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.friend_item,viewGroup,false);
            }
            final Friend friend = getChild(i, i1);
            CircleImageView headIV = rowView.findViewById(R.id.user_head);
            TextView nicknameTV = rowView.findViewById(R.id.nickname);
            TextView profileTV = rowView.findViewById(R.id.profile);
            TextView stateTV = rowView.findViewById(R.id.state);

            if (friend.getHead() != null)
                headIV.setImageBitmap(friend.getHead());

            String nickname = friend.getNickname();
            if (nickname==null||nickname.isEmpty()){
                nicknameTV.setText(friend.getAccount());
            }else{
                nicknameTV.setText(nickname);
            }
            profileTV.setText(friend.getProfile());
            stateTV.setText("不在线");
            stateTV.setTextColor(Color.RED);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendListItem.onClick(friend);
                }
            });

            return rowView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    private FriendListItem friendListItem = new FriendListItem() {
        @Override
        public void onClick(Friend friend) {
            Intent intent = new Intent(getActivity(),ChatActivity.class);
            PublicData.friend = friend;
            startActivity(intent);
        }
    };

    interface FriendListItem{

        void onClick(Friend friend);
    }
}
