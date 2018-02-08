package models;

import java.util.UUID;

/**
 * Created by Talanath on 3/25/2017.
 */

public class User {

    private long _ID;
    private UUID globalID;
    private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;

    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public UUID getGlobalID() {
        return globalID;
    }

    public void setGlobalID(UUID globalID) {
        this.globalID = globalID;
    }

    //basic constructor for session work. Naughty.
    public User(long _ID)
    {
        this._ID = _ID;
    }

    public User(long _ID, String userName, UUID globalid) {
        this._ID = _ID;
        UserName = userName;
        globalID = globalid;
    }

    public User(String userName, UUID globalid) {
        UserName = userName;
        globalID = globalid;
    }
}
