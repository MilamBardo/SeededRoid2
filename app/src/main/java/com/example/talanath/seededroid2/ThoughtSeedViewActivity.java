package com.example.talanath.seededroid2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import interfaces.ISeed;

public class ThoughtSeedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

            setContentView(R.layout.activity_thoughtseed_view);

            MySQLiteHelper helper = new MySQLiteHelper(this);

            Intent intent = getIntent();
            String seedIdString = intent.getStringExtra(Intent.EXTRA_TEXT);
            long seedId = Long.parseLong(seedIdString);

            models.ThoughtSeed tseed = helper.GetThoughtSeedByID(seedId);

            android.widget.TextView seedTypeName = (android.widget.TextView) findViewById(R.id.txtSeedType);
            seedTypeName.setText(tseed.getSeedType().getSeedTypeName());

            android.widget.TextView thought = (android.widget.TextView) findViewById(R.id.txtThought);
            thought.setText(tseed.getThought());

            StringBuilder tagBuilder = new StringBuilder();
            ArrayList<models.BaseSeedTag> tags = tseed.getTags();
            for (int i = 0; i < tags.size(); i++) {

                tagBuilder.append(tags.get(i).getTag().getTagText());
                tagBuilder.append(" ");
            }

            android.widget.TextView texttags = (android.widget.TextView) findViewById(R.id.txtTags);
            texttags.setText(tagBuilder.toString());
        }
        catch(Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }
}
