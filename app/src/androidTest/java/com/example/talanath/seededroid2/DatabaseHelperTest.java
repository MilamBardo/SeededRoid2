package com.example.talanath.seededroid2;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import db.SeedenContract;
import models.ExceptionError;
import models.Garden;
import models.GardenSeed;
import models.ImageSeed;
import models.SeedFactory;
import models.ThoughtSeed;
import models.SeedType;
import models.Tree;
import models.TreeScene;
import models.User;
import utils.Constants;


import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Talanath on 4/1/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private MySQLiteHelper database;

    private SeedType seedTypeImage;
    private SeedType seedTypeThoughtSeed;


    private models.Tag tag_Corruption;
    private models.Tag tag_Pollution;
    private models.Tag tag_Politics;

    private models.User appUser;
    private models.User seedProducer1;

    @Before
    public void setUp() throws Exception {
        try {
            getTargetContext().deleteDatabase("Seeden.db");
            database = new MySQLiteHelper(getTargetContext());

            seedTypeImage = new SeedType("ImageSeed", "SeedIcon_Image");
            seedTypeThoughtSeed = new SeedType("ThoughtSeed", "SeedIcon_Lightbulb.png");

            seedTypeImage = database.AddSeedType(seedTypeImage);
            seedTypeThoughtSeed = database.AddSeedType(seedTypeThoughtSeed);

            String tagText1 = "Politics";
            tag_Politics= new models.Tag(tagText1);
            tag_Politics = database.AddTag(tag_Politics);

            String tagText2 = "Corruption";
            tag_Corruption = new models.Tag(tagText2);
            tag_Corruption = database.AddTag(tag_Corruption);

            String tagText3 = "Pollution";
            tag_Pollution = new models.Tag(tagText3);
            tag_Pollution = database.AddTag(tag_Pollution);

            appUser = new User("BobTheTestDog", UUID.randomUUID());
            appUser = database.AddUser(appUser);
            seedProducer1 = new User("SeedProducer1", UUID.randomUUID());
            seedProducer1 = database.AddUser(seedProducer1);
            SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getTargetContext());

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("token_id", Long.toString(appUser.get_ID()) );
            editor.commit();
        }
        catch (Exception ex)
        {
            Assert.fail(ex.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void useAppContext() throws Exception {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.example.talanath.seededroid2", appContext.getPackageName());

        // Context of the app under test.
        Context appContextInstrum = InstrumentationRegistry.getContext();
        assertEquals("com.example.talanath.seededroid2.test", appContextInstrum.getPackageName());
    }

    //region D E B U G G I N G  S E T U P ////////////////////////////////

    @Test
    public void SetUpForDebug()
    {
        try {
            Garden garden1 = AddGarden_FlowerGarden();
            AddGarden_WeedGarden();

            ImageSeed iSeed = null;
            ThoughtSeed tSeed = null;

            List<GardenSeed> gseeds = garden1.getSeeds();
            for (int i=0; i<gseeds.size(); i++)
            {
                GardenSeed gseed = gseeds.get(i);
                if (gseed.getSeedType().getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED))tSeed = database.GetThoughtSeedByID(gseed.getSeedID());
                if (gseed.getSeedType().getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE))iSeed = database.GetImageSeedByID(gseed.getSeedID());
            }

            AddTreeWithSeeds(iSeed, tSeed);
        }
        catch (Exception ex)
        {
            fail("fail in set up for debug");
        }
    }
    //endregion


    //region U S E R  T E S T S  ///////////////////

    @Test
    public void CreateUser() throws Exception {
        String username = "BigDave";

        models.User user = CreateUser(username);
        assertTrue(user != null);
        assertTrue(user.getUserName() == username);
    }

    @Test
    public void CreateTwoUser() throws Exception {
        String username = "BigDave";

        models.User user = CreateUser(username);
        assertTrue(user != null);
        assertTrue(user.getUserName() == username);

        String username2 = "Bonjo";

        models.User user2 = CreateUser(username2);
        assertTrue(user2 != null);
        assertTrue(user2.getUserName() == username2);
    }

    public models.User CreateUser(String uname) {


        UUID globalId = UUID.randomUUID();
        models.User user = new models.User(uname, globalId);

        user = database.AddUser(user);

        assertTrue(user.get_ID() > 0);

        //db.close();
        return user;
    }

    //endregion

    //region T H O U G H T  S E E D  T E S T S  //////////////

    @Test
    public void AddThoughtSeed() throws Exception {

        String thought = "need to reduce competition";
        models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());

        seed = database.SaveThoughtSeed(seed);

        assertTrue(seed != null);
        assertTrue(seed.getThought() == thought);
        assertTrue(seed.getSeedType() != null);
        assertTrue(seed.getSeedType().get_ID() != 0);

        models.ThoughtSeed updatedseed = database.GetThoughtSeedByID(seed.get_ID());

        //check get method
        assertTrue(updatedseed.get_ID() == seed.get_ID());
        assertTrue(updatedseed.getThought().equals(thought));
        assertTrue(updatedseed.getSeedType() != null);
        assertTrue(updatedseed.getSeedType().get_ID() != 0);
    }

    @Test
    public void UpdateThoughtSeed() throws Exception {

        String thought = "need to reduce competition";
        models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());

        seed = database.SaveThoughtSeed(seed);

        assertTrue(seed != null);


        assertTrue(seed.getThought() == thought);

        String newthought = "need to attack the individualist ideologies.";
        seed.setThought(newthought);

        database.SaveThoughtSeed(seed);

        models.ThoughtSeed updatedseed = database.GetThoughtSeedByID(seed.get_ID());

        assertTrue(updatedseed.get_ID() == seed.get_ID());
        assertTrue((updatedseed.getThought().equalsIgnoreCase(newthought)));
        models.SeedType type= updatedseed.getSeedType();
        assertTrue(type != null);
        assertTrue(type.get_ID() != 0);

    }

    @Test
    public void AddThoughtSeedWithTags() throws Exception {

        try {
            //Add some tags
            String tagText = "Homelessness";
            models.Tag tag = new models.Tag(tagText);
            tag = database.AddTag(tag);

            String tagText2 = "Corruption";
            models.Tag tag2 = new models.Tag(tagText2);
            tag2 = database.AddTag(tag2);

            String tagText3 = "Pollution";
            models.Tag tag3 = new models.Tag(tagText3);
            tag3 = database.AddTag(tag3);


            String thought = "need to reduce competition";
            models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());
            seed.AddSeedTag(tag);
            seed.AddSeedTag(tag3);

            //models.ThoughtSeed seedw = database.GetThoughtSeedByID(1);
            seed = database.SaveThoughtSeed(seed);

            assertTrue(seed != null);
            assertTrue(seed.getThought() == thought);
            assertTrue(seed.getTags().size() == 2);

            models.ThoughtSeed fetchedSeed = database.GetThoughtSeedByID(seed.get_ID());

            assertTrue(fetchedSeed.getTags().size() == 2);
        }
        catch(Exception ex)
        {
            fail(ex.getMessage());
        }

    }


    public models.ThoughtSeed AddThoughtSeedBasic() throws Exception {

        String thought = "need to reduce competition";
        models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());

        seed = database.SaveThoughtSeed(seed);

        assertTrue(seed != null);
        assertTrue(seed.getThought() == thought);
        assertTrue(seed.getSeedType() != null);
        assertTrue(seed.getSeedType().get_ID() != 0);

        models.ThoughtSeed updatedseed = database.GetThoughtSeedByID(seed.get_ID());

        //check get method
        assertTrue(updatedseed.get_ID() == seed.get_ID());
        assertTrue(updatedseed.getThought().equals(thought));
        assertTrue(updatedseed.getSeedType() != null);
        assertTrue(updatedseed.getSeedType().get_ID() != 0);

        return updatedseed;
    }
    //endregion

    //region I M A G E  S E E D  T E S T S  ////////////////////

    private static java.io.File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new java.io.File(resource.getPath());
    }

    @Test
    public void fileObjectShouldNotBeNull() throws Exception {
        java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
        assertThat(file, org.hamcrest.CoreMatchers.notNullValue());
    }

    String[] permissions = new String[]{
            //Manifest.permission.INTERNET,
            //Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.VIBRATE,
            //Manifest.permission.RECORD_AUDIO,
    };
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(permissions);

    //@Rule
    //public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void AddImageSeed() throws Exception
    {
        Log.i("info: ", "Entering TEST");
        try {

            services.ImageService imageService = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

            //java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
            String imavbfgename = "testimage";
            String imagefilelocation = "";
            SeedType seedtype = seedTypeImage;

            File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

            //fetches from Test folder assets folder if one exists
            InputStream istream =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("lahratree1000smoother.png");

            Random randomGenerator = new Random();
            int random = randomGenerator.nextInt(900000);
            String imgname = "newwave"+random+".jpeg";
            File outputfile
                    = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
            imageService.SaveImage(istream, outputfile);


            models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedtype, seedProducer1);

            //Add some tags
            String tagText = "Homelessness";
            models.Tag tag = new models.Tag(tagText);
            tag = database.AddTag(tag);

            //Second tag will need to insert
            String tagText2 = "Obscure Knowledge";
            models.Tag tag2 = new models.Tag(tagText2);

            iSeed.AddSeedTag(tag);
            iSeed.AddSeedTag(tag2);

            iSeed = database.SaveImageSeed(iSeed);

            assertTrue(iSeed != null);
            assertTrue(iSeed.get_ID() != 0);
            assertTrue(iSeed.get_ID() > 0);
            assertTrue(iSeed.getImagefilelocation().equalsIgnoreCase(outputfile.getCanonicalPath()));
            assertTrue(iSeed.getImagename().equalsIgnoreCase(imgname));

            models.ImageSeed fetchedSeed = database.GetImageSeedByID(iSeed.get_ID());

            assertTrue(fetchedSeed != null);
            assertTrue(fetchedSeed.get_ID() != 0);
            assertTrue(fetchedSeed.get_ID() > 0);
            assertTrue(fetchedSeed.getImagefilelocation().equalsIgnoreCase(outputfile.getCanonicalPath()));
            assertTrue(fetchedSeed.getImagename().equalsIgnoreCase(imgname));
            assertTrue(fetchedSeed.getSeedType().getSeedTypeName().equalsIgnoreCase("ImageSeed"));
        }
        catch(Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void DeleteImageSeed() throws Exception
    {
        Log.i("info: ", "Entering TEST");
        try {

            services.ImageService imageService = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

            //java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
            String imavbfgename = "testimage";
            String imagefilelocation = "";
            SeedType seedtype = seedTypeImage;

            File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

            //fetches from Test folder assets folder if one exists
            InputStream istream =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("lahratree1000smoother.png");

            Random randomGenerator = new Random();
            int random = randomGenerator.nextInt(900000);
            String imgname = "newwave"+random+".jpeg";
            File outputfile
                    = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
            imageService.SaveImage(istream, outputfile);


            models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedtype, seedProducer1);

            //Add some tags
            String tagText = "Homelessness";
            models.Tag tag = new models.Tag(tagText);
            tag = database.AddTag(tag);

            //Second tag will need to insert
            String tagText2 = "Obscure Knowledge";
            models.Tag tag2 = new models.Tag(tagText2);

            iSeed.AddSeedTag(tag);
            iSeed.AddSeedTag(tag2);

            iSeed = database.SaveImageSeed(iSeed);

            File imgFile = new File(iSeed.getImagefilelocation());

            Assert.assertTrue(imgFile != null);
            Assert.assertTrue(imgFile.exists());

            database.DeleteImageSeed(iSeed.get_ID());

            models.ImageSeed seeddeleted = database.GetImageSeedByID(iSeed.get_ID());

            assertTrue(seeddeleted == null);

            File deletedFile = new File(iSeed.getImagefilelocation());

            assertTrue(deletedFile.exists() == false);

        }
        catch(Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    public models.ImageSeed AddImageSeedBasic() throws Exception
    {
        services.ImageService imageService = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

        //java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
        String imavbfgename = "testimage";
        String imagefilelocation = "";
        SeedType seedtype = seedTypeImage;

        File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

        //fetches from Test folder assets folder if one exists
        InputStream istream =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("underbridge.jpeg");

        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt(900000);
        String imgname = "newwave"+random+".jpeg";
        File outputfile
                = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
        imageService.SaveImage(istream, outputfile);


        models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedtype, seedProducer1);

        iSeed = database.SaveImageSeed(iSeed);

        return iSeed;
    }

    public 	android.graphics.Bitmap getBitmapFromTestAssets(String fileName) {
        android.graphics.Bitmap bitmap = null;
        try {
            android.content.Context testContext = android.support.test.InstrumentationRegistry.getInstrumentation().getContext();
            android.content.res.AssetManager assetManager = testContext.getAssets();

            java.io.InputStream testInput = assetManager.open(fileName);
            bitmap = android.graphics.BitmapFactory.decodeStream(testInput);

        }
        catch(java.io.IOException ex) {
            //not sure what to write here
        }
        finally
        {
            return bitmap;
        }

    }
    //endregion


    //region T A G  T E S T S ///////////////////////////////////

    @Test
    public void InsertTag()
    {
        String tagText = "Homelessness";

        models.Tag tag = new models.Tag(tagText);

        tag = database.AddTag(tag);

        assertTrue((tag.get_ID() != 0));

        models.Tag fetchedTag = database.GetTagByID(tag.get_ID());

        assertTrue(fetchedTag.get_ID() == tag.get_ID());
        assertTrue(fetchedTag.getTagText().equalsIgnoreCase(tag.getTagText()));
    }

    @Test
    public void UpdateTag()
    {
        String tagText = "Homelessness";

        models.Tag tag = new models.Tag(tagText);

        tag = database.AddTag(tag);

        models.Tag fetchedTag = database.GetTagByID(tag.get_ID());

        String newTagText = "Corruption";
        tag.setTagText(newTagText);

        database.UpdateTag(tag);

        models.Tag updatedTag = database.GetTagByID(tag.get_ID());

        assertTrue(updatedTag.getTagText().equalsIgnoreCase(newTagText));

    }

    @Test
    public void GetAllTags()
    {
        String tagText = "Homelessness";
        models.Tag tag = new models.Tag(tagText);
        tag = database.AddTag(tag);

        String tagText2 = "Corruption";
        models.Tag tag2 = new models.Tag(tagText2);
        tag2 = database.AddTag(tag2);

        String tagText3 = "Pollution";
        models.Tag tag3 = new models.Tag(tagText3);
        tag3 = database.AddTag(tag3);

        ArrayList<models.Tag> tags = database.GetAllTags();

        assertTrue(tags.size() == 3);
        String one = tags.get(0).getTagText();
        assertTrue( (one.equalsIgnoreCase(tagText)) || (one.equalsIgnoreCase(tagText2)) || (one.equalsIgnoreCase(tagText3)) );
        String two = tags.get(1).getTagText();
        assertTrue(two.equalsIgnoreCase(tagText) || two.equalsIgnoreCase(tagText2) || two.equalsIgnoreCase(tagText3));
        String three = tags.get(2).getTagText();
        assertTrue(three.equalsIgnoreCase(tagText) || three.equalsIgnoreCase(tagText2) || three.equalsIgnoreCase(tagText3));
    }

    @Test
    public void GetAllTagsByLetter()
    {
        String tagText = "Homelessness";
        models.Tag tag = new models.Tag(tagText);
        tag = database.AddTag(tag);

        String tagText2 = "Corruption";
        models.Tag tag2 = new models.Tag(tagText2);
        tag2 = database.AddTag(tag2);

        String tagText3 = "Haircuts";
        models.Tag tag3 = new models.Tag(tagText3);
        tag3 = database.AddTag(tag3);

        ArrayList<models.Tag> tags = database.GetAllByLetter('H');

        assertTrue(tags.size() == 2);
        String one = tags.get(0).getTagText();
        assertTrue( (one.equalsIgnoreCase(tagText))  || (one.equalsIgnoreCase(tagText3)) );
        String two = tags.get(1).getTagText();
        assertTrue(two.equalsIgnoreCase(tagText)  || two.equalsIgnoreCase(tagText3));
    }
    //endregion

    //region  G A R D E N  T E S T S  //////////////////
    @Test
    public void AddGarden() throws Exception {

        String GardenName = "Flower Garden";

        models.Garden garden = new models.Garden(GardenName);

        garden = database.SaveGarden(garden);

        assertTrue(garden.get_ID() > 0);

        models.Garden fetchedGarden = database.GetGardenByID(garden.get_ID());

        assertTrue(fetchedGarden != null);
        assertTrue(fetchedGarden.getGardenName().equalsIgnoreCase(garden.getGardenName()));

    }


    @Test
    public void AddGarden_WithVarietyOfSeeds()
    {
        try {
            AddGarden_FlowerGarden();
        }
        catch(Exception ex)
        {
            fail("failed");
        }
    }

    private Garden AddGarden_FlowerGarden() throws Exception {

        //Add some tags

        String gardenName = "Flower Garden";

        models.Garden garden = new models.Garden(gardenName);

        // T H O U G H T  S E E D 1 //////////
        String thought = "need to reduce competition";
        models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());
        seed.AddSeedTag(tag_Politics);
        seed.AddSeedTag(tag_Pollution);
        seed = database.SaveThoughtSeed(seed);

        // T H O U G H T  S E E D 2 //////////
        String thought2 = "refrain from attachment to groups";
        models.ThoughtSeed seed2 = SeedFactory.getInstance().CreateThoughtSeed(thought2, getTargetContext());
        seed2.AddSeedTag(tag_Politics);
        seed2.AddSeedTag(tag_Corruption);
        seed2 = database.SaveThoughtSeed(seed2);

        // I M A G E  S E E D  ////////////////////////////////
        services.ImageService imageService = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

        //java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
        String imavbfgename = "testimage";
        String imagefilelocation = "";
        SeedType seedtype = seedTypeImage;

        File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

        //fetches from Test folder assets folder if one exists
        InputStream istream =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("lahratree_square.png");

        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt(900000);
        String imgname = "newwave"+random+".jpeg";
        File outputfile
                = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
        imageService.SaveImage(istream, outputfile);


        models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedtype, seedProducer1);

        iSeed.AddSeedTag(tag_Pollution);

        iSeed = database.SaveImageSeed(iSeed);


        garden.addSeed(seed);
        garden.addSeed(seed2);
        garden.addSeed(iSeed);

        garden = database.SaveGarden(garden);

        assertTrue(garden.get_ID() > 0);

        models.Garden gardenfetched = database.GetGardenByID(garden.get_ID());

        assertTrue(gardenfetched != null);
        assertTrue(gardenfetched.getGardenName().equalsIgnoreCase(gardenName));
        assertTrue(gardenfetched.getSeeds().size() == 3);

        boolean thoughtsfound = true;
        for (int i=0; i < gardenfetched.getSeeds().size(); i++)
        {
            models.GardenSeed gseed = gardenfetched.getSeeds().get(i);
            long seedID = gardenfetched.getSeeds().get(i).getSeedID();
            if (gseed.getSeedType().getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED)) {
                models.ThoughtSeed ts = database.GetThoughtSeedByID(seedID);
                if (!(ts.getThought().equalsIgnoreCase(thought) || ts.getThought().equalsIgnoreCase(thought2)))
                    thoughtsfound = false;
            }
            else if(gseed.getSeedType().getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE))
            {
                models.ImageSeed is = database.GetImageSeedByID(seedID);
                assertTrue(is.get_ID() == iSeed.get_ID());
                assertTrue(is.getImagename().equalsIgnoreCase(iSeed.getImagename()));
                assertTrue(is.getImagefilelocation().equalsIgnoreCase(iSeed.getImagefilelocation()));
            }

        }
        assertTrue(thoughtsfound);

        return gardenfetched;
    }



    public void AddGarden_WeedGarden() throws Exception {

        //Add some tags

        String gardenName = "Weed Garden";

        models.Garden garden = new models.Garden(gardenName);

        // T H O U G H T  S E E D 1 //////////

        String thought = "Despite grave opposition to their party, the cabinet continued to push through a vast swathe of ideological policies which impacted the country negatively for a very long time to come. In the end, it became known that short terminist behaviour will also damage long term prospects.";
        models.ThoughtSeed seed1 = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());
        seed1.AddSeedTag(tag_Politics);
        seed1 = database.SaveThoughtSeed(seed1);

        // T H O U G H T  S E E D 2 //////////
        String thought2 = "Only light can save the masses";
        models.ThoughtSeed seed2 = SeedFactory.getInstance().CreateThoughtSeed(thought2, getTargetContext());
        seed2.AddSeedTag(tag_Politics);
        seed2 = database.SaveThoughtSeed(seed2);

        // I M A G E  S E E D  ////////////////////////////////
        services.ImageService imageService = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

        String imavbfgename = "testimage";
        String imagefilelocation = "";
        SeedType seedtype = seedTypeImage;

        File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

        //fetches from Test folder assets folder if one exists
        InputStream istream =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("crag.png");

        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt(900000);
        String imgname = "newwave"+random+".jpeg";
        File outputfile
                = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
        imageService.SaveImage(istream, outputfile);


        models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedtype, seedProducer1);

        iSeed.AddSeedTag(tag_Pollution);

        iSeed = database.SaveImageSeed(iSeed);

        // I M A G E  S E E D  2////////////////////////////////
        services.ImageService imageService2 = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

        //fetches from Test folder assets folder if one exists
        InputStream istream2 =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("assynt.png");

        int random2 = randomGenerator.nextInt(900000);
        String imgname2 = "assynt"+random2+".jpeg";
        File outputfile2
                = imageService2.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname2);
        imageService2.SaveImage(istream2, outputfile2);


        models.ImageSeed iSeed2 = new models.ImageSeed(imgname2, outputfile2.getCanonicalPath(), seedtype, seedProducer1);

        iSeed2.AddSeedTag(tag_Corruption);

        iSeed2 = database.SaveImageSeed(iSeed2);


        garden.addSeed(seed1);
        garden.addSeed(seed2);
        garden.addSeed(iSeed);
        garden.addSeed(iSeed2);
        garden = database.SaveGarden(garden);
    }

    @Test
    public void AddGardenWithBaseSeeds() throws Exception
    {
        //Add some tags
        String tagText = "Homelessness";
        models.Tag tag = new models.Tag(tagText);
        tag = database.AddTag(tag);

        String tagText2 = "Corruption";
        models.Tag tag2 = new models.Tag(tagText2);
        tag2 = database.AddTag(tag2);

        String tagText3 = "Pollution";
        models.Tag tag3 = new models.Tag(tagText3);
        tag3 = database.AddTag(tag3);

        String gardenName = "Flower Garden";

        models.Garden garden = new models.Garden(gardenName);

        // T H O U G H T  S E E D 1 //////////
        String thought = "need to reduce competition";
        models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());
        seed.AddSeedTag(tag);
        seed.AddSeedTag(tag3);
        seed = database.SaveThoughtSeed(seed);

        // T H O U G H T  S E E D 2 //////////
        String thought2 = "refrain from attachment to groups";
        models.ThoughtSeed seed2 = SeedFactory.getInstance().CreateThoughtSeed(thought2, getTargetContext());
        seed2.AddSeedTag(tag);
        seed2.AddSeedTag(tag2);
        seed2 = database.SaveThoughtSeed(seed2);

        //Now add an image seed

        models.ImageSeed iSeed = AddImageSeedBasic();


        garden.addSeed(seed);
        garden.addSeed(seed2);
        garden.addSeed(iSeed);

        garden = database.SaveGarden(garden);

        assertTrue(garden.get_ID() > 0);
        assertTrue(garden.getSeeds().size() == 3);
    }

    @Test
    public void AddGardenWithSeedsAndTags() throws Exception
    {
        //Add some tags
        String tagText = "Homelessness";
        models.Tag tag = new models.Tag(tagText);
        tag = database.AddTag(tag);

        String tagText2 = "Corruption";
        models.Tag tag2 = new models.Tag(tagText2);
        tag2 = database.AddTag(tag2);

        String tagText3 = "Pollution";
        models.Tag tag3 = new models.Tag(tagText3);
        tag3 = database.AddTag(tag3);

        String gardenName = "Flower Garden";

        models.Garden garden = new models.Garden(gardenName);

        garden.addTag(tag);
        garden.addTag(tag3);

        // T H O U G H T  S E E D 1 //////////
        String thought = "need to reduce competition";
        models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());
        seed.AddSeedTag(tag);
        seed.AddSeedTag(tag3);
        seed = database.SaveThoughtSeed(seed);

        // T H O U G H T  S E E D 2 //////////
        String thought2 = "refrain from attachment to groups";
        models.ThoughtSeed seed2 = SeedFactory.getInstance().CreateThoughtSeed(thought2, getTargetContext());
        seed2.AddSeedTag(tag);
        seed2.AddSeedTag(tag2);
        seed2 = database.SaveThoughtSeed(seed2);

        //Now add an image seed

        models.ImageSeed iSeed = AddImageSeedBasic();


        garden.addSeed(seed);
        garden.addSeed(seed2);
        garden.addSeed(iSeed);

        garden = database.SaveGarden(garden);

        assertTrue(garden.get_ID() > 0);
        assertTrue(garden.getSeeds().size() == 3);

        models.Garden fetchedgarden = database.GetGardenByID(garden.get_ID());
        assertTrue(fetchedgarden.getGardenTags().size() == 2);
    }


    //endregion

    //region T R E E S  ////////////////////////

    private Tree AddTreeWithSeeds(ImageSeed iSeed, ThoughtSeed tSeed) throws Exception
    {
        //  T R E E   /////////////////////////////////////

        String treename = "Homeless at 5pm";

        ArrayList<TreeScene> scenes = new ArrayList<>();

        int duration = 10;
        int sceneOrderNumber = 1;

        TreeScene treeScene = new TreeScene(iSeed, tSeed, duration);
        scenes.add(treeScene);

        Tree tree = new Tree(treename);
        tree.addTreeScene(treeScene);

        tree = database.SaveTree(tree);

        assertTrue(tree.get_ID() > 0);

        Tree fetchedTree = database.GetTreeByID(tree.get_ID());

        assertTrue(fetchedTree != null);
        assertTrue(fetchedTree.get_ID() >0);
        assertTrue(fetchedTree.getTreeName().equalsIgnoreCase(tree.getTreeName()));
        assertTrue(fetchedTree.getTreeScenes().size() == 1);
        assertTrue(fetchedTree.getTreeScenes().get(0).getSceneOrderNumber() == sceneOrderNumber);

        return fetchedTree;
    }

    @Test
    public void AddTreeTest() throws Exception
    {
        try
        {

            String gardenName = "Flower Garden";

            models.Garden garden = new models.Garden(gardenName);

            // T H O U G H T  S E E D 1 //////////
            String thought = "need to reduce competition";
            models.ThoughtSeed seed = SeedFactory.getInstance().CreateThoughtSeed(thought, getTargetContext());
            seed.AddSeedTag(tag_Politics);
            seed.AddSeedTag(tag_Pollution);
            seed = database.SaveThoughtSeed(seed);

            // T H O U G H T  S E E D 2 //////////
            String thought2 = "refrain from attachment to groups";
            models.ThoughtSeed seed2 = SeedFactory.getInstance().CreateThoughtSeed(thought2, getTargetContext());
            seed2.AddSeedTag(tag_Politics);
            seed2.AddSeedTag(tag_Corruption);
            seed2 = database.SaveThoughtSeed(seed2);

            // I M A G E  S E E D  ////////////////////////////////
            services.ImageService imageService = new services.ImageService(android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext());

            //java.io.File file = getFileFromPath(this, "assets/underbridge.jpeg");
            String imavbfgename = "testimage";
            String imagefilelocation = "";
            SeedType seedtype = seedTypeImage;

            File imageStorageLoc = imageService.getImageStorageDir("ImageSeeds");

            //fetches from Test folder assets folder if one exists
            InputStream istream =  android.support.test.InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getAssets().open("lahratree1000smoother.png");

            Random randomGenerator = new Random();
            int random = randomGenerator.nextInt(900000);
            String imgname = "newwave"+random+".jpeg";
            File outputfile
                    = imageService.createImageImageFile(imageStorageLoc.getCanonicalPath(), imgname);
            imageService.SaveImage(istream, outputfile);


            models.ImageSeed iSeed = new models.ImageSeed(imgname, outputfile.getCanonicalPath(), seedtype, seedProducer1);

            iSeed.AddSeedTag(tag_Pollution);

            iSeed = database.SaveImageSeed(iSeed);


            garden.addSeed(seed);
            garden.addSeed(seed2);
            garden.addSeed(iSeed);

            garden = database.SaveGarden(garden);

            List<Garden> gardens = database.GetAllGardens();
            assertTrue(gardens.size() == 1);

            garden = gardens.get(0);
            List<GardenSeed> gseeds = garden.getSeeds();
//            ImageSeed iSeed = null;
//            ThoughtSeed tSeed = null;

            //doesn't matter which thoughtseed we take.
            for (int i=0; i<gseeds.size(); i++)
            {
                GardenSeed gseed = gseeds.get(i);
                if (gseed.getSeedType().getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED)) seed = database.GetThoughtSeedByID(gseed.getSeedID());
                if (gseed.getSeedType().getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE)) iSeed = database.GetImageSeedByID(gseed.getSeedID());
            }


            //  T R E E   /////////////////////////////////////

            String treename = "Homeless at 5pm";

            ArrayList<TreeScene> scenes = new ArrayList<>();

            int duration = 10;
            //String textSpeed = Constants.SCENE_TEXTSPEED_NORMAL;
            int sceneOrderNumber = 1;

            TreeScene treeScene = new TreeScene(iSeed, seed, duration);
            scenes.add(treeScene);

            Tree tree = new Tree(treename);
            tree.addTreeScene(treeScene);

            tree = database.SaveTree(tree);

            assertTrue(tree.get_ID() > 0);

            Tree fetchedTree = database.GetTreeByID(tree.get_ID());

            assertTrue(fetchedTree != null);
            assertTrue(fetchedTree.get_ID() >0);
            assertTrue(fetchedTree.getTreeName().equalsIgnoreCase(tree.getTreeName()));
            assertTrue(fetchedTree.getTreeScenes().size() == 1);

            TreeScene scene = fetchedTree.getTreeScenes().get(0);

            assertTrue(scene.getImageSeed().getImagename().equalsIgnoreCase(iSeed.getImagename()));
            assertTrue(scene.getImageSeed().getImagefilelocation().equalsIgnoreCase(iSeed.getImagefilelocation()));
            assertTrue(scene.getImageSeed().getSeedType().getSeedTypeName().equalsIgnoreCase(iSeed.getSeedType().getSeedTypeName()));
            assertTrue(scene.getImageSeed().get_ID() == iSeed.get_ID());
            assertTrue(scene.getThoughtSeed().get_ID() == seed.get_ID());
            assertTrue(scene.getThoughtSeed().getThought().equalsIgnoreCase(seed.getThought()));
            assertTrue(scene.getSceneDuration() == duration);
            //assertTrue(scene.getTextSpeed().equalsIgnoreCase(textSpeed));

        }
        catch(Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    //endregion

    //region T R E E  S C E N E S

    @Test
    public void CreateEmptyTreeScene()
    {
        try
        {
            String treename = "Empty Tree";
            Tree tree = new Tree(treename);

            TreeScene scene = new TreeScene(null, null, 5);
            tree.addTreeScene(scene);
            tree = database.SaveTree(tree);

            Tree fetchedtree = database.GetTreeByID(tree.get_ID());
            assertTrue(fetchedtree != null);
            assertTrue(fetchedtree.get_ID() > 0);
            assertTrue(fetchedtree.getTreeScenes().size() == 1);
        }
        catch(Exception ex)
        {
            fail(ex.getMessage());
        }
    }
    //endregion


    //region E X C E P T I O N  T E S T S  /////////////////////

//    @Test
//    public void HandleExceptionGracefullyTest()
//    {
//        try
//        {
//            throw new Exception("What a load of codswallop");
//        }
//        catch (Exception ex)
//        {
//            Instrumentation mInstrumentation = android.support.test.InstrumentationRegistry.getInstrumentation();
//// We register our interest in the activity
//            Instrumentation.ActivityMonitor monitor = mInstrumentation.addMonitor(HomeActivity.class.getName(), null, false);
//// We launch it
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setClassName(mInstrumentation.getTargetContext(), HomeActivity.class.getName());
//            mInstrumentation.startActivitySync(intent);
//
//            Activity currentActivity = android.support.test.InstrumentationRegistry.getInstrumentation().waitForMonitor(monitor);
//
//            ExceptionHandler handler = new ExceptionHandler(currentActivity);
//            ExceptionError error = handler.HandleExceptionGracefully(ex);
//
//            assertTrue(error.get_ID() > 0);
//
//            ExceptionError fetchedError = database.GetExceptionErrorByID(error.get_ID());
//
//            assertTrue(fetchedError != null);
//            assertTrue(fetchedError.get_ID() > 0);
//            assertTrue(fetchedError.getError().length() > 0);
//        }
//    }
    //endregion

    //region M Y  S C E N E  R E Q U E S T S

    @Test
    public void AddMySceneRequest()
    {
        try {
            ImageSeed iSeed = AddImageSeedBasic();
            ThoughtSeed tSeed = AddThoughtSeedBasic();
            models.Tree tree = AddTreeWithSeeds(iSeed, tSeed);
            models.User user = CreateUser("Dave");

            String inkDesc = "We wish for a lyrical description of a hill in summer heat.";
            String imgDesc = "We wish for images of hills in summer";

            models.MySceneRequest request = new models.MySceneRequest(tree.get_ID(), user.get_ID(), imgDesc, inkDesc);

            //request = database.SaveSceneRequest(request);



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //endregion

    //region R A N D O M  T E S T S

    @Test
    public void AnagramTest() throws Exception {

        ArrayList<String> anagramList = Anagram("ToadFace");
        for (int i = 0; i < anagramList.size(); i++)
        {
            Log.i("Anagram:" + i, anagramList.get(i));

        }
        Log.i("Anagram:", anagramList.get(0));
        Log.i("Anagram Total:", Integer.toString(anagramList.size()));
    }

    public ArrayList<String> Anagram(String anagram)
    {
        ArrayList<String> strings = new ArrayList<String>();

        int alength = anagram.length();
        if (alength == 1)
        {
            strings.add(anagram);
            return strings;
        }
        else {

            //foreach letter in string, rotate positions
            for (int i = 0; i < anagram.length(); i++) {

                char startChar = anagram.charAt(i);

                StringBuilder otherLetters = new StringBuilder();
                for (int j = 0; j < anagram.length(); j++) {
                    if (i != j) {
                        otherLetters = otherLetters.append(anagram.charAt(j));
                    }
                }
                ArrayList<String> fetchedStrings = Anagram(otherLetters.toString());
                for (int k =0; k < fetchedStrings.size(); k++)
                {
                    StringBuilder newString = new StringBuilder();
                    newString.append(startChar);
                    newString.append(fetchedStrings.get(k));
                    strings.add(newString.toString());
                }
            }
            return strings;
        }
    }

    @Test
    public void TestAnagram2()
    {
        Anagram2("", "ToadFace");
    }
    public void Anagram2(String prefix, String anagram)
    {
        //ArrayList<String> strings = new ArrayList<String>();
        if (anagram == "")
        {
            //strings.add(prefix);
            Log.i("Anagram:", prefix);
        }
        else {
            for (int i = 0; i < anagram.length(); i++) {

                String curr = anagram.substring(i, i+1);
                String pre = anagram.substring(0, i);
                String post = anagram.substring(i + 1);
                Anagram2(prefix + curr, pre+ post);
            }
        }
    }

    public int Factorial(int number)
    {
        if (number == 1)
        {
            return 1;
        }
        else
        {
            return number * (Factorial(number -1));
        }
    }

    @Test
    public void ReverseTest() throws Exception {

        ArrayList list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList newlist = reverselist(list);

        for (int i = 0; i < newlist.size(); i++)
        {
            Log.i("valu 1e:", newlist.get(i).toString());
            //Log.i("cunio:", "cu");
        }
    }
    public ArrayList reverselist(ArrayList list)
    {
        //stop when list is one
        if (list.size() == 1)
        {
            return list;
        }
        else {
            //create new list
            ArrayList newlist = new ArrayList();
            //ArrayList newlist2 = new ArrayList();
            Object o = list.get(0);
            list.remove(0);
            //add reveselist to first postition
            newlist.addAll(reverselist(list));
            //add first position to end
            newlist.add(o);

            //System.out.print(newlist);
            return newlist;
        }
    }

    //endregion
}
