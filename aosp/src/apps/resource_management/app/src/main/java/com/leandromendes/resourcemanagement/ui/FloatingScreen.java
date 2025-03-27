package com.leandromendes.resourcemanagement.ui;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.leandromendes.resourcemanagement.util.AppInfo.GetTotalAppsInstall;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leandromendes.resourcemanagement.R;

public class FloatingScreen {
    private WindowManager windowManager;
    private LayoutInflater inflater;
    private WindowManager.LayoutParams params;
    private View infoFloatingView;
    private TextView textView;
    private Context context;

    public FloatingScreen(@NonNull Context context) {
        // Gets an instance of WindowManager to manage the floating window.
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // Gets a LayoutInflater to inflate the layout of the floating window.
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        this.context = context;
    }

    /**
     * Displays a floating window with information about the total number of installed applications.
     * <p>
     * This method creates and displays a floating window that overlays other applications on the screen.
     * The window displays the total number of applications installed on the device and a button to close it.
     */
    public void showInfoWindow() {

        // Gets and set the total number of apps on the floating screen
        textView.setText("Total Apps Installed: " + GetTotalAppsInstall(this.context));

        // Add the floating window to the screen using WindowManager
        windowManager.addView(infoFloatingView, params);
    }

    public void removeInfoWindow() {
        if (infoFloatingView != null && infoFloatingView.isAttachedToWindow()) {
            windowManager.removeView(infoFloatingView);
        }
    }

    public View getInfoFloatingView() {
        return infoFloatingView;
    }
}
