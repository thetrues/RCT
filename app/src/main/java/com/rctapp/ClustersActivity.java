package com.rctapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rctapp.adapter.PlatformAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityClustersBinding;
import com.rctapp.models.PlatformModel;
import com.rctapp.platform.PlatformActivity;
import com.rctapp.utils.Api;
import com.rctapp.utils.TrustAllSSL;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.rctapp.R.string.fail_to_load_data;

public class ClustersActivity extends AppCompatActivity {
    OkHttpClient client;
    UserPreference userPreference;
    ActivityClustersBinding binding;
    ProgressDialog progressDialog;
    private PlatformAdapter platformAdapter;
    private List<PlatformModel> platformModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClustersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.clusterRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        userPreference = new UserPreference(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");

        getPlatforms();
    }

    public void getPlatforms() {
        progressDialog.show();
        Request request = new Request.Builder()
                .url(Api.main_url + Api.platforms)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        Log.d("runPlatform: ", data);
                        JSONObject object = new JSONObject(data);
                        JSONObject object1 = object.getJSONObject("data");
                        platformModelList = new ArrayList<>();
                        JSONArray array = object1.getJSONArray("platform");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject p = array.getJSONObject(i);
                            platformModelList.add(new PlatformModel(p.getString("id"), p.getString("country"),
                                    p.getString("platform_name"), p.getString("platform_country_dial_code"),
                                    p.getString("phone_number"), p.getString("platform_region"), p.getString("leader_name"), "",
                                    p.getInt("active"), p.getInt("number_of_members")));
                        }
                        platformAdapter = new PlatformAdapter(getApplicationContext(), platformModelList);
                        binding.clusterRecycler.setAdapter(platformAdapter);
                        platformAdapter.setOnPlatformClick(model -> {
                            Intent i = new Intent(getApplicationContext(), PlatformActivity.class);
                            i.putExtra("id", model.getId());
                            i.putExtra("name", model.getPlatform_name());
                            i.putExtra("region", model.getPlatform_region());
                            i.putExtra("active", model.getActive());
                            i.putExtra("country", model.getCountry());
                            i.putExtra("image", model.getImage_path());
                            i.putExtra("phone", model.getPhone_number());
                            i.putExtra("leader", model.getLeader_name());
                            i.putExtra("members", model.getNumber_of_member());
                            i.putExtra("code", model.getPlatform_country_dial_code());
                            startActivity(i);
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("run: ", e.getMessage());
                    }

                });
            }
        });
    }

}