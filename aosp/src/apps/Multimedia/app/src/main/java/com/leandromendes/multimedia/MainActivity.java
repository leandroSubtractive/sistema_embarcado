package com.leandromendes.multimedia;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.leandromendes.multimedia.util.Permissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Permissions permissions = new Permissions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verify permissions
        verifyPermissions();
    }

    private void verifyPermissions(){
        List<String> permissionsNotGranted = new ArrayList<>();
        for (String permission : Permissions.requiredPermissions){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }

        if(!permissionsNotGranted.isEmpty())
        {
            requestPermissions(permissionsNotGranted.toArray(new String[0]), this.permissions.getMultiplePermissionsRequestCode());
        } else {
            Toast.makeText(this, "All permissions have been granted!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGoToSettingsDialog(List<String> deniedPermissions) {
        new AlertDialog.Builder(this)
                .setTitle("Permission denied")
                .setMessage("The following permissions are required for correct operation: "
                        + deniedPermissions.toString()
                        .replace("[", "")
                        .replace("]", "")
                        + " Please grant the permission in the application settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(this, "Limited functionality.", Toast.LENGTH_SHORT).show())
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.permissions.getMultiplePermissionsRequestCode()) {
            List<String> permissionsNotGranted = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(permissions[i])) {
                        permissionsNotGranted.add(Permissions.permissionDescription.get(permissions[i]));
                    }
                }
            }

            if (!permissionsNotGranted.isEmpty()) {
                showGoToSettingsDialog(permissionsNotGranted);
            }
        }
    }
}