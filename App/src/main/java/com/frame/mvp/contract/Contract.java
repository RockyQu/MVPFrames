package com.frame.mvp.contract;

import com.tool.common.base.DefaultAdapter;
import com.tool.common.frame.BaseView;

/**
 * 定义Model、View的接口
 */
public interface Contract {


    interface View extends BaseView {

        void setAdapter(DefaultAdapter adapter);

        void startLoadMore();

        void endLoadMore();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model {

    }
}