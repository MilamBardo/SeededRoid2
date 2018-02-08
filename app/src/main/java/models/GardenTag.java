package models;

/**
 * Created by Talanath on 10/10/2017.
 */

public class GardenTag {

    private long _ID;
    private long GardenID;
    private long TagID;

    //We don't want to pull entire object back.
    //private long SeedID;

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

    public long getTagID() {
        return TagID;
    }

    public void setTagID(long tagID) {
        TagID = tagID;
        setDirty(true);
    }



    public boolean isDirty() {
        return IsDirty;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
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

    public GardenTag(long gardenID, long tagID) {
        this.GardenID = gardenID;
        this.TagID = tagID;
        setDirty(true);
    }

    public GardenTag(long _ID, long gardenID, long tagID) {
        this._ID = _ID;
        this.GardenID = gardenID;
        this.TagID = tagID;
    }

}
