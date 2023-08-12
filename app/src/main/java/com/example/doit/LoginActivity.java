package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;

public class LoginActivity extends AppCompatActivity {
    EditText email_text, password_text;
    MaterialButton loginButton;
    ProgressBar progressBar;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        signup = findViewById(R.id.signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateNewAccountActivity.class));
            }
        });
    }
    void loginUser() {
        String email = email_text.getText().toString();
        String password = password_text.getText().toString();

        boolean isValidated = validateData(email, password);

        if (!isValidated){
            return;
        }
        else {
            loginAccountinFirebase(email, password);
        }
    }
     void loginAccountinFirebase(String email, String password) {
        changeinProgress(true);
         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         firebaseAuth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                        changeinProgress(false);
                        if (task.isSuccessful()){
                            //Means login is success
                            if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                //Go to main activity
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Email id is not verified", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            //Login Failed
                            changeinProgress(false);
                            Toast.makeText(LoginActivity.this, "Failed to login because" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                     }
                 });

    }

    void changeinProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String password){
        //Validate the data that are inputted by the user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_text.setError("Invalid email format");
            return false;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Fill up all the details", Toast.LENGTH_SHORT).show();
        }
        if (password.length()<6){
            password_text.setError("Password too short. Please enter at least 6 characters");
            return false;
        }
        return true;
    }
}