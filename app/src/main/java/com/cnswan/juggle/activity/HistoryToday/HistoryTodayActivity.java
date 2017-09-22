package com.cnswan.juggle.activity.HistoryToday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.adapter.HistoryTodayAdapter;
import com.zx.freetime.bean.historytoday.ResultBean;
import com.zx.freetime.ui.news.tech.TechNewsFragment;
import com.zx.freetime.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class HistoryTodayActivity extends AppCompatActivity implements HistoryTodayContract.View {
    TextView mTitle;
    RecyclerView mRecyclerView;
    HistoryTodayAdapter mAdapter;
    HistoryTodayContract.Presenter mPresenter;

    public ArrayList<String> titles = new ArrayList<>(); //刚一开始是空的;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_today);
        mTitle = (TextView) findViewById(R.id.history_title_tv);
        ArrayList<String> list = TimeUtil.getTodayTime();
        mTitle.setText("历史上的" + list.get(1) + "月" + list.get(2) + "日");

        mRecyclerView = (RecyclerView) findViewById(R.id.history_today_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new HistoryTodayAdapter(this, titles);

        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new HistoryTodayPresenter(this);
        mPresenter.start();
    }

    @Override
    public void load(List<ResultBean> datas) {
        //这里倒着读,因为后面的内容好;datas中一共有三个元素;
        for (int i = datas.size() - 1; i >= 0; i--) {
            ResultBean resultBean = datas.get(i);
            for (int j = 0; j < resultBean.getLists().size(); j++) {
//                System.out.println(resultBean.getLists().get(j));
                titles.add(resultBean.getLists().get(j).getTitle());
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorView() {
        Toast.makeText(this, "服务器暂时停止服务⊙﹏⊙!!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
