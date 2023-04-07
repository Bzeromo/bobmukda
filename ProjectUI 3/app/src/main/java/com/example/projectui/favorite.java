package com.example.projectui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationBarView;

public class favorite extends AppCompatActivity {

    ImageView back_image,home_button,recommend_recipe_button,scheduler_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);


        back_image = (ImageView) findViewById(R.id.back_image);
        home_button = (ImageView) findViewById(R.id.home_button);
        recommend_recipe_button = (ImageView) findViewById(R.id.recommend_recipe_button);
        scheduler_button = (ImageView) findViewById(R.id.scheduler_button);


        recommend_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), recommend_recipe_activity.class);

                startActivity(intent);
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });

        scheduler_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), schedul.class);

                startActivity(intent);
            }
        });



    }

}