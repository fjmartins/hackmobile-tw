package com.tw.hackmob.saferide.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tw.hackmob.saferide.R;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.model.Request;
import com.tw.hackmob.saferide.model.Route;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phgm on 09/04/2017.
 */

public class RequestRideHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtFrom)
    TextView txtFrom;

    @BindView(R.id.txtTo)
    TextView txtTo;

    @BindView(R.id.txtRouteDriver)
    TextView txtRouteDriver;

    @BindView(R.id.txtTime)
    TextView txtTime;

    @BindView(R.id.reject)
    Button mReject;

    @BindView(R.id.accept)
    Button mAccept;

    public RequestRideHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Request request, final View.OnClickListener rejectListener, final View.OnClickListener acceptListener) {
        txtFrom.setText(request.getRoute().getFrom().getName());
        txtTo.setText(request.getRoute().getTo().getName());
        txtTime.setText(request.getRoute().getTime());
        txtRouteDriver.setText(request.getRoute().getOwner().getName());

        mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectListener.onClick(v);
            }
        });
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptListener.onClick(v);
            }
        });
    }
}
