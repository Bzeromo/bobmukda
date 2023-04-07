package com.example.projectui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    EditText idEditText;
    EditText pwEditText;

    Button btnLogin;
    Button btnJoin;

    String sql;
    Cursor cursor;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);

        idEditText = (EditText) findViewById(R.id.idEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);

        btnLogin = (Button) findViewById(R.id.loginBtn);
        btnJoin = (Button) findViewById(R.id.registerBtn);

        helper = new DatabaseOpenHelper(LoginActivity.this, DatabaseOpenHelper.tableName,null,version);

        database = helper.getWritableDatabase();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();

                if(id.length() == 0 || pw.length() ==0) {
                    // id,pw가 입력되지 않은 경우
                    Toast toast = Toast.makeText(LoginActivity.this,"아이디와 비밀번호는 필수 입력사항입니다.",Toast.LENGTH_SHORT);
                    toast.show();

                    return;
                }
                sql ="SELECT id FROM " + helper.tableName + " WHERE id = '" + id + "'";

                cursor = database.rawQuery(sql,null);

                if(cursor.getCount()!= 1) {
                    // 아이디가 틀렸을 경우
                    Toast toast = Toast.makeText(LoginActivity.this, "아이디가 존재하지 않습니다", Toast.LENGTH_SHORT);
                    toast.show();

                    return;
                }

                sql = "SELECT pw FROM " + helper.tableName + " WHERE id = '" + id + "'";

                cursor = database.rawQuery(sql,null);

                cursor.moveToNext();

                if(!pw.equals(cursor.getString(0))) {

//                if(cursor.getCount()!= 1) {
                    // 비밀번호가 틀렸을 경우
                    Toast toast = Toast.makeText(LoginActivity.this,"비밀번호가 틀렸습니다",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    // 로그인에 성공한 경우
                    Toast toast = Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_SHORT);
                    toast.show();

                    // 화면전환 인텐트 실행

                    Intent intent = new Intent (getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                cursor.close();

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 버튼
                Toast toast = Toast.makeText(LoginActivity.this,"회원가입 화면으로 이동", Toast.LENGTH_SHORT);
                toast.show();

                // 회원가입으로 화면전환 인텐트 실행
                Intent intent = new Intent(getApplicationContext(),JoinActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}