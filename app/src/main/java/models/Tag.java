package models;

import java.util.UUID;

/**
 * Created by Talanath on 4/21/2017.
 */

public class Tag {

    private long _ID;
    private UUID GlobalTagID;
    private String TagText;

    public void setTagText(String tagText) {
        TagText = tagText;
    }

    public String getTagText() {

        return TagText;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public Tag(long _id, UUID globalTagID, String tagText) {
        this._ID = _id;
        this.GlobalTagID = globalTagID;
        TagText = tagText;
    }

    public Tag(String tagText) {
        TagText = tagText;
    }
}
