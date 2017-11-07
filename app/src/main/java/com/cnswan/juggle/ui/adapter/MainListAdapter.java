package com.cnswan.juggle.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnswan.juggle.R;
import com.cnswan.juggle.bean.Library;

import java.util.List;

/**
 * Created by cnswan on 2017/11/6.
 */

public class MainListAdapter extends BaseQuickAdapter<Library, BaseViewHolder> {

    public MainListAdapter(@Nullable List<Library> data) {
        super(R.layout.item_library, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Library item) {
        helper.setText(R.id.tv_lib_name, mContext.getString(R.string.name_format, item.getName()));
        helper.setText(R.id.tv_lib_desc, mContext.getString(R.string.desc_format, item.getDesc()));
    }
}
