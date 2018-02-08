package models;

import java.util.UUID;

/**
 * Created by Talanath on 1/16/2018.
 */

public class MySceneRequest {

    private long _ID;

    private UUID GlobalID;
    private long TreeID;
    private long UserID;
    private String ImageDescription;
    private String InkDescription;

    private boolean Uploaded;
    private boolean UploadDate;

    public MySceneRequest(long treeID, long userID, String imageDescription, String inkDescription) {
        TreeID = treeID;
        UserID = userID;
        ImageDescription = imageDescription;
        InkDescription = inkDescription;
        Uploaded = false;
        GlobalID = UUID.randomUUID();
    }

    public MySceneRequest(long _ID, UUID globalID, long treeID, long userID, String imageDescription, String inkDescription, boolean uploaded, boolean uploadDate) {
        this._ID = _ID;
        GlobalID = globalID;
        TreeID = treeID;
        UserID = userID;
        ImageDescription = imageDescription;
        InkDescription = inkDescription;
        Uploaded = uploaded;
        UploadDate = uploadDate;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public UUID getGlobalID() {
        return GlobalID;
    }

    public void setGlobalID(UUID globalID) {
        GlobalID = globalID;
    }

    public long getTreeID() {
        return TreeID;
    }

    public void setTreeID(long treeID) {
        TreeID = treeID;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    public String getImageDescription() {
        return ImageDescription;
    }

    public void setImageDescription(String imageDescription) {
        ImageDescription = imageDescription;
    }

    public String getInkDescription() {
        return InkDescription;
    }

    public void setInkDescription(String inkDescription) {
        InkDescription = inkDescription;
    }

    public boolean isUploaded() {
        return Uploaded;
    }

    public void setUploaded(boolean uploaded) {
        Uploaded = uploaded;
    }

    public boolean isUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(boolean uploadDate) {
        UploadDate = uploadDate;
    }
}
