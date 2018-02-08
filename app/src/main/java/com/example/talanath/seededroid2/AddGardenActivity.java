package com.example.talanath.seededroid2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;

public class AddGardenActivity extends AppCompatActivity {

    MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_add_garden);

            if (!doesDatabaseExist(this, "Seeden.db")) {
                helper = new MySQLiteHelper(this);
            }

            addListenerOnSaveGardenButton();
        }
        catch(Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    public void addListenerOnSaveGardenButton() {
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.fabAddGarden);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.widget.EditText gardenText = (android.widget.EditText) findViewById(R.id.editGardenName);
                String gardenName = gardenText.getText().toString();
                android.widget.EditText gardenTags = (android.widget.EditText) findViewById(R.id.editGardenTags);
                String tagstring = gardenTags.getText().toString();
                tagstring = tagstring.replaceAll("\\s+", "").trim();
                String[] tags = tagstring.split("#");
                models.Garden newgarden = new models.Garden(gardenName);

                helper.SaveGarden(newgarden);

                android.content.Intent intent = new android.content.Intent(AddGardenActivity.this, AddGardenActivity.class);
                startActivity(intent);
            }
        });
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        java.io.File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}


