package com.rctapp.platform;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rctapp.Auth;
import com.rctapp.R;
import com.rctapp.adapter.SellerMultipleSelectAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityPlatformBinding;
import com.rctapp.models.SellerModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.Tools;
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

public class PlatformActivity extends AppCompatActivity {
    ActivityPlatformBinding binding;
    OkHttpClient client;
    List<SellerModel> sellerModelList;
    SellerMultipleSelectAdapter sellersAdapter;
    String gradeSelected = "1";
    UserPreference userPreference;
    StringBuilder sellers;
    List<String> sellerIds;
    String variety;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlatformBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userPreference = new UserPreference(this);
        sellers = new StringBuilder();
        sellerIds = new ArrayList<>();
        Tools.NetPolicy();
        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        Intent i = getIntent();
        binding.sellerRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        setTitle(i.getExtras().getString("name"));
        getSellers(i.getExtras().getString("id"));
        binding.giveTender.setVisibility(View.GONE);
        binding.giveTender.setOnClickListener(view1 -> GiveTenderMany(sellerIds));
    }

    public void getSellers(String platformId){
        Request request = new Request.Builder()
                .url(Api.get_seller_by_platform(platformId))
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               runOnUiThread(() -> Log.d( "run: ", e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               runOnUiThread(() -> {
                    try {
                        String data = response.body().string();
                        JSONObject object = new JSONObject(data);
                        sellerModelList = new ArrayList<>();
                        JSONObject object1 = object.getJSONObject("data");
                        JSONArray array = object1.getJSONArray("sellerInformations");
                        Log.d( "onResponse: ", data);
                        for (int i=0; i<array.length(); i++){
                            JSONObject s = array.getJSONObject(i);
                            sellerModelList.add(new SellerModel(s.getString("id"), s.getString("category"), s.getString("full_name"),
                                    s.getString("dial_code"), s.getString("phone_number"), s.getString("platform_name"), s.getString("platform_leader"),
                                    s.getString("location"), s.getString("card_type"), s.getString("application_type"),
                                    s.getString("is_tbs_certified"), s.getString("image_path"), s.getString("certificate_path"),
                                    s.getString("card_path"), s.getInt("is_graded")));
                        }
                        sellersAdapter = new SellerMultipleSelectAdapter(PlatformActivity.this, sellerModelList);
                        binding.sellerRecycler.setAdapter(sellersAdapter);
                        sellersAdapter.setOnSellerClick(model -> SellerDialog(model));
                        sellersAdapter.setOnCheckBoxClick(model -> {
                            if (!sellerIds.contains(model.getId())) {
                                sellerIds.add(model.getId());
                                sellers.append(model.getId());
                            }else {
                                sellerIds.remove(model.getId());
                            }
                            Log.d("OnCheckClick: ", sellerIds.toString());
                            if (sellerIds.size() !=0){
                                binding.giveTender.setVisibility(View.VISIBLE);
                            }else {
                                binding.giveTender.setVisibility(View.GONE);
                            }
                        });


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("onResponse: ", e.getMessage());
                    }

                });
            }
        });
    }


    public void SellerDialog(SellerModel model){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.seller_details, null);
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.names)).setText(model.getFull_name());
        ((TextView) dialog.findViewById(R.id.phone)).setText(model.getPhone_number());
        ((TextView) dialog.findViewById(R.id.platformName)).setText(model.getPlatform_name());
        ((TextView) dialog.findViewById(R.id.location)).setText(model.getLocation());
        ((TextView) dialog.findViewById(R.id.category)).setText(model.getApplication_type());
        ((TextView) dialog.findViewById(R.id.size)).setText(model.getCategory());
        dialog.findViewById(R.id.bt_close).setOnClickListener(view -> dialog.dismiss());
        dialog.findViewById(R.id.giveTender).setOnClickListener(view -> {
            if (userPreference.getLoggedIn()){
                GiveTender(model);
            }else{
               Login();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void GiveTender(SellerModel model) {
        final Dialog tenderDialog = new Dialog(this);
        tenderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        tenderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.give_tender_dialog, null);
        tenderDialog.setContentView(mView);
        tenderDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(tenderDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Spinner gr = tenderDialog.findViewById(R.id.spinnerGrade);
        String[] grade = {"1", "2", "3"};
        ((TextView) tenderDialog.findViewById(R.id.names)).setText(model.getFull_name());
        tenderDialog.findViewById(R.id.bt_close).setOnClickListener(view -> tenderDialog.dismiss());

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grade);
        gr.setAdapter(gradeAdapter);
        gr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gradeSelected = grade[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tenderDialog.findViewById(R.id.giveTender).setOnClickListener(view -> {
            String quantity = ((EditText) tenderDialog.findViewById(R.id.quantity)).getText().toString().trim();
            String pickup = ((EditText) tenderDialog.findViewById(R.id.location)).getText().toString().trim();
            String variety = ((EditText) tenderDialog.findViewById(R.id.variety)).getText().toString().trim();
            Log.d("onClick: ", gradeSelected);
            JSONObject object = new JSONObject();
            try {
                JSONObject usd = new JSONObject();
                usd.put("id", model.getId());
                JSONArray array = new JSONArray();
                array.put(usd);
                JSONObject seller_selection = new JSONObject();
                seller_selection.put("seller_id", array);
                object.put("quantity", quantity);
                object.put("grade", gradeSelected);
                object.put("variety", variety);
                object.put("seller_selection", seller_selection);
                Log.d("onClick1: ", object.toString());

                DataApi.GiveTender(object, PlatformActivity.this, userPreference.getToken());

                Toast.makeText(PlatformActivity.this, "Success", Toast.LENGTH_SHORT).show();

                tenderDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        tenderDialog.create();
        tenderDialog.show();
    }


    public void GiveTenderMany(List<String> ids) {
        final Dialog tenderDialog = new Dialog(this);
        tenderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        tenderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.give_tender_dialog, null);
        tenderDialog.setContentView(mView);
        tenderDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(tenderDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        tenderDialog.findViewById(R.id.bt_close).setOnClickListener(view -> tenderDialog.dismiss());

        Spinner gr = tenderDialog.findViewById(R.id.spinnerGrade);
        Spinner vr = tenderDialog.findViewById(R.id.spinnerVariety);
        String[] grade = {"1", "2", "3"};
        ((TextView) tenderDialog.findViewById(R.id.names)).setText(String.format("Give %s Sellers Tender", ids.size()));

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grade);
        gr.setAdapter(gradeAdapter);
        gr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gradeSelected = grade[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> varietyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataApi.getVariety(this));
        vr.setAdapter(varietyAdapter);
        vr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variety = DataApi.getVariety(getApplicationContext()).get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



            tenderDialog.findViewById(R.id.giveTender).setOnClickListener(view -> {
                String quantity = ((EditText) tenderDialog.findViewById(R.id.quantity)).getText().toString().trim();
                String pickup = ((EditText) tenderDialog.findViewById(R.id.location)).getText().toString().trim();
                Log.d("onClick: ", gradeSelected);
                JSONObject object = new JSONObject();
                try {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < ids.size(); i++) {
                        JSONObject usd = new JSONObject();
                        usd.put("id", ids.get(i));
                        array.put(usd);
                    }

                    JSONObject seller_selection = new JSONObject();
                    seller_selection.put("seller_id", array);
                    object.put("quantity", quantity);
                    object.put("grade", gradeSelected);
                    object.put("location", pickup);
                    object.put("variety", variety);
                    object.put("seller_selection", seller_selection);
                    Log.d("onClick1: ", object.toString());
                    if (!quantity.isEmpty() && !variety.isEmpty() && !pickup.isEmpty()) {
                        if (userPreference.getLoggedIn()) {
                            DataApi.GiveTender(object, this, userPreference.getToken());
                            Log.d("GiveTenderMany: ", object.toString());
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Login();
                        }
                    } else {
                        Toast.makeText(this, "Fill all inputs", Toast.LENGTH_SHORT).show();
                        ((EditText) tenderDialog.findViewById(R.id.quantity)).setError("Please add quantity");
                    }

                    tenderDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        tenderDialog.create();
        tenderDialog.show();
    }


    public void Login(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("You must login to continue");
        alert.setPositiveButton("Login", (dialogInterface, i) -> startActivity(new Intent(this, Auth.class))).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }});
        alert.create().show();
    }


}