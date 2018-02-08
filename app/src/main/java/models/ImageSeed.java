package models;

import java.util.ArrayList;

import interfaces.ISeed;

/**
 * Created by Talanath on 5/17/2017.
 */

public class ImageSeed extends BaseSeed implements ISeed{



    public String imagename;
    public String imagefilelocation;

    private ArrayList<BaseSeedTag> tags;

    private boolean IsDirty;

    public String getImagename() {
        return imagename;
    }

//    public void setImagename(String imagename) {
//        this.imagename = imagename;
//    }

    public String getImagefilelocation() {
        return imagefilelocation;
    }

//    public void setImagefilelocation(String imagefilelocation) {
//        this.imagefilelocation = imagefilelocation;
//    }

    public boolean isDirty() {
        return IsDirty;
    }

    public void setDirty(boolean dirty) {
        IsDirty = dirty;
    }

//    public void addTag(models.Tag tag)
//    {
//        BaseSeedTag tsTag = new BaseSeedTag( this._ID, tag);
//        this.tags.add(tsTag);
//
//        setDirty(true);
//    }

    public ImageSeed(String imagename, String imagefilelocation, SeedType seedtype, User createdBy) {
        super(seedtype, createdBy);
        this.imagename = imagename;
        this.imagefilelocation = imagefilelocation;
    }

    public ImageSeed(long _ID, String imagename, String imagefilelocation, ArrayList<BaseSeedTag> tags, SeedType seedtype, User createdBy) {
        super(_ID, seedtype, createdBy);
        this.imagename = imagename;
        this.imagefilelocation = imagefilelocation;
        if (tags != null && tags.size() > 0) {
            this.tags = tags;
        }
    }
}
