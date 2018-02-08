package com.example.talanath.seededroid2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.BaseSeedTag;

public class ImageSeedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try {

            setContentView(R.layout.activity_image_seed_view);

            MySQLiteHelper helper = new MySQLiteHelper(this);

            Intent intent = getIntent();
            String seedIdString = intent.getStringExtra(Intent.EXTRA_TEXT);
            long seedId = Long.parseLong(seedIdString);

            models.ImageSeed iseed = helper.GetImageSeedByID(seedId);

            android.widget.ImageView imgSeedView = (android.widget.ImageView) findViewById(R.id.imgSeed);

            File imgFile = new File(iseed.getImagefilelocation());

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                imgSeedView.setImageBitmap(myBitmap);
            }

            StringBuilder tagBuilder = new StringBuilder();
            ArrayList<BaseSeedTag> tags = iseed.getTags();
            for (int i = 0; i < tags.size(); i++) {

                tagBuilder.append(tags.get(i).getTag().getTagText());
                tagBuilder.append(" ");
            }

            android.widget.TextView texttags = (android.widget.TextView) findViewById(R.id.txtTags);
            texttags.setText(tagBuilder.toString());
        }
        catch (Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }
}
