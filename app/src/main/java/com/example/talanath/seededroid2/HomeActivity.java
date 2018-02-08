package com.example.talanath.seededroid2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.SeedFactory;
import models.SeedType;
import models.User;
import utils.Constants;

public class HomeActivity extends AppCompatActivity {

    MySQLiteHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            String dbName = "Seeden.db";
            //check db exists before continuing
            // if (!doesDatabaseExist(this, dbName ))
            //{
            database = new MySQLiteHelper(this);
            // }
            //else
            //{
            //  database = this.getDatabasePath(dbName);
            //}

            setContentView(R.layout.activity_home);

            loginUser();

            addListenerOnGardensButton();
            addListenerOnTreeButton();
            addlistenerOnSeedButton();
            addListenerOnMySeedsButton();
            //RunDebugScript();
        }
        catch (Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    public void loginUser()
    {
        String username = "Bobthedog";
        //Simulate process
        User user = database.GetUserByName(username);
        if ( user != null) {

        }
        else
        {
            user = database.AddUser(new User(username, UUID.randomUUID()) );
        }

        SharedPreferences  pref= PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token_id", Long.toString(user.get_ID()) );
        editor.commit();

//        SharedPreference  pref= PreferenceManager.getDefaultSharedPreferences(Activity.this);
//
//        String existing_token_id=pref.getString("token_id",null);
    }

    public void addListenerOnMySeedsButton() throws Exception {
        //throw new Exception("Test exception throw");
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.fabViewMySeeds);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(HomeActivity.this, MySeedsActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addListenerOnGardensButton() throws Exception {
        //throw new Exception("Test exception throw");
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.fabViewGardens);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(HomeActivity.this, GardensViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnTreeButton() throws Exception {
        //throw new Exception("Test exception throw");
        android.widget.ImageButton imageButton3 = (android.widget.ImageButton) findViewById(R.id.fabViewTrees);
        imageButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(HomeActivity.this, TreesViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addlistenerOnSeedButton() throws Exception
    {
        //throw new Exception("Test exception throw");
        android.widget.ImageButton imageButton3 = (android.widget.ImageButton) findViewById(R.id.fabNewSeed);
        imageButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                android.content.Intent intent = new android.content.Intent(HomeActivity.this, SeedAddActivity.class);
                startActivity(intent);
            }
        });
    }

    //Debug script will enter the data we need
    private void RunDebugScript()
    {
        try {
            //Add Seed Types
            SeedType seedTypeImage = new SeedType(Constants.SEEDTYPE_IMAGE, "SeedIcon_Image");
            SeedType seedTypeThoughtSeed = new SeedType(Constants.SEEDTYPE_THOUGHTSEED, "SeedIcon_Lightbulb.png");

            seedTypeImage = database.AddSeedType(seedTypeImage);
            seedTypeThoughtSeed = database.AddSeedType(seedTypeThoughtSeed);

            //And tags
            String tagText = "Homelessness";
            models.Tag tag = new models.Tag(tagText);
            tag = database.AddTag(tag);

            String tagText2 = "Corruption";
            models.Tag tag2 = new models.Tag(tagText2);
            tag2 = database.AddTag(tag2);

            String tagText3 = "Housing";
            models.Tag tag3 = new models.Tag(tagText3);
            tag3 = database.AddTag(tag3);

            //Add Seeds
            String thought = "need to reduce competition";
            models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, this.getApplicationContext());
            seed.AddSeedTag(tag);
            seed.AddSeedTag(tag3);

            seed = database.SaveThoughtSeed(seed);

            services.ImageService imageService = new services.ImageService(this.getApplicationContext());

            //java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
//            String imavbfgename = "testimage";
//            String imagefilelocation = "";
//
//            File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");
//
//            models.ImageSeed iSeed = new models.ImageSeed("newwave3.jpeg", imageStorageLoc.getCanonicalPath(), seedTypeImage);

            File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

            //fetches from Test folder assets folder if one exists
            InputStream istream =  this.getApplicationContext().getResources().getAssets().open("underbridge.jpeg");

            Random randomGenerator = new Random();
            int random = randomGenerator.nextInt(900000);
            String imgname = "newwave"+"debug"+".jpeg";
            File outputfile
                    = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
            imageService.SaveImage(istream, outputfile);

            SharedPreferences  pref= PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

            String existing_token_id=pref.getString("token_id",null);

            models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedTypeImage, new User(Long.parseLong(existing_token_id)));

            iSeed = database.SaveImageSeed(iSeed);

            iSeed.AddSeedTag(tag);
            iSeed.AddSeedTag(tag2);
            iSeed = database.SaveImageSeed(iSeed);

            //Add Garden
            String gardenName = "Flower Garden";

            models.Garden garden = new models.Garden(gardenName);

            garden.addTag(tag);
            garden.addTag(tag3);

            //Add Seeds to Garden
            garden.addSeed(seed);
            garden.addSeed(iSeed);

            garden = database.SaveGarden(garden);

            //check we have id

            int x = 0;
            x++;
        }
        catch(Exception ex)
        {
            //DEAL WITH EXCEPTION
            Log.e("ERROR", ex.getMessage().toString());
        }
    }

    /**
     * Check if the database exist and can be read.
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    private static boolean doesDatabaseExist(Context context, String dbName) {
        java.io.File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
