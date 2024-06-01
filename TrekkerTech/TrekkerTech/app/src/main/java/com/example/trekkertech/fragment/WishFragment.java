package com.example.trekkertech.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekkertech.Database.DatabaseHelper;
import com.example.trekkertech.Database.Destination;
import com.example.trekkertech.Activity.MainActivity;
import com.example.trekkertech.R;
import com.example.trekkertech.Adapter.WishlistAdapter;

import java.util.List;

public class WishFragment extends Fragment {
    private static WishFragment instance;
    private RecyclerView recyclerView;
    private WishlistAdapter adapter;
    private DatabaseHelper databaseHelper;

    public static WishFragment getInstance() {
        if (instance == null) {
            instance = new WishFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewWishlist);
        databaseHelper = new DatabaseHelper(getContext());

        if (databaseHelper == null) {
            Log.e("WishFragment", "DatabaseHelper is null");
        } else {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            List<Destination> wishlist = databaseHelper.getAllDestinations(username);

            adapter = new WishlistAdapter(getContext(), wishlist, databaseHelper);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigation();
    }
}
