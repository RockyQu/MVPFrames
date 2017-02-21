package com.tool.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tool.common.utils.KnifeUtils;

/**
 * BaseHolder
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    // 点击事件
    protected OnViewClickListener onViewClickListener = null;

    public BaseHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        // 绑定
        KnifeUtils.bindTarget(this, itemView);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public abstract void setData(T data);

    /**
     * 释放资源
     */
    protected void onRelease(){

    }

    @Override
    public void onClick(View view) {
        if (onViewClickListener != null) {
            onViewClickListener.onViewClick(view, this.getLayoutPosition());
        }
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    public void setOnItemClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }
}