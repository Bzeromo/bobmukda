package com.example.projectui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class favorite extends AppCompatActivity {

    ImageView back_image,home_button,recommend_recipe_button,scheduler_button;
    ArrayList<String> recipe_name,recipe_img,recipe_info,search_name;
    private SearchView mSearchView;

    ListViewAdapter adapter;

    ListView recipe_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);

        back_image = (ImageView) findViewById(R.id.back_image);
        home_button = (ImageView) findViewById(R.id.home_button);
        recommend_recipe_button = (ImageView) findViewById(R.id.recommend_recipe_button);
        scheduler_button = (ImageView) findViewById(R.id.scheduler_button);

        recipe_list = (ListView)findViewById(R.id.recipe_list);

        mSearchView = (SearchView) findViewById(R.id.searchView);

        recipe_name = new ArrayList<>();
        recipe_info = new ArrayList<>();
        recipe_img = new ArrayList<>();
        search_name = new ArrayList<>();

        displayList(recipe_name,recipe_info,recipe_img,"");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                displayList(recipe_name,recipe_info,recipe_img,newText);

                return true;
            }
        });


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

        recipe_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(), recipe.class);

                intent.putExtra("recipe_name", recipe_name.get(i));
                intent.putExtra("recipe_img",recipe_img.get(i));

                startActivityForResult(intent,1);
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

    void displayList(ArrayList<String> recipe_name, ArrayList<String> recipe_info, ArrayList<String> recipe_img, String searchtxt){
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        if(searchtxt.length()==0){
            recipe_name.clear();
            recipe_img.clear();
            recipe_info.clear();

            DataBaseHelper helper = new DataBaseHelper(this);
            SQLiteDatabase database = helper.getReadableDatabase();

            //Cursor라는 그릇에 목록을 담아주기
            //조리시간 적게 걸리는 순으로
            Cursor cursor = database.rawQuery("SELECT * FROM recipe_basic WHERE FAVOR LIKE 1 order by COOKING_TIME limit 30", null);

            //리스트뷰에 목록 채워주는 도구인 adapter준비
            adapter = new ListViewAdapter();

            while (cursor.moveToNext()){
                recipe_name.add(cursor.getString(1));
                recipe_img.add(cursor.getString(13));
                recipe_info.add(cursor.getString(2));
            }


            for(int i=0; i<recipe_name.size(); i++){
                adapter.addItemToList(recipe_name.get(i), recipe_info.get(i), recipe_img.get(i));
            }

            //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
            recipe_list.setAdapter(adapter);
        }

        else{
            recipe_name.clear();
            recipe_img.clear();
            recipe_info.clear();

            DataBaseHelper helper = new DataBaseHelper(this);
            SQLiteDatabase database = helper.getReadableDatabase();
            String nSearchtxt = "%"+searchtxt + "%";

            //Cursor라는 그릇에 목록을 담아주기
            //조리시간 적게 걸리는 순으로
            Cursor cursor = database.rawQuery("SELECT * FROM recipe_basic WHERE FAVOR LIKE 1 and RECIPE_NM_KO like ? order by COOKING_TIME limit 30", new String[]{nSearchtxt});

            //리스트뷰에 목록 채워주는 도구인 adapter준비
            adapter = new ListViewAdapter();

            while (cursor.moveToNext()){
                recipe_name.add(cursor.getString(1));
                recipe_img.add(cursor.getString(13));
                recipe_info.add(cursor.getString(2));
            }

            for(int i=0; i<recipe_name.size(); i++){
                adapter.addItemToList(recipe_name.get(i), recipe_info.get(i), recipe_img.get(i));
            }

            //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
            recipe_list.setAdapter(adapter);

        }

    }
}