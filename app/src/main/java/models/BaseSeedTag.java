package models;

import java.util.UUID;

/**
 * Created by Talanath on 5/17/2017.
 */

public class BaseSeedTag {

    private long _ID;
    private long SeedID;

    private Tag Tag;

    public long get_ID() {
        return _ID;
    }

    public long getSeedID() {
        return SeedID;
    }

    public models.Tag getTag() {
        return Tag;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public BaseSeedTag(long _ID, long seedID, models.Tag tag) {
        this._ID = _ID;
        SeedID = seedID;
        Tag = tag;
    }

    public void setSeedID(long seedID) {
      SeedID = seedID;
    }

    public BaseSeedTag(long seedID, models.Tag tag) {
        SeedID = seedID;
        Tag = tag;
    }
}
