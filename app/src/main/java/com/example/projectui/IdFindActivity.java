package com.example.projectui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;

public class IdFindActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_find_popup);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
