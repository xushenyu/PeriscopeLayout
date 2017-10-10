package com.xsy.periscopelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FavorLayout favor;
    final private static String str2 = "这是master分支";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favor = (FavorLayout) findViewById(R.id.favor);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favor.addHeart(MainActivity.this);
            }
        });
    }
}
