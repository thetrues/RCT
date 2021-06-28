package com.rctapp.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.rctapp.Auth;
import com.rctapp.R;
import com.rctapp.adapter.SectionPageAdpter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.FragmentNotificationBinding;
import com.rctapp.pages.notificattion.QouteNotification;
import com.rctapp.pages.notificattion.RequestNotification;


public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentNotificationBinding binding;
    UserPreference userPreference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPreference = new UserPreference(getContext());
        if (userPreference.getLoggedIn()) {
            setupViewPager(binding.viewPager);
            binding.tab.setupWithViewPager(binding.viewPager);
        }else{
            binding.noNotification.setVisibility(View.VISIBLE);
            binding.login.setOnClickListener(view1 -> startActivity(new Intent(getContext(), Auth.class)));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdpter adapter = new SectionPageAdpter(getChildFragmentManager());
        adapter.addFragment(new RequestNotification(), getString(R.string.requests));
        adapter.addFragment(new QouteNotification(), getString(R.string.quotes));
        viewPager.setAdapter(adapter);
    }
}