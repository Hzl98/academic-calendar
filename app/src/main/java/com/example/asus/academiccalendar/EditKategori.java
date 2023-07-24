package com.example.asus.academiccalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditKategori extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    int mDefaultColor, warnakat;
    Button warna, save, delete;
    TextInputLayout legend;
    Spinner day;
    Switch notif;
    Integer[] arrDays = {1, 2, 3, 4, 5, 6, 7};
    ArrayAdapter<Integer> adapterDays;
    FirebaseAuth firebaseAuth;
    DatabaseReference kategoriRef;
    String katid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kategori);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDefaultColor = ContextCompat.getColor(EditKategori.this, R.color.colorPrimaryDark);
        legend = (TextInputLayout) findViewById(R.id.text_input_legend);
        warna = (Button) findViewById(R.id.button_warna);
        save = (Button) findViewById(R.id.button_save);
        delete = (Button) findViewById(R.id.button_delete);
        notif = (Switch) findViewById(R.id.switch_notif);
        day = (Spinner) findViewById(R.id.spinner_days);

        adapterDays = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                arrDays
        );
        adapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapterDays);

        if (notif != null) {
            notif.setOnCheckedChangeListener(this);
        }
        warna.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        Intent intent = getIntent();
        Kategori k = (Kategori) intent.getSerializableExtra("Kategori");
        katid = k.getId();
        legend.getEditText().setText(k.getNama());
        if (k.getNotifikasi() == -1){notif.setChecked(false); day.setEnabled(false);}
        else {notif.setChecked(true); day.setEnabled(true);}
        day.setSelection(k.getNotifikasi()-1);
        warna.setBackgroundColor(k.getWarna());
        warnakat = k.getWarna();

    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[] {
                                mDefaultColor
                        }
                );
                mDefaultColor = color;
                warna.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            day.setEnabled(true);
        } else {
            day.setEnabled(false);
        }
    }

    private boolean deleteKategori(String kategoriID, String nama, int notifikasi, int warna, int status){
        kategoriRef = FirebaseDatabase.getInstance().getReference("Kategori").child(kategoriID);

        Kategori kat = new Kategori(kategoriID, nama, notifikasi, warna, status);

        kategoriRef.setValue(kat);

        Toast.makeText(this, "Kategori sudah dihapus", Toast.LENGTH_LONG).show();
        this.finish();
        return true;
    }

    private boolean updateKategori(String id, String nama, int notifikasi, int warna){
        kategoriRef = FirebaseDatabase.getInstance().getReference("Kategori").child(id);

        Kategori kat = new Kategori(id, nama, notifikasi, warna);

        kategoriRef.setValue(kat);

        Toast.makeText(this, "Kategori sudah diupdate", Toast.LENGTH_LONG).show();
        this.finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == warna){openColorPicker();}
        else if (view == save){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah anda yakin data kategori sudah benar?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int notifikasi;
                            String namakat = legend.getEditText().getText().toString();
                            if (notif.isChecked()){notifikasi = Integer.parseInt(day.getSelectedItem().toString());}
                            else {notifikasi = -1;}
                            int warnapilihan = mDefaultColor;

                            updateKategori(katid, namakat, notifikasi, warnapilihan);
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
        else if (view == delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah anda yakin ingin menghapus kategori ini?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int notifikasi;
                            String namakat = legend.getEditText().getText().toString();
                            if (notif.isChecked()){notifikasi = Integer.parseInt(day.getSelectedItem().toString());}
                            else {notifikasi = -1;}
                            int warnapilihan = warnakat;
                            int status = 0;

                            deleteKategori(katid, namakat, notifikasi, warnapilihan, status);
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
}
