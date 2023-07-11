package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login1 extends AppCompatActivity {
    EditText email1,password1;
    Button button11;
    ProgressBar progressbar;
    TextView buttonlogin1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        email1=findViewById(R.id.email1);
        password1=findViewById(R.id.password1);
        button11=findViewById(R.id.button11);
        progressbar=findViewById(R.id.progressbar);
        buttonlogin1=findViewById(R.id.buttonlogin1);
        button11.setOnClickListener(v-> loginUser());
        buttonlogin1.setOnClickListener((v) -> startActivity(new Intent(Login1.this,LoginPage.class)));
    }

    void loginUser(){
        String email2=email1.getText().toString();
        String pass=password1.getText().toString();
        boolean validate=validate(email2,pass);
        if(!validate){
            return;
        }
        createAccountInFirebase1(email2,pass);
    }

    private void createAccountInFirebase1(String email2, String pass) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        change(true);
        firebaseAuth.signInWithEmailAndPassword(email2,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                change(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login1.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(Login1.this, "not verified", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Login1.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void change(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            button11.setVisibility(View.GONE);

        }else{
            progressbar.setVisibility(View.GONE);
            button11.setVisibility(View.VISIBLE);
        }
    }
    boolean validate(String email2,String password2){
        if(!Patterns.EMAIL_ADDRESS.matcher(email2).matches()){
            email1.setError("Enter valid ID only!!!");
            return false;
        }
        if(password2.length()<6){
            password1.setError("Only of 5 length");
            return false;
        }
        return true;
    }
}