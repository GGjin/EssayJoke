package com.gg.essayjoke.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gg.essayjoke.ProcessConnection;

/**
 * Creator:  GG
 * Time   :  2017/9/25
 * Mail   :  gg.jin.yu@gmail.com
 * Explain:
 */

public class MessageService extends Service {

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(MessageService.this,"绑定完成M",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接以后需要再次开启服务绑定一下
            //开启服务
            startService(new Intent(MessageService.this,GuardService.class));

            bindService(new Intent(MessageService.this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //开启服务
        bindService(new Intent(this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){};
    }
}
