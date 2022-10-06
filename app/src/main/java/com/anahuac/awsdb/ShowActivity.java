package com.anahuac.awsdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class ShowActivity extends AppCompatActivity implements ListAdapter.OnElementListener {


    public static final String DATABASE_NAME = "androidapp";
    public static final String url = "jdbc:mysql://android.cmdkwbzztt1n.us-east-2.rds.amazonaws.com:3306/" + DATABASE_NAME;
    public static final String username = "admin", password = "admin123";
    public static final String TABLE_NAME = "users";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupEmail, popupName;
    private Button popupCancelBtn, popupSaveBtn;

    List<ListElement> elements;
    ResultSet rs;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        selectData();
    }

    public void selectData() {

        new Thread(() -> {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
                elements = new ArrayList<>();

                while (rs.next()) {
                    elements.add(new ListElement(rs.getInt(1),rs.getString(2),rs.getString(3)));
                }

                c = this;
                listAdapter = new ListAdapter(elements, this, this);
                recyclerView = findViewById(R.id.listRecyclerView);


                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(c));
                        recyclerView.setAdapter(listAdapter);
                }
            });
        }).start();
    }

    public static void insertData(String name, String email) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("INSERT INTO " + TABLE_NAME + "(nombre, email) VALUES('" + name + "', '" + email + "')");

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void back(View view) {finish();}

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onElementClick(int position) {
        updateData(elements.get(position).getEmail(), elements.get(position).getName(), elements.get(position).getId());
    }

    public void updateData(String email, String name, int id){
        dialogBuilder = new AlertDialog.Builder(this);
        final View userPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        popupEmail = (EditText) userPopupView.findViewById(R.id.popupEmail);
        popupName = (EditText) userPopupView.findViewById(R.id.popupName);
        popupSaveBtn = (Button) userPopupView.findViewById(R.id.saveBtn);
        popupCancelBtn = (Button) userPopupView.findViewById(R.id.cancelBtn);

        popupEmail.setText(email);
        popupName.setText(name);

        dialogBuilder.setView(userPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        popupCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        popupSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, name;
                email = popupEmail.getText().toString().trim();
                name = popupName.getText().toString().trim();
                if(email.isEmpty() || name.isEmpty()){
                    Toasty.error(getApplicationContext(),"Ingrese los datos completos", Toast.LENGTH_SHORT, true).show();
                }else{
                    if(!validarEmail(email)){
                        Toasty.error(getApplicationContext(), "Email no válido", Toast.LENGTH_SHORT, true).show();
                    }else {
                        new Thread(() -> {
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection connection = DriverManager.getConnection(url, username, password);
                                Statement statement = connection.createStatement();

                                statement.execute("UPDATE " + TABLE_NAME + " SET nombre = '" + name + "', email = '" + email + "' where id = "+id);
                                connection.close();
                                finish();
                                startActivity(getIntent());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            }
        });


    }
}