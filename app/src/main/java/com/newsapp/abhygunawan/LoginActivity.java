package com.newsapp.abhygunawan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.ForOverride;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.newsapp.abhygunawan.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pwd);

        progressBar = (ProgressBar) findViewById(R.id.pbar);
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.fgt_pwd);
        forgotPassword.setOnClickListener(this);
    }


    public void onClick(View v){
        switch(v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.fgt_pwd:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Dude, please enter your email, dont u wanna try this app?");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("dude dont forget @");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Where's the password? did u eat it?");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("too short");
            editTextPassword.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        //redirect
                        Toast.makeText(LoginActivity.this, "Login Successfully, welcome ", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                        finish();
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check Your Spam Email to verify your account", Toast.LENGTH_LONG * 10).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login!, please check your credential", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
