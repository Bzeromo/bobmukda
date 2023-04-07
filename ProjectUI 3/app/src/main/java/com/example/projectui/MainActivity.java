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

public class MainActivity extends AppCompatActivity {

    ImageView recommend_recipe_button,scheduler_button,favorite_button;
    LinearLayout recipe,currentFoodLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        recommend_recipe_button = (ImageView) findViewById(R.id.recommend_recipe_button);
        scheduler_button = (ImageView) findViewById(R.id.scheduler_button);
        favorite_button = (ImageView) findViewById(R.id.favorite_button);
        recipe = (LinearLayout) findViewById(R.id.recipe) ;

        currentFoodLayout = (LinearLayout) findViewById(R.id.currentFoodLayout) ;

        recommend_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), recommend_recipe_activity.class);

                startActivity(intent);
            }
        });

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), favorite.class);

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

        currentFoodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fridge.class);

                startActivity(intent);
            }
        });






    }

}