package com.leandromendes.resourcemanagement.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leandromendes.resourcemanagement.R;

import java.util.List;

public class AppViewAdapter extends RecyclerView.Adapter<AppViewAdapter.AppViewHolder> {

    private final List<ApplicationInfo> appList;
    private final PackageManager packageManager;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class AppViewHolder extends RecyclerView.ViewHolder {
        private final ImageView appIcon;
        private final TextView AppName;

        public AppViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.iconView);
            AppName = itemView.findViewById(R.id.appTextView);
        }

        public ImageView getAppIcon() {
            return appIcon;
        }

        public TextView getAppName() {
            return AppName;
        }
    }

    public AppViewAdapter(Context context, List<ApplicationInfo> appsList) {
        this.appList = appsList;
        this.packageManager = context.getPackageManager();
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        ApplicationInfo appInfo = appList.get(position);
        // contents of the view with that element
        holder.getAppName().setText(appInfo.loadLabel(packageManager));
        holder.getAppIcon().setImageDrawable(appInfo.loadIcon(packageManager));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appList.size();
    }
}
