package com.example.trekkertech.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.trekkertech.Database.Destination;
import com.example.trekkertech.Activity.MainActivity;
import com.example.trekkertech.R;
import com.google.gson.Gson;

public class DetailFragment extends Fragment {

    private Destination destination;
    private ProgressBar progressBar;
    private TextView nameTextView, countryTextView, continentTextView, populationTextView;
    private TextView currencyTextView, languageTextView, bestTimeToVisitTextView, topAttractionsTextView;
    private TextView localDishesTextView, activitiesTextView, descriptionTextView;
    private ImageButton ib_back;
    private ImageView imageView, location, populationIcon, money, languageIcon, time;
    private LinearLayout bgtrans;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Initialize views
        initializeViews(view);
        hideViews();

        // Get the destination object from the arguments
        if (getArguments() != null) {
            String destinationJson = getArguments().getString("destination");
            new LoadDestinationTask().execute(destinationJson);
        }

        return view;
    }

    private void initializeViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        nameTextView = view.findViewById(R.id.textName);
        countryTextView = view.findViewById(R.id.textCountry);
        continentTextView = view.findViewById(R.id.textContinent);
        populationTextView = view.findViewById(R.id.textPopulation);
        currencyTextView = view.findViewById(R.id.textCurrency);
        languageTextView = view.findViewById(R.id.textLanguage);
        bestTimeToVisitTextView = view.findViewById(R.id.textBestTimeToVisit);
        topAttractionsTextView = view.findViewById(R.id.textTopAttractions);
        localDishesTextView = view.findViewById(R.id.textLocalDishes);
        activitiesTextView = view.findViewById(R.id.textActivities);
        descriptionTextView = view.findViewById(R.id.textDescription);
        imageView = view.findViewById(R.id.imageView);
        location = view.findViewById(R.id.location);
        populationIcon = view.findViewById(R.id.population);
        money = view.findViewById(R.id.money);
        languageIcon = view.findViewById(R.id.language);
        time = view.findViewById(R.id.time);
        bgtrans = view.findViewById(R.id.bgtrans);
        ib_back = view.findViewById(R.id.btn_back);
        ib_back.setOnClickListener(v -> showExitConfirmationDialog());
    }

    private void hideViews() {
        progressBar.setVisibility(View.VISIBLE);
        nameTextView.setVisibility(View.GONE);
        countryTextView.setVisibility(View.GONE);
        continentTextView.setVisibility(View.GONE);
        populationTextView.setVisibility(View.GONE);
        currencyTextView.setVisibility(View.GONE);
        languageTextView.setVisibility(View.GONE);
        bestTimeToVisitTextView.setVisibility(View.GONE);
        topAttractionsTextView.setVisibility(View.GONE);
        localDishesTextView.setVisibility(View.GONE);
        activitiesTextView.setVisibility(View.GONE);
        descriptionTextView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        location.setVisibility(View.GONE);
        populationIcon.setVisibility(View.GONE);
        money.setVisibility(View.GONE);
        bgtrans.setVisibility(View.GONE);
        languageIcon.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        ib_back.setVisibility(View.GONE);
    }

    private void showViews() {
        progressBar.setVisibility(View.GONE);
        nameTextView.setVisibility(View.VISIBLE);
        countryTextView.setVisibility(View.VISIBLE);
        continentTextView.setVisibility(View.VISIBLE);
        populationTextView.setVisibility(View.VISIBLE);
        currencyTextView.setVisibility(View.VISIBLE);
        languageTextView.setVisibility(View.VISIBLE);
        bestTimeToVisitTextView.setVisibility(View.VISIBLE);
        topAttractionsTextView.setVisibility(View.VISIBLE);
        localDishesTextView.setVisibility(View.VISIBLE);
        activitiesTextView.setVisibility(View.VISIBLE);
        descriptionTextView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);
        populationIcon.setVisibility(View.VISIBLE);
        money.setVisibility(View.VISIBLE);
        bgtrans.setVisibility(View.VISIBLE);
        languageIcon.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        ib_back.setVisibility(View.VISIBLE);
    }

    private void showExitConfirmationDialog() {
        getActivity().onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideBottomNavigation();
    }

    private class LoadDestinationTask extends AsyncTask<String, Void, Destination> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Destination doInBackground(String... strings) {
            String destinationJson = strings[0];
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Gson().fromJson(destinationJson, Destination.class);
        }

        @Override
        protected void onPostExecute(Destination destination) {
            super.onPostExecute(destination);

            if (destination != null) {
                nameTextView.setText(destination.getName());
                countryTextView.setText(destination.getCountry());
                continentTextView.setText(destination.getContinent());
                populationTextView.setText(destination.getPopulation());
                currencyTextView.setText(destination.getCurrency());
                languageTextView.setText(destination.getLanguage());
                bestTimeToVisitTextView.setText(destination.getBest_time_to_visit());
                topAttractionsTextView.setText(Html.fromHtml("<b>Top Attractions:</b> " + String.join(", ", destination.getTop_attractions())));
                localDishesTextView.setText(Html.fromHtml("<b>Local Famous Dishes:</b> " + String.join(", ", destination.getLocal_dishes())));
                activitiesTextView.setText(Html.fromHtml("<b>Activities Available:</b> " + String.join(", ", destination.getActivities())));
                descriptionTextView.setText(destination.getDescription());

                Glide.with(DetailFragment.this)
                        .load(destination.getImage())
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);

                showViews();
            }
        }
    }
}
