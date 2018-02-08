package models;

/**
 * Created by Talanath on 5/27/2017.
 */
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.talanath.seededroid2.ImageSeedTakePhotoActivity;

import db.MySQLiteHelper;
import extensions.ApplicationOverride;
import utils.Constants;

public  class SeedFactory {

    private MySQLiteHelper database;
    private Context Context;

    private static SeedFactory instance = null;
    private static ArrayList<models.SeedType> seedTypes;

    protected SeedFactory() {
        // Exists only to defeat instantiation.
    }

    public static SeedFactory getInstance() {
        if(instance == null) {
            instance = new SeedFactory();
        }
        return instance;
    }

    public models.ThoughtSeed CreateThoughtSeed(String thought, Context context) throws java.lang.Exception
    {
        models.SeedType thoughtseedtype = null;
        this.Context = context;
        thoughtseedtype = GetSeedType(Constants.SEEDTYPE_THOUGHTSEED);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this.Context);
        String existing_token_id=pref.getString("token_id",null);
        Long tid = Long.parseLong(existing_token_id);
        User appUser = new User(tid);

        models.ThoughtSeed ts = new models.ThoughtSeed(thought, thoughtseedtype, appUser);
        return ts;
    }

    public models.ImageSeed CreateImageSeed(String imagename, String imagefilelocation, Context context) throws java.lang.Exception
    {
        models.SeedType imageSeedType;
        this.Context= context;
        imageSeedType = GetSeedType(Constants.SEEDTYPE_IMAGE);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this.Context);
        String existing_token_id=pref.getString("token_id",null);
        Long tid = Long.parseLong(existing_token_id);
        User appUser = new User(tid);

        models.ImageSeed is = new models.ImageSeed(imagename, imagefilelocation, imageSeedType, appUser);
        return is;
    }

    private models.SeedType GetSeedType(String seedstring) throws java.lang.Exception
    {
        ArrayList<SeedType> seedTypes = getSeedTypes();

        for (int i = 0; i < seedTypes.size(); i++) {
            models.SeedType seedType= seedTypes.get(i);
            if (seedType.getSeedTypeName().equalsIgnoreCase(seedstring)) return seedType;
        }
        throw new java.lang.Exception("Seed type not found");

    }

    public ArrayList<SeedType> getSeedTypes() throws Exception{

        if (seedTypes == null)
        {
            database = new MySQLiteHelper(Context);
            seedTypes = database.GetAllSeedTypes();
        }
        return seedTypes;
    }
}
