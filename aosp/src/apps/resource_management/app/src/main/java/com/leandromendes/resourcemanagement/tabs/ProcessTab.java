package com.leandromendes.resourcemanagement.tabs;

import android.app.ActivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leandromendes.resourcemanagement.R;
import com.leandromendes.resourcemanagement.adapters.ProcessViewAdapter;
import com.leandromendes.resourcemanagement.util.ProcessInfo;

import java.util.List;

public class ProcessTab extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_process_info, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProcess);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = ProcessInfo.GetAllProcessInfo(getContext());
        ProcessViewAdapter processViewAdapter = new ProcessViewAdapter(getContext(), runningAppProcessInfos);
        recyclerView.setAdapter(processViewAdapter);

    }
}
