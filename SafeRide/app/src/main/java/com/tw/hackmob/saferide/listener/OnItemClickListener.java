package com.tw.hackmob.saferide.listener;

import android.view.View;

import com.tw.hackmob.saferide.model.Route;

/**
 * Created by bot2 on 4/9/2017.
 */

public interface OnItemClickListener {
    void onItemClick(Route route);
    boolean onItemLongClick(View view, Route route);
}
