package models;

/**
 * Created by Talanath on 4/25/2017.
 */

public class GardenThoughtSeed {

    private long _ID;
    private long GardenID;
    private models.ThoughtSeed ThoughtSeed;

    private boolean IsDirty;
    private boolean IsDeleted;

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public long getGardenID() {
        return GardenID;
    }

    public void setGardenID(long gardenID) {
        GardenID = gardenID;
        setDirty(true);
    }

    public models.ThoughtSeed getThoughtSeed() {
        return ThoughtSeed;
    }

    public void setThoughtSeed(models.ThoughtSeed thoughtSeed) {
        ThoughtSeed = thoughtSeed;
        setDirty(true);
    }

    //region D U P S
    public boolean isDirty() {
        return IsDirty;
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

    //endregion


    //region C O N S T R U C T O R S

    public GardenThoughtSeed(long _ID, long gardenID, models.ThoughtSeed thoughtSeed) {
        this._ID = _ID;
        this.GardenID = gardenID;
        this.ThoughtSeed = thoughtSeed;
    }

    public GardenThoughtSeed(long gardenID, models.ThoughtSeed thoughtSeed) {
        GardenID = gardenID;
        ThoughtSeed = thoughtSeed;
        setDirty(true);
    }

    //endregion
}
