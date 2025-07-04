package com.leandromendes.multimedia.util;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
