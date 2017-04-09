package com.tw.hackmob.saferide.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tw.hackmob.saferide.R;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.model.Route;

import butterknife.BindView;

/**
 * Created by fjmartins on 4/9/2017.
 */

public class RouteHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtFrom)
    TextView txtFrom;

    @BindView(R.id.txtTo)
    TextView txtTo;

    @BindView(R.id.txtRouteDriver)
    TextView txtRouteDriver;

    @BindView(R.id.txtTime)
    TextView txtTime;

    public RouteHolder(View itemView) {
        super(itemView);
    }

    public void bind(final Route route, final OnItemClickListener listener) {
        
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(route);
            }
        });
    }
}
