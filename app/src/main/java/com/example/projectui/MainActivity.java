package com.example.projectui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView recommend_recipe_button,scheduler_button,favorite_button;
    LinearLayout currentFoodLayout,today_recommend_dishes;

    boolean pageopen = false;

    Animation leftanimation, rightanimation;

    LinearLayout slidingpage,todayScheduleLayout;
    Button go;

    ingredientAdapter adapter3;
    ArrayList<String> ingredient_name;
    String[] ingredient;
    ListView ingredient_listview;

    ListViewAdapter adapter;

    ListView recipe_list;

    ArrayList<String> recipe_name,recipe_img,recipe_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        recommend_recipe_button = (ImageView) findViewById(R.id.recommend_recipe_button);
        scheduler_button = (ImageView) findViewById(R.id.scheduler_button);
        favorite_button = (ImageView) findViewById(R.id.favorite_button);

        currentFoodLayout = (LinearLayout) findViewById(R.id.currentFoodLayout) ;
        today_recommend_dishes = (LinearLayout) findViewById(R.id.today_recommend_dishes);
        ingredient_listview =(ListView)findViewById(R.id.ingredient_listview);

        recipe_list = (ListView)findViewById(R.id.recipe_list);

        recipe_name = new ArrayList<>();
        recipe_info = new ArrayList<>();
        recipe_img = new ArrayList<>();

        displayList(recipe_name,recipe_info,recipe_img);


        //오늘의 일정 배너 클릭시 스케줄 화면
        todayScheduleLayout = (LinearLayout) findViewById(R.id.todayScheduleLayout);
        todayScheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), schedul.class);
                startActivity(intent);
            }
        });

        ingredient_name = new ArrayList<>();

        //냉장고 재료 넣을 문자열배열
        MyDBHelper helper2 = new MyDBHelper(this);
        SQLiteDatabase database2 = helper2.getReadableDatabase();
        Cursor cursor3;
        cursor3 = database2.rawQuery("select iName " +
                "from IngredientTable" +
                " where iNumber=1",null);

        adapter3 = new ingredientAdapter();

        while (cursor3.moveToNext()){
            ingredient_name.add(cursor3.getString(0));
        }

        for(int i=0; i<ingredient_name.size(); i++){
            adapter3.addItem(ingredient_name.get(i));

        }

        ingredient = ingredient_name.toArray(new String[ingredient_name.size()]);;

        for(int i=0; i<ingredient.length; i++){
            System.out.println(ingredient[i]);
        }

        ingredient_listview.setAdapter(adapter3);

        //슬라이딩페이지 로그아웃 기능
        Button logoutBtn = (Button) findViewById(R.id.logoutbtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText( getApplicationContext(), "로그아웃 완료", Toast.LENGTH_SHORT);
                toast.show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        slidingpage = (LinearLayout) findViewById(R.id.slidingpage);
        go = (Button) findViewById(R.id.go);

        leftanimation = AnimationUtils.loadAnimation(this,R.anim.translate_left);
        rightanimation = AnimationUtils.loadAnimation(this,R.anim.translate_right);

        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        leftanimation.setAnimationListener(animationListener);
        rightanimation.setAnimationListener(animationListener);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageopen){
                    slidingpage.startAnimation(rightanimation);
                }else{
                    slidingpage.setVisibility(View.VISIBLE);
                    slidingpage.startAnimation(leftanimation);
                }
            }
        });

    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(pageopen){
                slidingpage.setVisibility(View.INVISIBLE);

                go.setText("");
                go.setTextColor(Color.parseColor("#000000"));
                go.setTextSize(10);
                pageopen = false;
            }else{
                go.setText("Close");
                go.setTextColor(Color.parseColor("#000000"));
                go.setTextSize(10);
                pageopen=true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    //레시피 리스트 내장 db에서 받아서 보여주기
    void displayList(ArrayList<String> recipe_name, ArrayList<String> recipe_info, ArrayList<String> recipe_img){

        // 랜덤 레시피
        int num1 = (int)(Math.random()*243) + 1;

        String num1Str = Integer.toString(num1);

        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비

        recipe_name.clear();
        recipe_img.clear();
        recipe_info.clear();

        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor에서 가져옴
        Cursor cursor = database.rawQuery("SELECT * FROM recipe_basic", null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        adapter = new ListViewAdapter();

        while (cursor.moveToNext()) {
            if(cursor.getString(0).equals(num1Str)) {
                recipe_name.add(cursor.getString(1));
                recipe_img.add(cursor.getString(13));
                recipe_info.add(cursor.getString(2));
            }
        }
        for (int i = 0; i < recipe_name.size(); i++) {
            adapter.addItemToList(recipe_name.get(i), recipe_info.get(i),recipe_img.get(i));
        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        recipe_list.setAdapter(adapter);
        recipe_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(), recipe.class);

                intent.putExtra("recipe_name", recipe_name.get(i));
                intent.putExtra("recipe_img",recipe_img.get(i));

                startActivityForResult(intent,1);
            }
        });

    }


}
