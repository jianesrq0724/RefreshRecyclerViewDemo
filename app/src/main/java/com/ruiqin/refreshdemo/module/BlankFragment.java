package com.ruiqin.refreshdemo.module;

import android.os.Bundle;
import android.view.View;

import com.ruiqin.refreshdemo.R;
import com.ruiqin.refreshdemo.base.BaseFragment;

public class BlankFragment extends BaseFragment {
    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void stopLoad() {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blank;
    }
}
