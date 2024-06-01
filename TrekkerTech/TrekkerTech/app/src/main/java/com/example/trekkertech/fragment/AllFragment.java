package com.example.trekkertech.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekkertech.Database.Destination;
import com.example.trekkertech.ApiService.DestinationAPI;
import com.example.trekkertech.Adapter.DestinationAdapter;
import com.example.trekkertech.Activity.MainActivity;
import com.example.trekkertech.R;
import com.example.trekkertech.ApiService.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllFragment extends Fragment {

    TextView tv_see;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        tv_see = view.findViewById(R.id.textAll);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        childView.setScaleX(0.9f);
                        childView.setScaleY(0.9f);
                        childView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                childView.setScaleX(1.0f);
                                childView.setScaleY(1.0f);
                            }
                        }, 300);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        loadDestinations();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigation();
    }

    private void loadDestinations() {
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DestinationAPI destinationAPI = RetrofitClient.getRetrofitInstance().create(DestinationAPI.class);
                Call<List<Destination>> call = destinationAPI.getDestinations();
                call.enqueue(new Callback<List<Destination>>() {
                    @Override
                    public void onResponse(Call<List<Destination>> call, Response<List<Destination>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            List<Destination> destinations = response.body();
                            for (Destination destination : destinations) {
                                if ("Israel".equals(destination.getCountry())) {
                                    destination.setCountry("#FREEPALESTINE");
                                }
                            }
                            recyclerView.setAdapter(new DestinationAdapter(getActivity(), destinations));
                        } else {
                            Log.e("API_ERROR", "Failed to get data from API: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Destination>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("NETWORK_ERROR", "Network error occurred: " + t.getMessage());
                    }
                });
            }
        }, 1000);
    }
}
