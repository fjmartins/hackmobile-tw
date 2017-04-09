package com.tw.hackmob.saferide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tw.hackmob.saferide.adapter.RequestRideAdapter;
import com.tw.hackmob.saferide.adapter.RouteAdapter;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.model.Request;
import com.tw.hackmob.saferide.model.Route;
import com.tw.hackmob.saferide.utils.Session;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestRidesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecycler;

    private FirebaseDatabase mDatabase;
    private List<Request> mRequests = new ArrayList<>();

    private RequestRideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_rides);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("requests").orderByChild("userOwnerUid").equalTo(Session.getUser(this).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshop: dataSnapshot.getChildren()) {
                    Request request = requestSnapshop.getValue(Request.class);
                    mRequests.add(request);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new RequestRideAdapter(mRequests, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);
    }
}
