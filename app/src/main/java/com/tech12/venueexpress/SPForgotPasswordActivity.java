package com.tech12.venueexpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SPForgotPasswordActivity extends AppCompatActivity {
    EditText email;
    Button reset_password;
    View view;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spforgot_password);
        email = findViewById(R.id.sp_forgot_password_email);
        reset_password = findViewById(R.id.btn_sp_reset_password);

        firebaseAuth=FirebaseAuth.getInstance();
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Password sent to your email", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),CustomerLoginActivity.class));
                            finish();

                        }
                        else{
                            email.setText("");
                            Snackbar.make(view, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                });
            }
        });
    }
}
