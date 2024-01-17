package com.example.android_project3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso; // Assuming you are using Picasso for image loading

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductItem> productList;
    private Context context;

    public ProductAdapter(List<ProductItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem productItem = productList.get(position);

        // Set product details to the views
        holder.titleTextView.setText(productItem.getTitle());
        holder.codeTextView.setText("Code: " + productItem.getCode());

        // Load the image using Picasso (replace with your image loading library if different)
//        Picasso.get().load(productItem.getImageUrl()).placeholder(R.drawable.placeholder_image_background).into(holder.imageView);
        String imageUrl = productItem.getImageUrl();
        if (!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            // Handle the case where the image URL is empty
            // You can load a placeholder image or hide the ImageView
            // For example:
             Picasso.get().load(R.drawable.img).into(holder.imageView);
            // or
            // holder.productImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView codeTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            codeTextView = itemView.findViewById(R.id.codeTextView);
        }
    }
}
