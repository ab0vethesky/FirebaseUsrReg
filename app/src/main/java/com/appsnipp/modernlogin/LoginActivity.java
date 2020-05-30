package com.appsnipp.modernlogin;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity
{
    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    TextView signup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.editTextEmail);
        logInpasswd = findViewById(R.id.editTextPassword);
        btnLogIn = findViewById(R.id.cirLoginButton);

        authStateListener =
                new FirebaseAuth.AuthStateListener()
                {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                    {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null)
                        {
                            Toast.makeText(LoginActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                            Intent I = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(I);
                        } else
                        {
                            Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        btnLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userEmail = loginEmailId.getText().toString();
                String userPaswd = logInpasswd.getText().toString();
                if (userEmail.isEmpty())
                {
                    loginEmailId.setError("Provide your Email first!");
                    loginEmailId.requestFocus();
                } else if (userPaswd.isEmpty())
                {
                    logInpasswd.setError("Enter Password!");
                    logInpasswd.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty()))
                {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Login error! Check your login details and retry", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                startActivity(new Intent(LoginActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void onLoginClick(View View)
    {
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
