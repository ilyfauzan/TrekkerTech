package com.example.trekkertech.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekkertech.Database.DatabaseHelper;
import com.example.trekkertech.Database.Destination;
import com.example.trekkertech.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private List<Destination> wishlist;
    private Context context;
    private DatabaseHelper databaseHelper;

    public WishlistAdapter(Context context, List<Destination> wishlist, DatabaseHelper databaseHelper) {
        this.context = context;
        this.wishlist = wishlist;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Destination destination = wishlist.get(position);
        holder.textName.setText(destination.getName());
        holder.textCountry.setText(destination.getCountry());

        Picasso.get().load(destination.getImage()).placeholder(R.drawable.placeholder).into(holder.imageView);

        holder.buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Hapus Item")
                        .setMessage("Apakah Anda ingin menghapus item ini dari wishlist?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (wishlist.contains(destination)) {
                                    databaseHelper.deleteDestination(destination);
                                    wishlist.remove(destination);
                                    notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public void updateList(List<Destination> newList) {
        wishlist = newList;
        notifyDataSetChanged();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textCountry;
        ImageView imageView, buttonDel;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textCountry = itemView.findViewById(R.id.textCountry);
            imageView = itemView.findViewById(R.id.imageViewDestination);
            buttonDel = itemView.findViewById(R.id.buttonDel);
        }
    }
}
