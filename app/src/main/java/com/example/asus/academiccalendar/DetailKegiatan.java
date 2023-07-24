package com.example.asus.academiccalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailKegiatan extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener {

    DatabaseReference refUpKegiatan, dbkategori;
    ArrayList<Kategori> arrkategori = new ArrayList<>();
    ArrayList<String> listnamakategori = new ArrayList<>();
    ArrayAdapter<String> adapterKategori;
    TextInputLayout txtlayoutnamakeg;
    TextView txttglawal, txttglakhir;
    Button btnsavekegiatan, btndelkegiatan;
    ImageButton tmp;
    ImageButton opentglawal, opentglakhir;
    Spinner spkategori;
    Switch cektgl;
    String id_keg, idkategori;
    int setday, setyear, setmonth;
    int setendday, setendyear, setendmonth;
    boolean setdate = false;
    boolean setenddate = false;
    Map<String, Integer> arrBulan = new HashMap<String, Integer>();

    private int getSpinnerIndex(Spinner spinner, String item){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)){
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtlayoutnamakeg = findViewById(R.id.txtlayoutnamakeg);
        txttglawal = findViewById(R.id.txttglawal);
        txttglakhir = findViewById(R.id.txttglakhir);
        btnsavekegiatan = findViewById(R.id.btnsavekegiatan);
        btndelkegiatan = findViewById(R.id.delete);
        spkategori = findViewById(R.id.spkategori);
        opentglawal = findViewById(R.id.opentglawal);
        opentglakhir = findViewById(R.id.opentglakhir);
        cektgl = findViewById(R.id.cektgl);
        adapterKategori = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listnamakategori);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Intent intent = getIntent();
        final Kegiatan k = (Kegiatan) intent.getSerializableExtra("detilKeg");

        txtlayoutnamakeg.getEditText().setText(k.getNama());
        txttglawal.setText(k.getTglawal());
        txttglakhir.setText(k.getTglakhir());
        id_keg = k.getId();
        idkategori = k.getIdkat();

        refUpKegiatan = FirebaseDatabase.getInstance().getReference("Kegiatan");
        dbkategori = FirebaseDatabase.getInstance().getReference("Kategori");
        dbkategori.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrkategori.clear();
                listnamakategori.clear();
                int ctr = 0;
                for (DataSnapshot katSnap : dataSnapshot.getChildren()) {
                    Kategori kat = katSnap.getValue(Kategori.class);
                    arrkategori.add(kat);
                    listnamakategori.add(kat.getNama());
                }
                spkategori.setAdapter(adapterKategori);
                adapterKategori.notifyDataSetChanged();
                spkategori.setSelection(getSpinnerIndex(spkategori, k.getKategori()));
                spkategori.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (txttglawal.getText().toString().equalsIgnoreCase(txttglakhir.getText().toString())){
            cektgl.setChecked(true);
        }
        if (cektgl != null) {
            cektgl.setOnCheckedChangeListener(this);
        }
        btnsavekegiatan.setOnClickListener(this);
        btndelkegiatan.setOnClickListener(this);
        opentglawal.setOnClickListener(this);
        opentglakhir.setOnClickListener(this);

        arrBulan.put("January", 0);arrBulan.put("February", 1);arrBulan.put("March", 2);
        arrBulan.put("April", 3);arrBulan.put("May", 4);arrBulan.put("June", 5);
        arrBulan.put("July", 6);arrBulan.put("August", 7);arrBulan.put("September", 8);
        arrBulan.put("October", 9);arrBulan.put("November", 10);arrBulan.put("December", 11);

        String[] splittglawal = k.getTglawal().split(" ");
        setyear = Integer.parseInt(splittglawal[2]);
        setmonth = arrBulan.get(splittglawal[1]);
        setday = Integer.parseInt(splittglawal[0]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteKegiatan(String id, String idkat, String nama, String tglakhir, String tglawal, int status, String kategori, int warna, int notifikasi){
        refUpKegiatan = FirebaseDatabase.getInstance().getReference("Kegiatan").child(idkat).child(id);
        Kegiatan keg = new Kegiatan(id, idkat, nama, tglawal, tglakhir, status, kategori, warna, notifikasi);

        refUpKegiatan.setValue(keg);

        Toast.makeText(this, "Kegiatan sudah dihapus", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void updateKegiatan(String id, String idkat, String nama, String tglakhir, String tglawal, String kategori, int warna, int notifikasi){
        refUpKegiatan = FirebaseDatabase.getInstance().getReference("Kegiatan").child(idkat).child(id);
        Kegiatan keg = new Kegiatan(id, idkat, nama, tglakhir, tglawal, kategori, warna, notifikasi);

        refUpKegiatan.setValue(keg);

        Toast.makeText(this, "Kegiatan sudah diupdate", Toast.LENGTH_LONG).show();
        this.finish();
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
        else if (view == btnsavekegiatan){
            String nama = txtlayoutnamakeg.getEditText().getText().toString();
            String tglawal = txttglawal.getText().toString();
            String tglakhir = txttglakhir.getText().toString();
            String kategori = spkategori.getSelectedItem().toString();
            //Kegiatan keg = new Kegiatan(id_keg, nama, tglakhir, tglawal, kategori);
            //refUpKegiatan.child(id_keg).setValue(keg);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah anda yakin data kegiatan sudah benar?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nama = txtlayoutnamakeg.getEditText().getText().toString();
                            String tglawal = txttglawal.getText().toString();
                            String tglakhir = txttglakhir.getText().toString();
                            Kategori kat = arrkategori.get(spkategori.getSelectedItemPosition());
                            String id_kat = kat.getId();
                            String kategori = kat.getNama();
                            int warna = kat.getWarna();
                            int notifikasi = kat.getNotifikasi();

                            updateKegiatan(id_keg, id_kat, nama, tglakhir, tglawal, kategori, warna, notifikasi);
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
        else if (view == btndelkegiatan){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah anda yakin ingin menghapus kegiatan ini?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nama = txtlayoutnamakeg.getEditText().getText().toString();
                            String tglawal = txttglawal.getText().toString();
                            String tglakhir = txttglakhir.getText().toString();
                            Kategori kat = arrkategori.get(spkategori.getSelectedItemPosition());
                            String kategori = kat.getNama();
                            int warna = kat.getWarna();
                            int status = 0;
                            int notifikasi = kat.getNotifikasi();

                            deleteKegiatan(id_keg, idkategori, nama, tglakhir, tglawal, status, kategori, warna, notifikasi);
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
