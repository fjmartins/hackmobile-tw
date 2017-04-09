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
import com.tw.hackmob.saferide.async.NotificationAsync;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.listener.OnItemRequestListener;
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
                mRequests.removeAll(mRequests);
                for (DataSnapshot requestSnapshop: dataSnapshot.getChildren()) {
                    Request request = requestSnapshop.getValue(Request.class);
                    if (request.getStatus() != 2)
                        mRequests.add(request);
                }
                mAdapter.setRequests(mRequests);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new RequestRideAdapter(this, mRequests, new OnItemRequestListener() {
            @Override
            public void onItemClick(Request request) {
                request.setStatus(1);
                mDatabase.getReference().child("requests").child(request.getUid()).child("status").setValue(request.getStatus());

                String[] params = new String[] {
                        request.getUserRequest().getToken(),
                        "Pedido de Carona",
                        request.getUserOwner().getName() + " aceitou seu pedido de carona!",
                        "acceptRequest",
                        request.getUserOwner().getPhone()
                };

                new NotificationAsync(RequestRidesActivity.this).execute(params);
            }
        }, new OnItemRequestListener() {
            @Override
            public void onItemClick(Request request) {
                request.setStatus(2);
                mDatabase.getReference().child("requests").child(request.getUid()).child("status").setValue(request.getStatus());

                String[] params = new String[] {
                        request.getUserRequest().getToken(),
                        "Pedido de Carona",
                        request.getUserOwner().getName() + " rejeitou seu pedido de carona!",
                        "rejectRequest"
                };

                for (int i = 0; i < mRequests.size(); i++) {
                    if (mRequests.get(i).getUid().equals(request.getUid())) {
                        mRequests.remove(i);
                        break;
                    }
                }

                new NotificationAsync(RequestRidesActivity.this).execute(params);
            }
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);
    }
}
