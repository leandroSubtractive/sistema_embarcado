package com.leandromendes.resourcemanagement;

import static com.leandromendes.resourcemanagement.util.AppInfo.GetTotalAppsInstall;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.leandromendes.resourcemanagement.adapters.TabsSetAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ResourceManagerLifecycle";
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private final String[] tabsName = {"Apps", "Processes"};
    private WindowManager windowManager;
    private View infoFloatingView;
    private final ActivityResultLauncher<Intent> overlayPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (Settings.canDrawOverlays(MainActivity.this)) {
                            showInfoWindow();
                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
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

        Log.d(TAG, "onCreate() called");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        TabsSetAdapter adapter = new TabsSetAdapter(this);
        viewPager2.setAdapter(adapter);

        Button showInfoButton = findViewById(R.id.button);
        showInfoButton.setText("Total Apps");
        showInfoButton.setOnClickListener(v -> requestOverlayPermission());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabsName[position])).attach();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
        if (infoFloatingView != null) {
            windowManager.removeView(infoFloatingView);
        }
    }

    private void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            overlayPermissionLauncher.launch(intent);
        } else {
            // Permission already granted, create floating window
            showInfoWindow();
        }
    }

    private void showInfoWindow() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infoFloatingView = inflater.inflate(R.layout.info_window, null);

        int layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        windowManager.addView(infoFloatingView, params);

        Button closeButton = infoFloatingView.findViewById(R.id.closeButton);
        closeButton.setText("Close");

        TextView textView = infoFloatingView.findViewById(R.id.infoView);
        textView.setText("Total Apps Installed: " + GetTotalAppsInstall(this));

        closeButton.setOnClickListener(v -> windowManager.removeView(infoFloatingView));
    }
}