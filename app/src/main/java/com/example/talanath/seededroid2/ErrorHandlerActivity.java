package com.example.talanath.seededroid2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import db.MySQLiteHelper;
import models.ExceptionError;

public class ErrorHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_handler);

        addListenerOnContinueButton();

        MySQLiteHelper helper = new MySQLiteHelper(this);

        Intent intent = getIntent();
        String errorIDString = intent.getStringExtra(Intent.EXTRA_TEXT);

        //models.ExceptionError error = new ExceptionError(errorString);

        //error = helper.SaveExceptionError(error);

        android.widget.TextView txtError = (android.widget.TextView) findViewById(R.id.txtErrorID);
        txtError.setText("Error ID : "+errorIDString);


    }

    public void addListenerOnContinueButton() {
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.imgContinue);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(ErrorHandlerActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
