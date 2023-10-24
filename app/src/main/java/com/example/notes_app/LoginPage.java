package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText email,password;
    Button button1;
    ProgressBar progressbar;
    TextView login,blink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_login_page);
        button1=findViewById(R.id.button1);
        blink=findViewById(R.id.blink);
        progressbar=findViewById(R.id.progressbar);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.buttonlogin);
        Animation animation =new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(2500);
        animation.setStartOffset(1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        blink.startAnimation(animation);
        button1.setOnClickListener(v-> createAccount());
        login.setOnClickListener(v->finish());

    }

     void createAccount() {
        String email1=email.getText().toString();
        String pass=password.getText().toString();
        boolean validate=validate(email1,pass);
        if(!validate){
            return;
        }
        createAccountInFirebase(email1,pass);
    }

     void createAccountInFirebase(String email1, String pass) {
        change(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email1,pass).addOnCompleteListener(LoginPage.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        change(false);
                        if(task.isSuccessful()){
                            //Utility.show(LoginPage.this,"verified");
                            Toast.makeText(LoginPage.this, "verified", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            Intent intent =new Intent(LoginPage.this,Login1.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginPage.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                           // Utility.show(LoginPage.this,task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }
    void change(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            button1.setVisibility(View.GONE);

        }else{
            progressbar.setVisibility(View.GONE);
            button1.setVisibility(View.VISIBLE);
        }
    }
    boolean validate(String email1,String password1){
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Enter valid ID only!!!");
            return false;
        }
        if(password.length()<6){
            password.setError("Only of 5 length");
            return false;
        }
        return true;
    }
}