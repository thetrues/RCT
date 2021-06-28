package com.rctapp.pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rctapp.Auth;
import com.rctapp.MainActivity2;
import com.rctapp.R;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.FragmentAccountBinding;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.Tools;
import com.rctapp.utils.TrustAllSSL;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

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
    private final int PICK_IMAGE_REQUEST = 1;
    Uri resultUri;
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
    ProgressDialog pDialog;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Uploading ...");


        if (!userPreference.getLoggedIn()){
            binding.activeAccount.setVisibility(View.GONE);
            binding.loginCard.setVisibility(View.VISIBLE);
        }else{
            if (!userPreference.getImage().isEmpty()){
                String imgURL = Api.main_url+"/rctimages/rct-upload-encoded/" + userPreference.getImage();
                Tools.displayImageOriginal(getActivity(), binding.ivProfile, imgURL);
                Log.d("onViewCreated: ", imgURL);
            }else{
                try {
                   boolean status = DataApi.UpdateProfileImage(userPreference.getToken(), getActivity(), userPreference);
                    Log.d("update profile: ", String.valueOf(status));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.d( "onViewCreated: ", e.getMessage());
                }
            }
            binding.language.setOnClickListener(view13 -> ChooseLang());
            binding.names.setText(userPreference.getName());
            if (userPreference.getRole().toLowerCase().equals("buyer")){
                binding.userType.setText(R.string.buyer);
            }else if (userPreference.getRole().toLowerCase().equals("seller")){
                binding.userType.setText(R.string.seller);
            }
            binding.uploadButton.setVisibility(View.GONE);
            binding.uploadButton.setOnClickListener(view14 -> {
                try {
                    pDialog.show();
                    boolean status = DataApi.UpdateProfileImage(userPreference.getToken(), getActivity(), userPreference);
                    if (status){
                        boolean dpStatus = DataApi.deleteDp(userPreference);
                        if (dpStatus){
                            uploadProfile();
                        }else{
                            pDialog.dismiss();
                            Toast.makeText(getActivity(), "Try again later", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        uploadProfile();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            });
          //  binding.userType.setText(userPreference.getRole().toUpperCase());
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
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image)), PICK_IMAGE_REQUEST);
            }
        });
    }

    public void setLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config;
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        config = getActivity().getResources().getConfiguration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
        userPreference.setActiveLocale(localeCode);
        startActivity(new Intent(getActivity(), MainActivity2.class));
    }

    public void ChooseLang(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Language");
        String[] items = {"Swahili", "English"};
        builder.setItems(items, (dialogInterface, i) -> {
            if (i == 0){
                setLocale("sw");
            }else{
                setLocale("eng");
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult: ", String.valueOf(requestCode));
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity(), this);
        }

         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
           if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();
               Bitmap bitmap = null;
               try {
               bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
               binding.ivProfile.setImageBitmap(bitmap);
               Log.d( "onActivityResult: ", String.valueOf(data.getExtras().get("data")));
               binding.uploadButton.setVisibility(View.VISIBLE);
               } catch (IOException e) {
                   e.printStackTrace();
               }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("onActivityResult: ", error.getMessage());
            }
        }
    }

    public void uploadProfile() throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        okhttp3.MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
        Log.d("uploadProfile: ", resultUri.toString());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytarray = stream.toByteArray();
        String name = userPreference.getUserId()+".jpg";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", name, RequestBody.create(MEDIA_TYPE_PNG, bytarray))
                .build();
        Request request = new Request.Builder()
                .url(Api.main_url+Api.upload_profile+userPreference.getUserId())
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "Fail to upload", Toast.LENGTH_SHORT).show();
                    Log.d("run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        pDialog.dismiss();
                        String data = response.body().string();
                        Log.d("runUpload: ", data);
                        if (response.code() == 200){
                            Toast.makeText(getContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                            try {
                                boolean status = DataApi.UpdateProfileImage(userPreference.getToken(), getActivity(), userPreference);
                                Log.d("update profile: ", String.valueOf(status));
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                Log.d( "onViewCreated: ", e.getMessage());
                            }
                        }else{
                            Toast.makeText(getContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}