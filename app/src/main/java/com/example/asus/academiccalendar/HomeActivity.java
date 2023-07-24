package com.example.asus.academiccalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        progressDialog = new ProgressDialog(this);
    }

    private void userLogout(){
        progressDialog.setMessage("Logging Out...");
        progressDialog.show();

        progressDialog.dismiss();
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void onClick(View view) {

    }
}
