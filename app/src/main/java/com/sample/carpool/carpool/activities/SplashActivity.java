package com.sample.carpool.carpool.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sample.carpool.carpool.R;

/**
 * Created by akash on 11-02-2018.
 */

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    TextView tv1,tv2;
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btn = findViewById(R.id.btn_splash);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Quesat Bold Italic Demo.ttf");
        TextView tv1 =  findViewById(R.id.tv_lets);
        TextView tv2 = findViewById(R.id.tv_go);
        tv1.setTypeface(tf);
        tv2.setTypeface(tf);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });






        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
*/


    }
}
