package com.cnswan.juggle.activity.news.overview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.adapter.OverViewAdapter;
import com.zx.freetime.base.BaseFragment;
import com.zx.freetime.rx.RxBus;
import com.zx.freetime.rx.RxBusBaseMessage;
import com.zx.freetime.rx.RxCodeConstants;
import com.zx.freetime.ui.HistoryToday.HistoryTodayActivity;
import com.zx.freetime.utils.TimeUtil;
import com.zx.freetime.widget.ZBanner.ZBanner;
import com.zx.freetime.widget.ZBanner.ZBannerBean;
import com.zx.freetime.widget.ZRecyclerView.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class OverViewFragment extends BaseFragment {
    XRecyclerView mXRecyclerView;
    OverViewAdapter mAdapter;
    int start = 0;
    ArrayList<String> list = new ArrayList<>();
    View header;
    List<ZBannerBean> mList = new ArrayList<>();
    ZBanner mZBanner;

    ImageView techNews;
    ImageView historyToday;
    ImageView tulingChat;


    @Override
    public void initContentView(View contentView) {
        mXRecyclerView = (XRecyclerView) contentView.findViewById(R.id.fg_overview_rc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initHeader();
        init();
        mXRecyclerView.setAdapter(mAdapter);
        showContent();
    }

    @Override
    public int getFragmentContent() {
        return R.layout.fragment_overview;
    }

    @Override
    public void onRefresh() {

    }


    //你的这个header其实就是你的banner;
    void initHeader() {
        header = LayoutInflater.from(getContext()).inflate(R.layout.header_item_overview, null);
        mZBanner = (ZBanner) header.findViewById(R.id.zbanner);
        //设置选中和未选中时的图片
        mZBanner.setIndicators(R.drawable.ad_select, R.drawable.ad_unselect);
        //设置轮播间隔时间
        mZBanner.setDelay(3000);
        initData();//初始化list;
        mZBanner.setData(mList, new ZBanner.ImageCycleViewListener() {
            @Override
            public void onImageClick(ZBannerBean bean, int position, View imageView) {
                if (mZBanner.isCycle()) {
                    position = position - 1;
                }
               /* Toast.makeText(getActivity(), bean.getTitle() +
                        "选择了--" + position, Toast.LENGTH_LONG).show();*/
                switch (position) {
                    case 0: //跳转到今日头条;
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_SUB,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TOP, null));
                        break;
                    case 1://跳转到热门电影
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_SUB,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_MOVIE, null));
                        break;
                    case 2://跳转到精美图片
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_PARENT,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_PICTURE, null));
                        break;
                    case 3://跳转到聊天机器人
                        RxBus.getDefault().post(RxCodeConstants.JUMP_TO_PARENT,
                                new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TULING, null));
                        break;
                }
            }
        });
        mXRecyclerView.setPullRefreshEnabled(false);
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.addHeaderView(header);


        techNews = (ImageView) header.findViewById(R.id.btn_goto_tech);
        historyToday = (ImageView) header.findViewById(R.id.daily_btn);
        TextView day = (TextView) header.findViewById(R.id.tv_daily_text);
        day.setText(TimeUtil.getTodayTime().get(2));
        tulingChat = (ImageView) header.findViewById(R.id.btn_goto_chat);

        initListener();
    }

    void initListener() {
        techNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TO_SUB,
                        new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TECH, null));
            }
        });

        historyToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryTodayActivity.class));
            }
        });

        tulingChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TO_PARENT,
                        new RxBusBaseMessage(RxCodeConstants.JUMP_TO_TULING, null));
            }
        });
    }

    void init() {
        for (int i = start; i < 5; i++) {
            list.add("item" + i);
        }
        mAdapter = new OverViewAdapter(getActivity(), list);
    }


    private void initData() {
        mList.add(new ZBannerBean("今日头条",
                "http://img2.3lian.com/2014/c7/25/d/40.jpg"));
        mList.add(new ZBannerBean("热门电影",
                "http://img2.3lian.com/2014/c7/25/d/41.jpg"));
        mList.add(new ZBannerBean("精美图片",
                "http://imgsrc.baidu.com/forum/pic/item/b64543a98226cffc8872e00cb9014a90f603ea30.jpg"));
        mList.add(new ZBannerBean("图灵机器人",
                "http://imgsrc.baidu.com/forum/pic/item/261bee0a19d8bc3e6db92913828ba61eaad345d4.jpg"));
    }

    //TODO:首页并没有执行网络操作,所以直接显示,覆盖父类的...
    @Override
    protected void loadData() {
    }
}
