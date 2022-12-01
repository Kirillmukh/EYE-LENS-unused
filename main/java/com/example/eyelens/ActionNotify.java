package com.example.eyelens;

import static android.app.job.JobInfo.PRIORITY_HIGH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ActionNotify extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setPriority(PRIORITY_HIGH)
                .setContentTitle("Title here")
                .setContentText("Text here")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(false);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }
}
