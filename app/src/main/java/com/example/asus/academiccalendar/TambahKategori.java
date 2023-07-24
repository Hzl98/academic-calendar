package com.example.asus.academiccalendar;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class TambahKategori extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    int mDefaultColor;
    Button warna, save;
    TextInputLayout legend;
    Spinner day;
    Switch notif;
    Integer[] arrDays = {1, 2, 3, 4, 5, 6, 7};
    ArrayAdapter<Integer> adapterDays;
    DatabaseReference kategoriRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDefaultColor = ContextCompat.getColor(TambahKategori.this, R.color.colorPrimaryDark);
        legend = (TextInputLayout) findViewById(R.id.text_input_legend);
        warna = (Button) findViewById(R.id.button_warna);
        save = (Button) findViewById(R.id.button_save);
        notif = (Switch) findViewById(R.id.switch_notif);
        day = (Spinner) findViewById(R.id.spinner_days);
        day.setEnabled(false);

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
    }

    private void addKategori(){
        kategoriRef = FirebaseDatabase.getInstance().getReference("Kategori");

        int notifikasi;
        String namakat = legend.getEditText().getText().toString();
        if (notif.isChecked()){notifikasi = Integer.parseInt(day.getSelectedItem().toString());}
        else {notifikasi = -1;}
        int warnapilihan = mDefaultColor;

        String id = kategoriRef.push().getKey();

        Kategori kategori = new Kategori(id,namakat,notifikasi,warnapilihan);

        kategoriRef.child(id).setValue(kategori);

        this.finish();
        Toast.makeText(this, "Kategori Saved...", Toast.LENGTH_LONG).show();
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
    public void onClick(View view) {
        if (view == warna){openColorPicker();}
        else if (view == save){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Pastikan data Kategori sudah benar !")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addKategori();
                            legend.getEditText().setText("");
                            notif.setChecked(false);
                            day.setEnabled(false);
                            day.setSelection(0);
                            warna.setBackgroundResource(android.R.drawable.btn_default);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            day.setEnabled(true);
        } else {
            day.setEnabled(false);
        }
    }
}
