package cn.ucai.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.live.LiveConstants;
import cn.ucai.live.R;
import cn.ucai.live.data.model.Gift;
import cn.ucai.live.data.model.LiveRoom;
import cn.ucai.live.data.restapi.LiveException;
import cn.ucai.live.data.restapi.model.ResponseModule;

import com.blankj.ALog;
import com.bumptech.glide.Glide;
import cn.ucai.live.ThreadPoolManager;
import cn.ucai.live.data.restapi.ApiManager;
import cn.ucai.live.ui.GridMarginDecoration;
import cn.ucai.live.utils.L;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMPageResult;
import com.hyphenate.exceptions.HyphenateException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveListFragment extends Fragment {
    private static final String TAG = "LiveListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar loadmorePB;

    private static final int pageSize = 20;
    private String cursor;
    private boolean hasMoreData;
    private boolean isLoading;
    private final List<LiveRoom> liveRoomList = new ArrayList<>();
    private PhotoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadmorePB = (ProgressBar) getView().findViewById(R.id.pb_load_more);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleview);
        final GridLayoutManager glm = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridMarginDecoration(3));

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        showLiveList(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                showLiveList(false);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE
                        && hasMoreData
                        && !isLoading
                        && glm.findLastVisibleItemPosition() == glm.getItemCount() -1){
                    showLiveList(true);
                }
            }
        });

    }

    private void loadGiftList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Gift> list = ApiManager.get().getAllGifts();
                    if (list != null){
                        for (Gift gift:list) {
                            L.e(TAG,"gift = " + gift);
                        }
                    }
                } catch (LiveException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void showLiveList(final boolean isLoadMore){
        if (getLiveRoom()){
            ALog.e("getLiveRoom().........");
            return;
        }
        if(!isLoadMore)
            swipeRefreshLayout.setRefreshing(true);
        else
            loadmorePB.setVisibility(View.VISIBLE);
        isLoading = true;
        loadGiftList();
        ThreadPoolManager.getInstance().executeTask(new ThreadPoolManager.Task<ResponseModule<List<LiveRoom>>>() {
            @Override public ResponseModule<List<LiveRoom>> onRequest() throws HyphenateException {
                if(!isLoadMore){
                    cursor = null;
                }
                return ApiManager.get().getLivingRoomList(pageSize, cursor);
            }

            @Override public void onSuccess(ResponseModule<List<LiveRoom>> listResponseModule) {
                hideLoadingView(isLoadMore);
                List<LiveRoom> returnList = listResponseModule.data;
                ALog.e("returnList = " + returnList);
                if(returnList.size() < pageSize){
                    hasMoreData = false;
                    cursor = null;
                }else{
                    hasMoreData = true;
                    cursor = listResponseModule.cursor;
                }

                if(!isLoadMore) {
                    liveRoomList.clear();
                }
                liveRoomList.addAll(returnList);
                if(adapter == null){
                    adapter = new PhotoAdapter(getActivity(), liveRoomList);
                    recyclerView.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }

            @Override public void onError(HyphenateException exception) {
                hideLoadingView(isLoadMore);
            }
        });
    }

    private boolean getLiveRoom() {
        ALog.e("getLiveRoom()111111111");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ALog.e("getLiveRoom()2222222222");
                    final EMPageResult<EMChatRoom> result;
                    int pageCount = -1;
                    result = EMClient.getInstance().chatroomManager().
                            fetchPublicChatRoomsFromServer(0, pageSize);
                    ALog.e("getLiveRoom()333333333333");
                    //get chat room list
                    final List<EMChatRoom> chatRooms = result.getData();
                    pageCount = result.getPageCount();
                    ALog.e("getLiveRoom()444444444444");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            liveRoomList.clear();
                            ALog.e("liveRoomList = " + liveRoomList.size());
                            ALog.e("chatRooms = " + chatRooms.size());
                            if (chatRooms != null && chatRooms.size() > 0){
                                for (EMChatRoom room:chatRooms) {
                                    LiveRoom liveRoom = chatRoomToliveRoom(room);
                                    if (liveRoom != null){
                                        liveRoomList.add(liveRoom);
                                    }
                                }
                                ALog.e("liveRoomList = " + liveRoomList.size());
                                if(adapter == null){
                                    ALog.e("adapter == null= " + (adapter == null));
                                    adapter = new PhotoAdapter(getActivity(), liveRoomList);
                                    recyclerView.setAdapter(adapter);
                                }else{
                                    ALog.e("notifyDataSetChanged()");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    private LiveRoom chatRoomToliveRoom(EMChatRoom room) {
        LiveRoom liveRoom = null;
        if (room != null) {
            liveRoom = new LiveRoom();
            liveRoom.setId(room.getOwner());
            liveRoom.setDescription(room.getDescription());
            liveRoom.setChatroomId(room.getId());
            liveRoom.setAnchorId(room.getOwner());
            String s = LiveConstants.LIVE_COVER;
            if (room.getName().contains(s)) {
                int index = room.getName().indexOf(s);
                String name = room.getName().substring(0, index);
                ALog.e("name = " + name);
                String coverUrl = LiveConstants.LIVE_COVER_PREFIX
                        + room.getName().substring(index + s.length());
                ALog.e("coverUrl = " + coverUrl);
                liveRoom.setCover(coverUrl);
                liveRoom.setName(name);
            } else {
                liveRoom.setName(room.getName());
            }
            liveRoom.setAudienceNum(room.getMemberCount());
        }
        return liveRoom;
    }

    private void hideLoadingView(boolean isLoadMore){
        isLoading = false;
        if(!isLoadMore)
            swipeRefreshLayout.setRefreshing(false);
        else
            loadmorePB.setVisibility(View.INVISIBLE);
    }

    static class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

        private final List<LiveRoom> liveRoomList;
        private final Context context;

        public PhotoAdapter(Context context, List<LiveRoom> liveRoomList){
            this.liveRoomList = liveRoomList;
            this.context = context;
        }
        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(context).
                    inflate(R.layout.layout_livelist_item, parent, false));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = holder.getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) return;
                    context.startActivity(new Intent(context, LiveAudienceActivity.class)
                            .putExtra("liveroom", liveRoomList.get(position)));
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            LiveRoom liveRoom = liveRoomList.get(position);
            holder.anchor.setText(liveRoom.getName());
            holder.audienceNum.setText(liveRoom.getAudienceNum() + "人");
            Glide.with(context)
                    .load(liveRoomList.get(position).getCover())
                    .placeholder(R.color.placeholder)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return liveRoomList.size();
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        ImageView imageView;
        @BindView(R.id.author)
        TextView anchor;
        @BindView(R.id.audience_num) TextView audienceNum;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
