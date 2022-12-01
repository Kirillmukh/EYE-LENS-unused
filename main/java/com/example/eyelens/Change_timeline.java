package com.example.eyelens;

import static android.app.job.JobInfo.PRIORITY_HIGH;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.AvailableNetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Change_timeline extends AppCompatActivity {

    private EditText number, period;
    private static final int NOTIFY_ID = 1;
    public static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_timeline);

        createNotificationChannel();

        number = findViewById(R.id.timeline_num);
        period = findViewById(R.id.timeline_period);
        Button cnfrmBtn = findViewById(R.id.page4_confirm_button);

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
                //подключение к бд
                Calendar cal = getFinalDate(num, timeline);
                showToast("Изменения сохранены");

                Intent intent = new Intent(Change_timeline.this, ActionNotify.class);
                showToast("почти работает");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Change_timeline.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long timeAtBtnClick = System.currentTimeMillis();

                //long finalTime = cal.getTimeInMillis();
                long finalTime = 10 * 1000;


                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtBtnClick + finalTime,
                        pendingIntent);

                showToast("работает");
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    public PendingIntent getPendingIntent(){
//        Intent intent = new Intent(this, actionNotify().class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }

//    private void actionNotify(){
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
//                .setPriority(PRIORITY_HIGH)
//                .setContentTitle("Title here")
//                .setContentText("Text here")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setAutoCancel(false);
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//        managerCompat.notify(NOTIFY_ID, builder.build());
//    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}