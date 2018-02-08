package com.example.talanath.seededroid2;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import models.GardenSeed;
import models.ImageSeed;
import models.ThoughtSeed;
import models.Tree;
import models.TreeScene;
import utils.Constants;

public class TreeActivity extends AppCompatActivity {

    models.Tree Tree;
    MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {

            //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
            setContentView(R.layout.activity_tree);

            setWidgetListeners();

            helper = new MySQLiteHelper(this);

            Intent intent = getIntent();
            String treeIdString = intent.getStringExtra(Intent.EXTRA_TEXT);

            if (treeIdString != null && (!treeIdString.trim().isEmpty())) {
                long treeId = Long.parseLong(treeIdString);
                Tree = helper.GetTreeByID(treeId);
                android.widget.EditText txtTreeName = (android.widget.EditText) findViewById(R.id.txtTreeName);
                txtTreeName.setText(Tree.getTreeName(), TextView.BufferType.EDITABLE);

                LinearLayout layout = (LinearLayout)findViewById(R.id.layoutScenes);
                for (int i = 0; i < Tree.getTreeScenes().size(); i++)
                {
                    layout.addView(CreateSceneLayout(Tree.getTreeScenes().get(i), i));
                }
//
            }
        }
        catch(Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    private void setWidgetListeners()
    {
        addListenerOnAddSceneButton();

        addListenerOnRequestSceneButton();

        addListenerOnPlayButton();

    }

    private LinearLayout CreateSceneLayout(TreeScene scene, int sceneNumber)
    {

        final TreeScene copyscene = scene;
//        MyTreeSceneLayout layout = new MyTreeSceneLayout(this, scene);
//        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        lParams.height = 300;
//        layout.setLayoutParams(lParams);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//        layout.setBackground(ContextCompat.getDrawable(this, R.drawable.border_custom));
//        layout.setPadding(2,2,2,2);

        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        cParams.height = 300;
        container.setLayoutParams(cParams);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackground(ContextCompat.getDrawable(this, R.drawable.border_custom));
        container.setPadding(2,2,2,2);
        //container.setContentDescription();

        LinearLayout layoutMove = new LinearLayout(this);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        mParams.weight = 1;
        layoutMove.setLayoutParams(mParams );
        layoutMove.setOrientation(LinearLayout.HORIZONTAL);
        layoutMove.setPadding(30,30,30,30);
        layoutMove.setOnTouchListener(new DragTouchListener(scene));
        ImageView imgDraggableView = new ImageView(this);
        imgDraggableView.setPadding(20,20,20,20);
        imgDraggableView.setBackground(ContextCompat.getDrawable(this, R.drawable.drag));
        LinearLayout.LayoutParams dParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imgDraggableView.setLayoutParams(dParams);
        layoutMove.addView(imgDraggableView);
        container.addView(layoutMove);



        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lParams.weight = 4;
        layout.setLayoutParams(lParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tree = SaveTree();
                        String s = Tree.getTreeName();
                        if (Tree != null) {
                            android.content.Intent intent = new android.content.Intent(TreeActivity.this, TreeAddSceneActivity.class);
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(Tree.get_ID()));
                            intent.putExtra(Constants.INTENT_EXTRA_TEXT_TREESCENEID, Long.toString(copyscene.get_ID()));
                            startActivity(intent);
                        }
                    }
                });

        ImageView imgSeedView = new ImageView(this);
        imgSeedView.setPadding(20,20,20,20);

        File imgFile = new File(scene.getImageSeed().getImagefilelocation());
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imgSeedView.setImageBitmap(myBitmap);
        }
        LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        iParams.weight = 2;
        imgSeedView.setLayoutParams(iParams);
        layout.addView(imgSeedView);

        TextView txtSeedView = new TextView(this);
        txtSeedView.setPadding(20,20,20,20);
        txtSeedView.setText(scene.getThoughtSeed().getThought());

        txtSeedView.setLayoutParams(iParams);

        layout.addView(txtSeedView);

        container.addView(layout);
        container.setContentDescription(Long.toString(Tree.get_ID()) + "-"+Long.toString(scene.get_ID()));

        LinearLayout notdraggedcontainer = new LinearLayout(this);
        LinearLayout.LayoutParams ndParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        notdraggedcontainer.setLayoutParams(ndParams);
        notdraggedcontainer.setOrientation(LinearLayout.HORIZONTAL);
        notdraggedcontainer.setOnDragListener(new ChoiceDragImageListener());

        notdraggedcontainer.addView(container);
        //imgSeedView.setLayoutParams(lParams);
        //txtSeedView.setLayoutParams(lParams);

        return notdraggedcontainer;
    }

//    public class MyTreeSceneLayout extends LinearLayout {
//
//        private Context Context;
//        private TreeScene Scene;
//
//        public MyTreeSceneLayout (Context context, TreeScene scene)
//        {
//            super(context);
//            Context = context;
//            Scene = scene;
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(MotionEvent ev) {
//            // do whatever you want with the event
//            // and return true so that children don't receive it
//
//            Tree = SaveTree();
//            String s = Tree.getTreeName();
//            if (Tree != null) {
//                android.content.Intent intent = new android.content.Intent(TreeActivity.this, TreeAddSceneActivity.class);
//                intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(Tree.get_ID()));
//                intent.putExtra(Constants.INTENT_EXTRA_TEXT_TREESCENEID, Long.toString(Scene.get_ID()));
//                startActivity(intent);
//            }
//
//            return true;
//        }
//    }

    private class DragTouchListener implements View.OnTouchListener {

        models.TreeScene TreeScene;
        public DragTouchListener(TreeScene treeScene)
        {
            TreeScene = treeScene;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {

            long treeId = TreeScene.getTreeID();
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //setup drag
                //ClipData data = ClipData.newPlainText(Constants.CLIPDATA_TREESCENEID, Long.toString(TreeScene.get_ID()));
                long treeid = TreeScene.getTreeID();
                long sceneid = TreeScene.get_ID();
                ClipData.Item item = new ClipData.Item((CharSequence)Long.toString(treeid));  // 1st item
                ClipData.Item item1 = new ClipData.Item((CharSequence)Long.toString(sceneid));     //2nd item and so on

                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN}; //Declare mime type according to your logic
                ClipData data = new ClipData("TreeData", mimeTypes, item);
                data.addItem(item1);


                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder((View)view.getParent());

                view.startDrag(data, shadowBuilder, view, 0);

                return true;
            }
            else {
                return false;
            }
        }
    }

    private class ChoiceDragImageListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //handle drag events


                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        //no action necessary
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //no action necessary
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        //no action necessary
                        break;
                    case DragEvent.ACTION_DROP:
                        //handle the dragged view being dropped over a drop view

                        //handle the dragged view being dropped over a target view
                        ClipData clipData = event.getClipData();
                        ClipData.Item item = clipData.getItemAt(0);
                        ClipData.Item item2 = clipData.getItemAt(1);

                        long treeid = Long.parseLong(item.getText().toString());
                        long treesceneid = Long.parseLong(item2.getText().toString());

                        //Constants.CLIPDATA_TREESCENEID)
                        View view = (View) event.getLocalState();
                        //String[] seedinfo = view.getContentDescription().toString().split("-");
                        //String seedtype = seedinfo[0];
                        //ong id = Long.parseLong(seedinfo[1].toString());

                        //view dragged item is being dropped on
                        LinearLayout dropTarget = (LinearLayout) v;
                        LinearLayout dropTargetChild = (LinearLayout) dropTarget.getChildAt(0);

                        //NEED TO MAKE THESE PARENT LAYOUTS
                        //view being dragged and dropped
                        LinearLayout dragImage = (LinearLayout) view;
                        LinearLayout dragTarget = (LinearLayout) dragImage.getParent();
                        LinearLayout dragTargetParent = (LinearLayout) dragTarget.getParent();

//                    dropTargetParent.removeAllViews();
//                    dropTargetParent.addView(dragTarget);

                        dropTarget.removeAllViews();
                        dragTargetParent.removeAllViews();

                        String dropdesc = dropTargetChild.getContentDescription().toString();
                        String[] dropdessplit = dropdesc.split("-");

                        //They are here, so parse and then fetch tree
                        long droptreeid = Long.parseLong(dropdessplit[0]);
                        long dropsceneid = Long.parseLong(dropdessplit[1]);
                        TreeScene dropscene = helper.GetTreeSceneByTreeSceneID(dropsceneid);
                        int dropordernumber = dropscene.getSceneOrderNumber();

                        String dragdesc = dragTarget.getContentDescription().toString();
                        String[] dragdessplit = dragdesc.split("-");
                        //drag to get same
                        long dragtreeid = Long.parseLong(dragdessplit[0]);
                        long dragsceneid = Long.parseLong(dragdessplit[1]);
                        TreeScene dragscene = helper.GetTreeSceneByTreeSceneID(dragsceneid);
                        int dragordernumber = dragscene.getSceneOrderNumber();

                        //Now change to the ton of wire order
                        dropscene.ChangeSceneOrderNumber(dragordernumber);
                        dragscene.ChangeSceneOrderNumber(dropordernumber);


                        //Update scenes
                        helper.UpdateTreeScene(dropscene);
                        helper.UpdateTreeScene(dragscene);

                        //drooooooooop then add view
                        dropTarget.addView(dragTarget);

                        dragTargetParent.addView(dropTargetChild);


                        //update the text in the target view to reflect the data being dropped

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        //no action necessary
                        break;
                    default:
                        break;
                }

                return true;

        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = Math.round(ev.getX());
//        int y = Math.round(ev.getY());
//        for (int i = 0; i < getChildCount(); i++) {
//            if (isEditText(x, y, getChildAt(i))) {
//                return true;
//            }
//        }
//
//        return true;
//    }
//
//    public boolean isEditText(int x, int y, View view) {
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                if (isEditText(x, y, ((ViewGroup) view).getChildAt(i))) {
//                    return true;
//                }
//            }
//        } else if (view instanceof ImageView) {
//            if (x > view.getLeft() && x < view.getRight() && y > view.getTop() && y < view.getBottom()) {
//                if (view.getContentDescription().equals(Constants.CONTENTDESCRIPTION_DRAGGABLE) ) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public void addListenerOnAddSceneButton() {
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.imgAddScene);

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0){


                Tree = SaveTree();
                String s = Tree.getTreeName();
                if (Tree != null) {
                    android.content.Intent intent = new android.content.Intent(TreeActivity.this, TreeAddSceneActivity.class);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(Tree.get_ID()));
                    startActivity(intent);
                }
            }
        });
    }

    public void addListenerOnRequestSceneButton() {
        android.widget.ImageButton imageButton2 = (android.widget.ImageButton) findViewById(R.id.imgRequestScene);

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0){


                Tree = SaveTree();
                String s = Tree.getTreeName();
                if (Tree != null) {
                    android.content.Intent intent = new android.content.Intent(TreeActivity.this, SceneRequestActivity.class);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(Tree.get_ID()));
                    startActivity(intent);
                }
            }
        });
    }

    public void addListenerOnPlayButton() {
        android.widget.ImageButton imgPlay = (android.widget.ImageButton) findViewById(R.id.imgPlay);
        imgPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0){


                Tree = SaveTree();
                if (Tree != null) {
                    android.content.Intent intent = new android.content.Intent(TreeActivity.this, TreePlayActivity.class);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(Tree.get_ID()));
                    startActivity(intent);
                }
            }
        });
    }

    private Tree SaveTree()
    {

        android.widget.EditText txtTreeName = (android.widget.EditText) findViewById(R.id.txtTreeName);
        String treename = txtTreeName.getText().toString();
        if (treename.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "You need to Name the Tree before you can add a scene", Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            if (Tree == null || Tree.get_ID() == 0) {
                Tree = new Tree(treename);
            }
            else
            {
                Tree.setTreeName(treename);
            }

            Tree = helper.SaveTree(Tree);

            if (Tree.get_ID() == 0) {
                throw new RuntimeException("Tree id is zero after save");
            }
            else
            {
                return Tree;
            }
        }
    }
}
