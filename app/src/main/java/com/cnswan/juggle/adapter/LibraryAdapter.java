package com.cnswan.juggle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnswan.juggle.R;
import com.cnswan.juggle.bean.lib.Library;

import java.util.List;

/**
 * Created by 00013259 on 2017/9/30.
 */

public class LibraryAdapter extends BaseQuickAdapter<Library, BaseViewHolder> {


    public LibraryAdapter(@Nullable List<Library> data) {
        super(R.layout.item_library, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Library item) {
        helper.setText(R.id.tv_lib_name, item.getName());
    }
}
