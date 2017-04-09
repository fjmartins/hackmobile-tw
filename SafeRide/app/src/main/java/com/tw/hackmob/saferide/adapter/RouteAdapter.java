package com.tw.hackmob.saferide.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw.hackmob.saferide.R;
import com.tw.hackmob.saferide.holder.RouteHolder;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fjmartins on 4/9/2017.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteHolder> {

    private OnItemClickListener mListener;
    private List<Route> mList = new ArrayList<>();

    public RouteAdapter(List<Route> list, OnItemClickListener listener) {
        mList = list;
        mListener = listener;
    }

    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_adapter, parent, false);
        return new RouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteHolder holder, int position) {
        holder.bind(mList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRoutes(List<Route> routes) {
        this.mList = routes;
    }
}
