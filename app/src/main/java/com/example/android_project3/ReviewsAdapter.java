package com.example.android_project3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private JSONArray reviewsArray;

    public ReviewsAdapter(JSONArray reviewsArray) {
        this.reviewsArray = reviewsArray;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsViewHolder holder, int position) {
        String review = reviewsArray.optString(position, null);

        // Check if the title is not empty or null
        if (review != null && !review.isEmpty()) {
            holder.reviewText.setText(review);
        } else {
            // If the title is empty or null, hide the view
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewsArray.length();
    }

    public static class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public TextView reviewText;
        public View reviewLayout;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.reviewText);
            reviewLayout = itemView.findViewById(R.id.reviewLayout);
            reviewLayout.setBackgroundResource(R.drawable.rounded_corners);;
        }
    }
}
