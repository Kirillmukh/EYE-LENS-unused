package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void howToUseBtnClick(View v) {
        Intent intent = new Intent(this, How_to_use.class);
        startActivity(intent);
    }

    public void changeReceptBtnClick(View v) {
        Intent intent = new Intent(this, Change_recept.class);
        startActivity(intent);
    }

    public void changeTimelineBtnClick(View v) {
        Intent intent = new Intent(this, Change_timeline.class);
        startActivity(intent);
    }

    public void howToBtnClick(View v) {
        Intent intent = new Intent(this, Videos.class);
        startActivity(intent);
    }

}