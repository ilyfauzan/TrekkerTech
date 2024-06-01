package com.example.trekkertech.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekkertech.Database.DatabaseHelper;
import com.example.trekkertech.Database.Destination;
import com.example.trekkertech.R;
import com.example.trekkertech.fragment.DetailFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {
    private Context context;
    private List<Destination> destinations;

    public DestinationAdapter(Context context, List<Destination> destinations) {
        this.context = context;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.textName.setText(destination.getName());
        holder.textCountry.setText(destination.getCountry());

        Picasso.get().load(destination.getImage()).placeholder(R.drawable.placeholder).into(holder.imageView);

        SharedPreferences sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            boolean isInWishlist = databaseHelper.isDestinationInWishlist(destination, username);
            databaseHelper.close();

            if (isInWishlist) {
                holder.imageViewWishlist.setImageResource(R.drawable.lovebook);
            } else {
                holder.imageViewWishlist.setImageResource(R.drawable.love);
            }
        } else {
            holder.imageViewWishlist.setImageResource(R.drawable.love);
        }

        holder.textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String destinationJson = new Gson().toJson(destination);
                bundle.putString("destination", destinationJson);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String destinationJson = new Gson().toJson(destination);
                bundle.putString("destination", destinationJson);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        holder.textCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String destinationJson = new Gson().toJson(destination);
                bundle.putString("destination", destinationJson);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        holder.imageViewWishlist.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Add to Wishlist");
            builder.setMessage("Do you want to add this destination to your wishlist?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                String username1 = sharedPreferences.getString("username", "");

                if (!username1.isEmpty()) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    if (databaseHelper.isDestinationInWishlist(destination, username1)) {
                        Toast.makeText(context, "Destination already in wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseHelper.addDestination(destination, username1);
                        Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                    }
                    databaseHelper.close();
                } else {
                    Toast.makeText(context, "Please login to add to wishlist", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textCountry;
        ImageView imageView, imageViewWishlist;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textCountry = itemView.findViewById(R.id.textCountry);
            imageView = itemView.findViewById(R.id.imageViewDestination);
            imageViewWishlist = itemView.findViewById(R.id.imageViewWishlist);
        }
    }
}
