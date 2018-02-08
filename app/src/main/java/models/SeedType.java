package models;



import java.util.UUID;

/**
 * Created by Talanath on 5/17/2017.
 */

public class SeedType {

    private long _ID;
    private UUID _GLOBALID;
    private String SeedTypeName;
    private String SeedIconFileLocation;

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getSeedTypeName() {
        return SeedTypeName;
    }

    public void setSeedTypeName(String seedTypeName) {
        SeedTypeName = seedTypeName;
    }

    public String getSeedIconFileLocation() {
        return SeedIconFileLocation;
    }

    public void setSeedIconFileLocation(String seedIconFileLocation) {
        SeedIconFileLocation = seedIconFileLocation;
    }

    public SeedType(String seedTypeName, String seedIconFileLocation) {
        SeedTypeName = seedTypeName;
        SeedIconFileLocation = seedIconFileLocation;
    }

    public SeedType(long _ID, String seedTypeName, String seedIconFileLocation) {
        this._ID = _ID;
        SeedTypeName = seedTypeName;
        SeedIconFileLocation = seedIconFileLocation;
    }

}
