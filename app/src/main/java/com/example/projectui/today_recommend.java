package com.example.projectui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class today_recommend extends AppCompatActivity {

    ImageView back_image,home_button,recommend_recipe_button,scheduler_button,favorite_button;

    LinearLayout my_fridge;

    TodayRecommandAdapter adapter1,adapter2;

    ingredientAdapter adapter3;

    ListView recipe_list1,recipe_list2,recipe_list3,recipe_list4,ingredient_listview;
    ArrayList<String> recipe_name1,recipe_img1,recipe_info1,ingredient_name;
    ArrayList<String> recipe_name2,recipe_img2,recipe_info2;

    String[] ingredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_recommend);

        back_image = (ImageView) findViewById(R.id.backBtn);
        home_button = (ImageView) findViewById(R.id.home_button);
        recommend_recipe_button = (ImageView) findViewById(R.id.recommend_recipe_button);
        scheduler_button = (ImageView) findViewById(R.id.scheduler_button);
        favorite_button = (ImageView) findViewById(R.id.favorite_button);

        recipe_list1 = (ListView)findViewById(R.id.recipe_list1);
        recipe_list2 = (ListView)findViewById(R.id.recipe_list2);

        recipe_list3 = (ListView)findViewById(R.id.recipe_list3);
        recipe_list4 = (ListView)findViewById(R.id.recipe_list4);

        ingredient_listview =(ListView)findViewById(R.id.ingredient_listview);

        my_fridge = (LinearLayout) findViewById(R.id.my_fridge);

        my_fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fridge.class);

                startActivity(intent);
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

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

        recipe_img1 = new ArrayList<>();
        recipe_name1 = new ArrayList<>();
        recipe_info1 = new ArrayList<>();
        ingredient_name = new ArrayList<>();

        recipe_img2 = new ArrayList<>();
        recipe_name2 = new ArrayList<>();
        recipe_info2 = new ArrayList<>();

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

        ingredient_listview.setAdapter(adapter3);

        //추천 레시피
        displayList_fast(recipe_name1,recipe_info1,recipe_img1,ingredient);
        displayList_easy(recipe_name2,recipe_info2,recipe_img2,ingredient);

        recipe_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(), recipe.class);

                intent.putExtra("recipe_name", recipe_name1.get(i));
                intent.putExtra("recipe_img",recipe_img1.get(i));

                startActivityForResult(intent,1);
            }
        });

        recipe_list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(), recipe.class);

                intent.putExtra("recipe_name", recipe_name1.get(1));
                intent.putExtra("recipe_img",recipe_img1.get(1));

                startActivityForResult(intent,1);
            }
        });

        recipe_list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(), recipe.class);

                intent.putExtra("recipe_name", recipe_name2.get(i));
                intent.putExtra("recipe_img",recipe_img2.get(i));

                startActivityForResult(intent,1);
            }
        });

        recipe_list4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(), recipe.class);

                intent.putExtra("recipe_name", recipe_name2.get(1));
                intent.putExtra("recipe_img",recipe_img2.get(1));

                startActivityForResult(intent,1);
            }
        });

    }

    //가지고 있는 재료를 바탕으로 레시피 리스트 내장 db에서 받아서 보여주기
    void displayList_fast(ArrayList<String> recipe_name, ArrayList<String> recipe_info, ArrayList<String> recipe_img,String[] ingredient){
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        //조리시간 적게 걸리는 순으로
        Cursor cursor;

        String addSql="(b.IRDNT_NM=? ";
        for(int i=0;i<ingredient.length;i++){
            addSql+="OR b.IRDNT_NM like '"+"%"+ingredient[i]+"%"+"' ";
        }
        addSql+=")";

        //조리시간이 짧은 순서대로 select
        cursor = database.rawQuery("SELECT a.RECIPE_NM_KO,a.IMG_URL,a.SUMRY" +
                " FROM recipe_basic a, recipe_ingredient b" +
                " where a.RECIPE_ID = b.RECIPE_ID " +
                "and "+addSql+
                "AND b.IRDNT_TY_NM='"+"주재료"+"'" +
                "group by a.RECIPE_NM_KO" +
                " order by COOKING_TIME", null);


        //리스트뷰에 목록 채워주는 도구인 fast recipe adapter준비
        adapter1 = new TodayRecommandAdapter();
        adapter2 = new TodayRecommandAdapter();

        while (cursor.moveToNext()) {
            if(cursor.getString(0)!=null) {
                recipe_name.add(cursor.getString(0));
                recipe_img.add(cursor.getString(1));
                recipe_info.add(cursor.getString(2));
            }
        }

        try {
            adapter1.addItemToList(recipe_name.get(0), recipe_info.get(0), recipe_img.get(0));


            //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
            recipe_list1.setAdapter(adapter1);
        }catch(RuntimeException e){
            recipe_list1.setVisibility(View.INVISIBLE);
            recipe_list2.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "조회되는 최단시간 레시피가 없습니다.",Toast.LENGTH_SHORT).show();
        }

        try{
            adapter2.addItemToList(recipe_name.get(1), recipe_info.get(1), recipe_img.get(1));
            recipe_list2.setAdapter(adapter2);
        }catch(RuntimeException e){
            recipe_list2.setVisibility(View.INVISIBLE);
        }

    }

    //가지고 있는 재료를 바탕으로 레시피 리스트 내장 db에서 받아서 보여주기
    void displayList_easy(ArrayList<String> recipe_name, ArrayList<String> recipe_info, ArrayList<String> recipe_img,String[] ingredient){
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        recipe_name.clear();
        recipe_img.clear();
        recipe_info.clear();

        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        //조리시간 적게 걸리는 순으로
        Cursor cursor;

        String addSql="(b.IRDNT_NM=? ";
        for(int i=0;i<ingredient.length;i++){
            addSql+="OR b.IRDNT_NM like '"+"%"+ingredient[i]+"%"+"' ";
        }
        addSql+=")";

        //조리레벨이 초보환영인것
        cursor = database.rawQuery("SELECT a.RECIPE_NM_KO,a.IMG_URL,a.SUMRY" +
                " FROM recipe_basic a, recipe_ingredient b" +
                " where a.RECIPE_ID = b.RECIPE_ID " +
                "and "+addSql+
                "AND b.IRDNT_TY_NM='"+"주재료"+"'" +
                " and a.LEVEL_NM='"+"초보환영"+"'" +
                "group by a.RECIPE_NM_KO", null);

        //리스트뷰에 목록 채워주는 도구인 fast recipe adapter준비
        adapter1 = new TodayRecommandAdapter();
        adapter2 = new TodayRecommandAdapter();

        while (cursor.moveToNext()) {
            if(cursor.getString(0)!=null) {
                recipe_name.add(cursor.getString(0));
                recipe_img.add(cursor.getString(1));
                recipe_info.add(cursor.getString(2));


            }else{

            }
        }





        try {
            adapter1.addItemToList(recipe_name.get(0), recipe_info.get(0), recipe_img.get(0));


            //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
            recipe_list3.setAdapter(adapter1);
        }catch(RuntimeException e){
            recipe_list3.setVisibility(View.INVISIBLE);
            recipe_list4.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "조회되는 초보 레시피가 없습니다.",Toast.LENGTH_SHORT).show();
        }

        try{
            adapter2.addItemToList(recipe_name.get(1), recipe_info.get(1), recipe_img.get(1));
            recipe_list4.setAdapter(adapter2);
        }catch(RuntimeException e){
            recipe_list4.setVisibility(View.INVISIBLE);
        }


    }


}