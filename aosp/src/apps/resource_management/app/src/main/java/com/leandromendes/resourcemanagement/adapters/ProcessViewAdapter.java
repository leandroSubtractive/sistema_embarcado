package com.leandromendes.resourcemanagement.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leandromendes.resourcemanagement.R;

import java.util.List;

public class ProcessViewAdapter extends RecyclerView.Adapter<ProcessViewAdapter.ProcessViewHolder> {

    private final List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos;
    private final ActivityManager activityManager;

    public static class ProcessViewHolder extends RecyclerView.ViewHolder{
        private final TextView procInfo;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public ProcessViewHolder(View itemView) {
            super(itemView);
            procInfo = itemView.findViewById(R.id.processTextView);
        }

        public TextView getProcInfo() {
            return procInfo;
        }
    }

    public ProcessViewAdapter(@NonNull Context context, List<ActivityManager.RunningAppProcessInfo> appProcessList) {
        this.runningAppProcessInfos = appProcessList;
        this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.process_item, parent, false);
        return new ProcessViewAdapter.ProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        int memoryUsage;

        ActivityManager.RunningAppProcessInfo processInfo = runningAppProcessInfos.get(position);

        StringBuilder stringBuffer = new StringBuilder();
        Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});
        if (memoryInfos != null && memoryInfos.length > 0) {
            memoryUsage = memoryInfos[0].getTotalPss();
        }
        else {
            memoryUsage = 0;
        }

        stringBuffer.append("\nPID: ")
                .append(processInfo.pid)
                .append("\nProcess Name: ")
                .append(processInfo.processName)
                .append("\nMemory Usage: ")
                .append(memoryUsage).append("KB");

        holder.getProcInfo().setText(stringBuffer.toString());
    }

    @Override
    public int getItemCount() {
        return runningAppProcessInfos.size();
    }
}
