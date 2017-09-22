package com.cnswan.juggle.activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.adapter.ChatAdapter;
import com.zx.freetime.base.BaseFragment;
import com.zx.freetime.bean.chat.ChatBean;
import com.zx.freetime.http.Api;
import com.zx.freetime.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class ChatFragment extends Fragment implements ChatContract.View {
    RecyclerView mRecyclerView;
    EditText mEditText;
    Button mButton;

    ArrayList<String> list = new ArrayList<>();
    ChatAdapter mAdapter;
    LinearLayoutManager layoutManager;

    ChatContract.Presenter mPresenter;


    /*@Override
    public void initContentView(View contentView) {
        mButton = (Button) contentView.findViewById(R.id.talk_send_btn);
        mEditText = (EditText) contentView.findViewById(R.id.talk_content_et);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.talk_rv);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);

        initData();
        initListener();

        mPresenter = new ChatPresenter(this);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }


    void initView(View contentView) {
        mButton = (Button) contentView.findViewById(R.id.talk_send_btn);
        mEditText = (EditText) contentView.findViewById(R.id.talk_content_et);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.talk_rv);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    void initData() {
        list.add("您好,我是小智,让我陪你聊天吧^_^");
        mAdapter = new ChatAdapter(getActivity(), list);
        mRecyclerView.setAdapter(mAdapter);
    }

    void initListener() {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoft();
                return false;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = String.valueOf(mEditText.getText());
                if (TextUtils.isEmpty(send)) {
                    Toast.makeText(getActivity(), "发送内容不能为空^_^", Toast.LENGTH_LONG).show();
                } else {

                    mEditText.setText("");
                    list.add(send);
                    //TODO:这里判断键盘的宽高啊;
                    /*if (!flag && layoutManager.findViewByPosition(0).getTop() < 0) {
                        layoutManager.setStackFromEnd(true);  //这句很重要,是用来键盘弹出的时候将内容攻上去的;
                        flag = true;
                    }*/
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(list.size() - 1);
                    mPresenter.talk(send);
                }
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.toString().length() > 28) {
                    Toast.makeText(getActivity(), "发送内同不可以超过30个字", Toast.LENGTH_LONG).show();
                }
            }
        });

        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRecyclerView.scrollToPosition(list.size() - 1);
                return false;
            }
        });
    }

   /* @Override
    public int getFragmentContent() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onRefresh() {
        //这个方法暂时不要把,没网就是没网,最多提示一下当前不能用...
        list.add("啊哦,不知道哪出问题了,要不您先玩会别的^_^");
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(list.size() - 1);
    }*/


    void hideSoft() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void load(ChatBean chatBean) {
        if (chatBean.getErrorCode() == 0) {
            list.add(chatBean.getResult().getText());
        } else {
            list.add("呵呵");
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(list.size() - 1);
    }

    @Override
    public void showErrorView() {
        list.add("啊哦,不知道哪出问题了,要不您先玩会别的^_^");
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(list.size() - 1);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    //懒加载模块,刚一开始进来不需要联网,所以不需要加载的状态;
    //#################################################################
    //Fragment的View加载完毕的标记,在onCreateView方法调用的最后将该值进行设置;
    private boolean isViewCreated;

    //Fragment对用户可见的标记,在setUserVisibleHint方法中根据传入的参数设置该值
    private boolean isUIVisible;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;

        mPresenter = new ChatPresenter(this);

        initView(view);
        initData();
        initListener();

        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见,这是系统判断的;
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    protected void loadData() {
        Log.e("###", "我开始显示了");
        //showLoading();
    }
}

