package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Change_timeline extends AppCompatActivity {

    private EditText number, period;
    Calendar cal;
    Button cnfrmBtn;
    DBHelperPeriod dbHelperPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_timeline);

        createNotificationChannel();

        number = findViewById(R.id.timeline_num);
        period = findViewById(R.id.timeline_period);
        cnfrmBtn = findViewById(R.id.page4_confirm_button);

        dbHelperPeriod = new DBHelperPeriod(this);
        SQLiteDatabase database = dbHelperPeriod.getWritableDatabase();

        cnfrmBtn.setOnClickListener(v -> {
            String secondaryNum = number.getText().toString();
            String secondaryPeriod = period.getText().toString();
            if ((secondaryNum.equals("") || secondaryNum.equals("0")) && (secondaryPeriod.equals("") || isNotPeriod(secondaryPeriod))) {
                showToast("Оба поля введены некорректно");
            } else if (!secondaryNum.equals("") && (secondaryPeriod.equals("") || isNotPeriod(secondaryPeriod))) {
                showToast("Период введен некорректно");
            } else if (secondaryNum.equals("") || secondaryNum.equals("0")) {
                showToast("Число введено некорректно");
            } else {
                int num = Integer.parseInt(number.getText().toString());
                String timeline = period.getText().toString();
                cal = getFinalDate(num, timeline);
                setNotify();
                int length = getLengthOfPeriod(num, timeline);
                int finalDay = getFinalDayOfPeriod(cal);
                ContentValues contentValues = new ContentValues();

                contentValues.put(DBHelperPeriod.FINALTIME, finalDay);
                contentValues.put(DBHelperPeriod.LENGTH, length);

                database.delete(DBHelperPeriod.TABLE_PERIOD, null, null);
                database.insert(DBHelperPeriod.TABLE_PERIOD, null, contentValues);
                showToast("Сохранено");
            }
        });
    }

    public boolean isNotPeriod(String period) {
        return !period.equals("d") && !period.equals("w") && !period.equals("m");
    }

    public Calendar getFinalDate(int num, String period) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        switch (period) {
            case "d":
                calendar.add(Calendar.DAY_OF_MONTH, num);
                break;
            case "w":
                calendar.add(Calendar.WEEK_OF_MONTH, num);
                break;
            case "m":
                calendar.add(Calendar.MONTH, num);
                break;

        }
        return calendar;
    }

    private void setNotify() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ActionNotify.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "EYELENS";
            String description = "Срок истек. Замените линзы!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("EYELENS", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getLengthOfPeriod(int n, String period) {
        int time = 0;
        switch (period) {
            case "d":
                time = n;
                break;
            case "w":
                time = n * 7;
                break;
            case "m":
                time = n * 30;
                break;
        }
        return time;
    }

    private int getFinalDayOfPeriod(Calendar calendar) {
        return (int) Math.floor(((double) calendar.getTimeInMillis()) / 1000 / 60 / 60 / 24);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperPeriod.close();
    }
}