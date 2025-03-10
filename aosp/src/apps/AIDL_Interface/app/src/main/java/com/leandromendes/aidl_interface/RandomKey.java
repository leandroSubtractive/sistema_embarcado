package com.leandromendes.aidl_interface;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;


import java.util.Random;

public class RandomKey extends Service {
    Random random = new Random();
    private final IRandomKey.Stub sBinder = new IRandomKey.Stub() {
        @Override
        public int getRandomKey() throws RemoteException {
            // Generates random number in the defined range
            return 100000 + random.nextInt(899999);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }
}