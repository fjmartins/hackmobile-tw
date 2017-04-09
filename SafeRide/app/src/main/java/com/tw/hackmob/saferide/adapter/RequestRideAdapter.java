package com.tw.hackmob.saferide.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw.hackmob.saferide.R;
import com.tw.hackmob.saferide.holder.RequestRideHolder;
import com.tw.hackmob.saferide.holder.RouteHolder;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.model.Request;
import com.tw.hackmob.saferide.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phgm on 09/04/2017.
 */

public class RequestRideAdapter extends RecyclerView.Adapter<RequestRideHolder> {

    private View.OnClickListener mListenerAccept, mListenerReject;
    private List<Request> mList = new ArrayList<>();

    public RequestRideAdapter(List<Request> list, View.OnClickListener listenerAccept, View.OnClickListener listenerReject) {
        mList = list;
        mListenerAccept = listenerAccept;
        mListenerReject = listenerReject;
    }

    @Override
    public RequestRideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_ride_adapter, parent, false);
        return new RequestRideHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestRideHolder holder, int position) {
        holder.bind(mList.get(position), mListenerReject, mListenerAccept);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRoutes(List<Request> requests) {
        this.mList = requests;
    }
}

