package com.jayzonsolutions.lunchboxfoodmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {
    TextView sin;
    LinearLayout circle;
    TextView btnAddDish;
    TextView btnTestActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        circle = findViewById(R.id.circle);
        sin = findViewById(R.id.sin);
        btnAddDish = findViewById(R.id.btnAddDish);
        btnTestActivity = findViewById(R.id.btnTestActivity);

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this,signup.class);
                startActivity(it);

            }
        });
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,signin.class);
                startActivity(it);
            }
        });
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,AddDish.class);
                startActivity(it);
            }
        });

        btnTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,TestActivty.class);
                startActivity(it);
            }
        });

    }
}