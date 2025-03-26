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
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private View infoFloatingView;
    private TabsSetAdapter tabsSetAdapter;
    private TextView textView;

    /**
     * ActivityResultLauncher to request the overlay permission.
     * <p>
     *  This launcher is registered to start an Activity that requests the permission
     *  android.provider.Settings#ACTION_MANAGE_OVERLAY_PERMISSION to the user.
     * <p>
     *  The result callback checks whether permission has been granted.
     *  If permission is granted, the showInfoWindow() method is called to display the floating window.
     *  If permission is denied, a Toast is displayed informing the user.
     */
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

        // Get iD from the toolbar and type in the name of the application
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));

        // Get iD from TabLayout and viewPager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        // Instantiate a new tab adapter and define it on the view page
        tabsSetAdapter = new TabsSetAdapter(this);
        viewPager2.setAdapter(tabsSetAdapter);

        // Gets an instance of WindowManager to manage the floating window.
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // Gets a LayoutInflater to inflate the layout of the floating window.
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infoFloatingView = inflater.inflate(R.layout.info_window, null);
        textView = infoFloatingView.findViewById(R.id.infoView);

        // Creates the layout parameters for the floating window
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, // Window width adjusted to content
                WindowManager.LayoutParams.WRAP_CONTENT, // Window height adjusted to content
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Layout type for overlay
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Prevents the window from receiving incoming focus
                PixelFormat.TRANSLUCENT); // Makes the background of the window translucent

        // Sets the gravity of the floating window to center it on the screen
        params.gravity = Gravity.CENTER;

        // Defines the action of the application total display button,
        // if permission has already been given, displays the screen with the information,
        // otherwise makes the request
        Button showInfoButton = findViewById(R.id.button);
        showInfoButton.setOnClickListener(v -> requestOverlayPermission());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        // It initializes the TabLayoutMediator which integrates the tablayout with the viewPager2,
        // guaranteeing correct visualization between each tab.
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabsSetAdapter.getTabsName()[position])).attach();
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

    /**
     *  Requests permission to display overlays on the screen.
     * <p>
     *  This method checks whether the application has the necessary permission to display overlays.
     *  If permission is not granted, it starts an Activity that asks the user for permission.
     *  If permission is already granted, it calls the showInfoWindow() method to create and display the floating window.
     * <p>
     *  The SYSTEM_ALERT_WINDOW permission is required to display overlays on top of other applications.
     */
    private void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            // starts an Activity that will ask the user for permission.
            overlayPermissionLauncher.launch(intent);
        } else {
            // Permission already granted, create floating window
            showInfoWindow();
        }
    }

    /**
     * Displays a floating window with information about the total number of installed applications.
     * <p>
     * This method creates and displays a floating window that overlays other applications on the screen.
     * The window displays the total number of applications installed on the device and a button to close it.
     */
    private void showInfoWindow() {

        // Gets and set the total number of apps on the floating screen
        textView.setText("Total Apps Installed: " + GetTotalAppsInstall(this));

        // Add the floating window to the screen using WindowManager
        windowManager.addView(infoFloatingView, params);

        // Gets button that closes the overlay window
        Button closeButton = infoFloatingView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> windowManager.removeView(infoFloatingView));
    }
}