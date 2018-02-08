package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.SeedenContract;

/**
 * Created by Talanath on 11/5/2017.
 */

public class Tree {

    private long _ID;
    private String TreeName;
    private ArrayList<TreeScene> TreeScenes;
    //private ArrayList<models.Tags> Tagss;

    private boolean dirty;
    private boolean deleted;


    /*  M A P P E D  F R O M  D B  T R E E  */
    public Tree(long treeid, String treename, ArrayList<TreeScene> scenes)
    {
        _ID = treeid;
        TreeName = treename;
        TreeScenes = (scenes == null ) ? new ArrayList<TreeScene>() : scenes;
        dirty = false;
        deleted = true;
    }

    /* N E W  T R E E */
    public Tree(String treename)
    {
        TreeName = treename;
        TreeScenes = new ArrayList<TreeScene>();
        //TreeScenes = (scenes == null ) ? new ArrayList<TreeScene>() : scenes;
//        for (int i=0; i<TreeScenes.size(); i++)
//        {
//            TreeScene iteratedScene = TreeScenes.get(i);
//            iteratedScene.setTreeID(get_ID());
//        }
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _id) {
        this._ID = _id;

        for (int i = 0; i < TreeScenes.size(); i++)
        {
            TreeScenes.get(i).setTreeID(_ID);
        }
    }

    public String getTreeName() {
        return TreeName;
    }

    public void setTreeName(String treeName) {
        TreeName = treeName;
    }

    public List<TreeScene> getTreeScenes() {

        return Collections.unmodifiableList(TreeScenes);
    }

    public void addTreeScene(TreeScene scene)
    {
        if (scene.get_ID() != 0)
        {
            for (int i=0; i<TreeScenes.size(); i++)
            {
                TreeScene iteratedScene = TreeScenes.get(i);
                if (iteratedScene.get_ID() == scene.get_ID())
                {
                    iteratedScene.delete();
                }
            }
        }

        int maxordernumber = 0;
        for (int i=0; i<TreeScenes.size(); i++)
        {
            TreeScene iteratedScene = TreeScenes.get(i);
            if (iteratedScene.getSceneOrderNumber() > maxordernumber)
            {
                maxordernumber = iteratedScene.getSceneOrderNumber();
            }
        }
        maxordernumber++;
        scene.ChangeSceneOrderNumber(maxordernumber);
        scene.setTreeID(get_ID());
        scene.setDirty(true);
        TreeScenes.add(scene);
    }

    public void UpdateTreeScene(TreeScene scene)
    {
        TreeScene toUpdate = null;
        for (int i=0; i<TreeScenes.size(); i++)
        {
            TreeScene iteratedScene = TreeScenes.get(i);
            if (iteratedScene.get_ID() == scene.get_ID())
            {
                toUpdate = iteratedScene;
            }
        }
        if(toUpdate != null)
        {
            toUpdate.UpdateTreeScene(scene);
        }
    }

    public TreeScene GetTreeSceneByID(long treesceneid)
    {
        TreeScene foundscene = null;
        for (int i=0; i < TreeScenes.size(); i++)
        {
            TreeScene s = TreeScenes.get(i);
            if (s.get_ID() == treesceneid)
            {
                foundscene = s;
                break;
            }
        }
        return foundscene;
    }
    public void delete()
    {
        deleted = true;
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
