package models;

import java.util.UUID;

/**
 * Created by Talanath on 5/17/2017.
 */

public class GardenSeed {

    private long _ID;
    private long GardenID;
    //private long SeedTypeID;
    //private String SeedTypeName;


    //We don't want to pull entire object back.
    private long SeedID;

    private models.SeedType SeedType;

    //private long SeedNew;

    private boolean IsDirty;
    private boolean IsDeleted;

    public long get_ID() {
        return _ID;
    }


    public long getGardenID() {
        return GardenID;
    }

    public void setGardenID(long gardenID) {
        this.GardenID = gardenID;
        setDirty(true);
    }

    //public long getSeedTypeID() {
      //  return SeedTypeID;
    //}


    public models.SeedType getSeedType() {
        return SeedType;
    }

    public boolean isDirty() {
        return IsDirty;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public long getSeedID() {
        return SeedID;
    }

    public void setDirty(boolean dirty) {
        IsDirty = dirty;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public GardenSeed(long _ID, long gardenID, SeedType seedType, long seedID) {
        this._ID = _ID;
        this.GardenID = gardenID;
        this.SeedType = seedType;
        this.SeedID = seedID;
    }

    public GardenSeed(long gardenID, models.SeedType seedType, long seedID) {
        GardenID = gardenID;
        this.SeedType = seedType;
        this.SeedID = seedID;
        setDirty(true);
    }
}
