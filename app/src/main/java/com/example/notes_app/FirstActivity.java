package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class FirstActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        bottomNavigationView=findViewById(R.id.bnview);
        load(new DiaryFragment());
        bottomNavigationView
                .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id=item.getItemId();
                        if(id==R.id.home1){
                            load(new DiaryFragment());
                            return true;
                        }else if(id==R.id.home2){
                            load(new MapFragment());
                            return true;
                        }else if(id==R.id.hom3){
                            load(new CalenderFragment());
                            return true;
                        } else if (id==R.id.memo) {
                            load(new MemoryFragment());
                            return true;
                        }
                        return false;
                    }
                });
    }
    public void load(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.tab,fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onStop () {
        super .onStop() ;
        startService( new Intent( this, NotificationServices. class )) ;
    }
    public void closeApp (View view) {
        finish() ;
    }
}