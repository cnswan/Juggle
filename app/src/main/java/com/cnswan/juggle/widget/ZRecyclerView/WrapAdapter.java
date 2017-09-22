package com.cnswan.juggle.widget.ZRecyclerView;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangcai on 2016/1/28.
 * 这里继承了RecyclerView.Adapter,对其做了一层包装;
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //注意,我们定义的这些type的int值最好不要和adapter中的type中重复,否则,完了...............
    private static final int TYPE_REFRESH_HEADER = -5;  //刷新头;其实就是我们的yunheader
    private static final int TYPE_HEADER = -4;          //头,这是我们又在外面添加的额外的头;
    private static final int TYPE_NORMAL = 0;           //普通状态;
    private static final int TYPE_FOOTER = -3;          //尾;

    //记住,我们这个Adapter是个包装类,是对外部设置的adapter的一个包装;设置进来的adapter是不包含header和footer的;
    private RecyclerView.Adapter adapter;

    private SparseArray<View> mHeaderViews;

    private SparseArray<View> mFootViews;

    private int headerPosition = 1; //这里直接初始化为1,因为0的位置永远都是我们的yunheader;

    public WrapAdapter(SparseArray<View> headerViews, SparseArray<View> footViews, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.mHeaderViews = headerViews;
        this.mFootViews = footViews;
    }

    /*
    因为RecycleView支持LinearLayoutManager、GridLayoutManager、StaggeredGridLayoutManager，
    而GridLayoutManager、StaggeredGridLayoutManager在添加header时候需要注意横跨整个屏幕宽度
    GridLayoutManager是要设置SpanSize每行的占位大小
    StaggerLayoutManager 就是要获取StaggerLayoutManager的LayoutParams 的setFullSpan 方法来设置占位宽度，因此在WrapAdapter中做了针对性处理
    *
    GridLayoutManager提供了类似GridView的列表布局。
    Staggered的意思是错列的，StaggeredGridLayoutManager继承GridLayoutManager，允许宽高不相等，所以可以用来实现瀑布流的布局。
     */
    /*回调的时机是当这个RecyclerView作为ViewGroup被添加到window的时候;此时与Item还无关...*/
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            //针对GridLayoutManager做特殊处理;
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //这里判断了是否是header还是footer,如果是的话,那么其占位就是getSpanCount,否则就是1;
                    //eg:我们设置了span是3,表明是散列的,当然我们的头和尾必须是3,其余的都是1;
                    return (isHeader(position) || isFooter(position))? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    //这个方法被回调的时机是当一个item被创建然后添加到视图上的时候;也就是说这个时候item马上就会被看到了;
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //注意这里拿到的是itemview的lp;
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            //再次设置为fullSpan;不知道和上一个设置的效果有什么不同???
            p.setFullSpan(true);
        }
    }

    //判断是不是header,如果position在0~header.size()-1的范围内,就是header;
    public boolean isHeader(int position) {
        return position >= 0 && position < mHeaderViews.size();
    }

    //判断是不是footer
    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - mFootViews.size();
    }

    public boolean isRefreshHeader(int position) {
        return position == 0;
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    //显示一个Item,创建一个ViewHolder,是可以复用的;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REFRESH_HEADER) {
            //如果是云header
            return new SimpleViewHolder(mHeaderViews.get(0));
        } else if (viewType == TYPE_HEADER) {
            //如果是普通的hander;
            return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
        } else if (viewType == TYPE_FOOTER) {
            //如果是footer;
            return new SimpleViewHolder(mFootViews.get(0));
        }
        //其它情况,让adapter自己决定吧;
        return adapter.onCreateViewHolder(parent, viewType);
    }

    //这个方法也不陌生吧;注意传入了position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        int adjPosition = position - getHeadersCount();
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                adapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }


    @Override
    public int getItemCount() {
        if (adapter != null) {
            return getHeadersCount() + getFootersCount() + adapter.getItemCount();
        } else {
            //这种情况不会发生吧,你都没有设置adapter(没有设置数据源,那么就只有header和footer了)
            return getHeadersCount() + getFootersCount();
        }
    }


    //NOTE:如果你想用复杂的布局,那么这个方法是必不可缺的,就是让position和type产生对应关系;
    @Override
    public int getItemViewType(int position) {
        if (isRefreshHeader(position)) {//position为0;
            return TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)) {
            return TYPE_HEADER;
        }

        if (isFooter(position)) {
            return TYPE_FOOTER;
        }

        //参数传进来的position是包含header和footer的位置;而adapter中是不包含header和footer的;
        //能走到这一步,说明既不是header也不是footer;
        int adjPosition = position - getHeadersCount();//得到的结果是我们想展示的数据的真实position;
        int adapterCount;
        //我们自己的adapter也有一个自己的position和type的定义;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemViewType(adjPosition);
            }
        }
        //NOTE:感觉这个normal好像永远也执行不到吧...
        return TYPE_NORMAL;
    }


    /**
     * 参考:http://www.aichengxu.com/other/1631131.htm
     这里是做了一步优化吧;其实我感觉也是鸡肋;因为你不能确保你传入的数据adapter中复写了hashStableIds(),默认是返回false的,只有设置
     setHasStableIds(true),那么hashStableIds()才会返回true,从而达到:
     来自知乎:
     尽量能保证 Adapter 的 hasStableIds() 返回 true，这样在 notifyDataSetChanged() 的时候，
     如果 id 不变，ListView 将不会重新绘制这个 View，达到优化的目的；
     其实你不关注ID也无所谓;
     */
    @Override
    public long getItemId(int position) {
        if (adapter != null && position >= getHeadersCount()) {
            int adjPosition = position - getHeadersCount();
            int adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemId(adjPosition);
            }
        }
        //这里对header和footer的id均返回-1,因为数据再变,我们也不可能删除header和footer吧
        return -1;
    }


    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    //在onCreateViewHolder中new的;
    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}