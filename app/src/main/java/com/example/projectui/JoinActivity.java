package com.example.projectui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증 처리
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, idEditName, editBirth, editPhoneNumber; // 회원가입 입력 필드
    private Button mBtnRegister; // 회원가입 버튼
    private Button btnback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("파이어베이스 로그인 / 회원가입");

        mEtEmail = findViewById(R.id.idEditText);
        mEtPwd = findViewById(R.id.pwEditText);

        idEditName = (EditText) findViewById(R.id.idEditName);
        editBirth = (EditText) findViewById(R.id.editBirth);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        mBtnRegister = findViewById(R.id.btnJoin);
        btnback = findViewById(R.id.btnback);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString(); // 이메일
                String strPwd = mEtPwd.getText().toString(); // 비밀번호
                String strName = idEditName.getText().toString(); // 이름
                String strBirth = editBirth.getText().toString(); // 생년월일
                String strPhoneNumber = editPhoneNumber.getText().toString(); // 전화번호

                if(strEmail.isEmpty() || strPwd.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Firebase Auth 시작
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                            .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    // 정상적으로 회원가입 성공하면
                                    if(task.isSuccessful()) {
                                        // firebaseUser 객체를 만들어 회원가입된 유저를 할당
                                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                        // 사용자의 프로필을 업데이트 하기 위한 객체 생성

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(strName)
                                                .build();
                                        //유저의 이름 업데이트.
                                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });

                                        UserAccount account = new UserAccount(); // account 생성

                                        account.setIdToken(firebaseUser.getUid()); // Uid -> 고유값
                                        account.setEmailId(firebaseUser.getEmail()); // user 이메일 가져오기
                                        account.setPassword(strPwd); // user 패스워드 가져오기, 사용자가 입력한 값을 그대로 가져오는 것
                                        account.setName(strName);
                                        account.setBirth(strBirth);
                                        account.setPhoneNumber(strPhoneNumber);
                                        // setValue => 데이터베이스 insert 행위
                                        mDatabaseRef.child("UserAccount") .child(firebaseUser.getUid()).setValue(account);

                                        Toast.makeText(JoinActivity.this,"회원가입에 성공했습니다.",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(JoinActivity.this,"회원가입에 실패했습니다.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }


            }
        });

        // 뒤로가기 버튼 구현
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText( JoinActivity.this, "뒤로가기버튼을 눌렀습니다", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}