package com.leandromendes.resourcemanagement.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leandromendes.resourcemanagement.tabs.AppsTab;
import com.leandromendes.resourcemanagement.tabs.ProcessTab;

public class TabsSetAdapter extends FragmentStateAdapter {
    public TabsSetAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AppsTab();
            case 1:
                return new ProcessTab();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
