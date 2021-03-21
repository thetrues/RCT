package com.rct;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rct.database.UserPreference;
import com.rct.utils.DataApi;

public class MainActivity2 extends AppCompatActivity {
    UserPreference userPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        userPreference = new UserPreference(this);
        if (!DataApi.ReadPreference("token", "token", getApplicationContext()).isEmpty()){
            //   DataApi.getUserInformation(DataApi.ReadPreference("token", "token", getApplicationContext()), this);
        }
        if (userPreference.getLoggedIn() && userPreference.getRole().toLowerCase().equals("both")){
            CheckUserRole();
        }
        DataApi.GetVarietyServer(getApplicationContext());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_chat, R.id.navigation_notifications, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
     //   NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

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