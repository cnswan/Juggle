package com.cnswan.juggle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnswan.juggle.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int RECEIVE_ITEM = 0;
    private static final int SEND_ITEM = 1;


    Context mContext;
    ArrayList<String> list = new ArrayList<>();

    public ChatAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RECEIVE_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false);
            return new ChatLeftHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false);
            return new ChatRightHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position % 2 == 0) {
            ((ChatLeftHolder) holder).left_content.setText(list.get(position));
        } else {
            ((ChatRightHolder) holder).right_content.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return RECEIVE_ITEM;
        } else {
            return SEND_ITEM;
        }
    }


    static class ChatLeftHolder extends RecyclerView.ViewHolder {
        TextView left_content;

        public ChatLeftHolder(View itemView) {
            super(itemView);
            left_content = (TextView) itemView.findViewById(R.id.left_chat_tv);
        }
    }

    static class ChatRightHolder extends RecyclerView.ViewHolder {
        TextView right_content;

        public ChatRightHolder(View itemView) {
            super(itemView);
            right_content = (TextView) itemView.findViewById(R.id.right_chat_tv);
        }
    }
}
