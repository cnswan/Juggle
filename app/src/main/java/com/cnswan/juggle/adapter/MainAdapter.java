package com.cnswan.juggle.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnswan.juggle.R;

import java.util.List;

/**
 * Created by 00013259 on 2017/8/18.
 */

public class MainAdapter extends BaseQuickAdapter<String, MainAdapter.MainViewHolder> {

    public MainAdapter(@Nullable List<String> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void convert(MainViewHolder helper, String item) {

    }

    public static class MainViewHolder extends BaseViewHolder {

        public MainViewHolder(View view) {
            super(view);
        }
    }

}
