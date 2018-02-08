package models;


import java.util.Date;
import java.util.UUID;

/**
 * Created by Talanath on 11/6/2017.
 */

public class ExceptionError {

    private long _ID;
    private UUID GlobalID;
    private String Error;
    private Date DateCreated;
    private boolean Uploaded;
    private Date UploadDate;

    public ExceptionError(String error)
    {
        Error = error;
        DateCreated = new Date();
        Uploaded = false;
    }

    public ExceptionError(long id, UUID globalid, String error, Date datecreated, boolean uploaded, Date uploaddate)
    {
        _ID = id;
        GlobalID = globalid;
        Error = error;
        DateCreated = datecreated;
        Uploaded = uploaded;
        UploadDate = uploaddate;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getError() {
        return Error;
    }

    public Date getDateCreated() {
        return DateCreated;
    }
}
