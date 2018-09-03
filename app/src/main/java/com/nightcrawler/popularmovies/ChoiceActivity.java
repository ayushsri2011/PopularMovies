package com.nightcrawler.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoiceActivity extends AppCompatActivity {
    @BindView(R.id.btn_movies) TextView btn_movies;
    @BindView(R.id.btn_tv) TextView btn_tv;
    @BindView(R.id.btn_people) TextView btn_people;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        ButterKnife.bind(this);

//        btn_movies=(TextView)findViewById(R.id.btn_movies);
//        btn_tv=(TextView)findViewById(R.id.btn_tv);
//        btn_else=(Button)findViewById(R.id.btn_else);

        if(!CommonUtils.checkConnectivity(getBaseContext()))
            Toast.makeText(this, "Ensure net connectivity to proceed", Toast.LENGTH_SHORT).show();

        btn_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoiceActivity.this,MainActivity.class);
                intent.putExtra("category","movie");
                startActivity(intent);
            }
        });

        btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoiceActivity.this,MainActivity.class);
                intent.putExtra("category","tv");
                startActivity(intent);
            }
        });

        btn_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoiceActivity.this,MainActivity.class);
                intent.putExtra("category","person");
                startActivity(intent);
            }
        });

    }
}
