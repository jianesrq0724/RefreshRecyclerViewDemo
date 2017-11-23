package com.ruiqin.refreshdemo.module.home;

import com.ruiqin.refreshdemo.base.BaseModel;
import com.ruiqin.refreshdemo.base.BasePresenter;
import com.ruiqin.refreshdemo.base.BaseView;
import com.ruiqin.refreshdemo.module.home.adapter.MainRecyclerAdapter;
import com.ruiqin.refreshdemo.module.home.bean.MainRecyclerData;

import java.util.List;

/**
 * Created by ruiqin.shen
 * 类说明：
 */

public interface MainContract {
    interface Model extends BaseModel {
        List<MainRecyclerData> initData();
    }

    interface View extends BaseView {
        void setRecyclerAdapterSuccess(MainRecyclerAdapter mainRecyclerAdapter);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void setAdapter();
    }
}
