package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.Calendar;
import java.util.Date;

public class FinalActivity extends AppCompatActivity {

    Button b1, b2, b3, b4, b5;
    DBHelperPeriod dbHelperPeriod;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        b1 = findViewById(R.id.page5_but1);
        b2 = findViewById(R.id.page5_but2);
        b3 = findViewById(R.id.page5_but3);
        b4 = findViewById(R.id.page5_but4);
        b5 = findViewById(R.id.page5_but5);

        et = findViewById(R.id.et1);

        dbHelperPeriod = new DBHelperPeriod(this);

        b1.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "EYELENS";
                String description = "Срок истек. Замените линзы!";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("EYELENS", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            setNotify(getFinalMillis().getTimeInMillis());
            SQLiteDatabase database = dbHelperPeriod.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            long finalDay = getFinalMillis().getTimeInMillis() / 1000 / 60 / 60 / 24;
            contentValues.put(DBHelperPeriod.FINALTIME, finalDay);
            database.delete(DBHelperPeriod.TABLE_PERIOD, null, null);
            database.insert(DBHelperPeriod.TABLE_PERIOD, null, contentValues);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        b2.setOnClickListener(v -> {
            SQLiteDatabase database = dbHelperPeriod.getWritableDatabase();
            database.delete(DBHelperPeriod.TABLE_PERIOD, null, null);
            Intent intent = new Intent(this, Change_timeline.class);
            startActivity(intent);
        });

        b3.setOnClickListener(v -> {
            String t = et.getText().toString();
            int n = Integer.parseInt(t);
            if (t.equals("")) Toast.makeText(this, "Введите кол-во часов справа от кнопки", Toast.LENGTH_SHORT).show();
            else if (n <= 0) Toast.makeText(this, "Введите положительное кол-во часов", Toast.LENGTH_SHORT).show();
            else {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, n);
                setNotify(calendar.getTimeInMillis());
                Toast.makeText(this, "сохранено", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });

        b4.setOnClickListener(v -> {
            SQLiteDatabase database = dbHelperPeriod.getWritableDatabase();
            database.delete(DBHelperPeriod.TABLE_PERIOD, null, null);
            Toast.makeText(this, "Период сброшен", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        b5.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void setNotify(long millis) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ActionNotify.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    private Calendar getFinalMillis() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        SQLiteDatabase database = dbHelperPeriod.getReadableDatabase();
        Cursor cursor = database.query(DBHelperPeriod.TABLE_PERIOD, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DBHelperPeriod.LENGTH);
            int value = cursor.getInt(index);
            calendar.add(Calendar.DAY_OF_MONTH, value);
        } else Log.d("mLog", "Not yeat");
        cursor.close();
        return calendar;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperPeriod.close();
    }
}