package com.leandromendes.aidl_interface;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import java.util.Random;

public class RandomKey extends Service {

    private static final String TAG = "RandomKey";
    Random random = new Random();
    private final IRandomKey.Stub binder = new IRandomKey.Stub() {
        @Override
        public int getRandomKey() throws RemoteException {
            // Generates random number in the defined range
            int key = 100000 + random.nextInt(899999);
            Log.d(TAG, "New Key generated: " + key);
            return key;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}