package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Talanath on 6/4/2017.
 */

public  class BaseSeed {

    protected long _ID;
    private UUID globalSeedID;
    private models.SeedType seedType;
    private ArrayList<BaseSeedTag> tags;
    private User createdBy;

    public BaseSeed( models.SeedType seedtype, User creator)
    {
        seedType = seedtype;
        createdBy = creator;
        tags = new ArrayList<BaseSeedTag>();
    }

    public BaseSeed(long _id, models.SeedType seedtype, User creator)
    {
        _ID = _id;
        seedType = seedtype;
        tags = new ArrayList<BaseSeedTag>();
        createdBy = creator;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;

        for (int i =0; i < this.tags.size(); i++)
        {
            this.tags.get(i).setSeedID(_ID);
        }
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public long get_ID() {
        return _ID;
    }

    public UUID get_globalSeedID() {
        return globalSeedID;
    }

    public void setglobalSeedID(UUID _id) {
        this.globalSeedID = _id;
        for (int i = 0; i < tags.size(); i++)
        {
            //tags.get(i).setSeedID(this.globalSeedID);
        }
    }

//    public void setglobalSeedID(UUID _id) {
//        this.globalSeedID = _id;
//        for (int i = 0; i < tags.size(); i++)
//        {
//            //tags.get(i).setSeedID(this.globalSeedID);
//        }
//    }

    //endregion


    public ArrayList<BaseSeedTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<BaseSeedTag> tstags) {
        tags = tstags;
    }

    public void AddSeedTag( models.Tag tag)
    {
        BaseSeedTag tsTag = new BaseSeedTag( this._ID, tag);
        this.tags.add(tsTag);

        //setDirty(true);
    }

    public SeedType getSeedType(){
        return this.seedType;
    }


}
