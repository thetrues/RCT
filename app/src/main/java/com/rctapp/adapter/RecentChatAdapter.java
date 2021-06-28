package com.rctapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.R;
import com.rctapp.database.UserPreference;
import com.rctapp.models.MessageModel;
import com.rctapp.utils.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecentChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int CHAT_ME = 100;
    private final int CHAT_YOU = 200;
    CharSequence timePassedString;
    UserPreference userPreference;
    Activity activity;
    private List<MessageModel> items = new ArrayList<>();

    private Context ctx;
    private MessageRecyclerAdapter.OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, MessageModel obj, int position);
    }

    public void setOnItemClickListener(final MessageRecyclerAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecentChatAdapter(Context context, List<MessageModel> items, Activity activity, UserPreference userPreference) {
        this.items = items;
        this.ctx = context;
        this.userPreference = userPreference;
        this.activity = activity;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView text_content;
        public TextView text_time;
        public View lyt_parent;

        public ItemViewHolder(View v) {
            super(v);
            text_content = v.findViewById(R.id.text_content);
            text_time = v.findViewById(R.id.text_time);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (viewType == CHAT_ME) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_you, parent, false);
        }
        vh = new RecentChatAdapter.ItemViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MessageRecyclerAdapter.ItemViewHolder) {
            final MessageModel m = items.get(position);
            MessageRecyclerAdapter.ItemViewHolder vItem = (MessageRecyclerAdapter.ItemViewHolder) holder;
            vItem.text_content.setText(m.getMessage());
            vItem.text_time.setText(Tools.getFormattedDateSimple(m.getTime()));
            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, m, position);
                    }
                }
            });

        }
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).getSenderId().equals(userPreference.getUserId()) ? CHAT_ME : CHAT_YOU;
    }

    public void insertItem(MessageModel item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void setItems(List<MessageModel> items) {
        this.items = items;
    }

    private CharSequence formatTime(String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(dateTime);
            long time = date.getTime();
            timePassedString = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePassedString;
    }
}