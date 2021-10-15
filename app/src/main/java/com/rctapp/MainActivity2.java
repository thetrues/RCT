package com.rctapp;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rctapp.database.UserPreference;
import com.rctapp.pages.notificattion.RequestNotification;
import com.rctapp.utils.AllowX509TrustManager;
import com.rctapp.utils.DataApi;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements RequestNotification.GoChat {
    UserPreference userPreference;
    NavController navController;
    FirebaseUser user;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AllowX509TrustManager.allowAllSSL ();
        userPreference = new UserPreference(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setLocale(userPreference.getActiveLocale());
        DataApi.ReadPreference("token", "token", getApplicationContext());//   DataApi.getUserInformation(DataApi.ReadPreference("token", "token", getApplicationContext()), this);
        if (userPreference.getLoggedIn() && userPreference.getRole().toLowerCase().equals("both")){
            CheckUserRole();
        }

        if (user == null && userPreference.getLoggedIn()){
            firebaseAuth(userPreference.getUserId());
            Log.d("newUser: ", "new user");
        }else if(user != null){
            Log.d( "userExist: ", user.getUid());
        }

        DataApi.GetVarietyServer(getApplicationContext());
       AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_prices, R.id.navigation_chat, R.id.navigation_notifications, R.id.navigation_account)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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

    public void setLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        userPreference.setActiveLocale(localeCode);
    }

    @Override
    public void chat() {

    }

    public void firebaseAuth(String token) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("onComplete: " , user.getUid());
                    }else{
                        Log.d("onComplete: ", task.getException().toString());
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}