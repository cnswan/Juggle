package com.cnswan.juggle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnswan.juggle.R;
import com.cnswan.juggle.activity.WebViewActivity;
import com.cnswan.juggle.bean.technews.AndroidNewsBean;

import java.util.List;

public class TechNewsAdapter extends RecyclerView.Adapter<TechNewsAdapter.TechNewsHolder> {
    private List<AndroidNewsBean.ResultBean> mItems;
    private Context                          mContext;

    public TechNewsAdapter(Context context, List<AndroidNewsBean.ResultBean> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public TechNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tech_news, parent, false);
        return new TechNewsHolder(v);
    }

    @Override
    public void onBindViewHolder(TechNewsHolder holder, int position) {
        final AndroidNewsBean.ResultBean bean = mItems.get(position);
        if (bean.getImages() != null
                && bean.getImages().size() > 0
                && !TextUtils.isEmpty(bean.getImages().get(0))) {
            Glide.with(mContext)
                    .load(bean.getImages().get(0))
                    .asBitmap()
                    .placeholder(R.drawable.img_one_bi_one)
                    .error(R.drawable.img_one_bi_one)
                    .into(holder.icon);
        } else {
            holder.icon.setVisibility(View.INVISIBLE);
        }
        holder.desc.setText(bean.getDesc());
        holder.time.setText(bean.getPublishedAt());
        holder.author.setText(bean.getWho());
        final String url = bean.getUrl();
        holder.news_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);*/
                WebViewActivity.loadUrl(v.getContext(), bean.getUrl(), "加载中...");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    static class TechNewsHolder extends RecyclerView.ViewHolder {


        LinearLayout news_item;
        ImageView icon;
        TextView desc;
        TextView time;
        TextView author;

        public TechNewsHolder(View itemView) {
            super(itemView);
            news_item = (LinearLayout) itemView.findViewById(R.id.news_item);
            icon = (ImageView) itemView.findViewById(R.id.news_icon);
            desc = (TextView) itemView.findViewById(R.id.news_desc);
            time = (TextView) itemView.findViewById(R.id.news_time);
            author = (TextView) itemView.findViewById(R.id.news_author);
        }
    }
}
