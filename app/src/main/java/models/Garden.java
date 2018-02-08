package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Talanath on 3/25/2017.
 */

public class Garden {

    private long _ID;

    private String GardenName;
    private ArrayList<GardenSeed> Seeds;
    private ArrayList<GardenTag> GardenTags;

    private boolean dirty;

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;

        for (int i= 0; i < this.Seeds.size(); i++)
        {
            this.Seeds.get(i).setGardenID(_ID);
        }

        for (int i= 0; i < this.GardenTags.size(); i++)
        {
            this.GardenTags.get(i).setGardenID(_ID);
        }
    }

//    public void setGardenName(String gardenName) {
//        GardenName = gardenName;
//        dirty = true;
//    }

    public String getGardenName() {

        return GardenName;
    }

    public void addTag(models.Tag tag)
    {
        models.GardenTag gtag = new GardenTag(this.get_ID(), tag.get_ID());
        this.GardenTags.add(gtag);
        dirty=true;
    }

    public void addSeed(interfaces.ISeed seed)
    {
        GardenSeed gSeed = new GardenSeed(this._ID, seed.getSeedType(), seed.get_ID());
        this.Seeds.add(gSeed);
        dirty=true;
    }

    public List<GardenSeed> getSeeds() {
        return Seeds;
    }
    public List<GardenTag> getGardenTags() {
        return GardenTags;
    }

//    public void setSeeds(ArrayList<GardenSeed> seeds) {
//        Seeds = seeds;
//        setDirty(true);
//    }

    public Garden(long _ID, String gardenName, ArrayList<GardenSeed> seeds, ArrayList<GardenTag> tags) {
        this._ID = _ID;
        GardenName = gardenName;

        this.Seeds = seeds == null ?  new ArrayList<GardenSeed>() : seeds;
        this.GardenTags = tags == null ? new ArrayList<GardenTag>() : tags;
        dirty = false;
    }

    public Garden(String gardenName) {
        GardenName = gardenName;
        this.Seeds = new ArrayList<GardenSeed>();
        this.GardenTags = new ArrayList<GardenTag>();
        dirty = true;
    }

//    public Garden(String gardenName, String[] tags) {
//        GardenName = gardenName;
//        this.Seeds = new ArrayList<GardenSeed>();
//        this.GardenTags = new ArrayList<GardenTag>();
//        dirty = true;
//    }

    public boolean isDirty() {
        return dirty;
    }

}
