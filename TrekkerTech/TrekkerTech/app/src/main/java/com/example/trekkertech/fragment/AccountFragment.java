package com.example.trekkertech.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.trekkertech.Activity.LoginActivity;
import com.example.trekkertech.Activity.MainActivity;
import com.example.trekkertech.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountFragment extends Fragment {

    private EditText editTextUsername, editTextDateOfBirth, editTextLocation;
    private TextView textViewUsername, textViewDateOfBirth, textViewLocation, textViewAccount;
    private ImageView profileImage;
    private Button buttonSaveChanges, btnLogout;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private ImageButton btnback;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextUsername.setFocusable(false);
        editTextUsername.setClickable(false);
        editTextDateOfBirth = view.findViewById(R.id.editTextDateOfBirth);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewDateOfBirth = view.findViewById(R.id.textViewDateOfBirth);
        textViewLocation = view.findViewById(R.id.textViewLocation);
        textViewAccount = view.findViewById(R.id.textAccount);
        profileImage = view.findViewById(R.id.profileImage);
        buttonSaveChanges = view.findViewById(R.id.buttonSaveChanges);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnback = view.findViewById(R.id.btn_back);
        btnback.setOnClickListener(v -> showExitConfirmationDialog());

        progressBar.setVisibility(View.VISIBLE);
        editTextUsername.setVisibility(View.GONE);
        editTextDateOfBirth.setVisibility(View.GONE);
        editTextLocation.setVisibility(View.GONE);
        textViewUsername.setVisibility(View.GONE);
        textViewDateOfBirth.setVisibility(View.GONE);
        textViewLocation.setVisibility(View.GONE);
        textViewAccount.setVisibility(View.GONE);
        profileImage.setVisibility(View.GONE);
        buttonSaveChanges.setVisibility(View.GONE);
        buttonSaveChanges.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        btnback.setVisibility(View.GONE);
        sharedPreferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);

        new LoadAccountDataTask().execute();

        buttonSaveChanges.setOnClickListener(v -> showSaveChangesDialog());

        btnLogout.setOnClickListener(v -> {
            saveLoginStatus(false);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        profileImage.setOnClickListener(v -> openGallery());
    }

    private void showSaveChangesDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Save Changes")
                .setMessage("Do you want to save these changes?")
                .setPositiveButton("Yes", (dialog, which) -> saveChanges())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showExitConfirmationDialog() {
        getActivity().onBackPressed();
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private void saveChanges() {
        String username = sharedPreferences.getString("username", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", editTextUsername.getText().toString());
        editor.putString(username + "_date_of_birth", editTextDateOfBirth.getText().toString());
        editor.putString(username + "_location", editTextLocation.getText().toString());
        editor.apply();
        Toast.makeText(requireContext(), "Changes saved", Toast.LENGTH_SHORT).show();
    }

    private class LoadAccountDataTask extends AsyncTask<Void, Void, Void> {

        private String username;
        private String dateOfBirth;
        private String location;
        private String profileImagePath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            username = sharedPreferences.getString("username", "");
            dateOfBirth = sharedPreferences.getString(username + "_date_of_birth", "");
            location = sharedPreferences.getString(username + "_location", "");
            profileImagePath = sharedPreferences.getString(username + "_profile_image", "");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            editTextUsername.setText(username);
            editTextDateOfBirth.setText(dateOfBirth);
            editTextLocation.setText(location);

            if (!profileImagePath.isEmpty()) {
                Glide.with(requireContext()).load(profileImagePath).into(profileImage);
            }

            editTextUsername.setVisibility(View.VISIBLE);
            editTextDateOfBirth.setVisibility(View.VISIBLE);
            editTextLocation.setVisibility(View.VISIBLE);
            textViewUsername.setVisibility(View.VISIBLE);
            textViewDateOfBirth.setVisibility(View.VISIBLE);
            textViewLocation.setVisibility(View.VISIBLE);
            textViewAccount.setVisibility(View.VISIBLE);
            profileImage.setVisibility(View.VISIBLE);
            buttonSaveChanges.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnback.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.GONE);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                profileImage.setImageBitmap(selectedImage);

                saveImageToSharedPreferences(imageUri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideBottomNavigation();
    }

    private void saveImageToSharedPreferences(Uri imageUri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username", "");
        editor.putString(username + "_profile_image", imageUri.toString());
        editor.apply();
    }
}
