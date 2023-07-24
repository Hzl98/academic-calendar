package com.example.asus.academiccalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TambahKegiatan extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener {

    TextInputLayout txtlayoutnamakeg;
    Button btninskegiatan;
    ImageButton opentglawal, opentglakhir;
    ImageButton tmp;
    TextView txttglawal, txttglakhir;
    Spinner spkategori;
    Switch cektgl;
    ArrayList<Kategori> arrkategori = new ArrayList<>();
    ArrayList<String> listnamakategori = new ArrayList<>();
    ArrayAdapter<String> adapterKategori;
    DatabaseReference dbkategori, dbkegiatan;
    int setday, setyear, setmonth;
    int setendday, setendyear, setendmonth;
    boolean setdate = false;
    boolean setenddate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kegiatan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        opentglawal = findViewById(R.id.opentglawal);
        opentglakhir = findViewById(R.id.opentglakhir);
        txtlayoutnamakeg = findViewById(R.id.txtlayoutnamakeg);
        cektgl = findViewById(R.id.cektgl);
        txttglawal = findViewById(R.id.txttglawal);
        txttglakhir = findViewById(R.id.txttglakhir);
        spkategori = findViewById(R.id.spkategori);
        btninskegiatan = findViewById(R.id.btninskegiatan);
        dbkategori = FirebaseDatabase.getInstance().getReference("Kategori");
        dbkegiatan = FirebaseDatabase.getInstance().getReference("Kegiatan");

        adapterKategori = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listnamakategori);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (cektgl != null) {
            cektgl.setOnCheckedChangeListener(this);
        }
        btninskegiatan.setOnClickListener(this);
        opentglakhir.setOnClickListener(this);
        opentglawal.setOnClickListener(this);

        setyear = Calendar.getInstance().get(Calendar.YEAR);
        setmonth = Calendar.getInstance().get(Calendar.MONTH);
        setday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
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

        dbkategori.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrkategori.clear();
                for (DataSnapshot katSnap : dataSnapshot.getChildren()) {
                    Kategori kat = katSnap.getValue(Kategori.class);
                    arrkategori.add(kat);
                    listnamakategori.add(kat.getNama());
                }
                spkategori.setAdapter(adapterKategori);
                adapterKategori.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addKegiatan(){
        String nama = txtlayoutnamakeg.getEditText().getText().toString();
        String tglawal = txttglawal.getText().toString();
        String tglakhir = txttglakhir.getText().toString();
        Kategori kat = arrkategori.get(spkategori.getSelectedItemPosition());
        String id_kat = kat.getId();
        String namakat = kat.getNama();
        int warna = kat.getWarna();
        int notifikasi = kat.getNotifikasi();

        String id_keg = dbkegiatan.push().getKey();

        Kegiatan keg = new Kegiatan(id_keg, id_kat, nama, tglakhir, tglawal, namakat, warna, notifikasi);

        dbkegiatan.child(id_kat).child(id_keg).setValue(keg);

        this.finish();
        Toast.makeText(this, "Kegiatan Saved...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view == opentglawal){
            DatePickerDialog datePicker = new DatePickerDialog(this, this, setyear, setmonth, setday);
            datePicker.show();
            tmp = opentglawal;
            setenddate = false;
        }
        else if (view == opentglakhir){
            if (setenddate) {
                DatePickerDialog datePicker = new DatePickerDialog(this, this, setendyear, setendmonth, setendday);
                datePicker.show();
                tmp = opentglakhir;
            }
            else {
                DatePickerDialog datePicker = new DatePickerDialog(this, this, setyear, setmonth, setday);
                datePicker.show();
                tmp = opentglakhir;
            }
        }
        else if (view == btninskegiatan){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Pastikan data sudah benar !")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addKegiatan();
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
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());

        if (tmp == opentglawal){
            setyear = year;
            setmonth = month;
            setday = dayOfMonth;
            txttglawal.setText(currentDate);
            setdate = true;
        }
        else if (tmp == opentglakhir){
            setendyear = year;
            setendmonth = month;
            setendday = dayOfMonth;
            txttglakhir.setText(currentDate);
            setenddate = true;
            if (txttglakhir.getText().toString().equalsIgnoreCase(txttglawal.getText().toString())){
                cektgl.setChecked(true);
            }
            else{
                cektgl.setChecked(false);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            if (txttglawal != null){
                txttglakhir.setText(txttglawal.getText().toString());
            }
            else {
                txttglakhir.setText("");
            }
        }
    }
}
