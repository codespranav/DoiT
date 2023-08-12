package com.example.doit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateNewAccountActivity extends AppCompatActivity {

    EditText email_text, password_text,confirm_password_text;
    MaterialButton createAccount;
    ProgressBar progressBar;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);
        confirm_password_text = findViewById(R.id.confirm_password_text);
        login = findViewById(R.id.login);
        createAccount = findViewById(R.id.createAccount);
        progressBar = findViewById(R.id.progressBar);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateNewAccountActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void createAccount() {
            String email = email_text.getText().toString();
            String password = password_text.getText().toString();
            String confirmPassword = confirm_password_text.getText().toString();

            boolean isValidated = validateData(email, password, confirmPassword);

            if (!isValidated){
                return;
            }
            else {
                createAccountinFirebase(email, password);
            }
    }

    void createAccountinFirebase(String email, String password){
            changeinProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Creating Account is done
                    Toast.makeText(CreateNewAccountActivity.this, "Successfully created account", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    startActivity(new Intent(CreateNewAccountActivity.this, LoginActivity.class));
                    Toast.makeText(CreateNewAccountActivity.this, "Verify email sent to your registered email address in order to login", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNewAccountActivity.this, "Failed to create an account", Toast.LENGTH_SHORT).show();
                changeinProgress(false);
                email_text.setText("");
                password_text.setText("");
                confirm_password_text.setText("");
            }
        });
    }

    void changeinProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccount.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccount.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String password, String confirmPassword){
        //Validate the data that are inputted by the user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_text.setError("Invalid email format");
            return false;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Fill up all the details", Toast.LENGTH_SHORT).show();
        }
        if (password.length()<6){
            password_text.setError("Password too short. Please enter at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)){
            confirm_password_text.setError("Password and confirm password should to be same");
            return false;
        }
        return true;
    }
}