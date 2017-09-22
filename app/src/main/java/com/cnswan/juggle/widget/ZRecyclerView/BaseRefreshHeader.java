package com.cnswan.juggle.widget.ZRecyclerView;


/**
 * Created by jianghejie on 15/11/22.
 */
interface BaseRefreshHeader {
    int STATE_NORMAL = 0; //正常状态;就是高度为0,都没有显示呢...
    int STATE_RELEASE_TO_REFRESH = 1;   //释放刷新
    int STATE_REFRESHING = 2;           //刷新中
    int STATE_DONE = 3;                 //刷新完成;的一共通知信号吧;

    void onMove(float delta);           //让header刷新头移动

    boolean releaseAction();            //刷新动作;

    void refreshComplate();             //刷新完成的动作;

    int getVisiableHeight();            //获取刷新头此时的可见高度;
}
