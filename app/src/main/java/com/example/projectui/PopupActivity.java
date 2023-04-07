package com.example.projectui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PopupActivity extends Activity {

    TextView txtText,txtstore, txtday;

    Button deletebtn;

    MyDBHelper myHelper;
    SQLiteDatabase database;
    Cursor cursor;

    // 보관방법 데이터베이스
    DataBaseStoreHelper SHelper;
    SQLiteDatabase database2;
    Cursor cursor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Dialog popup_dialog = new Dialog(this);

        popup_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fridge_popup);

        //UI 객체생성
        txtText = (TextView)findViewById(R.id.txtResult);
        txtstore = (TextView) findViewById(R.id.txtStore);
        txtday = (TextView)findViewById(R.id.txtday);

        // 삭제버튼 객체
        deletebtn = (Button)findViewById(R.id.deletebtn);

        // 재료 데이터베이스 조작
        myHelper = new MyDBHelper(this);
        database = myHelper.getWritableDatabase();
        cursor = database.rawQuery("SELECT * FROM IngredientTable;", null);

        // 보관방법 데이터베이스 조작
        SHelper = new DataBaseStoreHelper(this);
        database2 = SHelper.getReadableDatabase();
        cursor2 = database2.rawQuery("SELECT * FROM store_method",null);

        //데이터 가져오기 - 메인에서 데이터를 가져옴 (재료 이름을 가져옴)
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        txtText.setText("재료 : " + data);

        String storeMethod = "";
        String today = "";

        for (int i = 0; i < 19; i ++ ) {
            try {
                cursor2.moveToNext();
                if(cursor2.getString(1).equals(data)) {
                    storeMethod = cursor2.getString(2);
                    txtstore.setText(storeMethod);
                }
            } catch (Exception e) {}
        }

        for(int i =0; i < 19; i++) {
            try {
                cursor.moveToNext();
                if (cursor.getString(0).equals(data)) {
                    today = cursor.getString(2);
                    txtday.setText("추가된 날짜 : " + today);
                }
            } catch (Exception e) {}
        }

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.execSQL("UPDATE IngredientTable SET iNumber = "+ 0 + " WHERE iName = '" + data+ "'");

                Intent intent = new Intent(getApplicationContext(), fridge.class);
                finish();
                startActivity(intent);

            }
        });

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", ""); // 전달되는 값 / 변수 이름 - result
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    // 부가 기능
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
