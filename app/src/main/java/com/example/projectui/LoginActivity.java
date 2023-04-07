package com.example.projectui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증 처리
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd; // 로그인 입력 필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("파이어베이스 로그인 / 회원가입");

        mEtEmail = findViewById(R.id.idEditText);
        mEtPwd = findViewById(R.id.pwEditText);

        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 로그인 요청
                String strEmail = mEtEmail.getText().toString(); // 이메일
                String strPwd = mEtPwd.getText().toString(); // 비밀번호

                if(strEmail.isEmpty() || strPwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {

                    mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                // 로그인 성공
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"환영합니다",Toast.LENGTH_SHORT).show();
                                finish();
                            }
//
                            else {
                                // 로그인 실패
                                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 버튼 - intent
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
    }
    public void popup_id(View v) {
        final Dialog popup_dialog = new Dialog(this);
        popup_dialog.setContentView(R.layout.id_find_popup);

        //팝업창 모서리 둥글게
        popup_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popup_dialog.show();

        //취소버튼 누르면 팝업창 닫기
        Button cancelBtn = (Button) popup_dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_dialog.dismiss();
            }
        });

    }
    public void popup_pass(View v) {
        final Dialog popup_dialog = new Dialog(this);
        popup_dialog.setContentView(R.layout.pass_find_popup);

        //팝업창 모서리 둥글게
        popup_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popup_dialog.show();

        //취소버튼 누르면 팝업창 닫기
        Button cancelBtn = (Button) popup_dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_dialog.dismiss();
            }
        });

    }

}