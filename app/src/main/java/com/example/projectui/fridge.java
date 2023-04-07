package com.example.projectui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

public class fridge extends AppCompatActivity {

    ImageView recommend_recipe_button,back_image,home_button,scheduler_button,favorite_button,plus_button;

    Button btnInit;

    TextView txtResult;

    // 데이터베이스 관련 객체 생성
    MyDBHelper myHelper;
    SQLiteDatabase database;
    Cursor cursor;

    // 재료이름 스트링
    String [] ingredientArr = new String[] {
            "계란", "우유", "만두", "참치", "치즈", "면", "김치",
            "닭고기", "돼지고기", "소고기", "양고기",
            "감자", "고구마", "옥수수", "밀가루",
            "양파", "파", "당근","피망"
    };

    // 재료 텍스트
    TextView [] textViews = new TextView[19];
    Integer [] textViewId = {R.id.check1,R.id.check2,R.id.check3,R.id.check4,R.id.check5,R.id.check6, R.id.check7,
            R.id.check8,R.id.check9,R.id.check10, R.id.check11,
            R.id.check12,R.id.check13,R.id.check14, R.id.check15,
            R.id.check16,R.id.check17,R.id.check18, R.id.check19 };

    ImageView [] image  = new ImageView[19]; // 아이콘 개수만큼 이미지 뷰 생성
    Integer [] imageId = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7,
            R.id.img8, R.id.img9, R.id.img10, R.id.img11,
            R.id.img12, R.id.img13, R.id.img14, R.id.img15,
            R.id.img16, R.id.img17, R.id.img18, R.id.img19 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge);

        txtResult = (TextView)findViewById(R.id.txtResult);

        recommend_recipe_button = (ImageView) findViewById(R.id.recommend_recipe_button);
        back_image = (ImageView) findViewById(R.id.back_image);
        home_button = (ImageView) findViewById(R.id.home_button);
        scheduler_button = (ImageView) findViewById(R.id.scheduler_button);
        favorite_button = (ImageView) findViewById(R.id.favorite_button);
        plus_button = (ImageView)findViewById(R.id.btnPlus);

        btnInit = (Button)findViewById(R.id.btnInit); // 초기화 버튼

        myHelper = new MyDBHelper(this);
        database = myHelper.getWritableDatabase();

        /** 냉장고 현황 **/

        // 테이블 가져오기
        cursor = database.rawQuery("SELECT * FROM IngredientTable;", null);

        for (int i = 0; i < textViewId.length; i ++ ) {
            final int index;
            index = i;
            image[index] = (ImageView) findViewById(imageId[index]);
            textViews [index] = (TextView)findViewById(textViewId[index]);

            try {
                cursor.moveToNext();

                if(!cursor.getString(1).equals("0")) {
                    image[index].setVisibility(View.VISIBLE);
                    textViews[index].setVisibility(View.VISIBLE);

                }

            } catch (Exception e) {

            }

        }

        // 이미지 클릭시 팝업 열기 - 보관방법과 추가 날짜
        for (int i = 0; i < imageId.length; i++) {
            final int index;
            index = i;
            image[index] = (ImageView) findViewById(imageId[index]);
            textViews [index] = (TextView)findViewById(textViewId[index]);

            image[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 인덱스에 해당하는 이미지 클릭시 팝업 오픈
                    popup(view); // 팝업 오픈

                    String value = ingredientArr[index];

                    Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                    intent.putExtra("data", value);
                    startActivityForResult(intent, 1);

                }
            });
        }


        // 냉장고 초기화버튼
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHelper.onUpgrade(database, 1, 2);

                for (int i = 0; i < textViewId.length; i ++ ) {
                    final int index;
                    index = i;
                    image[index] = (ImageView) findViewById(imageId[index]);
                    textViews [index] = (TextView)findViewById(textViewId[index]);

                    image[index].setVisibility(View.INVISIBLE);
                    textViews[index].setVisibility(View.INVISIBLE);

                }

                // database.close();
                Toast.makeText(getApplicationContext(),"냉장고 초기화",Toast.LENGTH_SHORT).show();

            }
        });

        recommend_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), recommend_recipe_activity.class);

                startActivity(intent);
            }
        });

        // 재료추가 시작 버튼
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cursor = database.rawQuery("SELECT * FROM ingredientTable",null);

                // 테이블이 비어있을때 테이블에 재료값들 추가
                if(cursor.getCount() == 0) {

                    for (int i = 0; i < ingredientArr.length; i ++ ) {
                        database.execSQL("INSERT INTO IngredientTable(iName,iNumber,iPeriod) VALUES"
                                + "("
                                +"'" + ingredientArr[i] + "',"
                                + 0 + ","
                                + 0
                                +")");
                    }

                }

                // 테이블이 이미 존재할 때
                else {
                    Intent intent =  new Intent(getApplicationContext(),ingredient_activity.class);

                    startActivity(intent);

                }

            }
        });

        // 뒤로가기 버튼
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });

        // 홈버튼
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });

        // 즐겨찾기 버튼
        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), favorite.class);

                startActivity(intent);
            }
        });

        // 시간표 버튼
        scheduler_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), schedul.class);

                startActivity(intent);
            }
        });

    }

    public void popup(View v){
        //데이터 담아서 팝업(액티비티) 호출

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }


}