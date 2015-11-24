package com.jackie.meidatest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnNotificate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击通知后的去处
                Intent intent = new Intent("com.jackie.meidatest.intent.notificationApp");
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                //创建通知对象并设置参数
                NotificationCompat.Builder ncb = new NotificationCompat.Builder(MainActivity.this);
                ncb.setContentTitle("来自app");//设置通知标题
                ncb.setContentText("这是app的通知");//设置通知内容
                ncb.setContentIntent(pendingIntent);//设置通知跳转的pendingIntent
                ncb.setTicker("您有一条新的app通知");//设置顶端通知小标题
                ncb.setAutoCancel(true);
                ncb.setSmallIcon(R.drawable.app_world);//设置通知图标
                ncb.setDefaults(Notification.DEFAULT_ALL);//全部设置为默认
                //得到NotificationManager对象
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //发出id为1的通知
                notificationManager.notify(1, ncb.build());
            }
        });
    }
}
