package com.example.asus.academiccalendar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MasterKategori extends AppCompatActivity {

    RecyclerView rcview_kategori;
    ArrayList<Kategori> arrKat = new ArrayList<>();
    adapter_kategori katAdapter;
    DatabaseReference dbkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_kategori);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahkategori = new Intent(MasterKategori.this, TambahKategori.class);
                startActivity(tambahkategori);
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbkategori = FirebaseDatabase.getInstance().getReference("Kategori");
        rcview_kategori = findViewById(R.id.rvKategori);
        rcview_kategori.setHasFixedSize(true);
        rcview_kategori.setLayoutManager(new LinearLayoutManager(this));
        katAdapter = new adapter_kategori(this, arrKat);

        katAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Kategori kat = arrKat.get(position);
                Intent to_edit = new Intent(MasterKategori.this, EditKategori.class);
                to_edit.putExtra("Kategori", kat);
                startActivity(to_edit);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        dbkategori.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrKat.clear();
                for (DataSnapshot kegSnap : dataSnapshot.getChildren()) {
                    Kategori kat = (Kategori) kegSnap.getValue(Kategori.class);
                    if(kat.getStatus() == 1){
                        arrKat.add(kat);
                    }
                }
                katAdapter.setKategoriList(arrKat);
                rcview_kategori.setAdapter(katAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
