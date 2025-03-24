package com.leandromendes.resourcemanagement.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.List;
public class AppInfo {
    @NonNull
    public static List<ApplicationInfo> GetAllAppsInfo(@NonNull Context context){
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getInstalledApplications(packageManager.GET_META_DATA);
    }
}
