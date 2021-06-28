package com.rctapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.rctapp.adapter.SectionPageAdpter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityMainBinding;
import com.rctapp.pages.AccountFragment;
import com.rctapp.pages.ChatFragment;
import com.rctapp.pages.HomeFragment;
import com.rctapp.pages.NotificationFragment;
import com.rctapp.utils.AllowX509TrustManager;
import com.rctapp.utils.DataApi;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final int HOME = 0;
    private static final int CHAT = 1;
    private static final int NOTIFICATION = 2;
    private static final int ACCOUNT = 3;
    UserPreference userPreference;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AllowX509TrustManager.allowAllSSL ();
       // MqttUtil.getInstance().subscribeMessage("123");
        userPreference = new UserPreference(this);
        if (!DataApi.ReadPreference("token", "token", getApplicationContext()).isEmpty()){
            try {
                DataApi.getUserInformation(DataApi.ReadPreference("token", "token", getApplicationContext()), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (userPreference.getLoggedIn() && userPreference.getRole().toLowerCase().equals("both")){
            CheckUserRole();
        }

        try {
            if (!userPreference.getLoggedIn()){
                DataApi.initAccount(getApplicationContext());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("onCreate: ", e.getMessage());
        }

        setupViewPager(binding.viewPager);
        binding.viewPager.setCurrentItem(HOME);

        binding.navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.account:
                    //replaceFragment(new SurveysFragment());
                    binding.viewPager.setCurrentItem(ACCOUNT);
                 //   binding.navigation.setSelectedItemId(item.getItemId());
                  //  binding.navigation.
                    break;
                case R.id.chat:
                    binding.viewPager.setCurrentItem(CHAT);
//                    binding.navigation.setSelectedItemId(item.getItemId());
                    //binding.navigation.setSelectedItemId(R.id.chat);
                    break;
                case R.id.notification:
                    binding.viewPager.setCurrentItem(NOTIFICATION);
                  //  binding.navigation.setSelectedItemId(item.getItemId());
                    break;
                default:
                    binding.viewPager.setCurrentItem(HOME);
                 //   binding.navigation.setSelectedItemId(item.getItemId());
                    break;
            }
            return false;
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdpter adapter = new SectionPageAdpter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new ChatFragment(), "Chat");
        adapter.addFragment(new NotificationFragment(), "Notification");
        adapter.addFragment(new AccountFragment(), "Account");
        viewPager.setAdapter(adapter);
    }

    public void CheckUserRole(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Choose Active Account");
        alert.setPositiveButton("Seller", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userPreference.setSeller(true);
                userPreference.setRole("Seller");
                userPreference.setIsBuyer(false);
            }
        }).setNegativeButton("Buyer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userPreference.setIsBuyer(true);
                userPreference.setRole("Buyer");
                userPreference.setSeller(false);
            }
        });
        alert.create().show();
    }


}