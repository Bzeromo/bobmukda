package com.example.projectui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;


public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        //토큰을 읽어들여 사용자의 정보를 읽어 자동 로그인 실행.
        //1. 로그인 해 있는 경우 바로 실행됨.
        //2. 로그아웃을 한 상태라면 로그인이 필요함.
        try {
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if(task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        Log.d(TAG,"아이디 토큰 = " + idToken);
                        Toast.makeText(getApplicationContext(),"환영합니다",Toast.LENGTH_SHORT).show();
                        startLoadingAutoLogin();
                    }
                }
            });
        } catch(Exception e) {
                startLoadingNeedLogin();

        }

    }
    //로고를 보여주기 위한 로딩 메소드 선언.
    private void startLoadingAutoLogin() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        },2000);

        }

    private void startLoadingNeedLogin() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

        },2000);

    }

}
