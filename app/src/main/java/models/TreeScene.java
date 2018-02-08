package models;

/**
 * Created by Talanath on 11/5/2017.
 */

public class TreeScene {

    private long _ID;

    private long TreeID;

    /* Want to eventuall make this generic so that a whole host of seed types and
    media types can be permitted.  For instance, a video seed.  But this may not work this the
    scene structure we currently have.  We will need to see.
     */
    private ImageSeed ImageSeed;
    private ThoughtSeed ThoughtSeed;

    private int SceneDuration;
    private String TextSpeed;

    private int SceneOrderNumber;

    private boolean isrequest;

    private boolean dirty;
    private boolean deleted;


    public TreeScene(ImageSeed imageseed, ThoughtSeed thoughtseed, int sceneDuration)
    {
        ImageSeed = imageseed;
        ThoughtSeed = thoughtseed;
        SceneDuration = sceneDuration;
        //TextSpeed = textSpeed;
        dirty = true;
        deleted = false;
    }

    public TreeScene(long id, long treeID, ImageSeed imageseed, ThoughtSeed thoughtseed, int sceneDuration, int sceneOrderNumber)
    {
        _ID = id;
        TreeID = treeID;
        ImageSeed = imageseed;
        ThoughtSeed = thoughtseed;
        SceneDuration = sceneDuration;
        //TextSpeed = textSpeed;
        SceneOrderNumber = sceneOrderNumber;
        dirty = false;
        deleted=false;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public long getTreeID() {
        return TreeID;
    }

    protected void setTreeID(long treeID) {
        this.TreeID = treeID;
    }

    public models.ImageSeed getImageSeed() {
        return ImageSeed;
    }

    public models.ThoughtSeed getThoughtSeed() {
        return ThoughtSeed;
    }

    public int getSceneDuration() {
        return SceneDuration;
    }

    public String getTextSpeed() {
        return TextSpeed;
    }

    public int getSceneOrderNumber() {
        return SceneOrderNumber;
    }

    protected void UpdateTreeScene(TreeScene scene)
    {
        ImageSeed = scene.getImageSeed();
        ThoughtSeed = scene.getThoughtSeed();
        SceneDuration = scene.getSceneDuration();

        dirty = true;
    }

    public void ChangeSceneOrderNumber(int ordernumber)
    {
        SceneOrderNumber = ordernumber;
        dirty = true;
    }

    protected void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void delete()
    {
        deleted = true;
        dirty = true;
    }

    public boolean isDirty()
    {
        return dirty;
    }
    public boolean isDeleted()
    {
        return deleted;
    }


}
