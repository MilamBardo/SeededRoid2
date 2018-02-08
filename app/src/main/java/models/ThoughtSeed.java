package models;

import java.util.ArrayList;
import java.util.UUID;
/**
 * Created by Talanath on 3/25/2017.
 */

public class ThoughtSeed extends BaseSeed implements interfaces.ISeed {

    //private long _ID;
    private User user;
    private String thought;



    private boolean IsDirty;

//    @Override
//    public void set_ID(long _ID) {
//        super._ID = _ID;
//
//    }

    public models.User getUser() {
        return user;
    }

    public void setUser(models.User user) {
        user = user;
        setDirty(true);
    }


    public String getThought() {
        return thought;
    }

    public void setThought(String t) {
        thought = t;
        setDirty(true);
    }

    public boolean isDirty() {
        return IsDirty;
    }

    public void setDirty(boolean dirty) {
        IsDirty = dirty;
    }

    //region C O N S T R U C T O R S   ////////////////////////
    public ThoughtSeed(long _id, String thought, models.SeedType seedtype, User createdBy) {
        super(_id, seedtype, createdBy);
        this.thought = thought;
        //tags = new ArrayList<models.BaseSeedTag>();
    }

    public ThoughtSeed(String thought, models.SeedType seedtype, User createdBy) {
        super(seedtype, createdBy);
        this.thought = thought;
        //tags = new ArrayList<models.BaseSeedTag>();
    }

    //endregion  //////////////////////////////////
}
