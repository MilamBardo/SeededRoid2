package db;

/**
 * Created by Talanath on 3/27/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import db.SeedenContract.ThoughtSeeds;
import db.SeedenContract.ImageSeeds;
import db.SeedenContract.BaseSeeds;
import db.SeedenContract.BaseSeedTags;
//import db.SeedenContract.SeedTags;
import db.SeedenContract.Gardens;
//import db.SeedenContract.GardenThoughtSeeds;
import db.SeedenContract.GardenSeeds;
import db.SeedenContract.GardenTags;
import db.SeedenContract.Tags;
import db.SeedenContract.Users;
import db.SeedenContract.SeedTypes;
import db.SeedenContract.Trees;
import db.SeedenContract.TreeScenes;
import db.SeedenContract.ExceptionErrors;
import db.SeedenContract.MySceneRequests;

import interfaces.ISeed;
import models.BaseSeed;
import models.BaseSeedTag;
import models.ExceptionError;
import models.Garden;
import models.GardenThoughtSeed;
import models.ImageSeed;
import models.SeedType;
import models.Tag;
import models.ThoughtSeed;
import models.Tree;
import models.TreeScene;
import models.User;


import static junit.framework.Assert.assertTrue;


//I pass beyond, where the only light is you and her.  May I be at peace again at some point.

public class MySQLiteHelper extends SQLiteOpenHelper {

    //region D B  S E T U P  ////////////////////////
    private static final String SQL_CREATE_SEEDTYPES =
            "CREATE TABLE " + SeedTypes.TABLE_NAME + " (" +
                    SeedTypes._ID + " INTEGER PRIMARY KEY," +
                    SeedTypes.COLUMN_NAME_SEEDTYPENAME + " TEXT," +
                    SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION + " TEXT,"+
                    SeedTypes.COLUMN_NAME_DATECREATED + " TEXT," +
                    SeedTypes.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";

    private static final String SQL_CREATE_THOUGHTSEEDS =
            "CREATE TABLE " + ThoughtSeeds.TABLE_NAME + " (" +
                    ThoughtSeeds._ID + " INTEGER PRIMARY KEY," +
                    ThoughtSeeds.COLUMN_NAME_THOUGHT + " TEXT," +
                    ThoughtSeeds.COLUMN_NAME_SEEDTYPEID + " INTEGER," +
                    ThoughtSeeds.COLUMN_NAME_DATECREATED + " TEXT," +
                    ThoughtSeeds.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_BASESEEDS =
            "CREATE TABLE " + BaseSeeds.TABLE_NAME + " (" +
                    BaseSeeds._ID + " INTEGER PRIMARY KEY," +
                    BaseSeeds.COLUMN_NAME_SEEDTYPEID + " INTEGER," +
                    BaseSeeds.COLUMN_NAME_DATECREATED + " TEXT," +
                    BaseSeeds.COLUMN_NAME_DATEUPDATED + " TEXT," +
                    BaseSeeds.COLUMN_NAME_CREATEDBY + " TEXT" +
                    ");";
    private static final String SQL_CREATE_BASESEEDTAGS =
            "CREATE TABLE " + BaseSeedTags.TABLE_NAME + " (" +
                    BaseSeedTags._ID + " INTEGER PRIMARY KEY," +
                    BaseSeedTags.COLUMN_NAME_BASESEEDID + " TEXT," +
                    BaseSeedTags.COLUMN_NAME_TAGID + " INTEGER," +
                    BaseSeedTags.COLUMN_NAME_DATECREATED + " TEXT," +
                    BaseSeedTags.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
//    private static final String SQL_CREATE_THOUGHTSEEDTAGS =
//            "CREATE TABLE " + ThoughtSeedTags.TABLE_NAME + " (" +
//                    ThoughtSeedTags._ID + " INTEGER PRIMARY KEY," +
//                    ThoughtSeedTags.COLUMN_NAME_THOUGHTSEEDID + " TEXT," +
//                    ThoughtSeedTags.COLUMN_NAME_TAGID + " INTEGER," +
//                    ThoughtSeedTags.COLUMN_NAME_DATECREATED + " TEXT," +
//                    ThoughtSeedTags.COLUMN_NAME_DATEUPDATED + " TEXT" +
//                    ");";
//    private static final String SQL_CREATE_SEEDTAGS =
//            "CREATE TABLE " + SeedTags.TABLE_NAME + " (" +
//                    SeedTags._ID + " INTEGER PRIMARY KEY," +
//                    SeedTags.COLUMN_NAME_SEEDID + " TEXT," +
//                    SeedTags.COLUMN_NAME_TAGID + " INTEGER," +
//                    SeedTags.COLUMN_NAME_DATECREATED + " TEXT," +
//                    SeedTags.COLUMN_NAME_DATEUPDATED + " TEXT" +
//                    ");";
    private static final String SQL_CREATE_GARDENS =
            "CREATE TABLE " + Gardens.TABLE_NAME + " (" +
                    Gardens._ID + " INTEGER PRIMARY KEY," +
                    Gardens.COLUMN_NAME_GARDENNAME + " TEXT," +
                    Gardens.COLUMN_NAME_DATECREATED + " TEXT," +
                    Gardens.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_GARDENSEEDS =
            "CREATE TABLE " + GardenSeeds.TABLE_NAME + " (" +
                    GardenSeeds._ID + " INTEGER PRIMARY KEY," +
                    GardenSeeds.COLUMN_NAME_GARDENID + " INTEGER," +
                    GardenSeeds.COLUMN_NAME_SEEDTYPEID + " TEXT," +
                    GardenSeeds.COLUMN_NAME_SEEDID + " TEXT," +
                    GardenSeeds.COLUMN_NAME_DATECREATED + " TEXT," +
                    GardenSeeds.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
//    private static final String SQL_CREATE_GARDENTHOUGHTSEEDS =
//            "CREATE TABLE " + GardenThoughtSeeds.TABLE_NAME + " (" +
//                    GardenThoughtSeeds._ID + " INTEGER PRIMARY KEY," +
//                    GardenThoughtSeeds.COLUMN_NAME_GARDENID + " INTEGER," +
//                    GardenThoughtSeeds.COLUMN_NAME_THOUGHTSEEDID + " INTEGER," +
//                    GardenThoughtSeeds.COLUMN_NAME_DATECREATED + " TEXT," +
//                    GardenThoughtSeeds.COLUMN_NAME_DATEUPDATED + " TEXT" +
//                    ");";
    private static final String SQL_CREATE_GARDENTAGS =
            "CREATE TABLE " + GardenTags.TABLE_NAME + " (" +
                    GardenTags._ID + " INTEGER PRIMARY KEY," +
                    GardenTags.COLUMN_NAME_GARDENID + " INTEGER," +
                    GardenTags.COLUMN_NAME_TAGID + " INTEGER," +
                    GardenTags.COLUMN_NAME_DATECREATED + " TEXT," +
                    GardenTags.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_TAGS =
            "CREATE TABLE " + Tags.TABLE_NAME + " (" +
                    Tags._ID + " INTEGER PRIMARY KEY," +
                    Tags.COLUMN_NAME_TAGTEXT + " TEXT," +
                    Tags.COLUMN_NAME_DATECREATED + " TEXT," +
                    Tags.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + Users.TABLE_NAME + " (" +
                    Users._ID + " INTEGER PRIMARY KEY," +
                    Users.COLUMN_NAME_USERNAME + " INTEGER," +
                    Users.COLUMN_NAME_GLOBALID + " TEXT," +
                    Users.COLUMN_NAME_PASSWORD + " TEXT," +
                    Users.COLUMN_NAME_DATECREATED + " TEXT," +
                    Users.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_IMAGESEEDS =
            "CREATE TABLE " + ImageSeeds.TABLE_NAME + " (" +
                    ImageSeeds._ID + " INTEGER PRIMARY KEY," +
                    ImageSeeds.COLUMN_NAME_IMAGENAME + " TEXT," +
                    ImageSeeds.COLUMN_NAME_IMAGEFILELOCATION + " TEXT," +
                    //GardenSeeds.COLUMN_NAME_SEEDTYPEID + " TEXT," +
                    ImageSeeds.COLUMN_NAME_DATECREATED + " TEXT," +
                    ImageSeeds.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_TREES =
            "CREATE TABLE " + Trees.TABLE_NAME + " (" +
                    Trees._ID + " INTEGER PRIMARY KEY," +
                    Trees.COLUMN_NAME_TREENAME + " TEXT," +
                    Trees.COLUMN_NAME_DATECREATED + " TEXT," +
                    Trees.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_TREESCENES =
            "CREATE TABLE " + TreeScenes.TABLE_NAME + " (" +
                    TreeScenes._ID + " INTEGER PRIMARY KEY," +
                    TreeScenes.COLUMN_NAME_TREEID + " INTEGER," +
                    TreeScenes.COLUMN_NAME_IMAGESEEDID + " INTEGER," +
                    TreeScenes.COLUMN_NAME_THOUGHTSEEDID + " INTEGER," +
                    TreeScenes.COLUMN_NAME_SCENEDURATION + " INTEGER," +
                    TreeScenes.COLUMN_NAME_SCENEORDERNUMBER + " INTEGER," +
                    TreeScenes.COLUMN_NAME_TEXTSPEED + " TEXT," +
                    TreeScenes.COLUMN_NAME_DATECREATED + " TEXT," +
                    TreeScenes.COLUMN_NAME_DATEUPDATED + " TEXT" +
                    ");";
    private static final String SQL_CREATE_MYSCENEREQUESTS =
            "CREATE TABLE " + MySceneRequests.TABLE_NAME + " (" +
                    MySceneRequests._ID + " INTEGER PRIMARY KEY," +
                    MySceneRequests.COLUMN_NAME_TREEID + " INTEGER," +
                    MySceneRequests.COLUMN_NAME_GLOBALID + " TEXT," +
                    MySceneRequests.COLUMN_NAME_USERID + " INTEGER," +
                    MySceneRequests.COLUMN_NAME_IMAGEDESCRIPTION + " TEXT," +
                    MySceneRequests.COLUMN_NAME_INKDESCRIPTION + " TEXT," +
                    MySceneRequests.COLUMN_NAME_UPLOADED + " INTEGER," +
                    MySceneRequests.COLUMN_NAME_UPLOADDATE + " TEXT " +
                    ");";

    private static final String SQL_CREATE_EXCEPTIONERRORS =
            "CREATE TABLE " + ExceptionErrors.TABLE_NAME + " (" +
                    ExceptionErrors._ID + " INTEGER PRIMARY KEY," +
                    ExceptionErrors.COLUMN_NAME_GLOBALID + " TEXT," +
                    ExceptionErrors.COLUMN_NAME_ERROR + " TEXT," +
                    ExceptionErrors.COLUMN_NAME_DATECREATED + " TEXT," +
                    ExceptionErrors.COLUMN_NAME_UPLOADED + " INTEGER" +
                    ExceptionErrors.COLUMN_NAME_UPLOADDATE + " TEXT" +
                    ");";

    private static final String SQL_DELETE_SEEDTYPES =
            ";DROP TABLE IF EXISTS " + SeedTypes.TABLE_NAME;
    private static final String SQL_DELETE_THOUGHTSEEDS =
            "DROP TABLE IF EXISTS " + ThoughtSeeds.TABLE_NAME;
    private static final String SQL_DELETE_BASESEEDS =
            "DROP TABLE IF EXISTS " + BaseSeeds.TABLE_NAME;
    private static final String SQL_DELETE_BASESEEDTAGS =
            ";DROP TABLE IF EXISTS " + BaseSeedTags.TABLE_NAME;
//    private static final String SQL_DELETE_SEEDTAGS =
//            ";DROP TABLE IF EXISTS " + SeedTags.TABLE_NAME;
    private static final String SQL_DELETE_GARDENS =
            ";DROP TABLE IF EXISTS " + Gardens.TABLE_NAME;
//    private static final String SQL_DELETE_GARDENTHOUGHTSEEDS =
//            ";DROP TABLE IF EXISTS " + GardenThoughtSeeds.TABLE_NAME;
    private static final String SQL_DELETE_GARDENSEEDS =
            ";DROP TABLE IF EXISTS " + GardenSeeds.TABLE_NAME;
    private static final String SQL_DELETE_GARDENTAGS =
            ";DROP TABLE IF EXISTS " + GardenTags.TABLE_NAME;
    private static final String SQL_DELETE_TAGS =
            ";DROP TABLE IF EXISTS " + Tags.TABLE_NAME;
    private static final String SQL_DELETE_USERS =
            ";DROP TABLE IF EXISTS " + Users.TABLE_NAME;
    private static final String SQL_DELETE_IMAGESEEDS =
            ";DROP TABLE IF EXISTS " + ImageSeeds.TABLE_NAME;
    private static final String SQL_DELETE_TREES =
            ";DROP TABLE IF EXISTS " + Trees.TABLE_NAME;
    private static final String SQL_DELETE_TREESCENES =
            ";DROP TABLE IF EXISTS " + TreeScenes.TABLE_NAME;
    private static final String SQL_DELETE_EXCEPTIONERRORS =
            ";DROP TABLE IF EXISTS " + ExceptionErrors.TABLE_NAME;
    private static final String SQL_DELETE_MYSCENEREQUESTS =
            ";DROP TABLE IF EXISTS " + MySceneRequests.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Seeden.db";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_SEEDTYPES);
        db.execSQL(SQL_DELETE_THOUGHTSEEDS);
        db.execSQL(SQL_DELETE_BASESEEDS);
        db.execSQL(SQL_DELETE_BASESEEDTAGS);
        //db.execSQL(SQL_DELETE_SEEDTAGS);
        db.execSQL(SQL_DELETE_GARDENS);
        //db.execSQL(SQL_DELETE_GARDENTHOUGHTSEEDS);
        db.execSQL(SQL_DELETE_GARDENSEEDS);
        db.execSQL(SQL_DELETE_GARDENTAGS);
        db.execSQL(SQL_DELETE_TAGS);
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_IMAGESEEDS);
        db.execSQL(SQL_DELETE_TREES);
        db.execSQL(SQL_DELETE_TREESCENES);
        db.execSQL(SQL_DELETE_EXCEPTIONERRORS);
        db.execSQL(SQL_DELETE_MYSCENEREQUESTS);

        db.execSQL(SQL_CREATE_SEEDTYPES);
        db.execSQL(SQL_CREATE_THOUGHTSEEDS);
        db.execSQL(SQL_CREATE_BASESEEDS);
        db.execSQL(SQL_CREATE_BASESEEDTAGS);
        //db.execSQL(SQL_CREATE_SEEDTAGS);
        db.execSQL(SQL_CREATE_GARDENS);
        //db.execSQL(SQL_CREATE_GARDENTHOUGHTSEEDS);
        db.execSQL(SQL_CREATE_GARDENSEEDS);
        db.execSQL(SQL_CREATE_GARDENTAGS);
        db.execSQL(SQL_CREATE_TAGS);
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_IMAGESEEDS);
        db.execSQL(SQL_CREATE_TREES);
        db.execSQL(SQL_CREATE_TREESCENES);
        db.execSQL(SQL_CREATE_EXCEPTIONERRORS);
        db.execSQL(SQL_CREATE_MYSCENEREQUESTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_SEEDTYPES);
        db.execSQL(SQL_DELETE_THOUGHTSEEDS);
        db.execSQL(SQL_DELETE_BASESEEDS);
        db.execSQL(SQL_DELETE_BASESEEDTAGS);
        //db.execSQL(SQL_DELETE_SEEDTAGS);
        db.execSQL(SQL_DELETE_GARDENS);
        //db.execSQL(SQL_DELETE_GARDENTHOUGHTSEEDS);
        db.execSQL(SQL_DELETE_GARDENSEEDS);
        db.execSQL(SQL_DELETE_GARDENTAGS);
        db.execSQL(SQL_DELETE_TAGS);
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_IMAGESEEDS);
        db.execSQL(SQL_DELETE_TREES);
        db.execSQL(SQL_DELETE_TREESCENES);
        db.execSQL(SQL_DELETE_EXCEPTIONERRORS);
        db.execSQL(SQL_DELETE_MYSCENEREQUESTS);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //endregion

    //region B A S E S E E D S /////////////////////////////////

    private BaseSeed AddBaseSeed(BaseSeed seed) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(BaseSeeds.COLUMN_NAME_SEEDTYPEID, seed.getSeedType().get_ID());
            values.put(BaseSeeds.COLUMN_NAME_DATECREATED, new Date().getTime());
            values.put(BaseSeeds.COLUMN_NAME_CREATEDBY, seed.getCreatedBy().get_ID());
            long idnew = db.insertOrThrow(BaseSeeds.TABLE_NAME, null, values);
            seed.set_ID(idnew);

            return seed;

        } finally {
            db.close();
        }
    }

    public List<BaseSeed> GetBaseSeedsRecentByNumberAndUserId(int number, User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            List<BaseSeed> seeds = new ArrayList<BaseSeed>();

            Cursor cursor = db.rawQuery(
                    "Select b._id, b.seedtypeid, s.seedtypename, s.seediconfilelocation, u._id, u.username, u.globalid " +
                            "From baseseeds b " +
                            "INNER JOIN seedtypes s " +
                            "on b.seedtypeid = s._id " +
                            "INNER JOIN users u "+
                            "on b.createdby = u._id "+
                            "WHERE b.createdby = ? " +
                            "ORDER BY b.datecreated ASC " +
                            "LIMIT ?"
                    ,
                    new String[]{Long.toString(user.get_ID()), number + ""}
            );
            while (cursor.moveToNext()) {

                long seedId = cursor.getLong(0);
                Long seedtypeid = cursor.getLong(1);
                String typeName = cursor.getString(2);
                String typeIconLocation = cursor.getString(3);
                Long userid = cursor.getLong(4);
                String userName = cursor.getString(5);
                UUID globalID = UUID.fromString( cursor.getString(6) );
                models.SeedType type = new models.SeedType(seedtypeid, typeName, typeIconLocation);
                User createdBy = new User(userid, userName, globalID);
                models.BaseSeed seed = new models.BaseSeed(seedId, type, createdBy);
                ArrayList<BaseSeedTag> tags = GetSeedTagsBySeedID(seedId);
                seed.setTags(tags);
                seeds.add(seed);
            }

            cursor.close();

            return seeds;
        } finally {
            db.close();
        }
    }
//    private BaseSeedTag SaveBaseSeedTag(BaseSeedTag btag)
//    {
//        if (btag.get_ID() == 0)
//        {
//            Tag tag = btag.getTag();
//            Tag fetched = GetTagByDesc(tag.getTagText());
//            if (fetched == null)
//            {
//                tag = AddTag(tag);
//            }
//
//            btag = AddBaseSeedTag(btag);
//        }
//
//        return btag;
//    }

//    private BaseSeedTag AddBaseSeedTag (BaseSeedTag btag)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(BaseSeedTags.COLUMN_NAME_TAGID, btag.get_ID());
//
//            long idnew = db.insertOrThrow(BaseSeedTags.TABLE_NAME, null, values);
//            btag.set_ID(idnew);
//
//
//            db.close();
//
//            return btag;
//
//        } finally {
//            db.close();
//        }
//    }

    //endregion

    //region  I S E E D S  ////////////////////////////

    public List<ISeed> GetISeedsRecentByNumber(int number)
    {
        List<ISeed> seeds = new ArrayList<ISeed>();

        return seeds;
    }

    /*
    Greater than for Date1, Less than or equal to for date 2
     */
    public List<ISeed> GetISeedsByDates(Date date1, Date date2)
    {
        List<ISeed> seeds = new ArrayList<ISeed>();

        return seeds;
    }

    //endregion

    //region T H O U G H T S E E D S  //////////////////////////

    public models.ThoughtSeed SaveThoughtSeed(models.ThoughtSeed thoughtseed) throws Exception {
        if (thoughtseed.get_ID() != 0) {

            thoughtseed = UpdateThoughtSeed(thoughtseed);
        } else {
            thoughtseed = (models.ThoughtSeed)AddBaseSeed(thoughtseed);
            thoughtseed = AddThoughtSeed(thoughtseed);
        }

        //Now Add Tags
        ArrayList<BaseSeedTag> tags = thoughtseed.getTags();
        for (int i = 0; i < tags.size(); i++) {
            BaseSeedTag stag = tags.get(i);
            stag = SaveSeedTag(stag);

        }

        return thoughtseed;
    }

    private models.ThoughtSeed AddThoughtSeed (models.ThoughtSeed thoughtseed) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ThoughtSeeds._ID, thoughtseed.get_ID());
            values.put(ThoughtSeeds.COLUMN_NAME_SEEDTYPEID, thoughtseed.getSeedType().get_ID());
            values.put(ThoughtSeeds.COLUMN_NAME_THOUGHT, thoughtseed.getThought());

            long idnew = db.insertOrThrow(ThoughtSeeds.TABLE_NAME, null, values);
            thoughtseed.set_ID(idnew);

            db.close();
            return thoughtseed;

        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
            throw ex;
        }
        finally {
            db.close();
        }
    }



    private models.ThoughtSeed UpdateThoughtSeed(models.ThoughtSeed thoughtseed) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(ThoughtSeeds.COLUMN_NAME_THOUGHT, thoughtseed.getThought());

            long success = db.update(ThoughtSeeds.TABLE_NAME, values, ThoughtSeeds._ID + "=" + thoughtseed.get_ID(), null);

            return thoughtseed;
        } finally {
            db.close();
        }
    }

    public models.ThoughtSeed GetThoughtSeedByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
//            String[] projection = {
//                    ThoughtSeeds.TABLE_NAME+"."+ThoughtSeeds._ID,
//                    ThoughtSeeds.COLUMN_NAME_SEEDTYPEID,
//                    ThoughtSeeds.COLUMN_NAME_THOUGHT,
//                    ThoughtSeeds.COLUMN_NAME_SEEDTYPEID,
//                    SeedTypes.COLUMN_NAME_SEEDTYPENAME,
//                    SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION
//            };
//
//// Filter results WHERE "title" = 'My Title'
//            String selection = ThoughtSeeds.TABLE_NAME+"."+ThoughtSeeds._ID + " = ?" + " AND "+ ThoughtSeeds.TABLE_NAME+"."+ThoughtSeeds.COLUMN_NAME_SEEDTYPEID +" = "+SeedTypes.TABLE_NAME+"."+SeedTypes._ID;
//            String[] selectionArgs = {Long.toString(id)};
//
//// How you want the results sorted in the resulting Cursor
//            String sortOrder =
//                    ThoughtSeeds.TABLE_NAME+"."+ThoughtSeeds._ID + " DESC";
//
//            Cursor cursor = db.query(
//                    ThoughtSeeds.TABLE_NAME +","+ SeedTypes.TABLE_NAME,                     // The table to query
//                    projection,                               // The columns to return
//                    selection,                                // The columns for the WHERE clause
//                    selectionArgs,                            // The values for the WHERE clause
//                    null,                                     // don't group the rows
//                    null,                                     // don't filter by row groups
//                    sortOrder                                 // The sort order
//            );

            Cursor cursor = db.rawQuery(
                    "Select b._id, i.thought, b.seedtypeid, s.seedtypename, s.seediconfilelocation, u._id, u.username, u.globalid " +
                            "From baseseeds b "+
                            "INNER JOIN thoughtseeds i "+
                            "on b._id = i._id " +
                            "INNER JOIN seedtypes s " +
                            "on b.seedtypeid = s._id " +
                            "INNER JOIN users u "+
                            "on b.createdby = u._id "+
                            "WHERE b._id = ?"
                    ,
                    new String[] {id + ""}
            );
            ArrayList<ThoughtSeed> seeds = new ArrayList<ThoughtSeed>();
            while (cursor.moveToNext()) {

                long seedId = cursor.getLong(0);
                String thought = cursor.getString(1);
                long typeID = cursor.getLong(2);
                String typeName = cursor.getString(3);
                String typeIconLocation = cursor.getString(4);
                Long userid = cursor.getLong(5);
                String userName = cursor.getString(6);
                UUID globalID = UUID.fromString( cursor.getString(7) );
                models.SeedType type = new models.SeedType(typeID, typeName, typeIconLocation);
                User createdBy = new User(userid, userName, globalID);
                models.ThoughtSeed seed = new models.ThoughtSeed(seedId, thought, type, createdBy);
                ArrayList<BaseSeedTag> tags = GetSeedTagsBySeedID(seedId);
                seed.setTags(tags);
                seeds.add(seed);
            }
            cursor.close();

            if (seeds.size() == 0) return null;
            if (seeds.size() > 1) return null;

            return seeds.get(0);
        }
        finally {
            db.close();
        }
    }

    public void DeleteThoughtSeed (long thoughtSeedID)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            String where0 = GardenSeeds.COLUMN_NAME_SEEDID + "=?";
            String[] whereArgs0 = new String[]{String.valueOf(thoughtSeedID)};
            long success0 = db.delete(GardenSeeds.TABLE_NAME, where0, whereArgs0);

            String where = BaseSeeds._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(thoughtSeedID)};
            long success = db.delete(BaseSeeds.TABLE_NAME, where, whereArgs);

            String where2 = ThoughtSeeds._ID + "=?";
            String[] whereArgs2 = new String[]{String.valueOf(thoughtSeedID)};
            long success2 = db.delete(ThoughtSeeds.TABLE_NAME, where2, whereArgs2);



        }catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }

//        SQLiteDatabase db2 = this.getReadableDatabase();
//        try {
//            String where2 = ImageSeeds._ID + "=?";
//            String[] whereArgs2 = new String[] {String.valueOf(imageSeedID)};
//            long success2 = db2.delete(ImageSeeds.TABLE_NAME, where2, whereArgs2);
//        } finally {
//            db2.close();
//        }
    }
    //endregion

    //region I M A G E  S E E D /////////////////

    public models.ImageSeed SaveImageSeed(models.ImageSeed imageseed) {
        if (imageseed.get_ID() != 0) {
            //update image seed
            imageseed = imageseed;
        } else {
            imageseed = (models.ImageSeed) AddBaseSeed(imageseed);
            imageseed = AddImageSeed(imageseed);
        }

        //Now Add Tags
        ArrayList<BaseSeedTag> tags = imageseed.getTags();
        for (int i = 0; i < tags.size(); i++) {
            BaseSeedTag stag = tags.get(i);
            stag = SaveSeedTag(stag);
        }

        return imageseed;
    }

    public models.ImageSeed AddImageSeed(models.ImageSeed imageseed) {
        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(ImageSeeds._ID, imageseed.get_ID());
            values.put(ImageSeeds.COLUMN_NAME_IMAGENAME, imageseed.getImagename());
            values.put(ImageSeeds.COLUMN_NAME_IMAGEFILELOCATION, imageseed.getImagefilelocation());

            long id = db.insertOrThrow(ImageSeeds.TABLE_NAME, null, values);

            imageseed.set_ID(id);
            return imageseed;
        }
        finally {
            db.close();
        }
    }

    public models.ImageSeed GetImageSeedByID(long imageSeedID) {
        SQLiteDatabase db = this.getReadableDatabase();

        //NOW PERFORM READ
        //database.addExpenseType(new ExpenseType("Food"));
//        String[] projection = {
//                ImageSeeds.TABLE_NAME+"."+ImageSeeds._ID,
//                ImageSeeds.TABLE_NAME+"."+ImageSeeds.COLUMN_NAME_IMAGENAME,
//                ImageSeeds.TABLE_NAME+"."+ImageSeeds.COLUMN_NAME_IMAGEFILELOCATION,
//                BaseSeeds.TABLE_NAME+"."+BaseSeeds.COLUMN_NAME_SEEDTYPEID,
//                SeedTypes.TABLE_NAME+"."+SeedTypes.COLUMN_NAME_SEEDTYPENAME,
//                SeedTypes.TABLE_NAME+"."+SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION
//        };

//        String[] projectionSeedType = {
//                SeedTypes._ID,
//                SeedTypes.COLUMN_NAME_SEEDTYPENAME,
//                SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION
//        };

        // Filter results WHERE "title" = 'My Title'
//        String selection =
//                ImageSeeds.TABLE_NAME+"."+ImageSeeds._ID +" = "+BaseSeeds.TABLE_NAME+"."+BaseSeeds._ID + " AND "+
//                        BaseSeeds.TABLE_NAME+"."+BaseSeeds.COLUMN_NAME_SEEDTYPEID +" = "+SeedTypes.TABLE_NAME+"."+SeedTypes._ID + " AND "+
//                        ImageSeeds.TABLE_NAME+"."+ImageSeeds._ID + " = ?";
//        String[] selectionArgs = {Long.toString(imageSeedID)};

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                ImageSeeds.TABLE_NAME+"."+ImageSeeds._ID + " DESC";
//
//        Cursor cursor = db.query(
//                BaseSeeds.TABLE_NAME+","+ImageSeeds.TABLE_NAME + ","+SeedTypes.TABLE_NAME,                    // The table to query
//                projection,                              // The columns to return
//                selection,                                // The columns for the WHERE clause
//                selectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder                                 // The sort order
//        );
        try {
            Cursor cursor = db.rawQuery(
                    "Select b._id, i.imagename, i.imagefilelocation, b.seedtypeid, s.seedtypename, s.seediconfilelocation,  u._id, u.username, u.globalid " +
                            "From baseseeds b " +
                            "INNER JOIN imageseeds i " +
                            "on b._id = i._id " +
                            "INNER JOIN seedtypes s " +
                            "on b.seedtypeid = s._id " +
                            "INNER JOIN users u " +
                            "on b.createdby = u._id " +
                            "WHERE b._id = ?"
                    ,
                    new String[]{imageSeedID + ""}
            );

            ArrayList<ImageSeed> iseeds = new ArrayList<>();
            while (cursor.moveToNext()) {

                long iID = cursor.getLong(0);
                //assertTrue(iID == imageSeedID);
                String iName = cursor.getString(1);
                String iLocation = cursor.getString(2);
                long typeID = cursor.getLong(3);
                String typeName = cursor.getString(4);
                String typeIconLocation = cursor.getString(5);
                Long userid = cursor.getLong(6);
                String userName = cursor.getString(7);
                UUID globalID = UUID.fromString(cursor.getString(8));
                //don't care about the seeed type name
                models.SeedType seedtype = new models.SeedType(typeID, typeName, typeIconLocation);
                ArrayList<BaseSeedTag> tags = GetSeedTagsBySeedID(iID);
                User createdBy = new User(userid, userName, globalID);
                models.ImageSeed iSeed = new models.ImageSeed(iID, iName, iLocation, tags, seedtype, createdBy);
                iseeds.add(iSeed);
            }
            cursor.close();
            if (iseeds.size() == 0) {
                return null;
            }
            else {
                return iseeds.get(0);
            }
        }
        finally
        {
            db.close();
        }
    }

    public void DeleteImageSeed (long imageSeedID)
    {
        ImageSeed fetched = GetImageSeedByID(imageSeedID);
        String dir = fetched.getImagefilelocation();
        File file = new File(dir);
        boolean deleted = file.delete();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            String where0 = GardenSeeds.COLUMN_NAME_SEEDID + "=?";
            String[] whereArgs0 = new String[]{String.valueOf(imageSeedID)};
            long success0 = db.delete(GardenSeeds.TABLE_NAME, where0, whereArgs0);

            String where = BaseSeeds._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(imageSeedID)};
            long success = db.delete(BaseSeeds.TABLE_NAME, where, whereArgs);

            String where2 = ImageSeeds._ID + "=?";
            String[] whereArgs2 = new String[]{String.valueOf(imageSeedID)};
            long success2 = db.delete(ImageSeeds.TABLE_NAME, where2, whereArgs2);



        }catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }

//        SQLiteDatabase db2 = this.getReadableDatabase();
//        try {
//            String where2 = ImageSeeds._ID + "=?";
//            String[] whereArgs2 = new String[] {String.valueOf(imageSeedID)};
//            long success2 = db2.delete(ImageSeeds.TABLE_NAME, where2, whereArgs2);
//        } finally {
//            db2.close();
//        }
    }

//    private models.GardenThoughtSeed AddImageSeed(GardenThoughtSeed gardenseed) {
//        //Just need the ids for the Database
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(ImageSeeds._ID, gardenseed.getGardenID());
//        values.put(ImageSeeds., gardenseed.getThoughtSeed().get_ID());
//
//        long id = db.insertOrThrow(GardenThoughtSeeds.TABLE_NAME, null, values);
//
//        if (id < 1) return null;
//
//        gardenseed.set_ID(id);
//        db.close();
//        return gardenseed;
//    }

    //endregion

    //region T A G S  /////////////////////////////

    public models.Tag AddTag(models.Tag tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        //tag.set_ID(UUID.randomUUID());
        // New value for one column
        try {
            ContentValues values = new ContentValues();
            //values.put(Tags._ID, tag.get_ID().toString());
            values.put(Tags.COLUMN_NAME_TAGTEXT, tag.getTagText());

            long tagid = db.insertOrThrow(Tags.TABLE_NAME, null, values);

            tag.set_ID(tagid);

            return tag;
        }
        finally {
            db.close();
        }

    }

    public models.Tag UpdateTag(models.Tag tag) {
        SQLiteDatabase db = this.getReadableDatabase();

        try
        {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Tags.COLUMN_NAME_TAGTEXT, tag.getTagText());

        long success = db.update(Tags.TABLE_NAME, values, Tags._ID + "=" + tag.get_ID(), null);

            return tag;
        }
        finally {
            db.close();
        }

    }

    public models.Tag GetTagByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    Tags._ID,
                    Tags.COLUMN_NAME_TAGTEXT
            };

// Filter results WHERE "title" = 'My Title'
            String selection = Tags._ID + " = ?";
            String[] selectionArgs = {Long.toString(id)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Tags._ID + " DESC";

            Cursor cursor = db.query(
                    Tags.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.Tag> tags = new ArrayList<models.Tag>();
            while (cursor.moveToNext()) {

                Long TagId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Tags._ID));
                String tagtext = cursor.getString(1);
                models.Tag tag = new models.Tag(TagId, null, tagtext);
                tags.add(tag);
            }
            cursor.close();

            if (tags.size() == 0) return null;
            if (tags.size() > 1) return null;
            return tags.get(0);
        }
        finally {
            db.close();
        }

    }

    public models.Tag GetTagByDesc(String desc) {
        SQLiteDatabase db = this.getReadableDatabase();

        //NOW PERFORM READ
        //database.addExpenseType(new ExpenseType("Food"));
        String[] projection = {
                Tags._ID,
                Tags.COLUMN_NAME_TAGTEXT
        };

// Filter results WHERE "title" = 'My Title'
        String selection = Tags.COLUMN_NAME_TAGTEXT + " = ?";
        String[] selectionArgs = {desc};

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Tags._ID + " DESC";

        Cursor cursor = db.query(
                Tags.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<models.Tag> tags = new ArrayList<models.Tag>();
        while (cursor.moveToNext()) {

            Long TagId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Tags._ID));
            String tagtext = cursor.getString(1);
            models.Tag tag = new models.Tag(TagId, null, tagtext);
            tags.add(tag);
        }
        cursor.close();

        if (tags.size() == 0) return null;
        if (tags.size() > 1) return null;
        db.close();
        return tags.get(0);
    }

    public ArrayList<models.Tag> GetAllTags() {
        SQLiteDatabase db = this.getReadableDatabase();

        //NOW PERFORM READ
        //database.addExpenseType(new ExpenseType("Food"));
        String[] projection = {
                Tags._ID,
                Tags.COLUMN_NAME_TAGTEXT
        };

// Filter results WHERE "title" = 'My Title'
        //String selection = Tags._ID+ " = ?";
        //String[] selectionArgs = {Long.toString(id)};

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Tags._ID + " DESC";

        Cursor cursor = db.query(
                Tags.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null, //selection,                                // The columns for the WHERE clause
                null, //selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<models.Tag> tags = new ArrayList<models.Tag>();
        while (cursor.moveToNext()) {

            Long TagId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Tags._ID));
            String tagtext = cursor.getString(1);
            models.Tag tag = new models.Tag(TagId, null, tagtext);
            tags.add(tag);
        }
        cursor.close();

        db.close();
        return tags;
    }

    public ArrayList<models.Tag> GetAllByLetter(char c) {
        SQLiteDatabase db = this.getReadableDatabase();

        //NOW PERFORM READ
        //database.addExpenseType(new ExpenseType("Food"));
        String[] projection = {
                Tags._ID,
                Tags.COLUMN_NAME_TAGTEXT
        };

// Filter results WHERE "title" = 'My Title'
        String selection = Tags.COLUMN_NAME_TAGTEXT + " LIKE ?";
        String[] selectionArgs = {Character.toString(c) + "%"};

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Tags._ID + " DESC";

        Cursor cursor = db.query(
                Tags.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<models.Tag> tags = new ArrayList<models.Tag>();
        while (cursor.moveToNext()) {

            Long TagId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Tags._ID));
            String tagtext = cursor.getString(1);
            models.Tag tag = new models.Tag(TagId, null, tagtext);
            tags.add(tag);
        }
        cursor.close();

        db.close();
        return tags;
    }
    //endregion

    //region  U S E R  ///////////////////////////////////////////////////////////

    public models.User AddUser(models.User user) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_NAME_USERNAME, user.getUserName());
        values.put(Users.COLUMN_NAME_GLOBALID, user.getGlobalID().toString());


        long id = db.insertOrThrow(Users.TABLE_NAME, null, values);

        if (id < 1) return null;

        user.set_ID(id);
        db.close();
        return user;
    }

    public models.User GetUserByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        //NOW PERFORM READ
        //database.addExpenseType(new ExpenseType("Food"));
        String[] projection = {
                Users._ID,
                Users.COLUMN_NAME_USERNAME,
                Users.COLUMN_NAME_GLOBALID
        };

// Filter results WHERE "title" = 'My Title'
        String selection = Users._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Users._ID + " DESC";

        Cursor cursor = db.query(
                Users.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<models.User> users = new ArrayList<models.User>();
        while (cursor.moveToNext()) {

            long UserId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Users._ID));
            String username = cursor.getString(1);
            UUID globalId = UUID.fromString(cursor.getString(2));
            models.User user = new models.User(UserId, username, globalId);
            users.add(user);
        }
        cursor.close();

        if (users.size() == 0) return null;
        if (users.size() > 1) return null;
        db.close();
        return users.get(0);
    }

    public models.User GetUserByName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        //NOW PERFORM READ
        //database.addExpenseType(new ExpenseType("Food"));
        String[] projection = {
                Users._ID,
                Users.COLUMN_NAME_USERNAME,
                Users.COLUMN_NAME_GLOBALID
        };

// Filter results WHERE "title" = 'My Title'
        String selection = Users.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Users._ID + " DESC";

        Cursor cursor = db.query(
                Users.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<models.User> users = new ArrayList<models.User>();
        while (cursor.moveToNext()) {

            long UserId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Users._ID));
            String uname = cursor.getString(1);
            UUID globalId = UUID.fromString(cursor.getString(2));
            models.User user = new models.User(UserId, uname, globalId);
            users.add(user);
        }
        cursor.close();

        if (users.size() == 0) return null;
        if (users.size() > 1) return null;
        db.close();
        return users.get(0);
    }

    //endregion

    //region   S E E D T A G  /////////////////////////////////////////////

    public BaseSeedTag SaveSeedTag(BaseSeedTag baseSeedTag) {
        //Just need the ids for the Database

        if (baseSeedTag.get_ID() == 0) {

            Tag tag = baseSeedTag.getTag();
            Tag fetched = GetTagByDesc(tag.getTagText());
            if (fetched == null)
            {
                tag = AddTag(tag);
            }

            return AddSeedTag(baseSeedTag);
        } else {
            //need to add is deleted else section here
            return null;
        }
    }

    public BaseSeedTag AddSeedTag(BaseSeedTag BaseSeedTag) {
        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(BaseSeedTags.COLUMN_NAME_BASESEEDID, BaseSeedTag.getSeedID());
            values.put(BaseSeedTags.COLUMN_NAME_TAGID, BaseSeedTag.getTag().get_ID());

            long id = db.insertOrThrow(BaseSeedTags.TABLE_NAME, null, values);

            if (id < 1) return null;

            BaseSeedTag.set_ID(id);
            return BaseSeedTag;
        } finally {
            db.close();
        }
    }

//    public void UpdateSeedTag(models.BaseSeedTag seedTag) {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(SeedTags.COLUMN_NAME_SEEDID, seedTag.getSeedID().toString());
//        values.put(SeedTags.COLUMN_NAME_TAGID, seedTag.getTag().get_ID());
//
//        long success = db.update(ThoughtSeedTags.TABLE_NAME, values, ThoughtSeedTags._ID+"="+seedTag.get_ID(), null);
//
//        db.close();
//    }

    public ArrayList<BaseSeedTag> GetSeedTagsBySeedID(long seedID) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            //Create new querybuilder

            android.database.sqlite.SQLiteQueryBuilder queryBuilder = new
                    android.database.sqlite.SQLiteQueryBuilder();

//Specify books table and add join to categories table (use full_id for joining categories table)
            queryBuilder.setTables(BaseSeedTags.TABLE_NAME +
                    " INNER JOIN " + Tags.TABLE_NAME + " ON " +
                    BaseSeedTags.TABLE_NAME + "." + BaseSeedTags.COLUMN_NAME_TAGID + " = " + Tags.TABLE_NAME + "." + Tags._ID);

//Order by records by title
            //_OrderBy = BookColumns.BOOK_TITLE + " ASC";

            //projection
            String[] projection = {
                    BaseSeedTags.TABLE_NAME + "." + BaseSeedTags._ID,
                    BaseSeedTags.TABLE_NAME + "." + BaseSeedTags.COLUMN_NAME_BASESEEDID,
                    BaseSeedTags.TABLE_NAME + "." + BaseSeedTags.COLUMN_NAME_TAGID,
                    Tags.TABLE_NAME + "." + Tags.COLUMN_NAME_TAGTEXT
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = BaseSeedTags.COLUMN_NAME_BASESEEDID + " = ?";
            String[] selectionArgs = {Long.toString(seedID)};

            //Get cursor
            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, null);
            ArrayList<BaseSeedTag> seedtags = new ArrayList<BaseSeedTag>();
            while (cursor.moveToNext()) {

                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(BaseSeedTags._ID));
                long seedid = cursor.getLong(1);
                long tagid = cursor.getLong(2);
                String tagtext = cursor.getString(3);
                models.Tag tag = new models.Tag(tagid, null, tagtext);
                BaseSeedTag tsTag = new BaseSeedTag(id, seedid, tag);
                seedtags.add(tsTag);
            }
            cursor.close();

            return seedtags;
        } finally {
            db.close();
        }
    }

    //endregion

    //region S E E D  T Y P E S  ////////////////////////////////

    public models.SeedType AddSeedType(models.SeedType type) throws Exception
    {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
//        if (type.get_ID() == null)
//        {
//            type.set_ID(UUID.randomUUID());
//        }

            // New value for one column
            ContentValues values = new ContentValues();
            //values.put(SeedTypes._ID, type.get_ID());
            values.put(SeedTypes.COLUMN_NAME_SEEDTYPENAME, type.getSeedTypeName());
            values.put(SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION, type.getSeedIconFileLocation());

            long id = db.insertOrThrow(SeedTypes.TABLE_NAME, null, values);

            type.set_ID(id);
            return type;
        }
        finally
        {
            db.close();
        }
    }

    public models.SeedType GetSeedTypeBySeedTypeID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    SeedTypes._ID,
                    SeedTypes.COLUMN_NAME_SEEDTYPENAME,
                    SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION
            };

// Filter results WHERE "title" = 'My Title'
            String selection = SeedTypes._ID + " = ?";
            String[] selectionArgs = {Long.toString(id)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    SeedTypes._ID + " DESC";

            Cursor cursor = db.query(
                    SeedTypes.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.SeedType> types = new ArrayList<models.SeedType>();
            while (cursor.moveToNext()) {

                long typeId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(SeedTypes._ID));
                String typename = cursor.getString(1);
                String iconfilelocation = cursor.getString(2);
                models.SeedType type = new models.SeedType(typeId, typename, iconfilelocation);
                types.add(type);
            }
            cursor.close();

            if (types.size() == 0) return null;
            if (types.size() > 1) return null;

            return types.get(0);
        }
        finally
        {
            db.close();
        }
    }

    public ArrayList<models.SeedType> GetAllSeedTypes() throws  Exception{
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    SeedTypes._ID,
                    SeedTypes.COLUMN_NAME_SEEDTYPENAME,
                    SeedTypes.COLUMN_NAME_SEEDICONFILELOCATION
            };

// Filter results WHERE "title" = 'My Title'
            //String selection = Tags._ID+ " = ?";
            //String[] selectionArgs = {Long.toString(id)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    SeedTypes._ID + " DESC";

            Cursor cursor = db.query(
                    SeedTypes.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null, //selection,                                // The columns for the WHERE clause
                    null, //selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.SeedType> types = new ArrayList<models.SeedType>();
            while (cursor.moveToNext()) {

                long seedTypeId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(SeedTypes._ID));
                String seedtypename = cursor.getString(1);
                String iconfilelocation = cursor.getString(2);
                models.SeedType type = new models.SeedType(seedTypeId, seedtypename, iconfilelocation);
                types.add(type);
            }
            cursor.close();

            return types;
        }
        finally
        {
            db.close();
        }
    }
    //endregion

    //region G A R D E N S   ////////////////////////////

    public models.Garden SaveGarden(models.Garden garden) {

        if (garden.get_ID() > 0) {
            UpdateGarden(garden);
        } else {
            garden = AddGarden(garden);
        }

        for (int i = 0; i < garden.getSeeds().size(); i++) {
            models.GardenSeed seed = garden.getSeeds().get(i);
            if (seed.isDirty()) {
                seed = SaveGardenSeed(seed);
            }
        }

        for (int i = 0; i < garden.getGardenTags().size(); i++) {
            models.GardenTag gtag = garden.getGardenTags().get(i);
            if (gtag.isDirty()) {
                gtag = SaveGardenTag(gtag);
            }
        }
        return garden;
    }

    private models.Garden AddGarden(models.Garden garden) {
        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(Gardens.COLUMN_NAME_GARDENNAME, garden.getGardenName());

            long id = db.insertOrThrow(Gardens.TABLE_NAME, null, values);

            if (id < 1) return null;

            garden.set_ID(id);

            return garden;
        } finally {
            db.close();
        }

    }

    private void UpdateGarden(models.Garden garden) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Gardens.COLUMN_NAME_GARDENNAME, garden.getGardenName());

        long success = db.update(Gardens.TABLE_NAME, values, Gardens._ID + "=" + garden.get_ID(), null);

        db.close();
    }

    public models.Garden GetGardenByID(long gardenID) throws Exception {

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    Gardens._ID,
                    Gardens.COLUMN_NAME_GARDENNAME
            };

// Filter results WHERE "title" = 'My Title'
            String selection = Gardens.TABLE_NAME + "." + Gardens._ID + " = ?";
            String[] selectionArgs = {Long.toString(gardenID)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Gardens.TABLE_NAME + "." + Gardens._ID + " DESC";

            Cursor cursor = db.query(
                    Gardens.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.Garden> gardens = new ArrayList<models.Garden>();
            while (cursor.moveToNext()) {

                long gID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Gardens._ID));
                String gName = cursor.getString(1);
                ArrayList<models.GardenSeed> seeds = GetGardenSeedsByGardenID(gID);
                ArrayList<models.GardenTag> tags = GetGardenTagsByGardenID(gID);
                models.Garden garden = new models.Garden(gardenID, gName, seeds, tags);
                gardens.add(garden);
            }
            cursor.close();

            if (gardens.size() == 0) return null;
            if (gardens.size() > 1) return null;


            return gardens.get(0);
        }
        finally
        {
            db.close();
        }
    }

    public List<Garden> GetAllGardens() throws Exception{

        SQLiteDatabase db = this.getReadableDatabase();

        try {


            List<models.Garden> gardens = new ArrayList<models.Garden>();



            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    Gardens._ID,
                    Gardens.COLUMN_NAME_GARDENNAME
            };


// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Gardens.TABLE_NAME + "." + Gardens._ID + " DESC";

            Cursor cursor = db.query(
                    Gardens.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null, //selection,                                // The columns for the WHERE clause
                    null, //selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            while (cursor.moveToNext()) {

                long gID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(ThoughtSeeds._ID));
                String gName = cursor.getString(1);
                ArrayList<models.GardenSeed> seeds = GetGardenSeedsByGardenID(gID);
                ArrayList<models.GardenTag> tags = GetGardenTagsByGardenID(gID);
                models.Garden garden = new models.Garden(gID, gName, seeds, tags);
                gardens.add(garden);
            }
            cursor.close();

            return gardens;
        }
        finally
        {
            db.close();
        }

    }

    //endregion

    //region T R E E S    //////////////////////////////////

    public models.Tree SaveTree(models.Tree tree) {

        if (tree.get_ID() > 0) {
            UpdateTree(tree);
        } else {
            tree = AddTree(tree);
        }

        for (int i = 0; i < tree.getTreeScenes().size(); i++) {
            models.TreeScene scene = tree.getTreeScenes().get(i);
            if (scene.isDirty()) {
                scene = SaveTreeScene(scene);
            }
        }

//        for (int i = 0; i < Tree.getTreeTags().size(); i++) {
//            models.TreeTag gtag = Tree.getTreeTags().get(i);
//            if (gtag.isDirty()) {
//                gtag = SaveTreeTag(gtag);
//            }
//        }
        //return tree;
        return tree;
    }

    private models.Tree AddTree(models.Tree tree) {
        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(Trees.COLUMN_NAME_TREENAME, tree.getTreeName());

            long id = db.insertOrThrow(Trees.TABLE_NAME, null, values);

            if (id < 1) return null;

            tree.set_ID(id);

            return tree;
        } finally {
            db.close();
        }

    }

    private void UpdateTree(models.Tree tree) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            // New value for one column
            ContentValues values = new ContentValues();
            values.put(Trees.COLUMN_NAME_TREENAME, tree.getTreeName());

            String where = Trees._ID + "=?";
            String[] whereArgs = new String[] {String.valueOf(tree.get_ID())};

            long success = db.update(Trees.TABLE_NAME, values,  where, whereArgs);
        }
        finally {
            db.close();
        }
    }
    
    public models.Tree GetTreeByID(long treeid) throws Exception
    {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    Trees._ID,
                    Trees.COLUMN_NAME_TREENAME
            };

// Filter results WHERE "title" = 'My Title'
            String selection = Trees.TABLE_NAME + "." + Trees._ID + " = ?";
            String[] selectionArgs = {Long.toString(treeid)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Trees.TABLE_NAME + "." + Trees._ID + " DESC";

            Cursor cursor = db.query(
                    Trees.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.Tree> trees = new ArrayList<models.Tree>();
            while (cursor.moveToNext()) {

                long tID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Trees._ID));
                String tName = cursor.getString(1);
                ArrayList<models.TreeScene> scenes = GetTreeScenesByTreeID(tID);
                models.Tree tree = new models.Tree(tID, tName, scenes);
                trees.add(tree);
            }
            cursor.close();

            if (trees.size() == 0) return null;
            if (trees.size() > 1) return null;


            return trees.get(0);
        }
        finally
        {
            db.close();
        }
    }

    public ArrayList<models.Tree> GetAllTrees() throws Exception
    {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    Trees._ID,
                    Trees.COLUMN_NAME_TREENAME
            };

// Filter results WHERE "title" = 'My Title'
            //String selection = Trees.TABLE_NAME + "." + Trees._ID + " = ?";
            //String[] selectionArgs = {Long.toString(treeid)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Trees.TABLE_NAME + "." + Trees._ID + " DESC";

            Cursor cursor = db.query(
                    Trees.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,//selection,                                // The columns for the WHERE clause
                    null,//selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.Tree> trees = new ArrayList<models.Tree>();
            while (cursor.moveToNext()) {

                long tID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Trees._ID));
                String tName = cursor.getString(1);
                ArrayList<models.TreeScene> scenes = GetTreeScenesByTreeID(tID);
                //ArrayList<models.GardenTag> tags = GetGardenTagsByGardenID(gID);
                models.Tree tree = new models.Tree(tID, tName, scenes);
                trees.add(tree);
            }
            cursor.close();

            return trees;
        }
        finally
        {
            db.close();
        }
    }

    private models.TreeScene SaveTreeScene(models.TreeScene treescene)
    {
        
        if (treescene.get_ID() > 0 && treescene.isDeleted()) {
            DeleteTreeScene(treescene);
        }
        else if(treescene.get_ID() > 0 && treescene.isDirty())
        {
            UpdateTreeScene(treescene);
        }
        else {
            treescene = InsertTreeScene(treescene);
        }

        return treescene;
    }

    private models.TreeScene InsertTreeScene(models.TreeScene treescene) {

        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(TreeScenes.COLUMN_NAME_TREEID, treescene.getTreeID());
            if (treescene.getImageSeed() != null)
            {
                values.put(TreeScenes.COLUMN_NAME_IMAGESEEDID, treescene.getImageSeed().get_ID());
            }
            if (treescene.getThoughtSeed() != null) {
                values.put(TreeScenes.COLUMN_NAME_THOUGHTSEEDID, treescene.getThoughtSeed().get_ID());
            }
            values.put(TreeScenes.COLUMN_NAME_SCENEDURATION, treescene.getSceneDuration());
            values.put(TreeScenes.COLUMN_NAME_TEXTSPEED, treescene.getTextSpeed());
            values.put(TreeScenes.COLUMN_NAME_SCENEORDERNUMBER, treescene.getSceneOrderNumber());

            long id = db.insertOrThrow(TreeScenes.TABLE_NAME, null, values);

            if (id < 1) return null;

            treescene.set_ID(id);

            return treescene;
        } finally {
            db.close();
        }
    }

    public void UpdateTreeScene(models.TreeScene treescene) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            // New value for one column
            ContentValues values = new ContentValues();
            if (treescene.getImageSeed() != null) {
                values.put(TreeScenes.COLUMN_NAME_IMAGESEEDID, treescene.getImageSeed().get_ID());
            }
            if (treescene.getThoughtSeed() != null) {
                values.put(TreeScenes.COLUMN_NAME_THOUGHTSEEDID, treescene.getThoughtSeed().get_ID());
            }
            values.put(TreeScenes.COLUMN_NAME_SCENEDURATION, treescene.getSceneDuration());
            values.put(TreeScenes.COLUMN_NAME_SCENEORDERNUMBER, treescene.getSceneOrderNumber());

            String where = TreeScenes._ID + "=?";
            String[] whereArgs = new String[] {String.valueOf(treescene.get_ID())};

            long success = db.update(TreeScenes.TABLE_NAME, values,  where, whereArgs);
        }
        finally {
            db.close();
        }
    }
    private void DeleteTreeScene(models.TreeScene treescene) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(Gardens.COLUMN_NAME_GARDENNAME, gardenseed.getGardenName());

        ContentValues values = new ContentValues();
        String where = TreeScenes._ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(treescene.get_ID())};

        long success = db.delete(TreeScenes.TABLE_NAME, where, whereArgs);

        db.close();
    }

    public ArrayList<models.TreeScene> GetTreeScenesByTreeID(long treeid)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    TreeScenes._ID,
                    TreeScenes.COLUMN_NAME_TREEID,
                    TreeScenes.COLUMN_NAME_IMAGESEEDID,
                    TreeScenes.COLUMN_NAME_THOUGHTSEEDID,
                    TreeScenes.COLUMN_NAME_SCENEDURATION,
                    TreeScenes.COLUMN_NAME_SCENEORDERNUMBER
            };

// Filter results WHERE "title" = 'My Title'
            String selection = TreeScenes.TABLE_NAME + "." + TreeScenes.COLUMN_NAME_TREEID + " = ?";
            String[] selectionArgs = {Long.toString(treeid)};

// Important as the scenes need to be in order
            String sortOrder =
                    TreeScenes.TABLE_NAME + "." + TreeScenes.COLUMN_NAME_SCENEORDERNUMBER + " ASC";

            Cursor cursor = db.query(
                    TreeScenes.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.TreeScene> treescenes = new ArrayList<models.TreeScene>();
            while (cursor.moveToNext()) {

                long tID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Trees._ID));
                long treeID = cursor.getLong(1);
                long imageID = cursor.getLong(2);
                long thoughtID = cursor.getLong(3);
                int duration = cursor.getInt(4);
                //String textspeed = cursor.getString(5);
                int sceneordernumber = cursor.getInt(5);
                ImageSeed iSeed = GetImageSeedByID(imageID);
                ThoughtSeed tSeed = GetThoughtSeedByID(thoughtID);
                //ArrayList<models.GardenTag> tags = GetGardenTagsByGardenID(gID);
                models.TreeScene treescene = new models.TreeScene(tID, treeID, iSeed, tSeed, duration, sceneordernumber);
                treescenes.add(treescene);
            }
            cursor.close();

            return treescenes;
        }
        finally
        {
            db.close();
        }
    }

    public models.TreeScene GetTreeSceneByTreeSceneID(long treesceneid)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    TreeScenes._ID,
                    TreeScenes.COLUMN_NAME_TREEID,
                    TreeScenes.COLUMN_NAME_IMAGESEEDID,
                    TreeScenes.COLUMN_NAME_THOUGHTSEEDID,
                    TreeScenes.COLUMN_NAME_SCENEDURATION,
                    TreeScenes.COLUMN_NAME_SCENEORDERNUMBER
            };

// Filter results WHERE "title" = 'My Title'
            String selection = TreeScenes.TABLE_NAME + "." + TreeScenes._ID + " = ?";
            String[] selectionArgs = {Long.toString(treesceneid)};

// Important as the scenes need to be in order
            String sortOrder =
                    TreeScenes.TABLE_NAME + "." + TreeScenes.COLUMN_NAME_SCENEORDERNUMBER + " ASC";

            Cursor cursor = db.query(
                    TreeScenes.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.TreeScene> treescenes = new ArrayList<models.TreeScene>();
            while (cursor.moveToNext()) {

                long tID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(Trees._ID));
                long treeID = cursor.getLong(1);
                long imageID = cursor.getLong(2);
                long thoughtID = cursor.getLong(3);
                int duration = cursor.getInt(4);
                //String textspeed = cursor.getString(5);
                int sceneordernumber = cursor.getInt(5);
                ImageSeed iSeed = GetImageSeedByID(imageID);
                ThoughtSeed tSeed = GetThoughtSeedByID(thoughtID);
                //ArrayList<models.GardenTag> tags = GetGardenTagsByGardenID(gID);
                models.TreeScene treescene = new models.TreeScene(tID, treeID, iSeed, tSeed, duration, sceneordernumber);
                treescenes.add(treescene);
            }
            cursor.close();

            return treescenes.get(0);
        }
        finally
        {
            db.close();
        }
    }
    //endregion

    //region G A R D E N  S E E D S   ////////

    private ArrayList<models.GardenSeed> GetGardenSeedsByGardenID(long gardenID) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
//            String[] projection = {
//                    GardenSeeds._ID,
//                    GardenSeeds.COLUMN_NAME_GARDENID,
//                    GardenSeeds.COLUMN_NAME_SEEDTYPEID,
//                    GardenSeeds.COLUMN_NAME_SEEDID
//            };
//
//            // Filter results WHERE "title" = 'My Title'
//            String selection = GardenSeeds.COLUMN_NAME_GARDENID + " = ?";
//            String[] selectionArgs = {Long.toString(gardenID)};
//
//            // How you want the results sorted in the resulting Cursor
//            String sortOrder =
//                    GardenSeeds._ID + " DESC";
//
//            Cursor cursor = db.query(
//                    GardenSeeds.TABLE_NAME,                     // The table to query
//                    projection,                               // The columns to return
//                    selection,                                // The columns for the WHERE clause
//                    selectionArgs,                            // The values for the WHERE clause
//                    null,                                     // don't group the rows
//                    null,                                     // don't filter by row groups
//                    sortOrder                                 // The sort order
//            );
            Cursor cursor = db.rawQuery(
            "Select g._id, g.gardenid, g.seedid, g.seedtypeid, s.seedtypename, s.seediconfilelocation " +
                    "From gardenseeds g "+
                    "INNER JOIN seedtypes s " +
                    "on g.seedtypeid = s._id " +
                    "WHERE g.gardenid = ?"
                    ,
                    new String[] {gardenID + ""}
            );
            ArrayList<models.GardenSeed> gseeds = new ArrayList<models.GardenSeed>();
            while (cursor.moveToNext()) {

                long gardenSeedID = cursor.getLong(
                        cursor.getColumnIndexOrThrow("g._id"));
                long gID = cursor.getLong(1);
                long seedID = cursor.getLong(2);
                long seedtypeID = cursor.getLong(3);
                String seedName = cursor.getString(4);
                String seedicon = cursor.getString(5);
                models.SeedType seedType=new models.SeedType(seedtypeID, seedName, seedicon);

                models.GardenSeed gSeed = new models.GardenSeed(gardenSeedID, gID, seedType, seedID);
                gseeds.add(gSeed);
            }
            cursor.close();

            return gseeds;
        }
        finally
        {
            db.close();
        }
    }

    private models.GardenSeed SaveGardenSeed(models.GardenSeed gardenseed) {
        if (gardenseed.get_ID() > 0 && gardenseed.isDeleted()) {
            DeleteGardenSeed(gardenseed);
        } else {
            gardenseed = InsertGardenSeed(gardenseed);
        }

        return gardenseed;
    }

    private models.GardenSeed InsertGardenSeed(models.GardenSeed gardenseed) {

        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(GardenSeeds.COLUMN_NAME_GARDENID, gardenseed.getGardenID());
            values.put(GardenSeeds.COLUMN_NAME_SEEDTYPEID, gardenseed.getSeedType().get_ID());
            values.put(GardenSeeds.COLUMN_NAME_SEEDID, gardenseed.getSeedID());

            long id = db.insertOrThrow(GardenSeeds.TABLE_NAME, null, values);

            if (id < 1) return null;

            gardenseed.set_ID(id);

            return gardenseed;
        } finally {
            db.close();
        }
    }

    private models.GardenTag SaveGardenTag(models.GardenTag gardentag) {
        if (gardentag.get_ID() > 0 && gardentag.isDeleted()) {
            DeleteGardenTag(gardentag);
        } else {
            gardentag = InsertGardenTag(gardentag);
        }

        return gardentag;
    }

    private models.GardenTag InsertGardenTag(models.GardenTag gardentag) {

        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(GardenTags.COLUMN_NAME_GARDENID, gardentag.getGardenID());
            values.put(GardenTags.COLUMN_NAME_TAGID, gardentag.getTagID());

            long id = db.insertOrThrow(GardenTags.TABLE_NAME, null, values);

            if (id < 1) return null;

            gardentag.set_ID(id);

            return gardentag;
        } finally {
            db.close();
        }
    }

    private ArrayList<models.GardenTag> GetGardenTagsByGardenID(long gardenID) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    GardenTags._ID,
                    GardenTags.COLUMN_NAME_GARDENID,
                    GardenTags.COLUMN_NAME_TAGID
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = GardenTags.COLUMN_NAME_GARDENID + " = ?";
            String[] selectionArgs = {Long.toString(gardenID)};

            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    GardenTags._ID + " DESC";

            Cursor cursor = db.query(
                    GardenTags.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.GardenTag> gtags = new ArrayList<models.GardenTag>();
            while (cursor.moveToNext()) {

                long gardenTagID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(GardenTags._ID));
                long gID = cursor.getLong(1);
                long tagID = cursor.getLong(2);
                models.GardenTag gTag = new models.GardenTag(gardenTagID, gID, tagID);
                gtags.add(gTag);
            }
            cursor.close();

            return gtags;
        }
        finally
        {
            db.close();
        }
    }

    private void DeleteGardenSeed(models.GardenSeed gardenseed) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(Gardens.COLUMN_NAME_GARDENNAME, gardenseed.getGardenName());

        long success = db.delete(GardenSeeds.TABLE_NAME, GardenSeeds._ID + "=" + gardenseed.get_ID(), null);

        db.close();
    }

    private void DeleteGardenTag(models.GardenTag gardentag) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(Gardens.COLUMN_NAME_GARDENNAME, gardenseed.getGardenName());

        long success = db.delete(GardenTags.TABLE_NAME, GardenTags._ID + "=" + gardentag.get_ID(), null);

        db.close();
    }
    // Will need to convert to an Interface
//    private ArrayList<models.GardenThoughtSeed> GetGardenSeedsByGardenID(long gardenID)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        //NOW PERFORM READ
//        //database.addExpenseType(new ExpenseType("Food"));
//        String[] projection = {
//                GardenThoughtSeeds._ID,
//                GardenThoughtSeeds.COLUMN_NAME_GARDENID,
//                GardenThoughtSeeds.COLUMN_NAME_THOUGHTSEEDID
//        };
//
//        // Filter results WHERE "title" = 'My Title'
//        String selection = GardenThoughtSeeds.COLUMN_NAME_GARDENID+ " = ?";
//        String[] selectionArgs = {Long.toString(gardenID)};
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                GardenThoughtSeeds._ID + " DESC";
//
//        Cursor cursor = db.query(
//                GardenThoughtSeeds.TABLE_NAME,                     // The table to query
//                projection,                               // The columns to return
//                null, //selection,                                // The columns for the WHERE clause
//                null, //selectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder                                 // The sort order
//        );
//
//        ArrayList<models.GardenThoughtSeed> gseeds = new ArrayList<GardenThoughtSeed>();
//        while (cursor.moveToNext()) {
//
//            long gardenSeedID = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(ThoughtSeeds._ID));
//            long gID = cursor.getLong(1);
//            long tID = cursor.getLong(2);
//            models.ThoughtSeed seed = GetThoughtSeedByID(tID);
//            models.GardenThoughtSeed gSeed = new models.GardenThoughtSeed(gardenSeedID, gID, seed);
//            gseeds.add(gSeed);
//        }
//        cursor.close();
//
//        db.close();
//        return gseeds;
//    }

//    private models.GardenThoughtSeed SaveGardenThoughtSeed(GardenThoughtSeed gardenseed) {
//        if (gardenseed.get_ID() > 0 && gardenseed.isDeleted()) {
//            DeleteGardenThoughtSeed(gardenseed);
//        } else {
//            gardenseed = InsertGardenThoughtSeed(gardenseed);
//        }
//
//        return gardenseed;
//    }

//    private models.GardenThoughtSeed InsertGardenThoughtSeed(GardenThoughtSeed gardenseed) {
//
//        //Just need the ids for the Database
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(GardenThoughtSeeds.COLUMN_NAME_GARDENID, gardenseed.getGardenID());
//        values.put(GardenThoughtSeeds.COLUMN_NAME_THOUGHTSEEDID, gardenseed.getThoughtSeed().get_ID());
//
//        long id = db.insertOrThrow(GardenThoughtSeeds.TABLE_NAME, null, values);
//
//        if (id < 1) return null;
//
//        gardenseed.set_ID(id);
//        db.close();
//        return gardenseed;
//    }

    //We Presume that will will never updated a garden seed,  we will only delete it.
    //Perhaps in future we may choose to reposition it within a garden
//    private void DeleteGardenThoughtSeed(GardenThoughtSeed gardenseed) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // New value for one column
////        ContentValues values = new ContentValues();
////        values.put(Gardens.COLUMN_NAME_GARDENNAME, gardenseed.getGardenName());
//
//        long success = db.delete(GardenThoughtSeeds.TABLE_NAME, GardenThoughtSeeds._ID + "=" + gardenseed.get_ID(), null);
//
//        db.close();
//    }

    //endregion

    //region M Y  S C E N E  R E Q U E S T ////
//    public models.MySceneRequest SaveSceneRequest(models.MySceneRequest request) throws Exception {
//
//        if (request.get_ID() > 0)
//        {
//            AddMySceneRequest(request);
//
//        }
//
//    }

    private  models.MySceneRequest AddMySceneRequest(models.MySceneRequest request) throws Exception {


        SQLiteDatabase db = this.getReadableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(MySceneRequests._ID, request.get_ID());
            values.put(MySceneRequests.COLUMN_NAME_GLOBALID, request.getGlobalID().toString());
            values.put(MySceneRequests.COLUMN_NAME_TREEID, request.getTreeID());
            values.put(MySceneRequests.COLUMN_NAME_USERID, request.getUserID());
            values.put(MySceneRequests.COLUMN_NAME_IMAGEDESCRIPTION, request.getImageDescription());
            values.put(MySceneRequests.COLUMN_NAME_INKDESCRIPTION, request.getInkDescription());
            values.put(MySceneRequests.COLUMN_NAME_UPLOADED, request.isUploaded());

            long idnew = db.insertOrThrow(MySceneRequests.TABLE_NAME, null, values);
            request.set_ID(idnew);

            db.close();
            return request;

        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
            throw ex;
        }
        finally {
            db.close();
        }
    }
    //endregion

    //region E R R O R S  ////////////////////////

    public ExceptionError SaveExceptionError (ExceptionError error)
    {
        //Just need the ids for the Database
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            java.sql.Timestamp timestamp = new java.sql.Timestamp(error.getDateCreated().getTime());
            ContentValues values = new ContentValues();
            values.put(ExceptionErrors.COLUMN_NAME_ERROR, error.getError());
            values.put(ExceptionErrors.COLUMN_NAME_DATECREATED, timestamp.toString());
            values.put(ExceptionErrors.COLUMN_NAME_UPLOADED, false);

            long id = db.insertOrThrow(ExceptionErrors.TABLE_NAME, null, values);

            if (id < 1) return null;

            error.set_ID(id);

            return error;
        } finally {
            db.close();
        }
    }

    public ExceptionError GetExceptionErrorByID (long errorID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            //NOW PERFORM READ
            //database.addExpenseType(new ExpenseType("Food"));
            String[] projection = {
                    ExceptionErrors._ID,
                    ExceptionErrors.COLUMN_NAME_GLOBALID,
                    ExceptionErrors.COLUMN_NAME_ERROR,
                    ExceptionErrors.COLUMN_NAME_DATECREATED,
                    ExceptionErrors.COLUMN_NAME_UPLOADED
            };

// Filter results WHERE "title" = 'My Title'
            String selection = ExceptionErrors.TABLE_NAME + "." + ExceptionErrors._ID + " = ?";
            String[] selectionArgs = {Long.toString(errorID)};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    ExceptionErrors.TABLE_NAME + "." + ExceptionErrors._ID + " DESC";

            Cursor cursor = db.query(
                    ExceptionErrors.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<models.ExceptionError> errors = new ArrayList<models.ExceptionError>();
            while (cursor.moveToNext()) {

                long eID = cursor.getLong(
                        cursor.getColumnIndexOrThrow(ExceptionErrors._ID));
                String globalIDString = cursor.getString(1);
                UUID globalID = null;
                String errorText = cursor.getString(2);

                models.ExceptionError error = new models.ExceptionError(eID, globalID, errorText, null, false, null);
                errors.add(error);
            }
            cursor.close();

            if (errors.size() == 0) return null;
            if (errors.size() > 1) return null;


            return errors.get(0);
        }
        finally
        {
            db.close();
        }
    }



    //endregion


}
