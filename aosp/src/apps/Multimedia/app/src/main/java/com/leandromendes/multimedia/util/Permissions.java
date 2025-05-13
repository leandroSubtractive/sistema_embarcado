package com.leandromendes.multimedia.util;

import android.Manifest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Permissions {
    public static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_MEDIA_AUDIO
    };

    public static final Map<String, String> permissionDescription = new HashMap<>(){{
        put(requiredPermissions[0], "Music and Audio");
    }};

    private static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = Arrays.hashCode(requiredPermissions);

    public int getMultiplePermissionsRequestCode()
    {
        return MULTIPLE_PERMISSIONS_REQUEST_CODE;
    }

}
