package com.tw.hackmob.saferide.holder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tw.hackmob.saferide.R;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.listener.OnItemRequestListener;
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

    @BindView(R.id.call)
    Button mCall;

    public RequestRideHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Activity activity, final boolean solicitados, final Request request, final OnItemRequestListener rejectListener, final OnItemRequestListener acceptListener) {
        txtFrom.setText(request.getRoute().getFrom().getName());
        txtTo.setText(request.getRoute().getTo().getName());
        txtTime.setText(request.getRoute().getTime());

        if (solicitados)
            txtRouteDriver.setText(request.getUserOwner().getName());
        else
            txtRouteDriver.setText(request.getUserRequest().getName());

        if (solicitados && request.getStatus() == 0) {
            mReject.setVisibility(View.GONE);
            mAccept.setVisibility(View.GONE);
            mCall.setVisibility(View.GONE);
        } else if (solicitados && request.getStatus() == 1) {
            mReject.setVisibility(View.GONE);
            mAccept.setVisibility(View.GONE);
            mCall.setVisibility(View.VISIBLE);
        }

        if (!solicitados) {
            if (request.getStatus() == 0) {
                mReject.setVisibility(View.VISIBLE);
                mAccept.setVisibility(View.VISIBLE);
                mCall.setVisibility(View.GONE);
            } else {
                mReject.setVisibility(View.GONE);
                mAccept.setVisibility(View.GONE);
                mCall.setVisibility(View.VISIBLE);
            }
        }

        mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectListener.onItemClick(request);
            }
        });
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptListener.onItemClick(request);
            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                if (solicitados)
                    intent1.setData(Uri.parse("tel:" + request.getUserOwner().getPhone()));
                else
                    intent1.setData(Uri.parse("tel:" + request.getUserRequest().getPhone()));
                activity.startActivity(intent1);
            }
        });
    }
}
