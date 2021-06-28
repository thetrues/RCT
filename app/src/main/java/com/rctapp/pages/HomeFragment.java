package com.rctapp.pages;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.Auth;
import com.rctapp.BuyersActivity;
import com.rctapp.ClustersActivity;
import com.rctapp.R;
import com.rctapp.SellersByVarietyActivity;
import com.rctapp.adapter.BuyerRecyclerAdapter;
import com.rctapp.adapter.PlatformAdapter;
import com.rctapp.adapter.SellersAdapter;
import com.rctapp.adapter.TenderRecyclerAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.FragmentHomeBinding;
import com.rctapp.fragments.GiveTender;
import com.rctapp.fragments.SendQuote;
import com.rctapp.fragments.TenderRequest;
import com.rctapp.models.BuyerModel;
import com.rctapp.models.PlatformModel;
import com.rctapp.models.SellerModel;
import com.rctapp.models.TenderModel;
import com.rctapp.platform.PlatformActivity;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.Tools;
import com.rctapp.utils.TrustAllSSL;
import com.rctapp.utils.ViewAnimation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

import static com.rctapp.R.string.fail_to_load_data;
import static com.rctapp.R.string.fill_all_inputs;
import static com.rctapp.R.string.quote_sent;
import static com.rctapp.R.string.success;
import static com.rctapp.R.string.try_again;
import static com.rctapp.utils.Tools.toggleArrow;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private SellersAdapter sellersAdapter;
    private PlatformAdapter platformAdapter;
    private List<SellerModel> sellerModelList;
    private List<PlatformModel> platformModelList;
    AlertDialog alertDialog;
    OkHttpClient client;
    UserPreference userPreference;
    String gradeSelected = "1";
    List<TenderModel> tenderModels;
    private TenderRecyclerAdapter tenderRecyclerAdapter;
    private List<BuyerModel> buyerModels;
    private BuyerRecyclerAdapter buyerRecyclerAdapter;
    int Grade, Cert;
    AlertDialog dialog;
    ProgressDialog progressDialog;
    String variety;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.platformRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.sellersRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.tenderRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.buyersRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        userPreference = new UserPreference(getContext());
        Tools.NetPolicy();

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        client =  new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ...");


        if (userPreference.getSeller()) {
            getRecentRequest();
            getBuyers();
            binding.tvSeller.setVisibility(View.GONE);
            binding.tvSellerSub.setVisibility(View.GONE);
            binding.viewAllSeller.setVisibility(View.GONE);
            binding.tvPlatform.setVisibility(View.GONE);
            binding.tvPlatformSub.setVisibility(View.GONE);
            binding.viewAllCluster.setVisibility(View.GONE);
            binding.viewAllBuyer.setVisibility(View.VISIBLE);
            binding.tvBuyer.setVisibility(View.VISIBLE);
            binding.tvBuyerSub.setVisibility(View.VISIBLE);
            binding.tvTender.setVisibility(View.VISIBLE);
            binding.tvTenderSub.setVisibility(View.VISIBLE);

        }else{
            getSellers();
            getPlatforms();
            binding.tvSeller.setVisibility(View.VISIBLE);
            binding.tvSellerSub.setVisibility(View.VISIBLE);
            binding.tvPlatform.setVisibility(View.VISIBLE);
            binding.tvPlatformSub.setVisibility(View.VISIBLE);
            binding.viewAllSeller.setVisibility(View.VISIBLE);
            binding.viewAllCluster.setVisibility(View.VISIBLE);
            binding.tvBuyer.setVisibility(View.GONE);
            binding.tvBuyerSub.setVisibility(View.GONE);
            binding.tvTender.setVisibility(View.GONE);
            binding.tvTenderSub.setVisibility(View.GONE);
            binding.viewAllBuyer.setVisibility(View.GONE);


        }

        binding.lytExpandText.setVisibility(View.GONE);
        binding.lytExpandText2.setVisibility(View.GONE);
        binding.lytExpandText3.setVisibility(View.GONE);
        binding.btToggleText.setOnClickListener(view -> toggleSectionText(binding.btToggleText));
        binding.btToggleText2.setOnClickListener(view -> toggleSectionText2(binding.btToggleText2));
        binding.btToggleText3.setOnClickListener(view -> toggleSectionText3(binding.btToggleText3));

        binding.viewAllSeller.setOnClickListener(view -> ViewSeller());
        binding.viewAllBuyer.setOnClickListener(view -> startActivity(new Intent(getContext(), BuyersActivity.class)));
        binding.viewAllCluster.setOnClickListener(view -> startActivity(new Intent(getContext(), ClustersActivity.class)));
    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(binding.lytExpandText, () -> Tools.nestedScrollTo(binding.nestScrol, binding.lytExpandText));
        } else {
            ViewAnimation.collapse(binding.lytExpandText);
        }
    }

    private void toggleSectionText2(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(binding.lytExpandText2, () -> Tools.nestedScrollTo(binding.nestScrol, binding.lytExpandText2));
        } else {
            ViewAnimation.collapse(binding.lytExpandText2);
        }
    }

    private void toggleSectionText3(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(binding.lytExpandText3, () -> Tools.nestedScrollTo(binding.nestScrol, binding.lytExpandText3));
        } else {
            ViewAnimation.collapse(binding.lytExpandText3);
        }
    }


    public void getSellers() {
        progressDialog.show();
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxStale(60, TimeUnit.MINUTES)
                        .build())
                .url(Api.main_url + Api.sellers)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                   // getActivity().runOnUiThread(() -> Log.d("run: ", e.getMessage()));
                }catch (Exception ex){
                    Log.d("onFailure: " , ex.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                getActivity().runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        JSONObject object = new JSONObject(data);
                        sellerModelList = new ArrayList<>();
                        JSONObject object1 = object.getJSONObject("data");
                        JSONArray array = object1.getJSONArray("sellerInformations");
                        Log.d("onResponse: ", data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject s = array.getJSONObject(i);
                            sellerModelList.add(new SellerModel(s.getString("id"), s.getString("category"), s.getString("full_name"),
                                    s.getString("dial_code"), s.getString("phone_number"), s.getString("platform_name"), s.getString("platform_leader"),
                                    s.getString("location"), s.getString("card_type"), s.getString("application_type"),
                                    s.getString("is_tbs_certified"), s.getString("image_path"), s.getString("certificate_path"),
                                    s.getString("card_path"), s.getInt("is_graded")));
                        }
                        sellersAdapter = new SellersAdapter(getContext(), sellerModelList);
                        binding.sellersRecycler.setAdapter(sellersAdapter);
                        sellersAdapter.setOnSellerClick(model -> SellerDialog(model));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("onResponse: ", e.getMessage());
                    }

                });
            }
        });
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
                getActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                getActivity().runOnUiThread(() -> {
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
                                    p.getString("phone_number"), p.getString("platform_region"), p.getString("leader_name"), p.getString("image_path"),
                                    p.getInt("active"), p.getInt("number_of_members")));
                        }
                        platformAdapter = new PlatformAdapter(getActivity(), platformModelList);
                        binding.platformRecycler.setAdapter(platformAdapter);
                        platformAdapter.setOnPlatformClick(model -> {
                            Intent i = new Intent(getActivity(), PlatformActivity.class);
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

    public void SellerDialog(SellerModel model) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.seller_details, null);
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (!model.getImage_path().isEmpty()){
            String imgURL = "http://142.93.210.105/rctimages/rct-upload-encoded/" + model.getImage_path();
            Tools.displayImageOriginal(getActivity(), dialog.findViewById(R.id.iv_profile2), imgURL);
        }


        ((TextView) dialog.findViewById(R.id.names)).setText(model.getFull_name());
        ((TextView) dialog.findViewById(R.id.phone)).setText(model.getPhone_number());
        ((TextView) dialog.findViewById(R.id.platformName)).setText(model.getPlatform_name());
        ((TextView) dialog.findViewById(R.id.location)).setText(model.getLocation());
        ((TextView) dialog.findViewById(R.id.category)).setText(model.getApplication_type());
        ((TextView) dialog.findViewById(R.id.size)).setText(model.getCategory());
        dialog.findViewById(R.id.bt_close).setOnClickListener(view -> dialog.dismiss());
        dialog.findViewById(R.id.giveTender).setOnClickListener((View.OnClickListener) view -> {
            if (userPreference.getLoggedIn() && userPreference.getBuyer()) {
               // GiveTender(model);
                GiveTender giveTender = new GiveTender();
                giveTender.setSellerModel(model);
                giveTender.setUserPreference(userPreference);
                giveTender.show(getChildFragmentManager(), giveTender.getTag());
                dialog.dismiss();
            } else {
                Login();
                //Toast.makeText(getContext(), "You need to login", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void GiveTender(SellerModel model) {
        final Dialog tenderDialog = new Dialog(getContext());
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
        Spinner vr = tenderDialog.findViewById(R.id.spinnerVariety);

        String[] grade = {"1", "2", "3"};


        ((TextView) tenderDialog.findViewById(R.id.names)).setText(model.getFull_name());

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, grade);
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

        ArrayAdapter<String> veriAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, DataApi.getVariety(getContext()));
        vr.setAdapter(veriAdapter);
        vr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variety = DataApi.getVariety(getContext()).get(i);
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
                JSONObject usd = new JSONObject();
                usd.put("id", model.getId());
                JSONArray array = new JSONArray();
                array.put(usd);
                JSONObject seller_selection = new JSONObject();
                seller_selection.put("seller_id", array);
                object.put("quantity", quantity);
                object.put("grade", gradeSelected);
                object.put("location", pickup);
                object.put("variety", variety);
                object.put("seller_selection", seller_selection);
                Log.d("onClick1: ", object.toString());
                if (!quantity.isEmpty() && !variety.isEmpty() && !pickup.isEmpty()){
                    DataApi.GiveTender(object, getContext(), userPreference.getToken());
                }else {
                    Toast.makeText(getContext(), fill_all_inputs, Toast.LENGTH_SHORT).show();
                    ((EditText) tenderDialog.findViewById(R.id.quantity)).setError(getString(R.string.please_add_quantity));
                }
                Toast.makeText(getContext(), success, Toast.LENGTH_SHORT).show();
                tenderDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        tenderDialog.findViewById(R.id.bt_close).setOnClickListener(view -> tenderDialog.dismiss());
        tenderDialog.create();
        tenderDialog.show();
    }

    public void getRecentRequest(){
        progressDialog.dismiss();
        Request request = new Request.Builder()
                .url(Api.main_url+Api.get_recent_tender)
                .addHeader("Authorization", userPreference.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                    Log.d("run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                getActivity().runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        Log.d("runTender: ", data);
                        tenderModels = new ArrayList<>();
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i<array.length();i++){
                            JSONObject o = array.getJSONObject(i);
                            tenderModels.add(new TenderModel(o.getString("id"), String.valueOf(o.getLong("quantity")),
                                    String.valueOf(o.getLong("grade")), o.getString("variety"),
                                    "","", "", "", o.getString("created_time")));
                        }
                        Collections.reverse(tenderModels);
                        tenderRecyclerAdapter = new TenderRecyclerAdapter(getActivity(), tenderModels);
                        binding.tenderRecycler.setAdapter(tenderRecyclerAdapter);
                        tenderRecyclerAdapter.setOnTenderClick(model -> {
                           // ReviewTender(model);
                            SendQuote sendQuote = new SendQuote();
                            sendQuote.setTenderModel(model);
                            sendQuote.setUserPreference(userPreference);
                            sendQuote.show(getChildFragmentManager(), sendQuote.getTag());
                        });

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("runTend: ", e.getMessage());
                    }
                });
            }
        });
    }

    public void getBuyers(){
        progressDialog.dismiss();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/buyer/all-buyer")
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Fail to load data", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
            getActivity().runOnUiThread(() -> {
                try {
                    progressDialog.dismiss();
                    String data = response.body().string();
                    buyerModels = new ArrayList<>();
                    Log.d("run: ", data);
                    JSONObject ob = new JSONObject(data);
                    JSONArray array = ob.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        buyerModels.add(new BuyerModel(o.getString("id"), o.getString("name"), o.getString("countryName"), o.getString("phone_number"), o.getString("image"), o.getBoolean("active_status")));
                    }

                    buyerRecyclerAdapter = new BuyerRecyclerAdapter(getContext(), buyerModels);
                    binding.buyersRecycler.setAdapter(buyerRecyclerAdapter);
                    buyerRecyclerAdapter.setOnBuyerClick(model -> {
                       // RequestTenderBuyer(model);
                        TenderRequest fragment = new TenderRequest();
                        fragment.setBuyerModel(model);
                        fragment.setUserPreference(userPreference);
                        fragment.show(getChildFragmentManager(), fragment.getTag());
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            });
            }
        });
    }
    public void ReviewTender(TenderModel model){
        final Dialog tenderDialog = new Dialog(getContext());
        tenderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        tenderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.review_tender, null);
        tenderDialog.setContentView(mView);
        tenderDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(tenderDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((EditText) tenderDialog.findViewById(R.id.quantity)).setText(model.getQuantity());
        ((EditText) tenderDialog.findViewById(R.id.variety)).setText(model.getVariety());
        ((EditText) tenderDialog.findViewById(R.id.grade)).setText(model.getGrade());
        ((EditText) tenderDialog.findViewById(R.id.dateSent)).setText(model.getDateCreated());
        tenderDialog.findViewById(R.id.bt_close).setOnClickListener(view -> tenderDialog.dismiss());


        tenderDialog.findViewById(R.id.sendQuote).setOnClickListener(view -> {
            EditText q = (EditText) tenderDialog.findViewById(R.id.quantitySupply);
            EditText p = (EditText) tenderDialog.findViewById(R.id.price);
            EditText d = (EditText) tenderDialog.findViewById(R.id.supplyDetails);
            EditText loc =  (EditText) tenderDialog.findViewById(R.id.pickupLocation);
            String quantity = q.getText().toString().trim();
            String price = p.getText().toString().trim();
            String loca = loc.getText().toString().trim();
            boolean da = true;
            if (quantity.isEmpty()){
                q.setError(getString(R.string.add_quantity));
                da = false;
            }
            if (price.isEmpty()){
                p.setError(getString(R.string.add_price));
                da = false;
            }
            if (da){
                try {
               int Code = DataApi.SendQuote(userPreference.getToken(), model.getId(), quantity, price, d.getText().toString().trim(), loca, getActivity());
               if (Code == 200){
                   Toast.makeText(getContext(), quote_sent, Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(getContext(), try_again, Toast.LENGTH_SHORT).show();
               }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                tenderDialog.dismiss();

            }

        });
        tenderDialog.create();
        tenderDialog.show();
    }


    public void Login(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(R.string.you_must_login_to_continue);
        alert.setPositiveButton(R.string.login, (dialogInterface, i) -> startActivity(new Intent(getActivity(), Auth.class))).setNegativeButton("Cancel", (dialogInterface, i) -> { });
        alert.create().show();
    }

    public void RequestTenderBuyer(BuyerModel model){

        AlertDialog.Builder bu = new AlertDialog.Builder(getContext());
        View req = getLayoutInflater().inflate(R.layout.buyer_dialog, null);
        bu.setView(req);
         Spinner vr = req.findViewById(R.id.spinnerVariety);
        ((ImageButton) req.findViewById(R.id.bt_close)).setOnClickListener((View.OnClickListener) view -> dialog.dismiss());

        String[] grades = {"grade 1", "grade 2", "grade 3"};
        String[] cert = {"certified", "not certified"};
        String[] veri = {"Supper Kyelar", "Kilosa One"};
        ArrayAdapter<String> veriAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, DataApi.getVariety(getContext()));
        vr.setAdapter(veriAdapter);
        vr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variety = DataApi.getVariety(getContext()).get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> g = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, grades);
        ArrayAdapter<String> cet = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cert);
        ((Spinner) req.findViewById(R.id.spinnerGrade)).setAdapter(g);
        ((Spinner) req.findViewById(R.id.spinnerGrade)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Grade = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ((Spinner) req.findViewById(R.id.spinnerCertified)).setAdapter(cet);
        ((Spinner) req.findViewById(R.id.spinnerCertified)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cert = i + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        req.findViewById(R.id.sendRequest).setOnClickListener(view -> {
            try {
                int quantity = Integer.parseInt(((EditText) req.findViewById(R.id.quantity)).getText().toString().trim());
                int price = Integer.parseInt(((EditText) req.findViewById(R.id.sellingPrice)).getText().toString().trim());
                String details = ((EditText) req.findViewById(R.id.details)).getText().toString().trim();

                String location = ((EditText) req.findViewById(R.id.location)).getText().toString().trim();
            JSONObject ob = new JSONObject();
            ob.put("buyer_id", model.getId());
            JSONArray array = new JSONArray();
            array.put(ob);
            JSONObject obj = new JSONObject();
            obj.put("ids", array);

            int response = DataApi.RequestTender(userPreference.getToken(), quantity, price, 1, Grade, Cert, location, details, variety, obj);
                Log.d( "onClick: ", String.valueOf(response));
                if (response == 200)
                    Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Failed try again", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.d("onClick: ", e.getMessage());
            }
        });

        dialog = bu.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void ViewSeller(){
        AlertDialog.Builder aler = new AlertDialog.Builder(getContext());
        aler.setTitle("Choose Variety");
        aler.setItems(DataApi.getVarietyLocation(getContext()).toArray(new String[0]), (dialogInterface, i) -> {
            String name = DataApi.getVarietyLocation(getContext()).get(i);
            Intent sa = new Intent(getContext(), SellersByVarietyActivity.class);
            sa.putExtra("variety_id", i);
            sa.putExtra("variety", name);
            startActivity(sa);
        });
        aler.setPositiveButton("Close", (dialogInterface, i) -> {

        });
        aler.create().show();

    }
}