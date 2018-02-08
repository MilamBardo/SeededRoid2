package com.example.talanath.seededroid2;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ExceptionHandler.ExceptionHandler;
import db.MySQLiteHelper;
import db.SeedenContract;
import interfaces.ISeed;
import models.Garden;
import models.GardenSeed;
import models.ImageSeed;
import models.SeedType;
import models.ThoughtSeed;
import models.Tree;
import models.TreeScene;
import utils.Constants;

public class TreeAddSceneActivity extends AppCompatActivity {

    MySQLiteHelper helper;
    Tree tree;
    TreeScene EditTreeScene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try
        {

            setContentView(R.layout.activity_tree_add_scene);

            helper = new MySQLiteHelper(this);

            Intent intent = getIntent();
            String treeIdString = intent.getStringExtra(Intent.EXTRA_TEXT);
            long treeId = Long.parseLong(treeIdString);
            tree = helper.GetTreeByID(treeId);

            String treeSceneIdString = intent.getStringExtra(Constants.INTENT_EXTRA_TEXT_TREESCENEID);
            if (treeSceneIdString != null && !treeSceneIdString.isEmpty())
            {
                //in edit scene mode, so populate values in form
                EditTreeScene = tree.GetTreeSceneByID(Long.parseLong(treeSceneIdString));

                //Set Seed boxes
                ImageSeed iSeed = EditTreeScene.getImageSeed();
                android.widget.ImageView imgDropImage = (android.widget.ImageView) findViewById(R.id.imgDropImage);
                File imgFile = new File(iSeed.getImagefilelocation());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgDropImage.setImageBitmap(myBitmap);
                    imgDropImage.setContentDescription(Constants.SEEDTYPE_IMAGE+"-"+iSeed.get_ID());
                }

                ThoughtSeed tSeed = EditTreeScene.getThoughtSeed();
                android.widget.TextView txtDropText = (android.widget.TextView) findViewById(R.id.txtDropText);
                txtDropText.setText(tSeed.getThought());
                txtDropText.setContentDescription(Constants.SEEDTYPE_THOUGHTSEED+"-"+tSeed.get_ID());

                //Set Duration

                android.widget.SeekBar seek = (android.widget.SeekBar) findViewById(R.id.seekTime);
                seek.setProgress(EditTreeScene.getSceneDuration());
                android.widget.TextView txtTimeValue = (android.widget.TextView) findViewById(R.id.txtTimeValue);
                txtTimeValue.setText(EditTreeScene.getSceneDuration() + "secs");


            }

            setTitle("Add Tree Scene: "+tree.getTreeName());
            SetWidgetListeners();

            SetGardenSpinner();

        }
        catch (Exception ex)
        {
            ExceptionHandler handler = new ExceptionHandler(this);
            handler.HandleExceptionGracefully(Thread.currentThread(), ex);
        }
    }

    private void SetWidgetListeners()
    {
        android.widget.ImageView imgDropImage = (android.widget.ImageView) findViewById(R.id.imgDropImage);
        imgDropImage.setOnDragListener(new ChoiceDragImageListener());

        android.widget.TextView txtDropText = (android.widget.TextView) findViewById(R.id.txtDropText);
        txtDropText.setOnDragListener(new ChoiceDragTextListener());

        android.widget.Button btnBack = (android.widget.Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        android.widget.Button btnSave = (android.widget.Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Build up Scene Object

                long treeid = 0;
                if (tree != null) treeid = tree.get_ID();

                android.widget.ImageView imgDropImage = (android.widget.ImageView) findViewById(R.id.imgDropImage);
                View view = (View)imgDropImage;
                CharSequence imgdesc = view.getContentDescription();
                if (imgdesc == null)
                {
                    Toast.makeText(getApplicationContext(), "You need to drag an image seed into the box.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] imgseedinfo = imgdesc.toString().split("-");
                //String seedtype = imgseedinfo[0];
                long imageid = Long.parseLong(imgseedinfo[1].toString());
                ImageSeed iSeed = helper.GetImageSeedByID(imageid);

                android.widget.TextView txtDropText = (android.widget.TextView) findViewById(R.id.txtDropText);
                View viewT = (View)txtDropText;
                CharSequence txtdesc = viewT.getContentDescription();
                ThoughtSeed tSeed = null;
                if (txtdesc != null)
                {
                    //don't care if thoughtseed is empty
                    String[] txtseedinfo = txtdesc.toString().split("-");
                    long txtid = Long.parseLong(txtseedinfo[1].toString());
                    tSeed = helper.GetThoughtSeedByID(txtid);
                }

                android.widget.SeekBar seekBar = (android.widget.SeekBar) findViewById(R.id.seekTime);
                int duration = seekBar.getProgress();

                //android.widget.RadioGroup radGroup = (android.widget.RadioGroup) findViewById(R.id.radGroup);
                //int checkedid = radGroup.getCheckedRadioButtonId();
                //android.widget.RadioButton radButton = (android.widget.RadioButton) findViewById(checkedid);
                //String textSpeed = radButton.getText().toString();

                TreeScene scene;
                if (EditTreeScene == null) {
                    List<TreeScene> scenes = tree.getTreeScenes();
                    //int lastSceneOrderNumber = scenes.get(scenes.size() - 1).getSceneOrderNumber();
                    //int sceneOrderNumber = lastSceneOrderNumber++;
                    scene = new TreeScene(iSeed, tSeed, duration);
                    tree.addTreeScene(scene);
                }
                else
                {
                    scene = new TreeScene(EditTreeScene.get_ID(), EditTreeScene.getTreeID(), iSeed, tSeed, duration, EditTreeScene.getSceneOrderNumber());
                    tree.UpdateTreeScene(scene);
                }

                tree = helper.SaveTree(tree);

                android.content.Intent intent = new android.content.Intent(TreeAddSceneActivity.this, TreeActivity.class);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, Long.toString(treeid));
                startActivity(intent);
            }
        });

        SetRunTimeListener();

        //SetTextSpeedListener();

    }

//    private TreeScene BuildTreeScene()
//    {
//
//    }

    private void SetRunTimeListener()
    {
        final android.widget.TextView txtTimeValue = (android.widget.TextView) findViewById(R.id.txtTimeValue);
        android.widget.SeekBar seekBar = (android.widget.SeekBar) findViewById(R.id.seekTime);
        txtTimeValue.setText(10 + "s");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressVal = 10;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
                txtTimeValue.setText(progressVal + "secs");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtTimeValue.setText(progressVal + "s");
            }
        });
    }

//    private void SetTextSpeedListener()
//    {
//        final android.widget.TextView txtTxtSpeedValue = (android.widget.TextView) findViewById(R.id.txtTextSpeedValue);
//        android.widget.SeekBar seekBar = (android.widget.SeekBar) findViewById(R.id.seekSpeed);
//        txtTxtSpeedValue.setText("Normal");
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            int progressVal = 1;
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progressVal = progress;
//                switch (progress)
//                {
//                    case (1):
//                        txtTxtSpeedValue.setText("Normal");
//                        break;
//                    case(0):
//                        txtTxtSpeedValue.setText("Slow");
//                        break;
//                    case(2):
//                        txtTxtSpeedValue.setText("Fats");
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//    }

    private void SetGardenSpinner() throws Exception
    {
        final List<Garden> gardens = helper.GetAllGardens();

        final android.widget.Spinner spinGardens = (android.widget.Spinner) findViewById(R.id.spinGardens);

        final SpinAdapter adapter = new SpinAdapter(this,
                android.R.layout.simple_spinner_item,
                gardens);
        spinGardens.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        spinGardens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Garden garden = adapter.getItem(position);

                GridView gridView = (GridView) findViewById(R.id.grdSeeds);
                final List<GardenSeed> gseeds = garden.getSeeds();
                gridView.setAdapter(new CustomGridSeedsAdapter(TreeAddSceneActivity.this, gseeds));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    //region  T O U C H  E V E N T S ////////////////////////

    private class ChoiceTouchListener implements View.OnTouchListener {

        GardenSeed GardenSeed;
        public ChoiceTouchListener(GardenSeed gardenSeed)
        {
            GardenSeed = gardenSeed;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {

            models.SeedType seedType = GardenSeed.getSeedType();
            long seedId = GardenSeed.getSeedID();

            final LinearLayout layout = (LinearLayout) findViewById(R.id.laySeedDisplay);
            layout.removeAllViews();




//                            iParams.width = 0;
//                            iParams.weight = 1;
            if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED))
            {



                LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                iParams.weight=4;

                final ThoughtSeed tSeed = helper.GetThoughtSeedByID(seedId);
                TextView txtSeedView = new TextView(TreeAddSceneActivity.this);
                txtSeedView.setPadding(20,20,20,20);
                txtSeedView.setText(tSeed.getThought());
                txtSeedView.setLayoutParams(iParams);
                layout.addView(txtSeedView);

                LinearLayout.LayoutParams nParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                nParams.weight=1;
                nParams.gravity = Gravity.RIGHT;
                ImageView iView = new ImageView(TreeAddSceneActivity.this);
                iView.setImageResource(R.drawable.bin);
                iView.setLayoutParams(nParams);

                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                helper.DeleteThoughtSeed(tSeed.get_ID());
                                try {
                                    SetGardenSpinner();
                                }
                                catch(Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                                layout.removeAllViews();


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                iView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure you wish to delete this?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

//                        finish();
//                        startActivity(getIntent());
                    }
                });
                layout.addView(iView);
            }
            else if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_IMAGE)) {

                LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                iParams.weight=4;
                final ImageSeed iSeed = helper.GetImageSeedByID(seedId);
                ImageView imgSeedView = new ImageView(TreeAddSceneActivity.this);
                imgSeedView.setPadding(20,20,20,20);
                //ViewGroup.LayoutParams iParams = new ViewGroup.LayoutParams(android.view.ViewGroup);
                //imgSeedView.setMaxHeight(75);
                File imgFile = new File(iSeed.getImagefilelocation());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    imgSeedView.setImageBitmap(myBitmap);
                }
                imgSeedView.setLayoutParams(iParams);
                layout.addView(imgSeedView);


                LinearLayout.LayoutParams nParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                nParams.weight=1;
                nParams.gravity = Gravity.RIGHT;
                ImageView iView = new ImageView(TreeAddSceneActivity.this);
                iView.setImageResource(R.drawable.bin);
                iView.setLayoutParams(nParams);

                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                helper.DeleteImageSeed(iSeed.get_ID());
                                try {
                                    SetGardenSpinner();
                                }
                                catch(Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                                layout.removeAllViews();


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                iView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure you wish to delete this?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
//                        finish();
//                        startActivity(getIntent());
                    }
                });
                layout.addView(iView);

            }

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //setup drag
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

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
                    View view = (View) event.getLocalState();
                    String[] seedinfo = view.getContentDescription().toString().split("-");
                    String seedtype = seedinfo[0];
                    long id = Long.parseLong(seedinfo[1].toString());

                    //view dragged item is being dropped on
                    ImageView dropTarget = (ImageView) v;

                    //view being dragged and dropped
                    ImageView dropped = (ImageView) view;

                    switch (seedtype) {
                        case Constants.SEEDTYPE_IMAGE:

                            ImageSeed iSeed = helper.GetImageSeedByID(id);
                            File imgFile = new File(iSeed.getImagefilelocation());
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                dropTarget.setImageBitmap(myBitmap);
                                dropTarget.setContentDescription(Constants.SEEDTYPE_IMAGE+"-"+id);


                            }

                            break;
                        case Constants.SEEDTYPE_THOUGHTSEED:

                            break;

                        default:
                            break;
                    }

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

    private class ChoiceDragTextListener implements View.OnDragListener {
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
                    View view = (View) event.getLocalState();
                    String[] seedinfo = view.getContentDescription().toString().split("-");
                    String seedtype = seedinfo[0];
                    long id = Long.parseLong(seedinfo[1].toString());

                    //view dragged item is being dropped on
                    TextView dropTarget = (TextView) v;

                    //view being dragged and dropped
                    ImageView dropped = (ImageView) view;

                    switch (seedtype) {
                        case Constants.SEEDTYPE_THOUGHTSEED:

                            ThoughtSeed iSeed = helper.GetThoughtSeedByID(id);
                            dropTarget.setText(iSeed.getThought());
                            dropTarget.setContentDescription(Constants.SEEDTYPE_THOUGHTSEED+"-"+id);
                            break;

                        default:
                            break;
                    }

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
    //endregion

    //region  A D A P T E R S   ////////////////////

    public class SpinAdapter extends ArrayAdapter<Garden>{

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private List<Garden> values;

        public SpinAdapter(Context context, int textViewResourceId,
                           List<Garden> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.size();
        }

        @Override
        public Garden getItem(int position){
            return values.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = new TextView(context);
            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values.get(position).getGardenName());

            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = new TextView(context);
            label.setTextColor(Color.BLACK);
            label.setText(values.get(position).getGardenName());

            return label;
        }
    }

    public class CustomGridSeedsAdapter extends BaseAdapter {

        private android.content.Context Context;
        private List<GardenSeed> Seeds;
        LayoutInflater Inflater;

        public CustomGridSeedsAdapter(Context context, List<GardenSeed> seeds) {
            this.Context = context;
            this.Seeds = seeds;
            Inflater = (LayoutInflater) this.Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position,
                            View convertView, ViewGroup parent) {

            View grid;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                grid = Inflater.inflate( R.layout.seeds_grid_item, parent, false );
                //TextView textView = (TextView) grid.findViewById(R.id.txtGardenName);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
                //textView.setText(Seeds.get(position).getSeedType().getSeedTypeName());

                GardenSeed gSeed = Seeds.get(position);
                models.SeedType seedType = gSeed.getSeedType();

                if (seedType.getSeedTypeName().equalsIgnoreCase(Constants.SEEDTYPE_THOUGHTSEED))
                {
                    imageView.setImageResource(R.drawable.seedicon_lightbulb);
                    imageView.setContentDescription(Constants.SEEDTYPE_THOUGHTSEED+"-"+gSeed.get_ID());
                }
                else {
                    imageView.setImageResource(R.drawable.seedicon_image);
                    imageView.setContentDescription(Constants.SEEDTYPE_IMAGE+"-"+gSeed.get_ID());
                }
                imageView.setOnTouchListener(new ChoiceTouchListener(gSeed));
            }
            else {
                grid = (View) convertView;
            }
//            //exus
//            btn.setText(Gardens.get(position).getGardenName());
//            // filenames is an array of strings
//            btn.setTextColor(Color.WHITE);
//            btn.setBackgroundResource(R.drawable.gardenplus);
//            btn.setId(position);

            return grid;
        }

        @Override
        public int getCount() {
            return Seeds.size();
        }

        @Override
        public Object getItem(int position) {
            return Seeds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    //endregion

}
