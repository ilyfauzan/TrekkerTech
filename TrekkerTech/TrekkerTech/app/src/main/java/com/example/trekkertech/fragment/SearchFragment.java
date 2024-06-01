package com.example.trekkertech.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekkertech.Database.Destination;
import com.example.trekkertech.Adapter.DestinationAdapter;
import com.example.trekkertech.ApiService.DestinationAPI;
import com.example.trekkertech.Activity.MainActivity;
import com.example.trekkertech.R;
import com.example.trekkertech.ApiService.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private DestinationAdapter adapter;
    private List<Destination> destinationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String name;
    private ProgressBar progressBar;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSearch);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@android.support.annotation.NonNull RecyclerView rv, @android.support.annotation.NonNull MotionEvent e) {
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
        adapter = new DestinationAdapter(getContext(), destinationList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = view.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 1) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
                performSearch(newText);
                return true;
            }

            private void performSearch(String searchQuery) {
                name = searchQuery.toLowerCase();
                DestinationAPI destinationAPI = RetrofitClient.getRetrofitInstance().create(DestinationAPI.class);

                if (searchQuery.isEmpty()) {
                    recyclerView.setAdapter(new DestinationAdapter(getActivity(), new ArrayList<>()));
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                Call<List<Destination>> call = destinationAPI.getDestinations();

                call.enqueue(new Callback<List<Destination>>() {
                    @Override
                    public void onResponse(Call<List<Destination>> call, Response<List<Destination>> response) {
                        if (response.isSuccessful()) {
                            List<Destination> destinations = response.body();
                            ArrayList<Destination> destinationByName = new ArrayList<>();
                            for (Destination destination : destinations) {
                                if (destination.getName().toLowerCase().contains(name)) {
                                    if (Objects.equals(destination.getCountry(), "Israel")) {
                                        destination.setCountry("#FREEPALESTINE");
                                    }
                                    destinationByName.add(destination);
                                }
                            }

                            new Handler().postDelayed(() -> {
                                recyclerView.setAdapter(new DestinationAdapter(getActivity(), destinationByName));
                            }, 700);
                        } else {
                            Log.e("API_ERROR", "Failed to get data from API");
                        }
                        new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE), 700);
                    }

                    @Override
                    public void onFailure(Call<List<Destination>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigation();
    }
}
