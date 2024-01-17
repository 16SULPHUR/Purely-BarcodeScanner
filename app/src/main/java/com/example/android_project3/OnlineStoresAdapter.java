package com.example.android_project3;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import org.json.JSONArray;

public class OnlineStoresAdapter extends RecyclerView.Adapter<OnlineStoresAdapter.OnlineStoresViewHolder> {

    private JSONArray titlesArray;

    public OnlineStoresAdapter(JSONArray titlesArray) {
        this.titlesArray = titlesArray;
    }

    @NonNull
    @Override
    public OnlineStoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new OnlineStoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineStoresViewHolder holder, int position) {
        String title = titlesArray.optString(position, null);

        // Check if the title is not empty or null
        if (title != null && !title.isEmpty()) {
            holder.titleText.setText(title);
        } else {
            // If the title is empty or null, hide the view
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return titlesArray.length();
    }

    public static class OnlineStoresViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public View titleLayout;

        public OnlineStoresViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            titleLayout = itemView.findViewById(R.id.titleLayout);
            titleLayout.setBackgroundResource(R.drawable.rounded_corners);;
        }
    }
}
