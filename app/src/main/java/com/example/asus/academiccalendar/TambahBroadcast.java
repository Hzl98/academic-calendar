package com.example.asus.academiccalendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TambahBroadcast extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    DatabaseReference broadcastRef;
    EditText message;
    Button send, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_broadcast);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        message = (EditText) findViewById(R.id.textArea_information);
        send = (Button) findViewById(R.id.button_send);
        cancel = (Button) findViewById(R.id.button_cancel);

        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void addBroadcast(){
        broadcastRef = FirebaseDatabase.getInstance().getReference("Broadcast");

        String pesan = message.getText().toString();
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

        String id = broadcastRef.push().getKey();

        ClassBroadcast broadcast = new ClassBroadcast(id,pesan,date);

        broadcastRef.child(id).setValue(broadcast);

        Toast.makeText(this, "Broadcast Message Saved...", Toast.LENGTH_LONG).show();
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
        if (view == send){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah pesan sudah benar?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addBroadcast();
                            message.setText("");
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
        else if (view == cancel){

        }
    }
}
