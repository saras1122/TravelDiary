package com.example.notes_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_splash);
        lottie=findViewById(R.id.lottie);
        lottie.addAnimatorUpdateListener(
                (animation) -> {
                    // Do something.
                });
        lottie.playAnimation();

        if (lottie.isAnimating()) {
            // Do something.
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser==null){
                    startActivity(new Intent(SplashActivity.this, Login1.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
//                Intent intent =new Intent(SplashActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                //finish();
            }
        },  3500);
    }
}