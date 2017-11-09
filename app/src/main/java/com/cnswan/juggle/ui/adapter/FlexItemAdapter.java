package com.cnswan.juggle.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnswan.juggle.R;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

/**
 * FlexBoxLayout 适配 RecyclerView
 * Created by cnswan on 2017/11/9.
 */

public class FlexItemAdapter extends BaseQuickAdapter<FlexboxLayoutManager.LayoutParams, BaseViewHolder> {

    public FlexItemAdapter(@Nullable List<FlexboxLayoutManager.LayoutParams> data) {
        super(R.layout.item_flex, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FlexboxLayoutManager.LayoutParams item) {
        helper.setText(R.id.textview, String.valueOf(helper.getAdapterPosition()));
        helper.itemView.setLayoutParams(item);
    }

}
