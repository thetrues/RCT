package com.rct;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    public static final int SPLASH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
         }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init(){
        new Handler().postDelayed(() -> startActivity(new Intent(getApplicationContext(), MainActivity2.class)), SPLASH);

    }
}