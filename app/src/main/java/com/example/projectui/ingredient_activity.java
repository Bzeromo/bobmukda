package com.example.projectui;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ingredient_activity extends AppCompatActivity {

    // 돌아가기버튼과 재료추가 완료 버튼
    Button btnCheck;
    ImageView back_image;

    // 재료 정보 데이터베이스 객체 생성
    MyDBHelper myHelper;
    SQLiteDatabase database;
    Cursor cursor;

    // 재료이름 스트링
    String [] ingredientArr = new String[] {
            "계란", "우유", "만두", "참치", "치즈", "면", "김치",
            "닭고기", "돼지고기", "소고기", "양고기",
            "감자", "고구마", "옥수수", "밀가루",
            "양파", "파", "당근","피망"};

    CheckBox [] checkBoxes = new CheckBox[19];
    Integer CheckBoxId[] = {R.id.check1,R.id.check2,R.id.check3,R.id.check4,R.id.check5,R.id.check6, R.id.check7,
            R.id.check8,R.id.check9,R.id.check10, R.id.check11,
            R.id.check12,R.id.check13,R.id.check14, R.id.check15,
            R.id.check16,R.id.check17,R.id.check18, R.id.check19 };

    ImageView [] image  = new ImageView[19]; // 아이콘 개수만큼 이미지 뷰 생성
    Integer [] imageId = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7,
            R.id.img8, R.id.img9, R.id.img10, R.id.img11,
            R.id.img12, R.id.img13, R.id.img14, R.id.img15,
            R.id.img16, R.id.img17, R.id.img18, R.id.img19 };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_activity);

        back_image = (ImageView) findViewById(R.id.back_image);
        btnCheck = (Button)findViewById(R.id.btnCheck);

        myHelper = new MyDBHelper(this);

        // 데이터베이스 열기
        database = myHelper.getWritableDatabase();

        // 화면전환 인텐트
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(getApplicationContext(), fridge.class);
                startActivity(intent);
            }
        });

        // 이미지 클릭시에도 체크
        for (int i = 0; i < imageId.length; i++) {
            final int index;
            index = i;
            image[index] = (ImageView) findViewById(imageId[index]);

            checkBoxes[index] = (CheckBox) findViewById(CheckBoxId[index]);

            image[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(checkBoxes[index].isChecked()) {
                        checkBoxes[index].setChecked(false);

                    }
                    else {
                        checkBoxes[index].setChecked(true);


                    }

                }
            });
        }

        // 재료추가 버튼
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cursor = database.rawQuery("SELECT * FROM ingredientTable",null);

                cursor.moveToFirst();

                for(int i = 0; i < CheckBoxId.length; i++) {
                    final int index;
                    index = i;
                    checkBoxes[index] = (CheckBox) findViewById(CheckBoxId[index]);

                    // 체크박스가 체크되어 있는 경우
                    if(checkBoxes[index].isChecked()) {

                        // 체크되어있는 체크박스에 해당하는 재료 추가하기 0 -> 1로 추가 표현
                        database.execSQL("UPDATE IngredientTable SET iNumber = "+ 1 + " WHERE iName = '" + ingredientArr[index]+ "'");

                        // 체크되어있는 체크박스에 해당하는 재료를 추가한 날짜로 지정
                        database.execSQL("UPDATE IngredientTable SET iPeriod = date('now')  WHERE iName = '" + ingredientArr[index]+ "'");

                    }
                }

                Intent intent =  new Intent(getApplicationContext(), fridge.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"재료가 추가되었습니다",Toast.LENGTH_SHORT).show();

            }
        });

    }

}
