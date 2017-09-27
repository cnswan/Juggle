package com.cnswan.juggle.amvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * 嵌套fragment基类
 *
 * @author 00013259
 */
public class BaseInnerFragment extends Fragment implements FragmentInnerPresenter.UserVisibleCallback {

    private FragmentInnerPresenter innerPresenter;

    public BaseInnerFragment() {
        innerPresenter = new FragmentInnerPresenter(this, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        innerPresenter.activityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        innerPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        innerPresenter.pause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        innerPresenter.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void setWaitingShowToUser(boolean waitingShowToUser) {
        innerPresenter.setWaitingShowToUser(waitingShowToUser);
    }

    @Override
    public boolean isWaitingShowToUser() {
        return innerPresenter.isWaitingShowToUser();
    }

    @Override
    public boolean isVisibleToUser() {
        return innerPresenter.isVisibleToUser();
    }

    @Override
    public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {

    }

}
