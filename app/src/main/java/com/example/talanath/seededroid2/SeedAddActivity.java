package com.example.talanath.seededroid2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SeedAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_add);

        addListenerOnAddImageSeedButton();

        addListenerOnAddInkSeedButton();
    }



    public void addListenerOnAddImageSeedButton() {
        android.widget.Button imageButton2 = (android.widget.Button) findViewById(R.id.btnAddImageSeed);

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0){

                android.content.Intent intent = new android.content.Intent(SeedAddActivity.this, CaptureImageActivity2.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnAddInkSeedButton() {
        android.widget.Button imageButton2 = (android.widget.Button) findViewById(R.id.btnAddInkSeed);

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0){

                android.content.Intent intent = new android.content.Intent(SeedAddActivity.this, InkSeedAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
