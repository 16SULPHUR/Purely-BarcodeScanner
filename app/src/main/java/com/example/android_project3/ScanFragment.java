package com.example.android_project3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanFragment extends Fragment {

    public ScanFragment() {
    }
    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



//        Button scanBtn = view.findViewById(R.id.btn);

//        scanBtn.setOnClickListener(v->{
//            scanCode();
//        });
        scanCode();

        View view = inflater.inflate(R.layout.fragment_scan, container, false);

//        Glide.with(requireContext()).load(R.drawable.loader).into((ImageView) view.findViewById(R.id.loader));


        return view;
    }


 
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(isBarcodeNumber(result.getContents())){
            fetchBarcodeDetails(result.getContents());
        }
        else{
            showToast(result.getContents());
        }
    });


    private boolean isBarcodeNumber(String input) {
        // Define a regular expression for barcode numbers (adjust based on your requirements)
        String barcodePattern = "^[0-9]{12,13}$"; // Example: Accepts 12 to 13 digits

        // Check if the input matches the barcode pattern
        return input.matches(barcodePattern);
    }


    private void fetchBarcodeDetails(String barcode){
        OkHttpClient client = new OkHttpClient();

        String apiUrl = "https://product-barcode-lookup-api.vercel.app/s?code=" + barcode;

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        // You can now process the response data (e.g., parse JSON)
                        // Update UI or perform any other actions based on the response

                        // For example, displaying the response in a Toast
                        showToast(responseBody);
                        replaceFragmentWithResponse(responseBody,barcode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle error response
                    showToast("Failed to fetch data");
                }
//                return null;
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network failure
                showToast("Network error");
            }
        });
    }

    private void showToast(final String text) {
        // Ensure this is run on the UI thread
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        });
    }

    private void replaceFragmentWithResponse(String response, String barcode) {
        // Create a new fragment instance with the response data
        ResponseFragment responseFragment = ResponseFragment.newInstance(response, barcode);

        // Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragment_container, responseFragment);

        // Optional: Add the transaction to the back stack
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}