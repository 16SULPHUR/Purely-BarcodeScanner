package com.example.android_project3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        TextView label = view.findViewById(R.id.label);
        RecyclerView productsRecyclerView;
        ProductAdapter productAdapter;

        String productsString = fetch("https://product-barcode-lookup-api.vercel.app/p");
        try {
            JSONObject responseJson = new JSONObject(productsString);
            JSONArray productsArray = responseJson.getJSONArray("products");

            // Create a list to store product items
            List<ProductItem> productList = new ArrayList<>();

            // Iterate through each product in the array
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject productObject = productsArray.getJSONObject(i);

                // Extract product details
                String title = productObject.optString("title", "N/A");
                String code = productObject.optString("code", "N/A");
                String imageUrl = productObject.optString("image", "");

                // Create a ProductItem and add it to the list
                ProductItem productItem = new ProductItem(title, code, imageUrl);
                productList.add(productItem);
            }

            // Set up RecyclerView with ProductAdapter
            productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
            productAdapter = new ProductAdapter(productList);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            productsRecyclerView.setAdapter(productAdapter);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    public String fetch(String apiUrl) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        CountDownLatch latch = new CountDownLatch(1); // Initialize with 1 permit

        AtomicReference<String> responseBody = new AtomicReference<>(); // Use AtomicReference to hold the value

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        responseBody.set(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle error response
                    responseBody.set("Failed to fetch data");
                }
                latch.countDown(); // Release the permit
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network failure
                responseBody.set("Network error");
                latch.countDown(); // Release the permit
            }
        });

        try {
            latch.await(); // Wait until the response is received or the request fails
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseBody.get();
    }





    private void showToast(final String text) {
        // Ensure this is run on the UI thread
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        });
    }
}

