package db;

/**
 * Created by Talanath on 4/1/2017.
 */

public final class SeedenContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SeedenContract () {}

    public static final String DB_FULL_PATH ="";

    /* Inner class that defines the table contents */
    public static class SeedTypes implements
            android.provider.BaseColumns{
        public static final String TABLE_NAME = "seedtypes";
        public static final String COLUMN_NAME_GLOBALSEEDTYPEID = "globalseedtypeid";
        public static final String COLUMN_NAME_SEEDTYPENAME = "seedtypename";
        public static final String COLUMN_NAME_SEEDICONFILELOCATION = "seediconfilelocation";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }
    public static class BaseSeeds implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "baseseeds";
        public static final String COLUMN_NAME_GLOBALSEEDID = "globalseedid";
        public static final String COLUMN_NAME_SEEDTYPEID = "seedtypeid";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
        public static final String COLUMN_NAME_CREATEDBY = "createdby";

    }
    public static class BaseSeedTags implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "seedtags";
        public static final String COLUMN_NAME_BASESEEDID = "baseseedid";
        public static final String COLUMN_NAME_TAGID = "tagid";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class ThoughtSeeds implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "thoughtseeds";
        public static final String COLUMN_NAME_GLOBALSEEDID = "globalseedid";
        public static final String COLUMN_NAME_SEEDTYPEID = "seedtypeid";
        public static final String COLUMN_NAME_THOUGHT = "thought";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class ImageSeeds implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "imageseeds";
        public static final String COLUMN_NAME_GLOBALSEEDID = "globalseedid";
        public static final String COLUMN_NAME_SEEDTYPEID = "seedtypeid";
        public static final String COLUMN_NAME_IMAGENAME = "imagename";
        public static final String COLUMN_NAME_IMAGEFILELOCATION = "imagefilelocation";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

//    public static class ThoughtSeedTags implements
//            android.provider.BaseColumns {
//        public static final String TABLE_NAME = "thoughtseedtags";
//        public static final String COLUMN_NAME_THOUGHTSEEDID = "thoughtseedid";
//        public static final String COLUMN_NAME_TAGID = "tagid";
//        public static final String COLUMN_NAME_DATECREATED = "datecreated";
//        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
//    }

//    public static class SeedTags implements
//            android.provider.BaseColumns {
//        public static final String TABLE_NAME = "seedtags";
//        public static final String COLUMN_NAME_SEEDID = "seedid";
//        public static final String COLUMN_NAME_TAGID = "tagid";
//        public static final String COLUMN_NAME_DATECREATED = "datecreated";
//        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
//    }

    public static class Gardens implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "gardens";
        public static final String COLUMN_NAME_GARDENNAME = "gardenname";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

//    public static class GardenThoughtSeeds implements
//            android.provider.BaseColumns {
//        public static final String TABLE_NAME = "gardenthoughtseeds";
//        public static final String COLUMN_NAME_GARDENID = "gardenid";
//        public static final String COLUMN_NAME_THOUGHTSEEDID = "thoughtseedid";
//        public static final String COLUMN_NAME_DATECREATED = "datecreated";
//        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
//    }

    public static class GardenSeeds implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "gardenseeds";
        public static final String COLUMN_NAME_GARDENID = "gardenid";
        public static final String COLUMN_NAME_SEEDTYPEID = "seedtypeid";
        public static final String COLUMN_NAME_SEEDID = "seedid";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class GardenTags implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "gardentags";
        public static final String COLUMN_NAME_GARDENID = "gardenid";
        public static final String COLUMN_NAME_TAGID = "tagid";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class Users implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_GLOBALID = "globalid";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class Tags implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "tags";
        public static final String COLUMN_NAME_GLOBALTAGID = "globaltagid";
        public static final String COLUMN_NAME_TAGTEXT = "tagtext";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class Trees implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "trees";
        public static final String COLUMN_NAME_TREENAME = "treename";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class TreeScenes implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "treescenes";
        public static final String COLUMN_NAME_TREEID = "treeid";
        public static final String COLUMN_NAME_IMAGESEEDID = "imageseedid";
        public static final String COLUMN_NAME_THOUGHTSEEDID = "thoughtseedid";
        public static final String COLUMN_NAME_SCENEDURATION = "duration";
        public static final String COLUMN_NAME_TEXTSPEED = "textspeed";
        public static final String COLUMN_NAME_SCENEORDERNUMBER = "sceneordernumber";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_DATEUPDATED = "dateupdated";
    }

    public static class ExceptionErrors implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "exceptionerrors";
        public static final String COLUMN_NAME_GLOBALID = "globalid";
        public static final String COLUMN_NAME_ERROR = "error";
        public static final String COLUMN_NAME_DATECREATED = "datecreated";
        public static final String COLUMN_NAME_UPLOADED= "uploaded";
        public static final String COLUMN_NAME_UPLOADDATE= "uploaddate";
    }

    public static class MySceneRequests implements
            android.provider.BaseColumns {
        public static final String TABLE_NAME = "requestscene";
        public static final String COLUMN_NAME_GLOBALID = "globalid";
        public static final String COLUMN_NAME_TREEID = "treeid";
        public static final String COLUMN_NAME_USERID = "userid";
        public static final String COLUMN_NAME_IMAGEDESCRIPTION = "imagedescription";
        public static final String COLUMN_NAME_INKDESCRIPTION = "inkdescription";
        public static final String COLUMN_NAME_UPLOADED = "uploaded";
        public static final String COLUMN_NAME_UPLOADDATE = "uploaddate";

    }




}
