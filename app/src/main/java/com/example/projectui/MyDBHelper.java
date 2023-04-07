package com.example.projectui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 재료테이블 helper 클래스
public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context) {
        super(context, "Ingredient_DB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 재료 테이블 생성
        db.execSQL("CREATE TABLE IngredientTable (iName CHAR(20) PRIMARY KEY," +
                "iNumber INTEGER, iPeriod TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            // 존재할 경우 구버전을 삭제하고 새롭게 재료 테이블 생성
            db.execSQL("DROP TABLE IF EXISTS IngredientTable");
            onCreate(db);
        }
        catch (IllegalStateException e) {

        }

    }
}
