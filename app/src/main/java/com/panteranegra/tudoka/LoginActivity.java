package com.panteranegra.tudoka;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailET, passwordET;
    Button loginBTN;
    private FirebaseAuth mAuth;
    LinearLayout splash;
    RelativeLayout loading;
    private static final String TAG = "DocSnippets";
    ProgressDialog progress;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new ProgressDialog(this);

        progress.setTitle(R.string.cargando);


        emailET = findViewById(R.id.email_login);
        passwordET = findViewById(R.id.password_login);
        loginBTN = findViewById(R.id.login_btn);

        //reviso si hay user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();



        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                if (emailET.getText().toString().isEmpty() && passwordET.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter your email and password",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loginFB(emailET.getText().toString(), passwordET.getText().toString());
                }
            }
        });
    }

    public void loginFB(final String email, final String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            progress.dismiss();
                            Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }


                    }
                });
        // [END sign_in_with_email]

    }


}