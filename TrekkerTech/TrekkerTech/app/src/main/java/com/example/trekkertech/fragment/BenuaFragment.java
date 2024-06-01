package com.example.trekkertech.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class BenuaFragment extends Fragment {

    private RecyclerView recyclerView;
    private String continent;
    private TextView textAll;
    private ImageButton ib_back;
    private ProgressBar progressBar;
    private static final int DELAY_MILLIS = 700;

    public BenuaFragment(String continent) {
        this.continent = continent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_benua, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
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

        textAll = view.findViewById(R.id.textContinent);
        ib_back = view.findViewById(R.id.btn_back);
        progressBar = view.findViewById(R.id.progressBar);

        ib_back.setOnClickListener(v -> showExitConfirmationDialog());

        switch (continent) {
            case "Europe":
                textAll.setText("EUROPE");
                break;
            case "America":
                textAll.setText("AMERICA");
                break;
            case "Asia":
                textAll.setText("ASIA");
                break;
            case "Africa":
                textAll.setText("AFRICA");
                break;
            default:
                textAll.setText("ALL DESTINATIONS");
                break;
        }

        getActivity().setTitle(textAll.getText());

        new LoadDestinationsTask().execute(continent);

        return view;
    }

    private void showExitConfirmationDialog() {
        getActivity().onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideBottomNavigation();
    }

    private class LoadDestinationsTask extends AsyncTask<String, Void, List<Destination>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<Destination> doInBackground(String... strings) {
            String continent = strings[0];
            DestinationAPI destinationAPI = RetrofitClient.getRetrofitInstance().create(DestinationAPI.class);
            Call<List<Destination>> call;
            if (continent == null) {
                call = destinationAPI.getDestinations();
            } else {
                call = destinationAPI.getDestinationsByContinent(continent);
            }
            try {
                Response<List<Destination>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Destination> destinations = response.body();
                    ArrayList<Destination> destinationByContinent = new ArrayList<>();

                    for (Destination destination : destinations) {
                        if (destination.getContinent().contains(continent)) {
                            if (Objects.equals(destination.getCountry(), "Israel")) {
                                destination.setCountry("#FREEPALESTINE");
                            }
                            destinationByContinent.add(destination);
                        }
                    }
                    return destinationByContinent;
                } else {
                    Log.e("API_ERROR", "Failed to get data from API");
                }
            } catch (IOException e) {
                Log.e("NETWORK_ERROR", "Network error occurred: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Destination> destinationList) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (destinationList != null) {
                    recyclerView.setAdapter(new DestinationAdapter(getActivity(), destinationList));
                }
            }, DELAY_MILLIS);
        }
    }
}
