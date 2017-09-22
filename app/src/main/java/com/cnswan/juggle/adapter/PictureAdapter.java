package com.cnswan.juggle.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnswan.juggle.R;
import com.cnswan.juggle.activity.picture.DetailPictureActivity;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {

    private ArrayList<String> mItems;
    private Context mContext;

    public PictureAdapter(Context context, ArrayList<String> items) {
        super();
        mItems = items;
        mContext = context;
    }


    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_girl, parent, false);
        return new PictureHolder(v);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, final int position) {
        final String url = mItems.get(position);
        Log.e("###", "============onBindViewHolder url: " + url);
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_default_meizi)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                //.bitmapTransform(new CropCircleTransformation(mContext))  //如果想使用变换效果，这个注释可以打开
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setClass(mContext, DetailPictureActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);*/
                //###########################################
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);//第几张
                bundle.putStringArrayList("imageuri", mItems);
                Intent intent = new Intent(mContext, DetailPictureActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    static class PictureHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public PictureHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.girl_img);
        }
    }
}