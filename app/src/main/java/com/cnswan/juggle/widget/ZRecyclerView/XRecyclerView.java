package com.cnswan.juggle.widget.ZRecyclerView;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jingbin on 2016/1/28.
 * 继承了RecyclerView,
 */
public class XRecyclerView extends RecyclerView {
    private LoadingListener mLoadingListener;
    private WrapAdapter mWrapAdapter;
    //使用了两个list分别来盛放head和footer;
    private SparseArray<View> mHeaderViews = new SparseArray<View>();
    private SparseArray<View> mFootViews = new SparseArray<View>();

    private boolean pullRefreshEnabled = true;  //下拉刷新标志,默认是允许的...
    private boolean loadingMoreEnabled = true;  //加载更多标志,默认是允许的...

    private YunRefreshHeader mRefreshHeader; //下拉刷新头,如果在允许刷新的情况下,才进行初始化;
    private boolean isLoadingData;          //此时是否正在加载数据;因为下拉或者上拉后正在加载数据,如果此时你在执行上拉或者下拉,那么就无效了;
    public int previousTotal;               //记录之前已经加载的总量,因为接下来可能要刷新更新了;
    public boolean isnomore;                //没有更多的标志,那么就不会记载更多了,所以初始值为false;
    private float mLastY = -1;
    private static final float DRAG_RATE = 1.75f;
    // 是否是额外添加FooterView
    private boolean isOther = false;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    //在布局文件中使用XRecyclerView;进行的初始化;
    private void init(Context context) {
        //如果允许下拉刷新;则加入Headerview列表，设置加载图标
        if (pullRefreshEnabled) {
            YunRefreshHeader refreshHeader = new YunRefreshHeader(context);
            mHeaderViews.put(0, refreshHeader);//我们首先把这个下拉刷新的状态头添加进去;
            mRefreshHeader = refreshHeader;
        }

        //加载更多是默认的,不需要设置;那你这为什么设置了false呢?
        LoadingMoreFooter footView = new LoadingMoreFooter(context);
        addFootView(footView, false);
        mFootViews.get(0).setVisibility(GONE);
    }

    /**
     * 改为公有。供外添加view使用,使用标识
     * 注意：使用后不能使用 上拉刷新，否则添加无效
     * 使用时 isOther 传入 true，然后调用 noMoreLoading即可。
     *
     * 这个供外部使用的,本来默认是已经添加了默认的footer了,但是也可以供外部调用,添加自己的footer;
     * 这里的isOther == true表明的就是使用的外部的而不是默认的
     */
    public void addFootView(final View view, boolean isOther) {
        mFootViews.clear();
        mFootViews.put(0, view);
        this.isOther = isOther;
    }

    /**
     * 相当于加一个空白头布局：
     * 只有一个目的：为了滚动条显示在最顶端
     * 因为默认加了刷新头布局，不处理滚动条会下移。
     * 和 setPullRefreshEnabled(false) 一块儿使用
     * 使用下拉头时，此方法不应被使用！
     */
    public void clearHeader() {
        mHeaderViews.clear();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int height = (int) (1.0f * scale + 0.5f);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        View view = new View(getContext());//添加了一个空白的view;
        view.setLayoutParams(params);
        mHeaderViews.put(0, view);
    }

    //对外提供的addHeader的方法;
    public void addHeaderView(View view) {
        //首先允许下拉刷新;但是如果第一个header不是yunheader的话,就加入一个yunheader,这样确保只要允许下拉刷新,云header就在首位;
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof YunRefreshHeader)) {
            YunRefreshHeader refreshHeader = new YunRefreshHeader(getContext());
            mHeaderViews.put(0, refreshHeader);
            mRefreshHeader = refreshHeader;
        }
        mHeaderViews.put(mHeaderViews.size(), view);
    }

    //加载更多 ... 完成的通知
    private void loadMoreComplete() {
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (previousTotal <= getLayoutManager().getItemCount()) {
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_COMPLETE);
            } else {
                footView.setVisibility(View.GONE);
            }
        } else {
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
            } else {
                footView.setVisibility(View.GONE);
            }
            isnomore = true;
        }
        previousTotal = getLayoutManager().getItemCount();
    }

    public void noMoreLoading() {
        isLoadingData = false;
        final View footView = mFootViews.get(0);
        isnomore = true;
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
        // 额外添加的footView
        if (isOther) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    public void refreshComplete() {
        //  mRefreshHeader.refreshComplate();
        if (isLoadingData) {
            loadMoreComplete();
        } else {
            mRefreshHeader.refreshComplate();
        }
    }


    //之前只是为RecyclerView设置了view,那么如何展示呢,这就用到了setAdapter了,这里对Adapter进行了一层封装;
    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);//单独为这个adapter设置一个观察者对象,因为我们这里默认的观察者是mWrapAdapter
    }

    //加载更多实现,顺带复习一下滑动的三个状态:IDLE:停止滚动;TOUCH_SCROLL:正在滚动;FLING:手指抛动;
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //这里主要是监听了停止滚动时的情况,看这时候是不是要加载更多,那肯定要判断是不是到底部了;
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            //上面已经拿到了最后一个可见的Item了;这里注意区别getChildCount是获取目前可见的item的数量;getItemCount是获取数据源的数量;
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isnomore
                    && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING) {

                //准备加载更多了
                View footView = mFootViews.get(0);
                isLoadingData = true;//这是加载的flag为true;
                if (footView instanceof LoadingMoreFooter) {
                    //设置状态为loading;
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    footView.setVisibility(View.VISIBLE);
                }
                if (isNetWorkConnected(getContext())) {
                    mLoadingListener.onLoadMore();
                } else {
                    //如果是没网,那再延迟加载也没有用啊,还不如直接给出提示呢...
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingListener.onLoadMore();
                        }
                    }, 1000);
                }
            }
        }
    }

    //刷新条随着手指滑动慢慢显示,涉及到滑动,重写onTouchEvent，特别是针对MotionEvent.ACTION_MOVE处理
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) { //初始化的值就是-1;
            mLastY = ev.getRawY();//得到了绝对位置;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果是按下的那一刻;只是记录下按下的位置;
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动的那一小段时间段,记录下纵向的距离;
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) { //此时hander在top并且也允许下拉刷新;
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);//这里做了一个缓慢移动的效果,使得手指实际下拉距离和header下拉距离的1.75倍;
                    if (mRefreshHeader.getVisiableHeight() > 0 && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING) {
                        //这个判断鸡肋吧;
                        return false;
                    }
                }
                break;
            default:
                //这里包含了up或者cancel的操作;写个default有点误导...
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            //NOTE:重要的回调,如果是释放刷新,那么就开始调用加载的方法了;
                            mLoadingListener.onRefresh();
                            isnomore = false;
                            previousTotal = 0;
                            final View footView = mFootViews.get(0);
                            if (footView instanceof LoadingMoreFooter) {
                                if (footView.getVisibility() != View.GONE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    /**
     * 这个方法是用来判断我们的headerView是不是显示在top,这里只是简单的判断了getParent是不是null;
     * 可以这么理解,只有在header被现实出来的时候在是被添加到RecyclerView这个ViewGroup中,不显示的情况下,是被从ViewGroup中移除了;
     * @return
     */
    public boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.size() == 0) {
            return false;
        }

        View view = mHeaderViews.get(0);
        if (view.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }


    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        this.pullRefreshEnabled = pullRefreshEnabled;
    }

    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.loadingMoreEnabled = loadingMoreEnabled;
        if (!loadingMoreEnabled) {
            if (mFootViews != null) {
                mFootViews.remove(0);
            }
        } else {
            if (mFootViews != null) {
                LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
                addFootView(footView, false);
            }
        }
    }


    public void setLoadMoreGone() {
        if (mFootViews == null) {
            return;
        }
        View footView = mFootViews.get(0);
        if (footView != null && footView instanceof LoadingMoreFooter) {
            mFootViews.remove(0);
        }
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void reset() {
        isnomore = false;
        final View footView = mFootViews.get(0);
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).reSet();
        }
    }

    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
}
