package com.tw.hackmob.saferide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.tw.hackmob.saferide.model.User;
import com.tw.hackmob.saferide.utils.Data;
import com.tw.hackmob.saferide.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.name)
    EditText mName;

    @BindView(R.id.email)
    EditText mEmail;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.checkYes)
    RadioButton mCheckYes;

    @BindView(R.id.checkNo)
    RadioButton mCheckNo;

    @BindView(R.id.carLayout)
    LinearLayout mCarLayout;

    @BindView(R.id.carModel)
    EditText mCarModel;

    @BindView(R.id.plate)
    EditText mPlate;

    @BindView(R.id.color)
    EditText mColor;

    @BindView(R.id.register)
    Button mRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    User rideUser = new User();
                    rideUser.setName(mName.getText().toString());
                    rideUser.setEmail(user.getEmail());
                    rideUser.setCarModel(mCarModel.getText().toString());
                    rideUser.setPlate(mPlate.getText().toString());
                    rideUser.setColor(mPlate.getText().toString());

                    mDatabase.getReference().child("users").child(Utils.getUUID()).setValue(rideUser);

                    Data.saveUser(RegisterActivity.this, rideUser);

                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };

        mCheckYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCarLayout.setVisibility(View.VISIBLE);
            }
        });

        mCheckNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCarLayout.setVisibility(View.GONE);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, R.string.register_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
