package com.tool.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 数据适配器
 */
public abstract class DefaultAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {

    // 点击事件
    protected OnRecyclerViewItemClickListener onItemClickListener = null;
    // 绑定数据集
    protected List<T> data;
    // BaseHolder
    private BaseHolder<T> holder;

    public DefaultAdapter(List<T> data) {
        super();
        this.data = data;
    }

    /**
     * 创建Hodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        holder = getHolder(view);
        holder.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
            @Override
            public void onViewClick(View view, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, data.get(position), position);
                }
            }
        });
        return holder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        holder.setData(data.get(position));
    }

    /**
     * 获取数据集个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 获取数据集
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 获得Item的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return data == null ? null : data.get(position);
    }

    /**
     * 子类实现提供holder
     *
     * @param v
     * @return
     */
    public abstract BaseHolder<T> getHolder(View view);

    /**
     * 子类实现提供Item的布局
     *
     * @return
     */
    public abstract int getLayoutId();

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, T data, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}