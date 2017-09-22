package com.cnswan.juggle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnswan.juggle.R;

import java.util.ArrayList;

public class HistoryTodayAdapter extends RecyclerView.Adapter<HistoryTodayAdapter.HistoryTodayHolder> {

    Context mContext;
    private ArrayList<String> mItems;

    public HistoryTodayAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public HistoryTodayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_history_today, parent, false);
        return new HistoryTodayHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryTodayHolder holder, int position) {
        holder.historyTextView.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class HistoryTodayHolder extends RecyclerView.ViewHolder {
        TextView historyTextView;

        public HistoryTodayHolder(View itemView) {
            super(itemView);
            historyTextView = (TextView) itemView.findViewById(R.id.history_item_tv);
        }
    }
}
