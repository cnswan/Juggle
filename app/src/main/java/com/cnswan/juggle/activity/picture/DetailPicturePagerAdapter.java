package com.cnswan.juggle.activity.picture;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cnswan.juggle.R;
import com.cnswan.juggle.widget.Zoom.ZoomImageView;

import java.util.ArrayList;

/**
 * Created by zhangxin on 2017/3/23 0023.
 * <p>
 * Description :
 */

public class DetailPicturePagerAdapter extends PagerAdapter {

    ArrayList<String> imageuri;
    Context mContext;

    public DetailPicturePagerAdapter(Context context, ArrayList<String> imageuri) {
        this.mContext = context;
        this.imageuri = imageuri;
    }

    @Override
    public int getCount() {
        return imageuri.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_detail_picture, container, false);

        container.addView(view);

        setupPage(view, position);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void setupPage(View view, int index) {
        ZoomImageView zoomImageView = (ZoomImageView) view.findViewById(R.id.detail_zoom);
        Glide.with(mContext).load(imageuri.get(index))
                .crossFade(700).into(zoomImageView);
    }
}