package com.ruiqin.refreshdemo.module.recycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruiqin.refreshdemo.R;
import com.ruiqin.refreshdemo.base.BaseActivity;

public class BaseRecyclerView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycler_view);
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }
}
