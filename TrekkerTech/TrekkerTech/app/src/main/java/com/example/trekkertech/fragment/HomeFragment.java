package com.example.trekkertech.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class HomeFragment extends Fragment {

    private LinearLayout eropa;
    private LinearLayout amerika;
    private LinearLayout asia;
    private LinearLayout africa;
    private TextView textWelcome;
    private ImageView profileIcon;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        eropa = view.findViewById(R.id.eropa);
        amerika = view.findViewById(R.id.amerika);
        asia = view.findViewById(R.id.asia);
        africa = view.findViewById(R.id.africa);
        textWelcome = view.findViewById(R.id.textWelcome);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_pref", getActivity().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        textWelcome.setText("Welcome, " + username);

        profileIcon = view.findViewById(R.id.profileIcon);
        recyclerView = view.findViewById(R.id.recyclerView);

        loadProfileImage();
        setHoverAndClickListeners(eropa, "Europe");
        setHoverAndClickListeners(amerika, "America");
        setHoverAndClickListeners(asia, "Asia");
        setHoverAndClickListeners(africa, "Africa");

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment accountfragment = new AccountFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, accountfragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

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
        loadProfileImage();
        ((MainActivity) requireActivity()).showBottomNavigation();
    }

    private void loadDestinations() {
        DestinationAPI destinationAPI = RetrofitClient.getRetrofitInstance().create(DestinationAPI.class);
        Call<List<Destination>> call = destinationAPI.getDestinations();
        call.enqueue(new Callback<List<Destination>>() {
            @Override
            public void onResponse(Call<List<Destination>> call, Response<List<Destination>> response) {
                if (response.isSuccessful()) {
                    List<Destination> destinations = response.body();
                    if (destinations != null && destinations.size() > 10) {
                        destinations = destinations.subList(0, 10);
                    }
                    recyclerView.setAdapter(new DestinationAdapter(getActivity(), destinations));
                } else {
                    Log.e("API_ERROR", "Failed to get data from API");
                }
            }

            @Override
            public void onFailure(Call<List<Destination>> call, Throwable t) {
                Log.e("NETWORK_ERROR", "Network error occurred: " + t.getMessage());
            }
        });
    }

    private void setHoverAndClickListeners(LinearLayout layout, final String continent) {
        layout.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_out));
                }
                return false;
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_out));
                navigateToBenuaFragment(continent);
            }
        });
    }

    private void navigateToBenuaFragment(String continent) {
        Fragment benuaFragment = new BenuaFragment(continent);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, benuaFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String profileImagePath = sharedPreferences.getString(username + "_profile_image", "");

        if (!profileImagePath.isEmpty()) {
            Glide.with(requireContext()).load(profileImagePath).into(profileIcon);
        }
    }
}
