package com.example.android_project3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private TextView responseTextView;
    private TextView apiResponse;

    public ResponseFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ResponseFragment newInstance(String response, String barcode) {
        ResponseFragment fragment = new ResponseFragment();
        Bundle args = new Bundle();
        args.putString("response", response);
        args.putString("barcode", barcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_response, container, false);

//        responseTextView = view.findViewById(R.id.responseTextView);
//        apiResponse = view.findViewById(R.id.apiResponse);

        // Retrieve the response data from the arguments
        String response = getArguments().getString("response", "");

        // Set the response data to the TextView
//        apiResponse.setText(response);

        TextView titleLabel, descriptionLabel;
        RecyclerView titlesRecyclerView, descriptionsRecyclerView, reviewsRecyclerView, sustainabilityInfoRecyclerView;
        TitlesAdapter titlesAdapter;
        DescriptionsAdapter descriptionsAdapter;
        ImagesAdapter imagesAdapter;
        ReviewsAdapter reviewsAdapter;
        SustainabilityInfoAdapter sustainabilityInfoAdapter;

        titleLabel = view.findViewById(R.id.titleLabel);
        descriptionLabel = view.findViewById(R.id.descriptionLabel);
        titlesRecyclerView = view.findViewById(R.id.titlesRecyclerView);
        descriptionsRecyclerView = view.findViewById(R.id.descriptionsRecyclerView);
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        sustainabilityInfoRecyclerView = view.findViewById(R.id.sustainabilityInfoRecyclerView);

        try {
            JSONObject responseObject = new JSONObject(response);

            JSONObject dataObject = responseObject.getJSONObject("data");

            // Assuming your response format is a JSON object
            JSONArray titlesArray = dataObject.getJSONArray("title");
            JSONArray descriptionsArray = dataObject.getJSONArray("description");
            JSONArray imagesArray = dataObject.getJSONArray("images");
            JSONArray reviewsArray = dataObject.getJSONArray("reviews");

            JSONObject sustainabilityInfoObject = dataObject.getJSONObject("sustainabilityInfo");
            JSONArray packingDetails = sustainabilityInfoObject.getJSONArray("packingDetails");
            JSONArray productRecyclability = sustainabilityInfoObject.getJSONArray("productRecyclability");
            JSONArray ratings = sustainabilityInfoObject.getJSONArray("ratings");
            JSONArray recyclabilityDetails = sustainabilityInfoObject.getJSONArray("recyclabilityDetails");

            // Set up RecyclerViews with horizontal LinearLayoutManager
            titlesAdapter = new TitlesAdapter(titlesArray);
            descriptionsAdapter = new DescriptionsAdapter(descriptionsArray);
            reviewsAdapter = new ReviewsAdapter(reviewsArray);
            sustainabilityInfoAdapter = new SustainabilityInfoAdapter(packingDetails,productRecyclability,ratings,recyclabilityDetails);

            titlesRecyclerView = view.findViewById(R.id.titlesRecyclerView);
            descriptionsRecyclerView = view.findViewById(R.id.descriptionsRecyclerView);
            RecyclerView imagesRecyclerView = view.findViewById(R.id.imagesRecyclerView);

            titlesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            titlesRecyclerView.setAdapter(titlesAdapter);

            descriptionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            descriptionsRecyclerView.setAdapter(descriptionsAdapter);

            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            reviewsRecyclerView.setAdapter(reviewsAdapter);

            sustainabilityInfoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            sustainabilityInfoRecyclerView.setAdapter(sustainabilityInfoAdapter);

            imagesAdapter = new ImagesAdapter(imagesArray);
            imagesRecyclerView.setLayoutManager(
                    new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            imagesRecyclerView.setAdapter(imagesAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }


        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(view1 -> {
            String barcode = getArguments().getString("barcode", "");
            replaceFragmentWithProductForm(barcode);
        });

        return view;
    }

    private void replaceFragmentWithProductForm(String barcode) {
        // Create a new fragment instance with the response data
        ProductDetailsForm productDetailsFormFragment = ProductDetailsForm.newInstance(barcode);

        // Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragment_container, productDetailsFormFragment);

        // Optional: Add the transaction to the back stack
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}