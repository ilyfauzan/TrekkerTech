package com.example.trekkertech.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekkertech.R;

public class RegisterActivity extends AppCompatActivity {

    EditText et_username, et_password;
    Button btn_register;
    SharedPreferences sharedPreferences;
    TextView sign_in;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        btn_register = findViewById(R.id.register);
        sign_in = findViewById(R.id.sign_in);

        btn_register.setOnClickListener(view -> {
            String username = String.valueOf(et_username.getText());
            String password = String.valueOf(et_password.getText());

            if (!username.isEmpty() && !password.isEmpty()) {
                sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(username + "_password", password);
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
        sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}
