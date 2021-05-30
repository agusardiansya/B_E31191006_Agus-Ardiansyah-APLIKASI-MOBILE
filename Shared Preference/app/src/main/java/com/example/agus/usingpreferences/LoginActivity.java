package com.example.agus.usingpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText mViewUser, mViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** Menginisialisasi variable dengan Form User dan Form Password dari Layout LoginActivity */
        mViewUser = findViewById(R.id.et_emailSignin);
        mViewPassword = findViewById(R.id.et_passwordSignin);

        /** Menjalankan Method razia() Jika tombol SignIn di keyboard disentuh */
        mViewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    razia();
                    return true;
                }
                return false;
            }
        });

        /** Menjalankan Method razia() Jika merasakan tombol SignIn disentuh */
        findViewById(R.id.button_signinSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                razia();
            }
        });
        /** Ke RegisterActivity Jika merasakan tombol SignIn disentuh */
        findViewById(R.id.button_signupSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())) {
            startActivity(new Intent(getBaseContext(),MainActivity.class));
            finish();
        }
    }

    /** Mengecek inputan User dan Password dan memberikan akses ke MainActivity */
    private void razia() {
        /** Mereset semua eror dan fokus menjadi default */
        mViewUser.setError(null);
        mViewPassword.setError(null);
        View fokus = null;
        boolean cancel = false;
        /** Mengambil text dari form User dan form Password dengan variable baru bertipe String */
        String user = mViewUser.getText().toString();
        String password = mViewPassword.getText().toString();

        /** Jika form user kosong atau TIDAK memenuhi kriteria di Method cekUser() maka Set error
         *  di Form User dengan menset variable fokus dan error di View juga cancel menjadi true */
        if (TextUtils.isEmpty(user)){
            mViewUser.setError("This field is required");
            fokus = mViewUser;
            cancel = true;
        } else if (!cekUser(user)){
            mViewUser.setError("This Username is not found");
            fokus = mViewUser;
            cancel = true;
        }
        /** Jika cancel true, variable fokus mendapatka fokus */
        if (cancel) fokus.requestFocus();
        else masuk();
    }

    /** Menuju ke MainActivity dan Set User dan Status sedang login, di Preferences */
    private void masuk() {
        Preferences.setLoggedInUser(getBaseContext(), Preferences.getRegisteredUser(getBaseContext()));
        Preferences.setLoggedInStatus(getBaseContext(),true);
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    /** True jika parameter password sama dengan data password yang terdaftar dari Preferences */
    private boolean cekPassword(String password) {
        return password.equals(Preferences.getRegisteredPass(getBaseContext()));
    }
    /** True jika parameter user sama dengan data user yang terdaftar dari Preferences */
    private boolean cekUser(String user) {
        return user.equals(Preferences.getRegisteredUser(getBaseContext()));
    }
}