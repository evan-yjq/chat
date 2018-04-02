package com.evan.chat.friends;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.evan.chat.R;
import com.evan.chat.data.source.Friend.model.Friend;
import com.evan.chat.view.CircleImageView;

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
        mExpandableListAdapter = new FriendsExpandableListAdapter(new ArrayList<Friend>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friends_frag,container,false);

        mNoFriendView = root.findViewById(R.id.noFriends);
        mFriendsView = root.findViewById(R.id.friendsLL);
        expandableListView = root.findViewById(R.id.expandable_friends_list);
        expandableListView.setAdapter(mExpandableListAdapter);

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(),R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadFriends(false);
            }
        });

        swipeRefreshLayout.setScrollUpChild(expandableListView);
        setHasOptionsMenu(true);
        return root;
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
    public void showAddFriends() {

    }

    private static class FriendsExpandableListAdapter extends BaseExpandableListAdapter{

        private Map<String,List<Friend>>mFriends;
        private List<String>classification;

        public FriendsExpandableListAdapter(List<Friend>friends){
            mFriends = new HashMap<>();
            classification = new ArrayList<>();
            setFriends(friends);
        }

        public void replaceData(List<Friend>friends){
            setFriends(friends);
            notifyDataSetChanged();
        }

        private void setFriends(List<Friend>friends){
            for (Friend friend : friends) {
                String classify = friend.getClassification();
                if (classify == null) classify = "默认";
                List<Friend>f;
                if (classification.contains(classify)) {
                    f = mFriends.get(classify);
                }else{
                    classification.add(classify);
                    f = new ArrayList<>();
                }
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
            CircleImageView head = rowView.findViewById(R.id.user_head);
            TextView nickname = rowView.findViewById(R.id.nickname);
            TextView profile = rowView.findViewById(R.id.profile);
            TextView state = rowView.findViewById(R.id.state);

            head.setImageResource(R.mipmap.logo);
            if ("".equals(friend.getNickname())){
                nickname.setText(friend.getAccount());
            }else{
                nickname.setText(friend.getNickname());
            }
            profile.setText(friend.getProfile());
            state.setText("不在线");
            state.setTextColor(Color.RED);

            return rowView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
