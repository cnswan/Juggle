package com.cnswan.juggle.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.DefaultLayoutHelper;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.cnswan.juggle.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Alibaba VLayout
 * Created by cnswan on 2017/11/9.
 */

@Route(path = VLayoutActivity.ACT_PATH)
public class VLayoutActivity extends ManagedActivity {

    public static final String ACT_PATH = "/activity/vlayout/main";

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlayout);
        mRecyclerView = findViewById(R.id.vlayout_recycler_list);
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(10, 10, 10, 10);
            }
        });
        List<LayoutHelper> helpers = new LinkedList<>();
        // Grid布局， 支持横向的colspan
        final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setItemCount(25);
        // 固定布局，但之后当页面滑动到该图片区域才显示, 可以用来做返回顶部或其他书签等
        ScrollFixLayoutHelper scrollFixLayoutHelper = new ScrollFixLayoutHelper(FixLayoutHelper.TOP_RIGHT, 100, 100);
        scrollFixLayoutHelper.setShowType(ScrollFixLayoutHelper.SHOW_ON_ENTER);
        helpers.add(DefaultLayoutHelper.newHelper(30));
        helpers.add(scrollFixLayoutHelper);
        helpers.add(DefaultLayoutHelper.newHelper(2));
        helpers.add(gridLayoutHelper);
        layoutManager.setLayoutHelpers(helpers);
        mRecyclerView.setAdapter(new VirtualLayoutAdapter(layoutManager) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new VLayoutViewHolder(new TextView(VLayoutActivity.this));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 300);
                holder.itemView.setLayoutParams(layoutParams);

                ((TextView) holder.itemView).setText(String.valueOf(position));

                if (position == 30) {
                    layoutParams.height = 200;
                    layoutParams.width = 200;
                } else if (position > 35) {
                    layoutParams.height = 200 + (position - 30) * 100;
                }

                if (position > 35) {
                    holder.itemView.setBackgroundColor(0x66cc0000 + (position - 30) * 128);
                } else if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(0xaa00ff00);
                } else {
                    holder.itemView.setBackgroundColor(0xccff00ff);
                }
            }

            @Override
            public int getItemCount() {
                List<LayoutHelper> list = getLayoutHelpers();
                int count = 0;
                for (int i = 0; i < list.size(); i++) {
                    count += list.get(i).getItemCount();
                }
                return count;
            }
        });
    }

    static class VLayoutViewHolder extends RecyclerView.ViewHolder {

        public VLayoutViewHolder(View itemView) {
            super(itemView);
        }

    }
}
