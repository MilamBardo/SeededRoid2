package models;

import java.util.UUID;

/**
 * Created by Talanath on 3/25/2017.
 */

public class ThoughtSeedTag {

    private long _ID;
    private UUID ThoughtSeedID;
    private models.Tag Tag;


    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public UUID getThoughtSeedID() {
        return ThoughtSeedID;
    }

    public void setThoughtSeedID(UUID thoughtSeedID) {
        ThoughtSeedID = thoughtSeedID;
    }

    public models.Tag getTag() {
        return Tag;
    }

    public void setTag(models.Tag tag) {
        Tag = tag;
    }

    public ThoughtSeedTag(long _ID, UUID thoughtSeedID, models.Tag tag) {
        this._ID = _ID;
        ThoughtSeedID = thoughtSeedID;
        Tag = tag;
    }

    public ThoughtSeedTag(UUID thoughtSeedID, models.Tag tag) {
        ThoughtSeedID = thoughtSeedID;
        Tag = tag;
    }
}
