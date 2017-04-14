package com.nothing.mark.popmoviesapp.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}