package com.rct.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rct.Auth;
import com.rct.MainActivity2;
import com.rct.database.UserPreference;
import com.rct.databinding.FragmentAccountBinding;
import com.rct.utils.DataApi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private UserPreference userPreference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreference = new UserPreference(getActivity());

    }
    FragmentAccountBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!userPreference.getLoggedIn()){
            binding.activeAccount.setVisibility(View.GONE);
            binding.loginCard.setVisibility(View.VISIBLE);
        }else{
            binding.names.setText(userPreference.getName());
            binding.userType.setText(userPreference.getRole().toUpperCase());
            binding.phone.setText(String.format("%s%s", userPreference.getDialCode(), userPreference.getPhone()));

            binding.logout.setOnClickListener(view12 -> {
                userPreference.setIsBuyer(false);
                userPreference.setSeller(false);
                userPreference.clearSession();
                DataApi.ClearToken("token", getContext());
                DataApi.ClearToken("refreshToken", getContext());
                startActivity(new Intent(getContext(), MainActivity2.class));
            });
        }
        Log.d("onAccount ", String.valueOf(userPreference.getUserId()));
        Log.d("onAccount ", String.valueOf(userPreference.getToken()));
        Log.d("onAccount: ", DataApi.ReadPreference("token", "token", getActivity()));
        binding.login.setOnClickListener(view1 -> startActivity(new Intent(getContext(), Auth.class)));



    }
}