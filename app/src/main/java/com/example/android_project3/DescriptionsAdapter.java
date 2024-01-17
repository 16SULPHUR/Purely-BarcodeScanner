package com.example.android_project3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;

public class DescriptionsAdapter extends RecyclerView.Adapter<DescriptionsAdapter.ViewHolder> {

    private JSONArray descriptionsArray;
    ArrayList<String> descriptionsArray2 ;


    public DescriptionsAdapter(JSONArray descriptionsArray) {
        this.descriptionsArray = descriptionsArray;
        this.descriptionsArray2 = new ArrayList<>();

        for (int i = 0; i < descriptionsArray.length(); i++) {
            String description = descriptionsArray.optString(i, null);

            // Check if the description is not empty or null
            if (description != null && !description.isEmpty() && !description.trim().isEmpty() && description != "null") {
                descriptionsArray2.add(description);
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String description = descriptionsArray2.get(position);

        if (description != null && !description.isEmpty() && description!= "") {
            holder.descriptionTextView.setText(description);
        } else {
            // If the title is empty or null, hide the view
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return descriptionsArray2.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;
        View descriptionLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
            descriptionLayout.setBackgroundResource(R.drawable.rounded_corners);
            ;

        }
    }
}

