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
 * Explain:  守护进程的类编写
 */

public class GuardService extends Service {

    private final int mGuardId = 1;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(GuardService.this,"绑定完成G",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接以后需要 重新启动服务 然后绑定服务

            startService(new Intent(GuardService.this,MessageService.class));

            bindService(new Intent(GuardService.this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);

        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //能把服务设置成前台服务
//        startForeground(mGuardId, new Notification());

        //开启服务
        bindService(new Intent(this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };
    }
}
