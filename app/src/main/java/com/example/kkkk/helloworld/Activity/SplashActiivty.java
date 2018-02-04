package com.example.kkkk.helloworld.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.kkkk.helloworld.R;

public class SplashActiivty extends AppCompatActivity {
    Splashhandler splashhandler;
    Handler x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_actiivty);
        showSplash();
    }

    class Splashhandler implements Runnable {

        public void run() {
            Intent intent = new Intent(SplashActiivty.this, loginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void showSplash() {

        x = new Handler();
        splashhandler = new Splashhandler();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(x!=null&&splashhandler!=null){
            x.removeCallbacks(splashhandler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(x!=null&&splashhandler!=null){
            x.postDelayed(splashhandler, 1000);
        }
    }
}
