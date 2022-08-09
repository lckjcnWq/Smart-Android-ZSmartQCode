package com.theswitchbot.common.fragment;

import androidx.fragment.app.Fragment;

public class LazyFragment extends Fragment {

    private long enterTime;// 进入页面的时间
    private boolean loaded = false;
    private boolean isShowing = false;

    protected boolean isShowing(){
        return isShowing;
    }

    /**
     * 离开页面回调
     */
    protected void onLeave(long stayTime) {
        isShowing = false;
    }

    /**
     * 进入页面回调
     */
    protected void onEnter() {
        enterTime = System.currentTimeMillis();
        isShowing = true;
    }

    /**
     * 页面首次加载回调
     */
    protected void onLazy() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!loaded) {
            loaded = true;
            onLazy();
        }

        // 需要判断activity从后台进入前台时页面属于隐藏状态
        if (!isHidden()) {
            onEnter();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            onLeave(System.currentTimeMillis() - enterTime);
        } else {
            onEnter();
        }
    }

    protected boolean isLoaded() {
        return loaded;
    }

    @Override
    public void onPause() {
        super.onPause();

        onLeave(System.currentTimeMillis() - enterTime);
    }
}
