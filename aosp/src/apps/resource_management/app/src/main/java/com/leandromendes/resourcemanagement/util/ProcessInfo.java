package com.leandromendes.resourcemanagement.util;

import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

public class ProcessInfo {

    /**
     * Gets all the processes of the application that is running
     * <p>
     * @param context Context of the activity
     * @return Returns list of processes
     */
    @NonNull
    public static List<ActivityManager.RunningAppProcessInfo> GetAllProcessInfo(@NonNull Context context){
       ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       return activityManager.getRunningAppProcesses();
    }
}
