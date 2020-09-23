package com.example.reminderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class NotifierAlarm extends BroadcastReceiver {

    private AppDatabase appDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
       // appDatabase = AppDatabase.geAppdatabase(context.getApplicationContext());
//        RoomDAO roomDAO = appDatabase.getRoomDAO();
//        Reminder reminder = new Reminder();
//        reminder.setTitle(intent.getStringExtra("Message"));
//        reminder.setRemindedate(new Date(intent.getStringExtra("RemindDate")));
//        reminder.setId(intent.getIntExtra("id",0));
//        //roomDAO.Delete(reminder);
//        AppDatabase.destroyInstance();


        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context,HomeActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("To",intent.getStringExtra("emailTo"));
        intent1.putExtra("body",intent.getStringExtra("emailBody"));
        intent1.putExtra("subject",intent.getStringExtra("subject"));

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(HomeActivity.class);
        taskStackBuilder.addNextIntent(intent1);
        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01","hello", NotificationManager.IMPORTANCE_HIGH);
        }

        Notification notification = builder.setContentTitle("Reminder")
                .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
                .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);

    }
}
