package com.rct.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rct.R;
import com.rct.database.UserPreference;
import com.rct.models.ChatModel;
import com.rct.utils.Api;
import com.rct.utils.Tools;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

private final Context mContext;
private final List<ChatModel> chat;
private String currentUserID;
private final UserPreference userPreference;
OkHttpClient client;
private OnChatMessageClick onChatMessageClick;


public ChatMessageAdapter(Context mContext, List<ChatModel> chat, UserPreference userPreference){
        this.mContext = mContext;
        this.chat = chat;
        this.userPreference = userPreference;
        }

    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_list, parent, false);
        return new ChatMessageAdapter.ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(final ChatMessageAdapter.ViewHolder holder, int position) {
    final ChatModel model = chat.get(position);
        if (!model.isExpiration_status()){
            holder.tv_status.setText("Expired");
            holder.tv_status.setTextColor(Color.RED);
            Log.d( "onBindViewHolder: ", "expires");
        }
        holder.tv_time.setText(Tools.getUnixToDate(model.getTime()));
        View.OnClickListener listener = view -> onChatMessageClick.OnChatClick(model);
        holder.cv.setOnClickListener(listener);
          /*  if (userPreference.getUserId().equals(model.getSeller_id())){
                holder.tv_name.setText(Tools.toCamelCase(userPreference.getName()));
            }else{*/
               if (userPreference.getSeller()){
                   holder.tv_name.setText(model.getBuyer());
               }else{
                   holder.tv_name.setText(model.getSeller());
               }
           // }

            }
    @Override
    public int getItemCount() {
            return  chat.size();
    }


public class ViewHolder extends RecyclerView.ViewHolder{

    protected TextView tv_name, tv_status, tv_time;
    protected ImageView iv_image;
    protected View cv;

    public ViewHolder(View view) {
        super(view);
        this.tv_name = view.findViewById(R.id.name);
        this.tv_status = view.findViewById(R.id.status);
        this.tv_time = view.findViewById(R.id.time);
        this.cv = view.findViewById(R.id.cv_card);

    }
}

    public void removeAt(int position) {
        try {
            chat.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, chat.size());
        }catch (Exception e){

        }
    }

    public void setOnChatMessageClick(OnChatMessageClick onChatMessageClick) {
        this.onChatMessageClick = onChatMessageClick;
    }

    public OnChatMessageClick getOnChatMessageClick() {
        return onChatMessageClick;
    }

    public String getUser(String userId) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/user/information/"+userId)
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}