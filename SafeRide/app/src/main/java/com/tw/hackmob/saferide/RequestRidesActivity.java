package com.tw.hackmob.saferide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tw.hackmob.saferide.adapter.RequestRideAdapter;
import com.tw.hackmob.saferide.async.NotificationAsync;
import com.tw.hackmob.saferide.listener.OnItemRequestListener;
import com.tw.hackmob.saferide.model.Request;
import com.tw.hackmob.saferide.utils.Session;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestRidesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecycler;

    @BindView(R.id.solicitados)
    RadioButton mSolicitados;

    @BindView(R.id.aceitos)
    RadioButton mAceitos;

    private FirebaseDatabase mDatabase;
    private List<Request> mRequests = new ArrayList<>();
    private List<Request> mRequestsSolicitados = new ArrayList<>();

    private RequestRideAdapter mAdapter;
    private RequestRideAdapter mAdapterSolicitados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_rides);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("requests").orderByChild("userOwnerUid").equalTo(Session.getUser(this).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRequestsSolicitados.removeAll(mRequestsSolicitados);
                for (DataSnapshot requestSnapshop: dataSnapshot.getChildren()) {
                    Request request = requestSnapshop.getValue(Request.class);
                    if (request.getStatus() != 2)
                        mRequestsSolicitados.add(request);
                }
                mAdapter.setRequests(mRequestsSolicitados);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.getReference().child("requests").orderByChild("userRequestUid").equalTo(Session.getUser(this).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRequests.removeAll(mRequests);
                for (DataSnapshot requestSnapshop: dataSnapshot.getChildren()) {
                    Request request = requestSnapshop.getValue(Request.class);
                    if (request.getStatus() != 2)
                        mRequests.add(request);
                }
                mAdapterSolicitados.setRequests(mRequests);
                mAdapterSolicitados.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new RequestRideAdapter(this, false, mRequests, new OnItemRequestListener() {
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

        mAdapterSolicitados = new RequestRideAdapter(this, true, mRequestsSolicitados, new OnItemRequestListener() {
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

        if (mSolicitados.isChecked())
            mRecycler.setAdapter(mAdapterSolicitados);
        else if (mAceitos.isChecked())
            mRecycler.setAdapter(mAdapter);

        mSolicitados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mRecycler.setAdapter(mAdapterSolicitados);
                }
            }
        });

        mAceitos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mRecycler.setAdapter(mAdapter);
                }
            }
        });
    }
}
