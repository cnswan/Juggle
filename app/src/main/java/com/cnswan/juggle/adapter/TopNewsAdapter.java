package com.cnswan.juggle.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zx.freetime.R;
import com.zx.freetime.bean.topnews.TopNewsItem;
import com.zx.freetime.ui.WebViewActivity;
import com.zx.freetime.ui.news.movie.detail.MovieDetailActivity;
import com.zx.freetime.ui.news.top.detail.TopNewsDetailActivity;

import java.util.List;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.TopNewsHolder> {
    private List<TopNewsItem> mItems;
    private Activity mContext;

    public TopNewsAdapter(Activity context, List<TopNewsItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public TopNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_top_news, parent, false);
        return new TopNewsHolder(v);
    }

    @Override
    public void onBindViewHolder(final TopNewsHolder holder, int position) {
        final TopNewsItem bean = mItems.get(position);
        Log.e("###", bean.toString());
        holder.textView.setText(bean.getTitle());
        holder.sourceTextview.setText(bean.getAuthor_name());
        Glide.with(mContext)
                .load(bean.getThumbnail_pic_s())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);

        final String url = bean.getUrl();//这个才是详情页...

        holder.itemView.setScaleX(0.8f);
        holder.itemView.setScaleY(0.8f);

        holder.itemView.animate().scaleX(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
        holder.itemView.animate().scaleY(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();

        holder.topNewsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(mContext, TopNewsDetailActivity.class);
                //intent.putExtra("url", url);
                intent.putExtra("bean", bean);
                mContext.startActivity(intent);*/
//                WebViewActivity.loadUrl(v.getContext(), url, "加载中...");
                TopNewsDetailActivity.start(mContext, bean, holder.imageView);
                // mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    static class TopNewsHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout topNewsItem;
        TextView sourceTextview;
        ImageView imageView;

        TopNewsHolder(View itemView) {
            super(itemView);
            topNewsItem = (LinearLayout) itemView.findViewById(R.id.top_item_layout);
            imageView = (ImageView) itemView.findViewById(R.id.item_image_id);
            textView = (TextView) itemView.findViewById(R.id.item_text_id);
            sourceTextview = (TextView) itemView.findViewById(R.id.item_text_source_id);
        }
    }
}

