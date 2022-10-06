package com.anahuac.awsdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    EditText name, email;
    Button addbtn, showbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.editTextTextName);
        email = findViewById(R.id.editTextTextEmail);
        addbtn = findViewById(R.id.buttonAdd);
        showbtn = findViewById(R.id.buttonShow);
    }

    public void add(View view) {
        String name_str = name.getText().toString();
        String place_str = email.getText().toString();
        if (name_str.isEmpty()) {
            name.setError("Field cannot be empty");
        } else if (place_str.isEmpty()) {
            email.setError("Field cannot be empty");
        } else {
            ShowActivity.insertData(name_str, place_str);
            name.setText("");
            email.setText("");
            recreate();
            Toasty.success(getApplicationContext(), "Usuario agregado", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void show(View view) {
        startActivity(new Intent(MainActivity.this, ShowActivity.class));
    }

}