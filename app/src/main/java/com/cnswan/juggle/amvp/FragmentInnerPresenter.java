package com.cnswan.juggle.amvp;

import android.support.v4.app.Fragment;

import java.util.List;

public class FragmentInnerPresenter {

    private String              fragmentName;
    private boolean             waitingShowToUser;
    private Fragment            fragment;
    private UserVisibleCallback userVisibleCallback;

    public FragmentInnerPresenter(Fragment fragment, UserVisibleCallback userVisibleCallback) {
        this.fragment = fragment;
        this.userVisibleCallback = userVisibleCallback;
        // noinspection ConstantConditions
        this.fragmentName = fragment.getClass().getSimpleName();
    }

    public void activityCreated() {
        // 如果自己是显示状态，但父Fragment却是隐藏状态，就把自己也改为隐藏状态，并且设置一个等待显示的标记
        if (fragment.getUserVisibleHint()) {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment != null && !parentFragment.getUserVisibleHint()) {
                userVisibleCallback.setWaitingShowToUser(true);
                userVisibleCallback.callSuperSetUserVisibleHint(false);
            }
        }
    }

    public void resume() {
        if (fragment.getUserVisibleHint()) {
            userVisibleCallback.onVisibleToUserChanged(true, true);
        }
    }

    public void pause() {
        if (fragment.getUserVisibleHint()) {
            userVisibleCallback.onVisibleToUserChanged(false, true);
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (fragment.isResumed()) {
            userVisibleCallback.onVisibleToUserChanged(isVisibleToUser, false);
        }

        if (fragment.getActivity() != null) {
            List<Fragment> childFragmentList = fragment.getChildFragmentManager().getFragments();
            if (isVisibleToUser) {
                // 将所有正等待显示的子Fragment设置为显示状态，并取消等待显示标记
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment instanceof UserVisibleCallback) {
                            UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                            if (userVisibleCallback.isWaitingShowToUser()) {
                                userVisibleCallback.setWaitingShowToUser(false);
                                childFragment.setUserVisibleHint(true);
                            }
                        }
                    }
                }
            } else {
                // 将所有正在显示的子Fragment设置为隐藏状态，并设置一个等待显示标记
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment instanceof UserVisibleCallback) {
                            UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                            if (childFragment.getUserVisibleHint()) {
                                userVisibleCallback.setWaitingShowToUser(true);
                                childFragment.setUserVisibleHint(false);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 当前Fragment是否对用户可见
     */
    public boolean isVisibleToUser() {
        return fragment.isResumed() && fragment.getUserVisibleHint();
    }

    public boolean isWaitingShowToUser() {
        return waitingShowToUser;
    }

    public void setWaitingShowToUser(boolean waitingShowToUser) {
        this.waitingShowToUser = waitingShowToUser;
    }

    public interface UserVisibleCallback {
        void setWaitingShowToUser(boolean waitingShowToUser);

        boolean isWaitingShowToUser();

        boolean isVisibleToUser();

        void callSuperSetUserVisibleHint(boolean isVisibleToUser);

        void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause);
    }
}
