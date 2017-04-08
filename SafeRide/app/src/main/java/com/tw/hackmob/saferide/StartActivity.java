package com.tw.hackmob.saferide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tw.hackmob.saferide.model.User;
import com.tw.hackmob.saferide.utils.Data;
import com.tw.hackmob.saferide.utils.Session;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {
    @BindView(R.id.signin)
    Button mSignin;

    @BindView(R.id.register)
    Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        User user = Data.getUser(this);
        if (user != null) {
            Session.setUser(user);

            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);
        }

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
