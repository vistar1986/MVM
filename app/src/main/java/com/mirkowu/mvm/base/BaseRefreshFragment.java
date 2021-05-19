package com.mirkowu.mvm.base;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mirkowu.lib_base.fragment.BaseMVMFragment;
import com.mirkowu.lib_base.mediator.BaseMediator;
import com.mirkowu.lib_base.widget.RefreshHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

public abstract class BaseRefreshFragment<M extends BaseMediator> extends BaseMVMFragment<M>
        implements RefreshHelper.OnRefreshListener {

    protected RefreshHelper refreshHelper;

    /**
     * 必须调用 初始化
     *
     * @param refreshLayout
     */
    public void initRefresh(SmartRefreshLayout refreshLayout, RecyclerView recyclerView) {
        refreshHelper = new RefreshHelper(refreshLayout, recyclerView, this);

    }

    /**
     * 当前界面只有下拉刷新 没有上拉加载功能 用这个
     *
     * @param adapter
     * @param list
     */
    public void setLoadData(BaseQuickAdapter adapter, List<?> list) {
        if (refreshHelper != null) refreshHelper.setLoadData(adapter, list);
    }

    /**
     * 当有 下拉刷新  和 上拉加载功能 用这个
     *
     * @param adapter
     * @param list
     */
    public void setLoadMore(BaseQuickAdapter adapter, List<?> list) {
        if (refreshHelper != null) refreshHelper.setLoadMore(adapter, list);
    }

    public void setLoadMore(BaseQuickAdapter adapter, List<?> list, boolean hasMore) {
        if (refreshHelper != null) refreshHelper.setLoadMore(adapter, list, hasMore);
    }


    @Override
    public void onLoadNoMore() {

    }

    @Override
    public void onEmptyChange(boolean isEmpty) {

    }

}
