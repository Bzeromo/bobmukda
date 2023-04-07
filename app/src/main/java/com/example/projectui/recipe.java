package com.example.projectui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class recipe extends AppCompatActivity {

    ImageView foodImg;
    TextView foodNameTv;
    TextView main_ingredient,sub_ingredient,seasoning;
    String recipe_name="", recipe_image="";
    Integer favorBool;
    Bitmap bitmap;
    ListView recipe_progress;
    ArrayList<String> recipe_step;
    Button different_recipe;
    Button favor_btn;
    LinearLayout scrollView1;
    TextView cook_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);

        Intent intent = getIntent();
        recipe_name=intent.getStringExtra("recipe_name");
        recipe_image=intent.getStringExtra("recipe_img");

        //레시피 이름 출력
        foodNameTv = (TextView) findViewById(R.id.foodNameTv);
        foodNameTv.setText(recipe_name);

        //레시피 이미지출력
        foodImg = (ImageView) findViewById(R.id.foodImg);
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(recipe_image);

                    // Web에서 이미지를 가져온 뒤
                    // ImageView에 지정할 Bitmap을 만든다
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로 부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start(); // Thread 실행

        try {
            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
            mThread.join();

            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
            foodImg.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //레시피 과정 보여주기
        recipe_step=new ArrayList<String>();
        recipe_progress=(ListView)findViewById(R.id.recipe_progress);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.recipe_progress, recipe_step);
        recipe_progress.setAdapter(adapter);

        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COOKING_NO, COOKING_DC" +
                " FROM recipe_process" +
                " WHERE RECIPE_ID =(SELECT RECIPE_ID FROM recipe_basic WHERE RECIPE_NM_KO=?)" +
                " ORDER BY COOKING_NO ASC", new String[]{recipe_name});

        while (cursor.moveToNext())
        {
            recipe_step.add(cursor.getString(0)+". "+cursor.getString(1));
            adapter.notifyDataSetChanged();

        }

        cursor.close();
        dbHelper.close();

        //스크롤뷰 안에서 레시피 과정 리스트뷰가 스크롤가능하게 하는 메소드
        scrollView1 = (LinearLayout) findViewById(R.id.scrollview1);
        recipe_progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView1.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //다른 레시피 보기 버튼 기능 구현
        different_recipe=(Button) findViewById(R.id.different_recipe);
        different_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //주재료, 부재료, 양념 보여주기
        main_ingredient = (TextView) findViewById(R.id.main_ingredient);
        sub_ingredient = (TextView) findViewById(R.id.sub_ingredient);
        seasoning = (TextView) findViewById(R.id.seasoning);

        DataBaseHelper dbHelper2 = new DataBaseHelper(this);
        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

        Cursor cursor2 = db2.rawQuery("SELECT IRDNT_NM, IRDNT_CPCTY, IRDNT_TY_NM" +
                " FROM recipe_ingredient" +
                " WHERE RECIPE_ID =(SELECT RECIPE_ID FROM recipe_basic WHERE RECIPE_NM_KO=?)" +
                " ORDER BY IRDNT_TY_NM DESC;", new String[]{recipe_name});

        String ingre1="",ingre2="",ingre3="";

        while (cursor2.moveToNext())
        {
            switch(cursor2.getString(2)){
                case "주재료":
                    ingre1+=cursor2.getString(0)+" "+cursor2.getString(1)+", ";;
                    break;
                case "부재료":
                    ingre2+=cursor2.getString(0)+" "+cursor2.getString(1)+", ";;
                    break;
                default:
                    ingre3+=cursor2.getString(0)+" "+cursor2.getString(1)+", ";;
                    break;
            }
        }

        //조리시간 보여주기
        cook_time = (TextView) findViewById(R.id.cook_time);

        String cooking_time = cook_time.getText().toString();

        DataBaseHelper dbHelper3 = new DataBaseHelper(this);
        SQLiteDatabase db3 = dbHelper3.getReadableDatabase();

        Cursor cursor3 = db3.rawQuery("select COOKING_TIME from recipe_basic" +
                " where RECIPE_ID=(SELECT RECIPE_ID from recipe_basic where RECIPE_NM_KO=? group by RECIPE_NM_KO)", new String[]{recipe_name});

        while(cursor3.moveToNext()){
            cooking_time=cursor3.getString(0);
        }

        //즐겨찾기 여부
        favor_btn = (Button) findViewById(R.id.fvrBtn);

        DataBaseHelper dbHelper4 = new DataBaseHelper(this);
        SQLiteDatabase db4 = dbHelper4.getReadableDatabase();
        SQLiteDatabase db5 = dbHelper4.getWritableDatabase();

        Cursor cursor4 = db4.rawQuery("select FAVOR from recipe_basic" +
                " where RECIPE_ID=(SELECT RECIPE_ID from recipe_basic where RECIPE_NM_KO=? group by RECIPE_NM_KO)", new String[]{recipe_name});

        while(cursor4.moveToNext()){
            favorBool=cursor4.getInt(0);
        }

        if(favorBool == 0) {
            favor_btn.setBackgroundResource(R.drawable.star);
        }
        else {
            favor_btn.setBackgroundResource(R.drawable.star_light);
        }
        //즐겨찾기 버튼 클릭
        favor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorBool == 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FAVOR", 1);
                    Toast.makeText(getApplicationContext(), "즐겨찾기로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    favor_btn.setBackgroundResource(R.drawable.star_light);
                    db5.update("recipe_basic",
                            contentValues,
                            "RECIPE_NM_KO = ?",
                            new String[]{recipe_name});
                    Cursor cursor4 = db4.rawQuery("select FAVOR from recipe_basic" +
                            " where RECIPE_ID=(SELECT RECIPE_ID from recipe_basic where RECIPE_NM_KO=? group by RECIPE_NM_KO)", new String[]{recipe_name});
                    while(cursor4.moveToNext()){
                        favorBool=cursor4.getInt(0);
                    }
                }
                else {
                    favor_btn.setBackgroundResource(R.drawable.star);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FAVOR", 0);
                    Toast.makeText(getApplicationContext(), "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    favor_btn.setBackgroundResource(R.drawable.star);
                    db5.update("recipe_basic",
                            contentValues,
                            "RECIPE_NM_KO = ?",
                            new String[]{recipe_name});
                    Cursor cursor4 = db4.rawQuery("select FAVOR from recipe_basic" +
                            " where RECIPE_ID=(SELECT RECIPE_ID from recipe_basic where RECIPE_NM_KO=? group by RECIPE_NM_KO)", new String[]{recipe_name});
                    while(cursor4.moveToNext()){
                        favorBool=cursor4.getInt(0);
                    }
                }
            }
        });

        cursor2.close();
        dbHelper2.close();

        main_ingredient.setText("주재료: "+ingre1);
        sub_ingredient.setText("부재료: "+ingre2);
        seasoning.setText(ingre3);
        cook_time.setText("(조리시간:"+cooking_time+")");


    }

}