package com.yank.socketapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.yank.socketapp.App.CHANNEL_ID;

public class MySocketService extends Service {

    private Socket socket;
    private InputStream is;
    private InputStreamReader in;
    private BufferedReader br;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification("идет подключение...");
        socketConnect();
        return START_NOT_STICKY;
    }

    private void socketConnect() {
        final Handler handler = new Handler();
        Thread thread = new Thread(() -> {
            try {
                //socket = new Socket("128.10.10.207", 50503);
                socket = new Socket("192.168.30.207", 50503);
                if (socket.isConnected()) showNotification("подключен");
                while (socket.isConnected()) {
                    is = socket.getInputStream();
                    in = new InputStreamReader(is);
                    br = new BufferedReader(in);
                    final String st = br.readLine();

                    handler.post(() ->{
                        System.out.println(st);
                        Toast.makeText(this, st, Toast.LENGTH_LONG).show();
                        showNotification(st);
                    });
                }
            } catch (Exception e) {
                showNotification("ошибка подключения");
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void showNotification(String text){
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Socket App")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_polymer)
                .setContentIntent(notifyPendingIntent)
                .build();

        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("destroy");
        try {
            if (is != null) is.close();
            if (in != null) in.close();
            if (br != null) br.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        System.out.println("task removed");
    }
}
