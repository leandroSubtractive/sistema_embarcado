package com.leandromendes.resourcemanagement.tabs;

import android.content.pm.ApplicationInfo;
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
import com.leandromendes.resourcemanagement.adapters.AppViewAdapter;
import com.leandromendes.resourcemanagement.util.AppInfo;

import java.util.List;

public class AppsTab extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_apps_info, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewApps);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ApplicationInfo> allAppsInstalled = AppInfo.GetAllAppsInfo(getContext());
        AppViewAdapter appViewAdapter = new AppViewAdapter(getContext(), allAppsInstalled);
        recyclerView.setAdapter(appViewAdapter);

    }
}
