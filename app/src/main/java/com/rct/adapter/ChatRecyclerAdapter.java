package com.rct.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rct.R;
import com.rct.database.UserPreference;
import com.rct.models.QuoteModel;
import com.rct.utils.DataApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

private Context mContext;
private List<QuoteModel> chat;
private String currentUserID;
private OnChatClick chatClick;
private UserPreference userPreference;

public ChatRecyclerAdapter(Context mContext, List<QuoteModel> chat, UserPreference userPreference){
        this.mContext = mContext;
        this.chat = chat;
        this.userPreference = userPreference;}

@Override
public ChatRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quote_accepted_row, parent, false);
        return new ChatRecyclerAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(final ChatRecyclerAdapter.ViewHolder holder, int position) {
final QuoteModel model = chat.get(position);
    String user = null;
    try {
        user = DataApi.getUser(model.getSeller_id(), userPreference);
        Log.d("onCreate: ", user);
        JSONObject u = new JSONObject(user);
        JSONObject ui = u.getJSONObject("data");
        JSONObject uui = ui.getJSONObject("user");
        holder.tv_name.setText(uui.getString("name"));
    } catch (IOException | JSONException e) {
        e.printStackTrace();
    }
    if (userPreference.getSeller()){
        holder.tv_name.setVisibility(View.GONE);
    }

    holder.product.setText(model.getSupply_details());
    holder.price.setText(String.format("Price: %s", model.getSupply_price()));
    holder.quantity.setText(String.format("Quantity %s", model.getSupply_quantity()));

    View.OnClickListener listener = view -> chatClick.onChatClick(model);
    holder.chat.setOnClickListener(listener);
}


@Override
public int getItemCount() {
        return chat.size();
        }


public class ViewHolder extends RecyclerView.ViewHolder{

    protected TextView tv_name, product, quantity, price;
    protected Button chat;
    protected View cv;

    public ViewHolder(View view) {
        super(view);
        this.cv = view.findViewById(R.id.cv_card);
        this.tv_name = view.findViewById(R.id.names);
        this.product = view.findViewById(R.id.message);
        this.price = view.findViewById(R.id.price);
        this.quantity = view.findViewById(R.id.quantity);
        this.chat = view.findViewById(R.id.chat);
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

    public void setChatClick(OnChatClick chatClick) {
        this.chatClick = chatClick;
    }

    public OnChatClick getChatClick() {
        return chatClick;
    }
}

