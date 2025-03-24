package com.leandromendes.resourcemanagement.util;

import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

public class ProcessInfo {
    @NonNull
    public static List<ActivityManager.RunningAppProcessInfo> GetAllProcessInfo(@NonNull Context context){
       ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
       return activityManager.getRunningAppProcesses();
    }
}
