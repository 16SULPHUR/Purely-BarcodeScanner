
package com.example.android_project3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class SustainabilityInfoAdapter extends RecyclerView.Adapter<SustainabilityInfoAdapter.SustainabilityInfoViewHolder> {

    private JSONArray packingDetails, productRecyclability, ratings, recyclabilityDetails;

    public SustainabilityInfoAdapter(JSONArray packingDetails, JSONArray productRecyclability, JSONArray ratings, JSONArray recyclabilityDetails) {

        this.packingDetails = packingDetails;
        this.productRecyclability = productRecyclability;
        this.ratings = ratings;
        this.recyclabilityDetails = recyclabilityDetails;
    }

    @NonNull
    @Override
    public SustainabilityInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sustainability_info, parent, false);
        return new SustainabilityInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SustainabilityInfoViewHolder holder, int position) {
        String pd = packingDetails.optString(position, null);
        String pr = productRecyclability.optString(position, null);
        double r = ratings.optDouble(position, 0.0);
        String rd = recyclabilityDetails.optString(position, null);

        // Check if the field is not empty or null, and set the corresponding TextView
        if (pd != null && !pd.isEmpty()) {
            holder.packingDetailsTextView.setText("Packing Details : "+pd);
        } else {
            holder.packingDetailsTextView.setVisibility(View.GONE);
        }

        if (pr != null && !pr.isEmpty()) {
            holder.productRecyclabilityTextView.setText("Product Recyclability : "+pr);
        } else {
            holder.productRecyclabilityTextView.setVisibility(View.GONE);
        }
//
        // Assuming you have a TextView for displaying ratings
        if (r != 0.0) {
            holder.sustainableRatingsTextView.setText("Sustainability Ratings : "+String.valueOf(r));
        } else {
            holder.sustainableRatingsTextView.setVisibility(View.GONE);
        }
//
//
        if (rd != null && !rd.isEmpty()) {
            holder.recyclablityDetailsTextView.setText("Recyclability Details : "+rd);
        } else {
            holder.recyclablityDetailsTextView.setVisibility(View.GONE);
        }
//        holder.recyclablityDetailsTextView.setText(rd);
//        holder.productRecyclabilityTextView.setText(pr);
//        holder.sustainableRatingsTextView.setText(String.valueOf(r));
//        holder.packingDetailsTextView.setText(pd);

    }


    @Override
    public int getItemCount() {

        // Using the minimum length among all arrays
        Log.e("INFOOOOOOO", String.valueOf(packingDetails));
        Log.e("INFOOOOOOO", String.valueOf(productRecyclability));
        Log.e("INFOOOOOOO", String.valueOf(ratings));
        Log.e("INFOOOOOOO", String.valueOf(recyclabilityDetails));

        return Math.min(packingDetails.length(), Math.min(productRecyclability.length(), Math.min(ratings.length(), recyclabilityDetails.length())));

//        return ratings.length();

    }


    public static class SustainabilityInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView recyclablityDetailsTextView;
        public TextView productRecyclabilityTextView;
        public TextView sustainableRatingsTextView;
        public TextView packingDetailsTextView;
        public View infoLayout;

        public SustainabilityInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclablityDetailsTextView = itemView.findViewById(R.id.recyclablityDetailsTextView);
            productRecyclabilityTextView = itemView.findViewById(R.id.productRecyclabilityTextView);
            sustainableRatingsTextView = itemView.findViewById(R.id.sustainableRatingsTextView);
            packingDetailsTextView = itemView.findViewById(R.id.packingDetailsTextView);

            infoLayout = itemView.findViewById(R.id.infoLayout);
//            infoLayout.setBackgroundResource(R.drawable.rounded_corners);

        }
    }
}
