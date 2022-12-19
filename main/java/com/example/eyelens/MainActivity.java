package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    Button howToUse, changeRecept, changeTimeLine, typeOfLens;
    DBHelperPeriod dbHelperPeriod;
    TextView tv;

    public int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        howToUse = findViewById(R.id.page1_button1);
        changeRecept = findViewById(R.id.page1_button3);
        changeTimeLine = findViewById(R.id.period);
        typeOfLens = findViewById(R.id.page1_button2);

        tv = findViewById(R.id.page1_switch);
        tv.setVisibility(View.INVISIBLE);

        pb = findViewById(R.id.pb);
        pb.setMax(100);

        howToUse.setOnClickListener(v -> {
            Intent intent = new Intent(this, Videos.class);
            startActivity(intent);
        });

        changeRecept.setOnClickListener(v -> {
            Intent intent = new Intent(this, Change_recept.class);
            startActivity(intent);
        });

        changeTimeLine.setOnClickListener(v -> {
            Intent intent = new Intent(this, Change_timeline.class);
            startActivity(intent);
        });

        typeOfLens.setOnClickListener(v -> {
            Intent intent = new Intent(this, How_to_use.class);
            startActivity(intent);
        });

        dbHelperPeriod = new DBHelperPeriod(this);

        SQLiteDatabase database = dbHelperPeriod.getReadableDatabase();
        Cursor cursor = database.query(DBHelperPeriod.TABLE_PERIOD, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(DBHelperPeriod.FINALTIME);
            int index2 = cursor.getColumnIndex(DBHelperPeriod.LENGTH);

            double today = getToday();
            double finalDay = cursor.getInt(index1);
            double length = cursor.getInt(index2);
            double dif = ((length - (finalDay - today)) / length);
            progress = (int) Math.floor(dif * 100);

            int intDiffer = (int)(Math.ceil(finalDay - today));
            String text;
            if (intDiffer > 0) {
                String differ = String.valueOf(intDiffer);
                String day = getDay(intDiffer);
                text = "До замены " + differ + day;
            }
            else {
                text = "Пора менять линзы!!";
            }
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);

        } else {
            progress = 0;
        }

        cursor.close();
        if (progress >= 100) progress = 100;
        pb.setProgress(progress);

//        if (progress == 100) {
//            Intent intent = new Intent(this, FinalActivity.class);
//            startActivity(intent);
//        }
    }

    public double getToday() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return ((double) calendar.getTimeInMillis()) / 1000 / 60 / 60 / 24;
    }

    public String getDay(int n) {
        for (int i = 1; i < 800; i += 10) {
            if (i != 11 && i != 111 && i != 211 && i != 311 && i != 411 && i != 511 && i != 611 && i != 711 && n == i) return " день";
        }
        if ((n % 10 == 2 || n % 10 == 3 || n % 10 == 4) && (n % 100 / 10 != 1)) return " дня";
        else return " дней";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperPeriod.close();
    }
}