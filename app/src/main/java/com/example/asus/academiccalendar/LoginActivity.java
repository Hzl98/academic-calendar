package com.example.asus.academiccalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout nameText;
    private TextInputLayout passwordText;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameText = (TextInputLayout)findViewById(R.id.nameText);
        passwordText = (TextInputLayout) findViewById(R.id.passwordText);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    private boolean validateName() {
        String nama = nameText.getEditText().getText().toString();
        if (nama.isEmpty()){
            nameText.setError("Field tidak boleh kosong");
            return false;
        }else if (nama.length() > 17){
            nameText.setError("Email terlalu panjang");
            return false;
        }else{
            nameText.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String pass = passwordText.getEditText().getText().toString();
        if (pass.isEmpty()){
            passwordText.setError("Field tidak boleh kosong");
            return false;
        }else if (pass.length() > 10){
            passwordText.setError("Password terlalu panjang");
            return false;
        }else{
            passwordText.setError(null);
            return true;
        }
    }

    private void userlogin(){
        if (!validateName() | !validatePassword()){
            return;
        }

        String name = nameText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();

        /*if (TextUtils.isEmpty(name)){
            //name is empty
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }*/
        //if validation are ok
        //we will first show progressbar


        if((TextUtils.equals(name, "kalender@stts.edu")) && (TextUtils.equals(password, "abcde12345"))){
            finish();
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }
        else {
            Toast.makeText(this, "Email or Password is Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin){
            userlogin();
        }
    }
}
