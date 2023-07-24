package com.example.asus.academiccalendar;

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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MasterKegiatan extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcview_kegiatan;
    ArrayList<Kegiatan> arrKeg = new ArrayList<>();
    adapter_kegiatan kegAdapter;
    DatabaseReference dbkegiatan;
    Spinner spbulan, sptahun;
    int thisyear = Calendar.getInstance().get(Calendar.YEAR);
    int[] listtahun = new int[5];
    String[] isisptahun = new String[5];
    String[] isispbulan = {"January", "February", "April", "March", "May", "June", "July", "August", "September", "October", "November", "December"};
    ArrayAdapter<String> adapter_tahun, adapter_bulan;
    ImageButton imgbtnsearch;
    Kegiatan keg;
    boolean changed = false;
    String srctahun = "";
    String srcbulan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_kegiatan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_formkegiatan = new Intent(MasterKegiatan.this, TambahKegiatan.class);
                startActivity(to_formkegiatan);
            }
        });

        spbulan = findViewById(R.id.spbulan);
        sptahun = findViewById(R.id.sptahun);
        listtahun[0] = thisyear - 2;
        listtahun[1] = thisyear - 1;
        listtahun[2] = thisyear;
        listtahun[3] = thisyear + 1;
        listtahun[4] = thisyear + 2;

        isisptahun[0] = listtahun[0] + "";
        isisptahun[1] = listtahun[1] + "";
        isisptahun[2] = listtahun[2] + "";
        isisptahun[3] = listtahun[3] + "";
        isisptahun[4] = listtahun[2] + "";

        adapter_tahun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, isisptahun);
        adapter_tahun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptahun.setAdapter(adapter_tahun);
        sptahun.setSelection(2);
        adapter_bulan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, isispbulan);
        adapter_bulan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbulan.setAdapter(adapter_bulan);

        dbkegiatan = FirebaseDatabase.getInstance().getReference("Kegiatan");
        rcview_kegiatan = findViewById(R.id.rcview_kegiatan_item);
        imgbtnsearch = findViewById(R.id.imgbtnsearch);
        rcview_kegiatan.setHasFixedSize(true);
        rcview_kegiatan.setLayoutManager(new LinearLayoutManager(this));
        kegAdapter = new adapter_kegiatan(this, arrKeg);
        rcview_kegiatan.setAdapter(kegAdapter);
        kegAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Kegiatan k = arrKeg.get(position);
                Intent to_detailkegiatan = new Intent(MasterKegiatan.this, DetailKegiatan.class);
                to_detailkegiatan.putExtra("detilKeg", k);
                startActivity(to_detailkegiatan);
            }
        });
        imgbtnsearch.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getData();
    }

    protected void getData() {
        dbkegiatan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrKeg.clear();
                for (DataSnapshot katSnap : dataSnapshot.getChildren()) {
                    for (DataSnapshot kegSnap : katSnap.getChildren()) {
                        keg = (Kegiatan) kegSnap.getValue(Kegiatan.class);
                        //arrKeg.add(keg);
                        String[] sptgl = keg.getTglawal().split(" ");
                        String bulankeg = sptgl[1];
                        String tahunkeg = sptgl[2];
                        if (!changed && keg.getStatus() == 1) {
                            arrKeg.add(keg);
                        }
                        else if (changed && srcbulan.equalsIgnoreCase(bulankeg) && srctahun.equalsIgnoreCase(tahunkeg) && keg.getStatus() == 1) {
                            arrKeg.add(keg);
                        }
                        //System.out.println(changed);
                    }
                }
//                kegAdapter.setKegiatanList(arrKeg);
//                rcview_kegiatan.setAdapter(kegAdapter);
                kegAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == imgbtnsearch) {
            changed = true;
            srctahun = sptahun.getSelectedItem().toString();
            srcbulan = spbulan.getSelectedItem().toString();
            getData();
            kegAdapter.notifyDataSetChanged();
        }
    }
}
