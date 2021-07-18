package com.polije.sipakost.aplikasikos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    EditText signUpEmail, signUpPwd, confirmPwd, registNama, registNoTelp;
    Spinner spLevel;
    Button btBack, btRegister;
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference db;
    List<DataProfil> dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("dataKosts");
        dp = new ArrayList<>();

        btBack = (Button) findViewById(R.id.btBack);
        btRegister = (Button) findViewById(R.id.btRegister);
        signUpEmail = (EditText) findViewById(R.id.signUpEmail);
        signUpPwd = (EditText) findViewById(R.id.signUpPwd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = signUpEmail.getText().toString().trim();
                String password = signUpPwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {

                    Toast.makeText(getApplicationContext(), getString(R.string.no_email), Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_pwd), Toast.LENGTH_SHORT).show();
                    return;
                }else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_SHORT).show();
                    return;
                 }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "Selamat Anda Telah Terdaftar!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Daftar Gagal" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, TambahProfilActivity.class));

                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
