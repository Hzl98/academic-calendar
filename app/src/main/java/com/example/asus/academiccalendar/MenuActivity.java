package com.example.asus.academiccalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.core.Tag;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView kategori, calendar, kegiatan, broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        kategori = (ImageView) findViewById(R.id.IV_kategori);
        calendar = (ImageView) findViewById(R.id.IV_kalender);
        kegiatan = (ImageView) findViewById(R.id.IV_kegiatan);
        broadcast = (ImageView) findViewById(R.id.IV_broadcast);

        kategori.setOnClickListener(this);
        calendar.setOnClickListener(this);
        kegiatan.setOnClickListener(this);
        broadcast.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MenuActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        if (view == kategori){
            startActivity(new Intent(getApplicationContext(), MasterKategori.class));
        }
        else if (view == calendar){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
        else if (view == kegiatan){
            startActivity(new Intent(getApplicationContext(), MasterKegiatan.class));
        }
        else if (view == broadcast){
            startActivity(new Intent(getApplicationContext(), TambahBroadcast.class));
        }
    }
}
