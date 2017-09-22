package com.cnswan.juggle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnswan.juggle.R;
import com.cnswan.juggle.module.rxjava.RxBus;
import com.cnswan.juggle.module.rxjava.RxBusBaseMessage;
import com.cnswan.juggle.module.rxjava.RxCodeConstants;

import java.util.ArrayList;

public class OverViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<String> list;

    //设计一下:1个的情况:美图/电影/闲聊;
    //2个的情况下:头条/科技新闻;
    private static final int TYPE_TITLE = 1; // title
    private static final int TYPE_ONE = 2;// 一张图
    private static final int TYPE_TWO = 3;// 二张图
    private static final int TYPE_THREE = 4;// 三张图

    public OverViewAdapter(Context mContext, ArrayList<String> list) {
        super();
        this.mContext = mContext;
        this.list = list;
    }

    //这里就写死了啊
    @Override
    public int getItemViewType(int position) {
        if (position < 2) {  //0,1;
            return TYPE_TWO;
        } else {
            return TYPE_ONE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_overview_one, parent, false);
            return new OverViewOneHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_overview_two, parent, false);
            return new OverViewTwoHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.tv.setText(list.get(position));
        if (position < 2) {
            OverViewTwoHolder holder2 = (OverViewTwoHolder) holder;
            if (position == 0) {
                holder2.tv_title.setText("今日头条");
                holder2.img_title.setImageResource(R.drawable.home_title_top);
                holder2.img1.setImageResource(R.drawable.huoying1);
                holder2.tv1.setText("");
                holder2.img2.setImageResource(R.drawable.huoying2);
                holder2.tv2.setText("");
                holder2.ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_SUB,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TOP, null));
                    }
                });
            } else if (position == 1) {
                holder2.tv_title.setText("科技范儿");
                holder2.img_title.setImageResource(R.drawable.home_title_android);

                holder2.img1.setImageResource(R.drawable.huoying3);
                holder2.tv1.setText("");
                holder2.img2.setImageResource(R.drawable.huoying4);
                holder2.tv2.setText("");
                holder2.ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_SUB,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TECH, null));
                    }
                });
            }


        } else {
            OverViewOneHolder holder1 = (OverViewOneHolder) holder;
            if (position == 2) {
                holder1.tv_title.setText("热门电影");
                holder1.img_title.setImageResource(R.drawable.home_title_movie);
                holder1.img1.setImageResource(R.drawable.home_movie);
                holder1.tv1.setText("全球热门电影尽在其中O(∩_∩)O~");
                holder1.ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_SUB,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_MOVIE, null));
                    }
                });
            } else if (position == 3) {
                //使用缓存吧;
                holder1.tv_title.setText("精美图片");
                holder1.img_title.setImageResource(R.drawable.home_title_meizi);
                //NOTE:有一个硬编码;
                Glide.with(mContext).load("http://img2.3lian.com/2014/c7/25/d/40.jpg")
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder1.img1);
                holder1.tv1.setText("给你的双眼一次放松吧");
                holder1.ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_PARENT,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_PICTURE, null));
                    }
                });
            } else if (position == 4) {
                holder1.tv_title.setText("实在无聊");
                holder1.img_title.setImageResource(R.drawable.home_title_app);
                holder1.img1.setImageResource(R.drawable.home_chat);
                holder1.tv1.setText("图灵机器人陪你度过闲暇时间");
                holder1.ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_PARENT,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TULING, null));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OverViewOneHolder extends RecyclerView.ViewHolder {
        ImageView img_title;
        TextView tv_title;
        LinearLayout ll_more;

        ImageView img1;
        TextView tv1;


        public OverViewOneHolder(View itemView) {
            super(itemView);
            img_title = (ImageView) itemView.findViewById(R.id.iv_title_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title_type);
            ll_more = (LinearLayout) itemView.findViewById(R.id.ll_title_more);

            img1 = (ImageView) itemView.findViewById(R.id.iv_one_photo);
            tv1 = (TextView) itemView.findViewById(R.id.tv_one_photo_title);

        }
    }


    public static class OverViewTwoHolder extends RecyclerView.ViewHolder {
        ImageView img_title;
        TextView tv_title;
        LinearLayout ll_more;

        ImageView img1;
        TextView tv1;
        ImageView img2;
        TextView tv2;


        public OverViewTwoHolder(View itemView) {
            super(itemView);
            img_title = (ImageView) itemView.findViewById(R.id.iv_title_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title_type);
            ll_more = (LinearLayout) itemView.findViewById(R.id.ll_title_more);

            img1 = (ImageView) itemView.findViewById(R.id.iv_two_one_one);
            tv1 = (TextView) itemView.findViewById(R.id.tv_two_one_one_title);

            img2 = (ImageView) itemView.findViewById(R.id.iv_two_one_two);
            tv2 = (TextView) itemView.findViewById(R.id.tv_two_one_two_title);

        }
    }

}

