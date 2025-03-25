package com.leandromendes.resourcemanagement.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.List;

public class AppInfo {

    /**
     * Gets list of installed applications
     * <p>
     * @param context Context of the activity
     * @return Returns list of installed applications
     */
    @NonNull
    public static List<ApplicationInfo> GetAllAppsInfo(@NonNull Context context){
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getInstalledApplications(packageManager.GET_META_DATA);
    }

    /**
     * Gets the total number of applications
     * <p>
     * @param context Context of the activity
     * @return Returns the total number of applications
     */
    public static int GetTotalAppsInstall(@NonNull Context context) {
        return GetAllAppsInfo(context).size();
    }
}
