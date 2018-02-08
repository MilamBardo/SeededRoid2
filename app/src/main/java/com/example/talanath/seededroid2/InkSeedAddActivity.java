package com.example.talanath.seededroid2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.SeedFactory;
import models.Tag;
import models.ThoughtSeed;

public class InkSeedAddActivity extends AppCompatActivity {

    MySQLiteHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try {
            setContentView(R.layout.activity_ink_seed_add);
            database = new MySQLiteHelper(this);

            addListenerOnAddSaveSeedButton();
        }
        catch (Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    public void addListenerOnAddSaveSeedButton() {
        android.widget.Button imageButton2 = (android.widget.Button) findViewById(R.id.btnSaveSeed);

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0){

                EditText inkText = (EditText) findViewById(R.id.inkText);
                EditText txtTags = (EditText) findViewById(R.id.txtTags);

                String ink = inkText.getText().toString();
                String tagsS = txtTags.getText().toString();

                ThoughtSeed tseed = null;
                try {
                    tseed = SeedFactory.getInstance().CreateThoughtSeed(ink, InkSeedAddActivity.this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                String[] tags = tagsS.split(" ");
                for (int i = 0; i < tags.length; i++)
                {
                    Tag t = new Tag(tags[i]);
                    tseed.AddSeedTag(t);
                }

                try {
                    tseed = database.SaveThoughtSeed(tseed);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                android.content.Intent intent = new android.content.Intent(InkSeedAddActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
