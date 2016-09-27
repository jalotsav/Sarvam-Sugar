/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jalotsav.sarvamsugar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.UserSessionManager;
import com.jalotsav.sarvamsugar.model.MdlUserLogin;
import com.jalotsav.sarvamsugar.navgtndrawer.NavgtnDrwrMain;
import com.jalotsav.sarvamsugar.retrofitapihelper.RetroAPI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JALOTSAV Dev. on 16/7/16.
 */
public class Login extends AppCompatActivity implements AppConstants, View.OnClickListener {

    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    TextInputLayout mTxtInptLyotUsernmEmail, mTxtInptLyotPswrd;
    TextInputEditText mTxtInptEtUsernmEmail, mTxtInptEtPswrd;
    FloatingActionButton mFabSignin;
    SwitchCompat mSwtchCmptRembrme;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_login);

        mCordntrlyotMain = (CoordinatorLayout) findViewById(R.id.cordntrlyot_login);
        mPrgrsbrMain = (ProgressBar) findViewById(R.id.prgrsbr_login_main);
        mTxtInptLyotUsernmEmail = (TextInputLayout) findViewById(R.id.txtinputlyot_login_usernmemail);
        mTxtInptLyotPswrd = (TextInputLayout) findViewById(R.id.txtinputlyot_login_paswrd);
        mTxtInptEtUsernmEmail = (TextInputEditText) findViewById(R.id.et_login_usernmemail);
        mTxtInptEtPswrd = (TextInputEditText) findViewById(R.id.et_login_paswrd);
        mFabSignin = (FloatingActionButton) findViewById(R.id.fab_login_signin);
        mSwtchCmptRembrme = (SwitchCompat) findViewById(R.id.swtchcmpt_login_rembrme);

        mFabSignin.setOnClickListener(this);

        mFabSignin.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_login)
                .color(Color.WHITE));

        mTxtInptEtUsernmEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Check validation for Username or Email
                validateUsernmEmail();
            }
        });

        mTxtInptEtPswrd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Check validation for Password
                validatePassword();
            }
        });
    }

    // Check validation for Username or Email
    private boolean validateUsernmEmail() {

        if (mTxtInptEtUsernmEmail.getText().toString().trim().isEmpty()) {
            mTxtInptLyotUsernmEmail.setErrorEnabled(true);
            mTxtInptLyotUsernmEmail.setError(getString(R.string.entr_usernm_or_email_sml));
            requestFocus(mTxtInptEtUsernmEmail);
            return false;
        } else {
            mTxtInptLyotUsernmEmail.setError(null);
            mTxtInptLyotUsernmEmail.setErrorEnabled(false);
            return true;
        }
    }

    // Check validation for Password
    private boolean validatePassword() {
        if (mTxtInptEtPswrd.getText().toString().trim().isEmpty()) {
            mTxtInptLyotPswrd.setErrorEnabled(true);
            mTxtInptLyotPswrd.setError(getString(R.string.entr_pswrd_sml));
            requestFocus(mTxtInptEtPswrd);
            return false;
        } else {
            mTxtInptLyotPswrd.setError(null);
            mTxtInptLyotPswrd.setErrorEnabled(false);
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_login_signin:

                // Check all field Validation and call API
                checkVldtnCallApi();
                break;
        }
    }

    // Check all field Validation and call API
    private void checkVldtnCallApi() {
        // TODO Auto-generated method stub

        if (!validateUsernmEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        mPrgrsbrMain.setVisibility(View.VISIBLE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apiUserLogin = objRetrofit.create(RetroAPI.class);
        Call<MdlUserLogin> callUserLogin = apiUserLogin.userLogin(API_METHOD_USERLOGIN,
                mTxtInptEtUsernmEmail.getText().toString().trim(),
                mTxtInptEtPswrd.getText().toString().trim());
        callUserLogin.enqueue(new Callback<MdlUserLogin>() {
            @Override
            public void onResponse(Call<MdlUserLogin> call, Response<MdlUserLogin> response) {

                mPrgrsbrMain.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    MdlUserLogin objMdlUserLogin = response.body();
                    String result = objMdlUserLogin.getResult();
//                    String message = objMdlUserLogin.getMessage();

                    switch (result) {
                        case RESULT_ZERO:

                            mTxtInptLyotUsernmEmail.setErrorEnabled(true);
                            mTxtInptLyotUsernmEmail.setError(getString(R.string.invalid_usernm_or_email_sml));
                            requestFocus(mTxtInptEtUsernmEmail);
                            break;
                        case RESULT_TWO:

                            mTxtInptLyotPswrd.setErrorEnabled(true);
                            mTxtInptLyotPswrd.setError(getString(R.string.invalid_pswrd_sml));
                            requestFocus(mTxtInptEtPswrd);
                            break;
                        case RESULT_ONE:

                            UserSessionManager session = new UserSessionManager(Login.this);
                            if (mSwtchCmptRembrme.isChecked()) {
                                session.setUserName(mTxtInptEtUsernmEmail.getText().toString().trim());
                                finish();
                            }

                            startActivity(new Intent(Login.this, NavgtnDrwrMain.class));
                            break;
                        default:

                            showMySnackBar(getString(R.string.there_are_some_server_prblm));
                            break;
                    }
                } else
                    showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<MdlUserLogin> call, Throwable t) {

                t.printStackTrace();
                mPrgrsbrMain.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Show SnackBar with given message
    private void showMySnackBar(String message) {

        Snackbar.make(mCordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
    }
}
