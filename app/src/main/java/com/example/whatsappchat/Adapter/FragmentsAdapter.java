package com.example.whatsappchat.Adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsappchat.Fragments.CallsFragment;
import com.example.whatsappchat.Fragments.ChatsFragment;
public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:return new ChatsFragment();
        }
    }
    @Override
    public int getCount() {
        return 1;
    }
    public CharSequence getPageTitle(int position){
        String title=null;
        if(position==0){
            title="CHATS                                                        CALLS";
        }
        if(position==2){
            title="CALLS";
        }
        return title;
    }
}
