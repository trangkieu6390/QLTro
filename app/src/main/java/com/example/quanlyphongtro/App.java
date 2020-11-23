package com.example.quanlyphongtro;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public  static final  String CHANNEL_1_ID="chanel";
    @Override
    public  void onCreate() {

        super.onCreate();
        createNotification();
    }

    private void createNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(
                    CHANNEL_1_ID,"Chanel 1", NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("this is chanel 1");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}