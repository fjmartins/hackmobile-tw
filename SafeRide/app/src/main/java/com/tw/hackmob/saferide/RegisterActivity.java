package com.tw.hackmob.saferide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }
}
